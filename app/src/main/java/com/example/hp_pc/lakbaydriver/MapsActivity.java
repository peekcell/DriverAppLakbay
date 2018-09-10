package com.example.hp_pc.lakbaydriver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{
//        , GoogleApiClient.OnConnectionFailedListener

//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }

//    private static final String TAG = "MapsActivity";
//
//    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
//    private static final String COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
//    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
//    private static final float DEFAULT_ZOOM = 15.5f;
//    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
//            new LatLng(-40, -168), new LatLng(71, 136));
//
//    //widgets
//    private AutoCompleteTextView nsearchtext;
//    private ImageView ngps;
//
//    //vars
//
//    public FirebaseAuth nAuth;
//    public DatabaseReference userdata;
//    public FirebaseAuth.AuthStateListener firebaseAuthListener;
//
//    private Boolean mLocationPermissionGranted = false;
//    private FusedLocationProviderClient mFusedProviderClient;
//    private PlaceAutocompleteAdapter nPlaceAutocompleteAdapter;
    private GoogleMap nmap;
//    private GoogleApiClient nGoogleApiClient;
//    private PlaceInfo nPlace;
//    private LocationRequest mLocationRequest;
//
//    private Boolean status;
//
//    Location currentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toast.makeText(MapsActivity.this,"mapsact", Toast.LENGTH_SHORT).show();
//        userdata = FirebaseDatabase.getInstance().getReference();
//        nAuth = FirebaseAuth.getInstance();
//
//
//        final Switch status =  findViewById(R.id.driverStatus);
//        Boolean switchState = status.isChecked();
//        status.setChecked(false);
//
//
//
//
//        nsearchtext = findViewById(R.id.input_search);
//        ngps = findViewById(R.id.ic_gps);
//
//        getLocationPermission();
//        getDeviceLocation();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
//        Toast.makeText(MapsActivity.this, "Map is ready", Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "onMapReady: Map is Ready");
        nmap = googleMap;
//
//        if (mLocationPermissionGranted) {
//            getDeviceLocation();
//
//            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            nmap.setMyLocationEnabled(true);
//            nmap.getUiSettings().setMyLocationButtonEnabled(false);
////            nmap.getUiSettings().setCompassEnabled(true);
//
//            init();
//
//        }


    }


//    private void init(){
//        Log.d(TAG, "init: initializing");
//
//        nGoogleApiClient = new GoogleApiClient
//                .Builder(this)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(this, this)
//                .build();
//
//        nsearchtext.setOnItemClickListener(nAutocompleteClickListener);
//
//        //COUNTRY FILTER
//        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
//                .setTypeFilter(Place.TYPE_COUNTRY)
//                .setCountry("PH")
//                .build();
//
//        nPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this,
//                nGoogleApiClient, LAT_LNG_BOUNDS, autocompleteFilter);
//
//        nsearchtext.setAdapter(nPlaceAutocompleteAdapter);
//
//        nsearchtext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
//                if(actionId == EditorInfo.IME_ACTION_SEARCH
//                        || actionId == EditorInfo.IME_ACTION_DONE
//                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
//                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
//
//                    //execute our method for searching
//
//                    geoLocate();
//                    hideSoftKeyboard();
//                }
//
//                return false;
//            }
//        });
//        ngps.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: clicked gps icon");
//                getDeviceLocation();
//            }
//        });
//        hideSoftKeyboard();
//    }
//
//    private void geoLocate(){
//        Log.d(TAG, "geoLocate: geolocating");
//
//        String searchString = nsearchtext.getText().toString();
//
//        Geocoder geocoder = new Geocoder(MapsActivity.this);
//        List<Address> list = new ArrayList<>();
//        try{
//            list = geocoder.getFromLocationName(searchString, 1);
//        }catch (IOException e){
//            Log.d(TAG, "geoLocate: IOException: " + e.getMessage());
//        }
//        if (list.size() > 0){
//            Address address = list.get(0);
//
//            Log.d(TAG, "geoLocate: found a location: " + address.toString());
//            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
//
//            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
//
//                    address.getAddressLine(0));
//
//        }
//    }
//
//    private void getDeviceLocation() {
//        Log.d(TAG, "getDeviceLocation: Getting the Device Current Location");
//
//        mFusedProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
//
//        try {
//            if (mLocationPermissionGranted) {
//
//                final Task location = mFusedProviderClient.getLastLocation();
//
//                location.addOnCompleteListener(new OnCompleteListener() {
//                    @Override
//                    public void onComplete(@NonNull Task task) {
//                        if (task.isSuccessful() && task.getResult() != null) {
//                            Log.d(TAG, "onComplete: found location!");
//                            Location currentLocation = (Location) task.getResult();
//
//                            mLocationRequest = new LocationRequest();
//                            mLocationRequest.setInterval(100);
//                            mLocationRequest.setFastestInterval(100);
//                            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//
//                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
//                                    DEFAULT_ZOOM,
//                                    "My Location");
//
//
//                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                            DatabaseReference userdata = FirebaseDatabase.getInstance().getReference("client_request");
//
//                            GeoFire geoFire = new GeoFire(userdata);
//                            geoFire.setLocation(userId, new GeoLocation(currentLocation.getLatitude(), currentLocation.getLongitude()));
//
//                        } else {
//                            Log.d(TAG, "onComplete: Current Location is Null");
//                            Toast.makeText(MapsActivity.this, "Unable to get Current Location", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        } catch (SecurityException e) {
//            Log.d(TAG, "getDeviceLocation: Security Exception" + e.getMessage());
//        }
//    }
//
//    private void moveCamera(LatLng latLng, float zoom, String title) {
//        Log.d(TAG, "moveCamera: Moving the Camera to Lat" + latLng.latitude + ", lng" + latLng.longitude);
//        nmap.setMinZoomPreference(5.5f);
//        nmap.setMaxZoomPreference(18.0f);
//        nmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
//        nmap.clear();
//        if (!title.equals("My Location")){
//            MarkerOptions options = new MarkerOptions()
//                    .position(latLng)
//                    .title(title)
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_end));
//
//            nmap.addMarker(options);
//        }
//        hideSoftKeyboard();
//    }





