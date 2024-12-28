package com.example.lakbaydriver;


import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.lakbaydriver.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, RoutingListener{
//, GoogleApiClient.OnConnectionFailedListener
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }

    private static final String TAG = "MapsActivity";

    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15.5f;
//    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
//            new LatLng(-40, -168), new LatLng(71, 136)
//    );
//
//    //widgets
//    private AutoCompleteTextView nsearchtext;
    private ImageView ngps;
//
    public FirebaseAuth nAuth;
    public DatabaseReference userdata;
    public FirebaseAuth.AuthStateListener firebaseAuthListener;
//    //vars
    private Boolean LocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedProviderClient;
//    private PlaceAutocompleteAdapter PlaceAutocompleteAdapter;
    private GoogleMap nmap;
    GoogleApiClient GoogleApiClient;
    Location LastLocation;
    LocationRequest LocationRequest;

    private int status = 0;

    private String customerID = "", destination;
    private LatLng destinationLatLng;
    private String firstName, lastName;
    private String driverFirstName, driverLastName;
    private float rideDistance, ridePrice;
//    private String carType;

    private String total, price;
    private TextView name, phone, ndestination;
    private ImageView userImage;
    private RelativeLayout Info;
    private Button rideStatus;
    private ProgressBar pBar;
    Marker destinationMarker;


    private Switch mWorkingSwitch;

    private Boolean isLoggingOut = false;

    Button confirm;
//    private PlaceInfo nPlace;
//    private LocationRequest mLocationRequest;


    public HomeFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        Log.d(TAG, "initMap: Initializing Map");

        polylines = new ArrayList<>();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
//        this.getActivity().requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
//        pBar.setVisibility(View.VISIBLE);


        name = v.findViewById(R.id.tvName);
        phone = v.findViewById(R.id.tvPhone);
        ndestination = v.findViewById(R.id.tvDestination);
        userImage = v.findViewById(R.id.userImage);
        rideStatus = v.findViewById(R.id.rideStatus);
        Info = v.findViewById(R.id.switch1);


        mWorkingSwitch = v.findViewById(R.id.workingSwitch);
        mWorkingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    connectDriver();
                    mWorkingSwitch.setText("ONLINE");
                }else {
                    disconnectDriver();
                    mWorkingSwitch.setText("OFFLINE");
                }
            }
        });

//        pBar = v.findViewById(R.id.pBar);

//        confirm = v.findViewById(R.id.confirm);
//        v.findViewById(R.id.viewer).setVisibility(this);

        Toast.makeText(getContext(), "HOmeact", Toast.LENGTH_SHORT).show();

//        nsearchtext = v.findViewById(R.id.input_search);
//        ngps = v.findViewById(R.id.ic_gps);
//

//        nsearchtext = v.findViewById(R.id.input_search);
        ngps = v.findViewById(R.id.ic_gps);
        rideStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (status){
                    case 1:
                        status = 2;
                        erasePolylines();
                        if (destinationLatLng.latitude != 0.0 &&destinationLatLng.longitude != 0.0){
                            getRouteToMarker(destinationLatLng);

                            destinationMarker = nmap.addMarker(new MarkerOptions()
                                    .position(destinationLatLng)
                                    .title("Destination"));

                        }
                        rideStatus.setText("DRIVE COMPLETED");

                        break;

                    case 2:
                        recordRide();
                        endRide();

                        break;
                }
            }
        });

        ngps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked gps icon");
                getDeviceLocation();
            }
        });

        getLocationPermission();
