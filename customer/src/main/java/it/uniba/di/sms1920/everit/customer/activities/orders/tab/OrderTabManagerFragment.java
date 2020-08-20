package it.uniba.di.sms1920.everit.customer.activities.orders.tab;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.orders.OrderDetailFragment;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.request.OrderRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class OrderTabManagerFragment extends Fragment {

    public static String ARG_ORDER_KEY = "order_item";

    private long orderId;
    private Order order;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private OrderTabPagerAdapter pagerAdapter;

    public OrderTabManagerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey("item_id")){
            orderId = bundle.getLong("item_id", 0);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_tab_manager, container, false);

        if(savedInstanceState == null) {
            OrderRequest orderRequest = new OrderRequest();
            orderRequest.read(orderId, new RequestListener<Order>() {
                @Override
                public void successResponse(Order response) {
                    order = response;
                    setUpTabs(view);
                }


                @Override
                public void errorResponse(RequestException error) {
                    promptErrorMessage(error.getMessage());
                }
            });
        }
        else{
            order = savedInstanceState.getParcelable(ARG_ORDER_KEY);
            setUpTabs(view);
        }

        return view;
    }

    private void setUpTabs(View view){
        pagerAdapter = new OrderTabPagerAdapter(getActivity().getSupportFragmentManager(), 0);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_ORDER_KEY, order);
        OrderDetailFragment detailFragment = new OrderDetailFragment();
        detailFragment.setArguments(bundle);
        NotesFragment notesFragment = new NotesFragment();
        notesFragment.setArguments(bundle);

        tabLayout = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.viewPager);

        pagerAdapter.addFragment(detailFragment, getString(R.string.order_detail));
        pagerAdapter.addFragment(notesFragment, getString(R.string.notes));

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void promptErrorMessage(String message){
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_ok);

        TextView title = dialog.findViewById(R.id.textViewTitle);
        title.setText(it.uniba.di.sms1920.everit.utils.R.string.error);

        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v ->{
            dialog.dismiss();
            getActivity().finish();
        });

        dialog.show();
    }

    public Order getOrder(){
        return order;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_ORDER_KEY, order);
    }
}