package it.uniba.di.sms1920.everit.customer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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


public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.inflateMenu(R.menu.drawer_view);
        /** l'utente risulta sempre loggato a priori, setto a mano quello che devo testare
         * TODO: fix utente loggato dalla launcher act
        if(Providers.getAuthProvider() == null){
            navigationView.inflateMenu(R.menu.drawer_view_unlogged);
        }else{
            navigationView.inflateMenu(R.menu.drawer_view);
        }
         */
        init();
    }

    private void init() {
        Toolbar toolbar =  findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);

        View headerNav = navigationView.inflateHeaderView(R.layout.nav_view_header_user);
        ImageView imgHeaderNav = headerNav.findViewById(it.uniba.di.sms1920.everit.utils.R.id.imageLogo);
        TextView textHeaderNav = headerNav.findViewById(it.uniba.di.sms1920.everit.utils.R.id.TextViewDrawer);
        textHeaderNav.setText("NOME E COGNOME");

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
            case R.id.nav_profile: {
                if(isValidDestination(R.id.profileFragment)) {
                    Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.profileFragment);
                }
                break;
            }
            case R.id.nav_privacyNsecurity: {
                if(isValidDestination(R.id.privacySecurityFragment)) {
                    Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.privacySecurityFragment);
                }
                break;
            }
            case R.id.nav_order: {
                if(isValidDestination(R.id.orderFragment)) {
                    Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.orderFragment);
                }
                break;
            }
            case R.id.nav_review: {
                if(isValidDestination(R.id.reviewFragment)) {
                    Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.reviewFragment);
                }
                break;
            }
            case R.id.nav_setting: {
                if(isValidDestination(R.id.settingsFragment)) {
                    Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.settingsFragment);
                }
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
                //logout qui
                break;
            }
        }
        item.setChecked(true);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isValidDestination(int dest){
        return dest != Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId();
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
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), drawerLayout);
    }

}