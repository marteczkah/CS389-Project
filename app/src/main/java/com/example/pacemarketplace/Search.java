package com.example.pacemarketplace;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Search extends Fragment {

    FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference noteRef = database.collection("Products");
    FragmentManager transaction;
    RecyclerViewAdapter recyclerViewAdapter;
    RecyclerView rv;
    Dialog filterDialog;
    Button categories, applyPriceFilter;
    RelativeLayout loadingProducts, noProductsFound;
    RangeSlider priceSlider;

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
        final ArrayList<Product> filteredProducts = new ArrayList<>();
        transaction = getFragmentManager();
        categories = (Button) rootView.findViewById(R.id.filter_categories);
        applyPriceFilter = (Button) rootView.findViewById(R.id.apply_price);
        //relative layouts
        noProductsFound = (RelativeLayout) rootView.findViewById(R.id.search_page_no);
        loadingProducts = (RelativeLayout) rootView.findViewById(R.id.search_page_loading);
        //range slider
        priceSlider = (RangeSlider) rootView.findViewById(R.id.price_slider);

        SearchView searchView = rootView.findViewById(R.id.search_menu);

        List<Float> sliderValues = priceSlider.getValues();

        applyPriceFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Float> sliderValues = priceSlider.getValues();
                Float maxValue = sliderValues.get(1);
                Float minValue = sliderValues.get(0);
                ArrayList<Product> filterProducts = new ArrayList<>();
                for (int i = 0; i < allProducts.size(); i++) {
                    String productPrice = allProducts.get(i).getProductPrice();
                    Float priceNumber = Float.parseFloat(productPrice);
                    if (priceNumber < maxValue && priceNumber > minValue) {
                        filterProducts.add(allProducts.get(i));
                    }
                }
                if (filterProducts.size() == 0) {
                    noProductsFound.setVisibility(v.VISIBLE);
                } else {
                    noProductsFound.setVisibility(v.GONE);
                }
                recyclerViewAdapter = new RecyclerViewAdapter(filterProducts, context, transaction);
                rv.setAdapter(recyclerViewAdapter);
            }
        });

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
                    Boolean pNegotiation = (Boolean) document.get("pNegotiation");
                    String category = (String) document.get("category");
                    Product product = new Product(productName, price, productDescription, productID,
                            sellerID, getImgURI, pNegotiation, category); //getImgURI
                    loadingProducts.setVisibility(rootView.GONE);
                    allProducts.add(product);
                    recyclerViewAdapter = new RecyclerViewAdapter(allProducts, context, transaction);
                    rv.setAdapter(recyclerViewAdapter);
                }
            }
        });

        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] valuesToPick = new String[] {"All", "Textbooks", "Electronics", "Dorm Supplies",
                    "Clothes", "School Supplies", "Movies, Music & Games", "Beauty & Health",
                    "Other"};
                List<String> checkedValues = new ArrayList<>();
                ArrayList<Product> emptyList = new ArrayList<>();
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Filter by category")
                        .setPositiveButton("Apply filter", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (checkedValues.size() == 1 && checkedValues.get(0).equals("All")) {
                                    recyclerViewAdapter = new RecyclerViewAdapter(allProducts, context, transaction);
                                    rv.setAdapter(recyclerViewAdapter);
                                    return;
                                }
                                for (int i = 0; i < allProducts.size(); i++) {
                                    for (int m = 0; m < checkedValues.size(); m++) {
                                        if (allProducts.get(i).getCategory().equals(checkedValues.get(m))) {
                                            emptyList.add(allProducts.get(i));
                                        }
                                    }
                                }
                                recyclerViewAdapter = new RecyclerViewAdapter(emptyList, context, transaction);
                                rv.setAdapter(recyclerViewAdapter);
                                if (emptyList.size() == 0) {
                                    noProductsFound.setVisibility(rootView.VISIBLE);
                                } else {
                                    noProductsFound.setVisibility(rootView.GONE);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNeutralButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                recyclerViewAdapter = new RecyclerViewAdapter(allProducts, context, transaction);
                                rv.setAdapter(recyclerViewAdapter);
                            }
                        })
                        .setMultiChoiceItems(valuesToPick, null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    checkedValues.add(valuesToPick[which]);
                                }
                            }
                        })
                        .show();
            }
        });
        loadingProducts.setVisibility(rootView.VISIBLE);
        rv = rootView.findViewById(R.id.recycler_view_search);
        recyclerViewAdapter = new RecyclerViewAdapter(allProducts, context, transaction);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(recyclerViewAdapter);
        return rootView;
    }


}