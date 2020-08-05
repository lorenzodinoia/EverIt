package it.uniba.di.sms1920.everit.restaurateur.activities.accountDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mParent.toolbar.setTitle(R.string.account_info);

        ShopTypeRequest shopTypeRequest = new ShopTypeRequest();
        shopTypeRequest.readAll(new RequestListener<Collection<ShopType>>() {
            @Override
            public void successResponse(Collection<ShopType> response) {
                shopTypeList.addAll(response);
                init(view);
            }

            @Override
            public void errorResponse(RequestException error) {
                //TODO gestire error response
            }
        });
        return view;
    }

    public void init(View view){
        shopTypeSelected = restaurateur.getShopType();
        spinnerShopType = view.findViewById(R.id.spinnerShopTypeProfile);
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

        editTextVATProfileContainer = view.findViewById(R.id.editTextVATProfileContainer);
        editTextVATProfile = view.findViewById(R.id.editTextVATProfile);
        editTextVATProfile.setText(restaurateur.getVatNumber());
        editTextVATProfile.setEnabled(false);

        editTextPhoneNumberProfileContainer = view.findViewById(R.id.editTextPhoneNumberProfileContainer);
        editTextPhoneNumberProfile = view.findViewById(R.id.editTextPhoneNumberProfile);
        editTextPhoneNumberProfile.setText(restaurateur.getPhoneNumber());
        editTextPhoneNumberProfile.setEnabled(false);

        address = restaurateur.getAddress();
        editTextAddressProfileContainer = view.findViewById(R.id.editTextAddressProfileContainer);
        editTextAddressProfile = view.findViewById(R.id.editTextAddressProfile);
        editTextAddressProfile.setText(restaurateur.getAddress().getFullAddress());
        editTextAddressProfile.setEnabled(false);
        editTextAddressProfile.setOnClickListener(v -> {
            Intent intent = new Intent(mParent, AddressChooserActivity.class);
            startActivityForResult(intent, AddressChooserActivity.REQUEST_ADDRESS);
        });

        buttonEditConfirm = view.findViewById(R.id.buttonEditConfirm);
        buttonEditConfirm.setTag("Edit");
        buttonEditConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    if(editTextAddressProfile.getText().toString() == null){
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
                                //TODo non compare il toast
                                Toast.makeText(mParent, "Edited", Toast.LENGTH_LONG);
                            }

                            @Override
                            public void errorResponse(RequestException error) {
                                //TODO gestire error response
                                Log.d("test", error.toString());
                            }
                        });
                    }

                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof AccountDetailActivity){
            mParent = (AccountDetailActivity) context;
            restaurateur = mParent.getRestaurateur();
            shopTypeSelected = restaurateur.getShopType();
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
}