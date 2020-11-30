package com.example.pacemarketplace;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class AddProductPage extends Fragment {

    FirebaseAuth fAuth;
    Button addProduct;
    EditText addName;
    EditText addPrice;
    EditText addDescription;
    Button ch;
    ImageView img;
    StorageReference mStorageRef;
    FragmentManager fragmentManager;
    SwitchMaterial priceNegotiation;
    TextView nameWarning, priceWarning, descriptionWarning, categoryWarning, imageWarning;
    ScrollView scrollView;
    Boolean photoAdded = false;
    public Uri imguri;

    FirebaseFirestore database = FirebaseFirestore.getInstance();

    public AddProductPage() {
        //required empty constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_product_page, container, false);
        addProduct = (Button) v.findViewById(R.id.addNameToDatabase);
        addName = (EditText) v.findViewById(R.id.ProductName);
        addPrice = (EditText) v.findViewById(R.id.editTextNumberDecimal);
        addDescription = (EditText) v.findViewById(R.id.editTextTextMultiLine);
        priceNegotiation = (SwitchMaterial) v.findViewById(R.id.price_negotiation);
        scrollView = (ScrollView) v.findViewById(R.id.add_product_view);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categories_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AutoCompleteTextView editTextFilledExposedDropdown =
                v.findViewById(R.id.filled_exposed_dropdown);
        editTextFilledExposedDropdown.setAdapter(adapter);

        fragmentManager = getFragmentManager();
        fAuth = FirebaseAuth.getInstance();

        //Adding Image Stuff
        ch=(Button)v.findViewById(R.id.btnchoose);
        img=(ImageView)v.findViewById(R.id.imgview);
        mStorageRef= FirebaseStorage.getInstance().getReference("images");

        nameWarning = (TextView) v.findViewById(R.id.product_name_warning);
        priceWarning = (TextView) v.findViewById(R.id.product_price_warning);
        descriptionWarning = (TextView) v.findViewById(R.id.product_description_warning);
        categoryWarning = (TextView) v.findViewById(R.id.product_category_warning);
        imageWarning = (TextView) v.findViewById(R.id.product_image_warning);

        ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Filechooser();
            }
        });


        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean hasName = true, hasDescription = true, hasPrice = true, hasCategory = true,
                        hasImage=true;
                String productName = addName.getText().toString();
                //checking if user added product name
                if (TextUtils.isEmpty(productName)) {
                    nameWarning.setVisibility(v.VISIBLE);
                    hasName = false;
                } else {
                    nameWarning.setVisibility(v.GONE);
                }
                String productPrice = addPrice.getText().toString();
                //checking if user added product price
                if (TextUtils.isEmpty(productPrice)) {
                    priceWarning.setVisibility(v.VISIBLE);
                    hasPrice = false;
                } else {
                    priceWarning.setVisibility(v.GONE);
                }
                String productDescription = addDescription.getText().toString();
                //checking if user added product description
                if (TextUtils.isEmpty(productDescription)) {
                    descriptionWarning.setVisibility(v.VISIBLE);
                    hasDescription = false;
                } else {
                    descriptionWarning.setVisibility(v.GONE);
                }
                String category = editTextFilledExposedDropdown.getText().toString();
                //checking if user added product category
                if (TextUtils.isEmpty(category)) {
                    categoryWarning.setVisibility(v.VISIBLE);
                    hasCategory = false;
                } else {
                    categoryWarning.setVisibility(v.GONE);
                }
                Boolean negotiation = priceNegotiation.isChecked();
                String fileURI = "";
                if (!photoAdded) {
                    imageWarning.setVisibility(v.VISIBLE);
                    hasImage = false;
                } else {
                    imageWarning.setVisibility(v.GONE);
                    fileURI = Fileuploader();
                }


                final String userID = fAuth.getCurrentUser().getUid();
                final String id = database.collection("Products").document().getId();

                if (hasCategory && hasName && hasPrice && hasDescription && hasImage) {
                    Map<String,Object> data = new HashMap<>();
                    data.put("name", productName);
                    data.put("price", productPrice);
                    data.put("description", productDescription);
                    data.put("ImgURI",fileURI);
                    data.put("flags", 0);
                    data.put("sellerID", userID);
                    data.put("productID", id);
                    data.put("category", category);
                    data.put("pNegotiation", negotiation);
                    database.collection("Products").document(id).set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    DocumentReference docRef = database.collection("Users").document(userID);
                                    docRef.update("userProducts", FieldValue.arrayUnion(id));
//                                    Toast.makeText(getActivity().getBaseContext(), "Product added", Toast.LENGTH_LONG).show();
                                    Snackbar.make(scrollView, "Product added successfully",
                                            Snackbar.LENGTH_SHORT).show();
                                    Search sp = new Search();
                                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                                    transaction.replace(R.id.fragment, sp);
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar.make(scrollView, "Couldn't add the product",
                                            Snackbar.LENGTH_SHORT).show();
                                }
                            });
                    DocumentReference docRef = database.collection("MaxPrice").document(
                            "MzJTGdWzHoZ78wa76lnz");
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful())
                            {
                                DocumentSnapshot document = task.getResult();
//                            if (document.exists())
//                                Log.d(TAG, "onComplete: data:"+document.getData());
//                            else
//                                Log.d(TAG, "onComplete: no such doc");
                                float value = Float.parseFloat(productPrice);
                                if (value > document.getLong("Max"))
                                {
                                    docRef.update("Max", value);
                                }
                            }
//                        else
//                            Log.d(TAG, "onComplete: get failed with ", task.getException());

                        }
                    });
                    DocumentReference min = database.collection("MinPrice").document("7GpvcCeHeI5904qQbuem");
                    min.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful())
                            {
                                DocumentSnapshot document = task.getResult();
//                            if (document.exists())
//                                Log.d(TAG, "onComplete: data: "+document.getData());
//                            else
//                                Log.d(TAG, "onComplete: no such doc ");
                                float value = Float.parseFloat(productPrice);
                                if (value <document.getLong("Min"))
                                {
                                    min.update("Min",value);
                                }

                            }
                        }
                    });
                }
//                Fileuploader();
            }
        });
        return v;
    } //End OnCreateView

    private String getExtension(Uri uri)
    {
        @SuppressLint("RestrictedApi") ContentResolver cr= getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypemap= MimeTypeMap.getSingleton();
        return mimeTypemap.getExtensionFromMimeType(cr.getType(uri));

    }

    private String Fileuploader()
    {
        StorageReference Ref = mStorageRef.child(System.currentTimeMillis() + "."+getExtension(imguri));
        Ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        String downloadUrl = taskSnapshot.getStorage().getDownloadUrl().toString();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
        return "gs://" + Ref.getBucket() + Ref.getPath();

    }

    private void Filechooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { //Had @Nullable before Intent data but wasn't in example so I removed it
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1 && resultCode==RESULT_OK&& data!=null && data.getData()!=null) //Check RESULT_OK if not working
        {
            imguri=data.getData();
            img.setImageURI(imguri);
            img.setVisibility(getView().VISIBLE);
            photoAdded = true;
        }
    }

}
