package com.example.pacemarketplace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {

    private ArrayList<Product> products;
    private ArrayList<Product> productsFull;
    private Context mContext;
    private FragmentManager transaction;

    public RecyclerViewAdapter(ArrayList<Product> products, Context mContext, FragmentManager transaction) {
        this.products = products;
        this.mContext = mContext;
        this.transaction = transaction;
        productsFull = new ArrayList<>(products);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Product product = products.get(position);
        holder.productName.setText(product.getProductName());
        holder.productDescription.setText(product.getProductDescription());
        holder.productPrice.setText(product.getProductPrice());

        holder.productCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getName = product.getProductName();
                String getDescription = product.getProductDescription();
                String getPrice = product.getProductPrice();
                String getProductID = product.getProductID();
                String getSellerID = product.getSellerID();

                ProductDetails pd = new ProductDetails(getName, getDescription, getPrice, getProductID, getSellerID);
                FragmentTransaction fragmentTransaction = transaction.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, pd);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productDescription, productPrice;
        RelativeLayout productCard;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.product_name);
            productDescription = (TextView) itemView.findViewById(R.id.product_description);
            productPrice = (TextView) itemView.findViewById(R.id.product_price);
            productCard = (RelativeLayout) itemView.findViewById(R.id.product_card);
        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Product> filteredProducts = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredProducts.addAll(productsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Product product : productsFull) {
                    if (product.getProductName().toLowerCase().contains(filterPattern)) {
                        filteredProducts.add(product);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredProducts;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            products.clear();
            products.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
