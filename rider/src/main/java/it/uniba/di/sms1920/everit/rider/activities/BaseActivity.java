package it.uniba.di.sms1920.everit.rider.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import it.uniba.di.sms1920.everit.rider.BackgroundLocationService;
import it.uniba.di.sms1920.everit.rider.IBackgroundLocationService;
import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.rider.activities.accountDetail.AccountDetailActivity;
import it.uniba.di.sms1920.everit.rider.activities.deliverHistory.DeliveryHistoryListActivity;
import it.uniba.di.sms1920.everit.rider.activities.works.WorksActivity;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private IBackgroundLocationService backgroundLocationService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            backgroundLocationService = IBackgroundLocationService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            backgroundLocationService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent serviceIntent = new Intent(this, BackgroundLocationService.class);
        this.bindService(serviceIntent, this.serviceConnection, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (this.backgroundLocationService != null) {
            this.unbindService(this.serviceConnection);
        }
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);

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
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), drawerLayout);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home: {
                NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.main, true).build();
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.homeFragment, null, navOptions);
                break;
            }

            case R.id.nav_deliveries: {
                try {
                    if ((this.backgroundLocationService != null) && (this.backgroundLocationService.isWorking())) {
                        Intent intent = new Intent(this, WorksActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Snackbar snackbar = Snackbar.make(navigationView, R.string.service_not_started, Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction(R.string.got_it, v -> snackbar.dismiss());
                        snackbar.show();
                    }
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            }

            case R.id.nav_deliveries_history: {
                Intent intent = new Intent(this, DeliveryHistoryListActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.nav_account_detail: {
                Intent intent = new Intent(this, AccountDetailActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.exit: {
                Providers.getAuthProvider().logout(new RequestListener<Boolean>() {
                    @Override
                    public void successResponse(Boolean response) {
                        finish();
                        startActivity(getIntent());
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        promptErrorMessage(error.getMessage());
                    }
                });

                break;
            }

        }
        item.setChecked(true);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void promptErrorMessage(String message){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_ok);

        TextView title = dialog.findViewById(R.id.textViewTitle);
        title.setText(it.uniba.di.sms1920.everit.utils.R.string.error);

        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
