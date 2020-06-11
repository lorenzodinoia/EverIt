package it.uniba.di.sms1920.everit.customer.activities.results;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.models.Product;
import it.uniba.di.sms1920.everit.utils.models.ProductCategory;

public class CustomExpandibleMenuAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<ProductCategory> expandableListDetail;

    CustomExpandibleMenuAdapter(Context context, List<ProductCategory> expandableListDetail) {
        this.context = context;
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
        Product child = (Product) getChild(listPosition, expandedListPosition);
        final String expandedListText = child.getName();
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        List<Product> values = new ArrayList<>(expandableListDetail.get(listPosition).getProducts());

        convertView = layoutInflater.inflate(it.uniba.di.sms1920.everit.utils.R.layout.list_item, null);

        TextView expandedListTextView = convertView.findViewById(R.id.expandedListItem);
        expandedListTextView.setText(values.get(expandedListPosition).getName());

        TextView productDescription = convertView.findViewById(R.id.textViewDescription);
        productDescription.setText(values.get(expandedListPosition).getDetails());

        TextView productPrice = convertView.findViewById(R.id.textViewPrice);
        productPrice.setText(String.valueOf(values.get(expandedListPosition).getPrice()));

        MaterialButton btnAddItem =   convertView.findViewById(R.id.btnModItem);
        MaterialButton btnRemovelItem =  convertView.findViewById(R.id.btnDelItem);

        return convertView;
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
