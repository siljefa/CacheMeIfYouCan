package no.hiof.stianad.cachemeifyoucan;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class CustomSheetBehavior
{
    private MainActivity parentActivity;
    private View fillerSpaceForToolbar;
    private View bottomSheet;

    private BottomSheetBehavior sheetBehavior;
    private int lastSheetState;
    private boolean userIsChangingSheetState = false;

    public CustomSheetBehavior(View view, MainActivity parentActivity)
    {
        this.bottomSheet = view;
        this.parentActivity = parentActivity;
        sheetBehavior = BottomSheetBehavior.from(view);
        fillerSpaceForToolbar = view.findViewById(R.id.filler);
        setUpCustomSheetBehavior();
    }


    //Change sheetBehavior so that collapsed state height is 200.
    //Make the sheet Hideable.
    //Make it so the user can drag the sheet to the half expanded state.
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
                    //When the user is moving the sheet it enters the dragging state.
                    // Setting a flag here makes it possible to differentiate between user interaction and other ways the sheet is moved.
                    case BottomSheetBehavior.STATE_DRAGGING:
                    {
                        userIsChangingSheetState = true;
                    }
                    case BottomSheetBehavior.STATE_HIDDEN:
                    {
                        setSheetState(BottomSheetBehavior.STATE_HIDDEN);
                    }
                }
            }


            //Set state based on where the user drops the sheet. This is hackish and prone to bugs.
            //This should have been done using a different BottomSheetBehavior.
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset)
            {
                //Using the flag userIsChangingSheetState we can know if the user was dragging the sheet or if it was moved in other ways.
                //Testing for the state settling we can know if the user has dropped the sheet.
                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_SETTLING && userIsChangingSheetState)
                {
                    //Expanded is slideOffset 1, HalfExpanded is slideOffset 0.5, Collapsed is slideOffset 0.
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
                    //The user has let dropped the sheet and is no longer changing the sheet state.
                    userIsChangingSheetState = false;
                }
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //Move sheet to new state and change appearance accordingly.
    public void setSheetState(int state)
    {
        //Make sure the system know the user is not moving the sheet manually
        //Set last sheet state because it's more reliable than getting current state.
        //Set the actual new state.
        //Change navigation options.
        //Change if the keyboard should be visible.
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
                hideKeyboard(bottomSheet);
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
                hideKeyboard(bottomSheet);
                break;
            }
            case BottomSheetBehavior.STATE_HIDDEN:
            {
                userIsChangingSheetState = false;
                //sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                lastSheetState = BottomSheetBehavior.STATE_HIDDEN;
                parentActivity.showBackButton(false);
                parentActivity.setToolbarColored(false);
                fillerSpaceForToolbar.setVisibility(View.GONE);
                hideKeyboard(bottomSheet);
                bottomSheet.clearFocus();
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
