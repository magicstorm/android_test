package com.lansun.tests;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lansun.tests.utils.ToastSingle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * Created by ly on 09/03/2017.
 */

public class ValidationActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String CODE_ADDRESS_MAPPING_FILE_NAME = "id_place";
    private EditText idEt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);
        findViews();
        setListeners();
    }

    private void findViews(){
        idEt = (EditText)findViewById(R.id.id_et);
    }

    private void initViews(){
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString();
            }
        };
    }

    private void setListeners(){
        idEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String id = idEt.getText().toString().trim();
                    if(TextUtils.isEmpty(id))return;
                    validateID(id);
                }
            }
        });
    }

    private void validateID(String id) {
        ValidationUtils.IDValidator validator = new ValidationUtils.IDValidator(id);
        File file = new File(Environment.getExternalStorageDirectory()+"/"+ CODE_ADDRESS_MAPPING_FILE_NAME);
        String addressCodeMappingStr = validator.readAddressCodeMapping(file);
        String jsonString = "{" + ("\"" + addressCodeMappingStr.trim().substring(1, addressCodeMappingStr.trim().length()-1)).replace(",", ",\"").replace(":", "\":") + "}";

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            sdf.parse("sssssss");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean fuck = jsonObject.has("654325");
        System.out.println("fuck");



        String[] addressCodeMappingArray = addressCodeMappingStr.substring(1, addressCodeMappingStr.length()-1).split(",");
        HashMap<String, String> addressCodeMappingMap = new HashMap<>();
        for(String map: addressCodeMappingArray) {
            String word1 = map.split(":")[0];
            String word2 = map.split(":")[1].trim();
            try {
                addressCodeMappingMap.put(
                        word1,
                        word2.substring(1, word2.length() - 1)
                );
            }
            catch (Exception e){
                System.out.println(
                        "word1: " + word1 + "\n" +
                                "word2: " + word2 + "\n"
                );
            }
        }

        Bundle bd = new Bundle();
    }

    @Override
    public void onClick(View v) {
        clearFocus();
    }

    private void clearFocus(){
        idEt.clearFocus();
    }
}
