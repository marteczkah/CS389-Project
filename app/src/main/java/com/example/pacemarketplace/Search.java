package com.example.pacemarketplace;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.DragEvent;
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
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class Search extends Fragment {

    FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference noteRef = database.collection("Products");
    FragmentManager transaction;
    RecyclerViewAdapter recyclerViewAdapter;
    RecyclerView rv;
    Button categories, applyPriceFilter, removeFilters;
    RelativeLayout loadingProducts, noProductsFound;
    RangeSlider priceSlider;
    TextView minPriceTV, maxPriceTV;
    ArrayList<Product> productsToShow = new ArrayList<>();
    Float minValue = 0.0f, maxValue=1000.0f;

    public TextView productTitle;


    public Search() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
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
        //buttons
        categories = (Button) rootView.findViewById(R.id.filter_categories);
        applyPriceFilter = (Button) rootView.findViewById(R.id.apply_price);
        removeFilters = (Button) rootView.findViewById(R.id.button_remove_filters);
        //relative layouts
        noProductsFound = (RelativeLayout) rootView.findViewById(R.id.search_page_no);
        loadingProducts = (RelativeLayout) rootView.findViewById(R.id.search_page_loading);
        //range slider
        priceSlider = (RangeSlider) rootView.findViewById(R.id.price_slider);
        priceSlider.setValues(minValue, maxValue);
        //text views
        minPriceTV = (TextView) rootView.findViewById(R.id.min_price_tv);
        maxPriceTV = (TextView) rootView.findViewById(R.id.max_price_tv);

        SearchView searchView = rootView.findViewById(R.id.search_menu);

        List<Float> sliderValues = priceSlider.getValues();



        removeFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priceSlider.setValues(minValue, maxValue);
                DecimalFormat currencyFormat = new DecimalFormat("$0.00");
                currencyFormat.setCurrency(Currency.getInstance("USD"));
                minPriceTV.setText(currencyFormat.format(minValue));
                maxPriceTV.setText(currencyFormat.format(maxValue));
                noProductsFound.setVisibility(v.GONE);
                recyclerViewAdapter = new RecyclerViewAdapter(allProducts, context, transaction);
                rv.setAdapter(recyclerViewAdapter);
            }
        });

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

        //setting the format of the slider values
        priceSlider.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                DecimalFormat currencyFormat = new DecimalFormat("$0.00");
                return currencyFormat.format(value);
            }
        });

        priceSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                List<Float> sliderValues = priceSlider.getValues();
                Float maxV = sliderValues.get(1);
                Float minV = sliderValues.get(0);
                DecimalFormat currencyFormat = new DecimalFormat("$0.00");
                minPriceTV.setText(currencyFormat.format(minV));
                maxPriceTV.setText(currencyFormat.format(maxV));
            }
        });

        database.collection("MaxPrice").document("MzJTGdWzHoZ78wa76lnz").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        String max = document.get("Max").toString();
                        Float newValue = Float.parseFloat(max);
                        if (newValue > maxValue) {
                            maxValue = newValue;
                            priceSlider.setValueTo(newValue);
                            priceSlider.setValues(0.0f, newValue);
                            maxPriceTV.setText("$" + newValue);
                        }
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
                    productsToShow = allProducts;
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
                                priceSlider.setValues(minValue, maxValue);
                                DecimalFormat currencyFormat = new DecimalFormat("$0.00");
                                minPriceTV.setText(currencyFormat.format(minValue));
                                maxPriceTV.setText(currencyFormat.format(maxValue));
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
                                priceSlider.setValues(minValue, maxValue);
                                DecimalFormat currencyFormat = new DecimalFormat("$0.00");
                                minPriceTV.setText(currencyFormat.format(minValue));
                                maxPriceTV.setText(currencyFormat.format(maxValue));
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