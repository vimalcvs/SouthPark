package com.example.southpark;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.southpark.model.Episode;

import java.util.ArrayList;
import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.ViewHolder> {

    private Context context;
    private List<Episode> episodesList;
    EpisodesListFragment listFragment;
    private ItemClickListener mClickListener;

    public EpisodesAdapter(Context context, EpisodesListFragment listFragment) {
        this.listFragment = listFragment;
        this.context = context;
        episodesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.row_list, parent,false);

        return new ViewHolder(itemView);
    }

    public void attachList(ArrayList<Episode> episodesList) {
        this.episodesList = episodesList;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView viewName;

        LinearLayout episodeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            viewName = itemView.findViewById(R.id.episodeNameId);
            episodeLayout = itemView.findViewById(R.id.rowId);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final Episode currentEpisode = episodesList.get(getAdapterPosition());
            listFragment.setCurrentEpisode(currentEpisode);
            mClickListener.onItemClick(currentEpisode);
        }
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(Episode episode);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.viewName.setText(episodesList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return episodesList.size();
    }
}
