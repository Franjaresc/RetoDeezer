package com.example.retodeezer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.retodeezer.adapter.TrackAdapter;
import com.example.retodeezer.model.Playlist;
import com.example.retodeezer.model.Track;
import com.example.retodeezer.service.ServiceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlayListActivity extends AppCompatActivity {

    private ImageView ivPlaylist;
    private TextView etTitle;
    private TextView etDescription;
    private TextView etTotal;
    private RecyclerView rvTracks;

    private TrackAdapter trackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);

        ivPlaylist = findViewById(R.id.single_playlist_image);
        etTitle = findViewById(R.id.single_playlist_title);
        etDescription = findViewById(R.id.single_playlist_description);
        etTotal = findViewById(R.id.single_playlist_total);

        rvTracks = findViewById(R.id.rv_tracks);
        trackAdapter = new TrackAdapter();

        rvTracks.setLayoutManager(new LinearLayoutManager(this));
        rvTracks.setAdapter(trackAdapter);
        rvTracks.setHasFixedSize(true);

        if (getIntent().hasExtra("id")) {
            String id = getIntent().getStringExtra("id");
            new Thread(() -> {
                new ServiceManager.GETPlaylist(id, new ServiceManager.GETPlaylist.OnResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        runOnUiThread(() -> {

                            JSONObject json = null;    // create JSON obj from string
                            try {
                                json = new JSONObject(response);

                                JSONObject tracks_object = json.getJSONObject("tracks");
                                JSONArray tracks_data = tracks_object.getJSONArray("data");

                                String tracks_json = tracks_data.toString();

                                String playlist_json = json.toString();

                                Playlist playlist = new Gson().fromJson(playlist_json, new TypeToken<Playlist>() {
                                }.getType());

                                ArrayList<Track> tracks = new Gson().fromJson(tracks_json, new TypeToken<List<Track>>() {
                                }.getType());

                                trackAdapter.setData(tracks);

                                etTitle.setText(playlist.getTitle());
                                etDescription.setText(playlist.getDescription());
                                etTotal.setText(playlist.getNb_tracks() + "");
                                Glide.with(PlayListActivity.this).load(playlist.getPicture_big()).into(ivPlaylist);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                });
            }).start();
        }

    }


}
