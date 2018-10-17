package no.hiof.stianad.cachemeifyoucan;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener
{
    private LatLng HIOF_CACHE = new LatLng(59.12797849, 11.35272861);
    private LatLng FREDRIKSTAD_CACHE = new LatLng(59.21047628, 10.93994737);
    private GoogleMap gMap;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment != null)
        {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        gMap = googleMap;
        googleMap.setOnMapLongClickListener(this);
        setUpDefaultUISettings();

        //gMap.addMarker(new MarkerOptions().position(HIOF_CACHE).title("Cache ved Østfold University College"));
        gMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(HIOF_CACHE, 15, 0, 0)));

        gMap.addMarker(new MarkerOptions().position(FREDRIKSTAD_CACHE).title("Cache ved Fredrikstad Kino"));
        gMap.animateCamera(CameraUpdateFactory.newLatLng(HIOF_CACHE), 2000, null);
    }

    private void setUpDefaultUISettings()
    {
        UiSettings uiSettings = gMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setMapToolbarEnabled(false);
    }

    public void addMarker(LatLng latLng)
    {
        gMap.addMarker(new MarkerOptions().position(latLng).title("Cache ved Østfold University College"));
    }

    @Override
    public void onMapLongClick(LatLng latLng)
    {
        addMarker(latLng);
    }
}
