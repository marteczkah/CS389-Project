package com.example.pacemarketplace;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddProductPage extends Fragment {

    Button addProduct;
    EditText addName;

    FirebaseFirestore database = FirebaseFirestore.getInstance();

    public AddProductPage() {
        //required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_product_page, container, false);
        addProduct = (Button) v.findViewById(R.id.addNameToDatabase);
        addName = (EditText) v.findViewById(R.id.ProductName);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = addName.getText().toString();
                Map<String,Object> data = new HashMap<>();
                data.put("Product Name", productName);
                database.collection("Products").document("Products Information").set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity().getBaseContext(), "Success", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity().getBaseContext(), "Failure", Toast.LENGTH_LONG).show();

                            }
                        });
            }
        });
        return v;
    }
}
