package com.example.southpark;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.southpark.model.Episode;

public class EpisodeFragment extends Fragment {

    private Episode currentEpisode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_episode, container, false);

        final Bundle bundle = getArguments();

        if (bundle != null) {
            currentEpisode = bundle.getParcelable("currentEpisode");
        }

        TextView nameView = rootView.findViewById(R.id.fragmentEpisodeNameId);
        TextView summaryView = rootView.findViewById(R.id.episodeSummaryId);
        ImageView episodeImageView = rootView.findViewById(R.id.episodeImageId);

        String nameString = currentEpisode.getName();
        nameView.setText(nameString);

        String imageString = currentEpisode.getImage();

        Glide.with(EpisodeFragment.this)
                .load(imageString)
                .apply(new RequestOptions().override(300, 300))
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        Toast.makeText(getActivity(), "Error: image URL issue", Toast.LENGTH_LONG).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(episodeImageView);

        String summaryString = currentEpisode.getSummary();
        summaryView.setText(summaryString);

        return rootView;
    }
}