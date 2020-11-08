package com.example.pacemarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private Button addProductPageButton;
    private Button favoritesPageButton;
    private Button accountSettingsButton;
    private Button yourProductsPageButton;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference noteRef = database.collection("Products");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Nav Bar for main activity
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this,R.id.fragment);

        //Need App bar configuration code, to change bar at the top title and such
        // Need to change compile Options and upgrade to javaVersion of 1.8
        //To Customize name of these at the top go to my_nav.xml and change the andorid:label
        NavigationUI.setupWithNavController(bottomNavigationView,navController);


        Profile profilePage = new Profile();
        FragmentManager fm = getSupportFragmentManager();
    }
}