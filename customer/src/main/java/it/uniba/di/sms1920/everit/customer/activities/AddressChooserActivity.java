package it.uniba.di.sms1920.everit.customer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.search.AutoSuggest;
import com.here.android.mpa.search.AutoSuggestPlace;
import com.here.android.mpa.search.Place;
import com.here.android.mpa.search.TextAutoSuggestionRequest;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import it.uniba.di.sms1920.everit.customer.MapFragmentManager;
import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.Address;
import it.uniba.di.sms1920.everit.utils.provider.LocationProvider;

/**
 * Class used to handle Address Chooser Activity which helps user to choose the correct delivery address for his order
 */
public class AddressChooserActivity extends AppCompatActivity {
    /**
     * Intent parameter used from caller activity to send the searching query
     */
    public static final String PARAMETER_QUERY = "query";
    /**
     * Intent request ID used from caller activity to request the chosen address as result
     */
    public static final int REQUEST_ADDRESS = 1;
    /**
     * Key used to get the chosen address from result intent extras
     */
    public static final String RESULT_ADDRESS = "chosenAddress";
    /**
     * User default location used if that GPS permissions are not granted or the location is unknown
     */
    private static final Address DEFAULT_POSITION = new Address(41.107834, 16.880462, "via Edoardo Orabona", "Bari");

    private static final String SAVED_USER_QUERY = "saved.user_query";

