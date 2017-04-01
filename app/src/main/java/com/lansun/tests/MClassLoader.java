package com.lansun.tests;

import android.renderscript.ScriptGroup;
import android.util.Log;
import android.widget.Toast;

import com.lansun.tests.utils.ToastSingle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
