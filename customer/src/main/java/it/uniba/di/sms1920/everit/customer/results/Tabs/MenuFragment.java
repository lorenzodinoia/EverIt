package it.uniba.di.sms1920.everit.customer.results.Tabs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.LinkedList;
import java.util.List;

import it.uniba.di.sms1920.everit.customer.DeliveryAddress;
import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.LoginActivity;
import it.uniba.di.sms1920.everit.customer.cartActivity.CartActivity;
import it.uniba.di.sms1920.everit.customer.results.CustomExpandibleMenuAdapter;
import it.uniba.di.sms1920.everit.customer.cart.Cart;
import it.uniba.di.sms1920.everit.customer.cart.CartConnector;
import it.uniba.di.sms1920.everit.customer.cart.PartialOrder;
import it.uniba.di.sms1920.everit.utils.models.ProductCategory;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.provider.Providers;

public class MenuFragment extends Fragment implements CartConnector {
    public static final String ARG_ITEM = "item";
    private static final String SAVED_RESTAURATEUR = "saved.restaurateur";

    private Restaurateur restaurateur;

    private ExpandableListView expandableListView ;
    private CustomExpandibleMenuAdapter expandableListAdapter;
    private List<ProductCategory> expandableListDetail = new LinkedList<>();
    private MaterialButton buttonOrder;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle arguments = getArguments();
            if ((arguments != null) && (arguments.containsKey(ARG_ITEM))) {
                this.restaurateur = arguments.getParcelable(ARG_ITEM);
            }
        }
        else if (savedInstanceState.containsKey(SAVED_RESTAURATEUR)) {
            this.restaurateur = savedInstanceState.getParcelable(SAVED_RESTAURATEUR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        this.initUi(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.restaurateur != null) {
            this.initData();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_RESTAURATEUR, this.restaurateur);
    }

    private void initUi(View view) {
        this.expandableListView = view.findViewById(R.id.expandableMenu);
        this.buttonOrder = view.findViewById(R.id.buttonOrderMenu);
    }

    private void initData() {
        this.expandableListDetail = (List<ProductCategory>) restaurateur.getProductCategories();
        this.expandableListAdapter = new CustomExpandibleMenuAdapter(getActivity(), MenuFragment.this , expandableListDetail);
        this.expandableListView.setAdapter(expandableListAdapter);

        this.buttonOrder.setOnClickListener(v -> {
            if(Providers.getAuthProvider().getUser() != null) {
                if (!getCart().isEmpty()) {
                    Activity parentActivity = getActivity();
                    if (parentActivity != null) {
                        Intent cartIntent = new Intent(getActivity(), CartActivity.class);
                        cartIntent.putExtra("MIN_PURCHASE", restaurateur.getMinPrice());
                        startActivity(cartIntent);
                    }
                }else {
                    promptErrorMessage(getString(R.string.empty_cart_message));
                }
            }
            else {
                Activity parentActivity = getActivity();
                if (parentActivity != null) {
                    Intent loginIntent = new Intent(parentActivity, LoginActivity.class);
                    loginIntent.putExtra(LoginActivity.INTENT_FLAG, 0);
                    startActivity(loginIntent);
                }
            }
        });
    }

    @Override
    public Cart getCart() {
        Cart cart = Cart.getInstance();

        if (cart == null) {
            Cart.init(getContext());
            cart = Cart.getInstance();
        }

        return cart;
    }

    @Override
    public PartialOrder getPartialOrder() {
        PartialOrder partialOrder = this.getCart().getPartialOrderOf(restaurateur);

        if (partialOrder == null) {
            partialOrder = this.getCart().initPartialOrderFor(restaurateur, DeliveryAddress.get());
        }

        return partialOrder;
    }

    private void promptErrorMessage(String message){
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_ok);

        TextView title = dialog.findViewById(R.id.textViewTitle);
        title.setText(it.uniba.di.sms1920.everit.utils.R.string.error);

        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v ->{
            dialog.dismiss();
        });

        dialog.show();
    }

}