package it.uniba.di.sms1920.everit.customer.activities.results.Tabs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.android.material.button.MaterialButton;

import java.util.LinkedList;
import java.util.List;

import it.uniba.di.sms1920.everit.customer.DeliveryAddress;
import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.cartActivity.CartActivity;
import it.uniba.di.sms1920.everit.customer.activities.results.CustomExpandibleMenuAdapter;
import it.uniba.di.sms1920.everit.customer.activities.results.ResultDetailActivity;
import it.uniba.di.sms1920.everit.customer.cart.Cart;
import it.uniba.di.sms1920.everit.customer.cart.CartConnector;
import it.uniba.di.sms1920.everit.customer.cart.PartialOrder;
import it.uniba.di.sms1920.everit.utils.models.ProductCategory;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;

public class MenuFragment extends Fragment implements CartConnector{

    private ResultDetailActivity resultDetailActivity;
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        expandableListView = rootView.findViewById(R.id.expandableMenu);

        expandableListDetail = (List<ProductCategory>) restaurateur.getProductCategories();
        expandableListAdapter = new CustomExpandibleMenuAdapter(getActivity(), MenuFragment.this , expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        buttonOrder = rootView.findViewById(R.id.buttonOrderMenu);
        buttonOrder.setOnClickListener(v -> {
            if(!getCart().isEmpty()) {
                Intent goIntent = new Intent(getActivity(), CartActivity.class);
                goIntent.putExtra("MIN_PURCHASE", restaurateur.getMinPrice());
                startActivity(goIntent);
            }
        });


        return  rootView;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof ResultDetailActivity){
            resultDetailActivity = (ResultDetailActivity) context;
            restaurateur = resultDetailActivity.passRestaurateur();
        }
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




}