package no.hiof.stianad.cachemeifyoucan;

import android.support.design.widget.BottomSheetBehavior;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

public class CacheBottomSheet
{
    private CustomSheetBehavior sheetBehavior;

    private Button foundCacheBtn, saveCacheBtn, weatherBtn;
    private EditText editTextLat, editTextLon, editTextDescription, editTextName;

    public CacheBottomSheet(View view, MainActivity parentActivity)
    {
        View cacheBottomSheet = view.findViewById(R.id.cache_bottom_sheet);
        sheetBehavior = new CustomSheetBehavior(cacheBottomSheet, parentActivity);

        editTextLat = cacheBottomSheet.findViewById(R.id.lat_edit);
        editTextLon = cacheBottomSheet.findViewById(R.id.lon_edit);
        editTextDescription = cacheBottomSheet.findViewById(R.id.description_edit);
        editTextName = cacheBottomSheet.findViewById(R.id.name_edit);
        foundCacheBtn = cacheBottomSheet.findViewById(R.id.foundCacheBtn);
        saveCacheBtn = cacheBottomSheet.findViewById(R.id.saveCacheBtn);
        weatherBtn = cacheBottomSheet.findViewById(R.id.weatherBtn);

        editTextLat.setOnFocusChangeListener((v, hasFocus) ->
        {
            if (hasFocus)
                sheetBehavior.setSheetState(BottomSheetBehavior.STATE_EXPANDED);
        });
        editTextLon.setOnFocusChangeListener((v, hasFocus) ->
        {
            if (hasFocus)
                sheetBehavior.setSheetState(BottomSheetBehavior.STATE_EXPANDED);
        });
        editTextDescription.setOnFocusChangeListener((v, hasFocus) ->
        {
            if (hasFocus)
                sheetBehavior.setSheetState(BottomSheetBehavior.STATE_EXPANDED);
        });
        editTextName.setOnFocusChangeListener((v, hasFocus) ->
        {
            if (!hasFocus)
                sheetBehavior.setSheetState(BottomSheetBehavior.STATE_EXPANDED);
        });

    }

    public void openViewBottomSheet(Cache selectedCache)
    {
        sheetBehavior.setSheetState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        saveCacheBtn.setVisibility(View.GONE);
        foundCacheBtn.setVisibility(View.VISIBLE);
        weatherBtn.setVisibility(View.VISIBLE);

        setEditTextEditable(editTextLat, false);
        setEditTextEditable(editTextLon, false);
        setEditTextEditable(editTextDescription, false);
        setEditTextEditable(editTextName, false);

        editTextDescription.setText(selectedCache.getDescription());
        editTextLat.setText(String.format(Locale.getDefault(), "%s", selectedCache.getLatLng().latitude));
        editTextLon.setText(String.format(Locale.getDefault(), "%s", selectedCache.getLatLng().longitude));
        editTextName.setText(selectedCache.getName());
    }

    /*
        Open bottomSheet in create cache mode.
        Make text editable. Show saveCacheBtn.
    */
    public void openEditBottomSheet(LatLng latLng)
    {
        sheetBehavior.setSheetState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        foundCacheBtn.setVisibility(View.GONE);
        saveCacheBtn.setVisibility(View.VISIBLE);
        weatherBtn.setVisibility(View.GONE);

        setEditTextEditable(editTextLat, true);
        setEditTextEditable(editTextLon, true);
        setEditTextEditable(editTextDescription, true);
        setEditTextEditable(editTextName, true);
        editTextDescription.setText("");
        editTextLat.setText(String.format(Locale.getDefault(), "%s", latLng.latitude));
        editTextLon.setText(String.format(Locale.getDefault(), "%s", latLng.longitude));
        editTextName.setText("");
    }


    private void setEditTextEditable(EditText editTextView, boolean editable)
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


    void setFoundCacheBtnOnClickListener(View.OnClickListener listener)
    {
        foundCacheBtn.setOnClickListener(listener);
    }

    void setSaveCacheBtnOnClickListener(View.OnClickListener listener)
    {
         saveCacheBtn.setOnClickListener(listener);
    }

    public void setWeatherBtnOnClickListener(View.OnClickListener listener)
    {
        weatherBtn.setOnClickListener(listener);
    }

    public EditText getEditTextLat()
    {
        return editTextLat;
    }

    public EditText getEditTextLon()
    {
        return editTextLon;
    }

    public EditText getEditTextDescription()
    {
        return editTextDescription;
    }

    public EditText getEditTextName()
    {
        return editTextName;
    }

    void setSheetState(int state)
    {
        sheetBehavior.setSheetState(state);
    }

    int getLastSheetState()
    {
        return sheetBehavior.getLastSheetState();
    }
}
