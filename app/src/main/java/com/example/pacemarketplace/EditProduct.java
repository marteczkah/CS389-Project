package com.example.pacemarketplace;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProduct extends Fragment {

    String productID, productName, productDescription, productPrice;
    TextView productNameTV, productDescriptionTV, productPriceTV;
    Button changeName, changeDescription, changePrice, deleteProduct, submitChanges;
    EditText newName, newDescription, newPrice;
    Boolean isNewName = false, isNewDescription = false, isNewPrice = false;
    FirebaseFirestore database;

    public EditProduct() {
        //empty constructor
    }

    public EditProduct(String productName, String productDescription, String productPrice, String productID) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productID = productID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_product, container, false);
        database = FirebaseFirestore.getInstance();
        //buttons
        changeName = (Button) v.findViewById(R.id.edit_name_button);
        changeDescription = (Button) v.findViewById(R.id.edit_description_button);
        changePrice = (Button) v.findViewById(R.id.edit_price_button);
        deleteProduct = (Button) v.findViewById(R.id.delete_product);
        submitChanges = (Button) v.findViewById(R.id.change_product_info);
        //edit texts
        newName = (EditText) v.findViewById(R.id.change_product_name);
        newDescription = (EditText) v.findViewById(R.id.change_product_description);
        newPrice = (EditText) v.findViewById(R.id.change_product_price);
        //text views
        productNameTV = (TextView) v.findViewById(R.id.name_edit);
        productNameTV.setText(productName);
        productDescriptionTV = (TextView) v.findViewById(R.id.description_edit);
        productDescriptionTV.setText(productDescription);
        productPriceTV = (TextView) v.findViewById(R.id.price_edit);
        productPriceTV.setText("$" + productPrice);

        //open text edit to change name
        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newName.setVisibility(v.VISIBLE);
                isNewName = true;
            }
        });
        //open text edit to change description
        changeDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newDescription.setVisibility(v.VISIBLE);
                isNewDescription = true;
            }
        });
        //open text edit to change price
        changePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPrice.setVisibility(v.VISIBLE);
                isNewPrice = true;
            }
        });
        //delete product
        deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.collection("Products").document(productID).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity().getBaseContext(),
                                        "Product deleted from the database", Toast.LENGTH_LONG)
                                        .show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity().getBaseContext(),
                                        "Product not deleted from the database", Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
            }
        });
        //submit changes
        submitChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNewName) {
                    String updateName = newName.getText().toString();
                    database.collection("Products").document(productID)
                            .update("name", updateName);
                }
                if (isNewDescription) {
                    String updateDescription = newDescription.getText().toString();
                    database.collection("Products").document(productID)
                            .update("description", updateDescription);
                }
                if (isNewPrice) {
                    String updatePrice = newPrice.getText().toString();
                    database.collection("Products").document(productID)
                            .update("price", updatePrice);
                }
            }
        });

        return v;
    }
}