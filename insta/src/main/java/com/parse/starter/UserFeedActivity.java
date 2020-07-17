package com.parse.starter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class UserFeedActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.message_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.message ){

            Intent intent = new Intent(getApplicationContext(),UserMessage.class);
            intent.putExtra("username",activeUsername);
            //intent.putExtra("username",activeUsername);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    String activeUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        final LinearLayout linearLayout= (LinearLayout) findViewById(R.id.chatEditText);

        Intent intent = getIntent();
        activeUsername = intent.getStringExtra("username");

        setTitle(activeUsername+"'s Feed");

        ParseQuery<ParseObject> query= new ParseQuery<ParseObject>("image");
        query.whereEqualTo("username",activeUsername);
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                if(e == null){

                    if(list.size()>0){

                        for(ParseObject object: list){

                            ParseFile file = (ParseFile) object.get("image");
                            file.getDataInBackground(new GetDataCallback() {

                                @Override
                                public void done(byte[] bytes, ParseException e) {

                                    if(e == null && bytes!=null){

                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);

                                        ImageView imageView = new ImageView(getApplicationContext());

                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                        ));

                                        imageView.setImageBitmap(bitmap);

                                        linearLayout.addView(imageView);


                                    }
                                }
                            });
                        }
                    }else if(list.size()==0){
                        TextView textView = new TextView(getApplicationContext());
                        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        textView.setText(activeUsername+"'s Feed is Empty");
                        textView.setTextColor(Color.parseColor("#000000"));
                        textView.setPadding(370, 750, 20, 20);// in pixels (left, top, right, bottom)
                        linearLayout.addView(textView);
                    }
                }else{
                    e.printStackTrace();
                }
            }
        });

    }
}
