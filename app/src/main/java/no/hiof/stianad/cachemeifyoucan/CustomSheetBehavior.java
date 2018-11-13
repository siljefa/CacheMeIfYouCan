package no.hiof.stianad.cachemeifyoucan;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;

public class CustomSheetBehavior
{
    private MainActivity parentActivity;
    private View fillerSpaceForToolbar;

    private BottomSheetBehavior sheetBehavior;
    private int lastSheetState;
    private boolean userIsChangingSheetState = false;

    public CustomSheetBehavior(View view, MainActivity parentActivity)
    {
        this.parentActivity = parentActivity;
        sheetBehavior = BottomSheetBehavior.from(view);
        fillerSpaceForToolbar = view.findViewById(R.id.filler);
        setUpCustomSheetBehavior();
    }


    /*
    Change sheetBehavior to set Collapsed state height to 200.
    Make sheet hideable.
    Make the sheet draggable to Half expanded.
 */
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
                        userIsChangingSheetState = true;
                    }
                }
            }

            /*
                Set state based on where user drop sheet. This is hackish and prone to bugs.
                This should have been done using a different BottomSheetBehavior.
             */
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset)
            {
                //To just change state when the user drags the sheet, check that user has been dragging sheet and that the sheet is dropped.
                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_SETTLING && userIsChangingSheetState)
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
                    userIsChangingSheetState = false;
                }
            }
        });
    }

    /*
        Move sheet to state and change appearance accordingly.
     */
    public void setSheetState(int state)
    {
        switch (state)
        {
            case BottomSheetBehavior.STATE_EXPANDED:
            {
                userIsChangingSheetState = false;
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                lastSheetState = BottomSheetBehavior.STATE_EXPANDED;
                parentActivity.showBackButton(true);
                parentActivity.setToolbarColored(true);
                fillerSpaceForToolbar.setVisibility(View.VISIBLE);
                break;
            }
            case BottomSheetBehavior.STATE_HALF_EXPANDED:
            {
                userIsChangingSheetState = false;
                sheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                lastSheetState = BottomSheetBehavior.STATE_HALF_EXPANDED;
                parentActivity.showBackButton(false);
                parentActivity.setToolbarColored(false);
                fillerSpaceForToolbar.setVisibility(View.GONE);
                break;
            }
            case BottomSheetBehavior.STATE_COLLAPSED:
            {
                userIsChangingSheetState = false;
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                lastSheetState = BottomSheetBehavior.STATE_COLLAPSED;
                parentActivity.showBackButton(false);
                parentActivity.setToolbarColored(false);
                fillerSpaceForToolbar.setVisibility(View.GONE);
                break;
            }
            case BottomSheetBehavior.STATE_HIDDEN:
            {
                userIsChangingSheetState = false;
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                lastSheetState = BottomSheetBehavior.STATE_HIDDEN;
                parentActivity.showBackButton(false);
                parentActivity.setToolbarColored(false);
                fillerSpaceForToolbar.setVisibility(View.GONE);
                break;
            }
            case BottomSheetBehavior.STATE_DRAGGING:
            {
                userIsChangingSheetState = true;
                break;
            }
        }
    }

    public int getLastSheetState()
    {
        return lastSheetState;
    }
}
