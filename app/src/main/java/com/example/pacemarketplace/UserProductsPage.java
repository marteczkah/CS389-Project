package com.example.pacemarketplace;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserProductsPage extends Fragment {

    FirebaseFirestore database = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth;
    RecyclerViewAdapter recyclerViewAdapter;
    RecyclerView rv;
    FragmentManager transaction;
    RelativeLayout loadingProducts, noProducts;
    Button addProduct;

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
        loadingProducts = v.findViewById(R.id.yp_page_loading);
        noProducts = v.findViewById(R.id.no_added_products);
        addProduct = v.findViewById(R.id.add_product_button);

        final DocumentReference docRef = database.collection("Users").document(userID);
        transaction = getFragmentManager();
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProductPage ap = new AddProductPage();
                FragmentManager transaction = getFragmentManager();
                FragmentTransaction fragmentTransaction = transaction.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, ap);
                fragmentTransaction.commit();
            }
        });
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                List<String> favoritesID = (List<String>) document.get("userProducts");
                if (favoritesID.size() == 0) {
                    loadingProducts.setVisibility(v.GONE);
                    noProducts.setVisibility(v.VISIBLE);
                } else {
                    noProducts.setVisibility(v.GONE);
                }
                for (final String id : favoritesID) {
                    database.collection("Products").document(id).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        String productName = document.get("name").toString();
                                        String productDescription = document.get("description").toString();
                                        String price = document.get("price").toString();
                                        String productID = document.get("productID").toString();
                                        String sellerID = document.get("sellerID").toString();
                                        String imgUri = document.get("ImgURI").toString();
                                        Boolean pNegotiation = (Boolean) document.get("pNegotiation");
                                        Product product = new Product(productName, price, productDescription,
                                                productID, sellerID, imgUri, pNegotiation);
                                        userProducts.add(product);
                                        loadingProducts.setVisibility(v.GONE);
                                        recyclerViewAdapter = new RecyclerViewAdapter(userProducts, context, transaction);
                                        rv.setAdapter(recyclerViewAdapter);
                                    } else {
                                        docRef.update("userProducts", FieldValue.arrayRemove(id));
                                    }
                                }
                            });
                }
            }
        });
        loadingProducts.setVisibility(v.VISIBLE);
        recyclerViewAdapter = new RecyclerViewAdapter(userProducts, context, transaction);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(recyclerViewAdapter);
        return v;
    }
}