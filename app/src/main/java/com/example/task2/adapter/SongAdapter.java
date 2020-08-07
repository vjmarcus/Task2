package com.example.task2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.task2.R;
import com.example.task2.RecyclerViewClickListener;
import com.example.task2.SecondActivity;
import com.example.task2.model.Song;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private List<Song> songs;
    //убрать сенконд активитит из назыания
    private RecyclerViewClickListener recyclerViewClickListener;

    public SongAdapter(List<Song> songs, RecyclerViewClickListener recyclerViewClickListener) {
        this.songs = songs;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,
                parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        holder.authorItemTextView.setText(songs.get(position).getAuthor());
        holder.titleItemTextView.setText(songs.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView authorItemTextView;
        private final TextView titleItemTextView;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            authorItemTextView = itemView.findViewById(R.id.authorItemTextView);
            titleItemTextView = itemView.findViewById(R.id.titleItemTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerViewClickListener.recyclerViewListClicked(view, this.getLayoutPosition());
        }
    }
}
