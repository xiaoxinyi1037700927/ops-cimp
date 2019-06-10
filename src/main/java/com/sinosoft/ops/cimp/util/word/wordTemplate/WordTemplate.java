package com.sinosoft.ops.cimp.util.word.wordTemplate;

import com.sinosoft.ops.cimp.util.word.analyzeWordFieldService.AnalyzeWordFieldTree;
import com.sinosoft.ops.cimp.common.model.TreeNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;



/**
 * 
 * @ClassName:  WordTemplate
 * @description: 基于xml方式的word模板解析类
 * @author:        sunch
 * @date:            2018年6月25日 下午4:29:28
 * @version        1.0.0
 * @since            JDK 1.7
 */
public class WordTemplate {
	
	private static final Logger logger = LoggerFactory.getLogger(WordTemplate.class);

	
	private static final String WORD_XML = "word/document.xml";
	
	
	private byte[] content;
	
	private ZipFile zipFile;
	
	private Document document;
	
	private String tempOutPath;

	private ZipOutputStream zipout;
	
	
	
	
	public WordTemplate(String tempOutPath,ZipOutputStream zipout) throws Exception{
		this.tempOutPath = tempOutPath;
		this.zipout = zipout;
		initDocument();
	}
	
	public WordTemplate(byte[] content,  String tempOutPath) throws Exception {
		this.content= content;
		this.tempOutPath = tempOutPath;
		buildDocument();
	}
	
	
	public WordTemplate(byte[] content,  String tempOutPath, ZipOutputStream zipout) throws Exception {
		this.content= content;
		this.tempOutPath = tempOutPath;
		this.zipout = zipout;
		buildDocument();
	}

	private void buildDocument() throws Exception {
		byte2Word(content, tempOutPath);
		initDocument();
	}
	
	
	
	public void buildDocument(byte[] content,String tempOutPath,ZipOutputStream zipout) throws Exception {
		this.tempOutPath = tempOutPath;
		this.zipout = zipout;
		byte2Word(content, tempOutPath);
		initDocument();
	}
	
	public void initDocument() throws Exception{
		zipFile = new ZipFile(tempOutPath);
		ZipEntry entry = zipFile.getEntry(WORD_XML);
		InputStream stream = zipFile.getInputStream(entry);
		document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
	}
	
	public void closeDocument() throws Exception{
		zipFile.close();
	}

	public void byte2Word(byte[] content,OutputStream out) throws IOException{
		out.write(content);
	}
	
