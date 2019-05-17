package com.example.retodeezer.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.retodeezer.PlayListActivity;
import com.example.retodeezer.R;
import com.example.retodeezer.model.SearchPlaylist;

import java.util.ArrayList;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.CustomViewHolder> {

    ArrayList<SearchPlaylist> data;

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;

        public CustomViewHolder(LinearLayout v) {
            super(v);
            root = v;
        }
    }

    public void showAllFriends(ArrayList<SearchPlaylist> allFriends) {
        for (int i = 0; i < allFriends.size(); i++) {
            if (!data.contains(allFriends.get(i))) data.add(allFriends.get(i));
        }
        notifyDataSetChanged();
    }

    public void setData(ArrayList<SearchPlaylist> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addPlayList(SearchPlaylist playList) {
        data.add(playList);
        notifyDataSetChanged();
    }

    public PlayListAdapter() {
        data = new ArrayList<>();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item_view, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        ((TextView) holder.root.findViewById(R.id.playlist_name)).setText(data.get(position).getTitle());
        ((TextView) holder.root.findViewById(R.id.playlist_creator)).setText(data.get(position).getUser().getName());
        ((TextView) holder.root.findViewById(R.id.playlist_total)).setText(data.get(position).getNb_tracks() + "");
        Glide.with(holder.root.getContext()).load(data.get(position).getPicture_small()).into((ImageView) holder.root.findViewById(R.id.playlist_image));
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.root.getContext(), PlayListActivity.class);
                intent.putExtra("id", data.get(position).getId());
                holder.root.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}

