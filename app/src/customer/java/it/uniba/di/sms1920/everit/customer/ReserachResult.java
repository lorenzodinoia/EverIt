package it.uniba.di.sms1920.everit.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import it.uniba.di.sms1920.everit.R;
import it.uniba.di.sms1920.everit.customer.fragments.CardFragmentMain;

public class ReserachResult extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserach_result);

        recyclerView = findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Serve creare l' adapter per fornire tutte le card
//        mAdapter = new MyAdapter(myDataset);
//        recyclerView.setAdapter(mAdapter);

        //Inizia la transaction per caricare il fragment

        // Create new fragment and transaction
        Fragment newFragment = new CardFragmentMain();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(new CardFragmentMain(), "eskere");
        transaction.addToBackStack(null);

        transaction.commit();


    }
}
