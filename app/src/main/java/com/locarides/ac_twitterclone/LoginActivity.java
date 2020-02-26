package com.locarides.ac_twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etLoginEmail,etLoginPassword;
    Button btnSignupAct,btnLoginAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginEmail=findViewById(R.id.etLoginEmail);
        etLoginPassword=findViewById(R.id.etLoginPassword);
        etLoginPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
                    onClick(btnLoginAct);
                }
                return false;
            }
        });

        btnSignupAct=findViewById(R.id.btnSignupAct);
        btnLoginAct=findViewById(R.id.btnLoginAct);

        btnSignupAct.setOnClickListener(this);
        btnLoginAct.setOnClickListener(this);
    }

    public void rootLoginLayoutTapped(View view) {
        try{
            InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnLoginAct:

                if(etLoginEmail.getText().toString().equals("")||etLoginPassword.getText().toString().equals("")){
                    FancyToast.makeText(LoginActivity.this,"None of the fields can be blank", Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
                }else{
                    final ParseUser parseUser=new ParseUser();
                    parseUser.setEmail(etLoginEmail.getText().toString());
                    parseUser.setPassword(etLoginPassword.getText().toString());

                    ParseUser.logInInBackground(etLoginEmail.getText().toString(), etLoginPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if(user!=null && e==null){
                                FancyToast.makeText(LoginActivity.this,user.getUsername()+" logging in", Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                                Intent intent=new Intent(LoginActivity.this,TwitterUserActivity.class);
                                startActivity(intent);
                            }else{
                                FancyToast.makeText(LoginActivity.this,e.getMessage(), Toast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                            }
                        }
                    });

                }
                break;

            case R.id.btnSignupAct:
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                break;
        }

    }
}