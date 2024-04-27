package com.resieasy.rezirent.Adapter;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.resieasy.rezirent.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MultioldImageAdapter extends RecyclerView.Adapter<MultioldImageAdapter.ViewHolder> {


    ArrayList<Uri> uriArrylist;

    public MultioldImageAdapter(ArrayList<Uri> uriArrylist) {
        this.uriArrylist = uriArrylist;
    }
    @NonNull
    @Override
    public MultioldImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.multiimage,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MultioldImageAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Picasso.get().load(uriArrylist.get(position)).into(holder.imageView1);

        holder.imageView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uriArrylist.remove(uriArrylist.get(position));
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,getItemCount());
            }
        });

    }

    @Override
    public int getItemCount() {
        return uriArrylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView1;
        ImageView imageView12;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView1= itemView.findViewById(R.id.image);
            imageView12= itemView.findViewById(R.id.deleteimagebtn);
        }
    }
}
