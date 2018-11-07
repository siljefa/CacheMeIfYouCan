package no.hiof.stianad.cachemeifyoucan;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, LocationListener
{
    private LatLng newCacheLocation;
    private GoogleMap gMap;
    private View bottomSheet;
    private int lastSheetState;
    private boolean isChangingSheetState = false;
    private BottomSheetBehavior mBehavior;
    private MainActivity parentActivity;
    private LatLng testLatLon;
    private Button closeSheetBtn, foundCacheBtn, saveCacheBtn;
    private  EditText editTextLat, editTextLon, editTextdescription, editTextName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        parentActivity = (MainActivity)getActivity();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null)
        {
            mapFragment.getMapAsync(this);
        }

        bottomSheet = view.findViewById(R.id.bottom_sheet2);
        mBehavior = BottomSheetBehavior.from(bottomSheet);
        mBehavior.setHideable(true);
        setSheetState(BottomSheetBehavior.STATE_HIDDEN);
        mBehavior.setPeekHeight(200);
        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback()
        {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState)
            {
                switch (newState)
                {
                    case BottomSheetBehavior.STATE_EXPANDED:
                    {
                    }
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                    {
                    }
                    case BottomSheetBehavior.STATE_COLLAPSED:
                    {
                    }
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
                if (mBehavior.getState() == BottomSheetBehavior.STATE_SETTLING && isChangingSheetState)
                {
                    if ((slideOffset > 0.5 && lastSheetState == BottomSheetBehavior.STATE_HALF_EXPANDED) || (slideOffset >= 0.60 && lastSheetState == BottomSheetBehavior.STATE_COLLAPSED))
                    {
                        setSheetState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    else if ((slideOffset > 0 && lastSheetState == BottomSheetBehavior.STATE_COLLAPSED) || (slideOffset < 1 && lastSheetState == BottomSheetBehavior.STATE_EXPANDED))
                    {
                        setSheetState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                    }
                    else if ((slideOffset < 0.5 && lastSheetState == BottomSheetBehavior.STATE_HALF_EXPANDED) || (slideOffset <= 0.40 && lastSheetState == BottomSheetBehavior.STATE_EXPANDED))
                    {
                        setSheetState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                    isChangingSheetState = false;
                }
            }
        });

        closeSheetBtn = bottomSheet.findViewById(R.id.closeSheetBtn);
        foundCacheBtn = bottomSheet.findViewById(R.id.foundCacheBtn);
        saveCacheBtn = bottomSheet.findViewById(R.id.saveCacheBtn);
        closeSheetBtn.setOnClickListener(v -> setSheetState(BottomSheetBehavior.STATE_COLLAPSED));
        foundCacheBtn.setOnClickListener(v -> setSheetState(BottomSheetBehavior.STATE_HIDDEN));
        saveCacheBtn.setOnClickListener(v ->
        {
            Caches.createCashe(newCacheLocation, "Hello","Some Name", 2);
            setSheetState(BottomSheetBehavior.STATE_COLLAPSED);
        });

        editTextLat = bottomSheet.findViewById(R.id.lat_edit);
        editTextLon = bottomSheet.findViewById(R.id.lon_edit);
        editTextdescription = bottomSheet.findViewById(R.id.description_edit);
        editTextName = bottomSheet.findViewById(R.id.name_edit);

        editTextLat.setOnFocusChangeListener((v, hasFocus) ->
        {
            if (!hasFocus)
            {
                hideKeyboard(v);
            }
            else
            {
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                lastSheetState = BottomSheetBehavior.STATE_EXPANDED;
                parentActivity.hideActionBar();
            }
        });
        editTextLon.setOnFocusChangeListener((v, hasFocus) ->
        {
            if (!hasFocus)
            {
                hideKeyboard(v);
            }
            else
            {
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                lastSheetState = BottomSheetBehavior.STATE_EXPANDED;
                parentActivity.hideActionBar();
            }
        });
        editTextdescription.setOnFocusChangeListener((v, hasFocus) ->
        {
            if (!hasFocus)
            {
                hideKeyboard(v);
            }
            else
            {
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                lastSheetState = BottomSheetBehavior.STATE_EXPANDED;
                parentActivity.hideActionBar();
            }
        });
        editTextName.setOnFocusChangeListener((v, hasFocus) ->
        {
            if (!hasFocus)
            {
                hideKeyboard(v);
            }
            else
            {
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                lastSheetState = BottomSheetBehavior.STATE_EXPANDED;
                parentActivity.hideActionBar();
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
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                lastSheetState = BottomSheetBehavior.STATE_EXPANDED;
                parentActivity.hideActionBar();
                closeSheetBtn.setVisibility(View.VISIBLE);
                break;
            }
            case BottomSheetBehavior.STATE_HALF_EXPANDED:
            {
                isChangingSheetState = false;
                mBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                lastSheetState = BottomSheetBehavior.STATE_HALF_EXPANDED;
                parentActivity.showActionBar();
                closeSheetBtn.setVisibility(View.GONE);
                break;
            }
            case BottomSheetBehavior.STATE_COLLAPSED:
            {
                isChangingSheetState = false;
                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                lastSheetState = BottomSheetBehavior.STATE_COLLAPSED;
                parentActivity.showActionBar();
                closeSheetBtn.setVisibility(View.GONE);
                break;
            }
            case BottomSheetBehavior.STATE_HIDDEN:
            {
                isChangingSheetState = false;
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                lastSheetState = BottomSheetBehavior.STATE_HIDDEN;
                break;
            }
            case BottomSheetBehavior.STATE_DRAGGING:
            {
                isChangingSheetState = true;
                break;
            }
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void openViewBottomSheet(Cache selectedCache)
    {
        setSheetState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        saveCacheBtn.setVisibility(View.GONE);
        foundCacheBtn.setVisibility(View.VISIBLE);

        setEditable(editTextLat, false);
        setEditable(editTextLon, false);
        setEditable(editTextdescription, false);
        setEditable(editTextName, false);

        editTextLat.setText(Double.toString(selectedCache.getLatLng().latitude));
        editTextLon.setText(Double.toString(selectedCache.getLatLng().longitude));
        editTextdescription.setText(selectedCache.getDescription());
        editTextName.setText(selectedCache.getName());
    }

    private void openEditBottomSheet(LatLng latLng)
    {
        setSheetState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        foundCacheBtn.setVisibility(View.GONE);
        saveCacheBtn.setVisibility(View.VISIBLE);

        setEditable(editTextLat, true);
        setEditable(editTextLon, true);
        setEditable(editTextdescription, true);
        setEditable(editTextName, true);
        editTextLat.setText(Double.toString(latLng.latitude));
        editTextLon.setText(Double.toString(latLng.longitude));
        editTextdescription.setText("");
        editTextName.setText("");
    }

    private void setEditable(EditText editTextView, boolean editable)
    {
        if(editable)
        {
            editTextView.setFocusable(true);
            editTextView.setClickable(true);
            editTextView.setFocusableInTouchMode(true);
            editTextView.setLongClickable(true);
            editTextView.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        else
        {
            editTextView.setFocusable(false);
            editTextView.setClickable(false);
            editTextView.setFocusableInTouchMode(false);
            editTextView.setLongClickable(false);
            editTextView.setInputType(InputType.TYPE_NULL);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        gMap = googleMap;
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMapClickListener(this);
        setUpDefaultUISettings();


        gMap.addMarker(new MarkerOptions().position(testLatLon).title("Cache ved Fredrikstad Kino"));
        gMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(testLatLon, 15, 0, 0)));
        gMap.animateCamera(CameraUpdateFactory.newLatLng(testLatLon), 2000, null);
    }

    private void setUpDefaultUISettings()
    {
        UiSettings uiSettings = gMap.getUiSettings();
        uiSettings.setCompassEnabled(false);
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setMapToolbarEnabled(false);
    }

    public void addMarker(LatLng latLng, String title)
    {
        gMap.addMarker(new MarkerOptions().position(latLng).title("Cache ved Østfold University College"));
    }

    @Override
    public void onMapLongClick(LatLng latLng)
    {
        addMarker(latLng, "");
        newCacheLocation = latLng;
        openEditBottomSheet(latLng);
    }

    @Override
    public boolean onMarkerClick(Marker marker)
    {
        Cache newCache = new Cache(new LatLng(20,20), "Dette er en test Cache", "TestNavn", 1);
        openViewBottomSheet(newCache);
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng)
    {
        if((lastSheetState == BottomSheetBehavior.STATE_EXPANDED) || (lastSheetState == BottomSheetBehavior.STATE_HALF_EXPANDED) )
        {
            //Hidden first because the BottomSheetBehavior thinks it's collapsed.
            mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            setSheetState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {
        testLatLon = new LatLng(location.getLatitude(), location.getLongitude());
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

    /*  args = new Bundle();
        args.putBoolean("isEditing", false);
        bottomSheetDialogFragment.setArguments(args);*/
}
