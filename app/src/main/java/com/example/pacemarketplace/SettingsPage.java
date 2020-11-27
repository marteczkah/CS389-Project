package com.example.pacemarketplace;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsPage extends Fragment {

    Button confirmPasswordChange, showLayout;
    EditText curentPassword, newPassword, confirmPassword;
    TextView dontMatch, tooShort;
    LinearLayout showPasswordChange;
    FirebaseAuth firebaseAuth;
    TextInputLayout newPasswordL, confirmPasswordL, currentPasswordL;

    public SettingsPage() {
        //required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        //button
        confirmPasswordChange = (Button) v.findViewById(R.id.button_change_password);
        showLayout = (Button) v.findViewById(R.id.change_password_open);
        //edit texts
        curentPassword = v.findViewById(R.id.current_password);
        newPassword = v.findViewById(R.id.new_password);
        confirmPassword = v.findViewById(R.id.new_password_confirm);
        //text views
        dontMatch = v.findViewById(R.id.passwords_dont_match);
        tooShort = v.findViewById(R.id.password_too_short);
        //linear layout
        showPasswordChange = v.findViewById(R.id.layout_change_password);
        firebaseAuth = FirebaseAuth.getInstance();
        //text input layout
        currentPasswordL = (TextInputLayout) v.findViewById(R.id.layout_current_password);
        newPasswordL = (TextInputLayout) v.findViewById(R.id.layout_new_password);
        confirmPasswordL = (TextInputLayout) v.findViewById(R.id.layout_confirm_password);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        showLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPasswordChange.setVisibility(v.VISIBLE);
            }
        });

        confirmPasswordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                String newP = newPassword.getText().toString();
                String confirmNewP = confirmPassword.getText().toString();
                tooShort.setVisibility(v.GONE);
                dontMatch.setVisibility(v.GONE);
                String email = user.getEmail();

                if (curentPassword.getText().toString().length() != 0) {
                    if (currentPasswordL.isErrorEnabled()) {
                        currentPasswordL.setError(null);
                    }
                    if (newPasswordL.isErrorEnabled()) {
                        newPasswordL.setError(null);
                    }
                    if (confirmPasswordL.isErrorEnabled()) {
                        confirmPasswordL.setError(null);
                    }
                    String currentP = curentPassword.getText().toString();
                    AuthCredential credential = EmailAuthProvider.getCredential(email, currentP);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (newP.equals(confirmNewP) && newP.length() > 5 ) {
                                    user.updatePassword(newP).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Snackbar.make(showPasswordChange, "Password changed",
                                                        Snackbar.LENGTH_SHORT).show();
                                                curentPassword.setText("");
                                                newPassword.setText("");
                                                confirmPassword.setText("");
                                                curentPassword.clearFocus();
                                                newPassword.clearFocus();
                                                confirmPassword.clearFocus();
                                            } else {
                                                Snackbar.make(showPasswordChange, "Couldn't change password",
                                                        Snackbar.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else if (newP.length() <= 5) {
                                    newPasswordL.setError("Password has to be more than 5 signs!");
                                } else if (!newP.equals(confirmNewP)) {
                                    confirmPasswordL.setError("Passwords don't match!");
                                }
                            } else {
                                Snackbar.make(showPasswordChange, "Couldn't authenticate user.",
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    currentPasswordL.setError("You need to enter current password!");
                    return;
                }
            }
        });

        return v;
    }
}

//package com.example.pacemarketplace;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.bumptech.glide.Glide;
//import com.firebase.ui.firestore.FirestoreRecyclerOptions;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.example.pacemarketplace.R;
//import com.example.pacemarketplace.Profile;
//import com.google.firebase.firestore.Query;
//
//import java.util.Objects;
//
//import static java.util.Objects.requireNonNull;
//
//public class SettingsPage extends Fragment {
//
//    private FirebaseUser firebaseUser;
//    private FirebaseFirestore firestore;
//
//    public SettingsPage() {
//        //empty constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View v = inflater.inflate(R.layout.account_settings_page, container, false);
//        return v;
//    }
//
//}
