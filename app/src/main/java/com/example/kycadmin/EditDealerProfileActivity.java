package com.example.kycadmin;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.storage.UploadTask;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditDealerProfileActivity extends AppCompatActivity {

    public static final String mIntentId = "0";
    String mSelectedId;
    TextView textViewEnterpriseName;
    TextView textViewEnterpriseContactPerson;
    TextView textViewEnterpriseContactNumber;
    TextView textViewAadhar;
    ImageView imageViewAadhar;
    ImageView imageViewPAN;
    ImageView imageViewDealer;
    ImageView imageViewCheque;
    ImageView imageViewLicence;
    ImageView imageViewAddressProof;
    ImageView imageViewAgreement;
    TextView textViewPan;
    TextView textViewGSTIN;
    TextView textViewAddr1;
    TextView textViewAddr2;
    TextView textViewAddr3;
    Button btnAadharUpload;
    Button btnPANUpload;
    Button btnChequeUpload;
    Button btnDealerUpload;
    Button btnTradeUpload;
    Button btnAgreementUpload;
    Button btnAddressUpload;
    StorageReference storageReference;

    File mediaStorageDirUser = null;
    FusedLocationProviderClient client;

    private Uri panUri, aadharUri, dealerUri, tradeUri, addressUri, agreement, fileUri, chequeUri;
    Bitmap trade, pan, aadhar_img, dealer, cheque, address_img, agreement_img;

    Dealer dealer_obj;

    int document;


    public static final int PICKFILE_RESULT_CODE = 1;
    private static final int CAMERA_REQUEST = 1888, GALLERY_REQUEST = 1889;
    private static final int MY_CAMERA_PERMISSION_CODE = 100, MY_GALLERY_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dealer_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestPermissionCamera();
        requestPermissionGPS();

        dealer_obj = new Dealer();

        textViewEnterpriseName = (TextView) findViewById(R.id.edit_profile_enterprise_name);
        textViewEnterpriseContactPerson = (TextView) findViewById(R.id.edit_profile_contact_person);
        textViewEnterpriseContactNumber = (TextView) findViewById(R.id.edit_profile_contact_number);
        textViewAadhar = (TextView) findViewById(R.id.edit_profile_aadhar);
        imageViewAadhar = (ImageView) findViewById(R.id.edit_profile_aadhar_picture);
        imageViewPAN = (ImageView) findViewById(R.id.edit_profile_pan_picture);
        imageViewDealer = (ImageView) findViewById(R.id.edit_profile_dealer_picture);
        imageViewCheque = (ImageView) findViewById(R.id.edit_profile_cheque_picture);
        imageViewLicence = (ImageView) findViewById(R.id.edit_profile_licence_picture);
        imageViewAddressProof = (ImageView) findViewById(R.id.edit_profile_address_proof_picture);
        imageViewAgreement = (ImageView) findViewById(R.id.edit_profile_agreement_picture);
        btnAadharUpload = (Button) findViewById(R.id.edit_profile_upload_aadhar);
        textViewPan = (TextView) findViewById(R.id.edit_profile_pan);
        textViewPan.setFilters(new InputFilter[]{new InputFilter.AllCaps()});;
        btnPANUpload = (Button) findViewById(R.id.edit_profile_upload_pan);
        btnAddressUpload = (Button) findViewById(R.id.edit_profile_upload_address_proof_picture);
        btnTradeUpload = (Button) findViewById(R.id.edit_profile_upload_licence_picture);
        btnDealerUpload = (Button) findViewById(R.id.edit_profile_upload_dealer_picture);
        btnAgreementUpload = (Button) findViewById(R.id.edit_profile_upload_agreement_picture);
        btnChequeUpload = (Button) findViewById(R.id.edit_profile_upload_cheque_picture);

        textViewGSTIN = (TextView) findViewById(R.id.edit_profile_gstin);
        textViewGSTIN.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        textViewAddr1 = (TextView) findViewById(R.id.edit_profile_addr1);
        textViewAddr2 = (TextView) findViewById(R.id.edit_profile_addr2);
        textViewAddr3 = (TextView) findViewById(R.id.edit_profile_addr3);

        textViewAadhar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!textViewAadhar.getText().toString().equals(dealer_obj.getAadhar()) && textViewAadhar.getText().toString().length() == 12)
                    btnAadharUpload.setVisibility(View.VISIBLE);
                else
                    btnAadharUpload.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textViewPan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!textViewPan.getText().toString().equals(dealer_obj.getPan()) && textViewPan.getText().toString().length() == 10)
                    btnPANUpload.setVisibility(View.VISIBLE);
                else
                    btnPANUpload.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnAadharUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aadharUri = getOutputMediaFileUri(EditDealerProfileActivity.this, aadhar_img);
                selectImage(3);
            }
        });

        btnPANUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panUri = getOutputMediaFileUri(EditDealerProfileActivity.this, pan);
                selectImage(2);
            }
        });

        btnTradeUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradeUri = getOutputMediaFileUri(EditDealerProfileActivity.this, trade);
                selectImage(1);
            }
        });

        btnDealerUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealerUri = getOutputMediaFileUri(EditDealerProfileActivity.this, dealer);
                selectImage(5);
            }
        });

        btnChequeUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chequeUri = getOutputMediaFileUri(EditDealerProfileActivity.this, cheque);
                selectImage(7);
            }
        });

        btnAddressUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
