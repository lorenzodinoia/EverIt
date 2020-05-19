package it.uniba.di.sms1920.everit.customer;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapCircle;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;

import java.util.ArrayList;
import java.util.List;

import it.uniba.di.sms1920.everit.utils.Address;

/**
 * Class for handle common operation with map view of HERE Maps API
 */
public class MapFragmentManager {
    /**
     * Zoom level
     */
    private static final double MAP_DEFAULT_ZOOM = 18;
    /**
     * Map orientation in degrees. By default no orientation is provided
     */
    private static final float MAP_DEFAULT_ORIENTATION = 0;
    /**
     * Map tilt
     */
    private static final float MAP_DEFAULT_TILT = 45;

    /**
     * Activity which hold map fragment
     */
    private AppCompatActivity parentActivity;
    /**
     * Map fragment instance
     *
     * @see AndroidXMapFragment
     */
    private AndroidXMapFragment mapFragment;
    /**
     * Map instance on which all operations will be performed
     *
     * @see Map
     */
    private Map map;
    /**
     * List of map markers
     */
    private List<MapObject> mapMarkers = new ArrayList<>();

    /**
     * Build a new map
     *
     * @param parentActivity Activity which hold map fragment
     * @param mapFragmentId Fragment ID in XML layout
     */
    public MapFragmentManager(AppCompatActivity parentActivity, int mapFragmentId) {
        this.parentActivity = parentActivity;
        this.initMap(mapFragmentId);
    }

    /**
     * Build a map with the view set to an address
     *
     * @param parentActivity Activity which hold map fragment
     * @param mapFragmentId Fragment ID in XML layout
     * @param position Address which will be reached by the map view
     */
    public MapFragmentManager(AppCompatActivity parentActivity, int mapFragmentId, Address position) {
        this.parentActivity = parentActivity;
        this.initMap(mapFragmentId, position);
    }

    /**
     * Initialize the map
     *
     * @param mapFragmentId Fragment ID in XML layout
     */
    private void initMap(int mapFragmentId) {
        this.initMap(mapFragmentId, null);
    }

    /**
     * Initialize the map and set the view position on an address
     *
     * @param mapFragmentId Fragment ID in XML layout
     * @param position Address which will be reached by the map view
     */
    private void initMap(int mapFragmentId, Address position) {
        this.mapFragment = (AndroidXMapFragment) parentActivity.getSupportFragmentManager().findFragmentById(mapFragmentId);

        if (this.mapFragment != null) {
            this.mapFragment.init(error -> {
                if (error.equals(OnEngineInitListener.Error.NONE)) {
                    map = mapFragment.getMap();
                    if (map != null) {
                        if (position != null) {
                            setCurrentPosition(position.getLatitude(), position.getLongitude());
                        }
                    }
                    else {
                        showMapInitError();
                    }
                }
                else {
                    showMapInitError();
                }
            });
        }
        else {
            this.showMapInitError();
        }
    }

    /**
     * Displays a toast to inform the user that the map cannot be initialized
     */
    private void showMapInitError() {
        String message = parentActivity.getApplicationContext().getString(R.string.map_error);
        Toast.makeText(parentActivity.getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * Add a new marker on the map
     *
     * @param address Marker's address
     */
    public void addMapMarker(Address address) {
        MapMarker mapMarker = new MapMarker();

        mapMarker.setCoordinate(new GeoCoordinate(address.getLatitude(), address.getLongitude()));
        mapMarker.setTitle(address.getFullAddress());
        this.mapMarkers.add(mapMarker);
        this.map.addMapObject(mapMarker);
    }

    /**
     * Remove all the markers from the map
     */
    public void cleanMapMarker() {
        if (!this.mapMarkers.isEmpty()) {
            this.map.removeMapObjects(this.mapMarkers);
            this.mapMarkers.clear();
        }
    }

    /**
     * Set a singleton marker removing all previously added markers.
     * A zoom over the marker will be performed
     *
     * @param address Marker's address
     */
    public void setSingleMapMarker(Address address) {
        this.cleanMapMarker();
        this.addMapMarker(address);
        this.map.setCenter(new GeoCoordinate(address.getLatitude(), address.getLongitude()), Map.Animation.BOW, MAP_DEFAULT_ZOOM, MAP_DEFAULT_ORIENTATION, MAP_DEFAULT_TILT);
    }

    /**
     * Set the "you are here" marker to show the current device's position
     *
     * @param latitude Device's latitude
     * @param longitude Device's longitude
     */
    public void setCurrentPosition(double latitude, double longitude) {
        GeoCoordinate coordinate = new GeoCoordinate(latitude, longitude);
        MapCircle youAreHereMarker = new MapCircle(3, coordinate);
        int fillColor = parentActivity.getColor(R.color.colorPrimary);
        int lineColor = parentActivity.getColor(R.color.colorAccent);
        youAreHereMarker.setFillColor(fillColor);
        youAreHereMarker.setLineColor(lineColor);
        youAreHereMarker.setLineWidth(10);

        this.map.addMapObject(youAreHereMarker);
        this.map.setCenter(coordinate, Map.Animation.NONE, MAP_DEFAULT_ZOOM, MAP_DEFAULT_ORIENTATION, MAP_DEFAULT_TILT);
    }
}
