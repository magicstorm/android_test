package com.lansun.tests;

import android.media.audiofx.AudioEffect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ly on 10/03/2017.
 */

public class AutoParcel {

    public static class Proxy implements Parcelable{
        Object mOriginal;

        public Proxy setObject(Parcel in){
            try {
                byte[] b = in.createByteArray();
                ByteArrayInputStream bis = new ByteArrayInputStream(b);
                ObjectInputStream ois = new ObjectInputStream(bis);
                this.mOriginal = ois.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return this;
        }
        public Proxy(){}

        public Proxy(Object obj){
            mOriginal = obj;
        }

        @Override
        public int describeContents() {
            return mOriginal instanceof FileDescriptor?CONTENTS_FILE_DESCRIPTOR:0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            Field[] fields = mOriginal.getClass().getDeclaredFields();
            ArrayList<String> fieldsList = new ArrayList<>();
            for(Field f: fields){
                fieldsList.add(f.getName());
            }
        }

        public static final Creator<Proxy> CREATOR = new Creator<Proxy>() {
            @Override
            public Proxy createFromParcel(Parcel source) {
                return new Proxy().setObject(source);
            }

            @Override
            public Proxy[] newArray(int size) {
                return new Proxy[size];
            }
        };



    }

    public static Parcelable wrap(Object obj){
        return new AutoParcel.Proxy(obj);
    }

    public static Object unwrap(Parcelable parcelable){
        return ((Proxy)parcelable).mOriginal;
    }


}
