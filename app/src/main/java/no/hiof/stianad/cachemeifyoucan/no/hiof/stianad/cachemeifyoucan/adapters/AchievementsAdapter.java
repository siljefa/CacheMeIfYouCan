package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.models.Achievement;
import no.hiof.stianad.cachemeifyoucan.R;

public class AchievementsAdapter extends ArrayAdapter<Achievement>
{
    public AchievementsAdapter(Context context, ArrayList<Achievement> achievements)
    {
        super(context, 0, achievements);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        Achievement achievement = getItem(position);

        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_achievement, parent, false);
        }

        TextView textView_name = convertView.findViewById(R.id.textView_name);
        TextView textView_description = convertView.findViewById(R.id.textView_description);

        assert achievement != null;
        textView_name.setText(achievement.name);
        textView_description.setText(achievement.description);

        return convertView;
    }
}