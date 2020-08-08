package it.uniba.di.sms1920.everit.customer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.accountDetail.AccountDetailActivity;
import it.uniba.di.sms1920.everit.customer.activities.cartActivity.CartActivity;
import it.uniba.di.sms1920.everit.customer.activities.orders.OrderListActivity;
import it.uniba.di.sms1920.everit.customer.activities.reviews.ReviewListActivity;
import it.uniba.di.sms1920.everit.customer.cart.Cart;
import it.uniba.di.sms1920.everit.utils.models.Customer;
import it.uniba.di.sms1920.everit.utils.provider.NoSuchCredentialException;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;


public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        drawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar =  findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.inflateHeaderView(it.uniba.di.sms1920.everit.utils.R.layout.nav_view_header);
        TextView headerNameDisplay = headerView.findViewById(R.id.TextViewDrawer);

        if(Providers.getAuthProvider().getUser() != null){
            navigationView.inflateMenu(R.menu.drawer_view);
        }else{
            try {
                Providers.getAuthProvider().loginFromSavedCredential(new RequestListener<Customer>() {
                    @Override
                    public void successResponse(Customer response) {
                        navigationView.inflateMenu(R.menu.drawer_view);
                        String userName = response.getName() + " " + response.getSurname();
                        headerNameDisplay.setText(userName);
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        navigationView.inflateMenu(R.menu.drawer_view_unlogged);
                    }
                });
            } catch (NoSuchCredentialException e) {
                navigationView.inflateMenu(R.menu.drawer_view_unlogged);
            }
        }

        init();
    }

    private void init() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:{
                NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.main, true).build();
                Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.homeFragment, null, navOptions);
                break;
            }
            case R.id.nav_order: {
                Intent intent = new Intent(getApplicationContext(), OrderListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_review: {
                Intent intent = new Intent(getApplicationContext(), ReviewListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_account_detail: {
                Intent intent = new Intent(getApplicationContext(), AccountDetailActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_signUp: {
                Intent goIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(goIntent);
                break;
            }
            case R.id.nav_login: {
                Intent goIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(goIntent);
                break;
            }
            case R.id.exit: {
                Providers.getAuthProvider().logout(new RequestListener<Boolean>() {
                    @Override
                    public void successResponse(Boolean response) {
                        //TODO creare stringa in string
                        Toast.makeText(getApplicationContext(), "Logout effettuato", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG);
                        //TODO gestire error response
                    }
                });

                finish();
                break;
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private boolean isValidDestination(int dest){
        return dest != Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:{
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }else{
                    return false;
                }
            }

            case R.id.goTo_cart:{
                if(Providers.getAuthProvider().getUser() != null) {
                    Intent cartIntent = new Intent(getApplicationContext(), CartActivity.class);
                    startActivity(cartIntent);
                }else{
                    Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(loginIntent);
                }
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), drawerLayout);
    }

    @Override
    protected void onDestroy() {
        Cart cart = Cart.getInstance();

        if ((cart != null) && (!cart.isEmpty())) {
            cart.saveToFile();
        }

        super.onDestroy();
    }
}