package it.uniba.di.sms1920.everit.customer.activities.cartActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.Map;

import it.uniba.di.sms1920.everit.customer.ProductRecyclerViewAdapter;
import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.cart.Cart;
import it.uniba.di.sms1920.everit.utils.models.Product;

public class Cart1Fragment extends Fragment {

    private TextView textViewAddress;
    private TextView textViewTotal;
    private TextView textViewSubTotal;
    private TextView textViewMinPurchase;
    private float subtotal = 0;
    private TextView textViewDeliveryCost;

    private RecyclerView recyclerViewProducts;

    private MaterialButton buttonOrder;
    private MaterialButton buttonEmptyCart;

    private Cart cart;
    private CartActivity mParent;

    private Float minPurchase;

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
                //TODO add custom dialog
                new AlertDialog.Builder(getActivity()).setTitle("Errore costo").setMessage("Il valore dei tuoi prodotti non raggiunge il minimo per proseguire con l'ordine").show();
            }
        });

        buttonEmptyCart = (MaterialButton) viewRoot.findViewById(R.id.buttonEmptyOrder);
        buttonEmptyCart.setOnClickListener(v -> {
            cart.clear();
            getActivity().finish();
            startActivity(getActivity().getIntent());
        });

    }

    private void setupComponent(){
        textViewAddress.setText(cart.getPartialOrder().getDeliveryAddress().getFullAddress());
        textViewMinPurchase.setText(String.valueOf(minPurchase));

        textViewDeliveryCost.setText(String.valueOf(cart.getPartialOrder().getRestaurateur().getDeliveryCost()));

        recyclerViewProducts.setAdapter(new ProductRecyclerViewAdapter(cart.getPartialOrder().getProducts()));

        Map<Product, Integer> map = cart.getPartialOrder().getProducts();

        for (Map.Entry<Product, Integer> pair : map.entrySet()) {
            subtotal = subtotal + ((pair.getKey().getPrice()) * (pair.getValue()));
        }

        textViewSubTotal.setText(String.valueOf(subtotal));
        textViewTotal.setText(String.valueOf(subtotal + cart.getPartialOrder().getRestaurateur().getDeliveryCost()));
    }




    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof CartActivity){
            mParent = (CartActivity) context;
        }
    }

}
