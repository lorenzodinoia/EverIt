package it.uniba.di.sms1920.everit.restaurateur.accountDetail;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.request.RestaurateurRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;


public class OrderSettingsFragment extends Fragment {

    private static final String ARG_RESTAURATEUR = "restaurateur_item";

    private AccountDetailActivity mParent;
    private Restaurateur restaurateur;

    private ScrollView scrollViewOrderSettings;
    private TextInputLayout editTextNumDeliveryPerTimeSlotContainer;
    private TextInputEditText editTextNumDeliveryPerTimeSlot;
    private TextInputLayout editTextDeliveryCostContainer;
    private TextInputEditText editTextDeliveryCost;
    private TextInputLayout editTextMinPriceContainer;
    private TextInputEditText editTextMinPrice;
    private MaterialButton buttonEditConfirm;

    public OrderSettingsFragment() {
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_settings, container, false);
        mParent.toolbar.setTitle(R.string.order_settings);
        initUi(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initUi(View view){
        scrollViewOrderSettings = view.findViewById(R.id.scrollViewOrderSettings);

        editTextNumDeliveryPerTimeSlotContainer = view.findViewById(R.id.editTextNumDeliveryPerTimeSlotContainer);
        editTextNumDeliveryPerTimeSlot = view.findViewById(R.id.editTextNumDeliveryPerTimeSlot);
        editTextNumDeliveryPerTimeSlot.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                focusOnView(editTextNumDeliveryPerTimeSlot);
            }
        });

        editTextDeliveryCostContainer = view.findViewById(R.id.editTextDeliveryCostContainer);
        editTextDeliveryCost = view.findViewById(R.id.editTextDeliveryCost);
        editTextDeliveryCost.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                focusOnView(editTextDeliveryCost);
            }
        });

        editTextMinPriceContainer = view.findViewById(R.id.editTextMinPriceContainer);
        editTextMinPrice = view.findViewById(R.id.editTextMinPrice);
        editTextMinPrice.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                focusOnView(editTextMinPrice);
            }
        });

        buttonEditConfirm = view.findViewById(R.id.buttonEditConfirm);
    }

    private void initData(){
        editTextNumDeliveryPerTimeSlot.setText(Integer.toString(restaurateur.getMaxDeliveryPerTimeSlot()));
        editTextNumDeliveryPerTimeSlot.setEnabled(false);

        editTextDeliveryCost.setText(Float.toString(restaurateur.getDeliveryCost()));
        editTextDeliveryCost.setEnabled(false);

        editTextMinPrice.setText(Float.toString(restaurateur.getMinPrice()));
        editTextMinPrice.setEnabled(false);

        buttonEditConfirm.setTag("Edit");
        buttonEditConfirm.setOnClickListener(v -> {
            String status = (String) buttonEditConfirm.getTag();
            if(status.equals("Edit")){
                buttonEditConfirm.setText(R.string.confirm);
                editTextNumDeliveryPerTimeSlot.setEnabled(true);
                editTextDeliveryCost.setEnabled(true);
                editTextMinPrice.setEnabled(true);
                v.setTag("Confirm");
            }
            else{
                boolean flag = true;
                if(!Utility.isMaxDeliveryTimeSlot(Integer.parseInt(editTextNumDeliveryPerTimeSlot.getText().toString()))){
                    editTextNumDeliveryPerTimeSlotContainer.setError(getString(R.string.error_num_delivery_time_slot));
                    flag = false;
                }
                if(!Utility.isMinPriceValid(Float.parseFloat(editTextMinPrice.getText().toString()), editTextMinPriceContainer, mParent)){
                    flag = false;
                }
                if(!Utility.isDeliveryCostValid(Float.parseFloat(editTextDeliveryCost.getText().toString()), editTextDeliveryCostContainer, mParent)){
                    flag = false;
                }

                if(flag){
                    restaurateur.setMaxDeliveryPerTimeSlot(Integer.parseInt(editTextNumDeliveryPerTimeSlot.getText().toString()));
                    restaurateur.setDeliveryCost(Float.parseFloat(editTextDeliveryCost.getText().toString()));
                    restaurateur.setMinPrice(Float.parseFloat(editTextMinPrice.getText().toString()));

                    RestaurateurRequest restaurateurRequest = new RestaurateurRequest();
                    restaurateurRequest.update(restaurateur, new RequestListener<Restaurateur>() {
                        @Override
                        public void successResponse(Restaurateur response) {
                            restaurateur = response;
                            Snackbar.make(buttonEditConfirm, R.string.account_edited, Snackbar.LENGTH_SHORT).show();

                            buttonEditConfirm.setTag("Edit");
                            buttonEditConfirm.setText(R.string.edit);
                            editTextNumDeliveryPerTimeSlot.setEnabled(false);
                            editTextDeliveryCost.setEnabled(false);
                            editTextMinPrice.setEnabled(false);
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(ARG_RESTAURATEUR, restaurateur);
    }

    private void focusOnView(View view){
        scrollViewOrderSettings.post(() -> scrollViewOrderSettings.scrollTo(0, view.getBottom()));
    }
}