//                chooseFile.setType("*/*");
//                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
//                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
//                document = 4;
                addressUri = getOutputMediaFileUri(EditDealerProfileActivity.this, address_img);
                selectImage(4);
            }
        });

        btnAgreementUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
//                chooseFile.setType("*/*");
//                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
//                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
//                document = 6;
                agreement = getOutputMediaFileUri(EditDealerProfileActivity.this, agreement_img);
                selectImage(6);
            }
        });

        Intent intent = getIntent();
        final Context context = this;
        mSelectedId = intent.getStringExtra(mIntentId);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("dealers").orderByChild("id").equalTo(mSelectedId)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        dealer_obj.setName(String.valueOf(ds.getValue(Dealer.class).getName().toString()));
                        dealer_obj.setContact(String.valueOf(ds.getValue(Dealer.class).getContact().toString()));
                        dealer_obj.setEnterpriseName(ds.getValue(Dealer.class).getEnterpriseName());
                        dealer_obj.setAddr1(ds.getValue(Dealer.class).getAddr1());
                        dealer_obj.setAddr2(ds.getValue(Dealer.class).getAddr2());
                        dealer_obj.setAddr3(ds.getValue(Dealer.class).getAddr3());
                        dealer_obj.setGstin(ds.getValue(Dealer.class).getGstin());
                        dealer_obj.setPan(ds.getValue(Dealer.class).getPan());
                        dealer_obj.setLat(ds.getValue(Dealer.class).getLat());
                        dealer_obj.setLon(ds.getValue(Dealer.class).getLon());
                        dealer_obj.setId(ds.getValue(Dealer.class).getId());
                        dealer_obj.setAadhar(ds.getValue(Dealer.class).getAadhar());
                        dealer_obj.setAddedBy(ds.getValue(Dealer.class).getAddedBy());
                        textViewEnterpriseName.setText(dealer_obj.getEnterpriseName());
                        textViewEnterpriseContactPerson.setText(dealer_obj.getName());
                        textViewEnterpriseContactNumber.setText(dealer_obj.getContact());
                        textViewAadhar.setText(dealer_obj.getAadhar());
                        textViewPan.setText(dealer_obj.getPan());
                        textViewGSTIN.setText(dealer_obj.getGstin());
                        textViewAddr1.setText(dealer_obj.getAddr1());
                        textViewAddr2.setText(dealer_obj.getAddr2());
                        textViewAddr3.setText(dealer_obj.getAddr3());
                        displayInitialPictures("aadhar", imageViewAadhar);
                        displayInitialPictures("pan", imageViewPAN);
                        displayInitialPictures("trade", imageViewLicence);
                        displayInitialPictures("dealer", imageViewDealer);
                        displayInitialPictures("cheque", imageViewCheque);
                        displayInitialPictures("address", imageViewAddressProof);
                        displayInitialPictures("agreement", imageViewAgreement);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            }
        );

    }

    public void displayInitialPictures(String type, final ImageView imageView){
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference mImageRef = storageReference.child("media/"+dealer_obj.getEnterpriseName().replace(" ","")+"/"+type);
        mImageRef.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        System.out.println(uri);
                        Glide.with(getApplicationContext()).load(uri).into(imageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println();
            }
        });
    }

    private void selectImage(final int doc) {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditDealerProfileActivity.this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            //dict.put(type, getOutputMediaFileUri(EditDealerProfileActivity.this, dict_bitmap.get(bitmap_type)));
                            document = doc;
                            startActivityForResult(intent, CAMERA_REQUEST);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            //Intent intent = new Intent();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                            //dict.put(type, getOutputMediaFileUri(EditDealerProfileActivity.this, dict_bitmap.get(bitmap_type)));
                            document = doc;
                            startActivityForResult(pickPhoto, GALLERY_REQUEST);
                            //intent.setType("image/*");
                            //intent.setAction(Intent.ACTION_GET_CONTENT);
                            //startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            String message = e.getMessage();
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public Uri getOutputMediaFileUri(Context context, Bitmap bitmap) {
        File mediaStorageDir = new File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Camera");
        //If File is not present create directory
        if (!mediaStorageDir.exists()) {
            if (mediaStorageDir.mkdir())
                Log.e("Create Directory", "Main Directory Created : " + mediaStorageDir);
        }
        mediaStorageDirUser = new File(
                mediaStorageDir, dealer_obj.getEnterpriseName());
        //If File is not present create directory
        if (!mediaStorageDirUser.exists()) {
            if (mediaStorageDirUser.mkdir())
                Log.e("Create Directory", "Main Directory Created : " + mediaStorageDir);
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());//Get Current timestamp
        File mediaFile = new File(mediaStorageDirUser.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");//create image path with system mill and image format

        return Uri.fromFile(mediaFile);

    }

    public void onClickCancel(View v){
        Intent intent =  new Intent(EditDealerProfileActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickUpdate(View v){
        final String enterpriseName = textViewEnterpriseName.getText().toString();
        final String enterpriseContactPerson = textViewEnterpriseContactPerson.getText().toString();
        final String enterpriseContactNumber = textViewEnterpriseContactNumber.getText().toString();
        final String aadhar = textViewAadhar.getText().toString();
        final String pan = textViewPan.getText().toString();
        final String gSTIN = textViewGSTIN.getText().toString();
        final String addr1 = textViewAddr1.getText().toString();
        final String addr2 = textViewAddr2.getText().toString();
        final String addr3 = textViewAddr3.getText().toString();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("dealers").orderByChild("id").equalTo(mSelectedId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren()) {
                            ds.getRef().child("enterpriseName").setValue(enterpriseName);
                            ds.getRef().child("name").setValue(enterpriseContactPerson);
                            ds.getRef().child("contact").setValue(enterpriseContactNumber);
                            ds.getRef().child("aadhar").setValue(aadhar);
                            ds.getRef().child("pan").setValue(pan);
                            ds.getRef().child("gstin").setValue(gSTIN);
                            ds.getRef().child("addr1").setValue(addr1);
                            ds.getRef().child("addr2").setValue(addr2);
                            ds.getRef().child("addr3").setValue(addr3);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

//        Upload data to Storage
        uploadFile(aadharUri, "aadhar");
        uploadFile(panUri, "pan");
        uploadFile(dealerUri, "dealer");
        uploadFile(tradeUri, "trade");
        uploadFile(chequeUri, "cheque");
        uploadFile(addressUri, "address");
        uploadFile(agreement, "agreement");

        Toast toast = Toast.makeText(EditDealerProfileActivity.this, "Updated Successfully!", Toast.LENGTH_LONG);
        toast.show();
        Intent intent =  new Intent(EditDealerProfileActivity.this, MainActivity.class);
        startActivity(intent);
    }



    private void requestPermissionGPS(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    private void requestPermissionCamera(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else{
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
        else{
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent("android.location.GPS_ENABLED_CHANGE");
                intent.putExtra("enabled", true);
                sendBroadcast(intent);
            }
            else{
                Toast.makeText(this, "GPS permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void uploadFile(Uri filePath, String type) {

        if (filePath != null) {
// final ProgressDialog progressDialog = new ProgressDialog(this);
// progressDialog.setTitle("Uploading...");
// progressDialog.show();

            StorageReference ref = storageReference.child("media/" + dealer_obj.getEnterpriseName().replace(" ", "") + "/" + type);
            putFile(ref, filePath);
        }
    }

    private void putFile(StorageReference ref, Uri filePath){
        ref.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//Toast.makeText(CreatingProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ERROR", e.toString());
//Toast.makeText(CreatingProfileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST || requestCode == GALLERY_REQUEST) {
            //String imgPath = imgUri.getPath();
            switch (document) {
                case 1:
                    if (requestCode == CAMERA_REQUEST) {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        trade = photo;
                        if (photo != null) {
                            String[] el = tradeUri.getPath().split("/", 15);
                            File myImage = new File(mediaStorageDirUser, el[el.length - 1]);
                            try {
                                FileOutputStream fOut = new FileOutputStream(myImage);
                                trade.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            imageViewLicence.setImageBitmap(trade);
                        }
                    } else if (requestCode == GALLERY_REQUEST) {
                        tradeUri = data.getData();
                        //Log.d(TAG, String.valueOf(selectedImage));
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), tradeUri);
                            trade = bitmap;
                            imageViewLicence.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        File myImage = new File(mediaStorageDirUser, tradeUri.getPath().split("/", 15)[10]);
//                        try {
//                            FileOutputStream fOut = new FileOutputStream(myImage);
//                            trade.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//                            fOut.flush();
//                            fOut.close();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                    }
                    break;
                case 2:
                    if (requestCode == CAMERA_REQUEST) {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        pan = photo;
                        if (photo != null) {
                            String[] el = panUri.getPath().split("/", 15);
                            File myImage = new File(mediaStorageDirUser, el[el.length - 1]);
                            try {
                                FileOutputStream fOut = new FileOutputStream(myImage);
                                pan.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            imageViewPAN.setImageBitmap(pan);
                        }
                    } else if (requestCode == GALLERY_REQUEST) {
                        panUri = data.getData();
                        //Log.d(TAG, String.valueOf(selectedImage));
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), panUri);
                            pan = bitmap;
                            imageViewPAN.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        File myImage = new File(mediaStorageDirUser, panUri.getPath().split("/", 15)[8]);
//                        try {
//                            FileOutputStream fOut = new FileOutputStream(myImage);
//                            pan.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//                            fOut.flush();
//                            fOut.close();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                    }
                    break;
                case 3:
                    if (requestCode == CAMERA_REQUEST) {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        aadhar_img = photo;
                        if (photo != null) {
                            String[] el = aadharUri.getPath().split("/", 15);
                            File myImage = new File(mediaStorageDirUser, el[el.length - 1]);
                            try {
                                FileOutputStream fOut = new FileOutputStream(myImage);
                                aadhar_img.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            imageViewAadhar.setImageBitmap(aadhar_img);
                        }
                    } else if (requestCode == GALLERY_REQUEST) {
                        aadharUri = data.getData();
                        //Log.d(TAG, String.valueOf(selectedImage));
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), aadharUri);
                            aadhar_img = bitmap;
                            imageViewAadhar.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        File myImage = new File(mediaStorageDirUser, aadharUri.getPath().split("/", 15)[11]);
//                        try {
//                            FileOutputStream fOut = new FileOutputStream(myImage);
//                            aadhar_img.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//                            fOut.flush();
//                            fOut.close();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                    }
                    break;
                case 4:
                    if (requestCode == CAMERA_REQUEST) {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        address_img = photo;
                        if (photo != null) {
                            String[] el = addressUri.getPath().split("/", 15);
                            File myImage = new File(mediaStorageDirUser, el[el.length - 1]);
                            try {
                                FileOutputStream fOut = new FileOutputStream(myImage);
                                address_img.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            imageViewAddressProof.setImageBitmap(address_img);
                        }
                    } else if (requestCode == GALLERY_REQUEST) {
                        addressUri = data.getData();
                        //Log.d(TAG, String.valueOf(selectedImage));
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), addressUri);
                            address_img = bitmap;
                            imageViewAddressProof.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        File myImage = new File(mediaStorageDirUser, aadharUri.getPath().split("/", 15)[11]);
//                        try {
//                            FileOutputStream fOut = new FileOutputStream(myImage);
//                            aadhar_img.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//                            fOut.flush();
//                            fOut.close();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                    }
                    break;
                case 5:
                    if (requestCode == CAMERA_REQUEST) {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        dealer = photo;
                        if (photo != null) {
                            String[] el = dealerUri.getPath().split("/", 15);
                            File myImage = new File(mediaStorageDirUser, el[el.length - 1]);
                            try {
                                FileOutputStream fOut = new FileOutputStream(myImage);
                                dealer.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            imageViewDealer.setImageBitmap(dealer);
                        }
                    } else if (requestCode == GALLERY_REQUEST) {
                        dealerUri = data.getData();
                        //Log.d(TAG, String.valueOf(selectedImage));
//                        File myImage = new File(mediaStorageDirUser, dealerUri.getPath().split("/", 15)[10]);
//                        try {
//                            FileOutputStream fOut = new FileOutputStream(myImage);
//                            dealer.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//                            fOut.flush();
//                            fOut.close();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dealerUri);
                            //System.out.println();
                            dealer = bitmap;
                            imageViewDealer.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 6:
                    if (requestCode == CAMERA_REQUEST) {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        agreement_img = photo;
                        if (photo != null) {
                            String[] el = agreement.getPath().split("/", 15);
                            File myImage = new File(mediaStorageDirUser, el[el.length - 1]);
                            try {
                                FileOutputStream fOut = new FileOutputStream(myImage);
                                agreement_img.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            imageViewAgreement.setImageBitmap(agreement_img);
                        }
                    } else if (requestCode == GALLERY_REQUEST) {
                        agreement = data.getData();
                        //Log.d(TAG, String.valueOf(selectedImage));
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), agreement);
                            agreement_img = bitmap;
                            imageViewAgreement.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        File myImage = new File(mediaStorageDirUser, aadharUri.getPath().split("/", 15)[11]);
//                        try {
//                            FileOutputStream fOut = new FileOutputStream(myImage);
//                            aadhar_img.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//                            fOut.flush();
//                            fOut.close();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                    }
                    break;
                case 7:
                    if (requestCode == CAMERA_REQUEST) {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        cheque = photo;
                        if (photo != null) {
                            String[] el = chequeUri.getPath().split("/", 15);
                            File myImage = new File(mediaStorageDirUser, el[el.length - 1]);
                            try {
                                FileOutputStream fOut = new FileOutputStream(myImage);
                                cheque.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            imageViewCheque.setImageBitmap(cheque);
                        }
                    } else if (requestCode == GALLERY_REQUEST) {
                        chequeUri = data.getData();
                        //Log.d(TAG, String.valueOf(selectedImage));
//                        File myImage = new File(mediaStorageDirUser, chequeUri.getPath().split("/", 15)[10]);
//                        try {
//                            FileOutputStream fOut = new FileOutputStream(myImage);
//                            cheque.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//                            fOut.flush();
//                            fOut.close();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), chequeUri);
                            cheque = bitmap;
                            imageViewCheque.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default: super.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }
//        switch (requestCode) {
//            case PICKFILE_RESULT_CODE:
//                if (resultCode == -1) {
//                    fileUri = data.getData();
//                    switch (document) {
//                        case 4:
//                            addressUri = fileUri;
//                            break;
//                        case 6:
//                            agreement = fileUri;
//                            break;
//                    }
//                }
//
//                break;
//        }
    }


}
