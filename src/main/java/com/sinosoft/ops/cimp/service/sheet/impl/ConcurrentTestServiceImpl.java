/**
 * @project:     iimp-gradle
 * @title:          ConcurrentTestServiceImpl.java
 * @copyright: ©2018 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.sheet.impl;


import com.sinosoft.ops.cimp.common.model.Constants;
import com.sinosoft.ops.cimp.common.model.DataStatus;
import com.sinosoft.ops.cimp.common.model.PageableQueryParameter;
import com.sinosoft.ops.cimp.common.service.BaseServiceImpl;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoItem;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoSet;
import com.sinosoft.ops.cimp.entity.sheet.Sheet;
import com.sinosoft.ops.cimp.entity.sheet.SheetData;
import com.sinosoft.ops.cimp.framework.spring.datasource.DynamicDataSource;
import com.sinosoft.ops.cimp.framework.spring.datasource.DynamicDataSourceContextHolder;
import com.sinosoft.ops.cimp.service.infostruct.SysInfoItemService;
import com.sinosoft.ops.cimp.service.infostruct.SysInfoSetService;
import com.sinosoft.ops.cimp.service.sheet.ConcurrentTestService;
import com.sinosoft.ops.cimp.service.sheet.SheetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: ConcurrentTestServiceImpl
 * @description: 并行计算测试服务类
 * @author:        Nil
 * @date:            2018年10月21日 下午10:11:25
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Service("concurrentTestService")
public class ConcurrentTestServiceImpl  extends BaseServiceImpl implements ConcurrentTestService {
	private static final Logger logger = LoggerFactory.getLogger(ConcurrentTestServiceImpl.class);
	public static final String MQ_TOPIC_NAME = "iimp.sheet.topic.ct";
    
    @Autowired
    private ThreadPoolTaskExecutor subTaskExecutor;
    @Autowired
    private SysInfoSetService sysInfoSetService = null;
    @Autowired
    private SysInfoItemService sysInfoItemService = null;
//    @Autowired
//    private InfoMaintainService infoMaintainService = null;
    @Autowired
    private SheetService sheetService;
//	@Autowired
//    private JmsTemplate jmsTemplate;
    
    private static ConcurrentHashMap<UUID, MyReport> reportMap = new ConcurrentHashMap<UUID,MyReport>();//待计算
    private static ConcurrentLinkedQueue<MyReport> instantReports = new ConcurrentLinkedQueue<MyReport>();//即时报表（如试算）
    private static ConcurrentLinkedQueue<MyReport> noninstantReports = new ConcurrentLinkedQueue<MyReport>();//非即时报表
    private static AtomicInteger toDoSqlTaskNum=new AtomicInteger(0);//待执行SQL任务数
    private static AtomicBoolean submitting=new AtomicBoolean(false);//是否正在提交SQL
    private static AtomicBoolean reported=new AtomicBoolean(false);//是否已报告
    
    private final ThreadPoolTaskExecutor mainTaskExecutor;
    private final int maxInstantSqlTaskNum;//最大即时SQL任务数
    private final int maxNoninstantSqlTaskNum;//最大非即时SQL任务数
    private int currentInstantIndex=0;//即时索引
    private int currentNoninstantIndex=0;//非即时索引
    
    @Autowired
    public ConcurrentTestServiceImpl(ThreadPoolTaskExecutor mainTaskExecutor) {
        this.mainTaskExecutor=mainTaskExecutor;
        this.maxInstantSqlTaskNum=Math.max(256, mainTaskExecutor.getCorePoolSize()*4);
        this.maxNoninstantSqlTaskNum=this.maxInstantSqlTaskNum/32;
    }
    
    /**
     * 启动任务
     * @param reportNum 报表数
     * @param rowNum 行数
     * @param columnNum 列数
     * @author Nil
     * @date:    2018年10月21日 下午10:27:45
     * @since JDK 1.7
     */
	public void startTask(int reportNum,int rowNum,int columnNum) {
        Random r = new Random();
        int setId=14;//EMP_A001
        SysInfoSet infoSet = sysInfoSetService.getById(setId);
        infoSet.getInfoItems().addAll(sysInfoItemService.getBySetId(infoSet.getId()));
        Map<SysInfoItem,Object> infoSetData = new HashMap<SysInfoItem,Object>();
        for(int i=1;i<=reportNum;i++) {
            boolean instant=true;
            String name="测试报表"+i;
            if(r.nextInt(100)<33) {//30%为即时报表
                instant=true;
                name+="[即时]";
            }else {
                instant=false;
                name+="[非即时]";
            }
            MyReport rpt=new MyReport(name);
            rpt.instant=instant;
            for(int rowNo=1;rowNo<=rowNum;rowNo++) {
                for(int columnNo=1;columnNo<=columnNum;columnNo++) {
                    rpt.sqls.add(new MySql(rowNo,columnNo,infoSet,infoSetData));
                }
            }
            reportMap.put(rpt.id, rpt);
            if(instant) {
                instantReports.add(rpt);
            }else {
                noninstantReports.add(rpt);
            }
        }
        reported.set(false);
        mainTaskExecutor.submit(new MyCalculator(null,null));//启动计算
        String message = "已提交"+reportNum+"个报表待计算，每个报表有"+rowNum+"行、"+columnNum+"列，共"+(rowNum*columnNum)+"个SQL";
        logger.info(message);
    }
    
    class MyReport {//报表
        public UUID id=UUID.randomUUID();
        public String name;
        public boolean instant=false;//是否即时计算
        public AtomicBoolean stored=new AtomicBoolean(false);//是否已存储
        public AtomicBoolean storing=new AtomicBoolean(false);//是否正在被存储
        public long createdTimeMillis=System.currentTimeMillis();
        public ArrayList<MySql> sqls = new ArrayList<MySql>();//SQL集
        public AtomicInteger runningSqlIndex=new AtomicInteger(-1);//执行中的SQL索引
        public long storeStartTimeMillis=0;//存储开始时间
        public long storeEndTimeMillis=0;//存储结束时间
        
        public MyReport(String name) {
            this.name=name;
        }
    }
    class MySql{//SQL
        public UUID id=UUID.randomUUID();
        public int rowNo=0;//行号
        public int columnNo=0;//列号
        public int appId = 100;
        public int dataSourceId = 4;
        public int setId=14;
        public SysInfoSet infoSet = null;
        public Map<SysInfoItem,Object> infoSetData = null;
        public PageableQueryParameter queryParameter = new PageableQueryParameter();
        
        public volatile boolean calculated=false;//已计算
        public long calculateStartTimeMillis=0;//计算开始时间
        public long calculateEndTimeMillis=0;//计算结束时间
        public boolean successed=false;
        public volatile String result=null;
        public String errorMessage=null; 
        
        public MySql(int rowNo,int columnNo,SysInfoSet infoSet,Map<SysInfoItem,Object> infoSetData) {
            String[] categoryIds= {"Emp003000","Emp003001","Emp003002"};
            String[] codes = {"D00.000.006.003.005.002","D00.000.006.003.005.003",
            		"D00.000.006.003.005.004","D00.000.006.003.005.005",
            		"D00.000.006.003.005.006","D00.000.006.003.005.007"};
            String[] categoryIds1= {"Cadre01","Cadre01","Cadre03","Cadre04","Cadre05"};
            String[] codes1 = {"000.006.004.003.004.001","000.006.004.003.005.003",
            		"000.006.004.003.005.004","000.006.004.003.005.001",
            		"000.006.004.003.005.002","000.006.004.003.005.005"};
            Random r = new Random();
            this.rowNo=rowNo;
            this.columnNo=columnNo;
            this.infoSet=infoSet;
            this.infoSetData=infoSetData;
            this.queryParameter.setPageNo(1);
            this.queryParameter.setPageSize(20);
            //this.queryParameter.getParameters().put("keyword",keyword);
            if(r.nextInt(100)<33) {//33%的概率为党员条件
                this.queryParameter.getParameters().put("categoryId",categoryIds[r.nextInt(categoryIds.length)]);
                this.queryParameter.getParameters().put("code",codes[r.nextInt(codes.length)]);
            }else {
                this.appId=200;
                this.dataSourceId=2;
                this.queryParameter.getParameters().put("categoryId",categoryIds1[r.nextInt(categoryIds1.length)]);
                this.queryParameter.getParameters().put("code",codes1[r.nextInt(codes1.length)]);
            }
//            this.appId=200;
//            this.dataSourceId=2;
//            this.queryParameter.getParameters().put("categoryId","Cadre00");
//            this.queryParameter.getParameters().put("code","000");
            
            this.queryParameter.getParameters().put("containchild",true);
        }
    }
    
    class MyCalculator implements Callable<String>{//报表计算
    	private MyReport report=null;
        private MySql sql=null;
        public MyCalculator(MyReport report,MySql sql) {
        	this.report=report;
            this.sql = sql;
        }
        @Override
        public String call() throws Exception {
//            if(this.sql!=null) {
//                this.sql.calculateStartTimeMillis=System.currentTimeMillis();//计算开始时间
//                try {
//                    DynamicDataSourceContextHolder.setDataSource("slave");//使用slave数据源进行计算
//                    PageableQueryResult queryResult = infoMaintainService.findByPage(this.sql.appId,this.sql.dataSourceId,this.sql.infoSet,this.sql.infoSetData,this.sql.queryParameter);
//                    this.sql.result=String.valueOf(queryResult.getTotalCount());
//                    this.sql.successed=true;
//                }catch(Exception e) {
//                    logger.error("计算SQL失败",e);
//                    this.sql.successed=false;
//                    this.sql.errorMessage=e.getMessage();
//                }finally {
//                    this.sql.calculated=true;
//                    toDoSqlTaskNum.decrementAndGet();
//                    this.sql.calculateEndTimeMillis=System.currentTimeMillis();//计算结束时间
//                    subTaskExecutor.execute(new MyStorer(this.report));//发起存储任务
//                }
//                //sb.append(new java.text.DecimalFormat("#.00").format(((double)(System.currentTimeMillis() - startTime))/1000.0));
//            }
//
//            if(toDoSqlTaskNum.get()<Math.max(64,mainTaskExecutor.getCorePoolSize()*2)) {
//            	if(submitting.compareAndSet(false, true)) {
//	                boolean hasSql=!instantReports.isEmpty();
//	                int i=0,j=0;
//	                while(hasSql) {//优先计算即时队列里的
//	                    hasSql=false;
//	                    Iterator<MyReport> it=instantReports.iterator();
//	                    while(it.hasNext()) {
//	                        MyReport rpt=it.next();
//	                        if((j++)<currentInstantIndex) {
//	                            continue;
//	                        }else {
//	                            currentInstantIndex=j;
//	                        }
//	                        int index=rpt.runningSqlIndex.incrementAndGet();
//	                        if(index>=rpt.sqls.size()) {
//	                            it.remove();
//	                        }else {
//	                            hasSql=true;
//	                            mainTaskExecutor.submit(new MyCalculator(rpt,rpt.sqls.get(index)));
//	                            toDoSqlTaskNum.incrementAndGet();
//	                            if((++i)>=maxInstantSqlTaskNum) break;
//	                        }
//	                    }
//	                }
//	                hasSql=!noninstantReports.isEmpty();
//	                i=0;j=0;
//	                while(hasSql) {
//	                    hasSql=false;
//	                    Iterator<MyReport> it=noninstantReports.iterator();
//	                    while(it.hasNext()) {
//	                        MyReport rpt=it.next();
//	                        if((j++)<currentNoninstantIndex) {
//                                continue;
//                            }else {
//                                currentNoninstantIndex=j;
//                            }
//	                        int index=rpt.runningSqlIndex.incrementAndGet();
//	                        if(index>=rpt.sqls.size()) {
//	                            it.remove();
//	                        }else {
//	                            hasSql=true;
//	                            mainTaskExecutor.submit(new MyCalculator(rpt,rpt.sqls.get(index)));
//	                            toDoSqlTaskNum.incrementAndGet();
//	                            if((++i)>=maxNoninstantSqlTaskNum) break;
//	                        }
//	                    }
//	                }
//	                submitting.set(false);
//            	}
//            }
            
            return "";
        }
    }
    
    class MyStorer implements Runnable{//报表存储
    	private MyReport report=null;
    	public MyStorer(MyReport report) {
    		this.report=report;
    	}
        @Override
        public void run() {
        	if(this.report.storing.compareAndSet(false, true)) {
                for(MySql sql:this.report.sqls) {
                	if(!sql.calculated) {//还有SQL未计算完
                		this.report.storing.set(false);
                		return;
                	}
                }
	            Collection<SheetData> sheetDatas=new LinkedList<SheetData>();
	            Timestamp now=new Timestamp(System.currentTimeMillis());
	            Sheet sheet=new Sheet();
	            sheet.setId(this.report.id);
	            sheet.setDesignId(Constants.UUID_ZERO);
	            sheet.setStatus(DataStatus.NORMAL.getValue());
	            sheet.setCreatedTime(now);
	            sheet.setLastModifiedTime(now);
	            sheet.setName(this.report.name);
	            for(MySql sql:this.report.sqls) {
	                SheetData e=new SheetData();
	                e.setId(sql.id);
	                e.setBeingEdited(false);
	                e.setColumnNo(sql.columnNo);
	                e.setCreatedTime(now);
	                e.setLastModifiedTime(now);
	                e.setRowNo(sql.rowNo);
	                e.setSheetId(this.report.id);
	                e.setStatus(DataStatus.NORMAL.getValue());
	                e.setStringValue(sql.result);
	                sheetDatas.add(e);
	            }
	            try {
	                //使用主数据源存储数据
	                DynamicDataSourceContextHolder.setDataSource(DynamicDataSource.DEFAULT_DATASOURCE);
	            	this.report.storeStartTimeMillis=System.currentTimeMillis();
	                sheetService.create( sheet, sheetDatas );
	                this.report.storeEndTimeMillis=System.currentTimeMillis();
	                this.report.stored.set(true);
	                logger.info(this.report.name+" 保存成功");
	            }catch(Exception e) {
	            	this.report.storing.set(false);
	                logger.error("保存数据失败",e);
	            }
        	}
            
            //如果报表都存储完成输出统计信息
            for (Map.Entry<UUID, MyReport> entry : reportMap.entrySet()) {
                MyReport rpt=entry.getValue();
                if(!rpt.stored.get()) {
                	return;
                }
            }
            //输出报告
            if(reported.compareAndSet(false, true)) {
	            java.text.DecimalFormat df = new java.text.DecimalFormat("###,###,###,##0.000000");
	            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	            StringBuilder sb=new StringBuilder();
	            long reportElapsedTime=0,minReportElapsedTime=Long.MAX_VALUE,maxReportElapsedTime=0,totalReportElapsedTime=0;
	            long minSqlElapsedTime=Long.MAX_VALUE,maxSqlElapsedTime=0,maxCalculateEndTimeMillis=0;
	            long reportStoreTime=0,minReportStoreTime=Long.MAX_VALUE,maxReportStoreTime=0,totalReportStoreTime=0;
	            for (Map.Entry<UUID, MyReport> entry : reportMap.entrySet()) {
	                MyReport rpt=entry.getValue();
	                reportElapsedTime=0;
	                maxCalculateEndTimeMillis=0;
	                sb.setLength(0);
	                for(MySql sql:rpt.sqls) {
	                    long sqlElapsedTime=sql.calculateEndTimeMillis - sql.calculateStartTimeMillis;
	                    reportElapsedTime+=sqlElapsedTime;
	                    maxSqlElapsedTime=Math.max(maxSqlElapsedTime, sqlElapsedTime);
	                    minSqlElapsedTime=Math.min(minSqlElapsedTime, sqlElapsedTime);
	                    maxCalculateEndTimeMillis=Math.max(maxCalculateEndTimeMillis, sql.calculateEndTimeMillis);
	                }
	                totalReportElapsedTime+=reportElapsedTime;
	                maxReportElapsedTime=Math.max(maxReportElapsedTime, reportElapsedTime);
	                minReportElapsedTime=Math.min(minReportElapsedTime, reportElapsedTime);
	                reportStoreTime = rpt.storeEndTimeMillis - rpt.storeStartTimeMillis;
	                minReportStoreTime=Math.min(minReportStoreTime,reportStoreTime);
	                maxReportStoreTime=Math.max(maxReportStoreTime,reportStoreTime);
	                totalReportStoreTime+=reportStoreTime;
	                
	                sb.append(rpt.name);
	                sb.append(sdf.format(new Date(rpt.createdTimeMillis)));
	                sb.append(" -> ");
	                sb.append(sdf.format(new Date(maxCalculateEndTimeMillis)));
	                sb.append(" 计算用时：");
	                sb.append(df.format(((double)reportElapsedTime)/1000.0)+"秒");
	                sb.append(" 存储用时：");
	                sb.append(df.format(((double)reportStoreTime)/1000.0)+"秒");
	                logger.info(sb.toString());
	            }
	            logger.info("SQL最小计算用时："+df.format(((double)minSqlElapsedTime)/1000.0)+"秒，最大计算用时："+df.format(((double)maxSqlElapsedTime)/1000.0)+"秒");
	            logger.info("报表最小计算用时："+df.format(((double)minReportElapsedTime)/1000.0)+"秒，最大计算用时："+df.format(((double)maxReportElapsedTime)/1000.0)+"秒");
	            logger.info("报表总计算用时："+df.format(((double)totalReportElapsedTime)/1000.0)+"秒");
	            logger.info("报表最小存储用时："+df.format(((double)minReportStoreTime)/1000.0)+"秒，最大存储用时："+df.format(((double)maxReportStoreTime)/1000.0)+"秒");
	            logger.info("报表总存储用时："+df.format(((double)totalReportStoreTime)/1000.0)+"秒");
	            reportMap.clear();
            }
        }
    }
    
	@Override
	public void startConcurrentTask(int reportNum, int rowNum, int columnNum) {
//		jmsTemplate.send(new ActiveMQTopic(MQ_TOPIC_NAME), new MessageCreator() {
//            @Override
//            public Message createMessage(Session session) throws JMSException {
//                MapMessage message = session.createMapMessage();
//                message.setInt("reportNum", reportNum);
//                message.setInt("rowNum", rowNum);
//                message.setInt("columnNum", columnNum);
//                return message;
//            }
//        });
	}

	@Override
//    @JmsListener(destination=MQ_TOPIC_NAME, containerFactory="jmsTopicListenerContainerFactory")
    public void onTopicMessage(MapMessage message) throws JMSException {
//    	startTask(message.getInt("reportNum"),message.getInt("rowNum"),message.getInt("columnNum"));
    }
    
    @Override
    public void startNonconcurrentTask(int reportNum, int rowNum, int columnNum) {
        startTask(reportNum, rowNum, columnNum);
    }
}
