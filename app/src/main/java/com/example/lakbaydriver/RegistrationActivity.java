package com.example.lakbaydriver;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    public Button submit;
    public EditText fname, lname, number;
    TextView bdate;
    Calendar date;
    int day, month, year;


    public FirebaseAuth nAuth;
    public DatabaseReference userdata;
    public FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        submit = findViewById(R.id.btn_submit);
        fname = findViewById(R.id.etfname);
        lname = findViewById(R.id.etlname);
        number = findViewById(R.id.mnumber);

        bdate = findViewById(R.id.bdate);
        date = Calendar.getInstance();

        day = date.get(Calendar.DAY_OF_MONTH);
        month = date.get(Calendar.MONTH);
        year = date.get(Calendar.YEAR);

        month = month+1;

        userdata = FirebaseDatabase.getInstance().getReference();

        nAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
//                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
//                    startActivity(intent);
//                    finish();
//                    return;
                }
            }
        };


//        bdate.setText(day+"/"+month+"/"+year);

        bdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegistrationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear + 1;
                        bdate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                    }
                },year, month, day);
                datePickerDialog.show();
            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> userinfo = new HashMap<String, String>();

                final String firstname = fname.getText().toString();
                final String lastname = lname.getText().toString();
                final String mobilenumber = number.getText().toString();
                final String birthdate = bdate.getText().toString();

                if(TextUtils.isEmpty(firstname) && TextUtils.isEmpty(lastname) && TextUtils.isEmpty(mobilenumber)){
                    Toast.makeText(getApplicationContext(), "Enter Your Information", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(firstname)) {
                    Toast.makeText(getApplicationContext(), "Enter Your First Name.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(lastname)) {
                    Toast.makeText(getApplicationContext(), "Enter Your Last Name.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(mobilenumber)) {
                    Toast.makeText(getApplicationContext(), "Enter Your Mobile Number.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(birthdate)) {
                    Toast.makeText(getApplicationContext(), "Enter Your Birthdate.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {

                    userinfo.put("driver_firstname", firstname);
                    userinfo.put("driver_lastname", lastname);
                    userinfo.put("driver_mobile", mobilenumber);
                    userinfo.put("driver_birthdate", birthdate);


                    String user_id = nAuth.getCurrentUser().getUid();
                    userdata.child("drivers").child(user_id).setValue(userinfo);



//

                }
                nAuth.signOut();
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        nAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        nAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
