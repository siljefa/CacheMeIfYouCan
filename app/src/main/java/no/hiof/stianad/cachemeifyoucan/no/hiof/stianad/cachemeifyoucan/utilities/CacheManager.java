package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.utilities;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.models.Cache;

public final class CacheManager
{

    private static HashMap<Integer, Cache> caches = new HashMap<>();
    private CacheManager()
    {

    }

    public static Cache createCache(LatLng latLng, String description, String name, int difficulty)
    {
        Cache newCache = new Cache(latLng, description, name, difficulty, caches.size()+1);
        caches.put((caches.size()+1), newCache);
        addCacheToDatabase(newCache);
        return  newCache;
    }

    public static HashMap<Integer, Cache> getCaches()
    {
        return caches;
    }

    private static void addCacheToDatabase(Cache cache)
    {
        //firebase refference and instance
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseCache = database.getReference("cache");
        String cacheId = databaseCache.push().getKey();
        databaseCache.child(cacheId).setValue(cache);
    }
}
