package com.resieasy.rezirent.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.resieasy.rezirent.Adapter.LeadsAdapter;
import com.resieasy.rezirent.Class.LeadClass;
import com.resieasy.rezirent.R;
import com.resieasy.rezirent.databinding.ActivityLeadShowBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class LeadShowActivity extends AppCompatActivity {

    ActivityLeadShowBinding binding;
    LeadsAdapter leadsAdapter;
    ArrayList<LeadClass> list;
    String count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLeadShowBinding.inflate(getLayoutInflater());



        setContentView(binding.getRoot());
        list=new ArrayList<>();
        leadsAdapter=new LeadsAdapter(this,list);
        binding.leadsrec.setAdapter(leadsAdapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        binding.leadsrec.setLayoutManager(layoutManager);

        binding.leadshimmer.setVisibility(View.VISIBLE);
        binding.leadshimmer.startShimmer();

        FirebaseFirestore.getInstance().collection("Lead").document(FirebaseAuth.getInstance().getUid())
                .collection("Nanded").orderBy("time", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){

                            binding.leadshimmer.setVisibility(View.GONE);
                            binding.leadshimmer.stopShimmer();
                            list.clear();
                            for (DocumentSnapshot data:queryDocumentSnapshots.getDocuments()){
                                LeadClass data1=data.toObject(LeadClass.class);
                                list.add(data1);
                            }

                            leadsAdapter.notifyDataSetChanged();
                            count= String.valueOf(queryDocumentSnapshots.size());

                            binding.leadcount.setText(count);

                        }else {
                            binding.leadshimmer.setVisibility(View.GONE);
                            binding.leadshimmer.stopShimmer();
                            Toast.makeText(LeadShowActivity.this, "Leads not available", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}