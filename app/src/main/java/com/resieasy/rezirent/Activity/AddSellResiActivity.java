package com.resieasy.rezirent.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.resieasy.rezirent.Adapter.MultipleImageAdapter;
import com.resieasy.rezirent.Class.SellResiClass;
import com.resieasy.rezirent.R;
import com.resieasy.rezirent.Class.SingleIDClass;
import com.resieasy.rezirent.databinding.ActivityAddSellResiBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddSellResiActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ActivityAddSellResiBinding binding;
    double latitude, longitude;

    String rentaltype[], areatype[];

    int PICK_IMG = 123, in;
    HashMap<String, String> hashMap;


    MultipleImageAdapter multipleImageAdapter;
    ProgressDialog dialog, dialog1;


    //new
    private static final int PICK_IMAGE = 1;


    private int upload_count = 0;

    ArrayList<Uri> newlist = new ArrayList<>();
    Uri newuri;
    int mainum = 0;

    ArrayList newStrings = new ArrayList<>();

     private MaterialTimePicker picker;
    Calendar calendar;
    String type,subtype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddSellResiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        dialog = new ProgressDialog(this);
        dialog.setTitle("Creating Account");
        dialog.setMessage("Please wait, we are creating your property account");
        dialog.setCancelable(false);

        hashMap = new HashMap<>();

        dialog1 = new ProgressDialog(this);
        dialog1.setMessage("Fetching current location....");
        dialog1.setCancelable(false);





        binding.justrehds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Sell, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.selltype.setAdapter(adapter);
        binding.selltype.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) AddSellResiActivity.this);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.Area, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.areatype.setAdapter(adapter1);
        binding.areatype.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) AddSellResiActivity.this);


        multipleImageAdapter = new MultipleImageAdapter(newlist);
        binding.multiimagerec.setLayoutManager(new GridLayoutManager(AddSellResiActivity.this, 3));
        binding.multiimagerec.setAdapter(multipleImageAdapter);

        binding.selltype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item.toString().equals("Flat")){
                    binding.flatview1.setVisibility(View.VISIBLE);
                    binding.roomview1.setVisibility(View.GONE);


                } else if (item.toString().equals("Room")) {
                    binding.flatview1.setVisibility(View.GONE);
                    binding.roomview1.setVisibility(View.VISIBLE);

                }else {
                    binding.flatview1.setVisibility(View.GONE);
                    binding.roomview1.setVisibility(View.GONE);

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.opengallerybtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_IMAGE);
                binding.currentimageview.setVisibility(View.VISIBLE);
            }
        });
        binding.capturelocation2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkpermission();
                dialog1.show();
            }
        });  binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.mapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddSellResiActivity.this, MapsActivity.class);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                intent.putExtra("name","Your residency name will fetch here");
                startActivity(intent);

            }
        });
        binding.submitresibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

                String type=binding.selltype.getSelectedItem().toString();



                if (type.equals("Flat")) {
                    binding.flatview1.setVisibility(View.VISIBLE);
                    int ID01 = binding.flatview1.getCheckedRadioButtonId();
                    RadioButton radioButton01 = findViewById(ID01);

                    if (radioButton01.getText().equals("1RK")) {
                        subtype = "1RK";

                    }
                    if (radioButton01.getText().equals("1BHK")) {
                        subtype = "1BHK";
                    }
                    if (radioButton01.getText().equals("2BHK")) {
                        subtype = "2BHK";
                    }
                    if (radioButton01.getText().equals("3BHK")) {
                        subtype = "3BHK";
                    }
                    if (radioButton01.getText().equals("4BHK")) {
                        subtype = "4BHK";
                    }
                    if (radioButton01.getText().equals("5BHK")) {
                        subtype = "5BHK";
                    }
                } else if (type.equals("Room")) {
                    binding.roomview1.setVisibility(View.VISIBLE);
                    int ID02 = binding.roomview1.getCheckedRadioButtonId();
                    RadioButton radioButton02 = findViewById(ID02);

                    if (radioButton02.getText().equals("Single Room")) {
                        subtype = "Single Room";

                    }
                    if (radioButton02.getText().equals("Double Room")) {
                        subtype = "Double Room";
                    }
                    if (radioButton02.getText().equals("Triple Room")) {
                        subtype = "Triple Room";
                    }
                } else if (type.equals("Stutter")) {
                    subtype = "Shutter";

                } else if (type.equals("House")) {
                    subtype = "House";

                } else if (type.equals("Building")) {
                    subtype = "Building";

                }else if (type.equals("Land")) {
                    subtype = "Land";

                }


                String nametext = binding.resiname.getText().toString();
                String addresstext = binding.resiaddress.getText().toString();
                //String locationtext = binding.showlocationtext.getText().toString();
                String onametext = binding.oname.getText().toString();
                String contacttext = binding.contact.getText().toString();
                String whatsapptext = binding.whatsapp.getText().toString();


                if (!nametext.isEmpty() && !addresstext.isEmpty()&& !onametext.isEmpty() && !contacttext.isEmpty() && !whatsapptext.isEmpty() ) {



                    if (newlist.isEmpty()) {
                        dialog.dismiss();
                        Toast.makeText(AddSellResiActivity.this, "Please select images", Toast.LENGTH_SHORT).show();

                    }
                    else if (!newlist.isEmpty()) {

                        if (newlist.size() < 11) {
                            mainum = newlist.size();
                            binding.numbertext.setText("If Loading Takes to long press button again");
                            StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("Nanded")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child("SellImage");

                            for (upload_count = 0; upload_count < newlist.size(); upload_count++) {

                                Uri IndividualImage = (Uri) newlist.get(upload_count);
                                final StorageReference ImageName = ImageFolder.child("Images" + IndividualImage.getLastPathSegment());



                                Bitmap bmp = null;
                                try {
                                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), IndividualImage);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bmp.compress(Bitmap.CompressFormat.JPEG, 40, baos);
                                byte[] data = baos.toByteArray();
                                UploadTask uploadTask2 = ImageName.putBytes(data);

                                uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot task) {
                                        ImageName.getDownloadUrl().addOnSuccessListener(
                                                new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        newStrings.add(String.valueOf(uri));


                                                        if (newStrings.size() == newlist.size()) {
                                                            storeLink(newStrings,mainum,type,subtype);
                                                        }

                                                    }
                                                }
                                        );

                                    }
                                });


                            }



                        }
                        else {
                            Toast.makeText(AddSellResiActivity.this, "Please don't select more than 10 images", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }



                    }

                }
                else {
                    if (nametext.isEmpty()){
                        dialog.dismiss();

                        binding.resiname.setError("Please enter residency name");
                        Toast.makeText(AddSellResiActivity.this, "Please enter residency name", Toast.LENGTH_SHORT).show();

                    }
                    if (addresstext.isEmpty()){
                        dialog.dismiss();

                        binding.resiaddress.setError("Please enter residency address");
                        Toast.makeText(AddSellResiActivity.this, "Please enter residency address", Toast.LENGTH_SHORT).show();

                    }

                    if (onametext.isEmpty()){
                        dialog.dismiss();

                        binding.oname.setError("Please enter residency operator name");
                        Toast.makeText(AddSellResiActivity.this, "Please enter residency operator name", Toast.LENGTH_SHORT).show();

                    }
                    if (contacttext.isEmpty()){
                        dialog.dismiss();

                        binding.contact.setError("Please enter contact number");
                        Toast.makeText(AddSellResiActivity.this, "Please enter contact number", Toast.LENGTH_SHORT).show();

                    }
                    if (whatsapptext.isEmpty()){
                        dialog.dismiss();

                        binding.whatsapp.setError("Please enter whatsapp number");
                        Toast.makeText(AddSellResiActivity.this, "Please enter whatsapp number", Toast.LENGTH_SHORT).show();

                    }


                }

            }
        });


    }






    private void storeLink(ArrayList<String> newStrings, int mainum, String typex, String subtypex) {

        HashMap<String, String> hashMap = new HashMap<>();
        int i;

        for (i = 0; i < newlist.size(); i++) {

            hashMap.put("image" + i, newStrings.get(i));

        }

        String name = binding.resiname.getText().toString();
        String address = binding.resiaddress.getText().toString();
        String area = binding.areatype.getSelectedItem().toString();
        String oname = binding.oname.getText().toString();
        String contact = binding.contact.getText().toString();
        String whatsapp = binding.whatsapp.getText().toString();
        String mail = binding.email.getText().toString();
        String rent = binding.rentamount.getText().toString();
        String erent = binding.explainrent.getText().toString();
        String more = binding.moredetails.getText().toString();
        String size = binding.propertysize.getText().toString();
        String userid1 = FirebaseAuth.getInstance().getUid();


        CollectionReference toolsCollectionRef = FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllImage");
        CollectionReference toolsCollectionRef2 = FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllData");
        CollectionReference toolsCollectionRef3 = FirebaseFirestore.getInstance().collection("OwnResi")
                .document(userid1).collection("Nanded");
        String newDocID = toolsCollectionRef.document().getId();

        toolsCollectionRef.document(newDocID).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Date date=new Date();
                SellResiClass data = new SellResiClass("Active","Sell",typex,subtypex, name, name.toLowerCase(), address,
                        area, oname, contact, whatsapp, mail, rent, erent, more,FirebaseAuth.getInstance().getUid(), newDocID,size,
                        "","","",7028, mainum, 19.114591, 77.291456,date.getTime());
                toolsCollectionRef2.document(newDocID).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        SingleIDClass myresi = new SingleIDClass(newDocID,"Sell","Nanded","","",7028,new Date().getTime());
                        toolsCollectionRef3.document(newDocID).set(myresi).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(AddSellResiActivity.this, "Data Uploaded Successfully, please refresh", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();

                                Toast.makeText(AddSellResiActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddSellResiActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                dialog.dismiss();

                Toast.makeText(AddSellResiActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.numbertext.setText("Uploaded Successfully");

        newlist.clear();
    }

    private void checkpermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getUserLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
        }


    }

    private void getUserLocation() {

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    binding.locationview.setVisibility(View.VISIBLE);
                    dialog1.dismiss();
                    Toast.makeText(AddSellResiActivity.this, "Current location fetch successfully", Toast.LENGTH_SHORT).show();
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    binding.showlocationtext.setText("Latitude: " + latitude + " & Longitude: " + longitude);
                    //LatLng usercl = new LatLng(latitude, longitude);


                } else {

                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Accepted", Toast.LENGTH_SHORT).show();
                getUserLocation();
            } else {
                Toast.makeText(this, "Permission Rejected", Toast.LENGTH_SHORT).show();
                dialog1.dismiss();

            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {

                if (data.getClipData() != null) {
                    int x = data.getClipData().getItemCount();

                    for (int i = 0; i < x; i++) {

                        newuri = data.getClipData().getItemAt(i).getUri();
                        newlist.add(newuri);
                    }
                    multipleImageAdapter.notifyDataSetChanged();
                    binding.numbertext.setText("You have select " + newlist.size() + " images");

                }
            }
        }
    }





    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String rentaltype = parent.getItemAtPosition(position).toString();
        String areatype = parent.getItemAtPosition(position).toString();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

