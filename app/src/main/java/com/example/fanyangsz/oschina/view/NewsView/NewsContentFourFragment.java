package com.example.fanyangsz.oschina.view.NewsView;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.fanyangsz.oschina.R;

/**
 * Created by fanyang.sz on 2016/1/11.
 */
public class NewsContentFourFragment extends Fragment {
    private ListView listView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_content,container,false);
        listView = (ListView)view.findViewById(R.id.listView_one);
        listView.setAdapter(new ArrayAdapter<String>(
                container.getContext(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                new String[]{
                        "456+", "b", "c", "d", "e", "f", "g", "h", "i","j","k","l","m","n"
                }));
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
