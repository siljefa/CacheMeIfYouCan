package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import no.hiof.stianad.cachemeifyoucan.R;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.adapters.AchievementsAdapter;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.models.Achievement;

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
        /*newAchievements.add(new Achievement("10k", "walk 10000 metres"));
        newAchievements.add(new Achievement("20k", "walk 20000 metres"));
        newAchievements.add(new Achievement("30k", "walk 30000 metres"));
        */

        newAchievements = FillArrayListWithAchievements();

        adapter.addAll(newAchievements);
    }

    private ArrayList<Achievement> FillArrayListWithAchievements(){
        ArrayList<Achievement> list = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        ref.child("achievement").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child: children) {
                    Achievement achievement = child.getValue(Achievement.class);
                    //adding achievements to list
                    list.add(achievement);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return list;
    }
}
