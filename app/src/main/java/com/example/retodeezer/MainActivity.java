package com.example.retodeezer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.retodeezer.adapter.PlayListAdapter;
import com.example.retodeezer.model.SearchPlaylist;
import com.example.retodeezer.service.ServiceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText txt_Search;
    private ImageButton btn_Search;

    private RecyclerView RV_List;
    private PlayListAdapter playlistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_Search = findViewById(R.id.et_search);
        btn_Search = findViewById(R.id.btn_search);

        RV_List = findViewById(R.id.playlist);
        playlistAdapter = new PlayListAdapter();

        RV_List.setLayoutManager(new LinearLayoutManager(this));
        RV_List.setAdapter(playlistAdapter);
        RV_List.setHasFixedSize(true);

        new Thread(() -> {
            new ServiceManager.GETSearchPlaylist("Nirvana", new ServiceManager.GETSearchPlaylist.OnResponseListener() {
                @Override
                public void onResponse(String response) {
                    runOnUiThread(() -> {

                        JSONObject json = null;    // create JSON obj from string
                        try {
                            json = new JSONObject(response);
                            JSONArray data = json.getJSONArray("data");

                            String playlist_json = data.toString();

                            ArrayList<SearchPlaylist> playlist = new Gson().fromJson(playlist_json, new TypeToken<List<SearchPlaylist>>() {
                            }.getType());

                            playlistAdapter.setData(playlist);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
        }).start();

        btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = txt_Search.getText().toString();
                if (!search.isEmpty()) {
                    new Thread(() -> {
                        new ServiceManager.GETSearchPlaylist(search, new ServiceManager.GETSearchPlaylist.OnResponseListener() {
                            @Override
                            public void onResponse(String response) {
                                runOnUiThread(() -> {

                                    JSONObject json = null;    // create JSON obj from string
                                    try {
                                        json = new JSONObject(response);
                                        JSONArray data = json.getJSONArray("data");

                                        String playlist_json = data.toString();

                                        ArrayList<SearchPlaylist> list = new Gson().fromJson(playlist_json, new TypeToken<List<SearchPlaylist>>() {
                                        }.getType());

                                        playlistAdapter.setData(list);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        });
                    }).start();
                }
            }
        });


    }
}
