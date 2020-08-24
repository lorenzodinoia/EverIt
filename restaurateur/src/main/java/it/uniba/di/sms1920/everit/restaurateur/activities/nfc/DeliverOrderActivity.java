package it.uniba.di.sms1920.everit.restaurateur.activities.nfc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.request.OrderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class DeliverOrderActivity extends AppCompatActivity {

    public static final String MIME_TEXT_PLAIN = "text/plain";

    private Order order;
    private DeliverOrderActivity activity = this;
    private int validationCode;
    int first;
    int second;
    int third;
    int fourth;
    int fifth;

    private NfcAdapter nfcAdapter;

    private TextView textViewMessageDeliverOrder;
    private TextInputEditText editTextValidationCode1;
    private TextInputEditText editTextValidationCode2;
    private TextInputEditText editTextValidationCode3;
    private TextInputEditText editTextValidationCode4;
    private TextInputEditText editTextValidationCode5;

    private MaterialButton btnConfirm;

    //TODO aggiungere stringhe

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_deliver_order);

        Toolbar toolbar = findViewById(R.id.toolbar_default);
        toolbar.setTitle(R.string.deliver_order);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //TODO crasha qui perchè boh
        /*Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            if (bundle.containsKey(OrderDetailFragment.ORDER)) {
                order = bundle.getParcelable(OrderDetailFragment.ORDER);
            }
        }*/

        if(!isNfcSupported()){
            Toast.makeText(this, "Nfc is not supported on this device", Toast.LENGTH_SHORT).show();
        }else{
            if(!nfcAdapter.isEnabled()){
                Toast.makeText(this, "NFC disabled on this device. Turn on to proceed", Toast.LENGTH_SHORT).show();
                nfcIntentSettings();
            }
        }

        initComponent();
    }

    //TODO verificare funzionamento dell'intent
    private void nfcIntentSettings(){
        Toast.makeText(this, "nfc disabled", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
        startActivity(intent);
    }

    private void initComponent(){
        textViewMessageDeliverOrder = findViewById(R.id.textViewMessageDeliverOrder);
        textViewMessageDeliverOrder.setText(R.string.message_deliver_order_activity);
        editTextValidationCode1 = findViewById(R.id.editTextValidationCode1);
        editTextValidationCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")) {
                    first = Integer.parseInt(s.toString());
                    editTextValidationCode1.clearFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) {
                    editTextValidationCode2.requestFocus();
                }
            }
        });
        editTextValidationCode2 = findViewById(R.id.editTextValidationCode2);
        editTextValidationCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")) {
                    second = Integer.parseInt(s.toString());
                }
                editTextValidationCode2.clearFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) {
                    editTextValidationCode3.requestFocus();
                }
            }
        });
        editTextValidationCode3 = findViewById(R.id.editTextValidationCode3);
        editTextValidationCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")) {
                    third = Integer.parseInt(s.toString());
                    editTextValidationCode3.clearFocus();                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) {
                    editTextValidationCode4.requestFocus();
                }
            }
        });
        editTextValidationCode4 = findViewById(R.id.editTextValidationCode4);
        editTextValidationCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")) {
                    fourth = Integer.parseInt(s.toString());
                    editTextValidationCode4.clearFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) {
                    editTextValidationCode5.requestFocus();
                }
            }
        });
        editTextValidationCode5 = findViewById(R.id.editTextValidationCode5);
        editTextValidationCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")) {
                    fifth = Integer.parseInt(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(v -> {
            getValidationCodeFromEditText();
            checkValidationCode();
        });
    }

    private void checkValidationCode(){
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.deliverOrderAsRestaurateur(19, validationCode, new RequestListener<Boolean>() {
            @Override
            public void successResponse(Boolean response) {
                if(response){
                    Toast.makeText(activity, R.string.delivered, Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Toast.makeText(activity, R.string.wrong_validation_code, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void errorResponse(RequestException error) {
                promptErrorMessage(error.getMessage());
            }
        });
    }

    private void getValidationCodeFromEditText(){
        validationCode = 0;
        validationCode += first*10000;
        validationCode += second*1000;
        validationCode += third*100;
        validationCode += fourth*10;
        validationCode += fifth;
    }

    private boolean isNfcSupported(){
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        return this.nfcAdapter != null;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onNewIntent(Intent intent) {
        receiveMessageFromDevice(intent);
    }

    private void receiveMessageFromDevice(Intent intent){
        String action = intent.getAction();
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){
            Parcelable[] parcelables = intent.getParcelableArrayExtra((NfcAdapter.EXTRA_NDEF_MESSAGES));

            NdefMessage inNdefMessage = (NdefMessage) parcelables[0];
            NdefRecord[] inNdefRecords = inNdefMessage.getRecords();
            NdefRecord ndefRecord_0 = inNdefRecords[0];

            String inMessage = new String(ndefRecord_0.getPayload());
            //TODO verificare contenuto inMessage
            validationCode = Integer.parseInt(inMessage);
            checkValidationCode();
        }
    }

    public void enableForegroundDispatch(AppCompatActivity activity, NfcAdapter adapter){

        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e){
            //TODO gestire exception
            throw new RuntimeException("Check mime type");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public void disableForegroundDispatch(final AppCompatActivity activity, NfcAdapter adapter){
        adapter.disableForegroundDispatch(activity);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isNfcSupported()) {
            if(nfcAdapter.isEnabled()){
                enableForegroundDispatch(this, this.nfcAdapter);
                receiveMessageFromDevice(getIntent());
            }
            else{
                nfcIntentSettings();
            }
        }
        else{
            Toast.makeText(this, "Nfc is not supported on this device", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isNfcSupported()) {
            if(nfcAdapter.isEnabled()){
                disableForegroundDispatch(this, this.nfcAdapter);
            }
            else{
                nfcIntentSettings();
            }
        }
        else{
            Toast.makeText(this, "Nfc is not supported on this device", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
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
        });

        dialog.show();
    }
}