    /**
     * Current user position, initialized to default position
     */
    private Address userPosition = DEFAULT_POSITION;
    /**
     * HERE Map manager which handles all operations on map fragment
     * @see MapFragmentManager
     */
    private MapFragmentManager mapFragmentManager;
    private TextInputEditText editTextQuery;
    private MaterialButton buttonSearch;
    private MaterialButton buttonBack;
    private FrameLayout frameAddressList;
    private FrameLayout frameAddressDetails;
    private TextView textViewAddressDetail;
    private TextView textViewCityDetail;
    private FloatingActionButton fabConfirmAddress;
    private RecyclerView recyclerView;
    private AddressRecyclerViewAdapter recyclerViewAdapter;
    /**
     * List of suggested places by HERE Maps API
     */
    private List<AutoSuggestPlace> suggestPlaces = new ArrayList<>();
    private String userQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_chooser);

        this.initComponents();

        Intent intent = getIntent();
        String query;
        if (savedInstanceState == null) {
            if ((intent != null) && ((query = intent.getStringExtra(PARAMETER_QUERY)) != null)) {
                //Query string is provided, so the autocomplete request is sent
                this.userQuery = query;
                this.editTextQuery.setText(this.userQuery);
            }
        }
        else if (savedInstanceState.containsKey(SAVED_USER_QUERY)) {
            this.userQuery = savedInstanceState.getString(SAVED_USER_QUERY);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (this.userQuery != null) {
            this.autocomplete(this.userQuery);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_USER_QUERY, this.userQuery);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LocationProvider.REQUEST_PERMISSION_GPS) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                //GPS permissions are granted then get the current position and loads the map
                loadCurrentPosition();
            }
            else {
                //GPS permissions are not granted then load the map without set the current position (default position will be used)
                Toast.makeText(getApplicationContext(), getString(R.string.message_no_gps_permission), Toast.LENGTH_LONG).show();
                initMap();
            }
        }
    }

    /**
     * Uses FusedLocationProviderClient API by Google Play Services to get the last known position of the device.
     * If the FusedLocationProviderClient gets the last known location successfully the user location is set and the map will be initialized
     *
     * @see FusedLocationProviderClient
     */
    @SuppressLint("MissingPermission")
    private void loadCurrentPosition() {
        FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                userPosition = new Address(location);
            }
            if (mapFragmentManager == null) {
                initMap();
            }
        });
    }

    /**
     * Show a detailed message to explain why the app needs location permission
     */
    private void showExplanationForLocation() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.gps_permission_explanation))
                .setPositiveButton(getString(R.string.ok_default), (dialog, which) -> LocationProvider.requestPermissions(this))
                .setNegativeButton(getString(R.string.cancel_default), (dialog, which) -> {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), getString(R.string.message_no_gps_permission), Toast.LENGTH_LONG).show();
                })
                .create()
                .show();
    }

    /**
     * Initialize all the UI components and check for the GPS permissions
     */
    private void initComponents() {
        LocationProvider.PermissionStatus permissionStatus = LocationProvider.hasPermissions(this);

        switch (permissionStatus) {
            case GRANTED:
                this.loadCurrentPosition();
                break;
            case NOT_GRANTED:
                LocationProvider.requestPermissions(this);
                break;
            case NOT_GRANTED_SHOW_EXPLANATION:
                showExplanationForLocation();
                break;
        }

        this.frameAddressList = this.findViewById(R.id.frameAddressList);

        this.frameAddressDetails = this.findViewById(R.id.frameAddressDetails);
        this.textViewAddressDetail = this.frameAddressDetails.findViewById(R.id.textViewAddressDetail);
        this.textViewCityDetail = this.frameAddressDetails.findViewById(R.id.textViewCityDetail);
        this.fabConfirmAddress = this.frameAddressDetails.findViewById(R.id.fabConfirmAddress);
        this.fabConfirmAddress.setOnClickListener(v -> {
            //When the confirm button is pressed the activity is closed and returns the chosen address as result
            Object tag = this.frameAddressDetails.getTag();
            if (tag instanceof Place) {
                Place chosenPlace = (Place) tag;
                sendBackResult(chosenPlace);
            }
        });

        RecyclerView recyclerView = this.findViewById(R.id.address_list);
        this.recyclerViewAdapter = new AddressRecyclerViewAdapter(this, suggestPlaces);
        recyclerView.setAdapter(this.recyclerViewAdapter);

        this.editTextQuery = this.findViewById(R.id.editTextQuery);
        this.buttonSearch = this.findViewById(R.id.buttonSearchQuery);
        this.buttonSearch.setOnClickListener(v -> {
            String query = editTextQuery.getText().toString().trim();
            if (!query.equals("")) {
                autocomplete(query);
            }
        });
        this.buttonBack = this.findViewById(R.id.buttonBack);
        this.buttonBack.setHeight(this.editTextQuery.getHeight());
        this.buttonBack.setOnClickListener(v -> super.onBackPressed());
    }

    /**
     * Initialize the map manager
     * @see MapFragmentManager
     */
    private void initMap() {
        this.mapFragmentManager = new MapFragmentManager(this, R.id.mapFragment, this.userPosition);
    }

    /**
     * Handle the autocomplete request provided by HERE Maps API.
     * It request a set of address suggestions based on query string.
     * The suggested addresses are selected according to a starting geographical position
     *
     * @param query Query string
     */
    private void autocomplete(String query) {
        GeoCoordinate center = new GeoCoordinate(this.userPosition.getLatitude(), this.userPosition.getLongitude()); //Geographical position used as center by the search
        TextAutoSuggestionRequest textAutoSuggestionRequest = new TextAutoSuggestionRequest(query);

        textAutoSuggestionRequest.setFilters(EnumSet.of(TextAutoSuggestionRequest.AutoSuggestFilterType.ADDRESS)); //It suggests only address (no companies, restaurants and other point of interest)
        textAutoSuggestionRequest.setSearchCenter(center);
        textAutoSuggestionRequest.setCollectionSize(3); //Maximum number of results to refine suggestions
        textAutoSuggestionRequest.setLocale(Locale.getDefault());

        textAutoSuggestionRequest.execute((autoSuggests, errorCode) -> {
            if (autoSuggests != null) {
                if (autoSuggests.size() > 0) {
                    setSearchResults(autoSuggests);
                }
                else {
                    Toast.makeText(getApplicationContext(), getString(R.string.search_autocomplete_empty), Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), getString(R.string.search_autocomplete_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Set and show the results to the user
     *
     * @param results List of suggestions
     */
    private void setSearchResults(List<AutoSuggest> results) {
        this.frameAddressList.setVisibility(View.VISIBLE); //Show results list

        this.suggestPlaces.clear();
        for (AutoSuggest item : results) {
            if (item instanceof AutoSuggestPlace) {
                AutoSuggestPlace suggestedPlace = (AutoSuggestPlace) item;
                this.suggestPlaces.add(suggestedPlace);
            }
        }

        this.recyclerViewAdapter.notifyDataSetChanged(); //Notify the adapter to update the layout
    }

    /**
     * Show the selected place on the map and a details card
     *
     * @param place Suggested address selected by the user
     */
    private void showPlace(Place place) {
        this.frameAddressList.setVisibility(View.GONE); //Closes the results list

        String address = place.getName();
        String city = place.getLocation().getAddress().getCity();
        double latitude = place.getLocation().getCoordinate().getLatitude();
        double longitude = place.getLocation().getCoordinate().getLongitude();

        this.mapFragmentManager.setSingleMapMarker(new Address(latitude, longitude, address, city)); //Set marker and zoom over it

        this.frameAddressDetails.setVisibility(View.VISIBLE); //Show details card
        this.frameAddressDetails.setTag(place);
        this.textViewAddressDetail.setText(address);
        this.textViewCityDetail.setText(city);
    }

    /**
     * Closes the activity sending the chosen address as result
     *
     * @param chosenPlace Suggested address selected and confirmed by the user
     */
    private void sendBackResult(Place chosenPlace) {
        String address = chosenPlace.getName();
        String city = chosenPlace.getLocation().getAddress().getCity();
        double latitude = chosenPlace.getLocation().getCoordinate().getLatitude();
        double longitude = chosenPlace.getLocation().getCoordinate().getLongitude();
        Address result = new Address(latitude, longitude, address, city);
        Intent resultIntent = new Intent();

        resultIntent.putExtra(RESULT_ADDRESS, result);

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    /**
     * Adapter for results list.
     * It handles item rendering and click event
     *
     * @see RecyclerView.Adapter
     */
    public static class AddressRecyclerViewAdapter extends RecyclerView.Adapter<AddressRecyclerViewAdapter.ViewHolder> {
        private final AddressChooserActivity parentActivity;
        private final List<AutoSuggestPlace> autoSuggestPlaces;
        /**
         * Item click event handler
         * Once suggestion is clicked a details request is performed
         */
        private final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoSuggestPlace suggestionClicked = (AutoSuggestPlace) view.getTag();
                if (suggestionClicked != null) {
                    suggestionClicked.getPlaceDetailsRequest().execute((place, errorCode) -> {
                        if (place != null) {
                            parentActivity.showPlace(place);
                        }
                    });
                }
            }
        };

        AddressRecyclerViewAdapter(AddressChooserActivity parentActivity, List<AutoSuggestPlace> addresses) {
            this.parentActivity = parentActivity;
            this.autoSuggestPlaces = addresses;
        }

        @NonNull
        @Override
        public AddressRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_list_content, parent, false);
            return new AddressRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AddressRecyclerViewAdapter.ViewHolder holder, int position) {
            AutoSuggestPlace suggestedPlace = this.autoSuggestPlaces.get(position);
            if (suggestedPlace != null) {
                holder.textViewAddress.setText(suggestedPlace.getTitle());
                holder.textViewCity.setText(suggestedPlace.getVicinity());

                holder.itemView.setTag(suggestedPlace);
                holder.itemView.setOnClickListener(this.onClickListener);
            }
        }

        @Override
        public int getItemCount() {
            return autoSuggestPlaces.size();
        }

        /**
         * Class which holds displayed data of a suggested address
         *
         * @see RecyclerView.ViewHolder
         */
        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView textViewAddress;
            final TextView textViewCity;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewAddress = itemView.findViewById(R.id.textViewAddress);
                textViewCity = itemView.findViewById(R.id.textViewCity);
            }
        }
    }
}
