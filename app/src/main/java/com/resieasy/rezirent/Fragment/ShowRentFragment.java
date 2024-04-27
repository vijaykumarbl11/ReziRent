package com.resieasy.rezirent.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

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
import com.resieasy.rezirent.Adapter.ResiShowOwnerAdapter;
import com.resieasy.rezirent.Class.SingleIDClass;
import com.resieasy.rezirent.databinding.FragmentShowRentBinding;

import java.util.ArrayList;
import java.util.List;


public class ShowRentFragment extends Fragment {

    FragmentShowRentBinding binding;



    public ShowRentFragment() {
        // Required empty public constructor
    }


    ProgressDialog dialog;




    ArrayList<SingleIDClass> list = new ArrayList<>();
    ResiShowOwnerAdapter adapter12;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShowRentBinding.inflate(inflater, container, false);


        adapter12 = new ResiShowOwnerAdapter(list, getContext());
        binding.showrentrec.setAdapter(adapter12);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        binding.showrentrec.setLayoutManager(manager);



        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Fetching Data . . . .");
        dialog.setCancelable(false);

        binding.progressBar.setVisibility(View.VISIBLE);

        Query query=FirebaseFirestore.getInstance().collection("OwnResi").document(FirebaseAuth.getInstance().getUid())
                .collection("Nanded")
                .whereEqualTo("type", "Rent")
                .orderBy("time", Query.Direction.DESCENDING);


        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                binding.showrentswip.setVisibility(View.VISIBLE);
                                binding.adddataframe.setVisibility(View.GONE);
                                binding.progressBar.setVisibility(View.GONE);

                                list.clear();
                                for (DocumentSnapshot data : queryDocumentSnapshots.getDocuments()) {
                                    SingleIDClass data1 = data.toObject(SingleIDClass.class);
                                    list.add(data1);
                                }
                                adapter12.notifyDataSetChanged();
                                dialog.dismiss();
                            } else {
                                binding.adddataframe.setVisibility(View.VISIBLE);
                                binding.progressBar.setVisibility(View.GONE);

                                Toast.makeText(getContext(), "No Data Available", Toast.LENGTH_SHORT).show();

                            }


                        }
                    });








        binding.showrentswip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Query query=FirebaseFirestore.getInstance().collection("OwnResi").document(FirebaseAuth.getInstance().getUid())
                        .collection("Nanded")
                        .whereEqualTo("type", "Rent")
                        .orderBy("time", Query.Direction.DESCENDING);


                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            binding.showrentswip.setVisibility(View.VISIBLE);
                            binding.adddataframe.setVisibility(View.GONE);
                            binding.progressBar.setVisibility(View.GONE);

                            list.clear();
                            List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot snapshot : snapshotList) {

                                SingleIDClass list01 = snapshot.toObject(SingleIDClass.class);
                                list.add(list01);
                            }
                            adapter12.notifyDataSetChanged();
                            Toast.makeText(getContext(), "Refresh Data", Toast.LENGTH_SHORT).show();

                        } else {
                            binding.adddataframe.setVisibility(View.VISIBLE);
                            binding.progressBar.setVisibility(View.GONE);

                            Toast.makeText(getContext(), "No Data Available", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
                binding.showrentswip.setRefreshing(false);
            }
        });


        binding.adddataimage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddResidencyActivity.class);
                startActivity(intent);
            }
        });


        return binding.getRoot();
    }
}