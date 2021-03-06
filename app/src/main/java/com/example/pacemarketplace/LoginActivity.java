package com.example.pacemarketplace;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText loginEmail,loginPassword;
    Button LoginButton, resendEmail;
    TextView mCreateBtn,forgotTextLink, needVerification;
    ProgressBar progressBar;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        loginEmail = findViewById(R.id.LoginEmail);
        loginPassword = findViewById(R.id.LoginPassword);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        LoginButton = findViewById(R.id.LoginButton);
        mCreateBtn = findViewById(R.id.createText);
        forgotTextLink = findViewById(R.id.forgotPassword);
        needVerification = (TextView) findViewById(R.id.need_verification_text);
        resendEmail = (Button) findViewById(R.id.resend_verification_button);

        resendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = fAuth.getCurrentUser();
                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Snackbar.make(resendEmail, "Verification email sent.", Snackbar.LENGTH_SHORT)
                                    .show();
                        } else {
                            Snackbar.make(resendEmail, "Failed to send verification email.", Snackbar.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
            }
        });


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    loginEmail.requestFocus();
                    loginEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    loginPassword.requestFocus();
                    loginPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    loginPassword.requestFocus();
                    loginPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // authenticate the user

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = fAuth.getCurrentUser();
                            if (user.isEmailVerified()) {
                                Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class).putExtra("activity", "login");
                                startActivity(intent);
                            } else {
                                needVerification.setVisibility(View.VISIBLE);
                                resendEmail.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        }else {
                            Toast.makeText(LoginActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });

            }
        });

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class).putExtra("activity", "login");
                startActivity(intent);
            }
        });

        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                resetMail.setHint("Email");
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        if (mail.length() != 0) {
                            fAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Snackbar.make(LoginButton, "Reset Link Sent To Your Email.",
                                                Snackbar.LENGTH_SHORT).show();
                                    } else {
                                        Snackbar.make(LoginButton, "Error ! Reset Link is Not Sent",
                                                Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Snackbar.make(LoginButton, "You need to enter your email!", Snackbar.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();

            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        String previous = intent.getStringExtra("activity");
        if (previous.equals("main")) {
            //users can't use the back button if they just logged out from the app
        } else {
            super.onBackPressed();
        }
    }
}
