package com.locarides.ac_twitterclone;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SendTweet extends AppCompatActivity {

    private EditText etTweet;
    private Button btnSendTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);

        etTweet=findViewById(R.id.etTweet);
    }

    public void sendTweet(View view) {

        ParseObject parseObject=new ParseObject("MyTweet");
        parseObject.put("user", ParseUser.getCurrentUser().getUsername());
        parseObject.put("tweet",etTweet.getText().toString());

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Saving..!");
        progressDialog.show();

        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    FancyToast.makeText(SendTweet.this, ParseUser.getCurrentUser().getUsername()+" 's tweet ("+etTweet.getText().toString()+") saved !!", Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                }else{
                    FancyToast.makeText(SendTweet.this, e.getMessage(), Toast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                }

                progressDialog.dismiss();
            }
        });
    }
}