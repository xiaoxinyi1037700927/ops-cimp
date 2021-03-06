package com.sinosoft.ops.cimp.util.word.pattern.processor;

import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.Shape;
import com.aspose.words.WrapType;

import java.util.Map;

/**
 * Created by SML
 * date : 2017/11/8
 * des :
 */
public class PhotoAttrValueProcessor extends AbstractAttrValueProcessor implements AttrValueProcessor {
    @Override
    public void processAttr(Document document, Map<String, Object> attrAndValue) throws Exception {
        String attrName = attrAndValue.keySet().toArray(new String[]{})[0];
        Object[] array = attrAndValue.values().toArray();
        if(array.length == 0 || array[0] == null){
        	return;
        }
        byte[] attrValue = (byte[])array [0];
        if (attrValue.length == 0) {
            return;
        }

        DocumentBuilder db = new DocumentBuilder(document);
        Shape shape = db.insertImage(attrValue, 83, 115);
        shape.setWrapType(WrapType.SQUARE);
        db.moveToMergeField(attrName);
        db.insertNode(shape);
    }
}
