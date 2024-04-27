package com.resieasy.rezirent.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.resieasy.rezirent.Activity.AddResidencyActivity;
import com.resieasy.rezirent.Activity.AddSellResiActivity;
import com.resieasy.rezirent.Activity.ProfileActivity;
import com.resieasy.rezirent.Adapter.ResiShowOwnerAdapter;
import com.resieasy.rezirent.Adapter.ShowSellOwnAdapter;
import com.resieasy.rezirent.Class.SingleIDClass;
import com.resieasy.rezirent.R;
import com.resieasy.rezirent.databinding.FragmentShowSellBinding;

import java.util.ArrayList;
import java.util.List;


public class ShowSellFragment extends Fragment {



    public ShowSellFragment() {
        // Required empty public constructor
    }
    FragmentShowSellBinding binding;


    ArrayList<SingleIDClass> list123=new ArrayList<>();
    ShowSellOwnAdapter adapter123;

    ProgressDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentShowSellBinding.inflate(inflater,container,false);

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Fetching Data . . . .");
        dialog.setCancelable(false);

        binding.progressBar.setVisibility(View.VISIBLE);



        adapter123=new ShowSellOwnAdapter(getContext(),list123);
        binding.showsellrec.setAdapter(adapter123);
        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        binding.showsellrec.setLayoutManager(manager);

        FirebaseAuth auth1=FirebaseAuth.getInstance();
        String userid11=auth1.getUid();

        Query query=FirebaseFirestore.getInstance().collection("OwnResi").document(userid11)
                .collection("Nanded")
                .whereEqualTo("type", "Sell")
                .orderBy("time", Query.Direction.DESCENDING);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            binding.showsellswip.setVisibility(View.VISIBLE);
                            binding.adddataframe.setVisibility(View.GONE);
                            binding.progressBar.setVisibility(View.GONE);
                            list123.clear();
                            List<DocumentSnapshot> snapshotList=queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot snapshot:snapshotList){

                                SingleIDClass list01=snapshot.toObject(SingleIDClass.class);
                                list123.add(list01);
                            }
                            adapter123.notifyDataSetChanged();

                        }else {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.adddataframe.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), "No Data Available", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        binding.showsellswip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Query query=FirebaseFirestore.getInstance().collection("OwnResi").document(userid11)
                        .collection("Nanded")
                        .whereEqualTo("type", "Sell");
                        //.orderBy("time", Query.Direction.DESCENDING);

                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            binding.showsellswip.setVisibility(View.VISIBLE);
                            binding.adddataframe.setVisibility(View.GONE);
                            binding.progressBar.setVisibility(View.GONE);

                            list123.clear();
                            List<DocumentSnapshot> snapshotList=queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot snapshot:snapshotList){

                                SingleIDClass list01=snapshot.toObject(SingleIDClass.class);
                                list123.add(list01);
                            }
                            adapter123.notifyDataSetChanged();
                            Toast.makeText(getContext(), "Refresh Data", Toast.LENGTH_SHORT).show();

                        }else {
                            binding.adddataframe.setVisibility(View.VISIBLE);
                            binding.progressBar.setVisibility(View.GONE);

                            Toast.makeText(getContext(), "No Data Available", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                binding.showsellswip.setRefreshing(false);

            }
        });
        binding.adddataimage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddSellResiActivity.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }
}