package com.example.pacemarketplace;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
//import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

public class LandingActivity extends AppCompatActivity {
    private static final int GALLERY_INTENT_CODE = 1023;
    TextView firstName, lastName, email, verifyMsg;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    Button resendCode, reloadPage;
    Button resetPassLocal, changeProfileImage;
    FirebaseUser user;
    ImageView profileImage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);


//        fAuth = FirebaseAuth.getInstance();
//        fStore = FirebaseFirestore.getInstance();
//        storageReference = FirebaseStorage.getInstance().getReference();
//        reloadPage = (Button) findViewById(R.id.reload_page);
//
//        resendCode = findViewById(R.id.resendCode);
//        verifyMsg = findViewById(R.id.verifyMsg);
//
//
//        userId = fAuth.getCurrentUser().getUid();
//        user = fAuth.getCurrentUser();

        if (!user.isEmailVerified()) {
            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Snackbar.make(resendCode, "Verification Email Has been Sent.", Snackbar.LENGTH_SHORT)
                                    .show();                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(resendCode, "Verification Email not sent.", Snackbar.LENGTH_SHORT)
                                    .show();
                        }
                    });
                }
            });
        }

//        reloadPage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (fAuth.getCurrentUser().isEmailVerified()) {
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                } else {
//                    Snackbar.make(resendCode, "Account not verified", Snackbar.LENGTH_SHORT)
//                            .show();
//                }
//            }
//        });

    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}
//credit to small academy and medium corporation - I followed along with their videos and articles
