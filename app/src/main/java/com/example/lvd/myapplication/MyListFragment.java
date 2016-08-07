package com.example.lvd.myapplication;

import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by lvd on 02/07/2016.
 */
public class MyListFragment extends ListFragment implements AdapterView.OnItemClickListener {
    //private final int MAX_ITEM = 109;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);

        int titles = R.raw.titles;
        InputStream is = this.getResources().openRawResource(titles);

        ArrayList<String> titleList = null;
        try {
            titleList = (ArrayList<String>) CommonUtils.ReadBaseDataToStringArray(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final String LIKE = getActivity().getString(R.string.like);
        final String DISLIKE = getActivity().getString(R.string.dislike);
        final int MAX_ITEM = Integer.parseInt(getActivity().getString(R.string.maxItem));

        int count = MAX_ITEM;


        for (String title: titleList){
            int rating = getRating(count);
            title = updateTitleRating(rating,title, LIKE, DISLIKE);
            titleList.set(MAX_ITEM - count,title);
            count--;
        }


        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,titleList);
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Planets, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Start a new intent
        Intent poemList = new Intent(getActivity(), DetailActivity.class);

        //Send data to other screen
        poemList.putExtra("position", position);
        getActivity().startActivity(poemList);
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

    public int getRating(int memoryItem){
        int rating = 0;
        Cursor cursor = getActivity().getContentResolver().query(RatingMemoryProvider.CONTENT_URI,null,"item='"+ memoryItem + "'",null,null);
        if (cursor.moveToFirst()){
            rating = cursor.getInt(cursor.getColumnIndex(RatingMemoryProvider.RATING));
        }
        return rating;
    }
}
