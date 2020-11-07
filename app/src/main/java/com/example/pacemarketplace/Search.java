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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Search extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference noteRef = database.collection("Products");
    FragmentManager transaction;
    RecyclerViewAdapter recyclerViewAdapter;
    RecyclerView rv;

    private DisplayProducts displayProducts;
    public TextView productTitle;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Search() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Search.
     */
    // TODO: Rename and change types and number of parameters
    public static Search newInstance(String param1, String param2) {
        Search fragment = new Search();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.search_page, container, false);
        final Context context = getContext();
        final ArrayList<Product> allProducts = new ArrayList<>();
        transaction = getFragmentManager();
//        Query query = database.collection("Products").orderBy("name", Query.Direction.DESCENDING);
////        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>().setQuery(query,Product.class).build();
////        transaction = getFragmentManager();
////        displayProducts = new DisplayProducts(options, context, transaction);
////        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_search);
////        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
////        recyclerView.setAdapter(displayProducts);
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
                    Product product = new Product(productName, price, productDescription, productID, sellerID);
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