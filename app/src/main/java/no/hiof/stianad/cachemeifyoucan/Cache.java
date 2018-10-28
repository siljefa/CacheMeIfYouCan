package no.hiof.stianad.cachemeifyoucan;

import com.google.android.gms.maps.model.LatLng;

public class Cache
{
    private String name;
    private LatLng latLng;
    private String description;
    private int difficulty;


    public Cache(LatLng latLng, String description, String name, int difficulty)
    {
        this.name = name;
        this.latLng = latLng;
        this.description = description;
        this.difficulty = difficulty;
    }

}
