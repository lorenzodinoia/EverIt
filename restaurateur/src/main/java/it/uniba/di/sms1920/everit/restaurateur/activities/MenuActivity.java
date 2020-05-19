package it.uniba.di.sms1920.everit.restaurateur.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import it.uniba.di.sms1920.everit.restaurateur.R;

public class MenuActivity extends AppCompatActivity implements DialogNewCategory.DialogNewCategoryListener {

    private ExpandableListView expandableListView ;
    private CustomExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<>();

    private Button btnAddNewCat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar =  findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.initComponent();
    }

    private void initComponent() {
        btnAddNewCat = findViewById(R.id.btnAddCategory);
        btnAddNewCat.setOnClickListener(v -> this.openDialog());
        this.initExpandableList();
    }

    private void initExpandableList() {
        expandableListView = findViewById(R.id.expandableMenu);
        this.fillListDetails();
        expandableListTitle = new ArrayList<>(this.expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

    }

    private void fillListDetails(){
        List<String> pizze = new ArrayList<String>();
        pizze.add("Margherita");
        pizze.add("Diavola");
        pizze.add("4 stagioni");
        pizze.add("");


        List<String> bevande = new ArrayList<String>();
        bevande.add("Coca Cola");
        bevande.add("Sprite");
        bevande.add("");

        List<String> dolci = new ArrayList<String>();
        dolci.add("Babà");
        dolci.add("Frappè");
        dolci.add("");

        expandableListDetail.put("PIZZE", pizze);
        expandableListDetail.put("BIBITE", bevande);
        expandableListDetail.put("DOLCI", dolci);
    }


    private void updateAdapter(){
        expandableListTitle = new ArrayList<>(this.expandableListDetail.keySet());
        expandableListAdapter.updateAdapter(expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
    }


    private void openDialog() {
        DialogNewCategory  newCatDialog = new DialogNewCategory();
        newCatDialog.show(getSupportFragmentManager(), "New Category");
    }


    @Override
    public void getNewCatName(String name) {
        String newCatName = name.toUpperCase();
        Toast.makeText(this, name, Toast.LENGTH_LONG).show();

        List<String> newCatItems = new ArrayList<>();
        newCatItems.add("");
        this.expandableListDetail.put(newCatName, newCatItems);

        updateAdapter();
    }

}

