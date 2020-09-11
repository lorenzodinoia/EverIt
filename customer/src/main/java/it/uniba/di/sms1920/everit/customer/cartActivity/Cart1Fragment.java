package it.uniba.di.sms1920.everit.customer.cartActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.io.Serializable;
import java.util.Map;

import it.uniba.di.sms1920.everit.customer.ProductRecyclerViewAdapter;
import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.cart.Cart;
import it.uniba.di.sms1920.everit.utils.models.Product;

public class Cart1Fragment extends Fragment {

    private  Map<Product, Integer> map = null;

    private TextView textViewAddress;
    private TextView textViewTotal;
    private TextView textViewSubTotal;
    private TextView textViewMinPurchase;
    private TextView textViewDeliveryCost;
    private RecyclerView recyclerViewProducts;
    private MaterialButton buttonOrder;
    private MaterialButton buttonEmptyCart;

    private Float minPurchase;
    private float subtotal=0;
    private float total=0;

    private Cart cart;
    private CartActivity mParent;



    public Cart1Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cart = Cart.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_cart1, parent, false);

        Bundle bundle = this.getArguments();
        minPurchase = bundle.getFloat("MIN_PURCHASE");

        this.initComponent(viewRoot);
        this.setupComponent();

        return viewRoot;
    }

    private void initComponent(View viewRoot){
        textViewAddress = viewRoot.findViewById(R.id.TextViewDeliveryAddress);
        textViewDeliveryCost = viewRoot.findViewById(R.id.textViewDeliveryCost);
        textViewSubTotal = viewRoot.findViewById(R.id.textViewSubTotal);
        textViewTotal = viewRoot.findViewById(R.id.textViewTotalPrice);
        textViewMinPurchase = viewRoot.findViewById(R.id.textViewMinPurchase);

        recyclerViewProducts = viewRoot.findViewById(R.id.recycleViewProducts);

        buttonOrder = (MaterialButton) viewRoot.findViewById(R.id.buttonContinueOrder);
        buttonOrder.setOnClickListener(v -> {
            if(subtotal > minPurchase) {
                Cart2Fragment fragment2 = new Cart2Fragment();
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerCartFragment, fragment2).addToBackStack(null).commit();
            }else {
                promptErrorMessage(getString(R.string.error_min_cost_cart));
            }
        });

        buttonEmptyCart = viewRoot.findViewById(R.id.buttonEmptyOrder);
        buttonEmptyCart.setOnClickListener(v -> {
            cart.clear();
            getActivity().finish();
            startActivity(getActivity().getIntent());
        });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("MAP", (Serializable) map);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){
            map = (Map<Product, Integer>) savedInstanceState.getSerializable("MAP");
        }else{
            if(map == null) {
                calculateValue();
            }
        }
    }

    private void setupComponent() {
        textViewAddress.setText(cart.getPartialOrder().getDeliveryAddress().getFullAddress());
        textViewMinPurchase.setText(String.valueOf(minPurchase));
        textViewDeliveryCost.setText(String.valueOf(cart.getPartialOrder().getRestaurateur().getDeliveryCost()));

        recyclerViewProducts.setAdapter(new ProductRecyclerViewAdapter(cart.getPartialOrder().getProducts()));

        calculateValue();

        textViewSubTotal.setText(String.valueOf(subtotal));
        textViewTotal.setText(String.valueOf(total));
    }

    private void  calculateValue(){
        if(map == null) {
            map = cart.getPartialOrder().getProducts();
            for (Map.Entry<Product, Integer> pair : map.entrySet()) {
                subtotal = subtotal + ((pair.getKey().getPrice()) * (pair.getValue()));
            }
            total = subtotal + cart.getPartialOrder().getRestaurateur().getDeliveryCost();
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof CartActivity){
            mParent = (CartActivity) context;
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
        });

        dialog.show();
    }
}
