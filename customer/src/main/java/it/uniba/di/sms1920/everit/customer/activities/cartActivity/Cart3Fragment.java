package it.uniba.di.sms1920.everit.customer.activities.cartActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.cart.Cart;
import it.uniba.di.sms1920.everit.utils.request.CustomerRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class Cart3Fragment extends Fragment {

    private final static int HOME_DELIVERY = 0;
    private final static int CUSTOMER_PICKUP = 1;

    private Cart cart;
    private CartActivity mParent;

    private MaterialButton buttonFinishOrder;
    private MaterialButton buttonBack;

    private RadioButton homeDelivery, customerPickup;

    private List<String> deliveryTime = new ArrayList<>();
    private Spinner spinnerDeliveryTime;
    private SpinnerAdapter spinnerDeliveryTimeAdapter;
    private String openingTimeSelected;

    public Cart3Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cart = Cart.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_cart3, parent, false);

        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.getAvaibleDeliveryTime(cart.getPartialOrder().getRestaurateur().getId(), new RequestListener<Collection<String>>() {
            @Override
            public void successResponse(Collection<String> response) {
                spinnerDeliveryTime = viewRoot.findViewById(R.id.spinnerDeliveryTime);
                homeDelivery = viewRoot.findViewById(R.id.home_delivery);
                customerPickup = viewRoot.findViewById(R.id.customer_pickup);

                 deliveryTime.addAll(response);

                 spinnerDeliveryTimeAdapter = new SpinnerAdapter(mParent, android.R.layout.simple_spinner_dropdown_item, deliveryTime);
                 spinnerDeliveryTime.setAdapter(spinnerDeliveryTimeAdapter);

                 spinnerDeliveryTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                 @Override
                 public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                     if(position != 0){
                     openingTimeSelected = spinnerDeliveryTimeAdapter.getItem(position);
                     } else{
                     openingTimeSelected = null;
                    }
                 }

                 @Override
                 public void onNothingSelected(AdapterView<?> parent) { }
                 });

                buttonFinishOrder = viewRoot.findViewById(R.id.buttonFinishOrder);
                buttonFinishOrder.setOnClickListener(v -> {

                    if(homeDelivery.isChecked()){
                        cart.getPartialOrder().setOrderType(HOME_DELIVERY);
                    }else if(customerPickup.isChecked()) {
                        cart.getPartialOrder().setOrderType(CUSTOMER_PICKUP);
                    }

                    cart.getPartialOrder().setDeliveryTime(openingTimeSelected);

                    /**
                    Order order;



                    OrderRequest orderRequest = new OrderRequest();
                    orderRequest.create(cart);
                     */

                });

                buttonBack = viewRoot.findViewById(R.id.buttonBack);
                buttonBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
            }

             @Override
             public void errorResponse(RequestException error) {
                 promptErrorMessage(error.getMessage());
             }

        });


        return viewRoot;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof CartActivity){
            mParent = (CartActivity) context;
        }
    }


    private static class SpinnerAdapter extends ArrayAdapter<String> {

        public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Nullable
        @Override
        public String getItem(int position) {
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

    private void promptErrorMessage(String message){
        Dialog dialog = new Dialog(mParent);
        dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_ok);

        TextView title = dialog.findViewById(R.id.textViewTitle);
        title.setText(it.uniba.di.sms1920.everit.utils.R.string.error);

        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v ->{
            dialog.dismiss();
            mParent.finish();
        });

        dialog.show();
    }
}