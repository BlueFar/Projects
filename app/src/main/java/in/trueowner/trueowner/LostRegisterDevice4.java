package in.trueowner.trueowner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class LostRegisterDevice4 extends AppCompatActivity {

    FloatingActionButton submitbutton;
    ImageView backbutton, imageupload, browse;
    String brandtemp, modeltemp, imei1temp, imei2temp, serialnotemp,mobilenotemp, picbilltemp, picprooftemp, userid;
    String name, mobileno;
    public long day1, month1, year1,price1;
    Uri picbill, picproof ;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;

    private static final int    Camera_Request_code = 1, Read_Request_code = 3;
    private int CAMERA_PERMISSION_CODE = 2, READ_PERMISSION_CODE = 4, WRITE_PERMISSION_CODE = 6;
    Boolean camera = false, write = false, read = false;

    private ProgressDialog mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_register_device4);

        Intent intent = getIntent();
        brandtemp = intent.getExtras().getString("Brand");
        modeltemp = intent.getExtras().getString("Model");
        imei1temp = intent.getExtras().getString("IMEI1");
        imei2temp = intent.getExtras().getString("IMEI2");
        serialnotemp = intent.getExtras().getString("SerialNo");
        day1 = intent.getExtras().getLong("DayPurchase");
        month1 = intent.getExtras().getLong("MonthPurchase");
        year1 = intent.getExtras().getLong("YearPurchase");
        price1 = intent.getExtras().getLong("PricePurchase");
        mobilenotemp = intent.getExtras().getString("MobileNo");
        picbill = Uri.parse(intent.getExtras().getString("PicBill"));
        picbilltemp = String.valueOf(picbill);
        backbutton = (ImageView) findViewById(R.id.lost_register_back_button4);
        imageupload = (ImageView) findViewById(R.id.lost_register_image_upload);
        browse = (ImageView) findViewById(R.id.lost_register_browse);
        mprogress = new ProgressDialog(this);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user !=null) {

            userid = user.getUid();

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23) {

                    if (ContextCompat.checkSelfPermission(LostRegisterDevice4.this,
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(LostRegisterDevice4.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File photoFile = null;
                        photoFile = createPhotoFile();


                        if (photoFile != null) {



                            String pathToFile = photoFile.getAbsolutePath();
                            picproof = FileProvider.getUriForFile(LostRegisterDevice4.this, "in.trueowner.fileprovider", photoFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, picproof);
                            startActivityForResult(intent, Camera_Request_code);

                        }


                    } else {

                        requestCameraPermission();
                        requestWritePermission();
                    }

                }

            }
        });

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23){

                    if (ContextCompat.checkSelfPermission(LostRegisterDevice4.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent, Read_Request_code);

                    } else {
                        requestStoragePermission();
                    }

                }

            }
        });
        } else {

            Intent intent1 = new Intent(LostRegisterDevice4.this, LoginPage.class);
            startActivity(intent1);
            finish();

        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user !=null){

            userid = user.getUid();

        } else {

            Intent intent1 = new Intent(LostRegisterDevice4.this, LoginPage.class);
            startActivity(intent1);
            finish();

        }

    }
    private File createPhotoFile() {

        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(name, ".jpg", storageDir);
        } catch (IOException e) {
            Log.d("my log", "Excep : " + e.toString());
        }
        return image;

    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("Permission needed");
            builder1.setMessage("Permission required to access camera");
            builder1.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(LostRegisterDevice4.this,
                            new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                }
            });
            builder1.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert11 = builder1.create();
            alert11.show();

            Button buttonbackground = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
            buttonbackground.setTextColor(Color.BLACK);

            Button buttonbackground1 = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
            buttonbackground1.setTextColor(Color.BLACK);

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("Permission needed");
            builder1.setMessage("Permission required to access Gallery");
            builder1.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(LostRegisterDevice4.this,
                            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION_CODE);
                }
            });
            builder1.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert11 = builder1.create();
            alert11.show();

            Button buttonbackground = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
            buttonbackground.setTextColor(Color.BLACK);

            Button buttonbackground1 = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
            buttonbackground1.setTextColor(Color.BLACK);

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION_CODE);
        }
    }

    private void requestWritePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("Permission needed");
            builder1.setMessage("Permission required to store image");
            builder1.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(LostRegisterDevice4.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_CODE);
                }
            });
            builder1.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert11 = builder1.create();
            alert11.show();

            Button buttonbackground = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
            buttonbackground.setTextColor(Color.BLACK);

            Button buttonbackground1 = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
            buttonbackground1.setTextColor(Color.BLACK);

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                camera = true;
                finaloutcome();

            } else {


            }
        }

        if (requestCode == READ_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                read = true;
                finaloutcome1();

            } else {


            }
        }

        if (requestCode == WRITE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                write = true;
                finaloutcome();


            } else {


            }
        }

    }

    private void finaloutcome() {

        if (camera.equals(true) && write.equals(true)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photoFile = null;
            photoFile = createPhotoFile();

            if (photoFile != null) {

                String pathToFile = photoFile.getAbsolutePath();
                picproof = FileProvider.getUriForFile(LostRegisterDevice4.this, "in.trueowner.fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, picproof);
                startActivityForResult(intent, Camera_Request_code);

            }

        }
        else {


        }

    }

    private void finaloutcome1() {

        if (read.equals(true)) {

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, Read_Request_code);




        }

        else {

            Toast.makeText(LostRegisterDevice4.this, "Dont have the permission", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Camera_Request_code && resultCode == RESULT_OK){

            if (picproof != null) {

                Toast.makeText(LostRegisterDevice4.this, "Image selected", Toast.LENGTH_SHORT).show();

                picbilltemp = String.valueOf(picproof);
                mprogress.setMessage("Saving Details...");
                mprogress.show();

                db.collection("TrackDevice").whereEqualTo("Imei1", imei1temp)
                        .limit(1).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if(task.getResult().isEmpty()){

                                        DocumentReference dref = db.collection("Users").document(userid).collection("Details").document("PersonalDetails");
                                        dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                name = documentSnapshot.getString("FullName");
                                                mobileno = documentSnapshot.getString("MobileNumber");

                                            }
                                        });

                                        Map<String, Object> productdetails = new HashMap<>();
                                        productdetails.put("ProductName", brandtemp+" "+modeltemp);
                                        productdetails.put("Imei1", imei1temp);
                                        productdetails.put("Imei2", imei2temp);
                                        productdetails.put("SerialNumber", serialnotemp);
                                        productdetails.put("PurchaseDay", day1);
                                        productdetails.put("PurchaseMonth", month1);
                                        productdetails.put("PurchaseYear", year1);
                                        productdetails.put("PurchasedPrice", price1);
                                        productdetails.put("MobileNumber", mobileno);
                                        productdetails.put("FullName", name);
                                        productdetails.put("PicBill", picbilltemp);
                                        productdetails.put("PicProof", picprooftemp);
                                        productdetails.put("UserID", userid);
                                        String documentid =  db.collection("TrackDevice").document().getId();
                                        productdetails.put("DocumentID", documentid);
                                        DocumentReference dr = db.collection("TrackDevice").document(documentid);
                                        dr.set(productdetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Intent intent = new Intent(LostRegisterDevice4.this, LostRegisterSuccessPage.class);
                                                mprogress.dismiss();
                                                startActivity(intent);
                                                finish();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                Toast.makeText(LostRegisterDevice4.this, "Unable to register at this time", Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                    } else {

                                        Toast.makeText(LostRegisterDevice4.this, "Product already registered as lost", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LostRegisterDevice4.this, TrackDevicePage.class);
                                        mprogress.dismiss();
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                        });


           /* intent.putExtra("Brand",brandtemp);
            intent.putExtra("Model",modeltemp);
            intent.putExtra("IMEI1",imei1temp);
            intent.putExtra("IMEI2",imei2temp);
            intent.putExtra("SerialNo",serialnotemp);
            intent.putExtra("DayPurchase",day1);
            intent.putExtra("MonthPurchase",month1);
            intent.putExtra("YearPurchase",year1);
            intent.putExtra("PricePurchase",price1);
            intent.putExtra("MobileNo",mobilenotemp);
                intent.putExtra("PicBill", picbilltemp);*/


            }

            else {

                Toast.makeText(LostRegisterDevice4.this, "Image not selected", Toast.LENGTH_SHORT).show();

            }

        }

        else if (requestCode == Read_Request_code && resultCode == RESULT_OK){

            picbill = data.getData();

            if (picproof != null) {

                Toast.makeText(LostRegisterDevice4.this, "Image selected", Toast.LENGTH_SHORT).show();

                picbilltemp = String.valueOf(picproof);
                mprogress.setMessage("Saving Details...");
                mprogress.show();

                db.collection("TrackDevice").whereEqualTo("Imei1", imei1temp)
                        .limit(1).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if(task.getResult().isEmpty()){

                                        DocumentReference dref = db.collection("Users").document(userid).collection("Details").document("PersonalDetails");
                                        dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                name = documentSnapshot.getString("FullName");
                                                mobileno = documentSnapshot.getString("MobileNumber");

                                            }
                                        });

                                        Map<String, Object> productdetails = new HashMap<>();
                                        productdetails.put("ProductName", brandtemp+" "+modeltemp);
                                        productdetails.put("Imei1", imei1temp);
                                        productdetails.put("Imei2", imei2temp);
                                        productdetails.put("SerialNumber", serialnotemp);
                                        productdetails.put("PurchaseDay", day1);
                                        productdetails.put("PurchaseMonth", month1);
                                        productdetails.put("PurchaseYear", year1);
                                        productdetails.put("PurchasedPrice", price1);
                                        productdetails.put("MobileNumber", mobileno);
                                        productdetails.put("FullName", name);
                                        productdetails.put("PicBill", picbilltemp);
                                        productdetails.put("PicProof", picprooftemp);
                                        productdetails.put("UserID", userid);
                                        String documentid =  db.collection("TrackDevice").document().getId();
                                        productdetails.put("DocumentID", documentid);
                                        DocumentReference dr = db.collection("TrackDevice").document(documentid);
                                        dr.set(productdetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Intent intent = new Intent(LostRegisterDevice4.this, LostRegisterSuccessPage.class);
                                                mprogress.dismiss();
                                                startActivity(intent);
                                                finish();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                Toast.makeText(LostRegisterDevice4.this, "Unable to register at this time", Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                    }
                                }
                            }
                        });


           /* intent.putExtra("Brand",brandtemp);
            intent.putExtra("Model",modeltemp);
            intent.putExtra("IMEI1",imei1temp);
            intent.putExtra("IMEI2",imei2temp);
            intent.putExtra("SerialNo",serialnotemp);
            intent.putExtra("DayPurchase",day1);
            intent.putExtra("MonthPurchase",month1);
            intent.putExtra("YearPurchase",year1);
            intent.putExtra("PricePurchase",price1);
            intent.putExtra("MobileNo",mobilenotemp);
                intent.putExtra("PicBill", picbilltemp);*/

            }
            else {

                Toast.makeText(LostRegisterDevice4.this, "Image not selected", Toast.LENGTH_SHORT).show();

            }

        }
    }
}
