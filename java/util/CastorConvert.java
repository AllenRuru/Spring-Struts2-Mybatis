package com.samsunganycar.util;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CastorConvert {

    /**
     * custor编码格式
     */
    private static String CUSTOR_CODE_FORMAT = "gbk";

    /**
     * 缓存的文件Map数据
     */
    private static Map<String,File> fileMap = new HashMap<String,File>();

    /**
     * 对象转换为报文
     * @param cardMapping
     * @param fileName
     * @return
     * @throws Exception
     */
    public String boToXml(Object xmlMapping,String fileName) {
        try {
            if (fileName==null){
                throw new IllegalArgumentException("xml映射文件名为空");
            }
            Mapping mapping = new Mapping();
            mapping.loadMapping(this.getFilePath(fileName));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            Writer writer = new OutputStreamWriter(byteArrayOutputStream, CUSTOR_CODE_FORMAT);
            Marshaller marshaller = new Marshaller(writer);
            marshaller.setMapping(mapping);
            marshaller.marshal(xmlMapping);
            return byteArrayOutputStream.toString(CUSTOR_CODE_FORMAT);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象转换为报文
     * @param cardMapping
     * @param fileName
     * @return
     * @throws Exception
     */
    public String boToXml(Object xmlMapping,URL url){
        try {
            Mapping mapping = null;
            if(url!=null){
                mapping =  new Mapping();
                mapping.loadMapping(url);
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            Writer writer = new OutputStreamWriter(byteArrayOutputStream, CUSTOR_CODE_FORMAT);
            Marshaller marshaller = new Marshaller(writer);
            if(mapping!=null)
                marshaller.setMapping(mapping);
            marshaller.marshal(xmlMapping);
            return byteArrayOutputStream.toString(CUSTOR_CODE_FORMAT);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象转换为报文，采用默认转换方式
     * @param xmlMapping
     * @return
     */
    public String boToXml(Object xmlMapping){
        URL url = null;
        return boToXml(xmlMapping,url);
    }

    /**
     * 将返回报文转换为业务对象
     * @param responseXml
     * @param fileName
     * @throws Exception
     */
    public Object xmlToBO(String responseXml,String fileName){
        try {
            if (fileName==null){
                throw new IllegalArgumentException("xml映射文件名为空");
            }
            Mapping map = new Mapping();
            map.loadMapping(this.getFilePath(fileName));
            Reader reader = new StringReader(responseXml);
            Unmarshaller unmarshaller = new Unmarshaller(map);
            return unmarshaller.unmarshal(reader);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 将返回报文转换为业务对象
     * @param responseXml
     * @param fileName
     * @throws Exception
     */
    public Object xmlToBO(String responseXml,URL url){
        try {
            Mapping map = new Mapping();
            map.loadMapping(url);
            Reader reader = new StringReader(responseXml);
            Unmarshaller unmarshaller = new Unmarshaller(map);
            return unmarshaller.unmarshal(reader);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定文件名路径
     * @param fileName
     * @return
     * @throws MalformedURLException
     */
    public URL getFilePath(String fileName) throws MalformedURLException{
         File file = (File)fileMap.get(fileName);
         if(file==null){
             File newFile = new File(fileName);
             fileMap.put(fileName,newFile);
             return newFile.toURI().toURL();
         }else{
             return file.toURI().toURL();
         }
    }

}
