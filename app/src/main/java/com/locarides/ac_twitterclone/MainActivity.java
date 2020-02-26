package com.locarides.ac_twitterclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    EditText etEmail,etUsername,etPassword;
    Button btnSignup,btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ParseInstallation.getCurrentInstallation().saveInBackground();

        setTitle(R.string.sign_up);

        etEmail=findViewById(R.id.etEmail);
        etUsername=findViewById(R.id.etUsername);
        etPassword=findViewById(R.id.etPassword);
        etPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
                    onClick(btnSignup);
                }
                return false;
            }
        });

        btnSignup=findViewById(R.id.btnSignUp);
        btnLogin=findViewById(R.id.btnLogin);

        btnSignup.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        if(ParseUser.getCurrentUser()!=null){
            transitionToTwitterActivity();
        }
    }

    private void transitionToTwitterActivity() {
        startActivity(new Intent(MainActivity.this,TwitterUserActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnSignUp:
                if(etEmail.getText().toString().equals("")||etUsername.getText().toString().equals("")||etPassword.getText().toString().equals("")){
                    FancyToast.makeText(MainActivity.this,"None of the fields can be blank", Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
                }else{
                    final ParseUser parseUser=new ParseUser();
                    parseUser.setEmail(etEmail.getText().toString());
                    parseUser.setUsername(etUsername.getText().toString());
                    parseUser.setPassword(etPassword.getText().toString());

                    final ProgressDialog progressDialog=new ProgressDialog(this);
                    progressDialog.setMessage("Signing up "+etUsername.getText().toString());
                    progressDialog.show();

                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null){
                                FancyToast.makeText(MainActivity.this,parseUser.get("username")+" signed up",Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                                transitionToTwitterActivity();
                            }else{
                                FancyToast.makeText(MainActivity.this,e.getCode()+": "+e.getMessage(),Toast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }
                break;

            case R.id.btnLogin:
                Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }

    }

    public void rootLoginLayoutTapped(View view) {
        try{
            InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}