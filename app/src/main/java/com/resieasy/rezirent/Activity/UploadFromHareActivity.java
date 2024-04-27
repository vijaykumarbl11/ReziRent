package com.resieasy.rezirent.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.resieasy.rezirent.R;
import com.resieasy.rezirent.databinding.ActivityUploadFromHareBinding;

public class UploadFromHareActivity extends AppCompatActivity {

    ActivityUploadFromHareBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadFromHareBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.resntview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadFromHareActivity.this, AddResidencyActivity.class);
                startActivity(intent);

            }
        });
        binding.sellview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadFromHareActivity.this, AddSellResiActivity.class);
                startActivity(intent);


            }
        });

        binding.cotbaseview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadFromHareActivity.this, AddHostelActivity.class);
                startActivity(intent);

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