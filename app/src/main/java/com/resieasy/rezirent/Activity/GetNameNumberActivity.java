package com.resieasy.rezirent.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.resieasy.rezirent.databinding.ActivityGetNameNumberBinding;

public class GetNameNumberActivity extends AppCompatActivity {
    ActivityGetNameNumberBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityGetNameNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Uploading . . . .");
        progressDialog.setCancelable(false);


        binding.skipbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GetNameNumberActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        binding.userupdatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String name=binding.username.getText().toString();
                String number=binding.usernumber.getText().toString();

                FirebaseFirestore.getInstance().collection("AllUser").document(FirebaseAuth.getInstance().getUid())
                        .update("name",name,"number",number).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(GetNameNumberActivity.this, "Submited Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(GetNameNumberActivity.this,MainActivity.class);
                                progressDialog.show();
                                startActivity(intent);
                                finishAffinity();
                            }
                        });

            }
        });

    }
}