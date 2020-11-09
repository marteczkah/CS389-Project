package com.example.pacemarketplace;

import java.io.InputStream;
import java.net.URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.drawable.Drawable;
import androidx.fragment.app.Fragment;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ComputableLiveData;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.prefs.Preferences;

import static android.content.Context.MODE_PRIVATE;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class ProductDetails extends Fragment {

    String name, description, price, productID, sellerID, imgUrl;
    TextView product_name, product_description, product_price;
    Button addFavorite;
    ToggleButton flagProductToggle;
    FirebaseFirestore database;
    FirebaseAuth fAuth;
    ImageView product_image;

    public ProductDetails() {
    //required empty constructor
    }

    public ProductDetails(String name, String description, String price, String productID, String sellerID, String imgUrl){
        this.name = name;
        this.description = description;
        this.price = price;
        this.productID = productID;
        this.sellerID = sellerID;
        this.imgUrl = imgUrl;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.product_details_page, container, false);
        //create objects
        product_name = (TextView) v.findViewById(R.id.product_details_name);
        product_description = (TextView) v.findViewById(R.id.product_details_description);
        product_price = (TextView) v.findViewById(R.id.product_details_price);
        product_image = (ImageView) v.findViewById(R.id.product_details_image);
        flagProductToggle = (ToggleButton) v.findViewById((R.id.toggleButton));
        //setting text
        product_name.setText(name);
        product_description.setText(description);
        product_price.setText("$"+price);

        StorageReference imgRef = FirebaseStorage
                                        .getInstance()
                                            .getReferenceFromUrl(imgUrl);
        try {
            imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url = uri.toString();
                    Picasso.get().load(url).fit().into(product_image);
                }
            }).wait(1000);
        }catch (Exception e){

        }
        database = FirebaseFirestore.getInstance();
        addFavorite = (Button) v.findViewById(R.id.add_favorite);
        fAuth = FirebaseAuth.getInstance();
        final String userID = fAuth.getCurrentUser().getUid();
        addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DocumentReference docRef = database.collection("Users").document(userID);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        List<String> favoritesList = (List<String>) document.get("favorites");
                        if (favoritesList.contains(productID)) {
                            docRef.update("favorites", FieldValue.arrayRemove(productID));
                            addFavorite.setText("Add to Favorites");
                        } else {
                            docRef.update("favorites", FieldValue.arrayUnion(productID));
                            addFavorite.setText("Remove from Favorites");
                        }
                    }
                });
            }
        });
        database.collection("Users").document(userID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        List<String> favorites = (List<String>) document.get("favorites");
                        if (favorites.contains(productID)) {
                            addFavorite.setText("Remove from favorites");
                        }
                    }
                });


        SharedPreferences preferences = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        boolean tgpref = preferences.getBoolean("tgpref", true);
        if (tgpref)
            flagProductToggle.setChecked(true);
        else
            flagProductToggle.setChecked(false);


        flagProductToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final DocumentReference docRef = database.collection("Products").document(productID);
                if (isChecked) {
                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("tgpref",true);
                    docRef.update("flags",FieldValue.increment(1));
                    editor.commit();

                } else {
                    SharedPreferences.Editor editor = preferences.edit();
                    docRef.update("flags",FieldValue.increment(-1));
                    editor.commit();

                }


            }

        });

        return v;
    }

}