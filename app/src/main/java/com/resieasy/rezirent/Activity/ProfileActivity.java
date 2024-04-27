package com.resieasy.rezirent.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.QuerySnapshot;
import com.resieasy.rezirent.Adapter.AdapterViewPager;
import com.resieasy.rezirent.R;
import com.resieasy.rezirent.Adapter.ResiShowOwnerAdapter;
import com.resieasy.rezirent.Class.SingleIDClass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.resieasy.rezirent.databinding.ActivityProfileBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {


    ActivityProfileBinding binding;
    ImageView image11;

    Uri selectedImage;

    String personName, personEmail, personalNumber;
    FirebaseAuth auth;
    String name, number, gmail, pic;
    ProgressDialog dialog;
    InterstitialAd mInterstitialAd;

    ProgressDialog ad_dialog;

    String[] courses = {"On Rent", "For Sell"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ad_dialog=new ProgressDialog(this);
        ad_dialog.setMessage("Ad loading");
        ad_dialog.setCancelable(false);

        //admob
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, String.valueOf(R.string.Profileleadbuttoninterstitial_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;

                    }
                });

        binding.leadbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mInterstitialAd != null) {
                    ad_dialog.show();
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ad_dialog.dismiss();
                            mInterstitialAd.show(ProfileActivity.this);
                            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    mInterstitialAd=null;
                                    Intent intent2=new Intent(ProfileActivity.this,LeadShowActivity.class);
                                    startActivity(intent2);
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    super.onAdFailedToShowFullScreenContent(adError);
                                    mInterstitialAd=null;
                                    Intent intent2=new Intent(ProfileActivity.this,LeadShowActivity.class);
                                    startActivity(intent2);
                                }
                            });
                        }
                    }, 1000);

                }
                else {
                    Intent intent2=new Intent(ProfileActivity.this,LeadShowActivity.class);
                    startActivity(intent2);
                }


            }
        });


        auth = FirebaseAuth.getInstance();
        String userid = auth.getUid();
        dialog = new ProgressDialog(ProfileActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Data Uploading .....");


        AdapterViewPager adapterViewPager = new AdapterViewPager(this);
        binding.viewpagr22.setAdapter(adapterViewPager);
        binding.tablyout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewpagr22.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.viewpagr22.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        binding.tablyout.getTabAt(position).select();
                }
                super.onPageSelected(position);

            }
        });


        FirebaseFirestore.getInstance().collection("OwnResi").document(userid).collection("Nanded")
                .whereEqualTo("type", "Rent")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            String count = String.valueOf(queryDocumentSnapshots.size());
                            binding.rentcount.setText(count);

                        }
                    }
                });

        FirebaseFirestore.getInstance().collection("OwnResi").document(userid).collection("Nanded")
                .whereEqualTo("type", "Sell")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            String count = String.valueOf(queryDocumentSnapshots.size());
                            binding.sellcount.setText(count);

                        }
                    }
                });
        FirebaseFirestore.getInstance().collection("OwnResi").document(userid).collection("Nanded")
                .whereEqualTo("type", "Hostel")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            String count = String.valueOf(queryDocumentSnapshots.size());
                            binding.hostelcount.setText(count);

                        }
                    }
                });
        FirebaseFirestore.getInstance().collection("Like").document(userid).collection("Nanded")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            String count = String.valueOf(queryDocumentSnapshots.size());
                            binding.likecount.setText(count);

                        }
                    }
                });







      /*  ArrayAdapter adapter=new ArrayAdapter(ProfileActivity.this, android.R.layout.simple_spinner_item,courses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.coursesspinner.setAdapter(adapter);
*/


        FirebaseFirestore.getInstance().collection("AllUser").document(userid)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        name = snapshot.getString("name");
                        number = snapshot.getString("number");
                        gmail = snapshot.getString("mail");

                        binding.profilename.setText(name);
                        binding.profilenumber.setText(number);
                        binding.profileemail.setText(gmail);
                        binding.optionalname.setText(name);


                    }
                });

        binding.showprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.showprofile.setVisibility(View.GONE);
                binding.allogoogleprofile.setVisibility(View.VISIBLE);
            }
        });  binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
        binding.hideprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.allogoogleprofile.setVisibility(View.GONE);
                binding.showprofile.setVisibility(View.VISIBLE);
            }
        });
        binding.addresidencybtn2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UploadFromHareActivity.class);
                startActivity(intent);
            }
        });
        binding.settingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        binding.editbtninprofile.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = findViewById(android.R.id.content);

                TextView dname, dnumber, dgmail;
                Button dadd;

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                View view = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.updateuserdatadialog, viewGroup, false);
                builder.setCancelable(true);
                builder.setView(view);

                dname = view.findViewById(R.id.dialogeditusername);
                dnumber = view.findViewById(R.id.dialogeditusernumber);
                dgmail = view.findViewById(R.id.dialogeditusermail);
                dadd = view.findViewById(R.id.dialogedituserupdatebtn);


                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dname.setText(name);
                dnumber.setText(number);
                dgmail.setText(gmail);


                dadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.show();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String userid = user.getUid();
                        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                        String username = dname.getText().toString();
                        String usernumber = dnumber.getText().toString();
                        String usermail = dgmail.getText().toString();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("number", usernumber);
                        hashMap.put("name", username);

                        firestore.collection("AllUser").document(userid).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dismiss();
                                alertDialog.dismiss();
                                Toast.makeText(ProfileActivity.this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show();

                            }
                        });


                    }
                });

                alertDialog.show();
            }
        });



    }


}