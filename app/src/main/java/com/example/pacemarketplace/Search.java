package com.example.pacemarketplace;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Search extends Fragment {

    FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference noteRef = database.collection("Products");
    FragmentManager transaction;
    RecyclerViewAdapter recyclerViewAdapter;
    RecyclerView rv;

    public TextView productTitle;


    public Search() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.search_page, container, false);
        this.setHasOptionsMenu(true);
        final Context context = getContext();
        final ArrayList<Product> allProducts = new ArrayList<>();
        transaction = getFragmentManager();
        SearchView searchView = rootView.findViewById(R.id.search_menu);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recyclerViewAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerViewAdapter.getFilter().filter(newText);
                return false;
            }
        });
        database.collection("Products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot documents = task.getResult();
                List<DocumentSnapshot> readDocuments = documents.getDocuments();
                for (int i = 0; i < readDocuments.size(); i++) {
                    DocumentSnapshot document = readDocuments.get(i);
                    String productName = document.get("name").toString();
                    String productDescription = document.get("description").toString();
                    String price = document.get("price").toString();
                    String productID = document.get("productID").toString();
                    String sellerID = document.get("sellerID").toString();
                    String getImgURI = document.get("ImgURI").toString();
                    Product product = new Product(productName, price, productDescription, productID, sellerID, getImgURI);
                    allProducts.add(product);
                    recyclerViewAdapter = new RecyclerViewAdapter(allProducts, context, transaction);
                    rv.setAdapter(recyclerViewAdapter);
                }
            }
        });
        rv = rootView.findViewById(R.id.recycler_view_search);
        recyclerViewAdapter = new RecyclerViewAdapter(allProducts, context, transaction);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(recyclerViewAdapter);
        return rootView;
    }
}