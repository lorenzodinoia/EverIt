package it.uniba.di.sms1920.everit.restaurateur.activities.signup;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_sign_up2, parent, false);
        this.initComponent(viewRoot);
        return viewRoot;
    }

    private void initComponent(View viewRoot) {
        editTextMaxDeliveryPerTimeSlotContainer = viewRoot.findViewById(R.id.editTextNumDeliveryPerTimeSlotContainer);
        editTextMaxDeliveryPerTimeSlot = viewRoot.findViewById(R.id.editTextNumDeliveryPerTimeSlot);

        editTextDeliveryCostContainer = viewRoot.findViewById(R.id.editTextDeliveryCostContainer);
        editTextDeliveryCost = viewRoot.findViewById(R.id.editTextDeliveryCost);

        editTextMinPriceContainer = viewRoot.findViewById(R.id.editTextMinPriceContainer);
        editTextMinPrice = viewRoot.findViewById(R.id.editTextMinPrice);

        editTextMaxDeliveryPerTimeSlot.setText(Integer.toString(restaurateurBuilder.getMaxDeliveryPerTimeSlot()));
        editTextDeliveryCost.setText(Float.toString(restaurateurBuilder.getDeliveryCost()));
        editTextMinPrice.setText(Float.toString(restaurateurBuilder.getMinPrice()));

        btnBack = viewRoot.findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(view -> {
            signUpActivity.getSupportFragmentManager().popBackStack();
        });
        btnContinue = viewRoot.findViewById(R.id.buttonContinue);
        btnContinue.setOnClickListener(view -> {

            boolean flag = true;
            int maxDeliveryTimeSlot = Integer.parseInt(editTextMaxDeliveryPerTimeSlot.getText().toString());
            float deliveryCost = Float.parseFloat(editTextDeliveryCost.getText().toString());
            float minPrice = Float.parseFloat(editTextMinPrice.getText().toString());

            if(!Utility.isMaxDeliveryTimeSlot(maxDeliveryTimeSlot)){
                flag = false;
                editTextMaxDeliveryPerTimeSlotContainer.setError(getString(R.string.error_num_delivery_time_slot));
            } else {
                editTextMaxDeliveryPerTimeSlotContainer.setError(null);
            }

            if (!Utility.isDeliveryCostValid(deliveryCost, editTextDeliveryCostContainer, signUpActivity)) {
                flag = false;
            } else {
                editTextDeliveryCostContainer.setError(null);
            }

            if (!Utility.isMinPriceValid(minPrice, editTextMinPriceContainer, signUpActivity)) {
                flag = false;
            } else {
                editTextMinPriceContainer.setError(null);
            }


            if (flag) {
                restaurateurBuilder.setMaxDeliveryPerTimeSlot(maxDeliveryTimeSlot);
                restaurateurBuilder.setDeliveryCost(Float.parseFloat(editTextDeliveryCost.getText().toString()));
                restaurateurBuilder.setMinPrice(Float.parseFloat(editTextMinPrice.getText().toString()));

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
            restaurateurBuilder = signUpActivity.getRestaurateurBuilder();
        }
    }

}
