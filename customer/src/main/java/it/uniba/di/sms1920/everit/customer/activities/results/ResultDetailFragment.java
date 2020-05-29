package it.uniba.di.sms1920.everit.customer.activities.results;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.models.Product;
import it.uniba.di.sms1920.everit.utils.models.ProductCategory;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.request.ProductCategoryRequest;
import it.uniba.di.sms1920.everit.utils.request.RestaurateurRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;


public class ResultDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    private static final String GONE = "GONE";
    private static final String VISIBLE = "VISIBLE";


    private Restaurateur restaurateur;
    private TextView textViewPhoneNumber, textViewAddress, textViewOpenClosed;
    private LinearLayout layoutMenuContainer, layoutMenuText;

    private ExpandableListView expandableListView ;
    private CustomExpandibleMenuAdapter expandableListAdapter;
    private List<ProductCategory> expandableListDetail = new LinkedList<>();


    public ResultDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getArguments() != null) && (getArguments().containsKey(ARG_ITEM_ID))) {
            long id = getArguments().getLong(ARG_ITEM_ID);

            RestaurateurRequest restaurateurRequest = new RestaurateurRequest();
            restaurateurRequest.read(id, new RequestListener<Restaurateur>() {
                @Override
                public void successResponse(Restaurateur response) {
                    restaurateur = response;
                    expandableListDetail = (List<ProductCategory>) restaurateur.getProductCategories();
                    expandableListAdapter = new CustomExpandibleMenuAdapter(getActivity(), expandableListDetail);
                    expandableListView.setAdapter(expandableListAdapter);

                    initComponent();
                }

                @Override
                public void errorResponse(RequestException error) {

                }
            });
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.result_detail, container, false);
        //TODO Mapping dati ristorante

        layoutMenuContainer = rootView.findViewById(R.id.layoutMenuContainer);
        layoutMenuText = rootView.findViewById(R.id.layoutMenuText);
        textViewAddress = rootView.findViewById(R.id.textViewAddress);
        textViewPhoneNumber = rootView.findViewById(R.id.textViewCall);
        textViewOpenClosed = rootView.findViewById(R.id.textViewOpenClosed);
        expandableListView = rootView.findViewById(R.id.expandableMenu);

        layoutMenuContainer.setVisibility(View.GONE);
        layoutMenuContainer.setTag(GONE);

        layoutMenuText.setOnClickListener(v -> showOrHide());


        return rootView;
    }

    private void initComponent(){
        textViewPhoneNumber.setText(restaurateur.getPhoneNumber());
        textViewAddress.setText(restaurateur.getAddress());
    }

    private void showOrHide(){
        String status = (String) layoutMenuContainer.getTag();
        if(status.equals(GONE)){
            layoutMenuContainer.setVisibility(View.VISIBLE);
            layoutMenuContainer.setTag(VISIBLE);
        }else{
            layoutMenuContainer.setVisibility(View.GONE);
            layoutMenuContainer.setTag(GONE);
        }

    }

}


