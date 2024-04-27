package com.resieasy.rezirent.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.resieasy.rezirent.Adapter.BothResiiAdapter;
import com.resieasy.rezirent.Class.BothResiClass;
import com.resieasy.rezirent.R;
import com.resieasy.rezirent.databinding.ActivityBothResiiBinding;

import java.util.ArrayList;

public class BothResiiActivity extends AppCompatActivity {

    ActivityBothResiiBinding binding;

    ArrayList<BothResiClass> list = new ArrayList<>();
    BothResiiAdapter adapter12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBothResiiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        adapter12 = new BothResiiAdapter(list, BothResiiActivity.this);
        binding.bothrec.setAdapter(adapter12);
        LinearLayoutManager manager = new LinearLayoutManager(BothResiiActivity.this);
        binding.bothrec.setLayoutManager(manager);


        String querytype = getIntent().getStringExtra("topquery");
        //All,Rent,Sell,Hostel



        if (querytype.equals("All")) {
            binding.bothshimmer.setVisibility(View.VISIBLE);
            binding.bothshimmer.startShimmer();
            Toast.makeText(this, "All Residency", Toast.LENGTH_SHORT).show();
            FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                    .collection("AllData").whereEqualTo("status", "Active")
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            list.clear();
                            for (DocumentSnapshot data : queryDocumentSnapshots.getDocuments()) {
                                BothResiClass data1 = data.toObject(BothResiClass.class);
                                list.add(data1);
                            }
                            adapter12.notifyDataSetChanged();
                           // dialog.dismiss();
                            binding.bothshimmer.setVisibility(View.GONE);
                            binding.bothshimmer.stopShimmer();

                        }
                    });

        }
        if (querytype.equals("Rent")) {
            binding.bothshimmer.startShimmer();
            binding.bothshimmer.setVisibility(View.VISIBLE);

            Toast.makeText(this, "Rental Residency", Toast.LENGTH_SHORT).show();
            FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                    .collection("AllData").whereEqualTo("rtype", "Rent").whereEqualTo("status", "Active")
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            list.clear();
                            for (DocumentSnapshot data : queryDocumentSnapshots.getDocuments()) {
                                BothResiClass data1 = data.toObject(BothResiClass.class);
                                list.add(data1);


                            }
                            adapter12.notifyDataSetChanged();

                            binding.bothshimmer.setVisibility(View.GONE);
                            binding.bothshimmer.stopShimmer();

                        }
                    });
        }
        if (querytype.equals("Sell")) {
            binding.bothshimmer.startShimmer();
            binding.bothshimmer.setVisibility(View.VISIBLE);

            Toast.makeText(this, "Property On Sell", Toast.LENGTH_SHORT).show();
            FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                    .collection("AllData").whereEqualTo("rtype", "Sell")
                    .whereEqualTo("status", "Active").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            list.clear();
                            for (DocumentSnapshot data : queryDocumentSnapshots.getDocuments()) {
                                BothResiClass data1 = data.toObject(BothResiClass.class);
                                list.add(data1);
                            }
                            adapter12.notifyDataSetChanged();
                            binding.bothshimmer.setVisibility(View.GONE);
                            binding.bothshimmer.stopShimmer();

                        }
                    });
        }
        if (querytype.equals("Hostel")) {
            binding.bothshimmer.startShimmer();
            binding.bothshimmer.setVisibility(View.VISIBLE);

            Toast.makeText(this, "Cot-Base Residency", Toast.LENGTH_SHORT).show();
            FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                    .collection("AllData").whereEqualTo("rtype", "Hostel")
                    .whereEqualTo("status", "Active").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            list.clear();
                            for (DocumentSnapshot data : queryDocumentSnapshots.getDocuments()) {
                                BothResiClass data1 = data.toObject(BothResiClass.class);
                                list.add(data1);
                            }
                            adapter12.notifyDataSetChanged();
                            binding.bothshimmer.setVisibility(View.GONE);
                            binding.bothshimmer.stopShimmer();

                        }
                    });

        }

        binding.showrentswip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (querytype.equals("All")) {
                    Toast.makeText(BothResiiActivity.this, "All Residency", Toast.LENGTH_SHORT).show();
                    FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                            .collection("AllData").whereEqualTo("status", "Active")
                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    list.clear();
                                    for (DocumentSnapshot data : queryDocumentSnapshots.getDocuments()) {
                                        BothResiClass data1 = data.toObject(BothResiClass.class);
                                        list.add(data1);
                                    }
                                    adapter12.notifyDataSetChanged();

                                }
                            });

                }
                if (querytype.equals("Rent")) {

                    Toast.makeText(BothResiiActivity.this, "Rent", Toast.LENGTH_SHORT).show();
                    FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                            .collection("AllData").whereEqualTo("rtype", "Rent").whereEqualTo("status", "Active")
                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    list.clear();
                                    for (DocumentSnapshot data : queryDocumentSnapshots.getDocuments()) {
                                        BothResiClass data1 = data.toObject(BothResiClass.class);
                                        list.add(data1);


                                    }
                                    adapter12.notifyDataSetChanged();

                                }
                            });
                }
                if (querytype.equals("Sell")) {

                    Toast.makeText(BothResiiActivity.this, "Sell", Toast.LENGTH_SHORT).show();
                    FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                            .collection("AllData").whereEqualTo("rtype", "Sell")
                            .whereEqualTo("status", "Active").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    list.clear();
                                    for (DocumentSnapshot data : queryDocumentSnapshots.getDocuments()) {
                                        BothResiClass data1 = data.toObject(BothResiClass.class);
                                        list.add(data1);
                                    }
                                    adapter12.notifyDataSetChanged();

                                }
                            });
                }
                if (querytype.equals("Hostel")) {

                    Toast.makeText(BothResiiActivity.this, "Hostel", Toast.LENGTH_SHORT).show();
                    FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                            .collection("AllData").whereEqualTo("rtype", "Hostel")
                            .whereEqualTo("status", "Active").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    list.clear();
                                    for (DocumentSnapshot data : queryDocumentSnapshots.getDocuments()) {
                                        BothResiClass data1 = data.toObject(BothResiClass.class);
                                        list.add(data1);
                                    }
                                    adapter12.notifyDataSetChanged();

                                }
                            });

                }

                binding.showrentswip.setRefreshing(false);
                Toast.makeText(BothResiiActivity.this, "Data Refresh", Toast.LENGTH_SHORT).show();
            }
        });


        binding.searchview1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {


                Query querySearch = FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                        .collection("AllData")//.whereEqualTo("status", "Active")
                        .orderBy("lowercase").startAt(text);

                querySearch.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        list.clear();
                        for (DocumentSnapshot data : queryDocumentSnapshots.getDocuments()) {
                            BothResiClass data1 = data.toObject(BothResiClass.class);
                            String status=data1.getStatus();
                            if (status.equals("Active")){
                                list.add(data1);

                            }
                        }
                        adapter12.notifyDataSetChanged();

                    }
                });
                return false;
            }
        });

        binding.homebottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BothResiiActivity.this, MainActivity.class);

                startActivity(intent);
                finish();
            }
        });


        binding.sortbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = findViewById(android.R.id.content);

                LinearLayout drent, dsell, dhostel, dall;


                AlertDialog.Builder builder = new AlertDialog.Builder(BothResiiActivity.this);
                View view = LayoutInflater.from(BothResiiActivity.this).inflate(R.layout.bothsortdialog, viewGroup, false);
                builder.setCancelable(true);
                builder.setView(view);

                drent = view.findViewById(R.id.rentsdialog);
                dsell = view.findViewById(R.id.sellsdialog);
                dhostel = view.findViewById(R.id.hoostelsdialog);
                dall = view.findViewById(R.id.allsdialog);


                AlertDialog alertDialog1 = builder.create();
                alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                drent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog1.dismiss();
                        binding.bothshimmer.startShimmer();
                        binding.bothshimmer.setVisibility(View.VISIBLE);

                        FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                                .collection("AllData").whereEqualTo("rtype", "Rent")
                                .whereEqualTo("status", "Active").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        list.clear();
                                        for (DocumentSnapshot data : queryDocumentSnapshots.getDocuments()) {
                                            BothResiClass data1 = data.toObject(BothResiClass.class);
                                            list.add(data1);
                                        }
                                        adapter12.notifyDataSetChanged();
                                        binding.bothshimmer.setVisibility(View.GONE);
                                        binding.bothshimmer.stopShimmer();

                                    }
                                });
                    }
                });
                dsell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog1.dismiss();
                        binding.bothshimmer.startShimmer();
                        binding.bothshimmer.setVisibility(View.VISIBLE);
                        FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                                .collection("AllData").whereEqualTo("rtype", "Sell")
                                .whereEqualTo("status", "Active").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        list.clear();
                                        for (DocumentSnapshot data : queryDocumentSnapshots.getDocuments()) {
                                            BothResiClass data1 = data.toObject(BothResiClass.class);
                                            list.add(data1);
                                        }
                                        adapter12.notifyDataSetChanged();
                                        binding.bothshimmer.setVisibility(View.GONE);
                                        binding.bothshimmer.stopShimmer();


                                    }
                                });

                    }
                });
                dhostel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog1.dismiss();
                        binding.bothshimmer.startShimmer();
                        binding.bothshimmer.setVisibility(View.VISIBLE);

                        FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                                .collection("AllData").whereEqualTo("rtype", "Hostel")
                                .whereEqualTo("status", "Active").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        list.clear();
                                        for (DocumentSnapshot data : queryDocumentSnapshots.getDocuments()) {
                                            BothResiClass data1 = data.toObject(BothResiClass.class);
                                            list.add(data1);
                                        }
                                        adapter12.notifyDataSetChanged();
                                        binding.bothshimmer.setVisibility(View.GONE);
                                        binding.bothshimmer.stopShimmer();

                                    }
                                });

                    }
                });
                dall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog1.dismiss();
                        binding.bothshimmer.startShimmer();
                        binding.bothshimmer.setVisibility(View.VISIBLE);

                        FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                                .collection("AllData").whereEqualTo("status", "Active")
                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        list.clear();
                                        for (DocumentSnapshot data : queryDocumentSnapshots.getDocuments()) {
                                            BothResiClass data1 = data.toObject(BothResiClass.class);
                                            list.add(data1);
                                        }
                                        adapter12.notifyDataSetChanged();
                                        binding.bothshimmer.setVisibility(View.GONE);
                                        binding.bothshimmer.stopShimmer();

                                    }
                                });
                    }
                });

                alertDialog1.show();
            }
        });


    }


}