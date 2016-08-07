package com.example.lvd.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lvd on 01/07/2016.
 */
public class DetailActivity extends Activity{
    static int memoryItem = 0;
    //private final int MAX_ITEM = 110;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_detail);

        final int MAX_ITEM = Integer.parseInt(this.getString(R.string.maxItem));

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            memoryItem = MAX_ITEM - extras.getInt("position");
        }

        final TextView memoryTextView = (TextView) findViewById(R.id.detailView);
        showDetailMemory(memoryItem, memoryTextView);

        memoryTextView.setOnTouchListener(new OnSwipeTouchListener(DetailActivity.this){
            public void onBottomToTopSwipe() {
                //Toast.makeText(DetailActivity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onLeftToRightSwipe() {
                //Toast.makeText(DetailActivity.this, "right", Toast.LENGTH_SHORT).show();
                if(memoryItem < MAX_ITEM) memoryItem += 1;
                showDetailMemory(memoryItem, memoryTextView);
            }
            public void onRightToLeftSwipe() {
                //Toast.makeText(DetailActivity.this, "left", Toast.LENGTH_SHORT).show();
                if(memoryItem > 0) memoryItem -= 1;
                showDetailMemory(memoryItem, memoryTextView);
            }
            public void onTopToBottomSwipe() {
                //Toast.makeText(DetailActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

            public void onDoubleClick(){

                new AlertDialog.Builder(DetailActivity.this)
                        .setTitle("Do you like this memory?")
                        //.setMessage("You tried and failed the maximum of permitted times!")
                        .setCancelable(false)
                        .setPositiveButton("Like", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateRating(1);
                                showDetailMemory(memoryItem, memoryTextView);
                            }
                        })

                        .setNegativeButton("Dislike", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateRating(-1);
                                showDetailMemory(memoryItem, memoryTextView);
                            }
                        })

                        .setNeutralButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create().show();

                //Toast.makeText(DetailActivity.this, "double click", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDetailMemory(int memoryItem, TextView memoryTextView){
        String myText;
        try {
            final int MAX_ITEM = Integer.parseInt(this.getString(R.string.maxItem));
            final String START = "*************** %s ***************";
            final String END = "%s-END";
            final String SEPARATE = "---------------------------";
            String LIKE = this.getString(R.string.like);
            String DISLIKE = this.getString(R.string.dislike);

            String startSignal = String.format(START, memoryItem);
            String endSignal = String.format(START, String.format(END, memoryItem));

            int poems = R.raw.mymails;
            InputStream is = this.getResources().openRawResource(poems);

            //Write last point to internal file
            final String LAST_POINT_FILE = this.getString(R.string.last_point_file);

            FileOutputStream fos = openFileOutput(LAST_POINT_FILE, Context.MODE_PRIVATE);
            fos.write(MAX_ITEM - memoryItem);
            fos.close();

            myText = CommonUtils.ReadSpecificBaseData(is, startSignal, endSignal);
            myText = myText.trim();
            String title, separate_title_body, body;

            int rating = 0;
            Cursor cursor = getContentResolver().query(RatingMemoryProvider.CONTENT_URI,null,"item='"+ memoryItem + "'",null,null);
            if (cursor.moveToFirst()){
                rating = cursor.getInt(cursor.getColumnIndex(RatingMemoryProvider.RATING));
            }

            if (memoryTextView != null){
                if (myText != null) {
                    // lvd: TODO here to make title of each Detail View highlight
                    int end_title_index = myText.indexOf(SEPARATE);
                    int separate_length = SEPARATE.length();

                    if (myText.length() > 38){
                        title = myText.substring(0, end_title_index - 1);
                        title = updateTitleRating(rating, title, LIKE, DISLIKE);

                        separate_title_body = myText.substring(end_title_index, end_title_index + title.length());
                        body = myText.substring(end_title_index + separate_length + 1);
                        memoryTextView.setText(Html.fromHtml("<h2><font color=\"red\">"+ title +"</font></h2>" +
                                "<h4><font color=\"blue\">"+ separate_title_body +"</font></h4>" + "<p>"+ body + "</p>"));
                    } else{
                        memoryTextView.setText(myText);
                    }
                }
                memoryTextView.setMovementMethod(new ScrollingMovementMethod());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int updateRating(int rate){
        String stringFilter = "item='" + memoryItem + "'";
        ContentValues values = new ContentValues();
        values.put(RatingMemoryProvider.RATING, rate);
        int count = getContentResolver().update(RatingMemoryProvider.CONTENT_URI, values, stringFilter, null);
        return count;
    }

    public String updateTitleRating(int rating, String title, String like, String dislike){
        if (rating == 1){
            if (title.contains(like)){
                return title;
            } else if (title.contains(dislike)){
                return title.substring(0, title.length() - dislike.length());
            } else{
                title += like;
            }
        } else if (rating == -1){
            if (title.contains(dislike)){
                return title;
            } else if (title.contains(like)){
                return title.substring(0, title.length() - like.length());
            } else{
                title += dislike;
            }
        }

        return title;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            super.onBackPressed();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
