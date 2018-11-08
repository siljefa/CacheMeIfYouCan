package no.hiof.stianad.cachemeifyoucan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class AchievementsFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_achievements, container, false);

        //

        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        AttachCustomAdapterToListView();
    }

    private void AttachCustomAdapterToListView() {
        ArrayList<Achievement> achievements = new ArrayList<>();

        AchievementsAdapter adapter = new AchievementsAdapter(this.getContext(), achievements);

        ListView listView = this.getView().findViewById(R.id.listView_achievements);

        listView.setAdapter(adapter);

        ArrayList<Achievement> newAchievements = new ArrayList<>();
        Achievement ach1 = new Achievement("10k", "walk 10000 metres");
        Achievement ach2 = new Achievement("20k", "walk 20000 metres");
        Achievement ach3 = new Achievement("30k", "walk 30000 metres");
        Achievement ach4 = new Achievement("40k", "walk 40000 metres");

        newAchievements.add(ach1);
        newAchievements.add(ach2);
        newAchievements.add(ach3);
        newAchievements.add(ach4);

        adapter.addAll(newAchievements);
    }
}
