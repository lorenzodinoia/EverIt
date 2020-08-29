package it.uniba.di.sms1920.everit.restaurateur.activities.signup;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;

public class SignUp2Fragment extends Fragment {

    public static final String SIGNUP2 = "signup2";
    private SignUpActivity signUpActivity;
    private Restaurateur.Builder restaurateurBuilder;

    private TextInputEditText editTextMaxDeliveryPerTimeSlot;
    private TextInputLayout editTextMaxDeliveryPerTimeSlotContainer;
    private TextInputEditText editTextDeliveryCost;
    private TextInputLayout editTextDeliveryCostContainer;
    private TextInputEditText editTextMinPrice;
    private TextInputLayout editTextMinPriceContainer;
    private MaterialButton btnContinue;
    private MaterialButton btnBack;

    public SignUp2Fragment() {
        // Required empty public constructor
    }

    //TODO se si gira due volte, crasha

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        restaurateurBuilder = signUpActivity.getRestaurateur();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_sign_up2, parent, false);

        editTextMaxDeliveryPerTimeSlotContainer = viewRoot.findViewById(R.id.editTextNumDeliveryPerTimeSlotContainer);
        editTextMaxDeliveryPerTimeSlot = viewRoot.findViewById(R.id.editTextNumDeliveryPerTimeSlot);
        editTextDeliveryCostContainer = viewRoot.findViewById(R.id.editTextDeliveryCostContainer);
        editTextDeliveryCost = viewRoot.findViewById(R.id.editTextDeliveryCost);
        editTextMinPriceContainer = viewRoot.findViewById(R.id.editTextMinPriceContainer);
        editTextMinPrice = viewRoot.findViewById(R.id.editTextMinPrice);
        btnBack = viewRoot.findViewById(R.id.buttonBack);
        btnContinue = viewRoot.findViewById(R.id.buttonContinue);

        return viewRoot;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {

        editTextMaxDeliveryPerTimeSlot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.equals("")) {
                    restaurateurBuilder.setMaxDeliveryPerTimeSlot(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextDeliveryCost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.equals("")) {
                    restaurateurBuilder.setDeliveryCost(Float.parseFloat(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextMinPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.equals("")) {
                    restaurateurBuilder.setMinPrice(Float.parseFloat(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if(restaurateurBuilder.getMaxDeliveryPerTimeSlot() > 0) {
            editTextMaxDeliveryPerTimeSlot.setText(Integer.toString(restaurateurBuilder.getMaxDeliveryPerTimeSlot()));
        }

        if(restaurateurBuilder.getDeliveryCost() > 0) {
            editTextDeliveryCost.setText(Float.toString(restaurateurBuilder.getDeliveryCost()));
        }

        if(restaurateurBuilder.getMinPrice() > 0) {
            editTextMinPrice.setText(Float.toString(restaurateurBuilder.getMinPrice()));
        }


        btnBack.setOnClickListener(view -> {
            signUpActivity.getSupportFragmentManager().popBackStack();
        });

        btnContinue.setOnClickListener(view -> {

            boolean flag = true;
            int maxDeliveryTimeSlot = Integer.parseInt(editTextMaxDeliveryPerTimeSlot.getText().toString());

            if (!Utility.isMaxDeliveryTimeSlot(maxDeliveryTimeSlot)) {
                flag = false;
                editTextMaxDeliveryPerTimeSlotContainer.setError(getString(R.string.error_num_delivery_time_slot));
            } else {
                editTextMaxDeliveryPerTimeSlotContainer.setError(null);
            }

            if (editTextDeliveryCost.getText().toString().trim().length() > 0){
                float deliveryCost = Float.parseFloat(editTextDeliveryCost.getText().toString());
                if (!Utility.isDeliveryCostValid(deliveryCost, editTextDeliveryCostContainer, signUpActivity)) {
                    flag = false;
                } else {
                    editTextDeliveryCostContainer.setError(null);
                }
            }

            if(editTextMinPrice.getText().toString().trim().length() > 0) {
                float minPrice = Float.parseFloat(editTextMinPrice.getText().toString());
                if (!Utility.isMinPriceValid(minPrice, editTextMinPriceContainer, signUpActivity)) {
                    flag = false;
                } else {
                    editTextMinPriceContainer.setError(null);
                }
            }

            if (flag) {
                restaurateurBuilder.setMaxDeliveryPerTimeSlot(maxDeliveryTimeSlot);
                if (editTextDeliveryCost.getText().toString().trim().length() > 0) {
                    restaurateurBuilder.setDeliveryCost(Float.parseFloat(editTextDeliveryCost.getText().toString()));
                }
                if(editTextMinPrice.getText().toString().trim().length() > 0) {
                    restaurateurBuilder.setMinPrice(Float.parseFloat(editTextMinPrice.getText().toString()));
                }

                OpeningTimeSelectionFragment openingTimeSelectionFragment = new OpeningTimeSelectionFragment();
                FragmentManager fragmentManager = signUpActivity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerSignUp, openingTimeSelectionFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof SignUpActivity) {
            signUpActivity = (SignUpActivity) context;
        }
    }


}
