package it.uniba.di.sms1920.everit.customer.activities.nfc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.models.Order;

public class OrderReceiverActivity extends AppCompatActivity implements NfcManager.NfcActivity {
    public static final String ARG_ITEM = "item";
    private static final String SAVED_ORDER = "saved.order";

    private NfcAdapter nfcAdapter;
    private NfcManager nfcManager;

    private Order order;

    private TextView textViewMessageDeliverOrder;
    private TextInputEditText editTextValidationCode1;
    private TextInputEditText editTextValidationCode2;
    private TextInputEditText editTextValidationCode3;
    private TextInputEditText editTextValidationCode4;
    private TextInputEditText editTextValidationCode5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_order);

        this.initUi();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if((extras != null) && (extras.containsKey(ARG_ITEM))) {
                this.order = extras.getParcelable(ARG_ITEM);
            }
        }
        else if (savedInstanceState.containsKey(SAVED_ORDER)) {
            this.order = savedInstanceState.getParcelable(SAVED_ORDER);
        }

        this.initNFC();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (this.order != null) {
            this.initData();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public String getPayload() {
        return this.order.getValidationCode();
    }

    @Override
    public void signalResult() {
        Toast.makeText(this, R.string.code_sent_successfully,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_ORDER, this.order);
    }

    private void initNFC() {
        if(!isNfcSupported()) {
            textViewMessageDeliverOrder.setText(R.string.message_nfc_sender_activity_with_no_nfc);
            Toast.makeText(this, R.string.nfc_not_supported, Toast.LENGTH_SHORT).show();
        }
        else {
            if (this.nfcAdapter.isEnabled()) {
                this.nfcManager = new NfcManager(this);
                this.nfcAdapter.setOnNdefPushCompleteCallback(nfcManager, this);
                this.nfcAdapter.setNdefPushMessageCallback(nfcManager, this);
                textViewMessageDeliverOrder.setText(R.string.message_nfc_sender_activity);
            }
            else {
                textViewMessageDeliverOrder.setText(R.string.message_nfc_sender_with_activation_request_nfc);
            }
        }
    }

    private void initUi() {
        Toolbar toolbar = findViewById(R.id.toolbar_default);
        toolbar.setTitle(R.string.send_code);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textViewMessageDeliverOrder = findViewById(R.id.textViewMessageDeliverOrder);
        textViewMessageDeliverOrder.setText(R.string.message_nfc_sender_activity);

        editTextValidationCode1 = findViewById(R.id.editTextValidationCode1);
        editTextValidationCode1.setEnabled(false);
        editTextValidationCode2 = findViewById(R.id.editTextValidationCode2);
        editTextValidationCode2.setEnabled(false);
        editTextValidationCode3 = findViewById(R.id.editTextValidationCode3);
        editTextValidationCode3.setEnabled(false);
        editTextValidationCode4 = findViewById(R.id.editTextValidationCode4);
        editTextValidationCode4.setEnabled(false);
        editTextValidationCode5 = findViewById(R.id.editTextValidationCode5);
        editTextValidationCode5.setEnabled(false);

        Button buttonConfirm = findViewById(R.id.btnConfirm);
        buttonConfirm.setVisibility(View.GONE);
    }

    private void initData() {
        String validationCode = order.getValidationCode();
        editTextValidationCode1.setText(String.valueOf(validationCode.charAt(0)));
        editTextValidationCode2.setText(String.valueOf(validationCode.charAt(1)));
        editTextValidationCode3.setText(String.valueOf(validationCode.charAt(2)));
        editTextValidationCode4.setText(String.valueOf(validationCode.charAt(3)));
        editTextValidationCode5.setText(String.valueOf(validationCode.charAt(4)));
    }

    private boolean isNfcSupported() {
        if (this.nfcAdapter != null) {
            return true;
        }
        else {
            this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            return this.nfcAdapter != null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}