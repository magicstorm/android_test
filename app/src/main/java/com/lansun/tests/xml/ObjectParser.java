package com.lansun.tests.xml;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Stack;

/**
 * Created by ly on 30/03/2017.
 */

public class ObjectParser {
    public static final String FIELD_TYPE_STR = "type";
    String filePath;
    String charSet;
    XmlPullParser xp;
    public ObjectParser(String path, String charSet) throws FileNotFoundException, XmlPullParserException {
        xp = Xml.newPullParser();
        xp.setInput(new FileInputStream(new File(path)), charSet);
    }

    /**
     * format:
     * <className>
     *     <fieldname>
     *         <type></type>
     *         <val></val>
     *         or
     *         <mapEntry></mapEntry>
     *         ...
     *         or
     *         <arrayVal></arrayVal>
     *         ...
     *         or
     *         <objectClassName>
     *             <fieldname>...</fieldname>
     *             ...
     *         </objectClassName>
     *
     *     </fieldname>
     *
     * </className>
     * @param clazz
     * @param <T>
     * @return
     * @throws XmlPullParserException
     */
    public <T> T parse(Class<T> clazz) throws XmlPullParserException, IOException, IllegalAccessException, InstantiationException {
        T obj = clazz.newInstance();
        return parseObject(clazz, 1);
    }

    public <T> T parseObject(Class<T> clazz, int objectDepth) throws XmlPullParserException, IllegalAccessException, InstantiationException, IOException {
        Stack<String> tags = new Stack<>();
        while(xp.getEventType()!=XmlPullParser.END_DOCUMENT){
            int fieldDepth = objectDepth + 1;
            if(xp.getDepth()== fieldDepth){
                switch (xp.getEventType()){
                    case XmlPullParser.START_TAG:
                        String fieldName = xp.getName();
                        parseField(fieldDepth);
                        break;
                }
            }

            xp.next();
        }
        return null;
    }

    private <T> T parseField(int fieldDepth) throws IOException, XmlPullParserException {
        Stack<String> fieldTags = new Stack<>();
        Class fieldClass = null;

        xp.next();

        if(xp.getDepth()<=fieldDepth){
            return null;
        }

        //get field type
        if(xp.getName().equals(FIELD_TYPE_STR)){
            String className = xp.nextText();
            try {
                fieldClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Log.e("field error", "can not find class: " + className);
            }
        }
        else{
            Log.e("field error", "first element is not type");
            return null;
        }

        if(fieldClass==String.class){
        }

        while(xp.getDepth()>fieldDepth){
        }
        return null;
    }
}
