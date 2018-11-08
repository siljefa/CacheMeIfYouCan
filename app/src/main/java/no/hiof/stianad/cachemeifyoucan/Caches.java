package no.hiof.stianad.cachemeifyoucan;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public final class Caches
{
    private static HashMap<Integer, Cache> caches = new HashMap<>();
    private Caches()
    {

    }
    public static HashMap<Integer, Cache> getCaches()
    {
        return caches;
    }

    public static Cache createCache(LatLng latLng, String description, String name, int difficulty)
    {
        Cache newCache = new Cache(latLng, description, name, difficulty);
        caches.put((caches.size()+1), newCache);
        return  newCache;
    }

        private static void addCacheToDatabse(Cache cache)
        {

        }
}
