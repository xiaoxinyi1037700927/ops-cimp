/**
 * @Project:      IIMP
 * @Title:           SysInfoSetDaoImpl.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.infostruct.impl;


import com.sinosoft.ops.cimp.common.dao.BaseEntityDaoImpl;
import com.sinosoft.ops.cimp.common.model.DataStatus;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoItem;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoSet;
import com.sinosoft.ops.cimp.repository.infostruct.SysInfoSetDao;
import com.sinosoft.ops.cimp.util.NameUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.beans.PropertyDescriptor;
import java.sql.*;
import java.util.*;

/**
 * @ClassName: SysInfoSetDaoImpl
 * @Description: TODO请描述一下这个类
 * @Author: wft
 * @Date: 2017年8月18日 下午4:40:53
 * @Version 1.0.0
 */
@Repository("sysInfoSetDao")
public class SysInfoSetDaoImpl extends BaseEntityDaoImpl<SysInfoSet> implements SysInfoSetDao {
    private static final Logger logger = LoggerFactory.getLogger(SysInfoSetDaoImpl.class);
    private final String userName = "EPLATFORM";
    @Resource(name = "dataSource")
    private DataSource dataSource = null;

    public SysInfoSetDaoImpl(EntityManagerFactory factory) {
        super(factory);
    }

    private static final Map<Integer,String> sqlTypes = new HashMap<Integer,String>(){
        private static final long serialVersionUID = 6717356211847638220L;
        {
            put(java.sql.Types.BIT, "BIT");
            put(java.sql.Types.TINYINT, "TINYINT");
            put(java.sql.Types.SMALLINT, "SMALLINT");
            put(java.sql.Types.INTEGER, "INTEGER");
            put(java.sql.Types.BIGINT, "BIGINT");
            put(java.sql.Types.FLOAT, "FLOAT");
            put(java.sql.Types.REAL, "REAL");
            put(java.sql.Types.DOUBLE, "DOUBLE");
            put(java.sql.Types.NUMERIC, "NUMERIC");
            put(java.sql.Types.DECIMAL, "DECIMAL");
            put(java.sql.Types.CHAR, "CHAR");
            put(java.sql.Types.VARCHAR, "VARCHAR");
            put(java.sql.Types.LONGVARCHAR, "LONGVARCHAR");
            put(java.sql.Types.DATE, "DATE");
            put(java.sql.Types.TIME, "TIME");
            put(java.sql.Types.TIMESTAMP, "TIMESTAMP");
            put(java.sql.Types.BINARY, "BINARY");
            put(java.sql.Types.VARBINARY, "VARBINARY");
            put(java.sql.Types.LONGVARBINARY, "LONGVARBINARY");
            put(java.sql.Types.NULL, "NULL");
            put(java.sql.Types.OTHER, "OTHER");
            put(java.sql.Types.BLOB, "BLOB");
            put(java.sql.Types.CLOB, "CLOB");
            put(java.sql.Types.BOOLEAN, "BOOLEAN");
            put(java.sql.Types.ROWID, "ROWID");
            put(java.sql.Types.NCHAR, "NCHAR");
            put(java.sql.Types.NVARCHAR, "NVARCHAR");
            put(java.sql.Types.LONGNVARCHAR, "LONGNVARCHAR");
            put(java.sql.Types.NCLOB, "NCLOB");
            put(java.sql.Types.SQLXML, "SQLXML");
        }
    };

