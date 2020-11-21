package com.example.pacemarketplace;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteProductsPage extends Fragment {

    FirebaseFirestore database = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth;
    RecyclerViewAdapter recyclerViewAdapter;
    RecyclerView rv;
    FragmentManager transaction;

    public FavoriteProductsPage() {
        //required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.favorite_products_page, container, false);
        final ArrayList<Product> favoriteProducts = new ArrayList<>();
        final Context context = getContext();
        fAuth = FirebaseAuth.getInstance();
        String userID = fAuth.getCurrentUser().getUid();
        rv = v.findViewById(R.id.recycler_view_favorite);
        final DocumentReference docRef = database.collection("Users").document(userID);
        transaction = getFragmentManager();
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                List<String> favoritesID = (List<String>) document.get("favorites");
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
                                        favoriteProducts.add(product);
                                        recyclerViewAdapter = new RecyclerViewAdapter(favoriteProducts, context, transaction);
                                        rv.setAdapter(recyclerViewAdapter);
                                    } else {
                                        docRef.update("favorites", FieldValue.arrayRemove(id));
                                    }
                                }
                            });
                }
            }
        });
        recyclerViewAdapter = new RecyclerViewAdapter(favoriteProducts, context, transaction);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(recyclerViewAdapter);
        return v;
    }
}
