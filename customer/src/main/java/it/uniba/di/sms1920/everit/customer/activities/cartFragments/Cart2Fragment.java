package it.uniba.di.sms1920.everit.customer.activities.cartFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import it.uniba.di.sms1920.everit.customer.R;

public class Cart2Fragment extends Fragment {

    private MaterialButton buttonNext, buttonBack;
    private TextInputLayout editTextOrderNotesContainer, editTextDeliveryNotesContainer;
    private TextInputEditText editTextOrderNotes, editTextDeliveryNotes;


    public Cart2Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_cart1, parent, false);
        this.initComponent(viewRoot);

        return viewRoot;
    }

    private void initComponent(View viewRoot){

        editTextDeliveryNotesContainer = viewRoot.findViewById(R.id.editTextDeliveryNotesContainer);
        editTextOrderNotesContainer = viewRoot.findViewById(R.id.editTextOrderNotesContainer);

        editTextDeliveryNotes = viewRoot.findViewById(R.id.editTexttDeliveryNotes);
        editTextOrderNotes = viewRoot.findViewById(R.id.editTextOrderNotes);

        buttonBack = viewRoot.findViewById(R.id.buttonBackOrder);
        buttonNext = viewRoot.findViewById(R.id.buttonNextOrder);

    }

}
