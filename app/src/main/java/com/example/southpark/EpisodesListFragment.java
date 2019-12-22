package com.example.southpark;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.southpark.model.Episode;
import com.example.southpark.sqlite_access.GetDatabase;

import java.util.ArrayList;

public class EpisodesListFragment extends Fragment implements EpisodesAdapter.ItemClickListener {

    private Episode currentEpisode;
    private GetDatabase getDatabase;

    private Context context;

    private ArrayList<Episode> episodesList;

    private EpisodesAdapter adapter;
    private RecyclerView rv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_episodes, container, false);

        context = getActivity();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        adapter = new EpisodesAdapter(getActivity(), this);
        adapter.setClickListener(this);

        rv = rootView.findViewById(R.id.episodesRVId);
        rv.setLayoutManager(linearLayoutManager);
        rv.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(context, linearLayoutManager.getOrientation());

        rv.addItemDecoration(dividerItemDecoration);
        rv.setAdapter(adapter);

        Bundle listBundle = getArguments();
        if(listBundle != null){
            episodesList = getArguments().getParcelableArrayList("episodesList");
            adapter.attachList(episodesList);
        }
        return rootView;
    }

    void setCurrentEpisode(Episode currentEpisode) {
        this.currentEpisode = currentEpisode;
        getDatabase = new GetDatabase(context);
    }

    @Override
    public void onItemClick(Episode episode) {

        EpisodeFragment episodeFragment = new EpisodeFragment();
        if (getFragmentManager() != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putParcelable("currentEpisode", getCurrentEpisode());
//        bundle.putParcelableArrayList("episodesList", episodesList);
            episodeFragment.setArguments(bundle);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.replace(R.id.fragments_container, episodeFragment);
            ft.addToBackStack(null).commit();
        }
    }

    private Episode getCurrentEpisode() {
        return currentEpisode;
    }
}