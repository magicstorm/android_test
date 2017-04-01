package com.lansun.tests;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by ly on 09/03/2017.
 */

public class ValidationUtils{
    public static class IDValidator{

        private String id;
        public IDValidator(String id){
            String idStr = id.trim();
            if(TextUtils.isEmpty(idStr))return;
            this.id = idStr;
        }

        public String readAddressCodeMapping(File idFile){
            try {
                FileReader fileReader = new FileReader(idFile);
                StringBuffer sb = new StringBuffer();
                int len = 0;
                char[] buff = new char[1024];
                while((len=fileReader.read(buff, 0, buff.length))!=-1){
                    sb.append(buff);
                    buff = new char[1024];
                }
                return sb.toString();
            } catch (Exception e) {
                return null;
            }
        }
    }

}
