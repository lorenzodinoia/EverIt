package it.uniba.di.sms1920.everit.restaurateur.activities;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.utils.models.Product;
import it.uniba.di.sms1920.everit.utils.models.ProductCategory;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.ProductCategoryRequest;
import it.uniba.di.sms1920.everit.utils.request.ProductRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<ProductCategory> expandableListDetail;

    CustomExpandableListAdapter(Context context, List<ProductCategory> expandableListDetail) {
        this.context = context;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        List<Product> values = new ArrayList<>(this.expandableListDetail.get(listPosition).getProducts());
        return values.get(expandedListPosition) ;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) { return expandedListPosition; }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Product child = (Product) getChild(listPosition, expandedListPosition);
        final String expandedListText = child.getName();
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        List<Product> values = new ArrayList<>(expandableListDetail.get(listPosition).getProducts());

        if(isLastChild){
            convertView = layoutInflater.inflate(R.layout.list_last_item, null);
            MaterialButton btnAdd = convertView.findViewById(R.id.btnAddItem);
            btnAdd.setOnClickListener(v -> {
                Dialog dialogModItem = new Dialog(context);
                dialogModItem.setContentView(R.layout.dialog_mod_item);
                dialogModItem.setTitle(R.string.addNewProduct);

                TextInputEditText newName = dialogModItem.findViewById(R.id.editTextProductName);
                TextInputEditText newDescription = dialogModItem.findViewById(R.id.editTextProductDescription);
                TextInputEditText newPrice = dialogModItem.findViewById(R.id.editTextProductPrice);

                MaterialButton confirm = dialogModItem.findViewById(R.id.BtnConfirm);
                MaterialButton cancel = dialogModItem.findViewById(R.id.BtnCancel);

                confirm.setOnClickListener(v1 -> {
                    if(!newName.getText().toString().isEmpty()) {
                        if (!newDescription.getText().toString().isEmpty()) {
                            if (!newPrice.getText().toString().isEmpty()) {
                                values.remove(values.size() - 1);

                                Product newProduct = new Product(
                                        expandableListDetail.get(listPosition).getId(),
                                        newName.getText().toString(),
                                        Float.valueOf(newPrice.getText().toString()),
                                        newDescription.getText().toString(),
                                        expandableListDetail.get(listPosition),
                                        (Restaurateur) Providers.getAuthProvider().getUser()
                                );

                                values.add(newProduct);

                                ProductRequest productRequest = new ProductRequest();
                                productRequest.create(newProduct, new RequestListener<Product>() {
                                    @Override
                                    public void successResponse(Product response) {
                                        Product lastProduct = new Product("", 0, "", expandableListDetail.get(listPosition), null);
                                        values.add(lastProduct);
                                        expandableListDetail.get(listPosition).setProducts(values);
                                        updateAdapter(); //EX
                                    }

                                    @Override
                                    public void errorResponse(RequestException error) {
                                    }
                                });
                                dialogModItem.dismiss();
                                updateAdapter();

                            }
                            else {
                                TextInputLayout newPriceLayout = dialogModItem.findViewById(R.id.editTextProductPriceContainer);
                                newPriceLayout.setError(String.valueOf(R.string.emptyFieldError));
                            }
                        }else{
                            TextInputLayout newDescriptionLayout = dialogModItem.findViewById(R.id.editTextProductDescriptionContainer);
                            newDescriptionLayout.setError(String.valueOf(R.string.emptyFieldError));
                        }
                    }else {
                        TextInputLayout newNameLayout = dialogModItem.findViewById(R.id.editTextProductNameContainer);
                        newNameLayout.setError(String.valueOf(R.string.emptyFieldError));
                    }

                });

                cancel.setOnClickListener(v1 -> dialogModItem.dismiss());

                dialogModItem.show();
            });
        }
        else {
            convertView = layoutInflater.inflate(it.uniba.di.sms1920.everit.utils.R.layout.list_item, null);

            TextView expandedListTextView = convertView.findViewById(R.id.expandedListItem);
            expandedListTextView.setText(values.get(expandedListPosition).getName());

            TextView productDescription = convertView.findViewById(R.id.textViewDescription);
            productDescription.setText(values.get(expandedListPosition).getDetails());

            TextView productPrice = convertView.findViewById(R.id.textViewPrice);
            productPrice.setText(String.valueOf(values.get(expandedListPosition).getPrice()));

            MaterialButton btnModItem = convertView.findViewById(R.id.btnModItem);
            Drawable iconMod = this.context.getDrawable(android.R.drawable.ic_menu_edit);
            btnModItem.setIcon(iconMod);
            btnModItem.setOnClickListener(v -> {
                Dialog dialogModItem = new Dialog(context);
                dialogModItem.setContentView(R.layout.dialog_mod_item);
                dialogModItem.setTitle(R.string.modProductName);

                TextInputEditText newName = dialogModItem.findViewById(R.id.editTextProductName);
                TextInputEditText newDescription = dialogModItem.findViewById(R.id.editTextProductDescription);
                TextInputEditText newPrice = dialogModItem.findViewById(R.id.editTextProductPrice);

                MaterialButton confirm = dialogModItem.findViewById(R.id.BtnConfirm);
                MaterialButton cancel = dialogModItem.findViewById(R.id.BtnCancel);

                confirm.setOnClickListener(v1 -> {
                    values.get(expandedListPosition).setName(newName.getText().toString());
                    values.get(expandedListPosition).setDetails(newDescription.getText().toString());
                    values.get(expandedListPosition).setPrice(Float.parseFloat(newPrice.getText().toString()));

                    expandableListDetail.get(listPosition).setProducts(values);

                    ProductRequest productRequest = new ProductRequest();
                    //TODO errore 404 problemi con l'id categoria credo
                    productRequest.update(values.get(expandedListPosition), new RequestListener<Product>() {
                        @Override
                        public void successResponse(Product response) {
                            updateAdapter();
                        }

                        @Override
                        public void errorResponse(RequestException error) {
                        }
                    });
                    dialogModItem.dismiss();
                });

                cancel.setOnClickListener(v1 -> dialogModItem.dismiss());

                dialogModItem.show();
            });

            MaterialButton btnDelItem = convertView.findViewById(R.id.btnDelItem);
            Drawable iconDel = this.context.getDrawable(android.R.drawable.ic_menu_delete);
            btnModItem.setIcon(iconDel);
            btnDelItem.setOnClickListener(v -> {
                Dialog dialogYN = new Dialog(context);
                dialogYN.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_choice_yn);
                dialogYN.setTitle(R.string.deleteCategory);

                TextView newName = dialogYN.findViewById(R.id.TextViewMessage);
                newName.setText(String.valueOf("You are going to delete " + expandedListText + ". Are you sure ?"));

                MaterialButton confirm = dialogYN.findViewById(R.id.BtnConfirm);
                MaterialButton cancel = dialogYN.findViewById(R.id.BtnCancel);

                confirm.setOnClickListener(v1 -> {
                    ProductRequest productRequest = new ProductRequest();
                    productRequest.delete(values.get(expandedListPosition).getId(), new RequestListener<Boolean>() {
                        @Override
                        public void successResponse(Boolean response) {
                            values.remove(expandedListPosition);
                            expandableListDetail.get(listPosition).setProducts(values);
                            updateAdapter();
                        }

                        @Override
                        public void errorResponse(RequestException error) {
                        }
                    });
                    updateAdapter(); //EX
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
            MaterialButton confirm = dialogModName.findViewById(R.id.BtnConfirm);
            MaterialButton cancel = dialogModName.findViewById(R.id.BtnCancel);

            confirm.setOnClickListener(v1 -> {
                if(!newName.getText().toString().isEmpty()) {
                    expandableListDetail.get(listPosition).setName(newName.getText().toString());
                    ProductCategoryRequest productCategoryRequest = new ProductCategoryRequest();
                    productCategoryRequest.update(expandableListDetail.get(listPosition), new RequestListener<ProductCategory>() {
                        @Override
                        public void successResponse(ProductCategory response) {
                            updateAdapter();
                        }

                        @Override
                        public void errorResponse(RequestException error) {
                            //TODO vedere
                        }
                    });
                    dialogModName.dismiss();
                    updateAdapter(); //EX
                }else{
                    TextInputLayout newNameLayout = dialogModName.findViewById(R.id.editTextCategoryNameContainer);
                    newNameLayout.setError(String.valueOf(R.string.emptyFieldError));
                }
            });

            cancel.setOnClickListener(v1 -> dialogModName.dismiss());

            dialogModName.show();
        });

        MaterialButton btnDelGroup = convertView.findViewById(R.id.btnDelGroup);
        btnDelGroup.setFocusable(false);
        btnDelGroup.setOnClickListener(v -> {
            Dialog dialogYN = new Dialog(context);
            dialogYN.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_choice_yn);
            dialogYN.setTitle(R.string.deleteCategory);

            TextView newName = dialogYN.findViewById(R.id.TextViewMessage);
            newName.setText(String.valueOf("You are going to delete " + listTitle+ " Are you sure ?"));


            MaterialButton confirm = dialogYN.findViewById(R.id.BtnConfirm);
            MaterialButton cancel = dialogYN.findViewById(R.id.BtnCancel);

            confirm.setOnClickListener(v1 -> {
                ProductCategoryRequest productCategoryRequest = new ProductCategoryRequest();
                productCategoryRequest.delete(expandableListDetail.get(listPosition).getId(), new RequestListener<Boolean>() {
                    @Override
                    public void successResponse(Boolean response) {
                        expandableListDetail.remove(expandableListDetail.get(listPosition));
                        updateAdapter();
                    }

                    @Override
                    public void errorResponse(RequestException error) {
                        //TODO da fare
                        Log.d("Error Response", error.toString());
                    }
                });
                updateAdapter(); //EX
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


    public void updateAdapter(){
        notifyDataSetChanged();
    }


    public void addCategory(){
        Dialog dialogNewCategory = new Dialog(context);
        dialogNewCategory.setContentView(R.layout.dialog_new_category);
        dialogNewCategory.setTitle("NEW CATEGORY");

        TextInputEditText newName = dialogNewCategory.findViewById(R.id.editTextCategoryName);
        MaterialButton cancel = dialogNewCategory.findViewById(R.id.BtnCancel);
        MaterialButton confirm = dialogNewCategory.findViewById(R.id.BtnConfirm);

        confirm.setOnClickListener(v1 -> {
            ProductCategory newCat = new ProductCategory(newName.getText().toString());
            List<Product> products = new LinkedList<>();
            newCat.setProducts(products);

            ProductCategoryRequest productCategoryRequest = new ProductCategoryRequest();
            productCategoryRequest.create(newCat, new RequestListener<ProductCategory>() {
                @Override
                public void successResponse(ProductCategory response) {
                    Product lastProduct = new Product("", 0, "", newCat, null);
                    products.add(lastProduct);
                    newCat.setProducts(products);

                    expandableListDetail.add(newCat);
                    updateAdapter();
                }

                @Override
                public void errorResponse(RequestException error) {
                    //TODO gestire risposta errore
                }
            });
            dialogNewCategory.dismiss();
            updateAdapter();
        });

        cancel.setOnClickListener(v1 -> dialogNewCategory.dismiss());

        dialogNewCategory.show();
        updateAdapter();
    }




}