//        getDeviceLocation();

        getAssignedCustomer();

        return v;
    }


    private void endRide(){
        rideStatus.setText("PICK UP CUSTOMER");
        erasePolylines();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("drivers").child(userId).child("client_request");
        driverRef.removeValue();

        DatabaseReference userdata = FirebaseDatabase.getInstance().getReference("clients_request");
        GeoFire geoFire = new GeoFire(userdata);
        geoFire.removeLocation(customerID);
        customerID = "";
        rideDistance = 0;

        if (pickupMarker != null){
            pickupMarker.remove();
        }
        if (destinationMarker != null){
            destinationMarker.remove();
        }
        if (assignedCustomerPickupLocationRefListener != null){
            assignedCustomerPickupLocationRef.removeEventListener(assignedCustomerPickupLocationRefListener);
        }
        Info.setVisibility(View.GONE);
        name.setText("");
        phone.setText("");
        ndestination.setText("Destination: ");
        userImage.setImageResource(R.mipmap.default_user);

    }

    private void recordRide(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("drivers").child(userId).child("history");
        DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference().child("clients").child(customerID).child("history");
        DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference().child("history");
        String requestID = historyRef.push().getKey();
        driverRef.child(requestID).setValue(true);
        customerRef.child(requestID).setValue(true);

        DatabaseReference drivertype = FirebaseDatabase.getInstance().getReference().child("drivers").child(userId).child("car_type");
        drivertype.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                   String carType = dataSnapshot.getValue().toString();
                   if (carType == "single"){
                       float basePrice = 70;
                       float perKm = 10;
                       ridePrice = ((rideDistance*perKm)+basePrice);
                       total = String.format("%.2f", ridePrice);
                       price = total;
                   } else if (carType == "family"){
                       float basePrice = 110;
                       float perKm = 15;
                       ridePrice = ((rideDistance*perKm)+basePrice);
                       total = String.format("%.2f", ridePrice);
                       price = String.valueOf(total);
                   }
                   else if (carType == "barkada"){
                       float basePrice = 160;
                       float perKm = 30;
                       ridePrice = ((rideDistance*perKm)+basePrice);
                       total = String.format("%.2f", ridePrice);
                       price = String.valueOf(total);
                   }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        HashMap map = new HashMap();
        map.put("driver", userId);
        map.put("driver_name", driverFirstName +" "+ driverLastName);
        map.put("customer", customerID);
        map.put("customer_name", firstName +" "+ lastName);
        map.put("rating", 0);
        map.put("timestamp", getCurrentTimestamp());
        map.put("destination", destination);
        map.put("location/from/lat", pickupLatLng.latitude);
        map.put("location/from/lng", pickupLatLng.longitude);
        map.put("location/to/lat", destinationLatLng.latitude);
        map.put("location/to/lng", destinationLatLng.longitude);
        map.put("distance", s);
        map.put("price", total);
        Toast.makeText(getContext(), price, Toast.LENGTH_SHORT).show();

//        map.put("price", ridePrice);


        historyRef.child(requestID).updateChildren(map);

    }

    private Long getCurrentTimestamp() {
        Long timestamp = System.currentTimeMillis()/1000;
        return timestamp;
    }


    private void getAssignedCustomer(){
        String driverID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference assignedCustomerRef = FirebaseDatabase.getInstance().getReference().child("drivers").child(driverID).child("client_request").child("customerRideID");
        assignedCustomerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
//                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
//                    if (map.get("customerRideID") != null){
//                        customerID = map.get("customerRideID").toString();
                    status = 1;
                    customerID = dataSnapshot.getValue().toString();
                    getAssignedCustomerPickupLocation();
                    getAssignedCustomerInfo();
                    getDriverInfo();
                    getAssignedCustomerDestination();
//                    }
                } else {
                    endRide();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getDriverInfo(){
        String driverID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userdata = FirebaseDatabase.getInstance().getReference().child("drivers").child(driverID);
        userdata.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
//                    pBar.setVisibility(View.GONE);
                    Info.setVisibility(View.VISIBLE);
                    if (map.get("firstName") != null && map.get("lastName") != null){
                        driverFirstName = map.get("firstName").toString();
                        driverLastName = map.get("lastName").toString();

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getAssignedCustomerInfo(){
        userdata = FirebaseDatabase.getInstance().getReference().child("clients").child(customerID);
        userdata.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
//                    pBar.setVisibility(View.GONE);
                    Info.setVisibility(View.VISIBLE);
                    if (map.get("firstName") != null && map.get("lastName") != null){
                        firstName = map.get("firstName").toString();
                        lastName = map.get("lastName").toString();
                        name.setText(firstName + " " + lastName);
                    }
                    if (map.get("mobile") != null){
                        phone.setText(map.get("mobile").toString());
                    }
                    if (map.get("imageUrl") != null){
                        Glide.with(getActivity().getApplication()).load(map.get("imageUrl").toString()).into(userImage);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getAssignedCustomerDestination(){
        String driverID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference assignedCustomerRef = FirebaseDatabase.getInstance().getReference().child("drivers").child(driverID).child("client_request");
        assignedCustomerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("destination") != null){
                        destination = map.get("destination").toString();
                        ndestination.setText("Destination: " + destination);
                    } else {
                        ndestination.setText("Destination: ");
                    }

                    Double destinationLat = 0.0;
                    Double destinationLng = 0.0;

                    if (map.get("destinationLat") != null){
                        destinationLat = Double.valueOf(map.get("destinationLat").toString());
                    }
                    if (map.get("destinationLng") != null){
                        destinationLng = Double.valueOf(map.get("destinationLng").toString());
                        destinationLatLng = new LatLng(destinationLat, destinationLng);
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    LatLng pickupLatLng;
    Marker pickupMarker;
    private DatabaseReference assignedCustomerPickupLocationRef;
    private ValueEventListener assignedCustomerPickupLocationRefListener;
    private void getAssignedCustomerPickupLocation(){
        assignedCustomerPickupLocationRef = FirebaseDatabase.getInstance().getReference().child("clients_request").child(customerID).child("l");
        assignedCustomerPickupLocationRefListener = assignedCustomerPickupLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && !customerID.equals("")){
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLng = 0;
                    if (map.get(0) != null){
                        Object latObject = map.get(0);
                        locationLat = Double.parseDouble(latObject.toString());
//                        locationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if (map.get(1) != null){
                        Object lngObject = map.get(1);
                        locationLng = Double.parseDouble(lngObject.toString());
//                        locationLng = Double.parseDouble(map.get(0).toString());
                    }
                    pickupLatLng = new LatLng(locationLat, locationLng);
                    if(pickupMarker != null){
                        pickupMarker.remove();
                    }
                    pickupMarker = nmap.addMarker(new MarkerOptions()
                            .position(pickupLatLng)
                            .title("Pickup Location")
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pickup)));

                    getRouteToMarker(pickupLatLng);

//                    clientMarker = nmap.addMarker(new MarkerOptions()
//                            .position(clientLatLng)
//                            .title("Pickup Location"));
//                    nmap.addMarker(new MarkerOptions().position(clientLatLng).title("Pickup Location"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getRouteToMarker(LatLng pickupLatLng) {
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(new LatLng(LastLocation.getLatitude(), LastLocation.getLongitude()), pickupLatLng)
                .build();
        routing.execute();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        Toast.makeText(getContext(), "Map is ready", Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "onMapReady: Map is Ready");
        nmap = googleMap;
//
//        if (mLocationPermissionGranted) {
//            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(getActivity(), FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
               return;
            }
            buildGoogleApiClient();
            nmap.setMyLocationEnabled(true);
            nmap.getUiSettings().setMyLocationButtonEnabled(false);
//            map.getUiSettings().setCompassEnabled(true);

//            init();

//        }


    }

    protected synchronized void buildGoogleApiClient(){
        GoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        GoogleApiClient.connect();
    }

    private String s;

    @Override
    public void onLocationChanged(Location location) {
        if (getContext() != null){

            if (!customerID.equals("")){
//                rideDistance += LastLocation.distanceTo(location)/1000;

                Location loc1 = new Location("");
                loc1.setLatitude(pickupLatLng.latitude);
                loc1.setLongitude(pickupLatLng.longitude);

                Location loc2 = new Location("");
                loc2.setLatitude(destinationLatLng.latitude);
                loc2.setLongitude(destinationLatLng.longitude);

                float adistance = loc1.distanceTo(loc2)/1000;
//                s = String.format("%.2f", distance);

                rideDistance = adistance;
            }

            LastLocation = location;

//        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
//        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            moveCamera(new LatLng(location.getLatitude(), location.getLongitude()),
                    DEFAULT_ZOOM,
                    "My Location");

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference refAvailable = FirebaseDatabase.getInstance().getReference("drivers_available");
            DatabaseReference refWorking = FirebaseDatabase.getInstance().getReference("drivers_working");
            GeoFire geoFireAvailable = new GeoFire(refAvailable);
            GeoFire geoFireWorking = new GeoFire(refWorking);

            if (!customerID.isEmpty()){
                geoFireAvailable.removeLocation(userId);
                geoFireWorking.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
            } else if (customerID.isEmpty()){
                geoFireAvailable.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
                geoFireWorking.removeLocation(userId);
            }

//            switch (customerID){
//                case "":
//                    geoFireWorking.removeLocation(userId);
//                    geoFireAvailable.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
//                    break;
//
//                default:
//                    geoFireAvailable.removeLocation(userId);
//                    geoFireWorking.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
//                    break;
//            }

        }
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest = new LocationRequest();
        LocationRequest.setInterval(3000);
        LocationRequest.setFastestInterval(3000);
        LocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: Moving the Camera to Lat" + latLng.latitude + ", lng" + latLng.longitude);
//        nmap.setMinZoomPreference(5.5f);
        nmap.setMaxZoomPreference(19.0f);
        nmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
//
//        if (!title.equals("My Location")){
//            MarkerOptions options = new MarkerOptions()
//                    .position(latLng)
//                    .title(title)
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_end));
//
//
//            nmap.addMarker(options);
//        }
//        hideSoftKeyboard();
    }


    private void hideSoftKeyboard(){
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: Getting the Device Current Location");

        mFusedProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        try {
            if (LocationPermissionGranted) {

                Task location = mFusedProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Log.d(TAG, "onComplete: found location!");
                            final Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,
                                    "My Location");


                        } else {
                            Log.d(TAG, "onComplete: Current Location is Null");
                            Toast.makeText(getContext(), "Unable to get Current Location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: Security Exception" + e.getMessage());
        }
    }
