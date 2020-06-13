package it.uniba.di.sms1920.everit.restaurateur.activities;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import androidx.annotation.RequiresApi;
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
import it.uniba.di.sms1920.everit.utils.request.ProductRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class MenuActivity extends AppCompatActivity {


    private ExpandableListView expandableListView ;
    private CustomExpandableListAdapter expandableListAdapter;
    private List<ProductCategory> expandableListDetail;

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
        expandableListView = findViewById(R.id.expandableMenu);
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListDetail, this);
        expandableListView.setAdapter(expandableListAdapter);

        btnAddNewCat = this.findViewById(R.id.btnAddCategory);
        btnAddNewCat.setOnClickListener(v -> expandableListAdapter.addCategory());

    }

    private void fillListDetails(){
        ProductCategoryRequest productCategoryRequest = new ProductCategoryRequest();
        productCategoryRequest.readAll(new RequestListener<Collection<ProductCategory>>() {
            @Override
            public void successResponse(Collection<ProductCategory> response) {
                expandableListDetail = (List<ProductCategory>) response;

                Product lastProduct = new Product("", 0, "", null, null);
                for(ProductCategory pd : expandableListDetail){
                    LinkedList<Product> pdProd = new LinkedList<>(pd.getProducts());
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



    public void addNewProductCategory(ProductCategory newCat){
        List<Product> products = new ArrayList<>();

        ProductCategoryRequest productCategoryRequest = new ProductCategoryRequest();
        productCategoryRequest.create(newCat, new RequestListener<ProductCategory>() {
            @Override
            public void successResponse(ProductCategory response) {

                Product lastProduct = new Product("", 0, "", null, null);
                products.add(lastProduct);

                response.setProducts(products);
                expandableListDetail.add(response);

                expandableListAdapter.notifyDataSetChanged();
            }

            @Override
            public void errorResponse(RequestException error) {
                //TODO gestire risposta errore
            }
        });

    }

    public void deleteProductCategory(ProductCategory productCategory){
        ProductCategoryRequest productCategoryRequest = new ProductCategoryRequest();
        productCategoryRequest.delete(productCategory.getId(), new RequestListener<Boolean>() {
            @Override
            public void successResponse(Boolean response) {
                expandableListDetail.remove(productCategory);
                expandableListAdapter.notifyDataSetChanged();
            }

            @Override
            public void errorResponse(RequestException error) {
                //TODO da fare
                Log.d("Error Response", error.toString());
            }
        });
    }

    public void updateProductCategory(ProductCategory cat, int listPosition){
        ProductCategoryRequest productCategoryRequest = new ProductCategoryRequest();
        productCategoryRequest.update(cat, new RequestListener<ProductCategory>() {
            @Override
            public void successResponse(ProductCategory response) {
                expandableListDetail.get(listPosition).setName(cat.getName());
                expandableListAdapter.notifyDataSetChanged();
            }

            @Override
            public void errorResponse(RequestException error) {
                //TODO vedere
            }
        });
    }



    public  void deleteCategoryItem(int listPosition, long idProd){
        Product product = expandableListDetail.get(listPosition).getProductByIndex(idProd);

        ProductRequest productRequest = new ProductRequest();
        productRequest.delete(product.getId(), new RequestListener<Boolean>() {
            @Override
            public void successResponse(Boolean response) {
                expandableListDetail.get(listPosition).getProducts().remove(product);

                expandableListAdapter.notifyDataSetChanged();
            }

            @Override
            public void errorResponse(RequestException error) {
            }
        });
    }

    public void addCategoryItem(int listPosition, Product product){
        List<Product> newValues = new ArrayList<>(expandableListDetail.get(listPosition).getProducts());

        ProductRequest productRequest = new ProductRequest();
        productRequest.create(product, new RequestListener<Product>() {
            @Override
            public void successResponse(Product response) {
                Product lastProduct = new Product("", 0, "", null, null);

                newValues.remove(newValues.size()-1);
                newValues.add(response);


                newValues.add(lastProduct);
                expandableListDetail.get(listPosition).setProducts(newValues);

                expandableListAdapter.notifyDataSetChanged();
            }

            @Override
            public void errorResponse(RequestException error) {
                Log.d("QUESTO", error.toString());
            }
        });

    }

    public void updateCategoryItem(Product modifiedProduct, int listPosition){
        ProductRequest productRequest = new ProductRequest();
        productRequest.update(modifiedProduct, new RequestListener<Product>() {
            @Override
            public void successResponse(Product response) {
                expandableListDetail.get(listPosition).getProductByIndex(modifiedProduct.getId()).setName(modifiedProduct.getName());
                expandableListDetail.get(listPosition).getProductByIndex(modifiedProduct.getId()).setDetails(modifiedProduct.getDetails());
                expandableListDetail.get(listPosition).getProductByIndex(modifiedProduct.getId()).setName(modifiedProduct.getName());
                expandableListAdapter.notifyDataSetChanged();
            }

            @Override
            public void errorResponse(RequestException error) {
                //TODO gestire
            }
        });
    }






}

