package it.uniba.di.sms1920.everit.customer.activities.cartActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.cartActivity.Cart1Fragment;
import it.uniba.di.sms1920.everit.customer.activities.cartActivity.CartEmptyFragment;
import it.uniba.di.sms1920.everit.customer.cart.Cart;

public class CartActivity extends AppCompatActivity {

    private Cart cart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //TODO sta toolbar è messa da xml e non inclusa perchè altrimenti si rompe a caso
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCart);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        Float minPurchase = intent.getFloatExtra("MIN_PURCHASE", 1);

        cart = Cart.getInstance();

        if(cart.isEmpty()){
            CartEmptyFragment cartEmptyFragment = new CartEmptyFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.containerCartFragment, cartEmptyFragment).addToBackStack(null).commit();
        }else{
            Bundle bundle = new Bundle();
            bundle.putFloat("MIN_PURCHASE", minPurchase);
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

