package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import no.hiof.stianad.cachemeifyoucan.R;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.activities.MainActivity;

public class FilterCacheFragment extends Fragment
{
    private boolean filterFoundCache = false;
    private boolean filterLocation = false;
    private boolean filterDifficulty = false;
    Button filterFoundCacheBtn;
    Button filterLocationBtn;
    Button filterDifficultyBtn;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.filter_cache_fragment, container, false);

        filterFoundCacheBtn = view.findViewById(R.id.filterFoundCacheButton);
        filterFoundCacheBtn.setOnClickListener(v ->
        {
            if(filterFoundCache)
            {
                filterFoundCache = false;
                filterFoundCacheBtn.setBackgroundColor(0x00ff00);
            }
            else
            {
                filterFoundCache = true;
                filterFoundCacheBtn.setBackgroundColor(0xFFFF0000);
            }

        });

        filterLocationBtn = view.findViewById(R.id.filterLocationForCacheBtn);
        filterLocationBtn.setOnClickListener(v ->
        {

            if(filterLocation)
            {
                filterLocation = false;
                filterLocationBtn.setBackgroundColor(0x00ff00);
            }
            else
            {
                filterLocation = true;
                filterLocationBtn.setBackgroundColor(0xFFFF0000);
            }

        });

        filterDifficultyBtn = view.findViewById(R.id.filterDifficultyOfCacheBtn);
        filterDifficultyBtn.setOnClickListener(v ->
        {
            if(filterDifficulty)
            {
                filterDifficulty = false;
                filterDifficultyBtn.setBackgroundColor(0x00ff00);
            }
            else
            {
                filterDifficulty = true;
                filterDifficultyBtn.setBackgroundColor(0xFFFF0000);
            }
        });

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if(hidden)
        {
            FragmentManager fragmentManager = ((MainActivity) getActivity()).getSupportFragmentManager();
            MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentByTag("map_fragment");
            mapFragment.setFilters(filterFoundCache, filterLocation, filterDifficulty);
        }
    }
}
