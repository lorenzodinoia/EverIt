package it.uniba.di.sms1920.everit.restaurateur.activities.signup;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.restaurateur.activities.AddressChooserActivity;
import it.uniba.di.sms1920.everit.restaurateur.SpinnerShopTypeAdapter;
import it.uniba.di.sms1920.everit.utils.Address;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.models.ShopType;
import it.uniba.di.sms1920.everit.utils.request.ShopTypeRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

import static android.app.Activity.RESULT_OK;

public class SignUp1Fragment extends Fragment {

    public static final String SIGNUP1 = "signup1";
    private final String ARG_SHOP_TYPES = "shop_types_signup1";
    private final String ARG_ADDRESS = "address_signup1";

    private SignUpActivity signUpActivity;
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
    private SpinnerShopTypeAdapter spinnerAdapter;
    private ArrayList<ShopType> shopTypes = new ArrayList<>();
    private TextView textViewEmptyShopType;

    public SignUp1Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("test", "onCreate");

        shopTypes.clear();

        if(savedInstanceState != null) {
            address = savedInstanceState.getParcelable(ARG_ADDRESS);
            ShopType first = new ShopType(-1, getString(R.string.default_shop_type));
            shopTypes.add(first);
            shopTypes.addAll(Objects.requireNonNull(savedInstanceState.getParcelableArrayList(ARG_SHOP_TYPES)));
        }

        restaurateurBuilder = signUpActivity.getRestaurateur();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_sign_up1, parent, false);
        Log.d("test", "onCreateView");
        initComponent(viewRoot);
        return viewRoot;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("test", "onStart");
        if(!shopTypes.isEmpty()){
            setSpinner();
            initData();
        }
        else{
            ShopTypeRequest shopTypeRequest = new ShopTypeRequest();
            shopTypeRequest.readAll(new RequestListener<Collection<ShopType>>() {
                @Override
                public void successResponse(Collection<ShopType> response) {
                    ShopType first = new ShopType(-1, getString(R.string.default_shop_type));
                    shopTypes.add(first);
                    shopTypes.addAll(response);
                    setSpinner();
                    initData();
                }

                @Override
                public void errorResponse(RequestException error) {
                    promptErrorMessage(error.getMessage());
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("test", "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("test", "onDestroy");
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
        textViewEmptyShopType = viewRoot.findViewById(R.id.textViewEmptyShopType);
        btnNext = viewRoot.findViewById(R.id.btnNext);
        spinnerShopType = viewRoot.findViewById(R.id.spinnerShopType);

    }

    private void initData(){

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

        editTextAddress.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), AddressChooserActivity.class);
            startActivityForResult(intent, AddressChooserActivity.REQUEST_ADDRESS);
        });
        btnNext.setOnClickListener(v -> {
            boolean flag = true;

            String shopName = editTextShopName.getText().toString();
            String VAT = editTextVAT.getText().toString();
            String phoneNumber = editTextPhoneNumber.getText().toString();

            if(!Utility.isShopNameValid(shopName, editTextShopNameContainer, signUpActivity)){
                flag = false;
            } else{
                editTextShopNameContainer.setError(null);
            }

            if(!Utility.isPhoneValid(phoneNumber, editTextPhoneNumberContainer, signUpActivity)){
                flag = false;
            } else{
                editTextPhoneNumberContainer.setError(null);
            }

            if(!Utility.isVATValid(VAT, editTextVATContainer, signUpActivity)){
                flag = false;
            } else{
                editTextVATContainer.setError(null);
            }

            if(restaurateurBuilder.getShopType() == null){
                flag = false;
                textViewEmptyShopType.setText(R.string.error_shop_type);
                textViewEmptyShopType.setTextColor(Color.parseColor("#ae0022"));
            } else{
                textViewEmptyShopType.setText("");
            }

            if(!Utility.isAddressValid(address, editTextAddressContainer, getActivity())){
                flag = false;
            } else{
                editTextAddressContainer.setError(null);
            }


            if(flag) {
                restaurateurBuilder.setShopName(editTextShopName.getText().toString());
                restaurateurBuilder.setPhoneNumber(editTextPhoneNumber.getText().toString());
                restaurateurBuilder.setVatNumber(editTextVAT.getText().toString());

                restaurateurBuilder.setAddress(address);
                SignUp2Fragment fragment2 = new SignUp2Fragment();
                FragmentManager fragmentManager = signUpActivity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerSignUp, fragment2).addToBackStack(SignUp2Fragment.SIGNUP2).commit();
            }
        });

    }

    private void setSpinner(){
        spinnerAdapter = new SpinnerShopTypeAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, shopTypes);
        spinnerShopType.setAdapter(spinnerAdapter);
        spinnerShopType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    restaurateurBuilder.setShopType(spinnerAdapter.getItem(position));
                }
                else{
                    restaurateurBuilder.setShopType(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

        if(context instanceof SignUpActivity){
            signUpActivity = (SignUpActivity) context;
        }
    }

    private void promptErrorMessage(String message){
        Dialog dialog = new Dialog(signUpActivity);
        dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_ok);

        TextView title = dialog.findViewById(R.id.textViewTitle);
        title.setText(it.uniba.di.sms1920.everit.utils.R.string.error);

        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v ->{
            dialog.dismiss();
            signUpActivity.finish();
        });

        dialog.show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(ARG_ADDRESS, address);

        shopTypes.remove(0);
        outState.putParcelableArrayList(ARG_SHOP_TYPES, shopTypes);
    }
}
