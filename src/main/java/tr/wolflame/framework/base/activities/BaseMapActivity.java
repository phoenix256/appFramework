package tr.wolflame.framework.base.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

import tr.wolflame.framework.R;
import tr.wolflame.framework.base.interfaces.BaseMapActivityInterface;
import tr.wolflame.framework.base.util.LogApp;
import tr.wolflame.framework.base.util.helper.MapLocationFramework;
import tr.wolflame.framework.base.util.helper.OnTaskCompleted;

public abstract class BaseMapActivity extends BaseAppCompatActivity implements OnMapReadyCallback, BaseMapActivityInterface {

    protected MapLocationFramework locationFramework;

    private final static String[] PERMISSIONS_ARRAY = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};


    @Override
    public int initLayoutId() {
        return R.layout.layout_base;
    }

    @Override
    public void onCreated() {

        initMap();
    }


    protected void initMap() {

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = new SupportMapFragment();

        replaceFragment(initContainerId(), mapFragment);

        mapFragment.getMapAsync(this);
    }

    private void onInitMethod() {
        if (ActivityCompat.checkSelfPermission(BaseMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(BaseMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            LogApp.d(TAG, String.valueOf("permissions are not granted"));

        } else {
            locationFramework.getGoogleMap().setMyLocationEnabled(true);

            onMapReadyNext();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {

        locationFramework = new MapLocationFramework(BaseMapActivity.this, googleMap, googleMap.getUiSettings());
        locationFramework.clearMarkers(false);

        // Sets the map type to be "hybrid"
        locationFramework.getGoogleMap().setMapType(initMapType());


        requestPermission(PERMISSIONS_ARRAY, new OnTaskCompleted<ArrayList<String>>() {
            @Override
            public void onTaskCompleted(String result) {

                onInitMethod();
            }

            @Override
            public void onTaskCompleted(ArrayList<String> result) {

                onInitMethod();
            }

            @Override
            public void onTaskError(String error) {

                LogApp.d(TAG, String.valueOf(error));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (locationFramework != null)
            locationFramework.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationFramework != null)
            locationFramework.onPause();
    }

}
