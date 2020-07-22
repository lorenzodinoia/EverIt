package it.uniba.di.sms1920.everit.restaurateur.activities.signup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.restaurateur.activities.AddressChooserActivity;
import it.uniba.di.sms1920.everit.utils.Address;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.models.ShopType;
import it.uniba.di.sms1920.everit.utils.request.ShopTypeRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

import static android.app.Activity.RESULT_OK;

public class SignUp1Fragment extends Fragment {

    private  SignUpActivity signUpActivity;
    private Restaurateur.Builder restaurateurBuilder;
    private TextInputLayout editTextShopNameContainer;
    private TextInputEditText editTextShopName;
    private TextInputLayout editTextPhoneNumberContainer;
    private TextInputEditText editTextPhoneNumber;
    private TextInputLayout editTextVATContainer;
    private TextInputEditText editTextVAT;
    private TextInputLayout editTextAddressContainer;
    private TextInputEditText editTextAddress;
    private Address address;
    private MaterialButton btnNext;

    private Spinner spinnerShopType;
    private SpinnerAdapter spinnerAdapter;
    private List<ShopType> shopTypes = new ArrayList<>();
    private ShopType shopTypeSelected;
    private TextView textViewEmptyShopType;

    public SignUp1Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_sign_up1, parent, false);
        restaurateurBuilder = signUpActivity.getRestaurateurBuilder();
        ShopTypeRequest shopTypeRequest = new ShopTypeRequest();
        shopTypeRequest.readAll(new RequestListener<Collection<ShopType>>() {
            @Override
            public void successResponse(Collection<ShopType> response) {
                ShopType first = new ShopType(-1, getString(R.string.default_shop_type));
                shopTypes.add(first);
                shopTypes.addAll(response);
                initComponent(viewRoot);
            }

            @Override
            public void errorResponse(RequestException error) {
                //TODO gestire errore
            }
        });

        return viewRoot;
    }

    private void initComponent(View viewRoot) {
        editTextPhoneNumberContainer = viewRoot.findViewById(R.id.editTextPhoneNumberContainer);
        editTextPhoneNumber = viewRoot.findViewById(R.id.editTextPhoneNumber);
        editTextShopNameContainer = viewRoot.findViewById(R.id.editTextShopNameContainer);
        editTextShopName = viewRoot.findViewById(R.id.editTextShopName);
        editTextVATContainer = viewRoot.findViewById(R.id.editTextVATContainer);
        editTextVAT = viewRoot.findViewById(R.id.editTextVAT);
        editTextAddressContainer = viewRoot.findViewById(R.id.editTextAddressContainer);
        editTextAddress = viewRoot.findViewById(R.id.editTextAddress);

        if(restaurateurBuilder != null) {
            if (restaurateurBuilder.getShopName() != null) {
                editTextShopName.setText(restaurateurBuilder.getShopName());
            }
            if (restaurateurBuilder.getAddress() != null) {
                editTextAddress.setText(restaurateurBuilder.getAddress().getFullAddress());
            }
            if (restaurateurBuilder.getPhoneNumber() != null) {
                editTextPhoneNumber.setText(restaurateurBuilder.getPhoneNumber());
            }
            if (restaurateurBuilder.getShopType() != null) {
                spinnerShopType.setSelection((int) restaurateurBuilder.getShopType().getId());
            }
        }
        textViewEmptyShopType = viewRoot.findViewById(R.id.textViewEmptyShopType);
        spinnerShopType = viewRoot.findViewById(R.id.spinnerShopType);
        spinnerAdapter = new SpinnerAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, shopTypes);
        spinnerShopType.setAdapter(spinnerAdapter);
        spinnerShopType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    shopTypeSelected = spinnerAdapter.getItem(position);
                }
                else{
                    shopTypeSelected = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editTextAddress.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), AddressChooserActivity.class);
            startActivityForResult(intent, AddressChooserActivity.REQUEST_ADDRESS);
        });
        btnNext = viewRoot.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(v -> {
            Address addressFake = new Address(16.305676, 41.13449, "via Piave, 20", "Andria");
            editTextAddress.setText(addressFake.getFullAddress());
            boolean flag = true;

            String shopName = editTextShopName.getText().toString();
            String VAT = editTextVAT.getText().toString();
            String phoneNumber = editTextPhoneNumber.getText().toString();

            if(!Utility.isShopNameValid(shopName)){
                flag = false;
                editTextShopNameContainer.setError(getString(R.string.error_shop_name));
            }
            else{
                editTextShopNameContainer.setError(null);
            }

            if(!Utility.isPhoneValid(phoneNumber)){
                flag = false;
                editTextPhoneNumberContainer.setError(getString(R.string.error_phone_number));
            }
            else{
                editTextPhoneNumberContainer.setError(null);
            }

            if(!Utility.isVATValid(VAT)){
                flag = false;
                editTextVATContainer.setError(getString(R.string.error_VAT_number));
            }
            else{
                editTextVATContainer.setError(null);
            }

            if(shopTypeSelected == null){
                flag = false;
                textViewEmptyShopType.setText(R.string.error_shop_type);
                textViewEmptyShopType.setTextColor(Color.parseColor("#ae0022"));
            }
            else{
                textViewEmptyShopType.setText("");
            }

            if(editTextAddress.getText() == null){
                flag = false;
                editTextAddressContainer.setError(getString(R.string.error_address));
            }
            else{
                editTextAddressContainer.setError(null);
            }


            if(flag) {
                restaurateurBuilder.setShopName(editTextShopName.getText().toString());
                restaurateurBuilder.setPhoneNumber(editTextPhoneNumber.getText().toString());
                restaurateurBuilder.setVatNumber(editTextVAT.getText().toString());
                restaurateurBuilder.setShopType(shopTypeSelected);

                restaurateurBuilder.setAddress(addressFake);
                restaurateurBuilder.setMaxDeliveryPerTimeSlot(0);
                restaurateurBuilder.setDeliveryCost(0);
                restaurateurBuilder.setMinPrice(0);
                SignUp2Fragment fragment2 = new SignUp2Fragment();
                FragmentManager fragmentManager = signUpActivity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerSignUp, fragment2).addToBackStack(null).commit();
            }
        });
    }

    private static class SpinnerAdapter extends  ArrayAdapter<ShopType>{


        public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<ShopType> objects) {
            super(context, resource, objects);
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Nullable
        @Override
        public ShopType getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return super.getDropDownView(position, convertView, parent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if ((requestCode == AddressChooserActivity.REQUEST_ADDRESS) && (resultCode == RESULT_OK)
                && (data != null) && (data.hasExtra(AddressChooserActivity.RESULT_ADDRESS))) {
            Address chosenAddress = data.getParcelableExtra(AddressChooserActivity.RESULT_ADDRESS);
            if (chosenAddress != null) {
                address = chosenAddress;
                editTextAddress.setText(chosenAddress.getFullAddress());
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof  SignUpActivity){
            signUpActivity = (SignUpActivity) context;
            restaurateurBuilder = signUpActivity.getRestaurateurBuilder();
        }
    }
}
