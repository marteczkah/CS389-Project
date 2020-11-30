package com.example.pacemarketplace;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;

public class Profile extends Fragment {

    private Button addProductPageButton;
    private Button favoritesPageButton;
    private Button accountSettingsButton;
    private Button yourProductsPageButton;
    private Button userLogOut;
    private TextView helloName;
    FirebaseAuth fAuth;
    FirebaseFirestore database;

    Context mContext = getContext();


    public Profile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.account_page, container, false);
        addProductPageButton = (Button) rootView.findViewById(R.id.addProductButton);
        favoritesPageButton = (Button) rootView.findViewById(R.id.yourFavoritesButton);
        accountSettingsButton = (Button) rootView.findViewById(R.id.settingsPageButton);
        yourProductsPageButton = (Button) rootView.findViewById(R.id.yourProductsButton);
        userLogOut = (Button) rootView.findViewById(R.id.logout);
        helloName = (TextView) rootView.findViewById(R.id.hello_name);
        fAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        String userID = fAuth.getUid();

        database.collection("Users").document(userID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String userName = document.get("fName").toString();
                            helloName.setText("Hello " + userName + "!");
                        }
                    }
                });

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

        userLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

    public void openAddProductPage() {
        AddProductPage s = new AddProductPage();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, s);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void openFavoritesPage() {
        FavoriteProductsPage s = new FavoriteProductsPage();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, s);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void openAccountSettingPage() {
        SettingsPage s = new SettingsPage();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, s);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void openYourProductsPage() {
        UserProductsPage s = new UserProductsPage();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, s);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();//logout
        mContext = getContext();
        startActivity(new Intent(mContext, LoginActivity.class));
    }
}