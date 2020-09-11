package it.uniba.di.sms1920.everit.restaurateur.activeOrders;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.request.OrderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class OrderDetailActivity extends AppCompatActivity {
    public static final String ARG_ITEM_ID = "item_id";

    private long orderId;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Toolbar toolbar = findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if ((extras != null) && (extras.containsKey(ARG_ITEM_ID))) {
                Object idObject = extras.get(ARG_ITEM_ID);
                if (idObject instanceof String) {
                    this.orderId = Long.parseLong((String) idObject);
                }
                else if (idObject instanceof Long) {
                    this.orderId = (long) idObject;
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if ((this.order == null) && (this.orderId != 0)) { //The order needs to be loaded
            this.loadData();
        }
    }

    private void loadData() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.readAsRestaurauter(this.orderId, new RequestListener<Order>() {
            @Override
            public void successResponse(Order response) {
                order = response;
                setUpFragment();
            }

            @Override
            public void errorResponse(RequestException error) {
                promptErrorMessage(error.getMessage());
            }
        });
    }

    private void setUpFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(OrderDetailFragment.ARG_ITEM, this.order);
        OrderDetailFragment fragment = new OrderDetailFragment();
        fragment.setArguments(bundle);
        this.getSupportFragmentManager().beginTransaction().add(R.id.order_detail_container, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void promptErrorMessage(String message){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_ok);

        TextView title = dialog.findViewById(R.id.textViewTitle);
        title.setText(it.uniba.di.sms1920.everit.utils.R.string.error);

        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v ->{
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }
}