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
import android.widget.CompoundButton;
import android.widget.Switch;

import no.hiof.stianad.cachemeifyoucan.R;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.activities.MainActivity;

public class FilterCacheFragment extends Fragment
{
    private boolean filterFoundCache = false;
    private boolean filterLocation = false;
    private boolean filterDifficulty = false;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.filter_cache_fragment, container, false);

        Switch filterFoundCacheSwitch = view.findViewById(R.id.filterFoundCacheSwitch);
        filterFoundCacheSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            if(isChecked)
            {
                filterFoundCache = false;
            }
            else
            {
                filterFoundCache = true;
            }
        });

        Switch filterLocationSwitch = view.findViewById(R.id.filterLocationSwitch);
        filterLocationSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            if(isChecked)
            {
                filterLocation = true;
            }
            else
            {
                filterLocation = false;
            }
        });

        Switch filterDifficultySwitch = view.findViewById(R.id.filterDifficultySwitch);
        filterDifficultySwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            if(isChecked)
            {
                filterDifficulty = false;
            }
            else
            {
                filterDifficulty = true;
            }
        });

        return view;
}

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if(hidden)
        {
            FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
            MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentByTag("map_fragment");
            mapFragment.setFilters(filterFoundCache, filterLocation, filterDifficulty);
        }
    }
}
