package it.uniba.di.sms1920.everit.customer.activities.accountDetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.models.Customer;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.CustomerRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class AccountDetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail_customer);

        toolbar = findViewById(R.id.toolbar_default);
        toolbar.setTitle(R.string.account_detail);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.read(Providers.getAuthProvider().getUser().getId(), new RequestListener<Customer>() {
            @Override
            public void successResponse(Customer response) {
                customer = (Customer) response;
                AccountDetailFragment accountDetailFragment = new AccountDetailFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerSettingsCustomer, accountDetailFragment).addToBackStack(null).commit();
            }

            @Override
            public void errorResponse(RequestException error) {
                //TODO gestire error response
            }
        });
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

    public Customer getCustomer(){
        return customer;
    }
}