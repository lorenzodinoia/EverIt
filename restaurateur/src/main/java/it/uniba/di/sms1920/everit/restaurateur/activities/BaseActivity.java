package it.uniba.di.sms1920.everit.restaurateur.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.restaurateur.activities.activeOrders.OrdersActivity;
import it.uniba.di.sms1920.everit.restaurateur.activities.openingTime.OpeningDateTimeActivity;
import it.uniba.di.sms1920.everit.restaurateur.activities.orderHistory.DoneOrderListActivity;
import it.uniba.di.sms1920.everit.restaurateur.activities.accountDetail.AccountDetailActivity;
import it.uniba.di.sms1920.everit.restaurateur.activities.review.ReviewListActivity;
import it.uniba.di.sms1920.everit.restaurateur.activities.signup.SignUpActivity;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Restaurateur restaurateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Toolbar toolbar =  findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerNav = navigationView.inflateHeaderView(R.layout.nav_view_header);

        /**
        if(Providers.getAuthProvider().getUser() != null){
            drawerLayout = findViewById(R.id.drawer_layout);
            this.restaurateur = (Restaurateur) Providers.getAuthProvider().getUser();

            Log.d("QUESTO", restaurateur.getShopName());
        }else{
            try {
                Providers.getAuthProvider().loginFromSavedCredential(new RequestListener<Restaurateur>() {
                    @Override
                    public void successResponse(Restaurateur response) {
                        restaurateur = response;
                        Log.d("QUESTO", restaurateur.getShopName());
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        //TODO gestire
                    }
                });
            } catch (NoSuchCredentialException e) {
                //TODO gestire
            }
        }
         */

        init();
    }

    private void init() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);

        navigationView.setNavigationItemSelectedListener(this);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) { }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), drawerLayout);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:{
                Intent intent = new Intent(this.getApplicationContext(), OrdersActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.order_history:{
                Intent intent = new Intent(this.getApplicationContext(), DoneOrderListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.datetime_opening:{
                Intent intent = new Intent(this.getApplicationContext(), OpeningDateTimeActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.review:{
                Intent intent = new Intent(this.getApplicationContext(), ReviewListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.sign_up: {
                Intent goIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(goIntent);
                break;
            }
            case R.id.Menu: {
                Intent goIntent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(goIntent);
                break;
            }
            case R.id.account_detail: {
                Intent goIntent = new Intent(getApplicationContext(), AccountDetailActivity.class);
                startActivity(goIntent);
                break;
            }
            case R.id.logout: {
                Providers.getAuthProvider().logout(new RequestListener<Boolean>() {
                    @Override
                    public void successResponse(Boolean response) {
                        //TODO creare stringa in string
                        Toast.makeText(getApplicationContext(), "Logout effettuato", Toast.LENGTH_LONG);
                        Intent goIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(goIntent);
                        finish();
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG);
                    }
                });

                break;
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private boolean isValidDestination(int dest){
        return dest != Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId();
    }

}
