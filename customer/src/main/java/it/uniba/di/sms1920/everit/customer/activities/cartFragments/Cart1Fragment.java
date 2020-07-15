package it.uniba.di.sms1920.everit.customer.activities.cartFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import it.uniba.di.sms1920.everit.customer.R;

public class Cart1Fragment extends Fragment {

    private TextView textViewAddress;
    private TextView textViewDeliveryDate;
    private TextView textViewTotal;
    private TextView textViewSubTotal;
    private TextView textViewDeliveryCost;

    private RecyclerView recyclerViewProducts;

    private MaterialButton buttonOrder;


    public Cart1Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_cart1, parent, false);

        initComponent(viewRoot);

        return viewRoot;
    }

    private void initComponent(View viewRoot){
        textViewAddress = viewRoot.findViewById(R.id.TextViewDeliveryAddress);
        textViewDeliveryCost = viewRoot.findViewById(R.id.textViewDeliveryCost);
        textViewSubTotal = viewRoot.findViewById(R.id.textViewSubTotal);
        textViewTotal = viewRoot.findViewById(R.id.textViewTotalPrice);

        recyclerViewProducts = viewRoot.findViewById(R.id.recycleViewProducts);

         buttonOrder = viewRoot.findViewById(R.id.buttonContinueOrder);
    }

    private void setupComponent(){
        /**
         textViewAddress.setText(String.valueOf(cart.getPartialOrder().getDeliveryAddress()));
         textViewDeliveryCost.setText(String.valueOf(cart.getPartialOrder().getRestaurateur().getDeliveryCost()));
         */
    }
}
