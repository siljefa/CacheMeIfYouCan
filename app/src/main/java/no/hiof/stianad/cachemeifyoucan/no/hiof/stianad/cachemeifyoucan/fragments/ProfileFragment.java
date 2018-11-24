package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import no.hiof.stianad.cachemeifyoucan.R;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.models.User;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.utilities.UserManager;

public class ProfileFragment  extends Fragment{

    private View thisView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        thisView = view;
        return view;
    }

    //Get User info when profile fragment is visible.
    //Also an attempt at exception handling
    @Override
    public void onHiddenChanged(boolean hidden)
    {
        if(!hidden)
        {
            try
            {
                TextView nameText = thisView.findViewById(R.id.name);
                nameText.setText(UserManager.getName());
            }
            catch (IllegalArgumentException e)
            {
                Toast.makeText(getActivity(), "Failed to find user", Toast.LENGTH_LONG).show();
            }
        }
    }
}
