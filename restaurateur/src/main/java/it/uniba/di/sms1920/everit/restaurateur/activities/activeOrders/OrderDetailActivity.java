package it.uniba.di.sms1920.everit.restaurateur.activities.activeOrders;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.request.OrderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;


public class OrderDetailActivity extends AppCompatActivity {

    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Toolbar toolbar = findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (savedInstanceState == null) {
            long id = getIntent().getLongExtra(OrderDetailFragment.ARG_ITEM_ID, 0);

            OrderRequest orderRequest = new OrderRequest();
            orderRequest.readAsRestaurauter(id, new RequestListener<Order>() {
                @Override
                public void successResponse(Order response) {
                    order = response;
                    Bundle arguments = new Bundle();
                    if(response.getStatus() == Order.Status.ORDERED){
                        arguments.putInt(OrderDetailFragment.INDEX, 0);
                    }
                    else{
                        arguments.putInt(OrderDetailFragment.INDEX, 1);
                    }

                    OrderDetailFragment fragment = new OrderDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.order_detail_container, fragment)
                            .commit();
                }

                @Override
                public void errorResponse(RequestException error) {
                    promptErrorMessage(error.getMessage());
                }
            });

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            navigateUpTo(new Intent(this, OrderListFragment.class));

            return true;
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

    public Order getOrder(){
        return order;
    }
}