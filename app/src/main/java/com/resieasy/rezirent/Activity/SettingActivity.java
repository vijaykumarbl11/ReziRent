package com.resieasy.rezirent.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.resieasy.rezirent.R;
import com.resieasy.rezirent.databinding.ActivitySettingBinding;

import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);


        binding.adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError);
                binding.adView.loadAd(adRequest);
            }
        });


        FirebaseAuth auth=FirebaseAuth.getInstance();


        binding.contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingActivity.this, SettingShowActivity.class);
                intent.putExtra("type","contactus");
                startActivity(intent);
            }
        });  binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              finish();
            }
        });
        binding.moreapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=ResiEasy-+Buy/Rent/Sell+Residency")));



            }
        });
        binding.aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingActivity.this, SettingShowActivity.class);
                intent.putExtra("type","aboutus");
                startActivity(intent);
            }
        });binding.privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingActivity.this, SettingShowActivity.class);
                intent.putExtra("type","pp");
                startActivity(intent);
            }
        });binding.termandcondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingActivity.this, SettingShowActivity.class);
                intent.putExtra("type","tc");
                startActivity(intent);
            }
        });binding.shareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent=new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT,"ResiEasy");
                    String applink="https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName();
                    intent.putExtra(Intent.EXTRA_TEXT,applink);
                    startActivity(Intent.createChooser(intent,"Share ResiEasy Application"));

                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(SettingActivity.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });binding.rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(SettingActivity.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });



        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SettingActivity.this);
                builder.setIcon(R.drawable.warna);
                builder.setTitle("LOGOUT");
                builder.setMessage("you are sure, you want to logout your account.");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GoogleSignInOptions gso = new GoogleSignInOptions.
                                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                                build();

                        GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(SettingActivity.this,gso);
                        googleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                HashMap<String,Object> hashMap=new HashMap<>();
                                hashMap.put("token","");
                                FirebaseFirestore.getInstance().collection("AllUser").document(FirebaseAuth.getInstance().getUid())
                                                .update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    auth.signOut();
                                                    Intent intent = new Intent(SettingActivity.this, SignInActivity.class);
                                                    startActivity(intent);
                                                    finishAffinity();
                                                    Toast.makeText(SettingActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }
                        });

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNeutralButton("Help", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SettingActivity.this, "for logout, press yes", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();


            }
        });
    }
}