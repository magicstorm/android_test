package com.lansun.tests;

import android.content.res.Resources;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.widget.Toast;

import com.lansun.tests.utils.ToastSingle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import dalvik.system.DexClassLoader;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;

/**
 * Created by ly on 31/03/2017.
 */

public class MClassLoader extends ClassLoader{

    public static final String PATCH_PATH = "PatchClass.java";
    public static final String BASE_URL = "http://10.0.1.121:8888/";

    private interface NetworkService{
        @GET(PATCH_PATH)
        Call<ResponseBody> getPatchFile();
    }

    private NetworkService getNetworkService(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).build();
        return retrofit.create(NetworkService.class);
    }

    private boolean compileJavaFile(String fileName) throws IOException {
        System.out.println("compile " + fileName + "...\n");

        Process p = Runtime.getRuntime().exec("javac " + fileName);

        try{
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int ret=p.exitValue();
        return ret==0;
    }

    public Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        Class clazz = null;

        //find class if loaded
        clazz = findLoadedClass(className);

        //get filePath according to className
        String filePath = className.replace(".", "/");

        //get file path
        String javaPath = filePath + ".java";
        String classPath = filePath + ".class";

        File javaFile = new File(javaPath);
        File classFile = new File(classPath);

        if(javaFile.exists()&&
                !classFile.exists()||
                //if new java file exist, it should be modified after classFile was modified last time
                //according to the code
                javaFile.lastModified() > classFile.lastModified()
                ){


            try {
                if(!compileJavaFile(javaPath)||!javaFile.exists()){
                   throw new Resources.NotFoundException("Compile failed: " + javaPath);
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new ClassNotFoundException(e.toString());
            }

        }

        try {
            byte[] raw = getBytes(classPath);
            clazz = defineClass(className, raw, 0, raw.length);
        } catch (IOException e) {
            //no problem here if we can reach here, it means the class might be in a library
        }

        //if define failed let's find it in library
        if(clazz==null)clazz = findSystemClass(className);

        //resolve class
        if(resolve&&clazz!=null)resolveClass(clazz);

        if(clazz==null)throw new ClassNotFoundException(className);

        return clazz;
    }


    public void downloadPatch (final String path){
        NetworkService ns = getNetworkService();
        Call<ResponseBody> call = ns.getPatchFile();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                InputStream is = response.body().byteStream();
                try {
                    savePatch(is, path);
                } catch (IOException e) {
                    Log.e("io", "IOException happened...");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private byte[] getBytes(String fileName) throws IOException {
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);


        byte[] bytes = new byte[(int)file.length());

        int r = fis.read(bytes);

        if(r<file.length()){
            throw new IOException("can not read file, it's too long...");
        }

        fis.close();

        return bytes;
    }

    private void savePatch(InputStream is, String path) throws IOException {
        FileOutputStream fos = new FileOutputStream(new File(path));
        int len = 0;
        byte[] b = new byte[8192];
        while((len = is.read(b))!=-1){
            fos.write(b, 0, len);
            b = new byte[8192];
        }
        is.close();
        fos.close();
        ToastSingle.showToastCenter(TestsApplication.getContext(), "saved to:" + path);
    }
}
