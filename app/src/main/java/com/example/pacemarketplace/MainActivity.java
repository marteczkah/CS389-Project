package com.example.pacemarketplace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashSet;


public class MainActivity extends AppCompatActivity {

    private Button addProductPageButton;
    private Button favoritesPageButton;
    private Button accountSettingsButton;
    private Button yourProductsPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.account_page);

        setContentView(R.layout.activity_main);

        //Upload image Test
        /*
        imageView = (ImageView)findViewById(R.id.imageView);
        button = (Button)findViewById(R.id.buttonLoadPicture);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }

         */

        //Nav Bar for main activity
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this,R.id.fragment);

        //Need App bar configuration code, to change bar at the top title and such
        // Need to change compile Options and upgrade to javaVersion of 1.8
        //To Customize name of these at the top go to my_nav.xml and change the andorid:label
        NavigationUI.setupWithNavController(bottomNavigationView,navController);

        addProductPageButton = (Button) findViewById(R.id.addProductButton);
        favoritesPageButton = (Button) findViewById(R.id.yourFavoritesButton);
        accountSettingsButton = (Button) findViewById(R.id.settingsPageButton);
        yourProductsPageButton = (Button) findViewById(R.id.yourProductsButton);

        Profile profilePage = new Profile();
        FragmentManager fm = getSupportFragmentManager();
//
//        addProductPageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                openAddProductPage();
//            }
//        });
//
//        favoritesPageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                openFavoriteProductsPage();
//            }
//        });
//
//        accountSettingsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                openAccountSettingsPage();
//            }
//        });
//
//        yourProductsPageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                openYourProductsPage();
//            }
//        });
    }

//    public void openAddProductPage() {
//        setContentView(R.layout.add_product_page);
//    }
//
//    public void openFavoriteProductsPage() {
//        setContentView(R.layout.favorite_products_page);
//    }
//
//    public void openAccountSettingsPage() {
//        setContentView(R.layout.account_settings_page);
//    }
//
//    public void openYourProductsPage() {
//        setContentView(R.layout.your_products_page);
//    /*
//        Set<Integer> topLevelDestinations = new HashSet<>();
//        topLevelDestinations.add(R.id.search);
//        topLevelDestinations.add(R.id.messages);
//        topLevelDestinations.add(R.id.profile);
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations).build();
//        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);
//
//    */
//    }

}