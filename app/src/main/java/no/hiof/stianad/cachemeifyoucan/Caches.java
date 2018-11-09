package no.hiof.stianad.cachemeifyoucan;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    public static Cache createCashe(LatLng latLng, String description, String name, int difficulty)
    {
        Cache newCache = new Cache(latLng, description, name, difficulty);
        caches.put((caches.size()+1), newCache);
        addCacheToDatabse(newCache);
        return  newCache;
    }

    private static void addCacheToDatabse(Cache cache)
    {
        //firebase refference and instance
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseCache = database.getReference("cache");
        String cacheId = databaseCache.push().getKey();
        databaseCache.child(cacheId).setValue(cache);

    }
}
