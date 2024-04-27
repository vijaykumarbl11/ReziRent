package com.resieasy.rezirent.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.AnimationTypes;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.resieasy.rezirent.Class.LeadClass;
import com.resieasy.rezirent.FcmNotificationsSender;
import com.resieasy.rezirent.R;
import com.resieasy.rezirent.databinding.ActivityShowSellDataBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ShowSellDataActivity extends AppCompatActivity {
    ActivityShowSellDataBinding binding;


    double latitude, longitude;
    String name,number,whatsapp,userid;
    int in;
    InterstitialAd mInterstitialAdcall;
    InterstitialAd mInterstitialAdwhats;
    ProgressDialog ad_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowSellDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ad_dialog=new ProgressDialog(this);
        ad_dialog.setMessage("Ad loading");
        ad_dialog.setCancelable(false);


        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        AdRequest adRequestcall = new AdRequest.Builder().build();
        AdRequest adRequestwhats = new AdRequest.Builder().build();


        InterstitialAd.load(this, String.valueOf(R.string.Showselldatacallintertitial_id), adRequestcall,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAdcall = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAdcall = null;
                    }
                });
        InterstitialAd.load(this, String.valueOf(R.string.Showselldatawhatsintertitial_id), adRequestwhats,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAdwhats = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAdwhats = null;
                    }
                });



        binding.adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError);
                binding.adView.loadAd(adRequest);
            }

        });

        String id = getIntent().getStringExtra("id");

        List<SlideModel> remotimage = new ArrayList<>();


        FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllData").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                         in = documentSnapshot.getLong("in").intValue();
                        latitude = documentSnapshot.getDouble("latitude").doubleValue();
                        longitude = documentSnapshot.getDouble("longitude").doubleValue();


                        name = documentSnapshot.getString("name");
                        String address = documentSnapshot.getString("address");
                        String type = documentSnapshot.getString("type");
                        String subtype = documentSnapshot.getString("subtype");
                        String area = documentSnapshot.getString("area");

                        String oname = documentSnapshot.getString("oname");
                        String idd = documentSnapshot.getString("id");
                         number = documentSnapshot.getString("number");
                         whatsapp = documentSnapshot.getString("whatsapp");
                        String mail = documentSnapshot.getString("mail");
                        String prize = documentSnapshot.getString("prize");
                        String eprize = documentSnapshot.getString("eprize");
                        String more = documentSnapshot.getString("more");
                        String size = documentSnapshot.getString("size");

                        userid = documentSnapshot.getString("userid");
                        if (!userid.equals(FirebaseAuth.getInstance().getUid())){
                            sendnotandlead(userid, id, subtype);
                        }

                        binding.name.setText(name);
                        binding.address.setText(address);
                        binding.samplesubtype.setText(subtype);
                        binding.samplearea.setText(area);
                        binding.oname.setText(oname);
                        binding.contact.setText(number);
                        binding.whatsapp.setText(whatsapp);
                        if (mail.isEmpty()){
                            binding.emailview.setVisibility(View.GONE);
                        }else {
                            binding.mail.setText(mail);
                        }
                        if (more.isEmpty()){
                            binding.moreview.setVisibility(View.GONE);
                        }else {
                            binding.moree.setText(more);
                        }
                        if (prize.isEmpty()){
                            binding.prizeview.setVisibility(View.GONE);
                        }else {
                            binding.prize.setText(prize+"₹");
                            binding.eprize.setText(eprize);
                        }
                        if (size.isEmpty()){
                            binding.sizeview.setVisibility(View.GONE);
                        }else {
                            binding.sizee.setText(size);
                        }


                        FirebaseFirestore.getInstance().collection("Nanded")
                                .document("NandedCity").collection("AllImage").document(idd).get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot snapshot) {
                                        for (int i=0;i<in;i++){

                                            String ima=snapshot.getString("image"+i);
                                            remotimage.add(new SlideModel(ima,i+1+"/"+in, ScaleTypes.FIT));
                                            binding.imageSlider.setImageList(remotimage,ScaleTypes.FIT);

                                        }


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ShowSellDataActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShowSellDataActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                });




        binding.mapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowSellDataActivity.this, MapsActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("name", name);
                startActivity(intent);

            }
        });
        binding.cmscontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mInterstitialAdcall != null) {
                    ad_dialog.show();
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ad_dialog.dismiss();
                            mInterstitialAdcall.show(ShowSellDataActivity.this);
                            mInterstitialAdcall.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    mInterstitialAdcall=null;
                                    Intent intent=new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:"+number));
                                    startActivity(intent);

                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    super.onAdFailedToShowFullScreenContent(adError);
                                    mInterstitialAdcall=null;
                                    Intent intent=new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:"+number));
                                    startActivity(intent);
                                }
                            });
                        }
                    }, 1000);

                }
                else {
                    Intent intent=new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+number));
                    startActivity(intent);
                }


            }
        });

        binding.cmswhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAdwhats != null) {
                    ad_dialog.show();
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ad_dialog.dismiss();
                            mInterstitialAdwhats.show(ShowSellDataActivity.this);
                            mInterstitialAdwhats.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    mInterstitialAdwhats=null;
                                    String wn="https://wa.me/+917028297606?text= Hi is anyone available?";
                                    Intent intent=new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("https://wa.me/+91"+whatsapp+"?text= Hi is anyone available?"));
                                    startActivity(intent);

                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    super.onAdFailedToShowFullScreenContent(adError);
                                    mInterstitialAdwhats=null;
                                    String wn="https://wa.me/+917028297606?text= Hi is anyone available?";
                                    Intent intent=new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("https://wa.me/+91"+whatsapp+"?text= Hi is anyone available?"));
                                    startActivity(intent);


                                }
                            });
                        }
                    }, 1000);

                }
                else {
                    String wn="https://wa.me/+917028297606?text= Hi is anyone available?";
                    Intent intent=new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://wa.me/+91"+whatsapp+"?text= Hi is anyone available?"));
                    startActivity(intent);


                }

            }
        });

    }

    private void sendnotandlead(String userid1, String id11, String subtype1) {

        FirebaseFirestore.getInstance().collection("Lead").document(userid1).collection("Nanded")
                .document(FirebaseAuth.getInstance().getUid()+id11).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        DocumentSnapshot snapshot1 = task.getResult();
                        if (snapshot1.exists()) {
                            LeadClass d = task.getResult().toObject(LeadClass.class);
                            Date date = new Date();
                            Long oldtt = d.getTime();
                            Long newtt = date.getTime();
                            long diffrence = newtt - oldtt;
                            long myValue = Long.parseLong(convertSecondsToHMmSs(diffrence));
                            if (myValue < 1) {
                                HashMap<String, Object> hashMap1 = new HashMap<>();
                                hashMap1.put("time", date.getTime());
                                hashMap1.put("resiid", id11);

                                FirebaseFirestore.getInstance().collection("Lead").document(userid1)
                                        .collection("Nanded").document(FirebaseAuth.getInstance().getUid()+id11).update(hashMap1);

                            } else {
                                HashMap<String, Object> hashMap2 = new HashMap<>();
                                hashMap2.put("time", date.getTime());
                                hashMap2.put("resiid", id11);

                                FirebaseFirestore.getInstance().collection("Lead").document(userid1)
                                        .collection("Nanded").document(FirebaseAuth.getInstance().getUid()+id11).update(hashMap2)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                FirebaseFirestore.getInstance().collection("AllUser").document(userid).get()
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot snapshot1) {
                                                                FirebaseFirestore.getInstance().collection("AllUser").document(FirebaseAuth.getInstance().getUid()).get()
                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentSnapshot snapshot2) {
                                                                                String token = snapshot1.getString("token");
                                                                                String myname = snapshot2.getString("name");
                                                                                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token, "ResiEasy, Lead For " + subtype1,
                                                                                        myname + " see your  " + subtype1 + " details, please check.", getApplicationContext(), ShowSellDataActivity.this);
                                                                                notificationsSender.SendNotifications();

                                                                            }
                                                                        });

                                                            }
                                                        });
                                            }
                                        });
                            }

                        } else {
                            Date date = new Date();
                            LeadClass leadClass = new LeadClass(FirebaseAuth.getInstance().getUid(), id11, "Nanded", "", "", 7028, date.getTime());
                            FirebaseFirestore.getInstance().collection("Lead").document(userid1)
                                    .collection("Nanded").document(FirebaseAuth.getInstance().getUid()+id11).set(leadClass)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            FirebaseFirestore.getInstance().collection("AllUser").document(userid).get()
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot snapshot1) {
                                                            FirebaseFirestore.getInstance().collection("AllUser").document(FirebaseAuth.getInstance().getUid()).get()
                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentSnapshot snapshot2) {
                                                                            String token = snapshot1.getString("token");
                                                                            String myname = snapshot2.getString("name");
                                                                            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token, "ResiEasy, Lead For " + subtype1,
                                                                                    myname + " see your  " + subtype1 + " details, please check.", getApplicationContext(), ShowSellDataActivity.this);
                                                                            notificationsSender.SendNotifications();

                                                                        }
                                                                    });

                                                        }
                                                    });
                                        }
                                    });
                        }

                    }
                });


    }

    public static String convertSecondsToHMmSs(long millis) {
        //long seconds = (millis / 1000) % 60;
        //long minutes = (millis / (1000 * 60)) % 60;
        long hours = millis / (1000 * 60 * 60);

        StringBuilder b = new StringBuilder();
        b.append(hours == 0 ? "00" : hours < 10 ? String.valueOf("0" + hours) : String.valueOf(hours));
        // b.append(":");
        //b.append(minutes == 0 ? "00" : minutes < 10 ? String.valueOf("0" + minutes) : String.valueOf(minutes));
        // b.append(":");
        // b.append(seconds == 0 ? "00" : seconds < 10 ? String.valueOf("0" + seconds) : String.valueOf(seconds));
        return b.toString();
    }

}