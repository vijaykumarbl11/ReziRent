package com.resieasy.rezirent.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.resieasy.rezirent.Activity.ShowHostelDataActivity;
import com.resieasy.rezirent.Activity.ShowResidencyDataActivity;
import com.resieasy.rezirent.Activity.ShowSellDataActivity;
import com.resieasy.rezirent.Class.BothResiClass;
import com.resieasy.rezirent.Class.LikeClass;
import com.resieasy.rezirent.R;
import com.resieasy.rezirent.databinding.BothhostelsapleBinding;
import com.resieasy.rezirent.databinding.BothresisampleBinding;
import com.resieasy.rezirent.databinding.BothsellsampleBinding;
import com.resieasy.rezirent.databinding.LayoutAdBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class BothResiiAdapter extends RecyclerView.Adapter {
    ArrayList<BothResiClass> list;
    Context context;
      int RESI_VIEW_TYPE = 4;
      int Sell_VIEW_TYPE = 2;
      int HOSTEL_VIEW_TYPE = 3;
      private static final int AD_VIEW = 1;
      private static final int ITEM_FEED_COUNT = 4;



    public BothResiiAdapter(ArrayList<BothResiClass> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == RESI_VIEW_TYPE) {
            View view1 = LayoutInflater.from(context).inflate(R.layout.bothresisample, parent, false);
            return new ResiViewHolder(view1);
        } else if (viewType == Sell_VIEW_TYPE) {
            View view2 = LayoutInflater.from(context).inflate(R.layout.bothsellsample, parent, false);
            return new SellViewHolder(view2);
        } else {
            View view3 = LayoutInflater.from(context).inflate(R.layout.bothhostelsaple, parent, false);
            return new HostelViewHolder(view3);
        }

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        BothResiClass data = list.get(position);
        String id = data.getId();





        if (holder.getClass() == ResiViewHolder.class) {

            ResiViewHolder viewHolder = (ResiViewHolder) holder;

            FirebaseFirestore.getInstance().collection("Nanded")
                    .document("NandedCity").collection("AllImage").document(id).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot snapshot) {
                            String firstimage = snapshot.getString("image0");
                            Picasso.get().load(firstimage).placeholder(R.drawable.iplaceholdr).into(viewHolder.binding.bothsampleimage);

                        }
                    });
            FirebaseFirestore.getInstance().collection("Nanded")
                    .document("NandedCity").collection("AllData").document(id).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot snapshot) {
                            String name = snapshot.getString("name");
                            String resitype = snapshot.getString("subtype");
                            String area = snapshot.getString("area");
                            String address = snapshot.getString("address");
                            String rent = snapshot.getString("rent");
                            int agree = snapshot.getLong("period").intValue();

                            viewHolder.binding.bothsamplename.setText(name);
                            viewHolder.binding.bothsampleaddress.setText(address);
                            viewHolder.binding.bothsamplearea.setText(area);
                            viewHolder.binding.bothsamplesubtype.setText(resitype);
                            viewHolder.binding.bothresirenttext.setText(rent + "₹/month");

                            if (agree == 708) {
                                viewHolder.binding.noagreeview.setVisibility(View.VISIBLE);
                                viewHolder.binding.yesagreeview.setVisibility(View.GONE);

                            } else {
                                viewHolder.binding.noagreeview.setVisibility(View.GONE);
                                viewHolder.binding.yesagreeview.setVisibility(View.VISIBLE);

                            }

                        }
                    });
            viewHolder.binding.resicart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ShowResidencyDataActivity.class);
                    intent.putExtra("id", id);
                    context.startActivity(intent);

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
                            if (clean.equals("Yes")) {
                                viewHolder.binding.cleanbotcart.setVisibility(View.VISIBLE);
                            }
                            if (ac.equals("Yes")) {
                                viewHolder.binding.acbotcart.setVisibility(View.VISIBLE);

                            }
                            if (rowater.equals("Yes")) {
                                viewHolder.binding.rowaterbotcart.setVisibility(View.VISIBLE);

                            }
                            if (water.equals("Yes")) {
                                viewHolder.binding.waterbotcart.setVisibility(View.VISIBLE);

                            }
                            if (wifi.equals("Yes")) {
                                viewHolder.binding.wifibotcart.setVisibility(View.VISIBLE);

                            }
                            if (cctv.equals("Yes")) {
                                viewHolder.binding.cctvbotcart.setVisibility(View.VISIBLE);

                            }
                            if (bed.equals("Yes")) {
                                viewHolder.binding.bedbotcart.setVisibility(View.VISIBLE);

                            }
                            if (hotwater.equals("Yes")) {
                                viewHolder.binding.hotwaterbotcart.setVisibility(View.VISIBLE);

                            }
                            if (table.equals("Yes")) {
                                viewHolder.binding.tablebotcart.setVisibility(View.VISIBLE);

                            }
                            if (locker.equals("Yes")) {
                                viewHolder.binding.lockerbotcart.setVisibility(View.VISIBLE);

                            }
                            if (fan.equals("Yes")) {
                                viewHolder.binding.fanbotcart.setVisibility(View.VISIBLE);

                            }
                            if (powerbackup.equals("Yes")) {
                                viewHolder.binding.backupbotcart.setVisibility(View.VISIBLE);

                            }
                            if (washing.equals("Yes")) {
                                viewHolder.binding.washingbotcart.setVisibility(View.VISIBLE);

                            }
                            if (security.equals("Yes")) {
                                viewHolder.binding.securitybotcart.setVisibility(View.VISIBLE);

                            }
                            if (inout.equals("Yes")) {
                                viewHolder.binding.inoutbotcart.setVisibility(View.VISIBLE);

                            }
                            if (attach.equals("Yes")) {
                                viewHolder.binding.attachedbotcart.setVisibility(View.VISIBLE);

                            }
                            if (shower.equals("Yes")) {
                                viewHolder.binding.showerbotcart.setVisibility(View.VISIBLE);

                            }
                            if (parking.equals("Yes")) {
                                viewHolder.binding.parkingbotcart.setVisibility(View.VISIBLE);

                            }
                            if (mess.equals("Yes")) {
                                viewHolder.binding.messbotcart.setVisibility(View.VISIBLE);

                            }
                            if (tv.equals("Yes")) {
                                viewHolder.binding.tvbotcart.setVisibility(View.VISIBLE);

                            }
                            if (gas.equals("Yes")) {
                                viewHolder.binding.gasbotcart.setVisibility(View.VISIBLE);

                            }
                            if (dining.equals("Yes")) {
                                viewHolder.binding.dianingbotcart.setVisibility(View.VISIBLE);

                            }
                            if (refrigerator.equals("Yes")) {
                                viewHolder.binding.refribotcart.setVisibility(View.VISIBLE);

                            }
                            if (sofa.equals("Yes")) {
                                viewHolder.binding.sofabotcart.setVisibility(View.VISIBLE);

                            }
                            if (elevator.equals("Yes")) {
                                viewHolder.binding.elevatorbotcart.setVisibility(View.VISIBLE);

                            }
                            if (play.equals("Yes")) {
                                viewHolder.binding.playbotcart.setVisibility(View.VISIBLE);

                            }
                            if (gym.equals("Yes")) {
                                viewHolder.binding.gymbotcart.setVisibility(View.VISIBLE);

                            }
                            if (studyroom.equals("Yes")) {
                                viewHolder.binding.studeybotcart.setVisibility(View.VISIBLE);

                            }
                            if (kitchen.equals("Yes")) {
                                viewHolder.binding.kitchenbotcart.setVisibility(View.VISIBLE);

                            }
                            if (balcony.equals("Yes")) {
                                viewHolder.binding.balconybotcart.setVisibility(View.VISIBLE);

                            }
                            if (indian.equals("Yes")) {
                                viewHolder.binding.indianbotcart.setVisibility(View.VISIBLE);

                            }
                            if (western.equals("Yes")) {
                                viewHolder.binding.westernbotcart.setVisibility(View.VISIBLE);

                            }
                            if (terrace.equals("Yes")) {
                                viewHolder.binding.terracebotcart.setVisibility(View.VISIBLE);

                            }
                            if (furnished.equals("Yes")) {
                                viewHolder.binding.furnishedbotcart.setVisibility(View.VISIBLE);

                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
            FirebaseFirestore.getInstance().collection("Like")
                    .document(FirebaseAuth.getInstance().getUid()).collection("Nanded").document(id).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    viewHolder.binding.unlike.setVisibility(View.GONE);
                                    viewHolder.binding.like.setVisibility(View.VISIBLE);

                                } else {
                                    viewHolder.binding.unlike.setVisibility(View.VISIBLE);
                                    viewHolder.binding.like.setVisibility(View.GONE);
                                }
                            }
                        }
                    });

            viewHolder.binding.unlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.binding.unlike.setVisibility(View.GONE);
                    viewHolder.binding.like.setVisibility(View.VISIBLE);
                    HashMap<String, Object> hashMap1 = new HashMap<>();
                    hashMap1.put("userid", FirebaseAuth.getInstance().getUid());

                    Date date = new Date();
                    LikeClass likeClass = new LikeClass(id, "Nanded","","",7028, date.getTime());
                    FirebaseFirestore.getInstance().collection("Like")
                            .document(FirebaseAuth.getInstance().getUid()).set(hashMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    FirebaseFirestore.getInstance().collection("Like")
                                            .document(FirebaseAuth.getInstance().getUid()).collection("Nanded")
                                            .document(id).set(likeClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                    Toast.makeText(context, "Added to your like list", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                }
                            });


                }
            });
            viewHolder.binding.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.binding.unlike.setVisibility(View.VISIBLE);
                    viewHolder.binding.like.setVisibility(View.GONE);
                    FirebaseFirestore.getInstance().collection("Like")
                            .document(FirebaseAuth.getInstance().getUid()).collection("Nanded")
                            .document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(context, "Remove from like list", Toast.LENGTH_SHORT).show();

                                }
                            });
                }
            });
        }
        else if (holder.getClass() == SellViewHolder.class) {

            SellViewHolder viewHolder = (SellViewHolder) holder;
            FirebaseFirestore.getInstance().collection("Nanded")
                    .document("NandedCity").collection("AllImage").document(id).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot snapshot) {
                            String firstimage = snapshot.getString("image0");
                            Picasso.get().load(firstimage).placeholder(R.drawable.iplaceholdr).into(viewHolder.binding.bothsampleimage);

                        }
                    });
            FirebaseFirestore.getInstance().collection("Nanded")
                    .document("NandedCity").collection("AllData").document(id).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot snapshot) {
                            String name = snapshot.getString("name");
                            String resitype = snapshot.getString("subtype");
                            String area = snapshot.getString("area");
                            String address = snapshot.getString("address");
                            String prize = snapshot.getString("prize");
                            viewHolder.binding.bothsamplename.setText(name);
                            viewHolder.binding.bothsampleaddress.setText(address);
                            viewHolder.binding.bothsamplearea.setText(area);
                            viewHolder.binding.bothsamplesubtype.setText(resitype);
                            if (prize.isEmpty()){
                                viewHolder.binding.prizeview.setVisibility(View.GONE);
                            }else {
                                viewHolder.binding.bothsampleprize.setText(prize + "₹");

                            }

                        }
                    });
            viewHolder.binding.sellcart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ShowSellDataActivity.class);
                    intent.putExtra("id", id);
                    context.startActivity(intent);

                }
            });
            FirebaseFirestore.getInstance().collection("Like")
                    .document(FirebaseAuth.getInstance().getUid()).collection("Nanded").document(id).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    viewHolder.binding.unlike.setVisibility(View.GONE);
                                    viewHolder.binding.like.setVisibility(View.VISIBLE);

                                } else {
                                    viewHolder.binding.unlike.setVisibility(View.VISIBLE);
                                    viewHolder.binding.like.setVisibility(View.GONE);
                                }
                            }
                        }
                    });
            viewHolder.binding.unlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.binding.unlike.setVisibility(View.GONE);
                    viewHolder.binding.like.setVisibility(View.VISIBLE);
                    HashMap<String, Object> hashMap1 = new HashMap<>();
                    hashMap1.put("userid", FirebaseAuth.getInstance().getUid());
                    Date date = new Date();
                    LikeClass likeClass = new LikeClass(id, "Nanded","","",7028, date.getTime());
                    FirebaseFirestore.getInstance().collection("Like")
                            .document(FirebaseAuth.getInstance().getUid()).set(hashMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    FirebaseFirestore.getInstance().collection("Like")
                                            .document(FirebaseAuth.getInstance().getUid()).collection("Nanded")
                                            .document(id).set(likeClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                    Toast.makeText(context, "Added to your like list", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                }
                            });


                }
            });
            viewHolder.binding.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.binding.unlike.setVisibility(View.VISIBLE);
                    viewHolder.binding.like.setVisibility(View.GONE);
                    FirebaseFirestore.getInstance().collection("Like")
                            .document(FirebaseAuth.getInstance().getUid()).collection("Nanded")
                            .document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(context, "Remove from like list", Toast.LENGTH_SHORT).show();

                                }
                            });
                }
            });
        }
        else if (holder.getClass() == HostelViewHolder.class) {
            HostelViewHolder viewHolder = (HostelViewHolder) holder;
            FirebaseFirestore.getInstance().collection("Nanded")
                    .document("NandedCity").collection("AllImage").document(id).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot snapshot) {
                            String firstimage = snapshot.getString("image0");
                            Picasso.get().load(firstimage).placeholder(R.drawable.iplaceholdr).into(viewHolder.binding.bothsampleimage);

                        }
                    });
            FirebaseFirestore.getInstance().collection("Nanded")
                    .document("NandedCity").collection("AllData").document(id).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot snapshot) {
                            String name = snapshot.getString("name");
                            String resitype = snapshot.getString("subtype");
                            String area = snapshot.getString("area");
                            String address = snapshot.getString("address");
                            String rent = snapshot.getString("rent");
                            int agree = snapshot.getLong("period").intValue();

                            viewHolder.binding.bothsamplename.setText(name);
                            viewHolder.binding.bothsampleaddress.setText(address);
                            viewHolder.binding.bothsamplearea.setText(area);
                            viewHolder.binding.bothsamplesubtype.setText(resitype);
                            viewHolder.binding.bothsampleprize.setText(rent + "₹/month");

                            if (agree == 708) {
                                viewHolder.binding.noagreeview.setVisibility(View.VISIBLE);
                                viewHolder.binding.yesagreeview.setVisibility(View.GONE);

                            } else {
                                viewHolder.binding.noagreeview.setVisibility(View.GONE);
                                viewHolder.binding.yesagreeview.setVisibility(View.VISIBLE);

                            }


                        }
                    });
            viewHolder.binding.hostelcart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ShowHostelDataActivity.class);
                    intent.putExtra("id", id);
                    context.startActivity(intent);

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
                            if (clean.equals("Yes")) {
                                viewHolder.binding.cleanbotcart.setVisibility(View.VISIBLE);
                            }
                            if (ac.equals("Yes")) {
                                viewHolder.binding.acbotcart.setVisibility(View.VISIBLE);

                            }
                            if (rowater.equals("Yes")) {
                                viewHolder.binding.rowaterbotcart.setVisibility(View.VISIBLE);

                            }
                            if (water.equals("Yes")) {
                                viewHolder.binding.waterbotcart.setVisibility(View.VISIBLE);

                            }
                            if (wifi.equals("Yes")) {
                                viewHolder.binding.wifibotcart.setVisibility(View.VISIBLE);

                            }
                            if (cctv.equals("Yes")) {
                                viewHolder.binding.cctvbotcart.setVisibility(View.VISIBLE);

                            }
                            if (bed.equals("Yes")) {
                                viewHolder.binding.bedbotcart.setVisibility(View.VISIBLE);

                            }
                            if (hotwater.equals("Yes")) {
                                viewHolder.binding.hotwaterbotcart.setVisibility(View.VISIBLE);

                            }
                            if (table.equals("Yes")) {
                                viewHolder.binding.tablebotcart.setVisibility(View.VISIBLE);

                            }
                            if (locker.equals("Yes")) {
                                viewHolder.binding.lockerbotcart.setVisibility(View.VISIBLE);

                            }
                            if (fan.equals("Yes")) {
                                viewHolder.binding.fanbotcart.setVisibility(View.VISIBLE);

                            }
                            if (powerbackup.equals("Yes")) {
                                viewHolder.binding.backupbotcart.setVisibility(View.VISIBLE);

                            }
                            if (washing.equals("Yes")) {
                                viewHolder.binding.washingbotcart.setVisibility(View.VISIBLE);

                            }
                            if (security.equals("Yes")) {
                                viewHolder.binding.securitybotcart.setVisibility(View.VISIBLE);

                            }
                            if (inout.equals("Yes")) {
                                viewHolder.binding.inoutbotcart.setVisibility(View.VISIBLE);

                            }
                            if (attach.equals("Yes")) {
                                viewHolder.binding.attachedbotcart.setVisibility(View.VISIBLE);

                            }
                            if (shower.equals("Yes")) {
                                viewHolder.binding.showerbotcart.setVisibility(View.VISIBLE);

                            }
                            if (parking.equals("Yes")) {
                                viewHolder.binding.parkingbotcart.setVisibility(View.VISIBLE);

                            }
                            if (mess.equals("Yes")) {
                                viewHolder.binding.messbotcart.setVisibility(View.VISIBLE);

                            }
                            if (tv.equals("Yes")) {
                                viewHolder.binding.tvbotcart.setVisibility(View.VISIBLE);

                            }
                            if (gas.equals("Yes")) {
                                viewHolder.binding.gasbotcart.setVisibility(View.VISIBLE);

                            }
                            if (dining.equals("Yes")) {
                                viewHolder.binding.dianingbotcart.setVisibility(View.VISIBLE);

                            }
                            if (refrigerator.equals("Yes")) {
                                viewHolder.binding.refribotcart.setVisibility(View.VISIBLE);

                            }
                            if (sofa.equals("Yes")) {
                                viewHolder.binding.sofabotcart.setVisibility(View.VISIBLE);

                            }
                            if (elevator.equals("Yes")) {
                                viewHolder.binding.elevatorbotcart.setVisibility(View.VISIBLE);

                            }
                            if (play.equals("Yes")) {
                                viewHolder.binding.playbotcart.setVisibility(View.VISIBLE);

                            }
                            if (gym.equals("Yes")) {
                                viewHolder.binding.gymbotcart.setVisibility(View.VISIBLE);

                            }
                            if (studyroom.equals("Yes")) {
                                viewHolder.binding.studeybotcart.setVisibility(View.VISIBLE);

                            }
                            if (kitchen.equals("Yes")) {
                                viewHolder.binding.kitchenbotcart.setVisibility(View.VISIBLE);

                            }
                            if (balcony.equals("Yes")) {
                                viewHolder.binding.balconybotcart.setVisibility(View.VISIBLE);

                            }
                            if (indian.equals("Yes")) {
                                viewHolder.binding.indianbotcart.setVisibility(View.VISIBLE);

                            }
                            if (western.equals("Yes")) {
                                viewHolder.binding.westernbotcart.setVisibility(View.VISIBLE);

                            }
                            if (terrace.equals("Yes")) {
                                viewHolder.binding.terracebotcart.setVisibility(View.VISIBLE);

                            }
                            if (furnished.equals("Yes")) {
                                viewHolder.binding.furnishedbotcart.setVisibility(View.VISIBLE);

                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
            FirebaseFirestore.getInstance().collection("Like")
                    .document(FirebaseAuth.getInstance().getUid()).collection("Nanded").document(id).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    viewHolder.binding.unlike.setVisibility(View.GONE);
                                    viewHolder.binding.like.setVisibility(View.VISIBLE);

                                } else {
                                    viewHolder.binding.unlike.setVisibility(View.VISIBLE);
                                    viewHolder.binding.like.setVisibility(View.GONE);
                                }
                            }
                        }
                    });
            viewHolder.binding.unlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.binding.unlike.setVisibility(View.GONE);
                    viewHolder.binding.like.setVisibility(View.VISIBLE);
                    HashMap<String, Object> hashMap1 = new HashMap<>();
                    hashMap1.put("userid", FirebaseAuth.getInstance().getUid());
                    Date date = new Date();
                    LikeClass likeClass = new LikeClass(id, "Nanded","","",7028, date.getTime());
                    FirebaseFirestore.getInstance().collection("Like")
                            .document(FirebaseAuth.getInstance().getUid()).set(hashMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    FirebaseFirestore.getInstance().collection("Like")
                                            .document(FirebaseAuth.getInstance().getUid()).collection("Nanded")
                                            .document(id).set(likeClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                    Toast.makeText(context, "Added to your like list", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                }
                            });


                }
            });

            viewHolder.binding.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.binding.unlike.setVisibility(View.VISIBLE);
                    viewHolder.binding.like.setVisibility(View.GONE);
                    FirebaseFirestore.getInstance().collection("Like")
                            .document(FirebaseAuth.getInstance().getUid()).collection("Nanded")
                            .document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(context, "Remove from like list", Toast.LENGTH_SHORT).show();

                                }
                            });

                }
            });
        }


    }

    @Override
    public int getItemViewType(int position) {
      if (list.get(position).getRtype().equals("Rent")){
          return RESI_VIEW_TYPE;
      } else if (list.get(position).getRtype().equals("Sell")) {
          return Sell_VIEW_TYPE;
      }else {
          return HOSTEL_VIEW_TYPE;
      }


    }


    @Override
    public int getItemCount() {

        return list.size();


    }

    public class ResiViewHolder extends RecyclerView.ViewHolder {
        BothresisampleBinding binding;

        public ResiViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = BothresisampleBinding.bind(itemView);

        }
    }

    public class SellViewHolder extends RecyclerView.ViewHolder {
        BothsellsampleBinding binding;


        public SellViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = BothsellsampleBinding.bind(itemView);

        }
    }

    public class HostelViewHolder extends RecyclerView.ViewHolder {
        BothhostelsapleBinding binding;


        public HostelViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = BothhostelsapleBinding.bind(itemView);

        }
    }

}


