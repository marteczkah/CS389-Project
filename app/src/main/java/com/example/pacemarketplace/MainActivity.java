package com.example.pacemarketplace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class MainActivity extends AppCompatActivity {

    private Button addProductPageButton;
    private Button favoritesPageButton;
    private Button accountSettingsButton;
    private Button yourProductsPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_page);

        addProductPageButton = (Button) findViewById(R.id.addProductButton);
        favoritesPageButton = (Button) findViewById(R.id.yourFavoritesButton);
        accountSettingsButton = (Button) findViewById(R.id.settingsPageButton);
        yourProductsPageButton = (Button) findViewById(R.id.yourProductsButton);

        addProductPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openAddProductPage();
            }
        });

        favoritesPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openFavoriteProductsPage();
            }
        });

        accountSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openAccountSettingsPage();
            }
        });

        yourProductsPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openYourProductsPage();
            }
        });
    }

    public void openAddProductPage() {
        setContentView(R.layout.add_product_page);
    }

    public void openFavoriteProductsPage() {
        setContentView(R.layout.favorite_products_page);
    }

    public void openAccountSettingsPage() {
        setContentView(R.layout.account_settings_page);
    }

    public void openYourProductsPage() {
        setContentView(R.layout.your_products_page);
    }

}