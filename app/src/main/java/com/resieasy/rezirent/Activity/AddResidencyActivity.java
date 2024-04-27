package com.resieasy.rezirent.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.resieasy.rezirent.Class.AddFlatClass;
import com.resieasy.rezirent.Adapter.MultipleImageAdapter;
import com.resieasy.rezirent.R;
import com.resieasy.rezirent.Class.SingleIDClass;
import com.resieasy.rezirent.databinding.ActivityAddResidencyBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AddResidencyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ActivityAddResidencyBinding binding;
    String rentaltype[], areatype[];

    int PICK_IMG = 123, in;
    private GoogleMap mMap;
    double latitude, longitude;

    HashMap<String, String> hashMap;


    MultipleImageAdapter multipleImageAdapter;
    ProgressDialog dialog, dialog1;

    private static final int PICK_IMAGE = 1;


    private int upload_count = 0;

    ArrayList<Uri> newlist = new ArrayList<>();
    Uri newuri;
    int mainum = 0;

    ArrayList newStrings = new ArrayList<>();
    String subtype;



    private MaterialTimePicker picker1, picker2;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddResidencyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(this);
        dialog.setTitle("Uploading...");
        dialog.setMessage("Please wait, we are creating your residency account");
        dialog.setCancelable(false);
        dialog1 = new ProgressDialog(this);
        dialog1.setMessage("Fetching current location....");
        dialog1.setCancelable(false);

        hashMap = new HashMap<>();


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Rental, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.rentaltype.setAdapter(adapter);
        binding.rentaltype.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) AddResidencyActivity.this);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.Area, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.areatype.setAdapter(adapter1);
        binding.areatype.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) AddResidencyActivity.this);


        multipleImageAdapter = new MultipleImageAdapter(newlist);
        binding.multiimagerec.setLayoutManager(new GridLayoutManager(AddResidencyActivity.this, 3));
        binding.multiimagerec.setAdapter(multipleImageAdapter);


        binding.rentaltype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item.toString().equals("Flat")) {
                    binding.flatview1.setVisibility(View.VISIBLE);
                    binding.roomview1.setVisibility(View.GONE);


                } else if (item.toString().equals("Room")) {
                    binding.flatview1.setVisibility(View.GONE);
                    binding.roomview1.setVisibility(View.VISIBLE);

                } else {
                    binding.flatview1.setVisibility(View.GONE);
                    binding.roomview1.setVisibility(View.GONE);

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        binding.yesdeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.deposit.setVisibility(View.VISIBLE);
                binding.nodepositblue.setVisibility(View.GONE);
            }
        });
        binding.nodeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.deposit.setVisibility(View.GONE);
                binding.nodepositblue.setVisibility(View.VISIBLE);

            }
        });
        binding.yescharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.extracharges.setVisibility(View.VISIBLE);
                binding.noextrablue.setVisibility(View.GONE);
            }
        });
        binding.nocharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.extracharges.setVisibility(View.GONE);
                binding.noextrablue.setVisibility(View.VISIBLE);

            }
        });

        binding.yesargee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.yesagreeview.setVisibility(View.VISIBLE);
                binding.noagreementtext.setVisibility(View.GONE);
            }
        });
        binding.noagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.yesagreeview.setVisibility(View.GONE);
                binding.noagreementtext.setVisibility(View.VISIBLE);
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              finish();
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

        binding.submitresibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

               String type=binding.rentaltype.getSelectedItem().toString();



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

                }

                int ID1 = binding.mainradiodeposit.getCheckedRadioButtonId();
                RadioButton radioButton11 = findViewById(ID1);

                int ID2 = binding.mainradioextra.getCheckedRadioButtonId();
                RadioButton radioButton22 = findViewById(ID2);

                int ID4 = binding.mainrediopolicy.getCheckedRadioButtonId();
                RadioButton radioButton33 = findViewById(ID4);

                if (radioButton11.getText().equals("Yes")) {
                    //deposit = binding.deposit.getText().toString();
                    if (!binding.deposit.getText().toString().isEmpty()) {
                       String deposit = binding.deposit.getText().toString();

                        if (radioButton22.getText().equals("Yes")) {
                            if (!binding.extracharges.getText().toString().isEmpty()) {
                                String extra = binding.extracharges.getText().toString();

                                if (radioButton33.getText().equals("Agreement will be done")) {
                                    if (!binding.periodtime.getText().toString().isEmpty()) {
                                        int period = Integer.parseInt(binding.periodtime.getText().toString());
                                      //  int[] array = { period };
                                        String policy = binding.agreementtext.getText().toString();
                                        checktext(type, subtype, deposit, extra, period, policy);
                                    } else {
                                        binding.periodtime.setError("Please enter how many months agreement will be done");
                                        dialog.dismiss();
                                        Toast.makeText(AddResidencyActivity.this, "Please enter how many months agreement will be done", Toast.LENGTH_SHORT).show();
                                    }


                                } else if (radioButton33.getText().equals("No Agreement, we have own rules")) {
                                   int period = 708;
                                 //   int[] array = { period };
                                    String policy = binding.noagreementtext.getText().toString();
                                   checktext(type, subtype, deposit, extra, period, policy);

                                }

                            } else {
                                binding.extracharges.setError("Please enter extra charges details");
                                dialog.dismiss();
                                Toast.makeText(AddResidencyActivity.this, "Please enter extra charges details", Toast.LENGTH_SHORT).show();
                            }

                        } else if (radioButton22.getText().equals("No")) {
                            String extra = "No extra charges will taken";

                            if (radioButton33.getText().equals("Agreement will be done")) {
                                if (!binding.periodtime.getText().toString().isEmpty()) {
                                    int period = Integer.parseInt(binding.periodtime.getText().toString());

                                    //int[] array = { period };
                                    String policy = binding.agreementtext.getText().toString();

                                    checktext(type, subtype, deposit, extra, period, policy);

                                } else {
                                    binding.periodtime.setError("Please enter how many months agreement will be done");
                                    dialog.dismiss();
                                    Toast.makeText(AddResidencyActivity.this, "Please enter how many months agreement will be done", Toast.LENGTH_SHORT).show();
                                }


                            } else if (radioButton33.getText().equals("No Agreement, we have own rules")) {
                               int period = 708;
                            //    int[] array = { period };
                               String policy = binding.noagreementtext.getText().toString();
                                checktext(type, subtype, deposit, extra, period, policy);
                            }

                        }
                    } else {
                        binding.deposit.setError("Please enter deposit details");
                        dialog.dismiss();
                        Toast.makeText(AddResidencyActivity.this, "Please enter deposit details", Toast.LENGTH_SHORT).show();
                    }

                }
                else if (radioButton11.getText().equals("No")) {
                   String deposit = "No deposit will taken";

                    if (radioButton22.getText().equals("Yes")) {
                        if (!binding.extracharges.getText().toString().isEmpty()) {
                            String extra = binding.extracharges.getText().toString();

                            if (radioButton33.getText().equals("Agreement will be done")) {
                                if (!binding.periodtime.getText().toString().isEmpty()) {
                                    int period = Integer.parseInt(binding.periodtime.getText().toString());

                                    //  int[] array = { period };
                                   String  policy = binding.agreementtext.getText().toString();
                                   checktext(type, subtype, deposit, extra, period, policy);

                                } else {
                                    binding.periodtime.setError("Please enter how many months agreement will be done");
                                    dialog.dismiss();
                                    Toast.makeText(AddResidencyActivity.this, "Please enter how many months agreement will be done", Toast.LENGTH_SHORT).show();
                                }


                            } else if (radioButton33.getText().equals("No Agreement, we have own rules")) {
                                int period = 708;
                              //  int[] array = { period };

                               String policy = binding.noagreementtext.getText().toString();
                                checktext(type, subtype, deposit, extra, period, policy);
                            }

                        } else {
                            binding.extracharges.setError("Please enter extra charges details");
                            dialog.dismiss();
                            Toast.makeText(AddResidencyActivity.this, "Please enter extra charges details", Toast.LENGTH_SHORT).show();
                        }

                    } else if (radioButton22.getText().equals("No")) {
                        String  extra = "No extra charges will taken";

                        if (radioButton33.getText().equals("Agreement will be done")) {
                            if (!binding.periodtime.getText().toString().isEmpty()) {
                                int period = Integer.parseInt(binding.periodtime.getText().toString());

                                //    int[] array = { period };
                                String policy = binding.agreementtext.getText().toString();
                                checktext(type, subtype, deposit, extra, period, policy);

                            } else {
                                binding.periodtime.setError("Please enter how many months agreement will be done");
                                dialog.dismiss();
                                Toast.makeText(AddResidencyActivity.this, "Please enter how many months agreement will be done", Toast.LENGTH_SHORT).show();
                            }


                        } else if (radioButton33.getText().equals("No Agreement, we have own rules")) {
                           int period = 708;
                          //  int[] array = { period };
                           String policy = binding.noagreementtext.getText().toString();
                            checktext(type, subtype, deposit, extra, period, policy);


                        }

                    }
                }


            }
        });
        binding.capturelocation2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkpermission();
                dialog1.show();
            }
        });
        binding.mapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddResidencyActivity.this, MapsActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("name", "Your residency name will fetch here");
                startActivity(intent);

            }
        });

    }


    private void checktext(String typez, String subtypez, String depositz, String extraz, int periodz, String policyz) {




        String name = binding.resiname.getText().toString();
        String address = binding.resiaddress.getText().toString();
       // String locationtext = binding.showlocationtext.getText().toString();
        String oname = binding.oname.getText().toString();
        String contact = binding.contact.getText().toString();
        String whatsapp = binding.whatsapp.getText().toString();
        String rent = binding.rentamount.getText().toString();






        if (!name.isEmpty() && !address.isEmpty() && !oname.isEmpty() && !contact.isEmpty() && !whatsapp.isEmpty() && !rent.isEmpty()) {


            if (newlist.isEmpty()) {
                dialog.dismiss();
                Toast.makeText(AddResidencyActivity.this, "Please select images", Toast.LENGTH_SHORT).show();

            } else if (!newlist.isEmpty()) {

                if (newlist.size() < 11) {
                    mainum = newlist.size();
                    uploadimages(typez, subtypez, depositz, extraz, periodz, policyz);
                } else {
                    Toast.makeText(AddResidencyActivity.this, "Please don't select more than 10 images", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }


            }

        }
        else {
            if (name.isEmpty()) {
                dialog.dismiss();

                binding.resiname.setError("Please enter residency name");
                Toast.makeText(AddResidencyActivity.this, "Please enter residency name", Toast.LENGTH_SHORT).show();

            }
            if (address.isEmpty()) {
                dialog.dismiss();

                binding.resiaddress.setError("Please enter residency address");
                Toast.makeText(AddResidencyActivity.this, "Please enter residency address", Toast.LENGTH_SHORT).show();


            }

            if (oname.isEmpty()) {
                dialog.dismiss();

                binding.oname.setError("Please enter residency operator name");
                Toast.makeText(AddResidencyActivity.this, "Please enter residency operator name", Toast.LENGTH_SHORT).show();

            }
            if (contact.isEmpty()) {
                dialog.dismiss();

                binding.contact.setError("Please enter contact number");
                Toast.makeText(AddResidencyActivity.this, "Please enter contact number", Toast.LENGTH_SHORT).show();

            }
            if (whatsapp.isEmpty()) {
                dialog.dismiss();

                binding.whatsapp.setError("Please enter whatsapp number");
                Toast.makeText(AddResidencyActivity.this, "Please enter whatsapp number", Toast.LENGTH_SHORT).show();

            }
            if (rent.isEmpty()) {
                dialog.dismiss();

                binding.rentamount.setError("Please enter monthly rent");
                Toast.makeText(AddResidencyActivity.this, "Please enter monthly rent", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private void uploadimages(String typex, String subtypex, String depositx, String extrax, int periodx, String policyx) {



            binding.numbertext.setText("If Loading Takes to long press button again");
            StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("Nanded")
                    .child(FirebaseAuth.getInstance().getUid())
                    .child("RentImage");


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

                                            storeLink(newStrings, newlist.size(),typex, subtypex, depositx, extrax, periodx, policyx);
                                        }

                                    }
                                }
                        );

                    }
                });



            }
    }




    private void storeLink(ArrayList<String> urlStrings, int mainum, String typec, String subtypec, String depositc, String extrac, int periodc, String policyc) {

        HashMap<String, String> hashMap = new HashMap<>();
        int i;

        for (i = 0; i < newlist.size(); i++) {
            hashMap.put("image" + i, urlStrings.get(i));
        }


        String name = binding.resiname.getText().toString();
        String address = binding.resiaddress.getText().toString();
        String locationtext = binding.showlocationtext.getText().toString();
        String  oname = binding.oname.getText().toString();
        String contact = binding.contact.getText().toString();
        String whatsapp = binding.whatsapp.getText().toString();
        String mail = binding.email.getText().toString();
        String rent = binding.rentamount.getText().toString();
        String erent=binding.explainrent.getText().toString();
        String more = binding.moredetails.getText().toString();
        String userid=FirebaseAuth.getInstance().getUid();
        String area=binding.areatype.getSelectedItem().toString();

        CollectionReference toolsCollectionRef = FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllImage");
        CollectionReference toolsCollectionRef2 = FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllData");
        CollectionReference toolsCollectionRef3 = FirebaseFirestore.getInstance().collection("OwnResi")
                .document(userid).collection("Nanded");
        String newDocID = toolsCollectionRef.document().getId();


        toolsCollectionRef.document(newDocID).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Date date = new Date();
                AddFlatClass data = new AddFlatClass("Active","Rent", typec, subtypec, name, name.toLowerCase(), address,
                        area, oname, contact, whatsapp, mail, rent, erent, depositc,extrac, more,policyc,FirebaseAuth.getInstance().getUid(),
                        newDocID,"","","",7028, mainum, periodc, 19.114591, 77.291456, date.getTime());
                toolsCollectionRef2.document(newDocID).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        SingleIDClass myresi = new SingleIDClass(newDocID, "Rent","Nanded","","",7028,new Date().getTime());
                        toolsCollectionRef3.document(newDocID).set(myresi).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                addfacility(newDocID);
                                addrules(newDocID);
                                Toast.makeText(AddResidencyActivity.this, "Data Uploaded Successfully, please refresh", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();

                                Toast.makeText(AddResidencyActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddResidencyActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                dialog.dismiss();

                Toast.makeText(AddResidencyActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        dialog.dismiss();
        binding.numbertext.setText("Uploaded Successfully");

        newlist.clear();
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
                    Toast.makeText(AddResidencyActivity.this, "Current location fetch successfully", Toast.LENGTH_SHORT).show();
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    binding.showlocationtext.setText("Latitude: " + latitude + " & Longitude: " + longitude);
                    //LatLng usercl = new LatLng(latitude, longitude);


                } else {
                    Toast.makeText(AddResidencyActivity.this, "Something is wrong, location is not fetched", Toast.LENGTH_LONG).show();

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


    private void addfacility(String newDocID) {
        String a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26, a27, a28, a29, a30, a31, a32, a33, a34;
        if (binding.checkCleanontime.isChecked()) {
            a1 = "Yes";

        } else {
            a1 = "No";
        }
        if (binding.checkac.isChecked()) {
            a2 = "Yes";
        } else {
            a2 = "No";
        }
        if (binding.checkrowateer.isChecked()) {
            a3 = "Yes";
        } else {
            a3 = "No";
        }
        if (binding.checkwateerr.isChecked()) {
            a4 = "Yes";
        } else {
            a4 = "No";
        }
        if (binding.checkwifi.isChecked()) {
            a5 = "Yes";
        } else {
            a5 = "No";
        }
        if (binding.checkcamera.isChecked()) {
            a6 = "Yes";
        } else {
            a6 = "No";
        }
        if (binding.checkbed.isChecked()) {
            a7 = "Yes";
        } else {
            a7 = "No";
        }
        if (binding.checkhotwater.isChecked()) {
            a8 = "Yes";
        } else {
            a8 = "No";
        }
        if (binding.checktable.isChecked()) {
            a9 = "Yes";
        } else {
            a9 = "No";
        }
        if (binding.checklocker.isChecked()) {
            a10 = "Yes";
        } else {
            a10 = "No";
        }
        if (binding.checkcooler.isChecked()) {
            a11 = "Yes";
        } else {
            a11 = "No";
        }
        if (binding.checkpower.isChecked()) {
            a12 = "Yes";
        } else {
            a12 = "No";
        }
        if (binding.checkwashing.isChecked()) {
            a13 = "Yes";
        } else {
            a13 = "No";
        }
        if (binding.checksecurity.isChecked()) {
            a14 = "Yes";
        } else {
            a14 = "No";
        }
        if (binding.checkinout.isChecked()) {
            a15 = "Yes";
        } else {
            a15 = "No";
        }
        if (binding.checkattached.isChecked()) {
            a16 = "Yes";
        } else {
            a16 = "No";
        }
        if (binding.checkshower.isChecked()) {
            a17 = "Yes";
        } else {
            a17 = "No";
        }
        if (binding.checkparking.isChecked()) {
            a18 = "Yes";
        } else {
            a18 = "No";
        }
        if (binding.checkmess.isChecked()) {
            a19 = "Yes";
        } else {
            a19 = "No";
        }
        if (binding.checktv.isChecked()) {
            a20 = "Yes";
        } else {
            a20 = "No";
        }
        if (binding.checkgas.isChecked()) {
            a21 = "Yes";
        } else {
            a21 = "No";
        }
        if (binding.checkdining.isChecked()) {
            a22 = "Yes";
        } else {
            a22 = "No";
        }
        if (binding.checkrefre.isChecked()) {
            a23 = "Yes";
        } else {
            a23 = "No";
        }
        if (binding.checksofa.isChecked()) {
            a24 = "Yes";
        } else {
            a24 = "No";
        }
        if (binding.checkelvator.isChecked()) {
            a25 = "Yes";
        } else {
            a25 = "No";
        }
        if (binding.checkground.isChecked()) {
            a26 = "Yes";
        } else {
            a26 = "No";
        }
        if (binding.checkgym.isChecked()) {
            a27 = "Yes";
        } else {
            a27 = "No";
        }
        if (binding.checkstudyroom.isChecked()) {
            a28 = "Yes";
        } else {
            a28 = "No";
        }
        if (binding.checkkitchenn.isChecked()) {
            a29 = "Yes";
        } else {
            a29 = "No";
        }
        if (binding.checkbalcony.isChecked()) {
            a30 = "Yes";
        } else {
            a30 = "No";
        }
        if (binding.checkindiant.isChecked()) {
            a31 = "Yes";
        } else {
            a31 = "No";
        }
        if (binding.checkwesterntt.isChecked()) {
            a32 = "Yes";
        } else {
            a32 = "No";
        }
        if (binding.checkterrace.isChecked()) {
            a33 = "Yes";
        } else {
            a33 = "No";
        }
        if (binding.checkfullf.isChecked()) {
            a34 = "Yes";
        } else {
            a34 = "No";
        }
        HashMap<String, Object> hashMap1 = new HashMap<>();
        hashMap1.put("clean", a1);
        hashMap1.put("ac", a2);
        hashMap1.put("rowater", a3);
        hashMap1.put("water", a4);
        hashMap1.put("wifi", a5);
        hashMap1.put("cctv", a6);
        hashMap1.put("bed", a7);
        hashMap1.put("hotwater", a8);
        hashMap1.put("table", a9);
        hashMap1.put("locker", a10);
        hashMap1.put("fan", a11);
        hashMap1.put("powerbackup", a12);
        hashMap1.put("washing", a13);
        hashMap1.put("security", a14);
        hashMap1.put("inout", a15);
        hashMap1.put("attach", a16);
        hashMap1.put("shower", a17);
        hashMap1.put("parking", a18);
        hashMap1.put("mess", a19);
        hashMap1.put("tv", a20);
        hashMap1.put("gas", a21);
        hashMap1.put("dining", a22);
        hashMap1.put("refrigerator", a23);
        hashMap1.put("sofa", a24);
        hashMap1.put("elevator", a25);
        hashMap1.put("play", a26);
        hashMap1.put("gym", a27);
        hashMap1.put("studyroom", a28);
        hashMap1.put("kitchen", a29);
        hashMap1.put("balcony", a30);
        hashMap1.put("indian", a31);
        hashMap1.put("western", a32);
        hashMap1.put("terrace", a33);
        hashMap1.put("furnished", a34);
        String more = binding.facility.getText().toString();
        hashMap1.put("more", more);

        FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                .collection("AllFacility").document(newDocID).set(hashMap1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddResidencyActivity.this, "facility fail", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void addrules(String newDocID) {
        String a01, a02, a03, a04, a05, a06, a07, a08;
        if (binding.clinerule.isChecked()) {
            a01 = "Yes";

        } else {
            a01 = "No";
        }
        if (binding.nottrublerule.isChecked()) {
            a02 = "Yes";
        } else {
            a02 = "No";
        }
        if (binding.licencerule.isChecked()) {
            a03 = "Yes";
        } else {
            a03 = "No";
        }
        if (binding.entryrule.isChecked()) {
            a04 = "Yes";
        } else {
            a04 = "No";
        }
        if (binding.alcoholrule.isChecked()) {
            a05 = "Yes";
        } else {
            a05 = "No";
        }
        if (binding.damagerule.isChecked()) {
            a06 = "Yes";
        } else {
            a06 = "No";
        }
        if (binding.outsiderrule.isChecked()) {
            a07 = "Yes";
        } else {
            a07 = "No";
        }
        if (binding.prentperule.isChecked()) {
            a08 = "Yes";
        } else {
            a08 = "No";
        }
        HashMap<String, Object> hashMap11 = new HashMap<>();
        hashMap11.put("clean", a01);
        hashMap11.put("trouble", a02);
        hashMap11.put("licence", a03);
        hashMap11.put("gateenry", a04);
        hashMap11.put("alcohol", a05);
        hashMap11.put("damage", a06);
        hashMap11.put("ousiders", a07);
        hashMap11.put("permission", a08);
        String more1 = binding.rules.getText().toString();
        hashMap11.put("more", more1);

        FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                .collection("AllRule").document(newDocID).set(hashMap11)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddResidencyActivity.this, "Rules fail", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String rentaltype = parent.getItemAtPosition(position).toString();
        String areatype = parent.getItemAtPosition(position).toString();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 41;

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = AddResidencyActivity.this.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) AddResidencyActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}