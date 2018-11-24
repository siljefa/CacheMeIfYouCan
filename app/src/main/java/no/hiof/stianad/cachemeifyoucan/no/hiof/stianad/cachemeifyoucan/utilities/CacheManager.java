package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.utilities;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.models.Cache;

public final class CacheManager
{

    private static HashMap<Integer, Cache> caches = new HashMap<>();
    private CacheManager()
    {

    }

    public static Cache createCache(LatLng latLng, String description, String name, int difficulty, String creator)
    {
        int cacheId = caches.size()+1;
        while(true)
        {
            if (!caches.containsKey(cacheId))
            {
                Cache newCache = new Cache(latLng, description, name, difficulty, caches.size() + 1, creator);
                caches.put((cacheId), newCache);
                addCacheToDatabase(newCache);
                return newCache;
            }
            else
                cacheId++;
        }
    }

    public static HashMap<Integer, Cache> getCaches()
    {
        return caches;
    }

    public static void setEventListener(no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.fragments.MapFragment map)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("cache");
        ref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot != null)
                {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        Cache value = snapshot.getValue(Cache.class);
                        caches.put(value.getCacheId(), value);
                        map.updateCachesOnMap();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error)
            {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private static void addCacheToDatabase(Cache cache)
    {
        //FireBase reference and instance
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseCache = database.getReference("cache");
        String cacheId = databaseCache.push().getKey();
        databaseCache.child(cacheId).setValue(cache);
    }

}
