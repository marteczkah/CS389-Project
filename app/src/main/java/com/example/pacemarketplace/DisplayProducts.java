package com.example.pacemarketplace;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.core.Filter;

import org.w3c.dom.Text;

public class DisplayProducts extends FirestoreRecyclerAdapter<Product, DisplayProducts.ProductHolder> {

    private Context mContext;
    private FragmentManager transaction;

    public DisplayProducts(FirestoreRecyclerOptions<Product> options, Context mContext, FragmentManager transaction) {
        super(options);
        this.mContext = mContext;
        this.transaction = transaction;
    }

    @Override
    protected void onBindViewHolder(final ProductHolder productHolder, int i, final Product product) {
        productHolder.productName.setText(product.getProductName());
        productHolder.productDescription.setText(product.getProductDescription());
        productHolder.productPrice.setText(product.getProductPrice());

        productHolder.productCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getName = product.getProductName();
                String getDescription = product.getProductDescription();
                String getPrice = product.getProductPrice();
                ProductDetails pd = new ProductDetails(getName, getDescription, getPrice);
                FragmentTransaction fragmentTransaction = transaction.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, pd);
                fragmentTransaction.commit();
            }
        });
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
        RelativeLayout productCard;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            productName = (TextView) itemView.findViewById(R.id.product_name);
            productPrice = (TextView) itemView.findViewById(R.id.product_price);
            productDescription = (TextView) itemView.findViewById(R.id.product_description);
            productCard = (RelativeLayout) itemView.findViewById(R.id.product_card);
        }
    }
}