//    private void initMap(){
//        Log.d(TAG, "initMap: Initializing Map");
//        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }
//
//
//    private void getLocationPermission(){
//        Log.d(TAG, "getLocationPermission: Getting Location Permissions");
//        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION,
//                android.Manifest.permission.ACCESS_COARSE_LOCATION};
//        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
//                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
//            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
//                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                mLocationPermissionGranted = true;
//                initMap();
//            }else {
//                ActivityCompat.requestPermissions(this,
//                        permissions,
//                        LOCATION_PERMISSION_REQUEST_CODE);
//            }else {
//            ActivityCompat.requestPermissions(this,
//                    permissions,
//                    LOCATION_PERMISSION_REQUEST_CODE);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        Log.d(TAG, "onRequestPermissionsResult: Called.");
//        mLocationPermissionGranted = false;
//
//        switch (requestCode){
//            case LOCATION_PERMISSION_REQUEST_CODE:{
//                if (grantResults.length > 0){
//                    for (int i = 0; i < grantResults.length; i++){
//                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
//                            mLocationPermissionGranted = false;
//                            Log.d(TAG, "onRequestPermissionsResult: Permission Failed!");
//                            return;
//                        }
//                    }
//                    Log.d(TAG, "onRequestPermissionsResult: Permission Granted");
//                    mLocationPermissionGranted = true;
//                    //initialize our map
//                    initMap();
//                }
//            }
//        }
//    }
//
//    private void hideSoftKeyboard(){
//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//    }
//
//
//    /*
//    ---------------------------------google places api autocmplete suggestions
//     */
//
//    private AdapterView.OnItemClickListener nAutocompleteClickListener =    new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            hideSoftKeyboard();
//
//            final AutocompletePrediction item = nPlaceAutocompleteAdapter.getItem(i);
//            final String placeId = item.getPlaceId();
//
//            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
//                    .getPlaceById(nGoogleApiClient, placeId);
//            placeResult.setResultCallback(nUpdatePlaceDetailsCallback);
//        }
//    };
//
//    private ResultCallback<PlaceBuffer> nUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
//        @Override
//        public void onResult(@NonNull PlaceBuffer places) {
//            if (!places.getStatus().isSuccess()){
//                Log.d(TAG, "onResult: Place query  did not complete succesfully" + places.getStatus().toString());
//                places.release();
//                return;
//            }
//            final Place place = places.get(0);
//
//
//            try{
//                nPlace = new PlaceInfo();
//                nPlace.setName(place.getName().toString());
//                nPlace.setAddress(place.getAddress().toString());
//                nPlace.setId(place.getId());
//                nPlace.setRating(place.getRating());
//                nPlace.setPhoneNumber(place.getPhoneNumber().toString());
//                nPlace.setWebsiteuri(place.getWebsiteUri());
//
//                Log.d(TAG, "onResult: Place:" + nPlace.toString());
//            }catch (NullPointerException e){
//                Log.e(TAG, "onResult: NullPointerException" + e.getMessage());
//            }
//
//            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
//                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, nPlace.getName());
//
//            places.release();
//        }
//    };



    @Override
    protected void onStart() {
        super.onStart();
//        nAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        nAuth.removeAuthStateListener(firebaseAuthListener);

//        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference userdata = FirebaseDatabase.getInstance().getReference("drivers_available");
//
//        GeoFire geoFire = new GeoFire(userdata);
//        geoFire.removeLocation(userId);
    }







}


