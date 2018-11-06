package no.hiof.stianad.cachemeifyoucan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener
{
    private LatLng HIOF_CACHE = new LatLng(59.12797849, 11.35272861);
    private LatLng FREDRIKSTAD_CACHE = new LatLng(59.21047628, 10.93994737);
    private GoogleMap gMap;
    View bottomSheet;
    View mainView;
    private int lastSheetState;
    private BottomSheetBehavior mBehavior;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mainView = view;
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null)
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
            boolean isChangingSheetState;

            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState)
            {
                if( newState == BottomSheetBehavior.STATE_EXPANDED)
                {
                    Button closeSheetBtn = bottomSheet.findViewById(R.id.closeSheetBtn);
                    closeSheetBtn.setVisibility(View.VISIBLE);
                }

                if (newState == BottomSheetBehavior.STATE_DRAGGING)
                {
                    isChangingSheetState = true;
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
                        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        lastSheetState = BottomSheetBehavior.STATE_EXPANDED;
                        ((MainActivity) getActivity()).hideActionBar();
                    } else if ((slideOffset > 0 && lastSheetState == BottomSheetBehavior.STATE_COLLAPSED) || (slideOffset < 1 && lastSheetState == BottomSheetBehavior.STATE_EXPANDED))
                    {
                        mBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                        lastSheetState = BottomSheetBehavior.STATE_HALF_EXPANDED;
                        ((MainActivity) getActivity()).showActionBar();
                    } else if ((slideOffset < 0.5 && lastSheetState == BottomSheetBehavior.STATE_HALF_EXPANDED) || (slideOffset <= 0.40 && lastSheetState == BottomSheetBehavior.STATE_EXPANDED))
                    {
                        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        lastSheetState = BottomSheetBehavior.STATE_COLLAPSED;
                        ((MainActivity) getActivity()).showActionBar();
                    }
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
            lastSheetState = BottomSheetBehavior.STATE_HIDDEN;
        });

        EditText editTextLat = bottomSheet.findViewById(R.id.lat_edit);
        EditText editTextLon = bottomSheet.findViewById(R.id.lon_edit);
        EditText editTextdescription = bottomSheet.findViewById(R.id.description_edit);
        EditText editTextName = bottomSheet.findViewById(R.id.name_edit);

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
                ((MainActivity) getActivity()).hideActionBar();
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
                ((MainActivity) getActivity()).hideActionBar();
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
                ((MainActivity) getActivity()).hideActionBar();
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
                ((MainActivity) getActivity()).hideActionBar();
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void openViewBottomSheet(Cache selectedCache)
    {
        // closeSheetBtn
        mBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        lastSheetState = BottomSheetBehavior.STATE_HALF_EXPANDED;
        Button saveCacheBtn = bottomSheet.findViewById(R.id.saveCacheBtn);
        Button foundCacheBtn = bottomSheet.findViewById(R.id.foundCacheBtn);
        Button closeSheetBtn = bottomSheet.findViewById(R.id.closeSheetBtn);
        saveCacheBtn.setVisibility(View.GONE);
        closeSheetBtn.setVisibility(View.GONE);
        foundCacheBtn.setVisibility(View.VISIBLE);

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

    private void openEditBottomSheet(LatLng latLng)
    {
        mBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        lastSheetState = BottomSheetBehavior.STATE_HALF_EXPANDED;
        Button foundCacheBtn = bottomSheet.findViewById(R.id.foundCacheBtn);
        Button saveCacheBtn = bottomSheet.findViewById(R.id.saveCacheBtn);
        Button closeSheetBtn = bottomSheet.findViewById(R.id.closeSheetBtn);
        foundCacheBtn.setVisibility(View.GONE);
        closeSheetBtn.setVisibility(View.GONE);
        saveCacheBtn.setVisibility(View.VISIBLE);

        EditText editTextLat = bottomSheet.findViewById(R.id.lat_edit);
        EditText editTextLon = bottomSheet.findViewById(R.id.lon_edit);
        EditText editTextdescription = bottomSheet.findViewById(R.id.description_edit);
        EditText editTextName = bottomSheet.findViewById(R.id.name_edit);

        setEditable(editTextLat);
        setEditable(editTextLon);
        setEditable(editTextdescription);
        setEditable(editTextName);
        editTextLat.setText(Double.toString(latLng.latitude));
        editTextLon.setText(Double.toString(latLng.longitude));
        editTextdescription.setText("");
        editTextName.setText("");
    }

    private void setNonEditable(EditText editTextView)
    {
        editTextView.setFocusable(false);
        editTextView.setClickable(false);
        editTextView.setFocusableInTouchMode(false);
        editTextView.setLongClickable(false);
        editTextView.setInputType(InputType.TYPE_NULL);
    }

    private void setEditable(EditText editTextView)
    {
        editTextView.setFocusable(true);
        editTextView.setClickable(true);
        editTextView.setFocusableInTouchMode(true);
        editTextView.setLongClickable(true);
        editTextView.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        gMap = googleMap;
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMapClickListener(this);
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
        openEditBottomSheet(latLng);
    }

    @Override
    public boolean onMarkerClick(Marker marker)
    {
        Cache newCache = new Cache(new LatLng(20,20), "Dette er en test Cache", "TestNavn", 1);
        openViewBottomSheet(newCache);
        /*CacheInfoSheetFragment bottomSheetDialogFragment = new CacheInfoSheetFragment();
        Bundle args = new Bundle();
        args.putBoolean("isEditing", false);
        bottomSheetDialogFragment.setArguments(args);
        //bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
        */return false;
    }

    @Override
    public void onMapClick(LatLng latLng)
    {
        if((lastSheetState == BottomSheetBehavior.STATE_EXPANDED) || (lastSheetState == BottomSheetBehavior.STATE_HALF_EXPANDED) )
        {
            //Hidden first because the BottomSheetBehavior thinks it's collapsed.
            mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            lastSheetState = BottomSheetBehavior.STATE_COLLAPSED;
        }
    }
}
