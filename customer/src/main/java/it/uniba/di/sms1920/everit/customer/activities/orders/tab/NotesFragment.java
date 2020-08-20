package it.uniba.di.sms1920.everit.customer.activities.orders.tab;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.models.Order;

public class NotesFragment extends Fragment {

    private EditText editTextOrderNotes, editTextDeliveryNotes;
    private Order order;

    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey(OrderTabManagerFragment.ARG_ORDER_KEY)){
            order = bundle.getParcelable(OrderTabManagerFragment.ARG_ORDER_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        if(order != null) {
            editTextOrderNotes = view.findViewById(R.id.editTextOrderNotes);
            editTextDeliveryNotes = view.findViewById(R.id.editTextDeliveryNotes);

            editTextOrderNotes.setText(order.getOrderNotes());
            editTextDeliveryNotes.setText(order.getDeliveryNotes());
        }

        return view;
    }

}