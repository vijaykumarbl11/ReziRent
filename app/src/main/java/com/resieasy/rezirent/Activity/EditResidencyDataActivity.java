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
import com.resieasy.rezirent.Class.AddFlatClass;
import com.resieasy.rezirent.Adapter.MultioldImageAdapter;
import com.resieasy.rezirent.Adapter.MultipleImageAdapter;
import com.resieasy.rezirent.R;
import com.resieasy.rezirent.databinding.ActivityEditResidencyDataBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class EditResidencyDataActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ActivityEditResidencyDataBinding binding;
    int PICK_IMAGE = 123;
    String policy;
    int period;
    int upload_count = 0;
    int inumber;
    String rentaltype[], areatype[];
    //String[] rentaltype1={" ","Flat","Room","Hostel/PG","Shutter","House","Building"};

    private ProgressDialog dialog, dialog1;

    // ArrayList ImageList = new ArrayList();
    ArrayList<Uri> oldlist = new ArrayList<>();
    ArrayList<Uri> newlist = new ArrayList<>();
    Uri newuri;
    Uri olduri;
    ArrayList newStrings = new ArrayList<>();
    ArrayList oldStrings = new ArrayList<>();
    double latitude, longitude;
    double oldlatitude, oldlongitude;

    double lat, lan;

    private MaterialTimePicker picker;
    Calendar calendar;
    MultipleImageAdapter multipleImageAdapter;
    MultioldImageAdapter multioldImageAdapter;
    // String id;
    Object item, item2;
    String name, status;
    int mainum = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditResidencyDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String id = getIntent().getStringExtra("id");


        dialog1 = new ProgressDialog(this);
        dialog1.setMessage("Fetching current location....");
        dialog1.setCancelable(false);

        dialog = new ProgressDialog(EditResidencyDataActivity.this);
        dialog.setMessage("Updating data please Wait.........!!!!!!");
        dialog.setCancelable(false);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Rental1, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.rentaltype.setAdapter(adapter);
        binding.rentaltype.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) EditResidencyDataActivity.this);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.Area1, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.areatype.setAdapter(adapter1);
        binding.areatype.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) EditResidencyDataActivity.this);

        multipleImageAdapter = new MultipleImageAdapter(newlist);
        binding.multiimagerec.setLayoutManager(new GridLayoutManager(EditResidencyDataActivity.this, 3));
        binding.multiimagerec.setAdapter(multipleImageAdapter);

        multioldImageAdapter = new MultioldImageAdapter(oldlist);
        binding.multiimagerec2.setLayoutManager(new GridLayoutManager(EditResidencyDataActivity.this, 3));
        binding.multiimagerec2.setAdapter(multioldImageAdapter);


        FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllData").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        inumber = documentSnapshot.getLong("in").intValue();
                        oldlatitude = documentSnapshot.getDouble("latitude").doubleValue();
                        oldlongitude = documentSnapshot.getDouble("longitude").doubleValue();
                        int period = documentSnapshot.getLong("period").intValue();
                        String period1 = String.valueOf(documentSnapshot.getLong("period").intValue());


                        String idd = documentSnapshot.getString("id");
                        String gettype = documentSnapshot.getString("type");
                        String getsubtype = documentSnapshot.getString("subtype");
                        String area = documentSnapshot.getString("area");
                        name = documentSnapshot.getString("name");
                        status = documentSnapshot.getString("status");
                        String address = documentSnapshot.getString("address");
                        String oname = documentSnapshot.getString("oname");
                        String number = documentSnapshot.getString("number");
                        String whatsapp = documentSnapshot.getString("whatsapp");
                        String mail = documentSnapshot.getString("mail");
                        String rent = documentSnapshot.getString("rent");
                        String erent = documentSnapshot.getString("erent");
                        String deposit = documentSnapshot.getString("deposit");
                        String extra = documentSnapshot.getString("extra");
                        String more = documentSnapshot.getString("more");
                        String oppolicy = documentSnapshot.getString("policy");


                        binding.olshowlocationtext.setText("Latitude " + oldlatitude + " And " + oldlongitude);

                        binding.viewresitypetext.setText(gettype);
                        binding.viewresisubtypetext.setText(getsubtype);
                        binding.vieareatext.setText(area);
                        binding.resiaddress.setText(address);
                        binding.resiname.setText(name);
                        binding.oname.setText(oname);
                        binding.contact.setText(number);
                        binding.whatsapp.setText(whatsapp);
                        binding.email.setText(mail);
                        binding.rentamount.setText(rent);
                        binding.explainrent.setText(erent);
                        binding.moredetails.setText(more);

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


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditResidencyDataActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });


                        if (gettype.equals("Flat")) {
                            binding.flatview1.setVisibility(View.VISIBLE);
                            binding.roomview1.setVisibility(View.GONE);
                            if (getsubtype.equals("1RK")) {
                                binding.rk1.setChecked(true);
                            }
                            if (getsubtype.equals("1BHK")) {
                                binding.bhk1.setChecked(true);
                            }
                            if (getsubtype.equals("2BHK")) {
                                binding.bhk2.setChecked(true);
                            }
                            if (getsubtype.equals("3BHK")) {
                                binding.bhk3.setChecked(true);
                            }

                        } else if (gettype.equals("Room")) {
                            binding.flatview1.setVisibility(View.GONE);
                            binding.roomview1.setVisibility(View.VISIBLE);
                            if (getsubtype.equals("Single Room")) {
                                binding.singleroom1.setChecked(true);
                            }
                            if (getsubtype.equals("Double Room")) {
                                binding.doubleroom1.setChecked(true);
                            }
                            if (getsubtype.equals("Triple Room")) {
                                binding.tripleroom1.setChecked(true);
                            }
                        }


                        if (deposit.equals("No deposit will taken")) {
                            binding.nodepositblue.setVisibility(View.VISIBLE);
                            binding.deposit.setVisibility(View.GONE);
                            binding.nodeposit.setChecked(true);
                            binding.yesdeposit.setChecked(false);
                        } else {
                            binding.nodepositblue.setVisibility(View.GONE);
                            binding.deposit.setVisibility(View.VISIBLE);
                            binding.deposit.setText(deposit);
                            binding.nodeposit.setChecked(false);
                            binding.yesdeposit.setChecked(true);
                        }
                        if (extra.equals("No extra charges will taken")) {
                            binding.noextrablue.setVisibility(View.VISIBLE);
                            binding.extracharges.setVisibility(View.GONE);
                            binding.yescharge.setChecked(false);
                            binding.nocharge.setChecked(true);
                        } else {
                            binding.noextrablue.setVisibility(View.GONE);
                            binding.extracharges.setVisibility(View.VISIBLE);
                            binding.extracharges.setText(extra);
                            binding.nocharge.setChecked(false);
                            binding.yescharge.setChecked(true);

                        }
                        if (period == 708) {
                            binding.noagreetext.setVisibility(View.VISIBLE);
                            binding.yesagreeview.setVisibility(View.GONE);
                            binding.noagreetext.setText(oppolicy);
                            binding.yesargee.setChecked(false);
                            binding.noagree.setChecked(true);
                        } else {
                            binding.noagreetext.setVisibility(View.GONE);
                            binding.yesagreeview.setVisibility(View.VISIBLE);
                            binding.periodtime.setText(period1);
                            binding.yesagreetext.setText(oppolicy);
                            binding.noagree.setChecked(false);
                            binding.yesargee.setChecked(true);

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditResidencyDataActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                });

        binding.rentaltype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position);

                if (item.toString().equals("Flat")) {

                    binding.flatview1.setVisibility(View.VISIBLE);
                    binding.roomview1.setVisibility(View.GONE);
                     binding.viewresitypetext.setVisibility(View.GONE);

                    int ID01 = binding.flatview1.getCheckedRadioButtonId();
                    RadioButton radioButton01 = findViewById(ID01);
                    binding.viewresitypetext.setText("Flat");

                    if (radioButton01.getText().equals("1RK")) {
                        binding.viewresisubtypetext.setText("1RK");

                    }
                    if (radioButton01.getText().equals("1BHK")) {
                        binding.viewresisubtypetext.setText("1BHK");

                    }
                    if (radioButton01.getText().equals("2BHK")) {
                        binding.viewresisubtypetext.setText("2BHK");

                    }
                    if (radioButton01.getText().equals("3BHK")) {
                        binding.viewresisubtypetext.setText("3BHK");

                    }
                    if (radioButton01.getText().equals("4BHK")) {
                        binding.viewresisubtypetext.setText("4BHK");

                    }
                    if (radioButton01.getText().equals("5BHK")) {
                        binding.viewresisubtypetext.setText("5BHK");

                    }

                } else if (item.toString().equals("Room")) {
                    binding.flatview1.setVisibility(View.GONE);
                    binding.roomview1.setVisibility(View.VISIBLE);
                     binding.viewresitypetext.setVisibility(View.GONE);
                    int ID02 = binding.roomview1.getCheckedRadioButtonId();
                    RadioButton radioButton02 = findViewById(ID02);
                    binding.viewresitypetext.setText("Room");

                    if (radioButton02.getText().equals("Single Room")) {
                        binding.viewresisubtypetext.setText("Single Room");


                    }
                    if (radioButton02.getText().equals("Double Room")) {
                        binding.viewresisubtypetext.setText("Double Room");


                    }
                    if (radioButton02.getText().equals("Triple Room")) {
                        binding.viewresisubtypetext.setText("Triple Room");


                    }

                } else if (item.toString().equals("Stutter")) {
                    binding.flatview1.setVisibility(View.GONE);
                    binding.roomview1.setVisibility(View.GONE);
                     binding.viewresitypetext.setVisibility(View.GONE);
                    binding.viewresitypetext.setText("Shutter");

                    binding.viewresisubtypetext.setText("Shutter");

                } else if (item.toString().equals("House")) {
                    binding.flatview1.setVisibility(View.GONE);
                    binding.roomview1.setVisibility(View.GONE);
                     binding.viewresitypetext.setVisibility(View.GONE);
                    binding.viewresitypetext.setText("House");

                    binding.viewresisubtypetext.setText("House");

                } else if (item.toString().equals("Building")) {
                    binding.flatview1.setVisibility(View.GONE);
                    binding.roomview1.setVisibility(View.GONE);
                    binding.viewresitypetext.setVisibility(View.GONE);
                    binding.viewresitypetext.setText("Building");

                    binding.viewresisubtypetext.setText("Building");


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
        FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                .collection("AllFacility").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String clean = documentSnapshot.getString("clean");
                        String ac = documentSnapshot.getString("ac");
                        String rowater = documentSnapshot.getString("rowater");
                        String water = documentSnapshot.getString("water");
                        String wifi = documentSnapshot.getString("wifi");
                        String cctv = documentSnapshot.getString("cctv");
                        String bed = documentSnapshot.getString("bed");
                        String hotwater = documentSnapshot.getString("hotwater");
                        String table = documentSnapshot.getString("table");
                        String locker = documentSnapshot.getString("locker");
                        String fan = documentSnapshot.getString("fan");
                        String powerbackup = documentSnapshot.getString("powerbackup");
                        String washing = documentSnapshot.getString("washing");
                        String security = documentSnapshot.getString("security");
                        String inout = documentSnapshot.getString("inout");
                        String attach = documentSnapshot.getString("attach");
                        String shower = documentSnapshot.getString("shower");
                        String parking = documentSnapshot.getString("parking");
                        String mess = documentSnapshot.getString("mess");
                        String tv = documentSnapshot.getString("tv");
                        String gas = documentSnapshot.getString("gas");
                        String dining = documentSnapshot.getString("dining");
                        String refrigerator = documentSnapshot.getString("refrigerator");
                        String sofa = documentSnapshot.getString("sofa");
                        String elevator = documentSnapshot.getString("elevator");
                        String play = documentSnapshot.getString("play");
                        String gym = documentSnapshot.getString("gym");
                        String studyroom = documentSnapshot.getString("studyroom");
                        String kitchen = documentSnapshot.getString("kitchen");
                        String balcony = documentSnapshot.getString("balcony");
                        String indian = documentSnapshot.getString("indian");
                        String western = documentSnapshot.getString("western");
                        String terrace = documentSnapshot.getString("terrace");
                        String furnished = documentSnapshot.getString("furnished");
                        String morefaci = documentSnapshot.getString("more");
                        if (clean.equals("Yes")) {
                            binding.checkCleanontime.setChecked(true);
                        }
                        if (ac.equals("Yes")) {
                            binding.checkac.setChecked(true);
                        }
                        if (rowater.equals("Yes")) {
                            binding.checkrowateer.setChecked(true);
                        }
                        if (water.equals("Yes")) {
                            binding.checkwateerr.setChecked(true);
                        }
                        if (wifi.equals("Yes")) {
                            binding.checkwifi.setChecked(true);
                        }
                        if (cctv.equals("Yes")) {
                            binding.checkcamera.setChecked(true);
                        }
                        if (bed.equals("Yes")) {
                            binding.checkbed.setChecked(true);
                        }
                        if (hotwater.equals("Yes")) {
                            binding.checkhotwater.setChecked(true);
                        }
                        if (table.equals("Yes")) {
                            binding.checktable.setChecked(true);
                        }
                        if (locker.equals("Yes")) {
                            binding.checklocker.setChecked(true);
                        }
                        if (fan.equals("Yes")) {
                            binding.checkcooler.setChecked(true);
                        }
                        if (powerbackup.equals("Yes")) {
                            binding.checkpower.setChecked(true);
                        }
                        if (washing.equals("Yes")) {
                            binding.checkwashing.setChecked(true);
                        }
                        if (security.equals("Yes")) {
                            binding.checksecurity.setChecked(true);
                        }
                        if (inout.equals("Yes")) {
                            binding.checkinout.setChecked(true);
                        }
                        if (attach.equals("Yes")) {
                            binding.checkattached.setChecked(true);
                        }
                        if (shower.equals("Yes")) {
                            binding.checkshower.setChecked(true);
                        }
                        if (parking.equals("Yes")) {
                            binding.checkparking.setChecked(true);
                        }
                        if (mess.equals("Yes")) {
                            binding.checkmess.setChecked(true);
                        }
                        if (tv.equals("Yes")) {
                            binding.checktv.setChecked(true);
                        }
                        if (gas.equals("Yes")) {
                            binding.checkgas.setChecked(true);
                        }
                        if (dining.equals("Yes")) {
                            binding.checkdining.setChecked(true);
                        }
                        if (refrigerator.equals("Yes")) {
                            binding.checkrefre.setChecked(true);
                        }
                        if (sofa.equals("Yes")) {
                            binding.checksofa.setChecked(true);
                        }
                        if (elevator.equals("Yes")) {
                            binding.checkelvator.setChecked(true);
                        }
                        if (play.equals("Yes")) {
                            binding.checkground.setChecked(true);
                        }
                        if (gym.equals("Yes")) {
                            binding.checkgym.setChecked(true);
                        }
                        if (studyroom.equals("Yes")) {
                            binding.checkstudyroom.setChecked(true);
                        }
                        if (kitchen.equals("Yes")) {
                            binding.checkkitchenn.setChecked(true);
                        }
                        if (balcony.equals("Yes")) {
                            binding.checkbalcony.setChecked(true);
                        }
                        if (indian.equals("Yes")) {
                            binding.checkindiant.setChecked(true);
                        }
                        if (western.equals("Yes")) {
                            binding.checkwesterntt.setChecked(true);
                        }
                        if (terrace.equals("Yes")) {
                            binding.checkterrace.setChecked(true);
                        }
                        if (furnished.equals("Yes")) {
                            binding.checkfullf.setChecked(true);
                        }
                        binding.facility.setText(morefaci);


                    }
                });
        FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                .collection("AllRule").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String clean = documentSnapshot.getString("clean");
                        String trouble = documentSnapshot.getString("trouble");
                        String licence = documentSnapshot.getString("licence");
                        String gateenry = documentSnapshot.getString("gateenry");
                        String alcohol = documentSnapshot.getString("alcohol");
                        String damage = documentSnapshot.getString("damage");
                        String ousiders = documentSnapshot.getString("ousiders");
                        String permission = documentSnapshot.getString("permission");
                        String morerule = documentSnapshot.getString("more");


                        if (clean.equals("Yes")) {
                            binding.clinerule.setChecked(true);
                        }
                        if (trouble.equals("Yes")) {
                            binding.nottrublerule.setChecked(true);
                        }
                        if (licence.equals("Yes")) {
                            binding.licencerule.setChecked(true);
                        }
                        if (gateenry.equals("Yes")) {
                            binding.entryrule.setChecked(true);
                        }
                        if (alcohol.equals("Yes")) {
                            binding.alcoholrule.setChecked(true);
                        }
                        if (damage.equals("Yes")) {
                            binding.damagerule.setChecked(true);
                        }
                        if (ousiders.equals("Yes")) {
                            binding.outsiderrule.setChecked(true);
                        }
                        if (permission.equals("Yes")) {
                            binding.prentperule.setChecked(true);
                        }
                        binding.rules.setText(morerule);

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
                binding.noagreetext.setVisibility(View.GONE);
            }
        });
        binding.noagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.yesagreeview.setVisibility(View.GONE);
                binding.noagreetext.setVisibility(View.VISIBLE);
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.justrehds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                Intent intent = new Intent(EditResidencyDataActivity.this, MapsActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("name", "Your residency name will fetch here");
                startActivity(intent);

            }
        });
        binding.olmapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditResidencyDataActivity.this, MapsActivity.class);
                intent.putExtra("latitude", oldlatitude);
                intent.putExtra("longitude", oldlongitude);
                intent.putExtra("name", name);
                startActivity(intent);

            }
        });
        String type = binding.rentaltype.getSelectedItem().toString();


        binding.submitresibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                String type = binding.rentaltype.getSelectedItem().toString();

                if (type.equals("Flat")) {
                    binding.flatview1.setVisibility(View.VISIBLE);
                    int ID01 = binding.flatview1.getCheckedRadioButtonId();
                    RadioButton radioButton01 = findViewById(ID01);
                    binding.viewresitypetext.setText("Flat");

                    if (radioButton01.getText().equals("1RK")) {
                        binding.viewresisubtypetext.setText("1RK");

                    }
                    if (radioButton01.getText().equals("1BHK")) {
                        binding.viewresisubtypetext.setText("1BHK");

                    }
                    if (radioButton01.getText().equals("2BHK")) {
                        binding.viewresisubtypetext.setText("2BHK");

                    }
                    if (radioButton01.getText().equals("3BHK")) {
                        binding.viewresisubtypetext.setText("3BHK");

                    }
                    if (radioButton01.getText().equals("4BHK")) {
                        binding.viewresisubtypetext.setText("4BHK");

                    }
                    if (radioButton01.getText().equals("5BHK")) {
                        binding.viewresisubtypetext.setText("5BHK");

                    }
                } else if (type.equals("Room")) {
                    binding.roomview1.setVisibility(View.VISIBLE);
                    int ID02 = binding.roomview1.getCheckedRadioButtonId();
                    RadioButton radioButton02 = findViewById(ID02);
                    binding.viewresitypetext.setText("Room");

                    if (radioButton02.getText().equals("Single Room")) {
                        binding.viewresisubtypetext.setText("Single Room");


                    }
                    if (radioButton02.getText().equals("Double Room")) {
                        binding.viewresisubtypetext.setText("Double Room");


                    }
                    if (radioButton02.getText().equals("Triple Room")) {
                        binding.viewresisubtypetext.setText("Triple Room");


                    }
                } else if (type.equals("Stutter")) {
                    binding.viewresitypetext.setText("Shutter");

                    binding.viewresisubtypetext.setText("Shutter");

                } else if (type.equals("House")) {
                    binding.viewresitypetext.setText("House");
                    binding.viewresisubtypetext.setText("House");

                } else if (type.equals("Building")) {
                    binding.viewresitypetext.setText("Building");
                    binding.viewresisubtypetext.setText("Building");

                } else if (type.equals("")){
                    String ttypee=binding.viewresitypetext.getText().toString();
                    if (ttypee.equals("Flat")){
                        int ID01 = binding.flatview1.getCheckedRadioButtonId();
                        RadioButton radioButton01 = findViewById(ID01);
                        if (radioButton01.getText().equals("1RK")) {
                            binding.viewresisubtypetext.setText("1RK");
                        }
                        if (radioButton01.getText().equals("1BHK")) {
                            binding.viewresisubtypetext.setText("1BHK");
                        }
                        if (radioButton01.getText().equals("2BHK")) {
                            binding.viewresisubtypetext.setText("2BHK");
                        }
                        if (radioButton01.getText().equals("3BHK")) {
                            binding.viewresisubtypetext.setText("3BHK");
                        }
                        if (radioButton01.getText().equals("4BHK")) {
                            binding.viewresisubtypetext.setText("4BHK");
                        }
                        if (radioButton01.getText().equals("5BHK")) {
                            binding.viewresisubtypetext.setText("5BHK");
                        }
                    } else if (ttypee.equals("Room")) {
                        int ID02 = binding.roomview1.getCheckedRadioButtonId();
                        RadioButton radioButton02 = findViewById(ID02);
                        if (radioButton02.getText().equals("Single Room")) {
                            binding.viewresisubtypetext.setText("Single Room");
                        }
                        if (radioButton02.getText().equals("Double Room")) {
                            binding.viewresisubtypetext.setText("Double Room");
                        }
                        if (radioButton02.getText().equals("Triple Room")) {
                            binding.viewresisubtypetext.setText("Triple Room");
                        }
                    } else if (ttypee.equals("Stutter")) {
                        binding.viewresisubtypetext.setText("Shutter");
                    } else if (ttypee.equals("House")) {
                        binding.viewresisubtypetext.setText("House");
                    } else if (ttypee.equals("Building")) {
                        binding.viewresisubtypetext.setText("Building");
                    }
                }

                int ID1 = binding.mainradiodeposit.getCheckedRadioButtonId();
                RadioButton radioButton11 = findViewById(ID1);

                int ID2 = binding.mainradioextra.getCheckedRadioButtonId();
                RadioButton radioButton22 = findViewById(ID2);

                int ID4 = binding.mainrediopolicy.getCheckedRadioButtonId();
                RadioButton radioButton33 = findViewById(ID4);

                if (radioButton11.getText().equals("Yes")) {
                    if (!binding.deposit.getText().toString().isEmpty()) {
                        String deposit = binding.deposit.getText().toString();

                        if (radioButton22.getText().equals("Yes")) {
                            if (!binding.extracharges.getText().toString().isEmpty()) {
                                String extra = binding.extracharges.getText().toString();

                                if (radioButton33.getText().equals("Agreement will be done")) {
                                    if (!binding.periodtime.getText().toString().isEmpty()) {
                                        int period = Integer.parseInt(binding.periodtime.getText().toString());
                                        String policy = binding.yesagreetext.getText().toString();
                                        checktext(id, deposit, extra, period, policy);
                                    } else {
                                        binding.periodtime.setError("Please enter how many months agreement will be done");
                                        dialog.dismiss();
                                        Toast.makeText(EditResidencyDataActivity.this, "Please enter how many months agreement will be done", Toast.LENGTH_SHORT).show();
                                    }


                                } else if (radioButton33.getText().equals("No Agreement, we have own rules")) {
                                    int period = 708;
                                    String policy = binding.noagreetext.getText().toString();
                                    checktext(id, deposit, extra, period, policy);

                                }

                            } else {
                                binding.extracharges.setError("Please enter extra charges details");
                                dialog.dismiss();
                                Toast.makeText(EditResidencyDataActivity.this, "Please enter extra charges details", Toast.LENGTH_SHORT).show();
                            }

                        } else if (radioButton22.getText().equals("No")) {
                            String extra = "No extra charges will taken";

                            if (radioButton33.getText().equals("Agreement will be done")) {
                                if (!binding.periodtime.getText().toString().isEmpty()) {
                                    int period = Integer.parseInt(binding.periodtime.getText().toString());

                                    String policy = binding.yesagreetext.getText().toString();

                                    checktext(id, deposit, extra, period, policy);

                                } else {
                                    binding.periodtime.setError("Please enter how many months agreement will be done");
                                    dialog.dismiss();
                                    Toast.makeText(EditResidencyDataActivity.this, "Please enter how many months agreement will be done", Toast.LENGTH_SHORT).show();
                                }


                            } else if (radioButton33.getText().equals("No Agreement, we have own rules")) {
                                int period = 708;
                                String policy = binding.noagreetext.getText().toString();
                                checktext(id, deposit, extra, period, policy);
                            }

                        }
                    } else {
                        binding.deposit.setError("Please enter deposit details");
                        dialog.dismiss();
                        Toast.makeText(EditResidencyDataActivity.this, "Please enter deposit details", Toast.LENGTH_SHORT).show();
                    }

                } else if (radioButton11.getText().equals("No")) {
                    String deposit = "No deposit will taken";

                    if (radioButton22.getText().equals("Yes")) {
                        if (!binding.extracharges.getText().toString().isEmpty()) {
                            String extra = binding.extracharges.getText().toString();

                            if (radioButton33.getText().equals("Agreement will be done")) {
                                if (!binding.periodtime.getText().toString().isEmpty()) {
                                    int period = Integer.parseInt(binding.periodtime.getText().toString());

                                    String policy = binding.yesagreetext.getText().toString();
                                    checktext(id, deposit, extra, period, policy);

                                } else {
                                    binding.periodtime.setError("Please enter how many months agreement will be done");
                                    dialog.dismiss();
                                    Toast.makeText(EditResidencyDataActivity.this, "Please enter how many months agreement will be done", Toast.LENGTH_SHORT).show();
                                }


                            } else if (radioButton33.getText().equals("No Agreement, we have own rules")) {
                                int period = 708;

                                String policy = binding.noagreetext.getText().toString();
                                checktext(id, deposit, extra, period, policy);
                            }

                        } else {
                            binding.extracharges.setError("Please enter extra charges details");
                            dialog.dismiss();
                            Toast.makeText(EditResidencyDataActivity.this, "Please enter extra charges details", Toast.LENGTH_SHORT).show();
                        }

                    } else if (radioButton22.getText().equals("No")) {
                        String extra = "No extra charges will taken";

                        if (radioButton33.getText().equals("Agreement will be done")) {
                            if (!binding.periodtime.getText().toString().isEmpty()) {
                                int period = Integer.parseInt(binding.periodtime.getText().toString());

                                String policy = binding.yesagreetext.getText().toString();
                                checktext(id, deposit, extra, period, policy);

                            } else {
                                binding.periodtime.setError("Please enter how many months agreement will be done");
                                dialog.dismiss();
                                Toast.makeText(EditResidencyDataActivity.this, "Please enter how many months agreement will be done", Toast.LENGTH_SHORT).show();
                            }


                        } else if (radioButton33.getText().equals("No Agreement, we have own rules")) {
                            int period = 708;
                            String policy = binding.noagreetext.getText().toString();
                            checktext(id, deposit, extra, period, policy);


                        }

                    }
                }


            }
        });


    }

    private void checktext(String idd, String depositz, String extraz, int periodz, String policyz) {


        String nametext = binding.resiname.getText().toString();
        String addresstext = binding.resiaddress.getText().toString();
        String onametext = binding.oname.getText().toString();
        String contacttext = binding.contact.getText().toString();
        String whatsapptext = binding.whatsapp.getText().toString();
        String renttext = binding.rentamount.getText().toString();


        if (!nametext.isEmpty() && !addresstext.isEmpty() && !onametext.isEmpty() && !contacttext.isEmpty() && !whatsapptext.isEmpty() && !renttext.isEmpty()) {




            if (newlist.isEmpty() && oldlist.isEmpty()) {
                dialog.dismiss();
                Toast.makeText(EditResidencyDataActivity.this, "Please select images", Toast.LENGTH_SHORT).show();

            }
            else if (!newlist.isEmpty() && oldlist.isEmpty()) {

                if (newlist.size() < 11) {
                    mainum = newlist.size();
                    StorageReference ImageFolder = FirebaseStorage.getInstance().getReference()
                            .child("Nanded")
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

                                                    storeLink(idd, newStrings, mainum, depositz, extraz, periodz, policyz);
                                                }

                                            }
                                        }
                                );
                            }
                        });




                    }
                } else {
                    Toast.makeText(EditResidencyDataActivity.this, "Please don't select more than 10 images", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }

            }
            else if (newlist.isEmpty() && !oldlist.isEmpty()) {

                if (oldlist.size() < 11) {
                    mainum = oldlist.size();
                    storeLink(idd,oldStrings, mainum, depositz, extraz, periodz, policyz);


                } else {
                    Toast.makeText(EditResidencyDataActivity.this, "Please don't select more than 10 images", Toast.LENGTH_SHORT).show();
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

                                                    newStrings.addAll(oldStrings);

                                                    storeLink(idd,newStrings, mainum, depositz, extraz, periodz, policyz);
                                                }

                                            }
                                        }
                                );

                            }
                        });




                    }
                } else {
                    Toast.makeText(EditResidencyDataActivity.this, "Please don't select more than 10 images", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }

            }
        }
        else {
            if (nametext.isEmpty()) {
                dialog.dismiss();

                binding.resiname.setError("Please enter residency name");
                Toast.makeText(EditResidencyDataActivity.this, "Please enter residency name", Toast.LENGTH_LONG).show();

            }
            if (addresstext.isEmpty()) {
                dialog.dismiss();

                binding.resiaddress.setError("Please enter residency address");
                Toast.makeText(EditResidencyDataActivity.this, "Please enter residency address", Toast.LENGTH_LONG).show();

            }
            if (onametext.isEmpty()) {
                dialog.dismiss();

                binding.oname.setError("Please enter residency operator name");
                Toast.makeText(EditResidencyDataActivity.this, "Please enter residency operator name", Toast.LENGTH_LONG).show();

            }
            if (contacttext.isEmpty()) {
                dialog.dismiss();

                binding.contact.setError("Please enter contact number");
                Toast.makeText(EditResidencyDataActivity.this, "Please enter contact number", Toast.LENGTH_LONG).show();

            }
            if (whatsapptext.isEmpty()) {
                dialog.dismiss();

                binding.whatsapp.setError("Please enter whatsapp number");
                Toast.makeText(EditResidencyDataActivity.this, "Please enter whatsapp number", Toast.LENGTH_LONG).show();

            }
            if (renttext.isEmpty()) {
                dialog.dismiss();

                binding.rentamount.setError("Please enter monthly rent");
                Toast.makeText(EditResidencyDataActivity.this, "Please enter monthly rent", Toast.LENGTH_LONG).show();

            }

        }

    }

    private void storeLink(String iddd, ArrayList<String> newStrings, int mainumb, String depositc, String extrac, int periodc, String policyc) {

        HashMap<String, String> hashMap = new HashMap<>();
        int i;

        for (i = 0; i < mainumb; i++) {

            hashMap.put("image" + i, newStrings.get(i));

        }

        String name = binding.resiname.getText().toString();
        String address = binding.resiaddress.getText().toString();
        String oname = binding.oname.getText().toString();
        String contact = binding.contact.getText().toString();
        String whatsapp = binding.whatsapp.getText().toString();
        String mail = binding.email.getText().toString();
        String rent = binding.rentamount.getText().toString();
        String erent = binding.explainrent.getText().toString();
        String more = binding.moredetails.getText().toString();
        String ltype = binding.viewresitypetext.getText().toString();
        String lsubtype = binding.viewresisubtypetext.getText().toString();
        String larea = binding.vieareatext.getText().toString();

        if (binding.showlocationtext.getText().toString().isEmpty()) {
            lat = oldlatitude;
            lan = oldlongitude;

        } else {
            lat = latitude;
            lan = longitude;
        }


        CollectionReference toolsCollectionRef = FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllImage");
        CollectionReference toolsCollectionRef2 = FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllData");

        toolsCollectionRef.document(iddd).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Date date = new Date();


                AddFlatClass data = new AddFlatClass(status,"Rent", ltype, lsubtype, name, name.toLowerCase(), address, larea, oname, contact, whatsapp, mail, rent, erent, depositc, extrac, more, policyc, FirebaseAuth.getInstance().getUid(), iddd,"","","",7028, mainumb, periodc, lat, lan, date.getTime());
                toolsCollectionRef2.document(iddd).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        addfacility(iddd);
                        addrules(iddd);
                        dialog.dismiss();
                        Toast.makeText(EditResidencyDataActivity.this, "Data Edited Successfully, please refresh", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditResidencyDataActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        finish();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                dialog.dismiss();

                Toast.makeText(EditResidencyDataActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditResidencyDataActivity.this, "Current location fetch successfully", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(EditResidencyDataActivity.this, "facility fail", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(EditResidencyDataActivity.this, "Rules fail", Toast.LENGTH_SHORT).show();

                    }
                });

    }
}