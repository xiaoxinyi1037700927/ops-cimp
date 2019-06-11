package com.sinosoft.ops.cimp.util.word.analyzeWordFieldService;

import com.alibaba.fastjson.JSON;
import com.sinosoft.ops.cimp.common.model.TreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Copyright © 2018 eSunny Info. Tech Ltd. All rights reserved.
 * @Package: AnalyzeWordField
 * @author: wft
 * @date: 2018年6月6日 下午3:26:05
 * @Description:
 */

public class AnalyzeWordField {


    public static void main(String[] args) throws Exception {
      //  modifyDocumentAndSave("C:/Users/Administrator/Desktop/aaa.docx");
        File file=new File("C:/Users/sunch/Desktop/sql导出/aaa.docx");
        ZipFile docxFile = new ZipFile(file);
        ZipEntry documentXML = docxFile.getEntry("word/document.xml");
        InputStream documentXMlIS= docxFile.getInputStream(documentXML);
        DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
        Document doc=dbf.newDocumentBuilder().parse(documentXMlIS);


        Collection<TreeNode> list = new LinkedList<>();

        NodeList this_book_list1 = doc.getElementsByTagName("*");
        String parentId="root";
        String proStartId="";
        for(int i=0;i<this_book_list1.getLength();i++)
        {
            Element oldBookStart=(Element)this_book_list1.item(i);

            if(oldBookStart.getNodeName()=="w:bookmarkStart")
            {
                //书签名
                String bookMarkName=oldBookStart.getAttribute("w:name");
                //书签id
                String bookMarkId=oldBookStart.getAttribute("w:id");
                //记录项目开始id
                if("_GoBack".equals(bookMarkName)){
                    continue;
                }
                if("项目开始".equals(bookMarkName)){
                    proStartId=bookMarkId;
                }
                if("项目结束".equals(bookMarkName)){
                    proStartId="root";
                }
                String startName=bookMarkName.substring(0,bookMarkName.length()-1);
                System.out.println(bookMarkName);
                if(bookMarkName.endsWith("结束")){
                    parentId=proStartId;
                    continue;
                }
                AnalyzeWordFieldTree t=new AnalyzeWordFieldTree();
                t.setId(bookMarkId);
                t.setText(bookMarkName);
                t.setParentId(parentId);
                list.add(t);
                parentId=bookMarkId==null?"":bookMarkId;
            } else
            if(oldBookStart.getNodeName()=="w:instrText")
            {
                if(oldBookStart.getTextContent().startsWith("MERGEFIELD")){
                    System.out.println(oldBookStart.getTextContent());
                    AnalyzeWordFieldTree t=new AnalyzeWordFieldTree();
                    String yutext =	oldBookStart.getTextContent().replaceAll(" ", "").replaceAll("MERGEFIELD", "")
                            .replaceAll("MERGEFORMAT", "").replace("*","").replaceAll("\\\\", "");
                    t.setId(yutext);
                    t.setText(yutext);
                    t.setLeaf(true);
                    t.setParentId(parentId);
                    list.add(t);
                }
            }
        }


        AnalyzeWordFieldTree root=new AnalyzeWordFieldTree();
        root.setId("root");
        root.setText("根书签域");
        Collection<TreeNode> rootChildren = new LinkedList<>();
        root.setChildren(rootChildren);
        for(TreeNode tree : list){
            if("root".equals(tree.getParentId())){
                rootChildren.add(tree);
            }
            Collection<TreeNode> l = new LinkedList<>();
            for(TreeNode tr : list){
                if(tree.getId().equals(tr.getParentId())){
                    l.add(tr);
                }
            }
            tree.setChildren(l);
        }

        System.out.println(JSON.toJSONString(root));



    }
/*    public static void modifyDocumentAndSave(String filePath) throws ZipException, IOException, SAXException, ParserConfigurationException{
        File file=new File(filePath);//"C:/Users/Administrator/Desktop/aaa.docx"

        ZipFile docxFile = new ZipFile(file);
        ZipEntry documentXML = docxFile.getEntry("word/document.xml");
        InputStream documentXMlIS= docxFile.getInputStream(documentXML);
        DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
        Document doc=dbf.newDocumentBuilder().parse(documentXMlIS);

        Map<String,String> bookMarkMap= new HashMap<String,String>();
        *//**
         *
         *//*
        AnalyzeWordFieldTree root=new AnalyzeWordFieldTree();
        root.setId("root");
        root.setText("根书签域");
        Collection<Treeable> rootChildren = new LinkedList<>();
        root.setChildren(rootChildren);

        NodeList docChilds = doc.getElementsByTagName("w:body").item(0).getChildNodes();//body的所有子标签
        for(int j=0;j<docChilds.getLength();j++){
            Element bodySon=(Element)docChilds.item(j);
            String bodySonName=bodySon.getTagName();
            System.out.println(bodySonName+"====---==");
            if (!"w:sectPr".equals(bodySonName)) {
                handleNode(bodySon,root);
            }
        }
        NodeList this_book_list = doc.getElementsByTagName("w:bookmarkStart");//所有书签
        if(this_book_list.getLength() != 0){
            System.out.println(this_book_list.getLength());
            for(int j=0;j<this_book_list.getLength();j++){
                //获取每个书签
                Element bookmark=(Element)this_book_list.item(j);
                //书签名
                String bookMarkName=bookmark.getAttribute("w:name");
                //书签名，跟需要替换的书签传入的map集合
                System.out.println(bookMarkName+"====---==");
            }
        }
    }*/

/*    public static void handleNode(Element bodySon,AnalyzeWordFieldTree tt){
        String bodySonName=bodySon.getTagName();//w:p  w:bookmarkStart  w:bookmarkEnd  w:sectPr
        NodeList firstNodes= bodySon.getChildNodes();
        //Collection<Treeable> children = new LinkedList<>();
        if ("w:p".equals(bodySon.getTagName())) {
            for (int i = 0; i < firstNodes.getLength(); i++) {
                Node node = firstNodes.item(i);
                if ("w:r".equals(node.getNodeName())) {
                    NodeList nli=node.getChildNodes();
                    for(int j=0;j<nli.getLength();j++){
                        if("w:instrText".equals(nli.item(j).getNodeName())&&!"".equals(nli.item(j).getTextContent().replaceAll(" ", ""))){
                            String yutext =	nli.item(j).getTextContent().replaceAll(" ", "").replaceAll("MERGEFIELD", "")
                                    .replaceAll("MERGEFORMAT", "").replace("*","").replaceAll("\\\\", "");
                            AnalyzeWordFieldTree t=new AnalyzeWordFieldTree();
                            t.setId(yutext);
                            t.setText(yutext);
                            t.setLeaf(true);
                            t.setParentId(tt.getId());
                            tt.getChildren().add(t);
                        }
                    }
                }
                else if("w:bookmarkStart".equals(node.getNodeName())){ }
            }
        }
        else if("w:bookmarkStart".equals(bodySon.getNodeName())){ }
    }*/
}