//
//    private void init(){
//        Log.d(TAG, "init: initializing");
//
//        GoogleApiClient = new GoogleApiClient
//                .Builder(getActivity())
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(getActivity(), this)
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
//
//
//
//        PlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(getContext(),
//                GoogleApiClient, LAT_LNG_BOUNDS, autocompleteFilter);
//
//        nsearchtext.setAdapter(PlaceAutocompleteAdapter);
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
//        Geocoder geocoder = new Geocoder(getContext());
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
//            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
//                    address.getAddressLine(0));
//        }
//    }
//
//    private void getDeviceLocation() {
//        Log.d(TAG, "getDeviceLocation: Getting the Device Current Location");
//
//        mFusedProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
//
//        try {
//            if (mLocationPermissionGranted) {
//
//                Task location = mFusedProviderClient.getLastLocation();
//                location.addOnCompleteListener(new OnCompleteListener() {
//                    @Override
//                    public void onComplete(@NonNull Task task) {
//                        if (task.isSuccessful() && task.getResult() != null) {
//                            Log.d(TAG, "onComplete: found location!");
//                            currentLocation = (Location) task.getResult();
//
//                            mLocationRequest = new LocationRequest();
//                            mLocationRequest.setInterval(100);
//                            mLocationRequest.setFastestInterval(100);
//                            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
//                                    DEFAULT_ZOOM,
//                                    "My Location");
//
//                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                            DatabaseReference userdata = FirebaseDatabase.getInstance().getReference("drivers_available");
//
//                            GeoFire geoFire = new GeoFire(userdata);
//                            geoFire.setLocation(userId, new GeoLocation(currentLocation.getLatitude(), currentLocation.getLongitude()));
//
//                        } else {
//                            Log.d(TAG, "onComplete: Current Location is Null");
//                            Toast.makeText(getContext(), "Unable to get Current Location", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        } catch (SecurityException e) {
//            Log.d(TAG, "getDeviceLocation: Security Exception" + e.getMessage());
//        }
//    }
//


    private void initMap(){
        Log.d(TAG, "initMap: Initializing Map");
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: Getting Location Permissions");
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getActivity(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            if (ContextCompat.checkSelfPermission(this.getActivity(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                LocationPermissionGranted = true;
                initMap();
            }else {
                ActivityCompat.requestPermissions(getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }else {
            ActivityCompat.requestPermissions(getActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: Called.");
        LocationPermissionGranted = false;
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if (grantResults.length > 0){
                    for (int i = 0; i < grantResults.length; i++){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            LocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: Permission Failed!");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: Permission Granted");
                    LocationPermissionGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }

    }

    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.colorAccent};

    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getContext(), "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int i) {

        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int e = 0; e <route.size(); e++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(15 + e * 3);
            polyOptions.addAll(route.get(e).getPoints());
            Polyline polyline = nmap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getContext(),"Route "+ (e+1) +": distance - "+ route.get(e).getDistanceValue()+": duration - "+ route.get(e).getDurationValue(),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRoutingCancelled() {

    }

    private void erasePolylines(){
        for (Polyline line : polylines){
            line.remove();
        }
        polylines.clear();
    }





    @Override
    public void onPause() {
        super.onPause();
//        GoogleApiClient.stopAutoManage(getActivity());
//        GoogleApiClient.disconnect();
//        nAuth.addAuthStateListener(firebaseAuthListener);
    }

//    private void disconnectDriver(){
//        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference userdata = FirebaseDatabase.getInstance().getReference("drivers_available");
//
//        GeoFire geoFire = new GeoFire(userdata);
//        geoFire.removeLocation(userId);
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
////        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
////        DatabaseReference userdata = FirebaseDatabase.getInstance().getReference("drivers_available");
////
////        GeoFire geoFireAvailable = new GeoFire(userdata);
////        geoFireAvailable.setLocation(userId, new GeoLocation(LastLocation.getLatitude(), LastLocation.getLongitude()));
//
//
//
//    }

    @Override
    public void onStop() {
        super.onStop();
        if (GoogleApiClient != null && GoogleApiClient.isConnected()) {
//            GoogleApiClient.stopAutoManage(getActivity());
            GoogleApiClient.disconnect();
//            nAuth.removeAuthStateListener(firebaseAuthListener);
        }



    }

    private void connectDriver(){
        if (ActivityCompat.checkSelfPermission(getContext(), FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(GoogleApiClient, LocationRequest, this);
    }


    public void disconnectDriver(){

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userdata = FirebaseDatabase.getInstance().getReference("drivers_available");

        GeoFire geoFire = new GeoFire(userdata);
        geoFire.removeLocation(userId);
    }



}














