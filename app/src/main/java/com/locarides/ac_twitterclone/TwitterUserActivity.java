package com.locarides.ac_twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TwitterUserActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;

    String followedUser="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_user);

        FancyToast.makeText(TwitterUserActivity.this,"Welcome "+ ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT,FancyToast.INFO,true).show();

        listView=findViewById(R.id.listView);

        arrayList=new ArrayList<>();
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_checked,arrayList);

        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this);

        try {
            ParseQuery<ParseUser> parseQuery=ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());

            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if(e==null){
                        if(objects.size()>0){
                            for(ParseUser user:objects){
                                arrayList.add(user.getUsername().toString());
                            }
                            listView.setAdapter(arrayAdapter);

                            //To retain teh fanOf state after each login
                            for(String twitteruser: arrayList){
                                if(Objects.requireNonNull(ParseUser.getCurrentUser().getList("fanOf")).contains(twitteruser)){
                                    followedUser=followedUser+twitteruser+"\n";
                                    listView.setItemChecked(arrayList.indexOf(twitteruser),true);

                                    FancyToast.makeText(TwitterUserActivity.this,ParseUser.getCurrentUser().getUsername()+" is following "+followedUser, Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                                }
                            }
                        }
                        //FancyToast.makeText(TwitterUserActivity.this,"Welcome "+ ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                    }else{
                        FancyToast.makeText(TwitterUserActivity.this,e.getMessage(), Toast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                    }
                }
            });
        }catch (Exception e){
            e.getMessage();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent intent=new Intent(TwitterUserActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                break;

            case R.id.send_tweet:
                startActivity(new Intent(TwitterUserActivity.this, SendTweet.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        CheckedTextView checkedTextView= (CheckedTextView) view;
        if(checkedTextView.isChecked()){
            FancyToast.makeText(TwitterUserActivity.this,arrayList.get(position)+" is now followed", Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
            ParseUser.getCurrentUser().add("fanOf",arrayList.get(position));

        }else{
            FancyToast.makeText(TwitterUserActivity.this,arrayList.get(position)+" is not followed", Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
            ParseUser.getCurrentUser().getList("fanOf").remove(arrayList.get(position));

            List CurrentFanOfList=ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");

            ParseUser.getCurrentUser().put("fanOf",CurrentFanOfList);
        }

        //Saving user's fanOf data
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                    FancyToast.makeText(TwitterUserActivity.this,"data saved", Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
                }
            }
        });
    }
}