package it.uniba.di.sms1920.everit.restaurateur;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import it.uniba.di.sms1920.everit.R;

public class RegisterActivityRestaurateur extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    private EditText editTextNameAct;
    private EditText editTextAddress;
    private EditText editTextPhone;
    private EditText editTextPIVA;
    private EditText editTextActivityDescription;
    private EditText editTextMail;
    private EditText editTextPassword;
    private Button buttonContinue;
    private Button buttonFinish;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_restaurateur);
        initComponents();

        Spinner spinner = findViewById(R.id.spinnerActivityType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }


    private void initComponents() {
        editTextMail =  findViewById(R.id.editTextMail);
        editTextNameAct = findViewById(R.id.editTextNameAct);
        editTextAddress = findViewById(R.id.editTextAddress);
        buttonContinue = findViewById(R.id.buttonContinue);

        buttonContinue.setOnClickListener( view -> {
            setContentView(R.layout.activity_register_restaurateur_p2);
            initComponentsP2();
        });

    }

    private void initComponentsP2(){
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPIVA = findViewById(R.id.editTextPIVA);
        editTextActivityDescription = findViewById(R.id.editTextDescription);
        buttonFinish = findViewById(R.id.buttonEnd);
    }


    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


}
