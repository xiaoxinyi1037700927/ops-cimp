package com.sinosoft.ops.cimp.util;

import javax.persistence.Column;
import javax.persistence.Table;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class toSqlUtils {

    public static String Model2Sql(Class<?> model)
    {
        StringBuilder item = new StringBuilder();
        String TableName ="";
        Table tb =(Table)model.getAnnotation(Table.class);
        TableName = tb.name();
        Field[] fields = model.getDeclaredFields();//获得属性

        if(fields !=null)
        {
            for (Field field : fields) {
                PropertyDescriptor pd;
                try {
                    pd = new PropertyDescriptor(field.getName(),
                            model);
                }catch(Exception e)
                {
                    continue;
                }
                Method method = pd.getReadMethod();//获得get方法
                Annotation[] annotations=method.getAnnotations();
                for(Annotation annotation :annotations)
                {
                    if(annotation.annotationType().getName().equals(Column.class.getName()))
                    {
                        Column cm =(Column) annotation;
                        item.append(TableName+"."+cm.name() +" as " + field.getName()+",");
                    }
                }
            }
        }
        return String.format("select %s from %s",item.substring(0,item.length()-1),TableName);
    }

    public static String getModelItem(Class<?> model)
    {
        StringBuilder item = new StringBuilder();
        String TableName ="";
        Table tb =(Table)model.getAnnotation(Table.class);
        TableName = tb.name();
        Field[] fields = model.getDeclaredFields();//获得属性

        if(fields !=null)
        {
            for (Field field : fields) {
                PropertyDescriptor pd;
                try {
                    pd = new PropertyDescriptor(field.getName(),
                            model);
                }catch(Exception e)
                {
                    continue;
                }
                Method method = pd.getReadMethod();//获得get方法
                Annotation[] annotations=method.getAnnotations();
                for(Annotation annotation :annotations)
                {
                    if(annotation.annotationType().getName().equals(Column.class.getName()))
                    {
                        Column cm =(Column) annotation;
                        item.append(TableName+"."+cm.name() +" as " + field.getName()+",");
                    }
                }
            }
        }
        return item.substring(0,item.length()-1);
    }

    public static String getModelColumn(Class<?> model)
    {
        StringBuilder item = new StringBuilder();
        String TableName ="";
        Table tb =(Table)model.getAnnotation(Table.class);
        TableName = tb.name();
        Field[] fields = model.getDeclaredFields();//获得属性

        if(fields !=null)
        {
            for (Field field : fields) {
                PropertyDescriptor pd;
                try {
                    pd = new PropertyDescriptor(field.getName(),
                            model);
                }catch(Exception e)
                {
                    continue;
                }
                Method method = pd.getReadMethod();//获得get方法
                Annotation[] annotations=method.getAnnotations();
                for(Annotation annotation :annotations)
                {
                    if(annotation.annotationType().getName().equals(Column.class.getName()))
                    {
                        Column cm =(Column) annotation;
                        item.append(TableName+"."+cm.name() +",");
                    }
                }
            }
        }
        return item.substring(0,item.length()-1);
    }
}
