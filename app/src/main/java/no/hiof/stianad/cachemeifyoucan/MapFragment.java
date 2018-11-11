package no.hiof.stianad.cachemeifyoucan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, LocationListener
{
    private MainActivity parentActivity;
    private GoogleMap gMap;
    private boolean mapReady = false;
    private HashMap<String, Integer> markersOnMap = new HashMap<>();
    private Marker selectedCacheMarker;

    private BottomSheetBehavior sheetBehavior;
    private int lastSheetState;
    private boolean isChangingSheetState = false;
    private View fillerSpaceForToolbar;
    private Button foundCacheBtn, saveCacheBtn, weatherBtn;
    private EditText editTextLat, editTextLon, editTextDescription, editTextName;

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
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null)
        {
            mapFragment.getMapAsync(this);
        }

        View cacheBottomSheet = view.findViewById(R.id.cache_bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(cacheBottomSheet);
        fillerSpaceForToolbar = cacheBottomSheet.findViewById(R.id.filler);
        editTextLat = cacheBottomSheet.findViewById(R.id.lat_edit);
        editTextLon = cacheBottomSheet.findViewById(R.id.lon_edit);
        editTextDescription = cacheBottomSheet.findViewById(R.id.description_edit);
        editTextName = cacheBottomSheet.findViewById(R.id.name_edit);
        foundCacheBtn = cacheBottomSheet.findViewById(R.id.foundCacheBtn);
        saveCacheBtn = cacheBottomSheet.findViewById(R.id.saveCacheBtn);
        weatherBtn = cacheBottomSheet.findViewById(R.id.weatherBtn);

        setUpCustomSheetBehavior();

        Caches.createCashe(new LatLng(37.42, -122.07), "Hello", "Some Name", 2);
        Caches.createCashe(new LatLng(37.47, -122.07), "Hello", "Some Name", 3);
        Caches.createCashe(new LatLng(37.62, -122.07), "Hello", "Some Name", 4);
        Caches.createCashe(new LatLng(37.72, -122.07), "Hello", "Some Name", 5);
        Caches.createCashe(new LatLng(38.82, -122.07), "Hello", "Some Name", 6);


        weatherBtn.setOnClickListener(v ->
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
        foundCacheBtn.setOnClickListener(v -> setSheetState(BottomSheetBehavior.STATE_HIDDEN));
        saveCacheBtn.setOnClickListener(v ->
        {
            //Silje was here, tried to make so that description and name from the txt fields are
            // passed as name and description values to the create cache function feel free to
            //comment out and go back to old if its wrong or messes up the code in any way
            String cDescription = editTextDescription.getText().toString();
            String cName = editTextName.getText().toString();
            String cLat = editTextLat.getText().toString();
            String cLon = editTextLon.getText().toString();

            if (cLat.length() > 0 && cLon.length() > 0)
            {
                LatLng latLng = new LatLng(Double.parseDouble(editTextLat.getText().toString()), Double.parseDouble(editTextLon.getText().toString()));
                Cache newCache = Caches.createCashe(latLng, cDescription, cName, 2);

                setSheetState(BottomSheetBehavior.STATE_COLLAPSED);
                if (selectedCacheMarker.getPosition() != latLng)
                {
                    selectedCacheMarker.setPosition(latLng);
                }
                markersOnMap.put(selectedCacheMarker.getId(), newCache.getCacheId());
            }
        });

        editTextLat.setOnFocusChangeListener((v, hasFocus) ->
        {
            if (hasFocus)
                setSheetState(BottomSheetBehavior.STATE_EXPANDED);
        });
        editTextLon.setOnFocusChangeListener((v, hasFocus) ->
        {
            if (hasFocus)
                setSheetState(BottomSheetBehavior.STATE_EXPANDED);
        });
        editTextDescription.setOnFocusChangeListener((v, hasFocus) ->
        {
            if (hasFocus)
                setSheetState(BottomSheetBehavior.STATE_EXPANDED);
        });
        editTextName.setOnFocusChangeListener((v, hasFocus) ->
        {
            if (!hasFocus)
                setSheetState(BottomSheetBehavior.STATE_EXPANDED);
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        gMap = googleMap;
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMapClickListener(this);
        setUpDefaultUISettings();
        mapReady = true;
    }

    private void onFirstLocation(LatLng latLon)
    {
        gMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLon, 10, 0, 0)));
        gMap.animateCamera(CameraUpdateFactory.newLatLng(latLon), 2000, null);

        HashMap<Integer, Cache> testCaches = Caches.getCaches();
        LatLngBounds testBounds = getBoundingBox(latLon, 10);

        for (Map.Entry<Integer, Cache> e : testCaches.entrySet())
        {
            Integer cacheId = e.getKey();
            Cache cache = e.getValue();
            addMarker(testBounds.northeast, "northeast BoundingBox");
            addMarker(testBounds.southwest, "southwest BoundingBox");

            if (testBounds.contains(cache.getLatLng()))
            {
                Marker newMarker = gMap.addMarker(new MarkerOptions().position(cache.getLatLng()).title("Cache ved Fredrikstad Kino"));
                markersOnMap.put(newMarker.getId(), cacheId);
            }
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng)
    {
        addMarker(latLng, "");
        openEditBottomSheet(latLng);
    }

    @Override
    public boolean onMarkerClick(Marker marker)
    {
        Integer cacheId = markersOnMap.get(marker.getId());

        if (cacheId != null)
        {
            Cache cache = Caches.getCaches().get(cacheId);
            openViewBottomSheet(cache);
        }
        selectedCacheMarker = marker;
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng)
    {
        if ((lastSheetState == BottomSheetBehavior.STATE_EXPANDED) || (lastSheetState == BottomSheetBehavior.STATE_HALF_EXPANDED))
        {
            //Hidden first because the BottomSheetBehavior thinks it's collapsed.
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            setSheetState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    public void addMarker(LatLng latLng, String title)
    {
        selectedCacheMarker = gMap.addMarker(new MarkerOptions().position(latLng).title(title));
    }

    private LatLngBounds getBoundingBox(LatLng latLng, double distance)
    {
        // Bounding box surrounding the point at given coordinates,
        // assuming local approximation of Earth surface as a sphere
        // of radius given by WGS84
        // http://en.wikipedia.org/wiki/Earth_radius
        double lat = Math.PI * latLng.latitude / 180.0;
        double lon = Math.PI * latLng.longitude / 180.0;
        double halfSide = 1000 * distance;

        // Radius of Earth at given latitude
        double radius = WGS84EarthRadius(lat);
        // Radius of the parallel at given latitude
        double pradius = radius * Math.cos(lat);

        double latMin = lat - halfSide / radius;
        double latMax = lat + halfSide / radius;
        double lonMin = lon - halfSide / pradius;
        double lonMax = lon + halfSide / pradius;

        LatLng ne = new LatLng(180.0 * latMin / Math.PI, 180.0 * lonMin / Math.PI);
        LatLng sw = new LatLng(180.0 * latMax / Math.PI, 180.0 * lonMax / Math.PI);

        return new LatLngBounds(ne, sw);
    }

    private static double WGS84EarthRadius(double lat)
    {
        double WGS84_a = 6378137.0; // Major semiaxis [m]
        double WGS84_b = 6356752.3; // Minor semiaxis [m]

        // http://en.wikipedia.org/wiki/Earth_radius
        double An = WGS84_a * WGS84_a * Math.cos(lat);
        double Bn = WGS84_b * WGS84_b * Math.sin(lat);
        double Ad = WGS84_a * Math.cos(lat);
        double Bd = WGS84_b * Math.sin(lat);
        return Math.sqrt((An * An + Bn * Bn) / (Ad * Ad + Bd * Bd));
    }

    private void setUpDefaultUISettings()
    {
        UiSettings uiSettings = gMap.getUiSettings();
        uiSettings.setCompassEnabled(false);
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setMapToolbarEnabled(false);
    }

    private void setUpCustomSheetBehavior()
    {
        sheetBehavior.setHideable(true);
        setSheetState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehavior.setPeekHeight(200);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback()
        {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState)
            {
                switch (newState)
                {
                    case BottomSheetBehavior.STATE_DRAGGING:
                    {
                        isChangingSheetState = true;
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset)
            {
                //Change State based on the position on screen
                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_SETTLING && isChangingSheetState)
                {
                    if ((slideOffset > 0.5 && lastSheetState == BottomSheetBehavior.STATE_HALF_EXPANDED) || (slideOffset >= 0.60 && lastSheetState == BottomSheetBehavior.STATE_COLLAPSED))
                    {
                        setSheetState(BottomSheetBehavior.STATE_EXPANDED);
                    } else if ((slideOffset > 0 && lastSheetState == BottomSheetBehavior.STATE_COLLAPSED) || (slideOffset < 1 && lastSheetState == BottomSheetBehavior.STATE_EXPANDED))
                    {
                        setSheetState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                    } else if ((slideOffset < 0.5 && lastSheetState == BottomSheetBehavior.STATE_HALF_EXPANDED) || (slideOffset <= 0.40 && lastSheetState == BottomSheetBehavior.STATE_EXPANDED))
                    {
                        setSheetState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                    isChangingSheetState = false;
                }
            }
        });
    }

    private void setSheetState(int state)
    {
        switch (state)
        {
            case BottomSheetBehavior.STATE_EXPANDED:
            {
                isChangingSheetState = false;
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                lastSheetState = BottomSheetBehavior.STATE_EXPANDED;
                parentActivity.showBackButton(true);
                parentActivity.setToolbarColored(true);
                fillerSpaceForToolbar.setVisibility(View.VISIBLE);
                break;
            }
            case BottomSheetBehavior.STATE_HALF_EXPANDED:
            {
                isChangingSheetState = false;
                sheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                lastSheetState = BottomSheetBehavior.STATE_HALF_EXPANDED;
                parentActivity.showBackButton(false);
                parentActivity.setToolbarColored(false);
                fillerSpaceForToolbar.setVisibility(View.GONE);
                break;
            }
            case BottomSheetBehavior.STATE_COLLAPSED:
            {
                isChangingSheetState = false;
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                lastSheetState = BottomSheetBehavior.STATE_COLLAPSED;
                parentActivity.showBackButton(false);
                parentActivity.setToolbarColored(false);
                fillerSpaceForToolbar.setVisibility(View.GONE);
                break;
            }
            case BottomSheetBehavior.STATE_HIDDEN:
            {
                isChangingSheetState = false;
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                lastSheetState = BottomSheetBehavior.STATE_HIDDEN;
                parentActivity.showBackButton(false);
                parentActivity.setToolbarColored(false);
                fillerSpaceForToolbar.setVisibility(View.GONE);
                break;
            }
            case BottomSheetBehavior.STATE_DRAGGING:
            {
                isChangingSheetState = true;
                break;
            }
        }
    }

    private void openViewBottomSheet(Cache selectedCache)
    {
        setSheetState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        saveCacheBtn.setVisibility(View.GONE);
        foundCacheBtn.setVisibility(View.VISIBLE);
        weatherBtn.setVisibility(View.VISIBLE);

        setEditable(editTextLat, false);
        setEditable(editTextLon, false);
        setEditable(editTextDescription, false);
        setEditable(editTextName, false);

        editTextDescription.setText(selectedCache.getDescription());
        editTextLat.setText(String.format(Locale.getDefault(), "%s", selectedCache.getLatLng().latitude));
        editTextLon.setText(String.format(Locale.getDefault(), "%s", selectedCache.getLatLng().longitude));
        editTextName.setText(selectedCache.getName());
    }

    private void openEditBottomSheet(LatLng latLng)
    {
        setSheetState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        foundCacheBtn.setVisibility(View.GONE);
        saveCacheBtn.setVisibility(View.VISIBLE);
        weatherBtn.setVisibility(View.GONE);

        setEditable(editTextLat, true);
        setEditable(editTextLon, true);
        setEditable(editTextDescription, true);
        setEditable(editTextName, true);
        editTextDescription.setText("");
        editTextLat.setText(String.format(Locale.getDefault(), "%s", latLng.latitude));
        editTextLon.setText(String.format(Locale.getDefault(), "%s", latLng.longitude));
        editTextName.setText("");
    }

    private void setEditable(EditText editTextView, boolean editable)
    {
        editTextView.setFocusable(editable);
        editTextView.setClickable(editable);
        editTextView.setFocusableInTouchMode(editable);
        editTextView.setLongClickable(editable);
        if (editable)
            editTextView.setInputType(InputType.TYPE_CLASS_TEXT);
        else
            editTextView.setInputType(InputType.TYPE_NULL);
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
        setSheetState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void closeSheet()
    {
        setSheetState(BottomSheetBehavior.STATE_HIDDEN);
    }

    public boolean isExpandedSheet()
    {
        if (lastSheetState == BottomSheetBehavior.STATE_EXPANDED)
            return true;
        else
            return false;
    }
}
