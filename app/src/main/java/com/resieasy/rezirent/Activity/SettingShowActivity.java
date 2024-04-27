package com.resieasy.rezirent.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.resieasy.rezirent.R;
import com.resieasy.rezirent.databinding.ActivitySettingShowBinding;

public class SettingShowActivity extends AppCompatActivity {
    ActivitySettingShowBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySettingShowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


       String type=getIntent().getStringExtra("type");

       if (type.equals("contactus")){
           binding.contacusview.setVisibility(View.VISIBLE);
       }
       if (type.equals("aboutus")){
           binding.aboutusview.setVisibility(View.VISIBLE);
       }
       if (type.equals("pp")){
           binding.privacypolicyview.setVisibility(View.VISIBLE);
       }
       if (type.equals("tc")){
           binding.termconditionview.setVisibility(View.VISIBLE);
       }

        binding.contactuscontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:7028297606"));
                startActivity(intent);

            }
        });

        binding.contactuswhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wn="https://wa.me/+917028297606?text= Hi is anyone available?";
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(wn));
                startActivity(intent);

            }
        });

        binding.contactusemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "help@resieasy.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "I have problem in ResiEasy Application");
                    intent.putExtra(Intent.EXTRA_TEXT, "Write your problem");
                    startActivity(intent);
                } catch (ActivityNotFoundException e){
                    Toast.makeText(SettingShowActivity.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });





    }
}