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
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.models.User;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.utilities.UserManager;

public class AchievementsFragment extends Fragment
{
    /**
     * Contains the achievement objects to be shown on screen
     */
    public static ArrayList<Achievement> achievements = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_achievements, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        AttachCustomAdapterToListView();
    }

    private void AttachCustomAdapterToListView()
    {

        AchievementsAdapter adapter = new AchievementsAdapter(this.getContext(), achievements);

        ListView listView = this.getView().findViewById(R.id.listView_achievements);

        listView.setAdapter(adapter);

        FillArrayListWithAchievements();

        adapter.addAll(achievements);
    }

    private void FillArrayListWithAchievements()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        ref.child("achievement").addListenerForSingleValueEvent(new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot achievementSnapshot : dataSnapshot.getChildren())
                {
                    if(UserManager.getAchievementsIds().contains(achievementSnapshot.getValue(Achievement.class).id ))
                    {
                        AchievementsFragment.achievements.add(achievementSnapshot.getValue(Achievement.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                //TODO: Fix some error handling bro
            }
        });
    }
}

