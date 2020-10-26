package com.example.pacemarketplace;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

public class ProductDetails extends Fragment {

    String name, description, price;
    TextView product_name, product_description, product_price;

    public ProductDetails() {
        //required empty constructor
    }

    public ProductDetails(String name, String description, String price){
        this.name = name;
        this.description = description;
        this.price = price;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.product_details_page, container, false);
        product_name = (TextView) v.findViewById(R.id.product_details_name);
        product_description = (TextView) v.findViewById(R.id.product_details_description);
        product_price = (TextView) v.findViewById(R.id.product_details_price);
        product_name.setText(name);
        product_description.setText(description);
        product_price.setText("$"+price);
        return v;
    }
}