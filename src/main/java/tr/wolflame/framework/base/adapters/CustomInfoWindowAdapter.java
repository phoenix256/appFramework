package tr.wolflame.framework.base.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import tr.wolflame.framework.R;

/**
 * Created by SADIK on 09/03/16.
 */
public class CustomInfoWindowAdapter implements InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        // Getting view from the layout file info_window_layout
        final View rootView = ((Activity) context).getLayoutInflater().inflate(R.layout.layout_info_window, null);

        // Getting the position from the marker
        final LatLng latLng = marker.getPosition();

        // Getting reference to the TextView to set latitude
        TextView tvLat = (TextView) rootView.findViewById(R.id.tv_lat);

        // Getting reference to the TextView to set longitude
        TextView tvLng = (TextView) rootView.findViewById(R.id.tv_lng);

        // Setting the latitude
        tvLat.setText("Latitude:" + latLng.latitude);

        // Setting the longitude
        tvLng.setText("Longitude:" + latLng.longitude);

        // Returning the view containing InfoWindow contents
        return rootView;
    }
}
