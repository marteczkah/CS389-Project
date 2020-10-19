package com.example.pacemarketplace;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class DisplayProducts extends FirestoreRecyclerAdapter<Product, DisplayProducts.ProductHolder> {

    public DisplayProducts(FirestoreRecyclerOptions<Product> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(ProductHolder productHolder, int i, Product product) {
        productHolder.productName.setText(product.getProductName());
        productHolder.productDescription.setText(product.getProductDescription());
        productHolder.productPrice.setText(product.getProductPrice());
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card,parent,false);

        return new ProductHolder(v);
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productPrice;
        TextView productDescription;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            productName = (TextView) itemView.findViewById(R.id.product_name);
            productPrice = (TextView) itemView.findViewById(R.id.product_price);
            productDescription = (TextView) itemView.findViewById(R.id.product_description);
        }
    }
}
