/*package no.hiof.stianad.cachemeifyoucan;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.maps.model.LatLng;


public class CacheInfoSheetFragment extends BottomSheetDialogFragment
{
    private boolean isEditing;
    private View contentView;
    private LatLng cachePosition;
    private BottomSheetBehavior mBehavior;
    private int lastSheetState;
    private boolean isChangingSheetState;

    @Override
    public void onStart()
    {
        super.onStart();
        mBehavior = BottomSheetBehavior.from((View) contentView.getParent());
        mBehavior.setPeekHeight(200);
        mBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        lastSheetState = BottomSheetBehavior.STATE_HALF_EXPANDED;

        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback()
        {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState)
            {
                if (newState == BottomSheetBehavior.STATE_HIDDEN)
                {
                    dismiss();
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
                    else if ((slideOffset > 0.1 && lastSheetState == BottomSheetBehavior.STATE_COLLAPSED) || (slideOffset < 0.95 && lastSheetState == BottomSheetBehavior.STATE_EXPANDED))
                    {
                        mBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                        lastSheetState = BottomSheetBehavior.STATE_HALF_EXPANDED;
                    }
                    else if ((slideOffset <= 0.45 && lastSheetState == BottomSheetBehavior.STATE_HALF_EXPANDED) || (slideOffset <= 0.40 && lastSheetState == BottomSheetBehavior.STATE_EXPANDED))
                    {
                        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        lastSheetState = BottomSheetBehavior.STATE_COLLAPSED;
                    }
                    else if (slideOffset == -1)
                    {
                        mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        lastSheetState = BottomSheetBehavior.STATE_HIDDEN;
                    }
                    isChangingSheetState = false;
                }
            }
        });

        if (!isEditing)
        {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            lastSheetState = BottomSheetBehavior.STATE_COLLAPSED;
            EditText editTextLat = contentView.findViewById(R.id.lat_edit);
            EditText editTextLon = contentView.findViewById(R.id.lon_edit);
            EditText editTextdescription = contentView.findViewById(R.id.description_edit);
            EditText editTextName = contentView.findViewById(R.id.name_edit);
            setNonEditable(editTextLat);
            setNonEditable(editTextLon);
            setNonEditable(editTextdescription);
            setNonEditable(editTextName);

            Button saveCacheBtn = contentView.findViewById(R.id.saveCacheBtn);
            saveCacheBtn.setVisibility(View.GONE);
        }
        else
        {
            Button foundCacheBtn = contentView.findViewById(R.id.foundCacheBtn);
            foundCacheBtn.setVisibility(View.GONE);
        }
        //setFocusChangeListners();
    }

    private void setFocusChangeListners()
    {
        EditText editTextLat = contentView.findViewById(R.id.lat_edit);
        EditText editTextLon = contentView.findViewById(R.id.lon_edit);
        EditText editTextdescription = contentView.findViewById(R.id.description_edit);

        View.OnFocusChangeListener focusChangeListener = (v, hasFocus) ->
        {
            mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            lastSheetState = BottomSheetBehavior.STATE_EXPANDED;
        };
        editTextLat.setOnFocusChangeListener(focusChangeListener);
        editTextLon.setOnFocusChangeListener(focusChangeListener);
        editTextdescription.setOnFocusChangeListener(focusChangeListener);
    }


    private void setNonEditable(EditText editTextView)
    {
        editTextView.setFocusable(false);
        editTextView.setClickable(false);
        editTextView.setFocusableInTouchMode(false);
        editTextView.setLongClickable(false);
        editTextView.setInputType(InputType.TYPE_NULL);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Suche verfeinern");


        //set the dialog to non-modal and disable dim out fragment behind
        Window window = dialog.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        return dialog;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        contentView = View.inflate(getContext(), R.layout.fragment_cache_info_sheet, null);

        Button funnetCacheBtn = contentView.findViewById(R.id.saveCacheBtn);
        funnetCacheBtn.setOnClickListener(v ->
        {
            LatLng i = new LatLng(2,2);
            Caches.createCashe(cachePosition, "Hello","Some Name", 2);
            mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        });

        if (getArguments() != null)
        {
            isEditing = getArguments().getBoolean("isEditing");
            cachePosition = new LatLng(getArguments().getDouble("Lat"), getArguments().getDouble("Lon"));
        }
        return contentView;
    }
}*/

