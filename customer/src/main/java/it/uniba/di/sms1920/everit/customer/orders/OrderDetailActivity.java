package it.uniba.di.sms1920.everit.customer.orders;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.BaseActivity;
import it.uniba.di.sms1920.everit.customer.nfc.NfcManager;
import it.uniba.di.sms1920.everit.customer.orders.tab.OrderTabManagerFragment;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.request.OrderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

/**
 * An activity representing a single Order detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link OrderListActivity}.
 */
public class OrderDetailActivity extends AppCompatActivity implements NfcManager.NfcActivity {
    public static final String ARG_ITEM_ID = "item_id";
    private static final String SAVED_ORDER = "saved.order";

    private NfcAdapter nfcAdapter;
    private NfcManager nfcManager;

    private long orderId;
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

        this.initNFC();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if ((this.order == null) && (this.orderId != 0)) { //The order needs to be loaded
            this.loadData();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_ORDER, this.order);
    }

    private void setUpFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(OrderTabManagerFragment.ARG_ITEM, this.order);
        OrderTabManagerFragment orderTabManagerFragment = new OrderTabManagerFragment();
        orderTabManagerFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().add(R.id.order_activity_detail_container, orderTabManagerFragment);
        transaction.addToBackStack(null).commit();
    }

    private void loadData() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.read(this.orderId, new RequestListener<Order>() {
            @Override
            public void successResponse(Order response) {
                order = response;
                setUpFragment();
            }

            @Override
            public void errorResponse(RequestException error) {
                Utility.showGenericMessage(OrderDetailActivity.this, error.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            if(isTaskRoot()){
                finish();
                Intent intent = new Intent(this, BaseActivity.class);
                startActivity(intent);
            }else {
                finish();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isNfcSupported() {
        if (this.nfcAdapter != null) {
            return true;
        }
        else {
            this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            return this.nfcAdapter != null;
        }
    }

    public boolean isNfcEnabled() {
        return (this.nfcAdapter != null && this.nfcAdapter.isEnabled());
    }

    private void initNFC() {
        if(!isNfcSupported()) {
            //textViewMessageDeliverOrder.setText(R.string.message_nfc_sender_activity_with_no_nfc);
        }
        else {
            if (this.nfcAdapter.isEnabled()) {
                this.nfcManager = new NfcManager(this);
                this.nfcAdapter.setOnNdefPushCompleteCallback(nfcManager, this);
                this.nfcAdapter.setNdefPushMessageCallback(nfcManager, this);
                //textViewMessageDeliverOrder.setText(R.string.message_nfc_sender_activity);
            }
            else {
                //textViewMessageDeliverOrder.setText(R.string.message_nfc_sender_with_activation_request_nfc);
            }
        }
    }

    @Override
    public String getPayload() {
        return this.order.getValidationCode();
    }

    @Override
    public void signalResult() {
        Toast.makeText(this, R.string.code_sent_successfully,Toast.LENGTH_LONG).show();
    }
}
