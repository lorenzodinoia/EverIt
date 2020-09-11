package it.uniba.di.sms1920.everit.customer.results;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.cart.CartConnector;
import it.uniba.di.sms1920.everit.utils.models.Product;
import it.uniba.di.sms1920.everit.utils.models.ProductCategory;

public class CustomExpandibleMenuAdapter extends BaseExpandableListAdapter {

    private Context context;
    private CartConnector cartConnector;
    private List<ProductCategory> expandableListDetail;


    public CustomExpandibleMenuAdapter(Context context, CartConnector cartConnector, List<ProductCategory> expandableListDetail) {
        this.context = context;
        this.cartConnector = cartConnector;
        this.expandableListDetail = expandableListDetail;
        removeEmptyGroups();
    }

    @Override
    public int getGroupCount() {
        return this.expandableListDetail.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListDetail.get(listPosition);
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ProductCategory group = (ProductCategory) getGroup(listPosition);
        String listTitle = group.getName();

        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.list_group, null);

        TextView listTitleTextView = convertView.findViewById(R.id.listTitle);
        listTitleTextView.setFocusable(false);

        listTitleTextView.setText(listTitle);

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) { return this.expandableListDetail.get(listPosition).getProducts().size(); }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        List<Product> values = new ArrayList<>(this.expandableListDetail.get(listPosition).getProducts());
        return values.get(expandedListPosition) ;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) { return expandedListPosition; }

    @Override
    public View getChildView(int listPosition, int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Product currentProduct = (Product) getChild(listPosition, expandedListPosition);

        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(it.uniba.di.sms1920.everit.utils.R.layout.list_item, null);

        TextView expandedListTextView = convertView.findViewById(R.id.expandedListItem);
        expandedListTextView.setText(currentProduct.getName());

        TextView productDescription = convertView.findViewById(R.id.textViewDescription);
        productDescription.setText(currentProduct.getDetails());

        TextView productPrice = convertView.findViewById(R.id.textViewPrice);
        productPrice.setText(String.valueOf(currentProduct.getPrice()));

        MaterialTextView editTextNumber = convertView.findViewById(R.id.editTextNumberContainer);
        editTextNumber.setVisibility(View.VISIBLE);
        editTextNumber.setText(String.valueOf(cartConnector.getPartialOrder().getProductQuantity(currentProduct)));


        MaterialButton btnAddItem = convertView.findViewById(R.id.btnModItem);
        btnAddItem.setOnClickListener(v -> addProduct(currentProduct, editTextNumber));

        MaterialButton btnRemoveItem = convertView.findViewById(R.id.btnDelItem);
        btnRemoveItem.setOnClickListener(v -> removeProduct(currentProduct, editTextNumber));

        return convertView;
    }

    private void addProduct(Product product, MaterialTextView editText){
        if(cartConnector.getPartialOrder().getProductQuantity(product) <= 999) {
            cartConnector.getPartialOrder().addProduct(product);
            editText.setText(String.valueOf(cartConnector.getPartialOrder().getProductQuantity(product)));
        }
    }

    private void removeProduct(Product product, MaterialTextView editText){
        if(cartConnector.getPartialOrder().getProductQuantity(product) > 0 ) {
            cartConnector.getPartialOrder().removeProduct(product);
            editText.setText(String.valueOf(cartConnector.getPartialOrder().getProductQuantity(product)));
        }
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public boolean hasStableIds() { return false; }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) { return false; }


    private void removeEmptyGroups(){
        for(ProductCategory it: new ArrayList<>(expandableListDetail)){
            if(it.getProducts().isEmpty()){
                expandableListDetail.remove(it);
            }
        }
        notifyDataSetChanged();
    }
}
