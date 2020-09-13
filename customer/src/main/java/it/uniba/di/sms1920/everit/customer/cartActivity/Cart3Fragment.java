package it.uniba.di.sms1920.everit.customer.cartActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.cart.Cart;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.request.CustomerRequest;
import it.uniba.di.sms1920.everit.utils.request.OrderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class Cart3Fragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    private Cart cart;
    private CartActivity mParent;

    private MaterialButton buttonFinishOrder;
    private MaterialButton buttonBack;

    private CardView cardViewDeliveryNotes;
    private EditText editTextDeliveryNotes;

    private RadioButton homeDelivery, takeAway;

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
                cardViewDeliveryNotes = viewRoot.findViewById(R.id.cardViewDeliveryNotes);
                editTextDeliveryNotes = viewRoot.findViewById(R.id.editTextDeliveryNotes);

                if(cart.getPartialOrder().getDeliveryNotes() != null){
                    editTextDeliveryNotes.setText(cart.getPartialOrder().getDeliveryNotes());
                }

                homeDelivery = viewRoot.findViewById(R.id.home_delivery);
                homeDelivery.setOnClickListener(v -> {
                    takeAway.setChecked(false);
                    cardViewDeliveryNotes.setVisibility(View.VISIBLE);
                });

                takeAway = viewRoot.findViewById(R.id.takeAway);
                takeAway.setOnClickListener( v -> {
                    homeDelivery.setChecked(false);
                    cardViewDeliveryNotes.setVisibility(View.GONE);
                });



                if(!response.isEmpty()) {
                    deliveryTime.clear();
                    deliveryTime.addAll(response);

                    spinnerDeliveryTimeAdapter = new SpinnerAdapter(mParent, android.R.layout.simple_spinner_dropdown_item, deliveryTime);
                    spinnerDeliveryTime.setAdapter(spinnerDeliveryTimeAdapter);

                    spinnerDeliveryTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            /*String time = spinnerDeliveryTimeAdapter.getItem(position);
                            if(time != null) {
                                String test = time.substring(0, 2);
                                test.trim();
                                if (!time.substring(0, 2).contains(":")) {
                                    String tempString = new String(time);
                                    time = "0"+tempString;
                                }
                            }
                            openingTimeSelected = time;*/
                            openingTimeSelected = spinnerDeliveryTimeAdapter.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            openingTimeSelected = spinnerDeliveryTimeAdapter.getItem(0);
                        }
                    });

                }
                else{
                    promptErrorMessage(getString(R.string.no_delivery_time));
                }

                buttonFinishOrder = viewRoot.findViewById(R.id.buttonFinishOrder);
                buttonFinishOrder.setOnClickListener(v -> {
                    buttonFinishOrder.setEnabled(false);

                    if(homeDelivery.isChecked()){
                        if(Utility.isValidOrderNote(editTextDeliveryNotes.getText().toString(), editTextDeliveryNotes, mParent)) {
                            cart.getPartialOrder().setDeliveryNotes(editTextDeliveryNotes.getText().toString());
                            cart.getPartialOrder().setOrderType(Order.OrderType.HOME_DELIVERY);
                        }

                    }else if(takeAway.isChecked()) {
                        cart.getPartialOrder().setOrderType(Order.OrderType.TAKEAWAY);
                    }

                    cart.getPartialOrder().setDeliveryTime(openingTimeSelected);

                    Order order = cart.getPartialOrder().partialOrderToOrder();

                    OrderRequest orderRequest = new OrderRequest();
                    orderRequest.create(order, new RequestListener<Order>() {
                        @Override
                        public void successResponse(Order response) {
                            cart.clear();
                            mParent.finishAffinity();
                            Intent intent = new Intent(getContext(), CartAnimConfirmActivity.class);
                            intent.putExtra(ARG_ITEM_ID, response.getId());
                            startActivity(intent);
                            
                        }

                        @Override
                        public void errorResponse(RequestException error) {
                            promptErrorMessage(error.getMessage());
                        }
                    });
                });

                buttonBack = viewRoot.findViewById(R.id.buttonBack);
                buttonBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
            }

             @Override
             public void errorResponse(RequestException error) {
                 buttonFinishOrder.setEnabled(true);
                 promptErrorMessage(error.getMessage());
             }

        });


        return viewRoot;
    }

    @Override
    public void onPause() {
        //cart.getPartialOrder().setDeliveryNotes(editTextDeliveryNotes.getText().toString());
        super.onPause();
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