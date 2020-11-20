package com.example.pacemarketplace;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
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
    Spinner categorySpinner;
    FragmentManager fragmentManager;
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
        categorySpinner = (Spinner) v.findViewById(R.id.categories_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categories_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        fragmentManager = getFragmentManager();


        fAuth = FirebaseAuth.getInstance();

        //Adding Image Stuff
        ch=(Button)v.findViewById(R.id.btnchoose);
        img=(ImageView)v.findViewById(R.id.imgview);
        mStorageRef= FirebaseStorage.getInstance().getReference("images");

        ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Filechooser();
            }
        });


        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = addName.getText().toString();
                String productPrice = addPrice.getText().toString();
                String productDescription = addDescription.getText().toString();
                String category = categorySpinner.getSelectedItem().toString();
                String fileURI = Fileuploader();


                final String userID = fAuth.getCurrentUser().getUid();
                final String id = database.collection("Products").document().getId();
                Map<String,Object> data = new HashMap<>();
                data.put("name", productName);
                data.put("price", productPrice);
                data.put("description", productDescription);
                data.put("ImgURI",fileURI);
                data.put("flags", 0);
                data.put("sellerID", userID);
                data.put("productID", id);
                data.put("category", category);
                database.collection("Products").document(id).set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                DocumentReference docRef = database.collection("Users").document(userID);
                                docRef.update("userProducts", FieldValue.arrayUnion(id));
                                Toast.makeText(getActivity().getBaseContext(), "Product added", Toast.LENGTH_LONG).show();
                                Search sp = new Search();
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.replace(R.id.fragment, sp);
                                transaction.commit();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity().getBaseContext(), "Couldn't add the product", Toast.LENGTH_LONG).show();

                            }
                        });
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
        }
    }

}
