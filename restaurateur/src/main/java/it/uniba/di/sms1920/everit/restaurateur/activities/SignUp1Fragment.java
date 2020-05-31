package it.uniba.di.sms1920.everit.restaurateur.activities;

import android.content.Context;
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
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Collection;
import java.util.List;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.utils.models.ShopType;
import it.uniba.di.sms1920.everit.utils.request.ShopTypeRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class SignUp1Fragment extends Fragment {

    private TextInputEditText editTextShopName;
    private TextInputEditText editTextPhoneNumber;
    private TextInputEditText editTextVAT;
    private MaterialButton btnNext;
    private Spinner spinnerShopType;
    private SpinnerAdapter spinnerAdapter;
    private List<ShopType> shopTypes;

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
                shopTypes = (List<ShopType>) response;
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
        editTextPhoneNumber = viewRoot.findViewById(R.id.editTextPhoneNumber);
        editTextShopName = viewRoot.findViewById(R.id.editTextShopName);
        editTextVAT = viewRoot.findViewById(R.id.editTextVAT);


        spinnerShopType = viewRoot.findViewById(R.id.spinnerShopType);
        spinnerAdapter = new SpinnerAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, shopTypes);
        spinnerShopType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ShopType shopType = spinnerAdapter.getItem(position);
                Toast.makeText(getActivity().getApplicationContext(), "ID: " + shopType.getId() + "Shop Type name: " + shopType.getName(), Toast.LENGTH_LONG);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnNext = viewRoot.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(v -> {

            String shopName = editTextShopName.getText().toString();
            String VAT = editTextVAT.getText().toString();
            String phoneNumber = editTextPhoneNumber.getText().toString();

            //TODO Controlli

            SignUp2Fragment fragment2 = new SignUp2Fragment();
            FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.containerSignUp,fragment2);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
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
