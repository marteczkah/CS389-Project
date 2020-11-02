package com.example.pacemarketplace;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetails extends Fragment {

    String name, description, price, productID, sellerID;
    TextView product_name, product_description, product_price;
    Button addFavorite;
    FirebaseFirestore database;
    FirebaseAuth fAuth;

    public ProductDetails() {
        //required empty constructor
    }

    public ProductDetails(String name, String description, String price, String productID, String sellerID){
        this.name = name;
        this.description = description;
        this.price = price;
        this.productID = productID;
        this.sellerID = sellerID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.product_details_page, container, false);
        database = FirebaseFirestore.getInstance();
        product_name = (TextView) v.findViewById(R.id.product_details_name);
        product_description = (TextView) v.findViewById(R.id.product_details_description);
        product_price = (TextView) v.findViewById(R.id.product_details_price);
        addFavorite = (Button) v.findViewById(R.id.add_favorite);
        product_name.setText(name);
        product_description.setText(description);
        product_price.setText("$"+price);
        addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth = FirebaseAuth.getInstance();
                String userID = fAuth.getCurrentUser().getUid();
                final DocumentReference docRef = database.collection("Users").document(userID);
//                docRef.update("favorites", FieldValue.arrayUnion(productID));
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        List<String> favoritesList = (List<String>) document.get("favorites");
                        if (favoritesList.contains(productID)) {
                            docRef.update("favorites", FieldValue.arrayRemove(productID));
                        } else {
                            docRef.update("favorites", FieldValue.arrayUnion(productID));
                        }
                    }
                });
            }
        });
        return v;
    }
}