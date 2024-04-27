package com.resieasy.rezirent.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import com.resieasy.rezirent.databinding.ActivityShowHostelDataBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ShowHostelDataActivity extends AppCompatActivity {
    ActivityShowHostelDataBinding binding;
    double latitude, longitude;
    String name, number, whatsapp, userid;
    int in;
    InterstitialAd mInterstitialAdcall;
    InterstitialAd mInterstitialAdwhats;
    ProgressDialog ad_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowHostelDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ad_dialog=new ProgressDialog(this);
        ad_dialog.setMessage("Ad loading");
        ad_dialog.setCancelable(false);


        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        AdRequest adRequestcall = new AdRequest.Builder().build();
        AdRequest adRequestwhats = new AdRequest.Builder().build();


        InterstitialAd.load(this, String.valueOf(R.string.Showhosteldatacallintertitial_id), adRequestcall,
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
        InterstitialAd.load(this, String.valueOf(R.string.Showhosteldatawhatsintertitial_id), adRequestwhats,
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
                        String idd = documentSnapshot.getString("id");
                        String type = documentSnapshot.getString("type");
                        String subtype = documentSnapshot.getString("subtype");
                        String area = documentSnapshot.getString("area");
                        String oname = documentSnapshot.getString("oname");
                        number = documentSnapshot.getString("number");
                        whatsapp = documentSnapshot.getString("whatsapp");
                        String mail = documentSnapshot.getString("mail");
                        String rent = documentSnapshot.getString("rent");
                        String erent = documentSnapshot.getString("erent");
                        String deposit = documentSnapshot.getString("deposit");
                        String extra = documentSnapshot.getString("extra");
                        String more = documentSnapshot.getString("more");
                        String opengate = documentSnapshot.getString("gopen");
                        String closegate = documentSnapshot.getString("gclose");
                        String policy = documentSnapshot.getString("policy");
                        int period = documentSnapshot.getLong("period").intValue();
                        String numperiod = String.valueOf(documentSnapshot.getLong("period").intValue());

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
                        binding.rent.setText(rent + "₹/month");
                        binding.erent.setText(erent);

                        binding.expagreeorpolicy.setText(policy);

                        if (mail.isEmpty()) {
                            binding.emailview.setVisibility(View.GONE);
                        } else {
                            binding.mail.setText(mail);
                        }
                        if (more.isEmpty()) {
                            binding.moreview.setVisibility(View.GONE);
                        } else {
                            binding.more.setText(more);
                        }


                        if (period == 708) {
                            binding.noagreeview.setVisibility(View.VISIBLE);
                            binding.yesagreeview.setVisibility(View.GONE);

                        } else {
                            binding.noagreeview.setVisibility(View.GONE);
                            binding.yesagreeview.setVisibility(View.VISIBLE);
                            binding.agreperiodn.setText(numperiod);

                        }
                        if (opengate.equals("No") && closegate.equals("No")) {
                            binding.gateview.setVisibility(View.GONE);
                            binding.nogateblue.setVisibility(View.VISIBLE);
                        } else {
                            binding.gateview.setVisibility(View.VISIBLE);
                            binding.nogateblue.setVisibility(View.GONE);
                            binding.openigtime.setText(opengate);
                            binding.closingtime.setText(closegate);
                        }
                        if (deposit.equals("No deposit will taken")) {
                            binding.nodepositblue.setVisibility(View.VISIBLE);
                            binding.depositview.setVisibility(View.GONE);
                        } else {
                            binding.nodepositblue.setVisibility(View.GONE);
                            binding.depositview.setVisibility(View.VISIBLE);
                            binding.deposit.setText(deposit);

                        }
                        if (extra.equals("No extra charges will taken")) {
                            binding.noextrablue.setVisibility(View.VISIBLE);
                            binding.extraview.setVisibility(View.GONE);
                        } else {
                            binding.noextrablue.setVisibility(View.GONE);
                            binding.extraview.setVisibility(View.VISIBLE);
                            binding.extra.setText(extra);

                        }


                        FirebaseFirestore.getInstance().collection("Nanded")
                                .document("NandedCity").collection("AllImage").document(idd).get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot snapshot) {
                                        for (int i = 0; i < in; i++) {

                                            String ima = snapshot.getString("image" + i);
                                            remotimage.add(new SlideModel(ima, i + 1 + "/" + in, ScaleTypes.FIT));
                                            binding.imageSlider.setImageList(remotimage, ScaleTypes.FIT);

                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ShowHostelDataActivity.this, "Image is not load, something is wrong", Toast.LENGTH_SHORT).show();

                                    }
                                });



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShowHostelDataActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();


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
                            mInterstitialAdcall.show(ShowHostelDataActivity.this);
                            mInterstitialAdcall.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    mInterstitialAdcall=null;
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + number));
                                    startActivity(intent);
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    super.onAdFailedToShowFullScreenContent(adError);
                                    mInterstitialAdcall=null;
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + number));
                                    startActivity(intent);
                                }
                            });
                        }
                    }, 1000);

                }
                else {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + number));
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
                            mInterstitialAdwhats.show(ShowHostelDataActivity.this);
                            mInterstitialAdwhats.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    mInterstitialAdwhats=null;
                                    String wn = "https://wa.me/+917028297606?text= Hi is anyone available?";
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("https://wa.me/+91" + whatsapp + "?text= Hi is anyone available?"));
                                    startActivity(intent);

                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    super.onAdFailedToShowFullScreenContent(adError);
                                    mInterstitialAdwhats=null;
                                    String wn = "https://wa.me/+917028297606?text= Hi is anyone available?";
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("https://wa.me/+91" + whatsapp + "?text= Hi is anyone available?"));
                                    startActivity(intent);

                                }
                            });
                        }
                    }, 1000);

                }
                else {
                    String wn = "https://wa.me/+917028297606?text= Hi is anyone available?";
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://wa.me/+91" + whatsapp + "?text= Hi is anyone available?"));
                    startActivity(intent);

                }

            }
        });

        FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                .collection("AllFacility").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String clean = documentSnapshot.getString("clean");
                        String ac = documentSnapshot.getString("ac");
                        String rowater = documentSnapshot.getString("rowater");
                        String water = documentSnapshot.getString("water");
                        String wifi = documentSnapshot.getString("wifi");
                        String cctv = documentSnapshot.getString("cctv");
                        String bed = documentSnapshot.getString("bed");
                        String hotwater = documentSnapshot.getString("hotwater");
                        String table = documentSnapshot.getString("table");
                        String locker = documentSnapshot.getString("locker");
                        String fan = documentSnapshot.getString("fan");
                        String powerbackup = documentSnapshot.getString("powerbackup");
                        String washing = documentSnapshot.getString("washing");
                        String security = documentSnapshot.getString("security");
                        String inout = documentSnapshot.getString("inout");
                        String attach = documentSnapshot.getString("attach");
                        String shower = documentSnapshot.getString("shower");
                        String parking = documentSnapshot.getString("parking");
                        String mess = documentSnapshot.getString("mess");
                        String tv = documentSnapshot.getString("tv");
                        String gas = documentSnapshot.getString("gas");
                        String dining = documentSnapshot.getString("dining");
                        String refrigerator = documentSnapshot.getString("refrigerator");
                        String sofa = documentSnapshot.getString("sofa");
                        String elevator = documentSnapshot.getString("elevator");
                        String play = documentSnapshot.getString("play");
                        String gym = documentSnapshot.getString("gym");
                        String studyroom = documentSnapshot.getString("studyroom");
                        String kitchen = documentSnapshot.getString("kitchen");
                        String balcony = documentSnapshot.getString("balcony");
                        String indian = documentSnapshot.getString("indian");
                        String western = documentSnapshot.getString("western");
                        String terrace = documentSnapshot.getString("terrace");
                        String furnished = documentSnapshot.getString("furnished");
                        String morefaci = documentSnapshot.getString("more");
                        if (clean.equals("Yes")) {
                            binding.clean.setVisibility(View.VISIBLE);
                        }
                        if (ac.equals("Yes")) {
                            binding.ac.setVisibility(View.VISIBLE);
                        }
                        if (rowater.equals("Yes")) {
                            binding.rowater.setVisibility(View.VISIBLE);
                        }
                        if (water.equals("Yes")) {
                            binding.water.setVisibility(View.VISIBLE);
                        }
                        if (wifi.equals("Yes")) {
                            binding.wifi.setVisibility(View.VISIBLE);
                        }
                        if (cctv.equals("Yes")) {
                            binding.cctv.setVisibility(View.VISIBLE);
                        }
                        if (bed.equals("Yes")) {
                            binding.bedandmat.setVisibility(View.VISIBLE);
                        }
                        if (hotwater.equals("Yes")) {
                            binding.hotwater.setVisibility(View.VISIBLE);
                        }
                        if (table.equals("Yes")) {
                            binding.table.setVisibility(View.VISIBLE);
                        }
                        if (locker.equals("Yes")) {
                            binding.locker.setVisibility(View.VISIBLE);
                        }
                        if (fan.equals("Yes")) {
                            binding.cooler.setVisibility(View.VISIBLE);
                        }
                        if (powerbackup.equals("Yes")) {
                            binding.backup.setVisibility(View.VISIBLE);
                        }
                        if (washing.equals("Yes")) {
                            binding.washing.setVisibility(View.VISIBLE);
                        }
                        if (security.equals("Yes")) {
                            binding.security.setVisibility(View.VISIBLE);
                        }
                        if (inout.equals("Yes")) {
                            binding.inout.setVisibility(View.VISIBLE);
                        }
                        if (attach.equals("Yes")) {
                            binding.attached.setVisibility(View.VISIBLE);
                        }
                        if (shower.equals("Yes")) {
                            binding.shower.setVisibility(View.VISIBLE);
                        }
                        if (parking.equals("Yes")) {
                            binding.parking.setVisibility(View.VISIBLE);
                        }
                        if (mess.equals("Yes")) {
                            binding.mess.setVisibility(View.VISIBLE);
                        }
                        if (tv.equals("Yes")) {
                            binding.tv.setVisibility(View.VISIBLE);
                        }
                        if (gas.equals("Yes")) {
                            binding.gas.setVisibility(View.VISIBLE);
                        }
                        if (dining.equals("Yes")) {
                            binding.dinning.setVisibility(View.VISIBLE);
                        }
                        if (refrigerator.equals("Yes")) {
                            binding.refrigi.setVisibility(View.VISIBLE);
                        }
                        if (sofa.equals("Yes")) {
                            binding.sofa.setVisibility(View.VISIBLE);
                        }
                        if (elevator.equals("Yes")) {
                            binding.elevator.setVisibility(View.VISIBLE);
                        }
                        if (play.equals("Yes")) {
                            binding.play.setVisibility(View.VISIBLE);
                        }
                        if (gym.equals("Yes")) {
                            binding.gym.setVisibility(View.VISIBLE);
                        }
                        if (studyroom.equals("Yes")) {
                            binding.stuyroom.setVisibility(View.VISIBLE);
                        }
                        if (kitchen.equals("Yes")) {
                            binding.kitchen.setVisibility(View.VISIBLE);
                        }
                        if (balcony.equals("Yes")) {
                            binding.balcony.setVisibility(View.VISIBLE);
                        }
                        if (indian.equals("Yes")) {
                            binding.indian.setVisibility(View.VISIBLE);
                        }
                        if (western.equals("Yes")) {
                            binding.western.setVisibility(View.VISIBLE);
                        }
                        if (terrace.equals("Yes")) {
                            binding.terrace.setVisibility(View.VISIBLE);
                        }
                        if (furnished.equals("Yes")) {
                            binding.furnised.setVisibility(View.VISIBLE);
                        }
                        if (!morefaci.isEmpty()) {
                            binding.morefaci.setVisibility(View.VISIBLE);
                            binding.morefacilitytext.setText(morefaci);

                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShowHostelDataActivity.this, "Facility are not load, something is wrong", Toast.LENGTH_SHORT).show();

                    }
                });
        FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                .collection("AllRule").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String clean = documentSnapshot.getString("clean");
                        String trouble = documentSnapshot.getString("trouble");
                        String licence = documentSnapshot.getString("licence");
                        String gateenry = documentSnapshot.getString("gateenry");
                        String alcohol = documentSnapshot.getString("alcohol");
                        String damage = documentSnapshot.getString("damage");
                        String ousiders = documentSnapshot.getString("ousiders");
                        String permission = documentSnapshot.getString("permission");
                        String morerule = documentSnapshot.getString("more");


                        if (clean.equals("Yes")) {
                            binding.clinerule.setVisibility(View.VISIBLE);
                        }
                        if (trouble.equals("Yes")) {
                            binding.nottrublerule.setVisibility(View.VISIBLE);
                        }
                        if (licence.equals("Yes")) {
                            binding.licencerule.setVisibility(View.VISIBLE);
                        }
                        if (gateenry.equals("Yes")) {
                            binding.entryrule.setVisibility(View.VISIBLE);
                        }
                        if (alcohol.equals("Yes")) {
                            binding.alcoholrule.setVisibility(View.VISIBLE);
                        }
                        if (damage.equals("Yes")) {
                            binding.damagerule.setVisibility(View.VISIBLE);
                        }
                        if (ousiders.equals("Yes")) {
                            binding.outsiderrule.setVisibility(View.VISIBLE);
                        }
                        if (permission.equals("Yes")) {
                            binding.prentperule.setVisibility(View.VISIBLE);
                        }
                        if (!morerule.isEmpty()) {
                            binding.morerules.setVisibility(View.VISIBLE);
                            binding.moreruletext.setText(morerule);
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShowHostelDataActivity.this, "Rules are not load, something is wrong", Toast.LENGTH_SHORT).show();

                    }
                });


        binding.mapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowHostelDataActivity.this, MapsActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("name", name);
                startActivity(intent);

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
                                                                                        myname + " see your  " + subtype1 + " details, please check.", getApplicationContext(), ShowHostelDataActivity.this);
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
                                                                                    myname + " see your  " + subtype1 + " details, please check.", getApplicationContext(), ShowHostelDataActivity.this);
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