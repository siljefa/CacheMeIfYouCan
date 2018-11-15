package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.models;

import com.google.android.gms.maps.model.LatLng;

public class Cache
{
    private String name;
    private LatLng latLng;
    private String description;
    private int difficulty;
    private int cacheId;


    public Cache(LatLng latLng, String description, String name, int difficulty, int cacheId)
    {
        this.name = name;
        this.latLng = latLng;
        this.description = description;
        this.difficulty = difficulty;
        this.cacheId = cacheId;
    }

    //Empty constructor needed for database.
    public Cache()
    {

    }

    public String getName()
    {
        return name;
    }

    public int getCacheId()
    {
        return cacheId;
    }

    public LatLng getLatLng()
    {
        return latLng;
    }

    public String getDescription()
    {
        return description;
    }

    public int getDifficulty()
    {
        return difficulty;
    }
}
