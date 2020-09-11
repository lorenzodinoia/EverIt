package it.uniba.di.sms1920.everit.customer.cartActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.cart.Cart;

public class CartActivity extends AppCompatActivity {

    private Cart cart;
    private static final String TAG = "MIN_PURCHASE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbarCart);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        Float minPurchase = intent.getFloatExtra(TAG, 1);

        cart = Cart.getInstance();

        if(cart.isEmpty()){
            TextView textViewEmptyCartMessage = this.findViewById(R.id.textViewEmptyCartMessage);
            textViewEmptyCartMessage.setVisibility(View.VISIBLE);
        }else{
            Bundle bundle = new Bundle();
            bundle.putFloat(TAG, minPurchase);
            Cart1Fragment cart1Fragment = new Cart1Fragment();
            cart1Fragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.containerCartFragment, cart1Fragment).addToBackStack(null).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            FragmentManager fm = getSupportFragmentManager();
            if(fm.getBackStackEntryCount() > 1){
                fm.popBackStack();
            }
            else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}

