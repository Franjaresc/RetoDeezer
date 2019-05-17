package com.example.retodeezer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.retodeezer.model.Track;
import com.example.retodeezer.service.ServiceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

public class TrackActivity extends AppCompatActivity {

    private ImageView ivCover;
    private TextView etTitle;
    private TextView etAuthor;
    private TextView etAlbum;
    private TextView etDuration;

    private Button btnPlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        ivCover = findViewById(R.id.track_image);
        etTitle = findViewById(R.id.track_title);
        etAuthor = findViewById(R.id.track_author);
        etAlbum = findViewById(R.id.track_album);
        etDuration = findViewById(R.id.track_duration);

        btnPlay = findViewById(R.id.btn_play);

        if (getIntent().hasExtra("id")) {
            String id = getIntent().getStringExtra("id");
            new Thread(() -> {
                new ServiceManager.GETTrack(id, new ServiceManager.GETTrack.OnResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        runOnUiThread(() -> {

                            JSONObject json = null;    // create JSON obj from string
                            try {
                                json = new JSONObject(response);


                                String track_json = json.toString();

                                Track track = new Gson().fromJson(track_json, new TypeToken<Track>() {
                                }.getType());

                                etTitle.setText(track.getTitle());
                                etAlbum.setText(track.getAlbum().getTitle());
                                etAuthor.setText(track.getArtist().getName());
                                etDuration.setText(track.getDuration() + "");

                                btnPlay.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = getPackageManager().getLaunchIntentForPackage("deezer.android.app");
                                        if (intent != null) {
                                            Intent a = new Intent(Intent.ACTION_VIEW, Uri.parse("deezer://www.deezer.com/track/" + track.getId()));
                                            startActivity(a);
                                        } else {
                                            Intent b = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.deezer.com/track/" + track.getId()));
                                            startActivity(b);
                                        }
                                    }
                                });

                                Glide.with(TrackActivity.this).load(track.getAlbum().getCover_medium()).into(ivCover);

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
