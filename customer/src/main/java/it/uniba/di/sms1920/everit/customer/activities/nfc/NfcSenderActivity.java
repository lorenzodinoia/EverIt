package it.uniba.di.sms1920.everit.customer.activities.nfc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.orders.OrderDetailFragment;
import it.uniba.di.sms1920.everit.utils.models.Order;

public class NfcSenderActivity extends AppCompatActivity implements NfcManager.NfcActivity {

    private NfcSenderActivity activity = this;

    private NfcAdapter nfcAdapter;
    private NfcManager manager;

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

        Toolbar toolbar = findViewById(R.id.toolbar_default);
        toolbar.setTitle(R.string.send_code);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            order = bundle.getParcelable(OrderDetailFragment.ORDER);
            /*if(bundle.containsKey(OrderDetailFragment.ORDER)){

            }*/
        }

        if(!isNfcSupported()){
            Toast.makeText(this, "Nfc is not supported on this device", Toast.LENGTH_SHORT).show();
        }else{
            if(!nfcAdapter.isEnabled()){
                Toast.makeText(this, "NFC disabled on this device. Turn on to exchange code", Toast.LENGTH_SHORT).show();
                nfcIntentSettings();
            }
        }

        initComponent();

        this.manager = new NfcManager(this);
        nfcAdapter.setOnNdefPushCompleteCallback(manager, this);
        nfcAdapter.setNdefPushMessageCallback(manager, this);
    }

    private void initComponent(){
        textViewMessageDeliverOrder = findViewById(R.id.textViewMessageDeliverOrder);
        textViewMessageDeliverOrder.setText(R.string.message_nfc_sender_activity);

        String validationCodeStr = new String(order.getValidationCode());
        editTextValidationCode1 = findViewById(R.id.editTextValidationCode1);
        editTextValidationCode1.setText(validationCodeStr.charAt(0));

        editTextValidationCode2 = findViewById(R.id.editTextValidationCode2);
        editTextValidationCode2.setText(validationCodeStr.charAt(1));

        editTextValidationCode3 = findViewById(R.id.editTextValidationCode3);
        editTextValidationCode3.setText(validationCodeStr.charAt(2));

        editTextValidationCode4 = findViewById(R.id.editTextValidationCode4);
        editTextValidationCode4.setText(validationCodeStr.charAt(3));

        editTextValidationCode5 = findViewById(R.id.editTextValidationCode5);
        editTextValidationCode5.setText(validationCodeStr.charAt(4));

        Button button = findViewById(R.id.btnConfirm);
        button.setVisibility(View.GONE);
    }

    //TODO verificare funzionamento dell'intent
    private void nfcIntentSettings(){
        Toast.makeText(this, "nfc disabled", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private  boolean isNfcSupported(){
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        return this.nfcAdapter != null;
    }

    @Override
    public String getOutcomingMessage() {
        return order.getValidationCode();
    }

    @Override
    public void signalResult() {
        runOnUiThread(() ->
                Toast.makeText(activity, "Beaming complete", Toast.LENGTH_LONG).show());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}