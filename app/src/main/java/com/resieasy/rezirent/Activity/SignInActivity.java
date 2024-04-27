package com.resieasy.rezirent.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.resieasy.rezirent.Class.UsersClass;
import com.resieasy.rezirent.databinding.ActivitySignInBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {
    ActivitySignInBinding binding;
    FirebaseAuth auth,mAuth;
    int RC_SIGN_IN = 100;
    String personName, personEmail,personalNumber;
    Uri personPhoto;
    String image1;

    ProgressDialog progressDialog,progressDialog2,progressDialog3;

    GoogleSignInClient googleSignInClient;
    StorageReference sreference;
    FirebaseStorage storage;
    FirebaseDatabase database;
    DatabaseReference dreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        database=FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("Please wait, we are creating your account for ResiEasy .....");
        progressDialog.setCancelable(false);
        progressDialog2=new ProgressDialog(this);
        progressDialog2.setTitle("Fetching Your Account ....." );
        progressDialog2.setCancelable(false);
        progressDialog3=new ProgressDialog(this);
        progressDialog3.setTitle("Please wait ....." );
        progressDialog3.setCancelable(false);

        image1="https://upload.wikimedia.org/wikipedia/commons/7/7c/Profile_avatar_placeholder_large.png";




        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("825775877561-fhd25aj13btnph23ojcvmf2gipgimtg7.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(SignInActivity.this, googleSignInOptions);


        binding.googlesigninbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog2.show();
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 123);


            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123) {
            progressDialog2.dismiss();

            progressDialog.show();

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                progressDialog2.dismiss();
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(SignInActivity.this);
                            if (acct != null) {
                                personName = user.getDisplayName();
                                personEmail = user.getEmail();
                                personalNumber = user.getPhoneNumber();
                                personPhoto = acct.getPhotoUrl();


                                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull Task<String> task) {
                                        String tokenn=task.getResult();

                                        FirebaseFirestore.getInstance().collection("AllUser").document(FirebaseAuth.getInstance().getUid())
                                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        DocumentSnapshot document=task.getResult();
                                                        if (document.exists()){
                                                            HashMap<String,Object> hashMap=new HashMap<>();
                                                            hashMap.put("token",tokenn);
                                                            FirebaseFirestore.getInstance().collection("AllUser").document(FirebaseAuth.getInstance().getUid())
                                                                    .update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {

                                                                            Intent intent=new Intent(SignInActivity.this, MainActivity.class);
                                                                            progressDialog.dismiss();
                                                                            Toast.makeText(SignInActivity.this, "Sign-In Successfully, Welcome To ResiEasy", Toast.LENGTH_LONG).show();
                                                                            startActivity(intent);
                                                                            finishAffinity();
                                                                        }
                                                                    });
                                                        }else {
                                                            UsersClass userClass=new UsersClass("",personEmail,"","Nanded",tokenn,FirebaseAuth.getInstance().getUid(),"","",7028);

                                                            FirebaseFirestore.getInstance().collection("AllUser").document(FirebaseAuth.getInstance().getUid())
                                                                    .set(userClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {

                                                                            progressDialog.dismiss();
                                                                            Toast.makeText(SignInActivity.this, "Your Account Is Created", Toast.LENGTH_LONG).show();
                                                                            Toast.makeText(SignInActivity.this, "Welcome To ResiEasy", Toast.LENGTH_LONG).show();

                                                                            Intent intent=new Intent(SignInActivity.this, GetNameNumberActivity.class);
                                                                            startActivity(intent);
                                                                            finishAffinity();

                                                                        }
                                                                    });
                                                        }
                                                    }
                                                });


                                    }
                                });


                            }
                        } else {
                            progressDialog.dismiss();

                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
}


