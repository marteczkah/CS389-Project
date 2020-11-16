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

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
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
    Button addFavorite, messageSeller, edit, productSold;
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
        database = FirebaseFirestore.getInstance();
        product_image = (ImageView) v.findViewById(R.id.product_details_image);
        flagProductToggle = (ToggleButton) v.findViewById((R.id.toggleButton));
        //text views
        product_name = (TextView) v.findViewById(R.id.product_details_name);
        product_description = (TextView) v.findViewById(R.id.product_details_description);
        product_price = (TextView) v.findViewById(R.id.product_details_price);
        //buttons
        addFavorite = (Button) v.findViewById(R.id.add_favorite);
        messageSeller = (Button) v.findViewById(R.id.message_seller);
        edit = (Button) v.findViewById(R.id.edit_product);
        productSold = (Button) v.findViewById(R.id.product_sold);
        //setting values
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

        //changing visibility of buttons based on whether user is the seller of product or not
        if (userID.equals(sellerID)) {
            edit.setVisibility(v.VISIBLE);
            productSold.setVisibility(v.VISIBLE);
        } else {
            addFavorite.setVisibility(v.VISIBLE);
            messageSeller.setVisibility(v.VISIBLE);
        }

        //adding/removing product from favorites
        addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DocumentReference docRef = database.collection("Users")
                        .document(userID);
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

        //changing text of favorite button if product in favorites
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
                    editor.putBoolean("tgpref",false);
                    docRef.update("flags",FieldValue.increment(-1));
                    editor.commit();

                }


            }

        //open edit product page
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProduct ep = new EditProduct(name, description, price, productID);
                FragmentManager transaction = getFragmentManager();
                FragmentTransaction fragmentTransaction = transaction.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, ep);
                fragmentTransaction.commit();
            }
        });

        //set product as sold, delete from the current products database
        productSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentTime = Calendar.getInstance().getTime();
                Map<String, Object> soldProduct = new HashMap<>();
                soldProduct.put("name", name);
                soldProduct.put("description", description);
                soldProduct.put("price", price);
                soldProduct.put("date", currentTime);
                database = FirebaseFirestore.getInstance();
                String id = database.collection("SoldProducts").document().getId();
                database.collection("SoldProducts").document(id).set(soldProduct)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity().getBaseContext(),
                                        "Product Marked Sold", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity().getBaseContext(),
                                        "Couldn't mark product as sold", Toast.LENGTH_LONG)
                                        .show();
                                return;
                            }
                        });
                database.collection("Products").document(productID).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity().getBaseContext(),
                                        "Product deleted from the database", Toast.LENGTH_LONG)
                                        .show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity().getBaseContext(),
                                        "Couldn't delete the product from database",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        return v;
    }

}