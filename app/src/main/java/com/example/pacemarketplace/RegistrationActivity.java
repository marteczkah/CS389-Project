package com.example.pacemarketplace;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText fName, lName,RegisterEmail,RegisterPassword;
    Button registerButton;
    TextView loginButton;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        fName   = findViewById(R.id.firstName);
        lName = findViewById(R.id.lastName);
        RegisterEmail      = findViewById(R.id.registration_email);
        RegisterPassword   = findViewById(R.id.password);
        registerButton = findViewById(R.id.NewMemberButton);
        loginButton   = findViewById(R.id.createText);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = RegisterEmail.getText().toString().trim();
                String password = RegisterPassword.getText().toString().trim();
                final String firstName = fName.getText().toString();
                final String lastName    = lName.getText().toString();

                if (TextUtils.isEmpty(firstName)){
                    fName.requestFocus();
                    fName.setError("First Name is required.");
                    return;
                }

                if (TextUtils.isEmpty(lastName)) {
                    lName.requestFocus();
                    lName.setError("Last name is required.");
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    RegisterEmail.requestFocus();
                   RegisterEmail.setError("Email is Required.");
                    return;
                }

                if (!email.endsWith("@pace.edu")) {
                    RegisterEmail.requestFocus();
                    RegisterEmail.setError("You have to use Pace email to create an account.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    RegisterPassword.requestFocus();
                    RegisterPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    RegisterPassword.requestFocus();
                    RegisterPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // register the user in firebase

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            // send verification link

                            FirebaseUser fuser = fAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegistrationActivity.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                }
                            });

//                            Toast.makeText(RegistrationActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class).putExtra("activity", "registration");
                            startActivity(intent);
                            userID = fAuth.getCurrentUser().getUid();
//                            String id = fStore.collection("Users").document().getId();
                            DocumentReference documentReference = fStore.collection("Users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            List<String> favorites = new ArrayList<>();
                            List<String> userProducts = new ArrayList<>();
                            user.put("fName", firstName);
                            user.put("email", email);
                            user.put("lName", lastName);
                            user.put("favorites", favorites);
                            user.put("userProducts", userProducts);
                            user.put("userID", userID);
                            fStore.collection("Users").document(userID).set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(RegistrationActivity.this, "account added to database", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegistrationActivity.this, "account not added to database", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "onFailure: " + e.toString());
                                        }
                                    });
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class).putExtra("activity", "registration");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = fAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){
            if (currentUser.isEmailVerified()) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class).putExtra("activity", "registration");
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class).putExtra("activity", "registration");
                startActivity(intent);
            }
        }
    }
}
