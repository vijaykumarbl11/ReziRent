package com.resieasy.rezirent.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
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

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.resieasy.rezirent.Activity.LeadShowActivity;
import com.resieasy.rezirent.Activity.ProfileActivity;
import com.resieasy.rezirent.Activity.ShowSellDataActivity;
import com.resieasy.rezirent.Class.LeadClass;
import com.resieasy.rezirent.Class.UsersClass;
import com.resieasy.rezirent.R;
import com.resieasy.rezirent.databinding.LayoutAdBinding;
import com.resieasy.rezirent.databinding.LeadshowsampleBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class LeadsAdapter extends RecyclerView.Adapter<LeadsAdapter.ViewHolder> {

    Context context;
    ArrayList<LeadClass> list;
    ProgressDialog ad_dialog;

    InterstitialAd mInterstitialAdcall;
    InterstitialAd mInterstitialAdwhats;

    public LeadsAdapter(Context context, ArrayList<LeadClass> list) {
        this.context = context;
        this.list = list;
    }

    private String getTime(String time, Long timestamp) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(time));
        String timee = new SimpleDateFormat("dd-MM-yy hh:mm aa").format(timestamp);
        return timee;
    }

    @NonNull
    @Override
    public LeadsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view1 = LayoutInflater.from(context).inflate(R.layout.leadshowsample, parent, false);
            return new ViewHolder(view1);

    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ad_dialog=new ProgressDialog(context);
        ad_dialog.setMessage("Ad loading");
        ad_dialog.setCancelable(false);

        AdRequest adRequestcall = new AdRequest.Builder().build();
        AdRequest adRequestwhats = new AdRequest.Builder().build();


        InterstitialAd.load(context, String.valueOf(R.string.Leadcallbtnintertitial_id), adRequestcall,
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
        InterstitialAd.load(context, String.valueOf(R.string.Leadwhatsappbtninterstitial_id), adRequestwhats,
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



        LeadClass data = list.get(position);

            String userid = data.getUserid();
            String resiid = data.getResiid();
            Long timestamp = data.getTime();
            String tt = String.valueOf(timestamp);

            holder.binding.datetext.setText(getTime(tt, timestamp));

            FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                    .collection("AllData").document(resiid).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(DocumentSnapshot snapshot) {
                            String rtype = snapshot.getString("rtype");

                            if (rtype.equals("Rent")) {
                                String name = snapshot.getString("name");
                                holder.binding.resiname.setText("Residency Name : " + name);

                            } else if (rtype.equals("Sell")) {
                                String name = snapshot.getString("name");
                                holder.binding.resiname.setText("Property Name : " + name);

                            } else if (rtype.equals("Hostel")) {
                                String name = snapshot.getString("name");
                                holder.binding.resiname.setText("Hostel/PG Name : " + name);

                            }

                        }
                    });


            FirebaseFirestore.getInstance().collection("AllUser").document(userid).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot snapshot) {

                            UsersClass data1 = snapshot.toObject(UsersClass.class);
                            String name = data1.getName();
                            String number = data1.getNumber();
                            holder.binding.profilename.setText(name);
                            holder.binding.profilenumber.setText(number);


                            holder.binding.callbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (mInterstitialAdcall != null) {
                                        ad_dialog.show();
                                        Handler handler = new Handler(Looper.getMainLooper());
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                ad_dialog.dismiss();
                                                mInterstitialAdcall.show((Activity) context);
                                                mInterstitialAdcall.setFullScreenContentCallback(new FullScreenContentCallback() {
                                                    @Override
                                                    public void onAdDismissedFullScreenContent() {
                                                        super.onAdDismissedFullScreenContent();
                                                        mInterstitialAdcall=null;
                                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                                        intent.setData(Uri.parse("tel:" + number));
                                                        context.startActivity(intent);
                                                    }

                                                    @Override
                                                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                                        super.onAdFailedToShowFullScreenContent(adError);
                                                        mInterstitialAdcall=null;
                                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                                        intent.setData(Uri.parse("tel:" + number));
                                                        context.startActivity(intent);
                                                    }
                                                });
                                            }
                                        }, 1000);

                                    }
                                    else {
                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                        intent.setData(Uri.parse("tel:" + number));
                                        context.startActivity(intent);
                                    }



                                }
                            });

                            holder.binding.whatsappbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (mInterstitialAdwhats != null) {
                                        ad_dialog.show();
                                        Handler handler = new Handler(Looper.getMainLooper());
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                ad_dialog.dismiss();
                                                mInterstitialAdwhats.show((Activity) context);
                                                mInterstitialAdwhats.setFullScreenContentCallback(new FullScreenContentCallback() {
                                                    @Override
                                                    public void onAdDismissedFullScreenContent() {
                                                        super.onAdDismissedFullScreenContent();
                                                        mInterstitialAdwhats=null;
                                                        String wn = "https://wa.me/+917028297606?text= Hi is anyone available?";
                                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                                        intent.setData(Uri.parse("https://wa.me/+91" + number + "?text=  Hey hi, have you visited my property on ResiEasy ?"));
                                                        context.startActivity(intent);
                                                    }

                                                    @Override
                                                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                                        super.onAdFailedToShowFullScreenContent(adError);
                                                        mInterstitialAdwhats=null;
                                                        String wn = "https://wa.me/+917028297606?text= Hi is anyone available?";
                                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                                        intent.setData(Uri.parse("https://wa.me/+91" + number + "?text=  Hey hi, have you visited my property on ResiEasy ?"));
                                                        context.startActivity(intent);
                                                    }
                                                });
                                            }
                                        }, 1000);

                                    }
                                    else {
                                        String wn = "https://wa.me/+917028297606?text= Hi is anyone available?";
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse("https://wa.me/+91" + number + "?text=  Hey hi, have you visited my property on ResiEasy ?"));
                                        context.startActivity(intent);
                                    }


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
        LeadshowsampleBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = LeadshowsampleBinding.bind(itemView);
        }
    }




}
