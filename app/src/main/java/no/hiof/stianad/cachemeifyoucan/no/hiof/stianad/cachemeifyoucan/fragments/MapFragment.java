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

import android.location.Location;
import android.location.LocationListener;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import no.hiof.stianad.cachemeifyoucan.R;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.utilities.CacheManager;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.activities.MainActivity;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.models.Cache;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.utilities.BoundingBox;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.utilities.UserManager;

public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener
{
    private MainActivity parentActivity;
    private GoogleMap gMap;
    private boolean mapReady = false;
    private boolean isFirstLocation = true;
    private LatLng lastPositionUpdate = null;

    //hashMap to hold caches on the map, and connected marker.
    private HashMap<String, Integer> cacheMarkersOnMap = new HashMap<>();
    private Marker selectedCacheMarker;
    private boolean filterFoundCache = true;
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

        CacheManager.setEventListener(this);
        setBottomSheetButtonListeners();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        gMap = googleMap;
        mapReady = true;
        setUpDefaultUISettings();
        parentActivity.requestLocationUpdates();

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
                Cache cache = CacheManager.getCaches().get(cacheId);
                cacheBottomSheet.openSheetInViewMode(cache);
                selectedCacheMarker = marker;
            } catch (NullPointerException e)
            {
                Toast.makeText(parentActivity, parentActivity.getString(R.string.toast_failed_to_find_cache), Toast.LENGTH_LONG).show();
            }
            return true;
        });

        googleMap.setOnMapClickListener(latLng ->
        {
            //Click on map should close sheet.
            if ((cacheBottomSheet.getLastSheetState() == BottomSheetBehavior.STATE_EXPANDED) || (cacheBottomSheet.getLastSheetState() == BottomSheetBehavior.STATE_HALF_EXPANDED))
            {
                //Hidden first because the BottomSheetBehavior thinks it's collapsed.
                cacheBottomSheet.setSheetState(BottomSheetBehavior.STATE_HIDDEN);
                cacheBottomSheet.setSheetState(BottomSheetBehavior.STATE_COLLAPSED);
            } else if (cacheBottomSheet.getLastSheetState() == BottomSheetBehavior.STATE_COLLAPSED)
            {
                cacheBottomSheet.setSheetState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
    }

    private void filterCaches()
    {
        if (lastPositionUpdate != null && mapReady)
        {
            //Map clear removes everything, should remove each marker form a list fo markers.
            gMap.clear();
            cacheMarkersOnMap = new HashMap<>();
            LatLngBounds testBounds = new BoundingBox(lastPositionUpdate, 10).getBoundingBox();
            for (Map.Entry<Integer, Cache> e : CacheManager.getCaches().entrySet())
            {
                Marker newMarker = null;
                Integer cacheId = e.getKey();
                Cache cache = e.getValue();
                if (filterLocation)
                {
                    if (testBounds.contains(cache.getLatLng()))
                    {
                        newMarker = addMarker(cache.getLatLng(), "");
                        cacheMarkersOnMap.put(newMarker.getId(), cacheId);
                    }
                } else
                {
                    newMarker = addMarker(cache.getLatLng(), "");
                    cacheMarkersOnMap.put(newMarker.getId(), cacheId);
                }
                if (filterFoundCache)
                {
                    if (UserManager.getFoundCacheIds().contains(cacheId) && cacheMarkersOnMap.containsValue(cacheId))
                    {
                        cacheMarkersOnMap.remove(newMarker.getId());
                        newMarker.remove();
                    }
                }
                if (filterDifficulty)
                {
                }
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

            fragmentTransaction.add(R.id.mainLayout, weatherFragmentWithBundle, "weather_fragment");
            fragmentTransaction.show(weatherFragmentWithBundle);
            fragmentTransaction.commit();
            parentActivity.showBackButton(true);
            parentActivity.setToolbarColored(false);
            parentActivity.setToolbarBackIconDown(false);
        });

        cacheBottomSheet.setFoundCacheBtnOnClickListener(v ->
        {
            UserManager.userFoundCacheListAdd(cacheMarkersOnMap.get(selectedCacheMarker.getId()));
            if (filterFoundCache)
            {
                cacheMarkersOnMap.remove(selectedCacheMarker.getId());
                selectedCacheMarker.remove();
            }
            cacheBottomSheet.setSheetState(BottomSheetBehavior.STATE_HIDDEN);
        });

        //Save a new cache to database with the information present in the BottomSheet.
        cacheBottomSheet.setSaveCacheBtnOnClickListener(v ->
        {
            String cDescription = cacheBottomSheet.getEditTextDescription().getText().toString();
            String cName = cacheBottomSheet.getEditTextName().getText().toString();
            String cLat = cacheBottomSheet.getEditTextLat().getText().toString();
            String cLon = cacheBottomSheet.getEditTextLon().getText().toString();
            String cCreator = cacheBottomSheet.getEditTextCreator().getText().toString();

            //Test if the location field has value.
            if (cLat.length() > 0 && cLon.length() > 0)
            {
                LatLng latLng = new LatLng(Double.parseDouble(cLat), Double.parseDouble(cLon));
                Cache newCache = CacheManager.createCache(latLng, cDescription, cName, 2, cCreator);

                //If the user has changed the cache location after placing the marker, move the marker.
                if (selectedCacheMarker.getPosition() != latLng)
                {
                    selectedCacheMarker.setPosition(latLng);
                }
                cacheMarkersOnMap.put(selectedCacheMarker.getId(), newCache.getCacheId());
                cacheBottomSheet.setSheetState(BottomSheetBehavior.STATE_COLLAPSED);
                UserManager.userCreatedCacheListAdd(newCache.getCacheId());
            }
            else
            {
                Toast.makeText(parentActivity, parentActivity.getString(R.string.toast_save_cache_failed), Toast.LENGTH_LONG).show();
                cacheBottomSheet.setSheetState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            }
        });
    }

    private Marker addMarker(LatLng latLng, String title)
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

    /*
       When map is ready and location is updated move camera and apply filter for caches.
    */
    private void onFirstLocation()
    {
        gMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(lastPositionUpdate, 10, 0, 0)));
        gMap.animateCamera(CameraUpdateFactory.newLatLng(lastPositionUpdate), 2000, null);
        filterCaches();
    }

    public void updateCachesOnMap()
    {
        filterCaches();
    }

    public void setCacheFilters(boolean filterFoundCache, boolean filterLocation, boolean filterDifficulty)
    {
        this.filterFoundCache = filterFoundCache;
        this.filterLocation = filterLocation;
        this.filterDifficulty = filterDifficulty;
        filterCaches();
    }

    @Override
    public void onLocationChanged(Location location)
    {
        lastPositionUpdate = new LatLng(location.getLatitude(), location.getLongitude());
        if (isFirstLocation)
        {
            onFirstLocation();
            isFirstLocation = false;
        }
        else
        {
            filterCaches();
        }
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

    public interface IMapFragment
    {
        void setToolbarBackIconDown(boolean down);

        void setToolbarColored(boolean addColor);

        void showBackButton(boolean showButton);
    }
}