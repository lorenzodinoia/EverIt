package it.uniba.di.sms1920.everit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.Collection;

import it.uniba.di.sms1920.everit.models.City;
import it.uniba.di.sms1920.everit.request.CityRequest;
import it.uniba.di.sms1920.everit.request.RequestListener;
import it.uniba.di.sms1920.everit.request.RequestManager;

public class LauncherActivity extends AppCompatActivity {
    private static final float DELAY = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        RequestManager.init(getApplicationContext());
        //TODO Inserire logica per scegliere activity da avviare
        Handler handler = new Handler();
        handler.postDelayed(() -> {
                Intent intent = new Intent(LauncherActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                }, ((int) DELAY * 1000));

        /* NON TOCCARE PORCO DIO
        Customer customer = new Customer("Michele", "Appicciafuco", "3453243487", "checco@gmail.com");
        customer.setPassword("porcodio");
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.create(customer, new RequestListener<Customer>() {
            @Override
            public void successResponse(Customer response) {
                Toast.makeText(getApplicationContext(), response.getEmail(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void errorResponse(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
        });
         */
    }
}
