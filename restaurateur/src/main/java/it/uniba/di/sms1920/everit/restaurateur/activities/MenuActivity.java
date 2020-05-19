package it.uniba.di.sms1920.everit.restaurateur.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import it.uniba.di.sms1920.everit.restaurateur.R;

public class MenuActivity extends AppCompatActivity implements DialogNewCategory.DialogNewCategoryListener {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;

    private MaterialButton btnAddNewCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar =  findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.initExpandableList();
        this.initComponent();
    }

    private void initComponent() {
        btnAddNewCat = this.findViewById(R.id.btnAddCategory);
        btnAddNewCat.setOnClickListener(v -> {
            this.openDialog();
        });
    }

    private void initExpandableList() {
        expandableListView = findViewById(R.id.expandableMenu);
        expandableListDetail = ExpandableListDataPump.getData();
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
    }

    private void openDialog() {
        DialogNewCategory  newCatDialog = new DialogNewCategory();
        newCatDialog.show(getSupportFragmentManager(), "New Category");
    }

    @Override
    public void getNewName(String name) {
        String newCatName = name;
        Toast.makeText(getApplicationContext(), newCatName, Toast.LENGTH_SHORT).show();
    }
}

