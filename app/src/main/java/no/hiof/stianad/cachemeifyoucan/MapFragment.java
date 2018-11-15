package no.hiof.stianad.cachemeifyoucan;

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
import java.util.Objects;


public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener
{
    private MainActivity parentActivity;
    private GoogleMap gMap;
    private boolean mapReady = false;
    //hashMap to hold caches on the map, and connected marker.
    private HashMap<String, Integer> cacheMarkersOnMap = new HashMap<>();
    private Marker selectedCacheMarker;

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

        Caches.createCashe(new LatLng(37.42, -122.07), "Hello", "Some Name", 2);
        Caches.createCashe(new LatLng(37.47, -122.07), "Hello", "Some Name", 3);
        Caches.createCashe(new LatLng(37.62, -122.07), "Hello", "Some Name", 4);
        Caches.createCashe(new LatLng(37.72, -122.07), "Hello", "Some Name", 5);
        Caches.createCashe(new LatLng(38.82, -122.07), "Hello", "Some Name", 6);



        //region BottomSheetButtons setOnClickListeners
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

        cacheBottomSheet.setFoundCacheBtnOnClickListener(v -> cacheBottomSheet.setSheetState(BottomSheetBehavior.STATE_HIDDEN));

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
                Cache newCache = Caches.createCashe(latLng, cDescription, cName, 2);

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
        //endregion
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        gMap = googleMap;
        googleMap.setOnMapLongClickListener(latLng ->
        {
            //Long click is used for crating new Caches.
            addMarker(latLng, "");
            cacheBottomSheet.openSheetInEditMode(latLng);
        });

        googleMap.setOnMarkerClickListener(marker ->
        {
            //Open cache info sheet
            try
            {
                Integer cacheId = cacheMarkersOnMap.get(marker.getId());
                Cache cache = Caches.getCaches().get(Objects.requireNonNull(cacheId));
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

        HashMap<Integer, Cache> testCaches = Caches.getCaches();
        LatLngBounds testBounds = new BoundingBox(latLon, 10).getBoundingBox();

        for (Map.Entry<Integer, Cache> e : testCaches.entrySet())
        {
            Integer cacheId = e.getKey();
            Cache cache = e.getValue();
            addMarker(testBounds.northeast, "northeast BoundingBox");
            addMarker(testBounds.southwest, "southwest BoundingBox");

            if (testBounds.contains(cache.getLatLng()))
            {
                Marker newMarker = gMap.addMarker(new MarkerOptions().position(cache.getLatLng()).title("Cache ved Fredrikstad Kino"));
                cacheMarkersOnMap.put(newMarker.getId(), cacheId);
            }
        }
    }

    public void addMarker(LatLng latLng, String title)
    {
        selectedCacheMarker = gMap.addMarker(new MarkerOptions().position(latLng).title(title));
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
