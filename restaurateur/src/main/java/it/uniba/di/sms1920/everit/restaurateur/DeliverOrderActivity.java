package it.uniba.di.sms1920.everit.restaurateur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.request.OrderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class DeliverOrderActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 0;
    public static final String ARG_ITEM = "item";
    private static final String SAVED_ORDER = "saved.order";
    public static final String MIME_TEXT_PLAIN = "text/plain";

    private Order order;

    private NfcAdapter nfcAdapter;

    private TextView textViewMessageDeliverOrder;
    private TextInputEditText editTextValidationCode1;
    private TextInputEditText editTextValidationCode2;
    private TextInputEditText editTextValidationCode3;
    private TextInputEditText editTextValidationCode4;
    private TextInputEditText editTextValidationCode5;
    private MaterialButton buttonConfirm;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.initNFC();
        if (this.order != null) {
            initData();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_ORDER, order);
    }

    private void initNFC() {
        if (isNfcSupported()) {
            if(this.nfcAdapter.isEnabled()) {
                textViewMessageDeliverOrder.setText(R.string.message_deliver_order_activity_with_nfc);
                this.enableForegroundDispatch(this, this.nfcAdapter);
            }
            else {
                textViewMessageDeliverOrder.setText(R.string.message_deliver_order_activity_with_nfc_off_nfc);
            }
        }
        else {
            textViewMessageDeliverOrder.setText(R.string.message_deliver_order_activity);
        }
    }

    private void initUi(){
        Toolbar toolbar = findViewById(R.id.toolbar_default);
        toolbar.setTitle(R.string.deliver_order);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textViewMessageDeliverOrder = findViewById(R.id.textViewMessageDeliverOrder);
        editTextValidationCode1 = findViewById(R.id.editTextValidationCode1);
        editTextValidationCode2 = findViewById(R.id.editTextValidationCode2);
        editTextValidationCode3 = findViewById(R.id.editTextValidationCode3);
        editTextValidationCode4 = findViewById(R.id.editTextValidationCode4);
        editTextValidationCode5 = findViewById(R.id.editTextValidationCode5);
        buttonConfirm = findViewById(R.id.btnConfirm);
    }

    private void initData(){
        editTextValidationCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")) {
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
        editTextValidationCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")) {
                    editTextValidationCode2.clearFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) {
                    editTextValidationCode3.requestFocus();
                }
            }
        });
        editTextValidationCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")) {
                    editTextValidationCode3.clearFocus();                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) {
                    editTextValidationCode4.requestFocus();
                }
            }
        });
        editTextValidationCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")) {
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
        editTextValidationCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")) {
                    editTextValidationCode5.clearFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        buttonConfirm.setOnClickListener(v -> {
            getValidationCodeFromEditText();
            checkValidationCode();
        });
    }

    private void checkValidationCode() {
        final int validationCode = this.getValidationCodeFromEditText();
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.deliverOrderAsRestaurateur(this.order.getId(), validationCode, new RequestListener<Boolean>() {
            @Override
            public void successResponse(Boolean response) {
                if(response) {
                    Toast.makeText(DeliverOrderActivity.this, R.string.delivered, Toast.LENGTH_LONG).show();
                    setResult(Activity.RESULT_OK);
                    finish();
                }
                else {
                    Toast.makeText(DeliverOrderActivity.this, R.string.wrong_validation_code, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void errorResponse(RequestException error) {
                promptErrorMessage(error.getMessage());
            }
        });
    }

    private int getValidationCodeFromEditText() {
        StringBuilder validationCodeBuilder = new StringBuilder();
        validationCodeBuilder.append(editTextValidationCode1.getText().toString());
        validationCodeBuilder.append(editTextValidationCode2.getText().toString());
        validationCodeBuilder.append(editTextValidationCode3.getText().toString());
        validationCodeBuilder.append(editTextValidationCode4.getText().toString());
        validationCodeBuilder.append(editTextValidationCode5.getText().toString());

        return Integer.parseInt(validationCodeBuilder.toString());
    }

    private void setValidationCodeToEditText(String validationCode) {
        if (validationCode.length() == 5) {
            this.editTextValidationCode1.setText(String.valueOf(validationCode.charAt(0)));
            this.editTextValidationCode2.setText(String.valueOf(validationCode.charAt(1)));
            this.editTextValidationCode3.setText(String.valueOf(validationCode.charAt(2)));
            this.editTextValidationCode4.setText(String.valueOf(validationCode.charAt(3)));
            this.editTextValidationCode5.setText(String.valueOf(validationCode.charAt(4)));
        }
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

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onNewIntent(Intent intent) {
        this.receiveMessageFromDevice(intent);
    }

    private void receiveMessageFromDevice(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] parcelables = intent.getParcelableArrayExtra((NfcAdapter.EXTRA_NDEF_MESSAGES));
            if ((parcelables != null) && (parcelables.length > 0)) {
                NdefMessage inNdefMessage = (NdefMessage) parcelables[0];
                NdefRecord[] inNdefRecords = inNdefMessage.getRecords();
                NdefRecord ndefRecord = inNdefRecords[0];
                String inMessage = new String(ndefRecord.getPayload());
                this.setValidationCodeToEditText(inMessage);
                this.checkValidationCode();
            }
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
        }
        catch (IntentFilter.MalformedMimeTypeException e){
            throw new RuntimeException("Check mime type");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public void disableForegroundDispatch(final AppCompatActivity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if((this.isNfcSupported()) && (this.nfcAdapter.isEnabled())) {
            disableForegroundDispatch(this, this.nfcAdapter);
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