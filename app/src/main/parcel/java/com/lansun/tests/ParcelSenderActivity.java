package com.lansun.tests;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.lansun.tests.R;

/**
 * Created by ly on 10/03/2017.
 */

public class ParcelSenderActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et;
    private int index = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_sender);

        et = (EditText)findViewById(R.id.input);
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    sendMessage();
                }
            }
        });


    }

    private void sendMessage(){
        String msgStr = et.getText().toString();
        Message msg = new Message(msgStr.trim(), index);
        Intent intent = new Intent(this, ParcelReceiverActivity.class);
        intent.putExtra("message", AutoParcel.wrap(msg));
        startActivity(intent);
        index += 1;
    }

    @Override
    public void onClick(View v) {
        et.clearFocus();
    }

    public class Message{
        protected String text;
        protected int index;

        public String getText() {
            return text;
        }
//
//        public void setText(String text) {
//            this.text = text;
//        }
//
//        public int getIndex() {
//            return index;
//        }
//
//        public void setIndex(int index) {
//            this.index = index;
//        }
//
        public Message(String text, int index){
            this.text = text;
            this.index = index;
        }
//
//        public Message(Parcel in){
//            this.text = in.readString();
//            this.index = in.readInt();
//        }
//
//        public static final Creator<Message> CREATOR = new Creator<Message>(){
//            @Override
//            public Message createFromParcel(Parcel source) {
//                return new Message(source);
//            }
//
//            @Override
//            public Message[] newArray(int size) {
//                return new Message[size];
//            }
//        };
//
//        @Override
//        public int describeContents() {
//            return 0;
//        }
//
//        @Override
//        public void writeToParcel(Parcel dest, int flags) {
//            dest.writeString(text);
//            dest.writeInt(index);
//        }
    }

}
