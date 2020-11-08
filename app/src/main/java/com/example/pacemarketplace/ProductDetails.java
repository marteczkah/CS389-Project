package com.example.pacemarketplace;

import java.io.InputStream;
import java.net.URL;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.drawable.Drawable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ProductDetails extends Fragment {

    String name, description, price, imgUrl;
    TextView product_name, product_description, product_price;
    ImageView product_image;

    FirebaseFirestore database = FirebaseFirestore.getInstance();
    public ProductDetails() {
    //required empty constructor
    }

    public ProductDetails(String name, String description, String price, String imgUrl){
        this.name = name;
        this.description = description;
        this.price = price;
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
        return v;
    }

}