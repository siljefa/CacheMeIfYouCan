package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import android.location.Location;
import android.location.LocationListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import no.hiof.stianad.cachemeifyoucan.R;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.utilities.CacheManager;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.models.User;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.activities.MainActivity;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.models.Cache;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.utilities.BoundingBox;

public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener
{
    private MainActivity parentActivity;
    private GoogleMap gMap;
    private boolean mapReady = false;
    private LatLng lastPositionUpdate;

    //hashMap to hold caches on the map, and connected marker.
    private HashMap<String, Integer> cacheMarkersOnMap = new HashMap<>();
    private Marker selectedCacheMarker;
    private boolean filterFoundCache = false;
    private boolean filterLocation = false;
    private boolean filterDifficulty = false;
    private CacheBottomSheet cacheBottomSheet;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        parentActivity = (MainActivity) getActivity();
        //SupportMapFragment handles the life cycle of a google map.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null)
        {
            // To acquire a GoogleMap in onMapReady.
            mapFragment.getMapAsync(this);
        }
        //Get a custom BottomSheet made for caches.
        cacheBottomSheet = new CacheBottomSheet(view, parentActivity);


        //region TestCaches
        /*Cache cache1 = CacheManager.createCache(new LatLng(37.42, -122.07), "Hello", "Some Name", 2);
        Cache cache2 = CacheManager.createCache(new LatLng(37.47, -122.07), "Hello", "Some Name", 3);
        Cache cache3 = CacheManager.createCache(new LatLng(37.62, -122.07), "Hello", "Some Name", 4);
        CacheManager.createCache(new LatLng(37.72, -122.07), "Hello", "Some Name", 5);
        CacheManager.createCache(new LatLng(38.82, -122.07), "Hello", "Some Name", 6);
        ArrayList<Integer> list  = User.getCacheIds();
        list.add(cache1.getCacheId());
        list.add(cache2.getCacheId());
        list.add(cache3.getCacheId());*/
        //endregion

        CacheManager.setEventListener();
        setBottomSheetButtonListeners();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        gMap = googleMap;
        googleMap.setOnMapLongClickListener(latLng ->
        {
            //Long click is used for crating new CacheManager.
            addMarker(latLng, "");
            cacheBottomSheet.openSheetInEditMode(latLng);
        });

        googleMap.setOnMarkerClickListener(marker ->
        {
            //Open cache info sheet
            try
            {
                Integer cacheId = cacheMarkersOnMap.get(marker.getId());
                Cache cache = CacheManager.getCaches().get(Objects.requireNonNull(cacheId));
                cacheBottomSheet.openSheetInViewMode(Objects.requireNonNull(cache));
                selectedCacheMarker = marker;
                return true;
            }
            catch (Exception e)
            {
                return false;
            }
        });

        googleMap.setOnMapClickListener(latLng ->
        {
            //Click on map should close sheet.
            if ((cacheBottomSheet.getLastSheetState() == BottomSheetBehavior.STATE_EXPANDED) || (cacheBottomSheet.getLastSheetState() == BottomSheetBehavior.STATE_HALF_EXPANDED))
            {
                //Hidden first because the BottomSheetBehavior thinks it's collapsed.
                cacheBottomSheet.setSheetState(BottomSheetBehavior.STATE_HIDDEN);
                cacheBottomSheet.setSheetState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            else if (cacheBottomSheet.getLastSheetState() == BottomSheetBehavior.STATE_COLLAPSED)
            {
                cacheBottomSheet.setSheetState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        setUpDefaultUISettings();
        mapReady = true;
    }
    /*
        When map is ready and location is updated, load caches around location.
     */
    private void onFirstLocation(LatLng latLon)
    {
        gMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLon, 10, 0, 0)));
        gMap.animateCamera(CameraUpdateFactory.newLatLng(latLon), 2000, null);

        LatLngBounds testBounds = new BoundingBox(latLon, 1000).getBoundingBox();

        filterCaches();

        /*for (Map.Entry<Integer, Cache> e : CacheManager.getCaches().entrySet())
        {
            Integer cacheId = e.getKey();
            Cache cache = e.getValue();
            addMarker(testBounds.northeast, "northeast BoundingBox");
            addMarker(testBounds.southwest, "southwest BoundingBox");


            if (testBounds.contains(cache.getLatLng()))
            {
                if(!User.getCacheIds().contains(cacheId))
                {
                    Marker newMarker = gMap.addMarker(new MarkerOptions().position(cache.getLatLng()).title("Cache ved Fredrikstad Kino"));
                    cacheMarkersOnMap.put(newMarker.getId(), cacheId);
                }
            }
        }*/
    }

    private void filterCaches()
    {
        cacheMarkersOnMap = new HashMap<>();
        LatLngBounds testBounds = new BoundingBox(lastPositionUpdate, 10000).getBoundingBox();
        for (Map.Entry<Integer, Cache> e : CacheManager.getCaches().entrySet())
        {
            Marker newMarker = null;
            Integer cacheId = e.getKey();
            Cache cache = e.getValue();
            if (filterLocation)
            {
                if(testBounds.contains(cache.getLatLng()))
                {
                    newMarker = addMarker(cache.getLatLng(),"2222 RRR");
                    cacheMarkersOnMap.put(newMarker.getId(),cacheId);
                }
            }
            else
            {
                newMarker = addMarker(cache.getLatLng(),"1111 RRR");
                cacheMarkersOnMap.put(newMarker.getId(),cacheId);
            }
            if (filterFoundCache)
            {
                if(User.getCacheIds().contains(cacheId) && cacheMarkersOnMap.containsValue(cacheId))
                {
                    cacheMarkersOnMap.remove(newMarker.getId());
                    selectedCacheMarker.remove();
                }
            }
            if (filterDifficulty)
            {
            }
        }
    }

    private void setBottomSheetButtonListeners()
    {
        cacheBottomSheet.setWeatherBtnOnClickListener(v ->
        {
            WeatherFragment weatherFragmentWithBundle = new WeatherFragment();
            Bundle args = new Bundle();
            args.putDouble("latitude", selectedCacheMarker.getPosition().latitude);
            args.putDouble("longitude", selectedCacheMarker.getPosition().longitude);
            weatherFragmentWithBundle.setArguments(args);

            FragmentManager fragmentManager = (parentActivity).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment fragment = fragmentManager.findFragmentByTag("map_fragment");
            if (fragment instanceof MapFragment)
            {
                fragmentTransaction.hide(fragment);
            }

            fragmentTransaction.add(R.id.mainLayout, weatherFragmentWithBundle, "weather_Fragment");
            fragmentTransaction.show(weatherFragmentWithBundle);
            fragmentTransaction.commit();
            parentActivity.showBackButton(true);
            parentActivity.setToolbarColored(false);
            parentActivity.setToolbarBackIconDown(false);
        });

        cacheBottomSheet.setFoundCacheBtnOnClickListener(v ->
        {
            User.getCacheIds().add(cacheMarkersOnMap.get(selectedCacheMarker.getId()));
            cacheMarkersOnMap.remove(selectedCacheMarker.getId());
            selectedCacheMarker.remove();
            cacheBottomSheet.setSheetState(BottomSheetBehavior.STATE_HIDDEN);
        });

        //Save a new cache to database with the information present in the BottomSheet.
        cacheBottomSheet.setSaveCacheBtnOnClickListener(v ->
        {
            String cDescription = cacheBottomSheet.getEditTextDescription().getText().toString();
            String cName = cacheBottomSheet.getEditTextName().getText().toString();
            String cLat = cacheBottomSheet.getEditTextLat().getText().toString();
            String cLon = cacheBottomSheet.getEditTextLon().getText().toString();

            //Test if the location field has value.
            if (cLat.length() > 0 && cLon.length() > 0)
            {
                LatLng latLng = new LatLng(Double.parseDouble(cLat), Double.parseDouble(cLon));
                Cache newCache = CacheManager.createCache(latLng, cDescription, cName, 2);

                //If the user has changed the cache location after placing the marker, move the marker.
                if (selectedCacheMarker.getPosition() != latLng)
                {
                    selectedCacheMarker.setPosition(latLng);
                }
                cacheMarkersOnMap.put(selectedCacheMarker.getId(), newCache.getCacheId());
                cacheBottomSheet.setSheetState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            else
            {
                Toast.makeText(parentActivity, Objects.requireNonNull(parentActivity).getString(R.string.toast_save_cache_failed), Toast.LENGTH_LONG).show();
                cacheBottomSheet.setSheetState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            }

        });
    }

    public Marker addMarker(LatLng latLng, String title)
    {
        selectedCacheMarker = gMap.addMarker(new MarkerOptions().position(latLng).title(title));
        return selectedCacheMarker;
    }

    /*
        Disable most mapUI
     */
    private void setUpDefaultUISettings()
    {
        UiSettings uiSettings = gMap.getUiSettings();
        uiSettings.setCompassEnabled(false);
        uiSettings.setTiltGesturesEnabled(false);
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setMapToolbarEnabled(false);
    }

    @Override
    public void onLocationChanged(Location location)
    {
        if (mapReady)
        {
            onFirstLocation(new LatLng(location.getLatitude(), location.getLongitude()));
        }
        lastPositionUpdate = new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }

    @Override
    public void onProviderEnabled(String provider)
    {
    }

    @Override
    public void onProviderDisabled(String provider)
    {
    }

    public void collapseSheet()
    {
        cacheBottomSheet.setSheetState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void closeSheet()
    {
        cacheBottomSheet.setSheetState(BottomSheetBehavior.STATE_HIDDEN);
    }

    public boolean isExpandedSheet()
    {
        return cacheBottomSheet.getLastSheetState() == BottomSheetBehavior.STATE_EXPANDED;
    }
}