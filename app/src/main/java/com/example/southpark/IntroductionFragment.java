package com.example.southpark;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.southpark.model.Episode;

import java.util.ArrayList;

public class IntroductionFragment extends Fragment {

    private ArrayList<String> seriesData;
    private ArrayList<Episode> episodesList;
    private FragmentTransaction ft;
    private String imageString, summaryString;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_introduction, container, false);

        final Bundle bundle = getArguments();

        if (bundle != null) {
            seriesData = bundle.getStringArrayList("seriesData");
            episodesList = bundle.getParcelableArrayList("episodesList");
        }
        if (seriesData != null) {
            imageString = seriesData.get(0);
            summaryString = seriesData.get(1);
        }

        ImageView imageView = rootView.findViewById(R.id.seriesImageId);

        Glide.with(IntroductionFragment.this)
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
                .into(imageView);

        TextView summaryView = rootView.findViewById(R.id.summaryId);
        summaryView.setText(summaryString);

        Button toListButton = rootView.findViewById(R.id.goToListBtn);
        toListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment elf = new EpisodesListFragment();

                Bundle episodeBundle = new Bundle();
                episodeBundle.putParcelableArrayList("episodesList", episodesList);
                elf.setArguments(episodeBundle);

                if (getFragmentManager() != null) {
                    ft = getFragmentManager().beginTransaction();
                }
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.replace(R.id.fragments_container,elf);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return rootView;
    }
}