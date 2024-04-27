package com.resieasy.rezirent.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import com.resieasy.rezirent.Class.LikeClass;
import com.resieasy.rezirent.R;
import com.resieasy.rezirent.databinding.LikeownsampleBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.ViewHolder> {

    Context context;
    ArrayList<LikeClass> list;

    public LikeAdapter(Context context, ArrayList<LikeClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LikeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.likeownsample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LikeAdapter.ViewHolder holder, int position) {
        LikeClass data = list.get(position);
        String id = data.getId();


        FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllImage").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        String firstimage = snapshot.getString("image0");
                        Picasso.get().load(firstimage).placeholder(R.drawable.iplaceholdr).into(holder.binding.bothsampleimage);

                    }
                });


        FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllData").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {

                        String rtype = snapshot.getString("rtype");

                        if (rtype.equals("Rent")){
                            holder.binding.toptext.setText("Rent");

                            String name = snapshot.getString("name");
                            String type = snapshot.getString("type");
                            String area = snapshot.getString("area");
                            String address = snapshot.getString("address");
                            String rent = snapshot.getString("rent");
                            int agree = snapshot.getLong("period").intValue();

                            holder.binding.bothsamplename.setText(name);
                            holder.binding.bothsampleaddress.setText(address);
                            holder.binding.bothsamplearea.setText(area);
                            holder.binding.bothsampletype.setText(type);
                            holder.binding.bothsampleprize.setText("Monthly Rent : "+rent+"₹");


                            if (agree == 708) {
                                holder.binding.noagreeview.setVisibility(View.VISIBLE);
                                holder.binding.yesagreeview.setVisibility(View.GONE);

                            } else {
                                holder.binding.noagreeview.setVisibility(View.GONE);
                                holder.binding.yesagreeview.setVisibility(View.VISIBLE);

                            }
                            holder.binding.resicart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(context, ShowResidencyDataActivity.class);
                                    intent.putExtra("id", id);
                                    context.startActivity(intent);
                                }
                            });


                        } else if (rtype.equals("Sell")) {
                            holder.binding.toptext.setText("Sell");

                            String name = snapshot.getString("name");
                            String type = snapshot.getString("type");
                            String area = snapshot.getString("area");
                            String address = snapshot.getString("address");
                            String rent = snapshot.getString("prize");
                            holder.binding.bothsamplename.setText(name);
                            holder.binding.bothsampleaddress.setText(address);
                            holder.binding.bothsamplearea.setText(area);
                            holder.binding.bothsampletype.setText(type);
                            if (rent.isEmpty()){
                                holder.binding.rentview.setVisibility(View.GONE);
                            }else {
                                holder.binding.bothsampleprize.setText("Selling Prize : "+rent+"₹");
                            }

                            holder.binding.resicart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(context, ShowSellDataActivity.class);
                                    intent.putExtra("id", id);
                                    context.startActivity(intent);
                                }
                            });



                        }else if (rtype.equals("Hostel")) {
                            holder.binding.toptext.setText("Cot-Base");

                            String name = snapshot.getString("name");
                            String type = snapshot.getString("type");
                            String area = snapshot.getString("area");
                            String address = snapshot.getString("address");
                            String rent = snapshot.getString("rent");
                            int agree = snapshot.getLong("period").intValue();


                            holder.binding.bothsamplename.setText(name);
                            holder.binding.bothsampleaddress.setText(address);
                            holder.binding.bothsamplearea.setText(area);
                            holder.binding.bothsampletype.setText(type);
                            holder.binding.bothsampleprize.setText("Per Person Monthly Rent : "+rent+"₹");


                            if (agree == 708) {
                                holder.binding.noagreeview.setVisibility(View.VISIBLE);
                                holder.binding.yesagreeview.setVisibility(View.GONE);

                            } else {
                                holder.binding.noagreeview.setVisibility(View.GONE);
                                holder.binding.yesagreeview.setVisibility(View.VISIBLE);

                            }
                            holder.binding.resicart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(context, ShowHostelDataActivity.class);
                                    intent.putExtra("id", id);
                                    context.startActivity(intent);
                                }
                            });


                        }




                    }
                });


        FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                .collection("AllFacility").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

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
                                holder.binding.cleanbotcart.setVisibility(View.VISIBLE);
                            }
                            if (ac.equals("Yes")) {
                                holder.binding.acbotcart.setVisibility(View.VISIBLE);

                            }
                            if (rowater.equals("Yes")) {
                                holder.binding.rowaterbotcart.setVisibility(View.VISIBLE);

                            }
                            if (water.equals("Yes")) {
                                holder.binding.waterbotcart.setVisibility(View.VISIBLE);

                            }
                            if (wifi.equals("Yes")) {
                                holder.binding.wifibotcart.setVisibility(View.VISIBLE);

                            }
                            if (cctv.equals("Yes")) {
                                holder.binding.cctvbotcart.setVisibility(View.VISIBLE);

                            }
                            if (bed.equals("Yes")) {
                                holder.binding.bedbotcart.setVisibility(View.VISIBLE);

                            }
                            if (hotwater.equals("Yes")) {
                                holder.binding.hotwaterbotcart.setVisibility(View.VISIBLE);

                            }
                            if (table.equals("Yes")) {
                                holder.binding.tablebotcart.setVisibility(View.VISIBLE);

                            }
                            if (locker.equals("Yes")) {
                                holder.binding.lockerbotcart.setVisibility(View.VISIBLE);

                            }
                            if (fan.equals("Yes")) {
                                holder.binding.fanbotcart.setVisibility(View.VISIBLE);

                            }
                            if (powerbackup.equals("Yes")) {
                                holder.binding.backupbotcart.setVisibility(View.VISIBLE);

                            }
                            if (washing.equals("Yes")) {
                                holder.binding.washingbotcart.setVisibility(View.VISIBLE);

                            }
                            if (security.equals("Yes")) {
                                holder.binding.securitybotcart.setVisibility(View.VISIBLE);

                            }
                            if (inout.equals("Yes")) {
                                holder.binding.inoutbotcart.setVisibility(View.VISIBLE);

                            }
                            if (attach.equals("Yes")) {
                                holder.binding.attachedbotcart.setVisibility(View.VISIBLE);

                            }
                            if (shower.equals("Yes")) {
                                holder.binding.showerbotcart.setVisibility(View.VISIBLE);

                            }
                            if (parking.equals("Yes")) {
                                holder.binding.parkingbotcart.setVisibility(View.VISIBLE);

                            }
                            if (mess.equals("Yes")) {
                                holder.binding.messbotcart.setVisibility(View.VISIBLE);

                            }
                            if (tv.equals("Yes")) {
                                holder.binding.tvbotcart.setVisibility(View.VISIBLE);

                            }
                            if (gas.equals("Yes")) {
                                holder.binding.gasbotcart.setVisibility(View.VISIBLE);

                            }
                            if (dining.equals("Yes")) {
                                holder.binding.dianingbotcart.setVisibility(View.VISIBLE);

                            }
                            if (refrigerator.equals("Yes")) {
                                holder.binding.refribotcart.setVisibility(View.VISIBLE);

                            }
                            if (sofa.equals("Yes")) {
                                holder.binding.sofabotcart.setVisibility(View.VISIBLE);

                            }
                            if (elevator.equals("Yes")) {
                                holder.binding.elevatorbotcart.setVisibility(View.VISIBLE);

                            }
                            if (play.equals("Yes")) {
                                holder.binding.playbotcart.setVisibility(View.VISIBLE);

                            }
                            if (gym.equals("Yes")) {
                                holder.binding.gymbotcart.setVisibility(View.VISIBLE);

                            }
                            if (studyroom.equals("Yes")) {
                                holder.binding.studeybotcart.setVisibility(View.VISIBLE);

                            }
                            if (kitchen.equals("Yes")) {
                                holder.binding.kitchenbotcart.setVisibility(View.VISIBLE);

                            }
                            if (balcony.equals("Yes")) {
                                holder.binding.balconybotcart.setVisibility(View.VISIBLE);

                            }
                            if (indian.equals("Yes")) {
                                holder.binding.indianbotcart.setVisibility(View.VISIBLE);

                            }
                            if (western.equals("Yes")) {
                                holder.binding.westernbotcart.setVisibility(View.VISIBLE);

                            }
                            if (terrace.equals("Yes")) {
                                holder.binding.terracebotcart.setVisibility(View.VISIBLE);

                            }
                            if (furnished.equals("Yes")) {
                                holder.binding.furnishedbotcart.setVisibility(View.VISIBLE);

                            }
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        holder.binding.unlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.unlike.setVisibility(View.GONE);
                holder.binding.like.setVisibility(View.VISIBLE);
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
        holder.binding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.unlike.setVisibility(View.VISIBLE);
                holder.binding.like.setVisibility(View.GONE);
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

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LikeownsampleBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = LikeownsampleBinding.bind(itemView);
        }
    }
}
