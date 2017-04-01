package com.lansun.tests.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by ly on 6/24/16.
 */
public class HelperFunctions {
    private static HashMap<Class<?>, Class<?>> getWrappersToPrimitivesMap(){
        HashMap<Class<?>, Class<?>> WRAPPERS_TO_PRIMITIVES = new HashMap<Class<?>, Class<?>>();
        WRAPPERS_TO_PRIMITIVES.put(Boolean.class, boolean.class);
        WRAPPERS_TO_PRIMITIVES.put(Byte.class, byte.class);
        WRAPPERS_TO_PRIMITIVES.put(Character.class, char.class);
        WRAPPERS_TO_PRIMITIVES.put(Double.class, double.class);
        WRAPPERS_TO_PRIMITIVES.put(Float.class, float.class);
        WRAPPERS_TO_PRIMITIVES.put(Integer.class, int.class);
        WRAPPERS_TO_PRIMITIVES.put(Long.class, long.class);
        WRAPPERS_TO_PRIMITIVES.put(Short.class, short.class);
        WRAPPERS_TO_PRIMITIVES.put(Void.class, void.class);
        return WRAPPERS_TO_PRIMITIVES;
    }
    private static HashMap<Class<?>, Class<?>> getPrimitivesToWrappersMap(){
        HashMap<Class<?>, Class<?>> PRIMITIVES_TO_WRAPPERS = new HashMap<Class<?>, Class<?>>();
        PRIMITIVES_TO_WRAPPERS.put(boolean.class, Boolean.class);
        PRIMITIVES_TO_WRAPPERS.put(byte.class, Byte.class);
        PRIMITIVES_TO_WRAPPERS.put(char.class, Character.class);
        PRIMITIVES_TO_WRAPPERS.put(double.class, Double.class);
        PRIMITIVES_TO_WRAPPERS.put(float.class, Float.class);
        PRIMITIVES_TO_WRAPPERS.put(int.class, Integer.class);
        PRIMITIVES_TO_WRAPPERS.put(long.class, Long.class);
        PRIMITIVES_TO_WRAPPERS.put(short.class, Short.class);
        PRIMITIVES_TO_WRAPPERS.put(void.class, Void.class);
        return PRIMITIVES_TO_WRAPPERS;
    }


    /**
     * @param f
     *      source float
     * @param dotn
     *      number of decimal after dot
     * @return
     *      result
     */
    public static float formatDecimal(float f, int dotn){
        return Math.round(f*Math.pow(10, dotn))/(float)Math.pow(10, dotn);
    }

    /**
     * Convert Org.json object to destination object
     * Notice: field name and type should be the same
     * @param jsonObj
     *      source jsonObject
     * @param custObj
     *      any kind of object you need and created
     */
    public static <T> void jsonObjToCustomObj(JSONObject jsonObj,  T custObj){

        Iterator<String> objKeys = jsonObj.keys();
        while(objKeys.hasNext()){
            String key = objKeys.next();
//            System.out.println("key:"+key);
            try {
//                System.out.println("convert object att: "+key+"->"+jsonObj.get(key));
                Field f = custObj.getClass().getDeclaredField(key);
                f.setAccessible(true);
//                System.out.println("field:"+f.toString());
                Class fType = f.getType();
                HashMap PRIMITIVES_TO_WRAPPERS = getPrimitivesToWrappersMap();
                if(PRIMITIVES_TO_WRAPPERS.containsKey(fType)){
                    fType = (Class)PRIMITIVES_TO_WRAPPERS.get(fType);
                }
//                System.out.println(key+":"+jsonObj.get(key));
                if(fType!=jsonObj.get(key).getClass()){

                    HashMap WRAPPERS_TO_PRIMITIVES = getWrappersToPrimitivesMap();
                    Class ftypeJson = jsonObj.get(key).getClass();
                    if(WRAPPERS_TO_PRIMITIVES.containsKey(ftypeJson)){
                        ftypeJson = (Class)WRAPPERS_TO_PRIMITIVES.get(ftypeJson);
                    }

                    Method method = fType.getDeclaredMethod("valueOf", ftypeJson);

//                        System.out.println(key+":"+jsonObj.get(key));
                    f.set(custObj, method.invoke(null, jsonObj.get(key)));
                }
                else{
                    f.set(custObj, jsonObj.get(key));
                }
            } catch (IllegalAccessException e) {
                System.out.println("illegal");
//                e.printStackTrace();
            } catch (NoSuchFieldException e) {
//                System.out.println("no field");
//                e.printStackTrace();
            } catch (JSONException e) {
                System.out.println("JSONException");
//                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                System.out.println("no Method");
//                e.printStackTrace();
            } catch (InvocationTargetException e) {
                System.out.println("InvocationTargetException");
//                e.printStackTrace();
            }
        }
    }

    public static class MyThread extends Thread{
        public volatile boolean isRun = true;
        public void close(){
            isRun = false;
        }
    };

    /**
     * Periodically run the method of a certain object
     * @param methodObject
     *      object
     * @param methodName
     *      methodName
     * @param interval
     *      interval
     * @param args
     *      method parameters array
     * @return
     *      a closeable thread object for the periodic work
     */
    public static MyThread setInterval(final Object methodObject, final String methodName, final long interval, final Object ... args){
         MyThread newThread = new MyThread(){
             @Override
             public void run() {
                 while(isRun){
                     try {
                         Method method = methodObject.getClass().getMethod(methodName, objectToClassArray(args));
                         method.invoke(methodObject, args);
                         Thread.sleep(interval);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     } catch (NoSuchMethodException e) {
                         e.printStackTrace();
                     } catch (InvocationTargetException e) {
                         e.printStackTrace();
                     } catch (IllegalAccessException e) {
                         e.printStackTrace();
                     }
                 }
             }
         };
         newThread.start();
         return newThread;
    }

    /**
     * Convert an objects array to class array
     * @param objects
     *      source objects array
     * @return
     *      array of classes of objects
     */
    public static Class<?>[] objectToClassArray(Object[] objects){
        ArrayList<Class<?>> classes = new ArrayList<>();
        for(Object object: objects){
            classes.add(object.getClass());
        }
        Class<?>[] classArray = new Class<?>[classes.size()];
        return classes.toArray(classArray);
    }

    public static boolean objHasAll(JSONObject jsonObject, String ...args){
        boolean has = true;
        try {
            for(int i=0;i<args.length;i++){
                boolean curhas = jsonObject.has(args[i])
                        &&(!jsonObject.getString(args[i]).equals("null"))
                        &&(jsonObject.getString(args[i])!=null)
                        &&(!(jsonObject.getString(args[i]).trim().equals("")));
                has = has && curhas;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return has;
    }


//    public static adjust
}
