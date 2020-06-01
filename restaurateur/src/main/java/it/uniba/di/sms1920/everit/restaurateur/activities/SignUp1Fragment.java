package it.uniba.di.sms1920.everit.restaurateur.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


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
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.ShopType;
import it.uniba.di.sms1920.everit.utils.request.ShopTypeRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class SignUp1Fragment extends Fragment {

    private TextInputLayout editTextShopNameContainer;
    private TextInputEditText editTextShopName;
    private TextInputLayout editTextPhoneNumberContainer;
    private TextInputEditText editTextPhoneNumber;
    private TextInputLayout editTextVATContainer;
    private TextInputEditText editTextVAT;
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

        btnNext = viewRoot.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(v -> {

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


            if(flag) {
                SignUp2Fragment fragment2 = new SignUp2Fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
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
}
