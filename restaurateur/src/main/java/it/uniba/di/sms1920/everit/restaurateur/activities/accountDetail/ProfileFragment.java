package it.uniba.di.sms1920.everit.restaurateur.activities.accountDetail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.restaurateur.SpinnerShopTypeAdapter;
import it.uniba.di.sms1920.everit.restaurateur.activities.AddressChooserActivity;
import it.uniba.di.sms1920.everit.utils.Address;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.models.ShopType;
import it.uniba.di.sms1920.everit.utils.request.RestaurateurRequest;
import it.uniba.di.sms1920.everit.utils.request.ShopTypeRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private final String ARG_RESTAURATEUR = "restaurateur_profile_fragment";

    private AccountDetailActivity mParent;
    private Restaurateur restaurateur;

    private Spinner spinnerShopType;
    private SpinnerShopTypeAdapter spinnerShopTypeAdapter;
    private TextInputLayout editTextVATProfileContainer;
    private TextInputEditText editTextVATProfile;
    private TextInputLayout editTextPhoneNumberProfileContainer;
    private TextInputEditText editTextPhoneNumberProfile;
    private TextInputLayout editTextAddressProfileContainer;
    private TextInputEditText editTextAddressProfile;
    private Address address;
    private MaterialButton buttonEditConfirm;

    private List<ShopType> shopTypeList = new ArrayList<>();
    private ShopType shopTypeSelected;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            restaurateur = mParent.getRestaurateur();
        }
        else{
            if(savedInstanceState.containsKey(ARG_RESTAURATEUR)){
                restaurateur = savedInstanceState.getParcelable(ARG_RESTAURATEUR);
            }
        }
        shopTypeSelected = restaurateur.getShopType();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initUi(view);
        mParent.toolbar.setTitle(R.string.account_info);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initUi(View view){
        spinnerShopType = view.findViewById(R.id.spinnerShopTypeProfile);
        editTextVATProfileContainer = view.findViewById(R.id.editTextVATProfileContainer);
        editTextVATProfile = view.findViewById(R.id.editTextVATProfile);
        editTextPhoneNumberProfileContainer = view.findViewById(R.id.editTextPhoneNumberProfileContainer);
        editTextPhoneNumberProfile = view.findViewById(R.id.editTextPhoneNumberProfile);
        editTextAddressProfileContainer = view.findViewById(R.id.editTextAddressProfileContainer);
        editTextAddressProfile = view.findViewById(R.id.editTextAddressProfile);
        buttonEditConfirm = view.findViewById(R.id.buttonEditConfirm);
    }

    private void initData(){
        ShopTypeRequest shopTypeRequest = new ShopTypeRequest();
        shopTypeRequest.readAll(new RequestListener<Collection<ShopType>>() {
            @Override
            public void successResponse(Collection<ShopType> response) {
                shopTypeList.addAll(response);
                init();
            }

            @Override
            public void errorResponse(RequestException error) {
                Dialog dialog = new Dialog(mParent);
                dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_ok);

                TextView title = dialog.findViewById(R.id.textViewTitle);
                title.setText(it.uniba.di.sms1920.everit.utils.R.string.error);

                TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
                textViewMessage.setText(error.getMessage());

                Button btnOk = dialog.findViewById(R.id.btnOk);
                btnOk.setOnClickListener(v ->{
                    mParent.getSupportFragmentManager().popBackStack();
                    dialog.dismiss();
                });

                dialog.show();
            }
        });
    }

    private void init(){
        shopTypeSelected = restaurateur.getShopType();
        spinnerShopTypeAdapter = new SpinnerShopTypeAdapter(mParent, android.R.layout.simple_spinner_dropdown_item, shopTypeList);
        spinnerShopType.setAdapter(spinnerShopTypeAdapter);
        spinnerShopType.setEnabled(false);
        spinnerShopType.setClickable(false);
        spinnerShopType.setSelection((int) restaurateur.getShopType().getId()-1);
        spinnerShopType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shopTypeSelected = spinnerShopTypeAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editTextVATProfile.setText(restaurateur.getVatNumber());
        editTextVATProfile.setEnabled(false);

        editTextPhoneNumberProfile.setText(restaurateur.getPhoneNumber());
        editTextPhoneNumberProfile.setEnabled(false);

        address = restaurateur.getAddress();

        editTextAddressProfile.setText(restaurateur.getAddress().getFullAddress());
        editTextAddressProfile.setEnabled(false);
        editTextAddressProfile.setOnClickListener(v -> {
            Intent intent = new Intent(mParent, AddressChooserActivity.class);
            startActivityForResult(intent, AddressChooserActivity.REQUEST_ADDRESS);
        });


        buttonEditConfirm.setTag("Edit");
        buttonEditConfirm.setOnClickListener(v -> {
            String status = (String) buttonEditConfirm.getTag();
            if(status.equals("Edit")){
                buttonEditConfirm.setText(R.string.confirm);
                spinnerShopType.setEnabled(true);
                spinnerShopType.setClickable(true);
                editTextVATProfile.setEnabled(true);
                editTextPhoneNumberProfile.setEnabled(true);
                editTextAddressProfile.setEnabled(true);
                v.setTag("Confirm");
            }
            else{
                boolean flag = true;
                if(!Utility.isVATValid(editTextVATProfile.getText().toString(), editTextVATProfileContainer, mParent)){
                    editTextVATProfileContainer.setError(getString(R.string.error_VAT_number));
                    flag = false;
                }
                if(!Utility.isPhoneValid(editTextPhoneNumberProfile.getText().toString(), editTextPhoneNumberProfileContainer, mParent)){
                    editTextPhoneNumberProfileContainer.setError(getString(R.string.error_phone_number));
                    flag = false;
                }
                if(editTextAddressProfile.getText().toString().trim().length() <= 0){
                    editTextAddressProfileContainer.setError(getString(R.string.error_address));
                    flag = false;
                }

                if(flag){
                    restaurateur.setShopType(shopTypeSelected);
                    restaurateur.setVatNumber(editTextVATProfile.getText().toString());
                    restaurateur.setPhoneNumber(editTextPhoneNumberProfile.getText().toString());
                    restaurateur.setAddress(address);

                    RestaurateurRequest restaurateurRequest = new RestaurateurRequest();
                    restaurateurRequest.update(restaurateur, new RequestListener<Restaurateur>() {
                        @Override
                        public void successResponse(Restaurateur response) {
                            restaurateur = response;
                            buttonEditConfirm.setTag("Edit");
                            buttonEditConfirm.setText(R.string.edit);
                            spinnerShopType.setEnabled(false);
                            spinnerShopType.setClickable(false);
                            editTextVATProfile.setEnabled(false);
                            editTextPhoneNumberProfile.setEnabled(false);
                            editTextAddressProfile.setEnabled(false);
                            Toast.makeText(mParent, R.string.account_edited, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void errorResponse(RequestException error) {
                            Dialog dialog = new Dialog(mParent);
                            dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_ok);

                            TextView title = dialog.findViewById(R.id.textViewTitle);
                            title.setText(it.uniba.di.sms1920.everit.utils.R.string.error);

                            TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
                            textViewMessage.setText(error.getMessage());

                            Button btnOk = dialog.findViewById(R.id.btnOk);
                            btnOk.setOnClickListener(v ->{
                                dialog.dismiss();
                            });

                            dialog.show();
                        }
                    });
                }

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof AccountDetailActivity){
            mParent = (AccountDetailActivity) context;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == AddressChooserActivity.REQUEST_ADDRESS) && (resultCode == RESULT_OK)
                && (data != null) && (data.hasExtra(AddressChooserActivity.RESULT_ADDRESS))) {
            Address chosenAddress = data.getParcelableExtra(AddressChooserActivity.RESULT_ADDRESS);
            if (chosenAddress != null) {
                editTextAddressProfile.setText(chosenAddress.getFullAddress());
                address = chosenAddress;
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(ARG_RESTAURATEUR, restaurateur);
    }
}