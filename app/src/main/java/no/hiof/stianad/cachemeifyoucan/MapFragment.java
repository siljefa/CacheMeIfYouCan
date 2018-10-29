package no.hiof.stianad.cachemeifyoucan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener
{
    private LatLng HIOF_CACHE = new LatLng(59.12797849, 11.35272861);
    private LatLng FREDRIKSTAD_CACHE = new LatLng(59.21047628, 10.93994737);
    private GoogleMap gMap;
    View bottomSheet;
    private boolean isChangingSheetState;
    private int lastSheetState;
    private BottomSheetBehavior mBehavior;
    private boolean isEditing = false;


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

        bottomSheet = view.findViewById(R.id.bottom_sheet2);
        mBehavior = BottomSheetBehavior.from(bottomSheet);
        mBehavior.setHideable(true);
        mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBehavior.setPeekHeight(200);
        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback()
        {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState)
            {
                if (newState == BottomSheetBehavior.STATE_HIDDEN)
                {
                    ((MainActivity) getActivity()).showActionBar();
                }
                if (newState == BottomSheetBehavior.STATE_COLLAPSED)
                {
                    ((MainActivity) getActivity()).showActionBar();
                }
                if (newState == BottomSheetBehavior.STATE_HALF_EXPANDED)
                {
                    ((MainActivity) getActivity()).showActionBar();
                }
                if (newState == BottomSheetBehavior.STATE_EXPANDED)
                {
                    ((MainActivity) getActivity()).hideActionBar();
                }
                if (newState == BottomSheetBehavior.STATE_DRAGGING)
                {
                    isChangingSheetState = true;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset)
            {
                if (mBehavior.getState() == BottomSheetBehavior.STATE_SETTLING && isChangingSheetState)
                {
                    int i = lastSheetState;
                    float j = slideOffset;
                    if ((slideOffset >= 0.1 && lastSheetState == BottomSheetBehavior.STATE_COLLAPSED))
                    {
                        i = 0;
                    }

                    if ((slideOffset >= 0.6 && lastSheetState == BottomSheetBehavior.STATE_HALF_EXPANDED) || (slideOffset >= 0.60 && lastSheetState == BottomSheetBehavior.STATE_COLLAPSED))
                    {
                        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        lastSheetState = BottomSheetBehavior.STATE_EXPANDED;
                    }
                    else if ((slideOffset > 0.1 && lastSheetState == BottomSheetBehavior.STATE_COLLAPSED) || (slideOffset < 1 && lastSheetState == BottomSheetBehavior.STATE_EXPANDED))
                    {
                        mBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                        lastSheetState = BottomSheetBehavior.STATE_HALF_EXPANDED;
                    }
                    else if ((slideOffset <= 0.45 && lastSheetState == BottomSheetBehavior.STATE_HALF_EXPANDED) || (slideOffset <= 0.40 && lastSheetState == BottomSheetBehavior.STATE_EXPANDED))
                    {
                        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        lastSheetState = BottomSheetBehavior.STATE_COLLAPSED;
                    }
                    /*else if (slideOffset <= 0.1 && lastSheetState == BottomSheetBehavior.STATE_COLLAPSED)
                    {
                        mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        lastSheetState = BottomSheetBehavior.STATE_HIDDEN;
                    }*/
                    isChangingSheetState = false;
                }
            }
        });

        Button funnetCacheBtn = bottomSheet.findViewById(R.id.saveCacheBtn);
        funnetCacheBtn.setOnClickListener(v ->
        {
            //LatLng i = new LatLng(2,2);
            //Caches.createCashe(cachePosition, "Hello","Some Name", 2);
            mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        });


    }

    private void openViewBottomSheet(Cache selectedCache)
    {
        mBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        lastSheetState = BottomSheetBehavior.STATE_HALF_EXPANDED;
        Button saveCacheBtn = bottomSheet.findViewById(R.id.saveCacheBtn);
        saveCacheBtn.setVisibility(View.GONE);
        //lastSheetState = BottomSheetBehavior.STATE_COLLAPSED;
        EditText editTextLat = bottomSheet.findViewById(R.id.lat_edit);
        EditText editTextLon = bottomSheet.findViewById(R.id.lon_edit);
        EditText editTextdescription = bottomSheet.findViewById(R.id.description_edit);
        EditText editTextName = bottomSheet.findViewById(R.id.name_edit);
        setNonEditable(editTextLat);
        setNonEditable(editTextLon);
        setNonEditable(editTextdescription);
        setNonEditable(editTextName);

        editTextLat.setText(Double.toString(selectedCache.getLatLng().latitude));
        editTextLon.setText(Double.toString(selectedCache.getLatLng().longitude));
        editTextdescription.setText(selectedCache.getDescription());
        editTextName.setText(selectedCache.getName());
    }

    private void openEditBottomSheet(Cache selectedCache)
    {
        mBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        Button foundCacheBtn = bottomSheet.findViewById(R.id.foundCacheBtn);
        foundCacheBtn.setVisibility(View.GONE);
    }


    private void setNonEditable(EditText editTextView)
    {
        editTextView.setFocusable(false);
        editTextView.setClickable(false);
        editTextView.setFocusableInTouchMode(false);
        editTextView.setLongClickable(false);
        editTextView.setInputType(InputType.TYPE_NULL);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        gMap = googleMap;
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnMarkerClickListener(this);
        setUpDefaultUISettings();

        //gMap.addMarker(new MarkerOptions().position(HIOF_CACHE).title("Cache ved Østfold University College"));
        gMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(HIOF_CACHE, 15, 0, 0)));

        gMap.addMarker(new MarkerOptions().position(HIOF_CACHE).title("Cache ved Fredrikstad Kino"));
        gMap.animateCamera(CameraUpdateFactory.newLatLng(HIOF_CACHE), 2000, null);
    }

    private void setUpDefaultUISettings()
    {
        UiSettings uiSettings = gMap.getUiSettings();
        uiSettings.setCompassEnabled(false);
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

        /*CacheInfoSheetFragment bottomSheetDialogFragment = new CacheInfoSheetFragment();
        Bundle args = new Bundle();
        args.putBoolean("isEditing", true);
        args.putDouble("Lat", latLng.latitude);
        args.putDouble("Lon", latLng.longitude);
        bottomSheetDialogFragment.setArguments(args);
        //bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());*/
    }

    @Override
    public boolean onMarkerClick(Marker marker)
    {
        Cache newCache = new Cache(new LatLng(20,20), "Dette er en test Cache", "TestNavn", 1);
        isEditing = true;
        openViewBottomSheet(newCache);
        /*CacheInfoSheetFragment bottomSheetDialogFragment = new CacheInfoSheetFragment();
        Bundle args = new Bundle();
        args.putBoolean("isEditing", false);
        bottomSheetDialogFragment.setArguments(args);
        //bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
        */return false;
    }
}
