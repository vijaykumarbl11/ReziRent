package com.resieasy.rezirent.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.resieasy.rezirent.Activity.EditHostelDataActivity;
import com.resieasy.rezirent.Activity.EditSellDataActivity;
import com.resieasy.rezirent.Activity.ProfileActivity;
import com.resieasy.rezirent.Activity.ShowHostelDataActivity;
import com.resieasy.rezirent.Class.LeadClass;
import com.resieasy.rezirent.Class.SingleIDClass;
import com.resieasy.rezirent.R;
import com.resieasy.rezirent.databinding.HostelownsampleBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class HostelOwnAdapter extends RecyclerView.Adapter<HostelOwnAdapter.ViewHolder> {

    Context context;
    ArrayList<SingleIDClass> list;

    InterstitialAd mInterstitialAdedit;
    InterstitialAd mInterstitialAdshow;
    ProgressDialog ad_dialog;

    public HostelOwnAdapter(Context context, ArrayList<SingleIDClass> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public HostelOwnAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hostelownsample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HostelOwnAdapter.ViewHolder holder, int position) {
        SingleIDClass data = list.get(position);
        String id = data.getId();


        ad_dialog=new ProgressDialog(context);
        ad_dialog.setMessage("Ad loading");
        ad_dialog.setCancelable(false);

        AdRequest adRequestedit = new AdRequest.Builder().build();
        AdRequest adRequestshow = new AdRequest.Builder().build();

        InterstitialAd.load(context, String.valueOf(R.string.Ownhosteleditinterstitial_id), adRequestedit,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAdedit = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAdedit = null;
                    }
                });
        InterstitialAd.load(context, String.valueOf(R.string.Ownhostelshowinterstitial_id), adRequestshow,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAdshow = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAdshow = null;
                    }
                });



        FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllImage").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        String firstimage = snapshot.getString("image0");
                        Picasso.get().load(firstimage).placeholder(R.drawable.iplaceholdr).into(holder.binding.srimage);

                    }
                });

        FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllData").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        String name = snapshot.getString("name");
                        String subtype = snapshot.getString("subtype");
                        String area = snapshot.getString("area");
                        String address = snapshot.getString("address");
                        String rent = snapshot.getString("rent");
                        String status = snapshot.getString("status");

                        int agree = snapshot.getLong("period").intValue();
                        int in = snapshot.getLong("in").intValue();

                        holder.binding.imagenumtext.setText(in+" images");



                        holder.binding.srname.setText(name);
                        holder.binding.sraddress.setText(address);
                        holder.binding.samplesubtype.setText(subtype);
                        holder.binding.samplearea.setText(area);
                        holder.binding.rent.setText(rent+"₹/month");

                        if (agree == 708) {
                            holder.binding.noagreeview.setVisibility(View.VISIBLE);
                            holder.binding.yesagreeview.setVisibility(View.GONE);

                        } else {
                            holder.binding.noagreeview.setVisibility(View.GONE);
                            holder.binding.yesagreeview.setVisibility(View.VISIBLE);

                        }
                        if (status.equals("Active")){
                            holder.binding.activestatsview.setVisibility(View.VISIBLE);
                            holder.binding.inactivestatsview.setVisibility(View.GONE);
                        }else {
                            holder.binding.activestatsview.setVisibility(View.GONE);
                            holder.binding.inactivestatsview.setVisibility(View.VISIBLE);
                        }

                    }
                });

        ProgressDialog ddialog=new ProgressDialog(context);
        ddialog.setCancelable(false);
        ddialog.setMessage("Please wait....");
        holder.binding.deletview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore.getInstance().collection("Nanded")
                        .document("NandedCity").collection("AllData").document(id).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot snapshot) {
                                String name = snapshot.getString("name");

                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                                builder.setIcon(R.drawable.warna);
                                builder.setTitle("DELETE --> "+name);
                                builder.setMessage("Are you sure, you want to delete your residency account.");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ddialog.show();

                                        String userid = FirebaseAuth.getInstance().getUid();

                                        FirebaseFirestore.getInstance().collection("OwnResi")
                                                .document(userid).collection("Nanded").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        FirebaseFirestore.getInstance().collection("Nanded")
                                                                .document("NandedCity").collection("AllImage").document(id).delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        FirebaseFirestore.getInstance().collection("Nanded")
                                                                                .document("NandedCity").collection("AllData").document(id)
                                                                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                                                                                                .collection("AllFacility").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(Void unused) {
                                                                                                        FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                                                                                                                .collection("AllRule").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onSuccess(Void unused) {

                                                                                                                        ddialog.dismiss();
                                                                                                                        dialog.dismiss();
                                                                                                                        Intent intent=new Intent(context, ProfileActivity.class);
                                                                                                                        context.startActivity(intent);
                                                                                                                        Toast.makeText(context, "Your residency account deleted successfully", Toast.LENGTH_LONG).show();

                                                                                                                        deletwlike(id);
                                                                                                                        deletelead(id);

                                                                                                                    }
                                                                                                                });
                                                                                                    }
                                                                                                });

                                                                                    }
                                                                                });
                                                                    }
                                                                });
                                                    }
                                                });

                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ddialog.dismiss();
                                        dialog.dismiss();
                                        Toast.makeText(context, "Thanks !!!", Toast.LENGTH_SHORT).show();

                                    }
                                }).setNeutralButton("Help", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ddialog.dismiss();
                                        dialog.dismiss();
                                        Toast.makeText(context, "for delete residency account, press yes", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                builder.show();

                            }
                        });


            }
        });


        holder.binding.editview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAdedit != null) {
                    ad_dialog.show();
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ad_dialog.dismiss();
                            mInterstitialAdedit.show((Activity) context);
                            mInterstitialAdedit.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    mInterstitialAdedit=null;
                                    Intent intent=new Intent(context, EditHostelDataActivity.class);
                                    intent.putExtra("id",id);
                                    context.startActivity(intent);
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    super.onAdFailedToShowFullScreenContent(adError);
                                    mInterstitialAdedit=null;
                                    Intent intent=new Intent(context, EditHostelDataActivity.class);
                                    intent.putExtra("id",id);
                                    context.startActivity(intent);
                                }
                            });
                        }
                    }, 1000);

                }
                else {
                    Intent intent=new Intent(context, EditHostelDataActivity.class);
                    intent.putExtra("id",id);
                    context.startActivity(intent);
                }




            }
        });
        holder.binding.viewview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAdshow != null) {
                    ad_dialog.show();
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ad_dialog.dismiss();
                            mInterstitialAdshow.show((Activity) context);
                            mInterstitialAdshow.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    mInterstitialAdshow=null;
                                    Intent intent=new Intent(context, ShowHostelDataActivity.class);
                                    intent.putExtra("id",id);
                                    context.startActivity(intent);
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    super.onAdFailedToShowFullScreenContent(adError);
                                    mInterstitialAdshow=null;
                                    Intent intent=new Intent(context, ShowHostelDataActivity.class);
                                    intent.putExtra("id",id);
                                    context.startActivity(intent);
                                }
                            });
                        }
                    }, 1000);

                }
                else {
                    Intent intent=new Intent(context, ShowHostelDataActivity.class);
                    intent.putExtra("id",id);
                    context.startActivity(intent);
                }




            }
        });
        holder.binding.activestatsview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("Nanded")
                        .document("NandedCity").collection("AllData").document(id).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot snapshot) {
                                String name = snapshot.getString("name");


                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                                builder.setIcon(R.drawable.warna);
                                builder.setTitle("Change Status --> "+name);
                                builder.setMessage("Are you sure, you want to change residency status.");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ddialog.show();
                                        HashMap<String,Object> hashMap=new HashMap<>();
                                        hashMap.put("status","Inactive");
                                        FirebaseFirestore.getInstance().collection("Nanded")
                                                .document("NandedCity").collection("AllData").document(id)
                                                .update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                        ddialog.dismiss();
                                                        dialog.dismiss();
                                                        holder.binding.activestatsview.setVisibility(View.GONE);
                                                        holder.binding.inactivestatsview.setVisibility(View.VISIBLE);
                                                        Toast.makeText(context, "Status changed successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ddialog.dismiss();
                                        dialog.dismiss();
                                        Toast.makeText(context, "Thanks !!!", Toast.LENGTH_SHORT).show();
                                    }
                                }).setNeutralButton("Help", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ddialog.dismiss();
                                        dialog.dismiss();
                                        Toast.makeText(context, "for change residency status, press yes", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                builder.show();

                            }
                        });


            }
        });
        holder.binding.inactivestatsview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("Nanded")
                        .document("NandedCity").collection("AllData").document(id).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot snapshot) {
                                String name = snapshot.getString("name");


                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                                builder.setIcon(R.drawable.warna);
                                builder.setTitle("Change Status --> "+name);
                                builder.setMessage("Are you sure, you want to change residency status.");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ddialog.show();
                                        HashMap<String,Object> hashMap=new HashMap<>();
                                        hashMap.put("status","Active");
                                        FirebaseFirestore.getInstance().collection("Nanded")
                                                .document("NandedCity").collection("AllData").document(id)
                                                .update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                        ddialog.dismiss();
                                                        dialog.dismiss();
                                                        holder.binding.activestatsview.setVisibility(View.VISIBLE);
                                                        holder.binding.inactivestatsview.setVisibility(View.GONE);
                                                        Toast.makeText(context, "Status changed successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ddialog.dismiss();
                                        dialog.dismiss();
                                    }
                                }).setNeutralButton("Help", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ddialog.dismiss();
                                        dialog.dismiss();
                                        Toast.makeText(context, "for change residency status, press yes", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                builder.show();

                            }
                        });

            }
        });
    }
    private void deletelead(String iddd) {
        FirebaseFirestore.getInstance().collection("Lead").document(FirebaseAuth.getInstance().getUid())
                .collection("Nanded").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot data : queryDocumentSnapshots.getDocuments()) {
                            LeadClass data1 = data.toObject(LeadClass.class);
                            String resiid=data1.getResiid();
                            String uid= data1.getUserid();

                            if (resiid.equals(iddd)){
                                FirebaseFirestore.getInstance().collection("Lead").document(FirebaseAuth.getInstance().getUid())
                                        .collection("Nanded").document(uid+resiid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                            }

                        }


                    }
                });


    }

    private void deletwlike(String idddd) {
        FirebaseFirestore.getInstance().collection("Like").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot data : queryDocumentSnapshots.getDocuments()) {
                    String useridd = data.getString("userid");

                    FirebaseFirestore.getInstance().collection("Like").document(useridd)
                            .collection("Nanded").document(idddd).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            });


                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        HostelownsampleBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = HostelownsampleBinding.bind(itemView);
        }
    }
}