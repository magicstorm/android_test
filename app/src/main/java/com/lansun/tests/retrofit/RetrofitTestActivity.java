package com.lansun.tests.retrofit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.lansun.tests.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by ly on 20/02/2017.
 */

public class RetrofitTestActivity extends AppCompatActivity{

    private ImageView imageView;
    private TextView tv;
    private List<Image> datas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_test);
        initDatas();
        findViews();
        getBitmap();
    }

    public interface TestService{
        //no params basic get
        @GET("home/popbannerlist.mx")
        Call<Image> getImage();

        @Headers({
                "Content-Type: image/png; charset=binary"
        })
        @GET
        Call<ResponseBody> getBimap(@Url String path); //this will override baseUrl which is a must for the api

    }

    class Image{

        Data data;
        String msg;
        String code;
    }

    class Data{
        List<Img> list;
        String size;
    }

    class Img{
        String index;
        String target_url;
        String url;
    }

    private void initDatas(){
        datas = new ArrayList<Image>();
    }

    private void findViews(){
        imageView = (ImageView)findViewById(R.id.iv);
        tv = (TextView)findViewById(R.id.tv);
    }

    private void getBitmap(){
        /**
         * download datas
         */
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://10.0.1.154/").build();

        TestService testService = retrofit.create(TestService.class);

        Call<Image> call= testService.getImage();


        call.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(Call<Image> call, Response<Image> response) {
                Image fuck = response.body();

            }

            @Override
            public void onFailure(Call<Image> call, Throwable throwable) {

            }
        });


        /**
         * download image
         */
        Retrofit retrofit1 = new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://7xn0yl.com1.z0.glb.clouddn.com/")
                .build();

        TestService testService1 = retrofit1.create(TestService.class);

        Call<ResponseBody> call1 = testService1.getBimap("http://7xn0yl.com1.z0.glb.clouddn.com/topbanner-jupiterdiamondactivity.jpg");

        call1.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                InputStream inputStream = response.body().byteStream();
                imageView.setImageBitmap(BitmapFactory.decodeStream(inputStream));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }


    /**
     * post data
     */


//        http://7xn0yl.com1.z0.glb.clouddn.com/topbanner-jupiterdiamondactivity.jpg


}
