package it.uniba.di.sms1920.everit.customer.activities.cartFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.cart.Cart;
import it.uniba.di.sms1920.everit.customer.cart.CartConnector;

public class Cart1Fragment extends Fragment {

    private TextView textViewAddress;
    private TextView textViewTotal;
    private TextView textViewSubTotal;
    private TextView textViewDeliveryCost;

    private RecyclerView recyclerViewProducts;

    private MaterialButton buttonOrder;

    private Cart cart;

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

        this.initComponent(viewRoot);
        this.setupComponent();

        return viewRoot;
    }

    private void initComponent(View viewRoot){
        textViewAddress = viewRoot.findViewById(R.id.TextViewDeliveryAddress);
        textViewDeliveryCost = viewRoot.findViewById(R.id.textViewDeliveryCost);
        textViewSubTotal = viewRoot.findViewById(R.id.textViewSubTotal);
        textViewTotal = viewRoot.findViewById(R.id.textViewTotalPrice);

        recyclerViewProducts = viewRoot.findViewById(R.id.recycleViewProducts);

         buttonOrder = (MaterialButton) viewRoot.findViewById(R.id.buttonContinueOrder);
         buttonOrder.setOnClickListener(v -> {
             Cart2Fragment fragment2 = new Cart2Fragment();
             FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
             fragmentTransaction.replace(R.id.containerCartFragment, fragment2).addToBackStack(null).commit();
         });
    }

    private void setupComponent(){
        textViewAddress.setText(cart.getPartialOrder().getDeliveryAddress().getFullAddress());
        textViewDeliveryCost.setText(String.valueOf(cart.getPartialOrder().getRestaurateur().getDeliveryCost()));
    }
}
