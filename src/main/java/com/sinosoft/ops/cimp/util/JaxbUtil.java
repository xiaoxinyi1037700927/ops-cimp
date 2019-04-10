package com.sinosoft.ops.cimp.util;

import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.exception.SystemException;
import com.vip.vjtools.vjkit.text.Charsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

public final class JaxbUtil {

    private JaxbUtil() {
    }

    /**
     * 编组，从类转换为xml字符串
     *
     * @param bean javaBean对象
     * @return javaBean对应的xml报文结构
     */
    public static String marshal(Object bean) {
        Writer writer = null;
        String packageName = bean.getClass().getPackage().getName();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(packageName);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
            writer = new StringWriter();
            marshaller.marshal(bean, writer);
        } catch (JAXBException e) {
            throw new SystemException(OpsErrorMessage.MODULE_NAME, e, OpsErrorMessage.ERROR_MESSAGE_100201, "JAXB");
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException ignored) {
            }
        }
        return writer.toString();
    }

    /**
     * 从xml字符串解码为类实例
     *
     * @param <T>      javaBean对象类型
     * @param docClass javaBean对象类
     * @param xml      转换XML报文
     * @return javaBean对象类型对应的实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T unmarshal(Class<T> docClass, String xml) {
        T t;
        try {
            String packageName = docClass.getPackage().getName();
            JAXBContext jaxbContext = JAXBContext.newInstance(packageName);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            InputStream is = stringToInputStream(xml);
            t = (T) unmarshaller.unmarshal(is);
        } catch (JAXBException e) {
            throw new SystemException(OpsErrorMessage.MODULE_NAME, e, OpsErrorMessage.ERROR_MESSAGE_100201, "JAXB");
        }
        return t;
    }

    /**
     * 从xml字符串解码为类实例
     *
     * @param <T>         javaBean对象类型
     * @param docClass    javaBean对象类
     * @param inputStream 转换XML报文
     * @return javaBean对象类型对应的实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T unmarshal(Class<T> docClass, InputStream inputStream) {
        T t;
        try {
            String packageName = docClass.getPackage().getName();
            JAXBContext jc = JAXBContext.newInstance(packageName);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            t = (T) unmarshaller.unmarshal(inputStream);
        } catch (JAXBException e) {
            throw new SystemException(OpsErrorMessage.MODULE_NAME, e, OpsErrorMessage.ERROR_MESSAGE_100201, "JAXB");
        }
        return t;
    }

    /**
     * xml报文准换为输入流
     *
     * @param str xml报文
     * @return 输入流
     */
    private static InputStream stringToInputStream(String str) {
        ByteArrayInputStream stream;
        stream = new ByteArrayInputStream(str.getBytes(Charsets.UTF_8));
        return stream;
    }
}