	public void byte2Word(byte[] content,String tempOutPath) throws Exception{
		this.tempOutPath = tempOutPath;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(tempOutPath);
			byte2Word(content, fos);
		} catch (Exception e) {
			logger.error("字节转word失败",e);
			throw new Exception("字节数组转word失败", e);
		}finally {
			if(fos != null)
				fos.close();
		}
	}

	
	
	
	
	
	public  Collection<TreeNode> analyzeWord2Tree(byte[] content,Object rootId) throws Exception{
		
		//获取所有节点
        NodeList this_book_list1 = document.getElementsByTagName("*");
        Stack<TreeNode> stack = new Stack<>();
        int o = 1;
        for(int i=0;i<this_book_list1.getLength();i++){
        	Element oldBookStart=(Element)this_book_list1.item(i);
        	//如果该节点为开始书签，压入 栈
        	if("w:bookmarkStart".equals(oldBookStart.getNodeName())){
        		String bookMarkName = oldBookStart.getAttribute("w:name");
        		if(StringUtils.isNotBlank(bookMarkName) && bookMarkName.length()>=2 && 
        				"开始".equals(bookMarkName.substring(bookMarkName.length()-2,bookMarkName.length()))){
        			AnalyzeWordFieldTree treeNode = new AnalyzeWordFieldTree();
        			treeNode.setId(UUID.randomUUID());
        			treeNode.setText(bookMarkName);
        			treeNode.setFieldType("w:bookmarkStart");
        			treeNode.setOrdinal(o);
        			stack.push(treeNode);
        			o++;
        		//如果为结束书签，弹栈直到遇到开始书签，并把弹出的元素添加到开始书签子集
        		}else if(StringUtils.isNotBlank(bookMarkName) && bookMarkName.length()>=2 && 
        				"结束".equals(bookMarkName.substring(bookMarkName.length()-2,bookMarkName.length()))){
        			AnalyzeWordFieldTree bookmark = null;
        			Stack<TreeNode> childs = new Stack<>();
        			Collection<TreeNode> childsList = new ArrayList<>();
        			while(!stack.isEmpty()){
        				//开始弹栈
        				AnalyzeWordFieldTree pop = (AnalyzeWordFieldTree) stack.pop();
        				//遇到书签停止弹栈
        				if("w:bookmarkStart".equals(pop.getFieldType())){
        					bookmark = pop;
        					break;
        				}else{
        					//弹出的元素赋值到 children
        					childs.push(pop);
        				}
        			}
        			//重新排序
        			while(!childs.empty()){
        				AnalyzeWordFieldTree treeable = (AnalyzeWordFieldTree) childs.pop();
        				treeable.setParentId(bookmark.getId());
        				childsList.add(treeable);
        			}
        			
        			bookmark.setChildren(childsList);
        			bookmark.setFieldType("");
        			stack.push(bookmark);
        		}
        	//如果为值域 直接压入栈
        	}else if("w:instrText".equals(oldBookStart.getNodeName())){
        		if(oldBookStart.getTextContent().indexOf("MERGEFIELD")>=0){
        			AnalyzeWordFieldTree treeNode=new AnalyzeWordFieldTree();
                    String yutext =	oldBookStart.getTextContent().replaceAll(" ", "").replaceAll("MERGEFIELD", "")
                            .replaceAll("MERGEFORMAT", "").replace("*","").replaceAll("\\\\", "");
                    treeNode.setId(UUID.randomUUID());
                    treeNode.setText(yutext);
                    treeNode.setFieldType("w:instrText");
                    treeNode.setOrdinal(o);
                    stack.push(treeNode);
                    o++;
        		}
        	}
        }
        int no = 1;
        for (TreeNode tree : stack) {
        	AnalyzeWordFieldTree analyzeWordFieldTree = (AnalyzeWordFieldTree)tree;
        	analyzeWordFieldTree.setParentId(rootId);
        	analyzeWordFieldTree.setNo(no+"");
        	if(!CollectionUtils.isEmpty(analyzeWordFieldTree.getChildren())){
        		createOrdinal(analyzeWordFieldTree.getChildren(),analyzeWordFieldTree.getNo());
        	}
        	no++;
		}
        closeDocument();
		return stack;
	}
	
	private static void createOrdinal(Collection<TreeNode> children, String no) {
		int i = 1;
		for (TreeNode treeable : children) {
			AnalyzeWordFieldTree tree = (AnalyzeWordFieldTree)treeable;
			tree.setNo(no+"."+i);
			if(!CollectionUtils.isEmpty(tree.getChildren())){
				createOrdinal(tree.getChildren(),tree.getNo());
			}
			i++;
		}
		
	}
	
	public void fillData(Collection<Map<String,Object>> collection) throws Exception{
		 //获取所有节点
        NodeList this_book_list1 = document.getElementsByTagName("w:t");
        if(!CollectionUtils.isEmpty(collection)){
        	for(int i=0;i<this_book_list1.getLength();i++){
            	Element oldBookStart=(Element)this_book_list1.item(i);
            	for (Map<String, Object> data : collection) {
    				if(StringUtils.isNoneBlank((String)data.get("name")) && 
    						(data.get("name").equals(oldBookStart.getTextContent())||
    								("«"+data.get("name")+"»").equals(oldBookStart.getTextContent()))){
    					oldBookStart.setTextContent((String)data.get("bindValue"));
    					collection.remove(data);
    					removeMerge(oldBookStart);
    					break;
    				}
    			}
            }

        }
        writeWord();
        closeDocument();
	}
	
	/**
	 * 
	 * 对模板赋值后输出word文档
	 * @throws Exception
	 * @author sunch
	 * @date:    2018年6月25日 下午7:01:58
	 * @since JDK 1.7
	 */
	private void writeWord() throws Exception{
	 	Transformer t = TransformerFactory. newInstance ().newTransformer();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        t.transform(new DOMSource(document), new
        		StreamResult(baos));
        //获取压缩文件所有预算
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while(entries.hasMoreElements()){
        	ZipEntry entry = entries.nextElement();
        	if(WORD_XML.equals(entry.getName())){
        		//如果遇到 document.xml 元素 把内存中的 document文件写入到 压缩文件
        		byte[] data = baos.toByteArray(); 
        		zipout.putNextEntry(new ZipEntry(entry.getName()));
        		zipout.write(data, 0,data.length);
        	}else{
        		//反之 直接写入
        		InputStream incoming = zipFile.getInputStream(entry);
        		byte[] data = new byte[1024*512];
        		int readCount = incoming.read(data, 0, (int)entry.getSize());
        		zipout.putNextEntry(new ZipEntry(entry.getName()));;
        		zipout.write(data, 0, readCount);
        	}
        }
	}
	
	/**
	 * 
	 * 删除节点的域
	 * @param node
	 * @author sunch
	 * @date:    2018年6月25日 下午6:58:57
	 * @since JDK 1.7
	 */
	private void removeMerge(Node node){
		Node root = node.getParentNode().getParentNode();
		Node wr = node.getParentNode();
		Node previousSibling = wr;	//前一个节点
		Node nextSibling = wr;		//后一个节点
		List<Node> nodes= new ArrayList<Node>();	//待删除的节点集合
		while(true){	//删除 node 节点前的所有节点 知道遇到 域  开始标签
			boolean flag = false;
			previousSibling = previousSibling.getPreviousSibling();
			nodes.add(previousSibling);
			Element e = (Element)previousSibling;
			NodeList childNodes = e.getChildNodes();
			for (int j = 0; j < childNodes.getLength(); j++) {
				if("w:fldChar".equals(childNodes.item(j).getNodeName())&&
						"begin".equals(((Element)childNodes.item(j)).getAttribute("w:fldCharType"))){
					flag=true;
					break;
				}
			}
			if(flag)break;
		}
		while(true){
			boolean flag = false;
			nextSibling = nextSibling.getNextSibling();
			nodes.add(nextSibling);
			Element e = (Element)nextSibling;
			NodeList childNodes = e.getChildNodes();
			for (int j = 0; j < childNodes.getLength(); j++) {
				if("w:fldChar".equals(childNodes.item(j).getNodeName())&&
						"end".equals(((Element)childNodes.item(j)).getAttribute("w:fldCharType"))){
					flag=true;
					break;
				}
			}
			if(flag) break;
				
		}
		for (Node node1 : nodes) {
			root.removeChild(node1);
		}
	}
	
	public  void fillDataForEach() throws  Exception{
		
		NodeList nodeList = document.getElementsByTagName("w:t");
		Map<Node, Node> nodeMap = new HashMap<Node,Node>();
		for(int i=0;i<nodeList.getLength();i++){
			Element element = (Element) nodeList.item(i);
			System.out.println(element.getTextContent());
			if("«${fmRel_0}»".equals(element.getTextContent())){
				Node root = element.getParentNode().getParentNode().getParentNode().getParentNode().getParentNode();
				Node tr = element.getParentNode().getParentNode().getParentNode().getParentNode();
				nodeMap.put(root, tr);
			}
		}
		
		Set<Node> set = nodeMap.keySet();
		for (Node root : set) {
			Node tr = nodeMap.get(root);
			for (int i = 0; i < 50; i++) {
				Node cloneNode = tr.cloneNode(true);
				root.insertBefore(cloneNode, tr);
			}
		}
		
		writeWord();
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public ZipFile getZipFile() {
		return zipFile;
	}

	public void setZipFile(ZipFile zipFile) {
		this.zipFile = zipFile;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public String getTempOutPath() {
		return tempOutPath;
	}

	public void setTempOutPath(String tempOutPath) {
		this.tempOutPath = tempOutPath;
	}

	public ZipOutputStream getZipout() {
		return zipout;
	}

	public void setZipout(ZipOutputStream zipout) {
		this.zipout = zipout;
	}

	
	
	
	
}
