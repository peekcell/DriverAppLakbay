package com.example.lakbaydriver;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.ListView;

import java.util.List;

import com.example.lakbaydriver.R;

public class PaymentActivity extends AppCompatActivity {

    ListView lvpmethods;
    List wew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvpmethods = findViewById(R.id.lvpmethods);

        String[] txtStrings = {"Cash"};
//        int[] icons = {R.drawable.};
//        wew.add("Cash");


//        lvpmethods.setAdapter(adapter);
    }

}
