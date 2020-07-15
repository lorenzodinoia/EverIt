package it.uniba.di.sms1920.everit.customer.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.cartFragments.Cart1Fragment;
import it.uniba.di.sms1920.everit.customer.activities.cartFragments.CartEmptyFragment;
import it.uniba.di.sms1920.everit.customer.cart.Cart;
import it.uniba.di.sms1920.everit.customer.cart.CartConnector;
import it.uniba.di.sms1920.everit.customer.cart.PartialOrder;

public class CartActivity extends AppCompatActivity implements CartConnector {

    private Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        /**
        Toolbar toolbar = findViewById(R.id.toolbarCart);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

         */

        cart = getCart();

        if(cart.isEmpty()){
            CartEmptyFragment cartEmptyFragment = new CartEmptyFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.containerCartFragment, cartEmptyFragment).addToBackStack(null).commit();
        }else{
            Cart1Fragment cart1Fragment = new Cart1Fragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.containerCartFragment, cart1Fragment).addToBackStack(null).commit();
        }


        //bottone torna menu ristorante


        //bottone check carrello


        //viewPager

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



    @Override
    public Cart getCart() {
        Cart cart = Cart.getInstance();

        if (cart == null) {
            Cart.init(this);
            cart = Cart.getInstance();
        }
        return cart;
    }

    @Override
    public PartialOrder getPartialOrder() {
        return null;
    }



}