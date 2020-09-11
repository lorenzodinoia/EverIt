package it.uniba.di.sms1920.everit.customer.cartActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.cart.Cart;
import it.uniba.di.sms1920.everit.utils.Utility;

public class Cart2Fragment extends Fragment  {

    private Cart cart;
    private CartActivity mParent;
    private MaterialButton buttonNext, buttonBack;
    private EditText editTextOrderNotes;


    public Cart2Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cart = Cart.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_cart2, parent, false);

        this.initComponent(viewRoot);
        this.setupComponent();

        return viewRoot;
    }

    private void initComponent(View viewRoot){
        editTextOrderNotes = viewRoot.findViewById(R.id.editTextOrderNotes);

        buttonBack =  viewRoot.findViewById(R.id.buttonBackOrder);
        buttonBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        buttonNext = viewRoot.findViewById(R.id.buttonNextOrder);
        buttonNext.setOnClickListener(v -> {
            if(Utility.isValidOrderNote(editTextOrderNotes.getText().toString(), editTextOrderNotes, mParent)){
                Cart3Fragment fragment3 = new Cart3Fragment();
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerCartFragment, fragment3).addToBackStack(null).commit();
            }

        });
    }

    private void setupComponent(){
        editTextOrderNotes.setText(cart.getPartialOrder().getOrderNotes());
    }

    @Override
    public void onPause() {
        cart.getPartialOrder().setOrderNotes(editTextOrderNotes.getText().toString());
        super.onPause();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof CartActivity){
            mParent = (CartActivity) context;
        }
    }

}
