package com.example.lakbaydriver;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "MapsActivity";
    private static final  int ERROR_DIALOG_REQUEST = 9001;
    NavigationView navigationView;

    private Boolean isLoggingOut = false;

    public FirebaseAuth nAuth;
    public FirebaseAuth.AuthStateListener firebaseAuthListener;
    public DatabaseReference userdata;

    private String userID;

    private String firstName, lastName, profileImageUrl;
    TextView email, name;
    ImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toggle.setDrawerIndicatorEnabled(true); // Ensure the default icon is shown


        startService(new Intent(DashboardActivity.this, OnAppKilled.class));

        if (isServicesOK()){
            navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

//            FragmentTransaction manager = getSupportFragmentManager().beginTransaction();
        }
//        onNavigationItemSelected(navigationView.getMenu().getItem(0));
//        navigationView.getMenu().getItem(0).setChecked(true);



        HomeFragment homeFragment = new HomeFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main_layout, homeFragment).commit();


        email = navigationView.getHeaderView(0).findViewById(R.id.tvEmail);
        name = navigationView.getHeaderView(0).findViewById(R.id.tvName);
        userImage = navigationView.getHeaderView(0).findViewById(R.id.ivImage);



        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String fEmail = user.getEmail();
        email.setText(fEmail);

        nAuth = FirebaseAuth.getInstance();
        userID = nAuth.getCurrentUser().getUid();
        userdata = FirebaseDatabase.getInstance().getReference().child("drivers").child(userID);

        getUserInfo();

    }


    private void getUserInfo(){
        userdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("firstName") != null && map.get("lastName") != null){
                        firstName = map.get("firstName").toString();
                        lastName = map.get("lastName").toString();
                        name.setText(firstName + " " + lastName);
//                        userName.setText(new UserPreference().getUser(MainActivity.this).userName);
                    }
                    if (map.get("imageUrl") != null){
                        profileImageUrl = map.get("imageUrl").toString();
                        Glide.with(getApplication()).load(profileImageUrl).into(userImage);
                    }               
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
//    private void init(){
//        Button BtnMap = findViewById(R.id.btnmap);
//        btnmap.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Intent intent = new Intent(MapsActivity.this);
//            }
//        });
//    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: cheking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(DashboardActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything fine and user can make map request
            Log.d(TAG, "isServicesOK: Google Play Services is Working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(DashboardActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else {
            Toast.makeText(this, "You can't make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_payment) {
//            PaymentFragment paymentFragment = new PaymentFragment();
//            FragmentManager manager = getSupportFragmentManager();
//            manager.beginTransaction().replace(R.id.main_layout, paymentFragment).commit();
                Intent payment = new Intent(DashboardActivity.this, PaymentActivity.class);
                startActivity(payment);

        } else if (id == R.id.nav_history) {
            Intent history = new Intent(DashboardActivity.this, HistoryActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("customerOrDriver", "clients");
            history.putExtra("customerOrDriver", "drivers");
            startActivity(history);


        } else if (id == R.id.nav_settings) {
            Intent settings = new Intent(DashboardActivity.this, AccountSettingsActivity.class);
            startActivity(settings);

        } else if (id == R.id.nav_signout) {
                isLoggingOut = true;
                disconnectDriver();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();



        } else if (id == R.id.nav_send) {

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void disconnectDriver(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userdata = FirebaseDatabase.getInstance().getReference("drivers_available");

        GeoFire geoFire = new GeoFire(userdata);
        geoFire.removeLocation(userId);
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (!isLoggingOut){
//            disconnectDriver();
//        }
//    }


    //    private class GoogleMap {
//    }
//
//    private class LatLng {
//        public LatLng(int i, int i1) {
//        }
//    }
//
//    private class MarkerOptions {
//        public void position(LatLng sydney) {
//        }
//    }
}
