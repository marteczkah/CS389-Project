package com.example.pacemarketplace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashSet;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this,R.id.fragment);

        //Need App bar configuration code, to change bar at the top title and such
        // Need to change compile Options and upgrade to javaVersion of 1.8
        //To Customize name of these at the top go to my_nav.xml and change the andorid:label
        NavigationUI.setupWithNavController(bottomNavigationView,navController);
    /*
        Set<Integer> topLevelDestinations = new HashSet<>();
        topLevelDestinations.add(R.id.search);
        topLevelDestinations.add(R.id.messages);
        topLevelDestinations.add(R.id.profile);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations).build();
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);

    */
    }

}