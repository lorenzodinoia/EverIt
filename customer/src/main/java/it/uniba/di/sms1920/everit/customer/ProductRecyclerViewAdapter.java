package it.uniba.di.sms1920.everit.customer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import it.uniba.di.sms1920.everit.utils.models.Product;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder>{
    private final List<ProductItem> productItems = new ArrayList<>();

    public ProductRecyclerViewAdapter(Map<Product, Integer> productItems) {
        this.productItems.clear();
        for (Map.Entry<Product, Integer> entry : productItems.entrySet()) {
            final ProductRecyclerViewAdapter.ProductItem productItem = new ProductRecyclerViewAdapter.ProductItem(entry.getKey(), entry.getValue());
            this.productItems.add(productItem);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductItem productItem = productItems.get(position);
        String productText = (productItem.getQuantity() > 1) ? String.format(Locale.getDefault(),"%s (x %d)", productItem.getProduct().getName(), productItem.getQuantity())
                : productItem.getProduct().getName();
        holder.textViewProductName.setText(productText);
        holder.textViewPrice.setText(String.format(Locale.getDefault(),"€ %.2f", productItem.getProduct().getPrice()*productItem.getQuantity()));

        holder.itemView.setTag(productItem);
    }

    @Override
    public int getItemCount() { return productItems.size(); }


    protected static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textViewProductName;
        final TextView textViewPrice;

        ViewHolder(View view) {
            super(view);
            textViewProductName = (TextView) view.findViewById(R.id.textViewProductName);
            textViewPrice = (TextView) view.findViewById(R.id.textViewPrice);
        }
    }

    private static class ProductItem {
        private Product product;
        private int quantity;

        private ProductItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        private Product getProduct() {
            return product;
        }

        private int getQuantity() {
            return quantity;
        }
    }
}
