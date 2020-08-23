package it.uniba.di.sms1920.everit.rider.activities.works.assignedOrder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import it.uniba.di.sms1920.everit.rider.R;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.request.RiderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class AssignedOrderDetailActivity extends AppCompatActivity {
    public static final String ARG_ITEM_ID = "item_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_order_detail);

        Toolbar toolbar = findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle extras = getIntent().getExtras();
        if ((extras != null) && (extras.containsKey(ARG_ITEM_ID))) {
            long assignedOrderId = extras.getLong(ARG_ITEM_ID);
            RiderRequest riderRequest = new RiderRequest();
            riderRequest.readAssignedOrder(assignedOrderId, new RequestListener<Order>() {
                @Override
                public void successResponse(Order response) {
                    loadFragment(response);
                }

                @Override
                public void errorResponse(RequestException error) {
                    //TODO Gestione errore
                }
            });
        }
    }

    private void loadFragment(Order assignedOrder) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(AssignedOrderDetailFragment.ARG_ITEM, assignedOrder);
        AssignedOrderDetailFragment fragment = new AssignedOrderDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().add(R.id.assigned_order_detail_container, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}