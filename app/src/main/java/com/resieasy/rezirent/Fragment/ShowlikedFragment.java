package com.resieasy.rezirent.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.resieasy.rezirent.Adapter.LikeAdapter;
import com.resieasy.rezirent.Class.LikeClass;
import com.resieasy.rezirent.Class.SingleIDClass;
import com.resieasy.rezirent.R;
import com.resieasy.rezirent.databinding.FragmentShowlikedBinding;

import java.util.ArrayList;
import java.util.List;


public class ShowlikedFragment extends Fragment {



    public ShowlikedFragment() {
        // Required empty public constructor
    }
    FragmentShowlikedBinding binding;

    ArrayList<LikeClass> list=new ArrayList<>();
    LikeAdapter likeAdapter;
    ProgressDialog dialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentShowlikedBinding.inflate(inflater,container,false);


        likeAdapter=new LikeAdapter(getContext(),list);
        binding.likerec.setAdapter(likeAdapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        binding.likerec.setLayoutManager(layoutManager);


        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Fetching Data . . . .");
        dialog.setCancelable(false);

        binding.progressBar.setVisibility(View.VISIBLE);

        FirebaseFirestore.getInstance().collection("Like")
                .document(FirebaseAuth.getInstance().getUid()).collection("Nanded")
                .orderBy("time", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {
                            binding.likerec.setVisibility(View.VISIBLE);
                            binding.adddataframe.setVisibility(View.GONE);
                            binding.progressBar.setVisibility(View.GONE);

                            list.clear();
                            List<DocumentSnapshot> snapshotList=queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot data:snapshotList){
                                LikeClass data1=data.toObject(LikeClass.class);
                                list.add(data1);
                            }
                            likeAdapter.notifyDataSetChanged();

                        } else {
                            binding.adddataframe.setVisibility(View.VISIBLE);
                            binding.progressBar.setVisibility(View.GONE);

                            Toast.makeText(getContext(), "No Data Available", Toast.LENGTH_SHORT).show();

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Something is wrong", Toast.LENGTH_SHORT).show();

                    }
                });

        binding.showrentswip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FirebaseFirestore.getInstance().collection("Like")
                        .document(FirebaseAuth.getInstance().getUid()).collection("Nanded")
                        .orderBy("time", Query.Direction.DESCENDING)
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                if (!queryDocumentSnapshots.isEmpty()) {
                                    binding.likerec.setVisibility(View.VISIBLE);
                                    binding.adddataframe.setVisibility(View.GONE);
                                    binding.progressBar.setVisibility(View.GONE);

                                    list.clear();
                                    List<DocumentSnapshot> snapshotList=queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot data:snapshotList){
                                        LikeClass data1=data.toObject(LikeClass.class);
                                        list.add(data1);
                                    }
                                    likeAdapter.notifyDataSetChanged();
                                    Toast.makeText(getContext(), "Data Refresh", Toast.LENGTH_SHORT).show();

                                } else {
                                    binding.adddataframe.setVisibility(View.VISIBLE);
                                    binding.progressBar.setVisibility(View.GONE);

                                    Toast.makeText(getContext(), "No Data Available", Toast.LENGTH_SHORT).show();

                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Something is wrong", Toast.LENGTH_SHORT).show();

                            }
                        });
                binding.showrentswip.setRefreshing(false);
            }
        });






        return binding.getRoot();
    }
}