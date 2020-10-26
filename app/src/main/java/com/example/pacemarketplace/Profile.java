package com.example.pacemarketplace;

import android.accounts.Account;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {

    private Button addProductPageButton;
    private Button favoritesPageButton;
    private Button accountSettingsButton;
    private Button yourProductsPageButton;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
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
        View rootView = inflater.inflate(R.layout.account_page, container, false);
        addProductPageButton = (Button) rootView.findViewById(R.id.addProductButton);
        favoritesPageButton = (Button) rootView.findViewById(R.id.yourFavoritesButton);
        accountSettingsButton = (Button) rootView.findViewById(R.id.settingsPageButton);
        yourProductsPageButton = (Button) rootView.findViewById(R.id.yourProductsButton);


        addProductPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddProductPage();
            }
        });

        favoritesPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFavoritesPage();
            }
        });

        accountSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccountSettingPage();
            }
        });

        yourProductsPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openYourProductsPage();
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    public void openAddProductPage() {
        AddProductPage s = new AddProductPage();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, s);
        transaction.commit();
    }

    public void openFavoritesPage() {
        FavoriteProductsPage s = new FavoriteProductsPage();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, s);
        transaction.commit();
    }

    public void openAccountSettingPage() {
        SettingsPage s = new SettingsPage();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, s);
        transaction.commit();
    }

    public void openYourProductsPage() {
        UserProductsPage s = new UserProductsPage();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, s);
        transaction.commit();
    }

//    public void addProductName(View view){
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//
//    }
}