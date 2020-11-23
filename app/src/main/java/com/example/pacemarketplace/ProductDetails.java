package com.example.pacemarketplace;

import java.io.InputStream;
import java.net.URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.drawable.Drawable;
import androidx.fragment.app.Fragment;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
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
    TextView product_name, product_description, product_price, price_negotiation;
    Button messageSeller, edit, productSold;
    ImageButton addFavorite;
    ToggleButton flagProductToggle;
    FirebaseFirestore database;
    FirebaseAuth fAuth;
    ImageView product_image;
    Boolean pNegotiation;
    LinearLayout linearLayout;

    public ProductDetails() {
    //required empty constructor
    }

    public ProductDetails(String name, String description, String price, String productID,
                          String sellerID, String imgUrl, Boolean pNegotiation){
        this.name = name;
        this.description = description;
        this.price = price;
        this.productID = productID;
        this.sellerID = sellerID;
        this.imgUrl = imgUrl;
        this.pNegotiation = pNegotiation;
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
        price_negotiation = (TextView) v.findViewById(R.id.product_details_negotation);
        //buttons
        addFavorite = (ImageButton) v.findViewById(R.id.add_favorite);
        messageSeller = (Button) v.findViewById(R.id.message_seller);
        edit = (Button) v.findViewById(R.id.edit_product);
        productSold = (Button) v.findViewById(R.id.product_sold);
        //setting values
        product_name.setText(name);
        product_description.setText(description);
        product_price.setText("$"+price);
        //linear layout
        linearLayout = (LinearLayout) v.findViewById(R.id.product_details_page);

        StorageReference imgRef = FirebaseStorage
                                        .getInstance()
                                            .getReferenceFromUrl(imgUrl);
        try {
            imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url = uri.toString();
//                    Picasso.get().load(url).fit().into(product_image);
                    Picasso.get().load(url).centerCrop().resize(product_image.getWidth(), product_image.getHeight())
                            .into(product_image);
                }
            }).wait(1000);
        }catch (Exception e){

        }
        database = FirebaseFirestore.getInstance();
        addFavorite = (ImageButton) v.findViewById(R.id.add_favorite);
        fAuth = FirebaseAuth.getInstance();
        final String userID = fAuth.getCurrentUser().getUid();

        //changing visibility of buttons based on whether user is the seller of the product or not
        if (userID.equals(sellerID)) {
            edit.setVisibility(v.VISIBLE);
            productSold.setVisibility(v.VISIBLE);
        } else {
            addFavorite.setVisibility(v.VISIBLE);
            messageSeller.setVisibility(v.VISIBLE);
            flagProductToggle.setVisibility(v.VISIBLE);
        }

        //setting the price open to negotiation text
        if (pNegotiation) {
            price_negotiation.setText(" - open to negotiation");
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
//                            addFavorite.setText("Add to Favorites");
                            addFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favourite_outlined_24dp));
                        } else {
                            docRef.update("favorites", FieldValue.arrayUnion(productID));
//                            addFavorite.setText("Remove from Favorites");
                            addFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favourite_filled_24dp));
                        }
                    }
                });
            }
        });

        messageSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.collection("Users").document(sellerID).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String email = document.get("email").toString();
                                    String mailTo = "mailto:" + email;
                                    Intent i = new Intent(Intent.ACTION_SEND);
                                    String subject = name + " - from Pace Marketplace";
                                    i.setType("message/rfc822");
                                    i.putExtra(Intent.EXTRA_SUBJECT, subject);
                                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
//                                    i.setData(Uri.parse(mailTo));
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    try {
                                        startActivity(Intent.createChooser(i, "Choose email..."));
                                    } catch (android.content.ActivityNotFoundException ex) {
                                        Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                                    }
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
//                            addFavorite.setText("Remove from favorites");
                            addFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favourite_filled_24dp));
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
                    editor.putBoolean("tgpref", true);
                    docRef.update("flags", FieldValue.increment(1));
                    editor.commit();

                } else {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("tgpref", false);
                    docRef.update("flags", FieldValue.increment(-1));
                    editor.commit();

                }


            }
        });

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
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Do you want to mark " + name + " as sold?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
                                                Snackbar.make(linearLayout, "Product marked sold.",
                                                        Snackbar.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(linearLayout, "Couldn't mark product as sold. Try again.",
                                                        Snackbar.LENGTH_SHORT).show();
                                                return;
                                            }
                                        });
                                database.collection("Products").document(productID).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Search sp = new Search();
                                                FragmentManager transaction = getFragmentManager();
                                                FragmentTransaction fragmentTransaction = transaction.beginTransaction();
                                                fragmentTransaction.replace(R.id.fragment, sp);
                                                fragmentTransaction.commit();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(linearLayout, "Couldn't mark product as sold. Try again.",
                                                        Snackbar.LENGTH_SHORT).show();
                                                return;
                                            }
                                        });
                            }
                        })
                        .show();
            }
        });

        return v;
    }

}