    @Override
    public Collection<SysInfoSet> getAllFromDBTables(int maxSetId, int maxItemId) {
        final List<SysInfoSet> allTableList = new LinkedList<SysInfoSet>();
        Connection connection = null;
        try {
            String url = "";
            String username = "";
            String password = "";
            String driverClassName = "";
            PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(dataSource.getClass());
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                if (propertyDescriptor.getReadMethod() != null && "url".equals(propertyDescriptor.getName())) {
                    url = (String) propertyDescriptor.getReadMethod().invoke(dataSource);
                } else if (propertyDescriptor.getReadMethod() != null
                        && "username".equals(propertyDescriptor.getName())) {
                    username = (String) propertyDescriptor.getReadMethod().invoke(dataSource);
                } else if (propertyDescriptor.getReadMethod() != null
                        && "password".equals(propertyDescriptor.getName())) {
                    password = (String) propertyDescriptor.getReadMethod().invoke(dataSource);
                } else if (propertyDescriptor.getReadMethod() != null
                        && "driverClassName".equals(propertyDescriptor.getName())) {
                    driverClassName = (String) propertyDescriptor.getReadMethod().invoke(dataSource);
                }
            }
            Class.forName(driverClassName);
            Properties prop = new Properties();
            prop.setProperty("user", username);
            prop.setProperty("password", password);
            prop.setProperty("remarks", "true");
            connection = DriverManager.getConnection(url, prop);
            DatabaseMetaData metaData = connection.getMetaData();
            allTableList.addAll(getAllTableList(connection, metaData, userName, maxSetId, maxItemId));
        } catch (Exception e) {
            logger.error("JDBC获取连接及DatabaseMetaData信息发生异常", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("DatabaseMetaData信息时关闭连接发生异常", e);
                }
            }
        }

        return allTableList;
    }

    /**
     * 获得该用户下面的所有表
     */    
    public List<SysInfoSet> getAllTableList(Connection connection, DatabaseMetaData metaData, String schemaName,
            int maxSetId, int maxItemId) {
        List<SysInfoSet> tableList = new ArrayList<SysInfoSet>();
        try {
            String[] types = { "TABLE" };
            ResultSet rs = metaData.getTables(null, schemaName, null, types);
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME"); // 表名 参数也可以是3
                if (tableName.indexOf("$")>=0) continue;
                SysInfoSet set = new SysInfoSet();
                set.setId(++maxSetId);
                set.setName(NameUtil.toCamelCase(tableName));
                set.setTableName(tableName);
                set.setDescription(rs.getString("REMARKS"));//备注
                set.setCreatedTime(new Timestamp(System.currentTimeMillis()));
                set.setInvalid(false);
                set.setStatus(DataStatus.NORMAL.getValue());
                set.setOrdinal(set.getId());
                set.setType((byte)0);
                set.setGroupId(-1);
                set.setGroupMain(false);
                set.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
                //获取表的字段
                maxItemId = getInfoItemsFromDBMeta(set, metaData, set.getInfoItems(), maxItemId);
                tableList.add(set);
            }
        } catch (SQLException e) {
            logger.error("获取EPLATFORM该用户下面的所有表失败", e);
        }
        return tableList;
    }

    /**
     * 通过数据库元信息获取信息集的所有信息项
     * 
     * @param infoSet
     *            信息集
     * @param metaData
     *            数据库元数据
     * @param items
     *            信息项集合
     * @param maxItemId
     *            最大信息项目标识
     * @return 新的最大信息项目标识
     */
    public int getInfoItemsFromDBMeta(SysInfoSet infoSet, DatabaseMetaData metaData, Collection<SysInfoItem> items,
            int maxItemId) {
        ResultSet rs = null;
        try {
            int ordinal = 1;
            Set<String> primaryKeys = new HashSet<String>();
            rs = metaData.getPrimaryKeys(null, userName, infoSet.getName());
            while (rs.next()) {
                primaryKeys.add(rs.getString("COLUMN_NAME"));
            }
            rs.close();
            rs = metaData.getColumns(null, userName, infoSet.getName(), "%");
            while (rs.next()) {
                SysInfoItem item = new SysInfoItem();
                String columnName = rs.getString("COLUMN_NAME");// 列名
                int nullable = rs.getInt("NULLABLE");// 是否允许为null
                item.setId(++maxItemId);
                item.setInfoSetId(infoSet.getId());
                item.setName(NameUtil.toCamelCase(columnName));
                item.setColumnName(columnName);
                item.setType((byte)0);
                item.setDescription(rs.getString("REMARKS"));// 列描述
                item.setDbType(rs.getString("TYPE_NAME"));// 字段类型 number varchar
                item.setJdbcType(sqlTypes.get(rs.getInt("DATA_TYPE")));//SQL类型
                if("OTHER".equals(item.getJdbcType())&&"NVARCHAR2".equals(item.getDbType())){
                    item.setJdbcType(sqlTypes.get(java.sql.Types.NVARCHAR));
                }
                item.setLength(rs.getInt("COLUMN_SIZE"));//字段长度
                item.setPrecision(rs.getInt("DECIMAL_DIGITS"));//小数位数
                item.setNullable(nullable == 1);
                item.setPrimaryKey(primaryKeys.contains(columnName));
                item.setInvalid(false);
                item.setInputType("text");
                item.setOrdinal(ordinal++);
                item.setCreatedTime(new Timestamp(System.currentTimeMillis()));
                item.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
                item.setStatus(DataStatus.NORMAL.getValue());
                items.add(item);
            }
        } catch (SQLException e) {
            logger.error("获取item时发生异常", e);
            return maxItemId;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
        }
        return maxItemId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<SysInfoSet> getAllOrderByNameCn() {
        return sessionFactory.getCurrentSession().createQuery("from SysInfoSet where status=0 order by nameCn").list();
    }

    @Override
    public int getMaxId() {
        Object obj = sessionFactory.getCurrentSession().createQuery("select MAX(id) from SysInfoSet").uniqueResult();
        return obj == null ? 0 : (int) obj;
    }

    @Override
    public SysInfoSet getByName(String name) {
        return (SysInfoSet) sessionFactory.getCurrentSession().createQuery("from SysInfoSet where status=0 and name =:name")
                .setParameter("name", name)
                .uniqueResult();
    }
    
    @Override
    public SysInfoSet getByTableName(String tableName) {
        return (SysInfoSet) sessionFactory.getCurrentSession().createQuery("from SysInfoSet where status=0 and tableName =:tableName")
                .setParameter("tableName", tableName)
                .uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<SysInfoSet> getByType(byte type) {
        return sessionFactory.getCurrentSession().createQuery("from SysInfoSet where status=0 and type=:type order by ordinal")
                .setParameter("type",type)
                .list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<SysInfoSet> getByGroupId(Integer groupId) {
        return sessionFactory.getCurrentSession().createQuery("from SysInfoSet where status=0 and groupId=:groupId and groupMain=0 order by ordinal")
                .setParameter("groupId",groupId)
                .list();
    }

    @Override
    public SysInfoSet getGroupMainSet(Integer groupId) {
        return (SysInfoSet) sessionFactory.getCurrentSession().createQuery("from SysInfoSet where status=0 and groupId=:groupId and groupMain=1 order by ordinal")
                .setParameter("groupId",groupId)
                .uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<SysInfoSet> getGroupSubSet(Integer groupId) {
        return sessionFactory.getCurrentSession().createQuery("from SysInfoSet where status=0 and groupId=:groupId and groupMain=0 order by ordinal")
                .setParameter("groupId",groupId)
                .list();
    }

	@Override
	public SysInfoItem findInfoItemby(Integer id) {
		StringBuilder sb = new StringBuilder("from SysInfoItem where status =0 and infoSetId=:id and primaryKey=1"); 
	       // sb.append(" order by i.ordinal");
		SysInfoItem infost= (SysInfoItem) sessionFactory.getCurrentSession().createQuery(sb.toString())
	   .setParameter("id",id ).uniqueResult();
		return infost;
	}

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Integer> getAllIds() {
        return sessionFactory.getCurrentSession()
                .createQuery("select id from SysInfoSet where status=0 order by ordinal")
                .list();
    }
    @SuppressWarnings("unchecked")
    @Override
    public Collection<SysInfoSet> getAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from SysInfoSet where status=0 order by ordinal")
                .list();
    }
    
    @Override
    public Integer getIdByName(String name) {
        return (Integer) sessionFactory.getCurrentSession()
                .createQuery("select id from SysInfoSet where status=0 and name=:name")
                .setParameter("name", name)
                .uniqueResult();
    }
    @Override
    public Integer getIdByTableName(String tableName) {
        return (Integer) sessionFactory.getCurrentSession()
                .createQuery("select id from SysInfoSet where status=0 and tableName=:tableName")
                .setParameter("tableName", tableName)
                .uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Integer> getIdsByGroupId(Integer groupId) {
        return sessionFactory.getCurrentSession()
                .createQuery("select id from SysInfoSet where status=0 and groupId=:groupId")
                .setParameter("groupId", groupId)
                .list();
    }
}
