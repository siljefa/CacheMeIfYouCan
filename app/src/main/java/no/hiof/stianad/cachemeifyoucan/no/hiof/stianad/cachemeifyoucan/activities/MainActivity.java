package no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;

import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.fragments.AchievementsFragment;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.fragments.FilterCacheFragment;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.fragments.MapFragment;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.fragments.ProfileFragment;
import no.hiof.stianad.cachemeifyoucan.R;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.fragments.WeatherFragment;
import no.hiof.stianad.cachemeifyoucan.no.hiof.stianad.cachemeifyoucan.utilities.AppService;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MapFragment.IMapFragment
{
    MapFragment mapFragment = new MapFragment();
    private ProfileFragment profileFragment = new ProfileFragment();
    private AchievementsFragment achievementsFragment = new AchievementsFragment();
    private FilterCacheFragment filterCacheFragment = new FilterCacheFragment();
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Toolbar topToolbar;

    public static final int PERMISSIONS_REQUEST_LOCATION = 99;
    private LocationManager locationManager;
    private String locationProvider;

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private boolean toolBarNavigationListenerIsRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(topToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, topToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        topToolbar.setBackgroundColor(Color.TRANSPARENT);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainLayout, mapFragment, "map_fragment");
        fragmentTransaction.add(R.id.mainLayout, profileFragment, "profile_fragment").hide(profileFragment);
        fragmentTransaction.add(R.id.mainLayout, achievementsFragment, "achievements_fragment").hide(achievementsFragment);
        fragmentTransaction.add(R.id.mainLayout, filterCacheFragment, "filter_cache_fragment").hide(filterCacheFragment);
        fragmentTransaction.commit();
        //showBackButton(false);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationProvider = locationManager.getBestProvider(new Criteria(), false);
        checkLocationPermission();

        //Starts foreground service to show notification
        startService();
    }

    //Starts service
    private void startService()
    {
        Intent serviceIntent = new Intent(this, AppService.class);

        //Build-in-function to check android version. Starts foreground service.
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    //Stops service. Method is nerver used, but shown in code to display how to stop a service.
    /*public void stopService(View v){
        Intent serviceIntent = new Intent(this, AppService.class);
        stopService(serviceIntent);
    }*/

    //Decides what kind of button to show on the toolbar, set drawer accessible when button is burger.
    public void showBackButton(boolean showBackButton)
    {
        if (showBackButton)
        {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            drawerToggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (mapFragment.isExpandedSheet())
            {
                setToolbarColored(true);
                setToolbarBackIconDown(true);
            } else
            {
                setToolbarColored(false);
                setToolbarBackIconDown(false);
            }

            if (!toolBarNavigationListenerIsRegistered)
            {
                drawerToggle.setToolbarNavigationClickListener(v ->
                        onBackNavigation());

                toolBarNavigationListenerIsRegistered = true;
            }
        } else
        {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            drawerToggle.setDrawerIndicatorEnabled(true);
            drawerToggle.setToolbarNavigationClickListener(null);
            toolBarNavigationListenerIsRegistered = false;
        }
    }

    private void onBackNavigation()
    {
        FragmentTransaction fragmentTransactionOnClick = fragmentManager.beginTransaction();

        if (achievementsFragment.isVisible())
        {
            fragmentTransactionOnClick.hide(achievementsFragment);
            fragmentTransactionOnClick.show(mapFragment);
            fragmentTransactionOnClick.commit();
            mapFragment.closeSheet();
            showBackButton(false);
        }
        if (profileFragment.isVisible())
        {
            fragmentTransactionOnClick.hide(profileFragment);
            fragmentTransactionOnClick.show(mapFragment);
            fragmentTransactionOnClick.commit();
            mapFragment.closeSheet();
            showBackButton(false);
        }
        if (filterCacheFragment.isVisible())
        {
            fragmentTransactionOnClick.hide(filterCacheFragment);
            fragmentTransactionOnClick.show(mapFragment);
            fragmentTransactionOnClick.commit();
            mapFragment.closeSheet();
            showBackButton(false);
        }
        if (mapFragment.isVisible())
        {
            mapFragment.collapseSheet();
            topToolbar.setBackgroundColor(Color.TRANSPARENT);
        }

        Fragment weatherFragment = fragmentManager.findFragmentByTag("weather_fragment");
        if (weatherFragment instanceof WeatherFragment)
        {
            fragmentTransactionOnClick.remove(weatherFragment);
            fragmentTransactionOnClick.show(mapFragment);
            fragmentTransactionOnClick.commit();
            if (mapFragment.isExpandedSheet())
            {
                showBackButton(true);
                setToolbarColored(true);
                setToolbarBackIconDown(true);
            } else
                showBackButton(false);
        }
    }

    public void setToolbarBackIconDown(boolean down)
    {
        if (down)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_expand_more_black_24dp);
        else
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
    }

    public void setToolbarColored(boolean addColor)
    {
        if (addColor)
            topToolbar.setBackgroundColor(Color.WHITE);
        else
            topToolbar.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void onBackPressed()
    {
        if(mapFragment.isVisible())
            super.onBackPressed();
        else
            onBackNavigation();
    }

    private boolean checkLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);

            /*if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission")
                        .setMessage("Location Permission is needed for the app to function.")
                        .setPositiveButton("Ok", (dialogInterface, i) ->
                        {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
                        })
                        .create()
                        .show();
            } else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
            }*/
            return false;
        } else
        {
            locationManager.requestLocationUpdates(locationProvider, 400, 1, mapFragment);
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case PERMISSIONS_REQUEST_LOCATION:
            {
                // If request is cancelled, the result array is empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        locationProvider = locationManager.getBestProvider(new Criteria(), false);
                        locationManager.requestLocationUpdates(locationProvider, 400, 1, mapFragment);
                    }
                }
                break;
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.requestLocationUpdates(locationProvider, 400, 1, mapFragment);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.removeUpdates(mapFragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.nav_profile:
            {
                showBackButton(true);
                fragmentTransaction.hide(mapFragment);
                fragmentTransaction.show(profileFragment);
                break;
            }
            case R.id.nav_achievements:
            {
                showBackButton(true);
                fragmentTransaction.hide(mapFragment);
                fragmentTransaction.show(achievementsFragment);
                break;
            }
            case R.id.nav_settings:
            {
                break;
            }
            case R.id.nav_filter:
            {
                showBackButton(true);
                fragmentTransaction.hide(mapFragment);
                fragmentTransaction.show(filterCacheFragment);
                break;
            }
            case R.id.nav_logout:
            {
                startActivity(new Intent(this, StartUpActivity.class));
                break;
            }
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public LatLng getLastKnownLocation()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
            return null;
        }
        double lat = locationManager.getLastKnownLocation(locationProvider).getLatitude();
        double lon = locationManager.getLastKnownLocation(locationProvider).getLatitude();

        return new LatLng(lat,lon);
    }
}
