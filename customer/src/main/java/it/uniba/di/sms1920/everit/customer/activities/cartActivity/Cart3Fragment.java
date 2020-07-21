package it.uniba.di.sms1920.everit.customer.activities.cartActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.cart.Cart;

public class Cart3Fragment extends Fragment {

    private Cart cart;

    private MaterialButton buttonFinishOrder;
    private MaterialButton buttonBack;
    private SwitchMaterial orderType;
    private Spinner spinnerShopType;
    //private SpinnerAdapter spinnerShopTypeAdapter;

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

        this.initComponent(viewRoot);
        return viewRoot;
    }


    private void initComponent(View viewRoot){
        buttonBack =  (MaterialButton) viewRoot.findViewById(R.id.buttonBack);
        buttonFinishOrder = (MaterialButton) viewRoot.findViewById(R.id.buttonFinishOrder);

        spinnerShopType = viewRoot.findViewById(R.id.spinnerShopType);

        orderType = viewRoot.findViewById(R.id.switchOrderType);
        orderType.setOnCheckedChangeListener((buttonView, isChecked) -> {

        });

        buttonBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }

}