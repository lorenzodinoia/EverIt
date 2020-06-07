package it.uniba.di.sms1920.everit.restaurateur.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.utils.models.Product;
import it.uniba.di.sms1920.everit.utils.models.ProductCategory;
import it.uniba.di.sms1920.everit.utils.request.ProductCategoryRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class MenuActivity extends AppCompatActivity {

    private ExpandableListView expandableListView ;
    private CustomExpandableListAdapter expandableListAdapter;
    private List<ProductCategory> expandableListDetail = new LinkedList<>();

    private MaterialButton btnAddNewCat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar =  findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.fillListDetails();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initComponent() {
        expandableListView = findViewById(R.id.expandableMenuOpening);
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        btnAddNewCat = this.findViewById(R.id.btnAddCategory);
        btnAddNewCat.setOnClickListener(v -> expandableListAdapter.addCategory());
    }

    private void fillListDetails(){
        ProductCategoryRequest productCategoryRequest = new ProductCategoryRequest();
        productCategoryRequest.readAll(new RequestListener<Collection<ProductCategory>>() {
            @Override
            public void successResponse(Collection<ProductCategory> response) {
                expandableListDetail = (List<ProductCategory>)response;

                Product lastProduct = new Product("", 0, "", null, null);
                for(ProductCategory pd : expandableListDetail){
                    List<Product> pdProd = new ArrayList<>(pd.getProducts());
                    pdProd.add(lastProduct);
                    pd.setProducts(pdProd);
                }
                initComponent();
            }

            @Override
            public void errorResponse(RequestException error) {
                //TODO il fragment dell'errore
            }

        });
    }

}

