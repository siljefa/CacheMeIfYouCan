package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;

import java.lang.annotation.Annotation;

public class Cache implements Annotation
{
    private String name;
    private double latitude;
    private double longitude;
    private String description = "";
    private String creator = "";
    private int difficulty;
    private int cacheId;

    public Cache(LatLng latLng, String description, String name, int difficulty, int cacheId, String creator)
    {
        this.name = name;
        this.latitude = latLng.latitude;
        this.longitude = latLng.longitude;
        this.description = description;
        this.difficulty = difficulty;
        this.cacheId = cacheId;
        this.creator = creator;
    }

    //Empty constructor needed for database.
    public Cache()
    {

    }

    public String getCreator()
    {
        return creator;
    }

    public void setCreator(String creator)
    {
        this.creator = creator;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setDifficulty(int difficulty)
    {
        this.difficulty = difficulty;
    }

    public void setCacheId(int cacheId)
    {
        this.cacheId = cacheId;
    }

    public String getName()
    {
        return name;
    }

    public int getCacheId()
    {
        return cacheId;
    }

    @Exclude
    public LatLng getLatLng()
    {
        return new LatLng(latitude, longitude);
    }

    public String getDescription()
    {
        return description;
    }

    public int getDifficulty()
    {
        return difficulty;
    }

    @Override
    public Class<? extends Annotation> annotationType()
    {
        return null;
    }
}
