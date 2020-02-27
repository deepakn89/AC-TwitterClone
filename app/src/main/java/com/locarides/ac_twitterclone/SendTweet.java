package com.locarides.ac_twitterclone;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendTweet extends AppCompatActivity {

    private EditText etTweet;
    private Button btnSendTweet,btnViewTweets;
    private ListView tweetListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);

        etTweet=findViewById(R.id.etTweet);
        tweetListView=findViewById(R.id.tweet_listView);
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

    public void viewOthersTweet(View view) {
        final ArrayList<HashMap<String,String>> tweetList=new ArrayList<>();
        final SimpleAdapter tweetAdapter=new SimpleAdapter(SendTweet.this,
                                                            tweetList,
                                                            android.R.layout.simple_list_item_2,
                                                            new String[] {"tweetUsername","tweetValue"},
                                                            new int[]{android.R.id.text1,android.R.id.text2});

        try{
            ParseQuery<ParseObject> parseQuery=ParseQuery.getQuery("MyTweet");
            parseQuery.whereContainedIn("user",ParseUser.getCurrentUser().getList("fanOf"));
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(objects.size()>0 && e==null){
                        for(ParseObject tweetObject:objects){
                            HashMap<String,String> userTweet=new HashMap<>();
                            userTweet.put("tweetUsername",tweetObject.getString("user"));
                            userTweet.put("tweetValue",tweetObject.getString("tweet"));

                            tweetList.add(userTweet);
                        }
                        tweetListView.setAdapter(tweetAdapter);
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}