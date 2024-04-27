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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.resieasy.rezirent.Adapter.MultioldImageAdapter;
import com.resieasy.rezirent.Adapter.MultipleImageAdapter;
import com.resieasy.rezirent.Class.SellResiClass;
import com.resieasy.rezirent.R;
import com.resieasy.rezirent.databinding.ActivityEditSellDataBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class EditSellDataActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ActivityEditSellDataBinding binding;
    int PICK_IMAGE = 123;
    String policy;
    int period;
    int upload_count = 0;
    int inumber;
    String rentaltype[], areatype[];
    //String[] rentaltype1={" ","Flat","Room","Hostel/PG","Shutter","House","Building"};

    private ProgressDialog dialog, dialog1;

    ArrayList<Uri> oldlist = new ArrayList<>();
    ArrayList<Uri> newlist = new ArrayList<>();
    Uri newuri;
    Uri olduri;

    double lat,lan;

    int mainum = 0;

    ArrayList newStrings = new ArrayList<>();
    ArrayList oldStrings = new ArrayList<>();
    double latitude, longitude;
    double oldlatitude, oldlongitude;


    private MaterialTimePicker picker;
    Calendar calendar;
    MultipleImageAdapter multipleImageAdapter;
    MultioldImageAdapter multioldImageAdapter;
    String id;
    String type,subtype,getsubtype;
    Object item,item2;
    String name,status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEditSellDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        id = getIntent().getStringExtra("id");



        dialog1 = new ProgressDialog(this);
        dialog1.setMessage("Fetching current location....");
        dialog1.setCancelable(false);

        dialog = new ProgressDialog(EditSellDataActivity.this);
        dialog.setMessage("Uploading Images please Wait.........!!!!!!");
        dialog.setCancelable(false);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Sell1, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.rentaltype.setAdapter(adapter);
        binding.rentaltype.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) EditSellDataActivity.this);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.Area1, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.areatype.setAdapter(adapter1);
        binding.areatype.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) EditSellDataActivity.this);


        multipleImageAdapter = new MultipleImageAdapter(newlist);
        binding.multiimagerec.setLayoutManager(new GridLayoutManager(EditSellDataActivity.this, 3));
        binding.multiimagerec.setAdapter(multipleImageAdapter);

        multioldImageAdapter = new MultioldImageAdapter(oldlist);
        binding.multiimagerec2.setLayoutManager(new GridLayoutManager(EditSellDataActivity.this, 3));
        binding.multiimagerec2.setAdapter(multioldImageAdapter);






        FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllData").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        inumber = documentSnapshot.getLong("in").intValue();
                        oldlatitude = documentSnapshot.getDouble("latitude").doubleValue();
                        oldlongitude = documentSnapshot.getDouble("longitude").doubleValue();

                         String gettype = documentSnapshot.getString("type");

                        String idd = documentSnapshot.getString("id");
                         getsubtype = documentSnapshot.getString("subtype");
                        String area = documentSnapshot.getString("area");
                         name = documentSnapshot.getString("name");
                         status = documentSnapshot.getString("status");
                        String address = documentSnapshot.getString("address");
                        String oname = documentSnapshot.getString("oname");
                        String number = documentSnapshot.getString("number");
                        String whatsapp = documentSnapshot.getString("whatsapp");
                        String mail = documentSnapshot.getString("mail");
                        String prize = documentSnapshot.getString("prize");
                        String eprize = documentSnapshot.getString("eprize");
                        String more = documentSnapshot.getString("more");
                        String size = documentSnapshot.getString("size");



                        binding.viewresitypetext.setText(gettype);
                        binding.vieareatext.setText(area);
                        binding.resiaddress.setText(address);
                        binding.resiname.setText(name);
                        binding.oname.setText(oname);
                        binding.contact.setText(number);
                        binding.whatsapp.setText(whatsapp);
                        binding.email.setText(mail);
                        binding.prizeamount.setText(prize);
                        binding.explainprize.setText(eprize);
                        binding.propertysize.setText(size);
                        binding.moredetails.setText(more);

                        binding.olshowlocationtext.setText("Latitude " + oldlatitude + " And " + oldlongitude);

                        FirebaseFirestore.getInstance().collection("Nanded")
                                .document("NandedCity").collection("AllImage").document(idd).get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot snapshot) {
                                        for (int i = 0; i < inumber; i++) {
                                            String immm = snapshot.getString("image" + i);

                                            olduri = Uri.parse(snapshot.getString("image" + i));
                                            oldlist.add(olduri);
                                            oldStrings.add(immm);


                                        }
                                        multioldImageAdapter.notifyDataSetChanged();
                                        binding.numbertext.setText("You have select " + oldlist.size() + " images");


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditSellDataActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });




                        if (gettype.equals("Flat")){
                            binding.flatview1.setVisibility(View.VISIBLE);
                            binding.roomview1.setVisibility(View.GONE);
                            if (getsubtype.equals("1RK")){
                                binding.rk1.setChecked(true);
                            } if (getsubtype.equals("1BHK")){
                                binding.bhk1.setChecked(true);
                            } if (getsubtype.equals("2BHK")){
                                binding.bhk2.setChecked(true);
                            } if (getsubtype.equals("3BHK")){
                                binding.bhk3.setChecked(true);
                            }

                        }else if(gettype.equals("Room")){
                            binding.flatview1.setVisibility(View.GONE);
                            binding.roomview1.setVisibility(View.VISIBLE);
                            if (getsubtype.equals("Single Room")){
                                binding.singleroom1.setChecked(true);
                            } if (getsubtype.equals("Double Room")){
                                binding.doubleroom1.setChecked(true);
                            } if (getsubtype.equals("Triple Room")){
                                binding.tripleroom1.setChecked(true);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditSellDataActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                });

        binding.rentaltype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position);
                if (item.toString().equals("Flat")) {

                    binding.flatview1.setVisibility(View.VISIBLE);
                    binding.roomview1.setVisibility(View.GONE);
                    binding.viewresitypetext.setVisibility(View.GONE);

                    binding.viewresitypetext.setText("Flat");


                } else if (item.toString().equals("Room")) {
                    binding.flatview1.setVisibility(View.GONE);
                    binding.roomview1.setVisibility(View.VISIBLE);
                    binding.viewresitypetext.setVisibility(View.GONE);
                    binding.viewresitypetext.setText("Room");


                } else if (item.toString().equals("Stutter")) {
                    binding.flatview1.setVisibility(View.GONE);
                    binding.roomview1.setVisibility(View.GONE);
                    binding.viewresitypetext.setVisibility(View.GONE);
                    binding.viewresitypetext.setText("Shutter");


                } else if (item.toString().equals("House")) {
                    binding.flatview1.setVisibility(View.GONE);
                    binding.roomview1.setVisibility(View.GONE);
                    binding.viewresitypetext.setVisibility(View.GONE);
                    binding.viewresitypetext.setText("House");


                } else if (item.toString().equals("Building")) {
                    binding.flatview1.setVisibility(View.GONE);
                    binding.roomview1.setVisibility(View.GONE);
                    binding.viewresitypetext.setVisibility(View.GONE);
                    binding.viewresitypetext.setText("Building");




                }else if (item.toString().equals("Land")) {
                    binding.flatview1.setVisibility(View.GONE);
                    binding.roomview1.setVisibility(View.GONE);
                    binding.viewresitypetext.setVisibility(View.GONE);
                    binding.viewresitypetext.setText("Land");


                } else if (item.toString().equals("")) {

                    binding.viewresitypetext.setVisibility(View.VISIBLE);

                }


            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        binding.areatype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item2 = parent.getItemAtPosition(position);
                if (item2.toString().equals("Anand Nagar")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Anand Nagar");

                } else if (item2.toString().equals("Asarjan")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Asarjan");

                } else if (item2.toString().equals("Ashok Nagar")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Ashok Nagar");

                } else if (item2.toString().equals("Baba Nagar")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Baba Nagar");

                } else if (item2.toString().equals("Bafna")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Bafna");

                } else if (item2.toString().equals("Balirampur")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Balirampur");

                }else if (item2.toString().equals("Bhagya Nagar")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Bhagya Nagar");

                } else if (item2.toString().equals("Chaitanya Nagar")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Chaitanya Nagar");

                }else if (item2.toString().equals("Chaufula")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Chaufula");

                } else if (item2.toString().equals("CIDCO")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("CIDCO");

                }else if (item2.toString().equals("Dhanegaon")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Dhanegaon");

                } else if (item2.toString().equals("Farande Nagar")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Farande Nagar");

                }else if (item2.toString().equals("Ganesh Nagar")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Ganesh Nagar");

                } else if (item2.toString().equals("Gopalchiwadi")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Gopalchiwadi");

                } else if (item2.toString().equals("Hanuman Gad")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Hanuman Gad");

                }else if (item2.toString().equals("Harsh Nagar")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Harsh Nagar");

                } else if (item2.toString().equals("Hingoli Gate")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Hingoli Gate");

                }else if (item2.toString().equals("HUDCO")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("HUDCO");

                } else if (item2.toString().equals("Hyder Bagh")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Hyder Bagh");

                }else if (item2.toString().equals("Itwara")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Itwara");

                } else if (item2.toString().equals("Kabra Nagar")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Kabra Nagar");

                }else if (item2.toString().equals("Kala Mandir")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Kala Mandir");

                } else if (item2.toString().equals("Kamtha Village")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Kamtha Village");

                }else if (item2.toString().equals("Kautha")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Kautha");

                } else if (item2.toString().equals("Kautha(New)")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Kautha(New)");

                }else if (item2.toString().equals("Khadkpura")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Khadkpura");

                } else if (item2.toString().equals("Labour Colony")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Labour Colony");

                }else if (item2.toString().equals("Lokmitra Nagar")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Lokmitra Nagar");

                } else if (item2.toString().equals("MIDC")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("MIDC");

                }else if (item2.toString().equals("Mondha(New)")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Mondha(New)");

                } else if (item2.toString().equals("Mondha(Old)")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Mondha(Old)");

                }else if (item2.toString().equals("Mujampeth")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Mujampeth");

                } else if (item2.toString().equals("Peer Burhan Nagar")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Peer Burhan Nagar");

                }else if (item2.toString().equals("Ravi Nagar(Kautha)")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Ravi Nagar(Kautha)");

                } else if (item2.toString().equals("Sarafa")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Sarafa");

                }else if (item2.toString().equals("Shahu Nagar")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Shahu Nagar");

                } else if (item2.toString().equals("Shivaji Nagar")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Shivaji Nagar");

                }else if (item2.toString().equals("Shrawasti Nagar")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Shrawasti Nagar");

                } else if (item2.toString().equals("Shri Nagar")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Shri Nagar");

                }else if (item2.toString().equals("Shyam Nagar")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Shyam Nagar");

                } else if (item2.toString().equals("Taroda bk")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Taroda bk");

                }else if (item2.toString().equals("Taroda kh")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Taroda kh");

                } else if (item2.toString().equals("Vadibudruk")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Vadibudruk");

                }else if (item2.toString().equals("Vajirabad")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Vajirabad");

                } else if (item2.toString().equals("Vasarani")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Vasarani");

                }else if (item2.toString().equals("Vishnupuri")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Vishnupuri");

                } else if (item2.toString().equals("Wajegaon")) {

                    binding.vieareatext.setVisibility(View.GONE);
                    binding.vieareatext.setText("Wajegaon");

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
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

                }else if (type.equals("Land")) {
                    subtype = "Land";

                } else if (type.equals("")) {

                    String ttypee=binding.viewresitypetext.getText().toString();
                    if (ttypee.equals("Flat")){
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
                    } else if (ttypee.equals("Room")) {
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
                    }  else if (ttypee.equals("Stutter")) {
                        subtype = "Shutter";

                    } else if (ttypee.equals("House")) {
                        subtype = "House";

                    } else if (ttypee.equals("Building")) {
                        subtype = "Building";

                    }else if (ttypee.equals("Land")) {
                        subtype = "Land";

                    }
                } else {
                    subtype=getsubtype;
                }


                String nametext = binding.resiname.getText().toString();
                String addresstext = binding.resiaddress.getText().toString();
                String onametext = binding.oname.getText().toString();
                String contacttext = binding.contact.getText().toString();
                String whatsapptext = binding.whatsapp.getText().toString();


                if (!nametext.isEmpty() && !addresstext.isEmpty() && !onametext.isEmpty() && !contacttext.isEmpty() && !whatsapptext.isEmpty() ) {




                    if (newlist.isEmpty() && oldlist.isEmpty()) {
                        dialog.dismiss();
                        Toast.makeText(EditSellDataActivity.this, "Please select images", Toast.LENGTH_SHORT).show();

                    }
                    else if (!newlist.isEmpty() && oldlist.isEmpty()) {

                        if (newlist.size() < 11) {
                            mainum = newlist.size();
                            StorageReference ImageFolder = FirebaseStorage.getInstance().getReference()
                                    .child("Nanded")
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
                                                        //String image=uri.toString();
                                                        newStrings.add(String.valueOf(uri));

                                                        if (newStrings.size() == newlist.size()) {


                                                            storeLink(id,newStrings, mainum,type,subtype);
                                                        }

                                                    }
                                                }
                                        );
                                    }
                                });

                            }
                        } else {
                            Toast.makeText(EditSellDataActivity.this, "Please don't select more than 10 images", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }

                    }
                    else if (newlist.isEmpty() && !oldlist.isEmpty()) {

                        if (oldlist.size() < 11) {
                            mainum = oldlist.size();
                            storeLink(id, oldStrings, mainum,type,subtype);


                        } else {
                            Toast.makeText(EditSellDataActivity.this, "Please don't select more than 10 images", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }

                    }
                    else if (!newlist.isEmpty() && !oldlist.isEmpty()) {
                        int combine = newlist.size() + oldlist.size();

                        if (combine < 11) {
                            mainum = combine;
                            StorageReference ImageFolder = FirebaseStorage.getInstance().getReference()
                                    .child("Nanded")
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
                                                        //String image=uri.toString();
                                                        newStrings.add(String.valueOf(uri));

                                                        if (newStrings.size() == newlist.size()) {

                                                            newStrings.addAll(oldStrings);

                                                            storeLink(id, newStrings, mainum,type,subtype);
                                                        }

                                                    }
                                                }
                                        );
                                    }
                                });

                            }
                        } else {
                            Toast.makeText(EditSellDataActivity.this, "Please don't select more than 10 images", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }

                    }
                }
                else {
                    if (nametext.isEmpty()){
                        dialog.dismiss();

                        binding.resiname.setError("Please enter residency name");
                        Toast.makeText(EditSellDataActivity.this, "Please enter residency name", Toast.LENGTH_LONG).show();

                    }
                    if (addresstext.isEmpty()){
                        dialog.dismiss();

                        binding.resiaddress.setError("Please enter residency address");
                        Toast.makeText(EditSellDataActivity.this, "Please enter residency address", Toast.LENGTH_LONG).show();

                    }
                    if (onametext.isEmpty()){
                        dialog.dismiss();

                        binding.oname.setError("Please enter residency operator name");
                        Toast.makeText(EditSellDataActivity.this, "Please enter residency operator name", Toast.LENGTH_LONG).show();

                    }
                    if (contacttext.isEmpty()){
                        dialog.dismiss();

                        binding.contact.setError("Please enter contact number");
                        Toast.makeText(EditSellDataActivity.this, "Please enter contact number", Toast.LENGTH_LONG).show();

                    }
                    if (whatsapptext.isEmpty()){
                        dialog.dismiss();

                        binding.whatsapp.setError("Please enter whatsapp number");
                        Toast.makeText(EditSellDataActivity.this, "Please enter whatsapp number", Toast.LENGTH_LONG).show();

                    }

                }

            }
        });





        binding.opengallerybtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_IMAGE);
                binding.cureentimageview.setVisibility(View.VISIBLE);
            }
        });
        binding.capturelocation2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkpermission();
                dialog1.show();
            }
        }); binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.mapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditSellDataActivity.this, MapsActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("name", "Your residency name will fetch here");
                startActivity(intent);

            }
        });binding.olmapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditSellDataActivity.this, MapsActivity.class);
                intent.putExtra("latitude", oldlatitude);
                intent.putExtra("longitude", oldlongitude);
                intent.putExtra("name", name);
                startActivity(intent);

            }
        });
    }



    private void storeLink(String iddd, ArrayList<String> newStrings, int mainumx, String type, String subtypex) {

        HashMap<String, String> hashMap = new HashMap<>();
        int i;


        for (i = 0; i < mainumx; i++) {
            hashMap.put("image" + i, newStrings.get(i));

        }

        String name = binding.resiname.getText().toString();
        String address = binding.resiaddress.getText().toString();
        String oname = binding.oname.getText().toString();
        String contact = binding.contact.getText().toString();
        String whatsapp = binding.whatsapp.getText().toString();
        String mail = binding.email.getText().toString();
        String prize = binding.prizeamount.getText().toString();
        String eprize = binding.explainprize.getText().toString();
        String size = binding.propertysize.getText().toString();
        String more = binding.moredetails.getText().toString();
        String ltype = binding.viewresitypetext.getText().toString();
        String larea = binding.vieareatext.getText().toString();



        if (binding.showlocationtext.getText().toString().isEmpty()){
            lat=oldlatitude;
            lan=oldlongitude;

        }else {
            lat=latitude;
            lan=longitude;
        }



        CollectionReference toolsCollectionRef = FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllImage");
        CollectionReference toolsCollectionRef2 = FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllData");

        toolsCollectionRef.document(this.id).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Date date=new Date();
                SellResiClass data = new SellResiClass(status,"Sell",ltype,subtypex, name, name.toLowerCase(), address, larea, oname, contact, whatsapp, mail, prize, eprize, more,FirebaseAuth.getInstance().getUid(),iddd,size,"","","",7028, mainumx, lat, lan,date.getTime());
                toolsCollectionRef2.document(EditSellDataActivity.this.id).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.dismiss();
                        Toast.makeText(EditSellDataActivity.this, "Data Edited Successfully, please refresh", Toast.LENGTH_LONG).show();
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditSellDataActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        finish();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                dialog.dismiss();

                Toast.makeText(EditSellDataActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditSellDataActivity.this, "Current location fetch successfully", Toast.LENGTH_SHORT).show();
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String rentaltype = parent.getItemAtPosition(position).toString();
        String areatype = parent.getItemAtPosition(position).toString();


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}