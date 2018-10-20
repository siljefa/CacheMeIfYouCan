package no.hiof.stianad.cachemeifyoucan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class CacheInfoSheetFragment extends BottomSheetDialogFragment
{
    private boolean isEditing;
    private View contentView;

    /*private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback()
    {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState)
        {
            if (newState == BottomSheetBehavior.STATE_HIDDEN)
            {
                dismiss();
            }
        }
        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset)
        {
        }
    };*/

    @Override
    public void onStart()
    {
        super.onStart();

        if(!isEditing)
        {
            EditText editText = contentView.findViewById(R.id.sheetTopText);
            editText.setFocusable(false);
            editText.setClickable(false);
            editText.setFocusableInTouchMode(false);
            editText.setLongClickable(false);
            editText.setInputType(InputType.TYPE_NULL);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        contentView = View.inflate(getContext(), R.layout.fragment_cache_info_sheet, null);
        if (getArguments() != null)
        {
            isEditing = getArguments().getBoolean("isEditing");
        }
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) contentView.getParent());
        mBehavior.setPeekHeight(200);
    }
}

