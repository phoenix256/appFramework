package tr.wolflame.framework.base.util.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import tr.wolflame.framework.R;
import tr.wolflame.framework.base.adapters.CustomInfoWindowAdapter;
import tr.wolflame.framework.base.util.LogApp;

/**
 * A {@link LocationSource} which reports a new location whenever a user long presses the map
 * at
 * the point at which a user long pressed the map.
 */
public class MapLocationFramework implements LocationSource, GoogleMap.OnMapLongClickListener, GoogleMap.OnMapClickListener {

    private final String TAG = this.getClass().getSimpleName();

    private OnLocationChangedListener mListener;

    private final static int ACCURACY = 100;

    protected GoogleMap googleMap;
    protected UiSettings uiSettings;

    private final Context context;

    private boolean clearMarkers = false;
    private boolean isOnClickActionEnabled = true;
    private boolean isOnLongClickActionEnabled = true;

    private Marker internalMarker;

    // Define color of marker icon
    private BitmapDescriptor DEFAULT_MARKER_ICON =
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);

    public MapLocationFramework(Context context, final GoogleMap googleMap, final UiSettings uiSettings) {
        this.googleMap = googleMap;
        this.uiSettings = uiSettings;
        this.context = context;

        initMethod();
    }

    private void initMethod() {

        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setMapToolbarEnabled(false);

        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLongClickListener(this);

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return false;
            }
        });

        googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(context));

        googleMap.setLocationSource(this);

        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                final Location location = new GPSTracker(context).getLocation();
                if (location != null) {
                    // Animating to the currently touched position
                    //googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                    onMapClick(new LatLng(location.getLatitude(), location.getLongitude()));
                }

                return false;
            }
        });
    }


    /**
     * Flag to keep track of the activity's lifecycle. This is not strictly necessary in this
     * case because onMapLongPress events don't occur while the activity containing the map is
     * paused but is included to demonstrate best practices (e.g., if a background service were
     * to be used).
     */
    private boolean mPaused = false;

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
    }

    public void clearMarkers(boolean clearMarkers) {
        this.clearMarkers = clearMarkers;
    }

    public void setOnClickActionEnabled(boolean isOnClickActionEnabled) {
        this.isOnClickActionEnabled = isOnClickActionEnabled;
    }

    public void setOnLongClickActionEnabled(boolean isOnLongClickActionEnabled) {
        this.isOnLongClickActionEnabled = isOnLongClickActionEnabled;
    }

    @Override
    public void deactivate() {
        mListener = null;
    }

    @Override
    public void onMapLongClick(LatLng point) {
        LogApp.d(TAG, String.valueOf("onMapLongClick"));

        if (isOnLongClickActionEnabled)
            onLongClickAction(point);
    }

    @Override
    public void onMapClick(LatLng point) {
        LogApp.d(TAG, String.valueOf("onMapClick"));

        if (isOnClickActionEnabled)
            onClickAction(point);
    }

    public void onPause() {
        mPaused = true;
    }

    public void onResume() {
        mPaused = false;
    }


    protected void onClickAction(LatLng point) {

        if (clearMarkers)
            googleMap.clear(); // Clears any existing markers from the GoogleMap


        if (internalMarker != null) {
            internalMarker.remove();
            internalMarker = null;
        }

        // Creating an instance of MarkerOptions to set position
        final MarkerOptions markerOptions = new MarkerOptions();

        // Setting position on the MarkerOptions
        markerOptions.position(point).title("Title").snippet("Some Description").icon(DEFAULT_MARKER_ICON);

        final float currentZoomLevel = googleMap.getCameraPosition().zoom;

        final float customZoomLevel = googleMap.getMinZoomLevel() * 2;

        // Animating to the currently touched position
        if (currentZoomLevel > customZoomLevel) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(point));
        } else {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, googleMap.getMinZoomLevel() * 3));
        }


        // Adding marker on the GoogleMap
        internalMarker = googleMap.addMarker(markerOptions);

        // Showing InfoWindow on the GoogleMap
        internalMarker.showInfoWindow();

    }

    protected void onLongClickAction(LatLng point) {
        if (mListener != null && !mPaused) {
            /*final Location location = new Location("LongPressLocationProvider");
            location.setLatitude(point.latitude);
            location.setLongitude(point.longitude);
            location.setAccuracy(ACCURACY);
            mListener.onLocationChanged(location);*/
            showAlertDialogForPoint(point);
        }
    }


    private void dropPinEffect(final Marker marker) {
        // Handler allows us to repeat a code block after a specified delay
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        // Use the bounce interpolator
        final android.view.animation.Interpolator interpolator =
                new BounceInterpolator();

        // Animate marker with a bounce updating its position every 15ms
        handler.post(new Runnable() {
            @Override
            public void run() {
                final long elapsed = SystemClock.uptimeMillis() - start;
                // Calculate t for bounce based on elapsed time
                final float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                // Set the anchor
                marker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    // Post this event again 15ms from now.
                    handler.postDelayed(this, 15);
                } else { // done elapsing, show window
                    marker.showInfoWindow();
                }
            }
        });
    }

    // Display the alert that adds the marker
    private void showAlertDialogForPoint(final LatLng point) {
        // inflate message_item.xml view
        final View messageView = LayoutInflater.from(context).
                inflate(R.layout.layout_message_item, null);
        // Create alert dialog builder
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // set message_item.xml to AlertDialog builder
        alertDialogBuilder.setView(messageView);

        // Create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        // Configure dialog button (OK)
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Extract content from alert dialog
                        String title = ((EditText) alertDialog.findViewById(R.id.etTitle)).
                                getText().toString();
                        String snippet = ((EditText) alertDialog.findViewById(R.id.etSnippet)).
                                getText().toString();
                        // Creates and adds marker to the map
                        final Marker marker = googleMap.addMarker(new MarkerOptions()
                                .position(point)
                                .title(title)
                                .snippet(snippet)
                                .icon(DEFAULT_MARKER_ICON));

                        // Animate marker using drop effect
                        // --> Call the dropPinEffect method here
                        dropPinEffect(marker);

                    }
                });

        // Configure dialog button (Cancel)
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // Display the dialog
        alertDialog.show();
    }


    private void drawPolylines() {
        // Instantiates a new Polyline object and adds points to define a rectangle
        final PolylineOptions rectOptions = new PolylineOptions()
                .add(new LatLng(37.35, -122.0))
                .add(new LatLng(37.45, -122.0))  // North of the previous point, but at the same longitude
                .add(new LatLng(37.45, -122.2))  // Same latitude, and 30km to the west
                .add(new LatLng(37.35, -122.2))  // Same longitude, and 16km to the south
                .add(new LatLng(37.35, -122.0)); // Closes the polyline.
        // Get back the mutable Polyline
        final Polyline polyline = googleMap.addPolyline(rectOptions);
    }

    private void drawPolygons() {
        // Instantiates a new Polygon object and adds points to define a rectangle
        final PolygonOptions rectOptions = new PolygonOptions()
                .add(new LatLng(37.35, -122.0),
                        new LatLng(37.45, -122.0),
                        new LatLng(37.45, -122.2),
                        new LatLng(37.35, -122.2),
                        new LatLng(37.35, -122.0))
                .strokeColor(Color.RED).fillColor(Color.BLUE);
        // Get back the mutable Polygon
        final Polygon polygon = googleMap.addPolygon(rectOptions);
    }

    private void drawCircles() {
        // Instantiates a new CircleOptions object and defines the center and radius
        final CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(37.4, -122.1))
                .radius(1000); // In meters

        // Get back the mutable Circle
        final Circle circle = googleMap.addCircle(circleOptions);
    }

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public UiSettings getUiSettings() {
        return uiSettings;
    }

}