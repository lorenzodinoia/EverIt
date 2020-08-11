package it.uniba.di.sms1920.everit.restaurateur.activities;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Product;
import it.uniba.di.sms1920.everit.utils.models.ProductCategory;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<ProductCategory> expandableListDetail;
    private MenuActivity menuActivity;

    CustomExpandableListAdapter(Context context, List<ProductCategory> expandableListDetail, MenuActivity menuActivity) {
        this.context = context;
        this.expandableListDetail = expandableListDetail;
        this.menuActivity = menuActivity;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        LinkedList<Product> values = new LinkedList<>(this.expandableListDetail.get(listPosition).getProducts());
        return values.get(expandedListPosition) ;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) { return expandedListPosition; }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Product product = (Product) getChild(listPosition, expandedListPosition);
        ProductCategory category = (ProductCategory) getGroup(listPosition);
        List<Product> products = new ArrayList<> (category.getProducts());

        final String expandedListText = product.getName();
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(isLastChild){
            convertView = layoutInflater.inflate(R.layout.list_last_item, null);
            MaterialButton btnAdd = convertView.findViewById(R.id.btnAddItem);
            btnAdd.setOnClickListener(v -> {
                Dialog dialogModItem = new Dialog(context);
                dialogModItem.setContentView(R.layout.dialog_mod_item);
                dialogModItem.setTitle(R.string.addNewProduct);

                TextInputEditText newName = dialogModItem.findViewById(R.id.editTextProductName);
                TextInputLayout newNameLayout = dialogModItem.findViewById(R.id.editTextProductNameContainer);

                TextInputEditText newDescription = dialogModItem.findViewById(R.id.editTextProductDescription);
                TextInputLayout newDescriptionLayout = dialogModItem.findViewById(R.id.editTextProductDescriptionContainer);

                TextInputEditText newPrice = dialogModItem.findViewById(R.id.editTextProductPrice);
                TextInputLayout newPriceLayout = dialogModItem.findViewById(R.id.editTextProductPriceContainer);

                MaterialButton confirm = dialogModItem.findViewById(R.id.BtnConfirm);
                MaterialButton cancel = dialogModItem.findViewById(R.id.BtnCancel);

                confirm.setOnClickListener(v1 -> {
                    if(Utility.isValidProductName(newName.getText().toString(), newNameLayout, context)){
                        if(Utility.isValidProductDescription(newDescription.getText().toString(), newDescriptionLayout, context)){
                            if(Utility.isValidProductPrice(Float.parseFloat(newPrice.getText().toString()), newPriceLayout, context)){
                                Product newProduct = new Product(
                                        newName.getText().toString(),
                                        Float.parseFloat(newPrice.getText().toString()),
                                        newDescription.getText().toString(),
                                        new ProductCategory(category.getId(), category.getName()),
                                        null
                                );

                                menuActivity.addCategoryItem(listPosition, newProduct);
                                dialogModItem.dismiss();
                            }
                        }
                    }

                });

                cancel.setOnClickListener(v1 -> dialogModItem.dismiss());

                dialogModItem.show();
            });
        }
        else {
            convertView = layoutInflater.inflate(it.uniba.di.sms1920.everit.utils.R.layout.list_item, null);

            TextView expandedListTextView = convertView.findViewById(R.id.expandedListItem);
            expandedListTextView.setText(product.getName());

            TextView productDescription = convertView.findViewById(R.id.textViewDescription);
            productDescription.setText(product.getDetails());

            TextView productPrice = convertView.findViewById(R.id.textViewPrice);
            productPrice.setText(String.valueOf(product.getPrice()));

            MaterialButton btnModItem = convertView.findViewById(R.id.btnModItem);
            Drawable iconMod = this.context.getDrawable(android.R.drawable.ic_menu_edit);
            btnModItem.setIcon(iconMod);
            btnModItem.setOnClickListener(v -> {
                Dialog dialogModItem = new Dialog(context);
                dialogModItem.setContentView(R.layout.dialog_mod_item);
                dialogModItem.setTitle(R.string.modProduct);

                TextInputEditText newName = dialogModItem.findViewById(R.id.editTextProductName);
                TextInputLayout newNameLayout = dialogModItem.findViewById(R.id.editTextProductPriceContainer);

                TextInputEditText newDescription = dialogModItem.findViewById(R.id.editTextProductDescription);
                TextInputLayout newDescriptionLayout = dialogModItem.findViewById(R.id.editTextProductDescriptionContainer);

                TextInputEditText newPrice = dialogModItem.findViewById(R.id.editTextProductPrice);
                TextInputLayout newPriceLayout = dialogModItem.findViewById(R.id.editTextProductPriceContainer);

                MaterialButton confirm = dialogModItem.findViewById(R.id.BtnConfirm);
                MaterialButton cancel = dialogModItem.findViewById(R.id.BtnCancel);

                confirm.setOnClickListener(v1 -> {

                    if(Utility.isValidProductName(newName.getText().toString(), newNameLayout, context)){
                        if(Utility.isValidProductDescription(newDescription.getText().toString(), newDescriptionLayout, context)){
                            if(Utility.isValidProductPrice(Float.parseFloat(newPrice.getText().toString()), newPriceLayout, context)){
                                Product temp = new Product(
                                        product.getId(),
                                        newName.getText().toString(),
                                        Float.parseFloat(newPrice.getText().toString()),
                                        newDescription.getText().toString(),
                                        new ProductCategory(category.getId(), category.getName()),
                                        null);

                                menuActivity.updateCategoryItem(temp, listPosition);

                                dialogModItem.dismiss();
                            }
                        }
                    }

                });

                cancel.setOnClickListener(v1 -> dialogModItem.dismiss());

                dialogModItem.show();
            });

            MaterialButton btnDelItem = convertView.findViewById(R.id.btnDelItem);
            Drawable iconDel = this.context.getDrawable(android.R.drawable.ic_menu_delete);
            btnDelItem.setIcon(iconDel);
            btnDelItem.setOnClickListener(v -> {
                Dialog dialogYN = new Dialog(context);
                dialogYN.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_y_n);

                TextView title = dialogYN.findViewById(R.id.textViewTitle);
                title.setText(R.string.deleteCategory);

                TextView newName = dialogYN.findViewById(R.id.textViewMessage);
                newName.setText(context.getString(R.string.You_will_delete) + expandedListText + context.getString(R.string.are_you_sure));

                MaterialButton confirm = dialogYN.findViewById(R.id.btnOk);
                MaterialButton cancel = dialogYN.findViewById(R.id.btnCancel);

                confirm.setOnClickListener(v1 -> {
                    menuActivity.deleteCategoryItem(listPosition, product.getId());
                    dialogYN.dismiss();
                });

                cancel.setOnClickListener(v1 -> dialogYN.dismiss());

                dialogYN.show();
            });
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) { return this.expandableListDetail.get(listPosition).getProducts().size(); }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListDetail.get(listPosition);
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
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ProductCategory group = (ProductCategory) getGroup(listPosition);
        String listTitle = group.getName();

        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.list_group, null);

        TextView listTitleTextView = convertView.findViewById(R.id.listTitle);
        listTitleTextView.setFocusable(false);

        MaterialButton btnModGroup = convertView.findViewById(R.id.btnModGroup);
        btnModGroup.setFocusable(false);
        btnModGroup.setOnClickListener(v -> {
            Dialog dialogModName = new Dialog(context);
            dialogModName.setContentView(R.layout.dialog_new_category);
            dialogModName.setTitle(R.string.modCategoryName);

            TextInputEditText newName = dialogModName.findViewById(R.id.editTextCategoryName);
            TextInputLayout newNameLayout = dialogModName.findViewById(R.id.editTextCategoryNameContainer);

            MaterialButton confirm = dialogModName.findViewById(R.id.BtnConfirm);
            MaterialButton cancel = dialogModName.findViewById(R.id.BtnCancel);

            confirm.setOnClickListener(v1 -> {
                if(Utility.isValidCategoryName(newName.getText().toString(), newNameLayout, context)) {
                    ProductCategory modCat = new ProductCategory(
                            group.getId(),
                            newName.getText().toString()
                    );

                    menuActivity.updateProductCategory(modCat, listPosition);
                    dialogModName.dismiss();

                }
            });

            cancel.setOnClickListener(v1 -> dialogModName.dismiss());

            dialogModName.show();
        });

        MaterialButton btnDelGroup = convertView.findViewById(R.id.btnDelGroup);
        btnDelGroup.setFocusable(false);
        btnDelGroup.setOnClickListener(v -> {
            Dialog dialogYN = new Dialog(context);
            dialogYN.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_y_n);

            TextView title = dialogYN.findViewById(R.id.textViewTitle);
            title.setText(R.string.deleteCategory);

            TextView newName = dialogYN.findViewById(R.id.textViewMessage);
            newName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            newName.setText(context.getString(R.string.You_will_delete) + listTitle + context.getString(R.string.are_you_sure));

            Button confirm = dialogYN.findViewById(R.id.btnOk);
            Button cancel = dialogYN.findViewById(R.id.btnCancel);

            confirm.setOnClickListener(v1 -> {
               menuActivity.deleteProductCategory(group);
               dialogYN.dismiss();
            });

            cancel.setOnClickListener(v1 -> dialogYN.dismiss());

            dialogYN.show();
        });

        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        super.unregisterDataSetObserver(observer);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    public void addCategory(){
        Dialog dialogNewCategory = new Dialog(context);
        dialogNewCategory.setContentView(R.layout.dialog_new_category);
        dialogNewCategory.setTitle(R.string.addNewCat);

        TextInputEditText newName = dialogNewCategory.findViewById(R.id.editTextCategoryName);
        TextInputLayout newNameLayout = dialogNewCategory.findViewById(R.id.editTextCategoryNameContainer);

        MaterialButton cancel = dialogNewCategory.findViewById(R.id.BtnCancel);
        MaterialButton confirm = dialogNewCategory.findViewById(R.id.BtnConfirm);

        confirm.setOnClickListener(v1 -> {
            if(Utility.isValidCategoryName(newName.getText().toString(), newNameLayout, context)){
                ProductCategory newCat = new ProductCategory(newName.getText().toString());

                menuActivity.addNewProductCategory(newCat);
                dialogNewCategory.dismiss();
            }

        });

        cancel.setOnClickListener(v1 -> dialogNewCategory.dismiss());

        dialogNewCategory.show();
    }


}
