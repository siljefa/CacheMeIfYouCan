package no.hiof.stianad.cachemeifyoucan;

import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
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

        /*
        For all editTextFields set the sheet state to expanded when in focus to make space for keyboard.
         */
        //region Set EditTextView onClickListeners.
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
            if (hasFocus)
                sheetBehavior.setSheetState(BottomSheetBehavior.STATE_EXPANDED);
        });
        //endregion

    }


    //BottomSheet can be opened in two different modes edit or view.
    //region BottomSheet modes.
    // When sheet is opened in view mode the text views are not editable. A found cache button and a weather button is visible.
    public void openSheetInViewMode(Cache selectedCache)
    {
        sheetBehavior.setSheetState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        saveCacheBtn.setVisibility(View.GONE);
        foundCacheBtn.setVisibility(View.VISIBLE);
        weatherBtn.setVisibility(View.VISIBLE);
        setEditTextFieldsEditable(false);

        editTextDescription.setText(selectedCache.getDescription());
        editTextLat.setText(String.format(Locale.getDefault(), "%s", selectedCache.getLatLng().latitude));
        editTextLon.setText(String.format(Locale.getDefault(), "%s", selectedCache.getLatLng().longitude));
        editTextName.setText(selectedCache.getName());
    }

    // When sheet is opened in edit mode the text views are editable. A save button is visible.
    public void openSheetInEditMode(LatLng latLng)
    {
        sheetBehavior.setSheetState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        foundCacheBtn.setVisibility(View.GONE);
        weatherBtn.setVisibility(View.GONE);
        saveCacheBtn.setVisibility(View.VISIBLE);
        setEditTextFieldsEditable(true);

        editTextDescription.setText("");
        editTextLat.setText(String.format(Locale.getDefault(), "%s", latLng.latitude));
        editTextLon.setText(String.format(Locale.getDefault(), "%s", latLng.longitude));
        editTextName.setText("");
    }
    //endregion

    //Set if all EditTextView in this bottomSheet should be editable or not.
    private void setEditTextFieldsEditable(boolean editable)
    {
        setEditTextViewEditable(editTextLat, editable);
        setEditTextViewEditable(editTextLon, editable);
        setEditTextViewEditable(editTextDescription, editable);
        setEditTextViewEditable(editTextName, editable);
    }

    //Set if a EditTextView should be editable or not.
    private void setEditTextViewEditable(EditText editTextView, boolean editable)
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
