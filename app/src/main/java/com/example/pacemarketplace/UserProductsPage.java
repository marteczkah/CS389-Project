package com.example.pacemarketplace;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserProductsPage extends Fragment {

    FirebaseFirestore database = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth;
    RecyclerViewAdapter recyclerViewAdapter;
    RecyclerView rv;
    FragmentManager transaction;

    public UserProductsPage() {
        //required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.your_products_page, container, false);
        final ArrayList<Product> userProducts = new ArrayList<>();
        final Context context = getContext();
        fAuth = FirebaseAuth.getInstance();
        String userID = fAuth.getCurrentUser().getUid();
        rv = v.findViewById(R.id.recycler_view_userproducts);
        final DocumentReference docRef = database.collection("Users").document(userID);
        transaction = getFragmentManager();
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                List<String> favoritesID = (List<String>) document.get("userProducts");
                int count = 0;
                for (String id : favoritesID) {
                    final int countValue = count;
                    database.collection("Products").document(id).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot document = task.getResult();
                                    String productName = document.get("name").toString();
                                    String productDescription = document.get("description").toString();
                                    String price = document.get("price").toString();
                                    String productID = document.get("productID").toString();
                                    String sellerID = document.get("sellerID").toString();
                                    String imgUri = document.get("ImgURI").toString();
                                    Product product = new Product(productName, price, productDescription, productID, sellerID, imgUri);
                                    userProducts.add(countValue, product);
                                    recyclerViewAdapter = new RecyclerViewAdapter(userProducts, context, transaction);
                                    rv.setAdapter(recyclerViewAdapter);
                                }
                            });
                    count++;
                }
            }
        });
        recyclerViewAdapter = new RecyclerViewAdapter(userProducts, context, transaction);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(recyclerViewAdapter);
        return v;
    }
}