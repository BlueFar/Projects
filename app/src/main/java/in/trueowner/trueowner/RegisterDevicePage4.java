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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class RegisterDevicePage4 extends AppCompatActivity {

    FloatingActionButton submitbutton;
    ImageView backbutton, imageupload, browse;
    String brandtemp, modeltemp, imei1temp, imei2temp, serialnotemp,mobilenotemp, picbilltemp, picprooftemp, productname;
    public long day1, month1, year1,price1;
    Uri picbill, picproof ;
    String userid, productid;
    Boolean verificationstatus = false, salestatus = false,stolenstatus = false, read = false;;

    private static final int    Camera_Request_code = 1, Read_Request_code = 3;
    private int CAMERA_PERMISSION_CODE = 2, READ_PERMISSION_CODE = 4, WRITE_PERMISSION_CODE = 6;
    Boolean camera = false, write = false;

    private ProgressDialog mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_device_page4);

        backbutton = (ImageView) findViewById(R.id.register_back_button4);
        imageupload = (ImageView) findViewById(R.id.register_image_upload);
        browse = (ImageView) findViewById(R.id.register_browse);

        mprogress = new ProgressDialog(this);


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

                    if (ContextCompat.checkSelfPermission(RegisterDevicePage4.this,
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(RegisterDevicePage4.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File photoFile = null;
                        photoFile = createPhotoFile();


                        if (photoFile != null) {



                            String pathToFile = photoFile.getAbsolutePath();
                            picproof = FileProvider.getUriForFile(RegisterDevicePage4.this, "in.trueowner.fileprovider", photoFile);
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

                    if (ContextCompat.checkSelfPermission(RegisterDevicePage4.this,
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(RegisterDevicePage4.this,
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
                    ActivityCompat.requestPermissions(RegisterDevicePage4.this,
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
                    ActivityCompat.requestPermissions(RegisterDevicePage4.this,
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
                    ActivityCompat.requestPermissions(RegisterDevicePage4.this,
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
                picproof = FileProvider.getUriForFile(RegisterDevicePage4.this, "in.trueowner.fileprovider", photoFile);
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

            Toast.makeText(RegisterDevicePage4.this, "Dont have the permission", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Camera_Request_code && resultCode == RESULT_OK){

            if (picproof != null) {

                Toast.makeText(RegisterDevicePage4.this, "Image selected", Toast.LENGTH_SHORT).show();

                mprogress.setMessage("Saving Details...");
                mprogress.show();

                Long purchaseamount;


                if (price1<5001){

                    purchaseamount =Long.valueOf(50);

                }

                else if (price1>5000 && price1<10001){

                    purchaseamount =Long.valueOf(100);

                }
                else if (price1>10000 && price1<15001){

                    purchaseamount =Long.valueOf(150);

                }

                else if (price1>15000 && price1<20001){

                    purchaseamount =Long.valueOf(200);

                }

                else if (price1>20000 && price1<25001){

                    purchaseamount =Long.valueOf(250);

                }

                else if (price1>25000 && price1<30001){

                    purchaseamount =Long.valueOf(300);

                }

                else if (price1>30000 && price1<35001){

                    purchaseamount =Long.valueOf(350);

                }

                else if (price1>35000 && price1<40001){

                    purchaseamount =Long.valueOf(400);

                }

                else if (price1>40000 && price1<45001){

                    purchaseamount =Long.valueOf(450);

                }

                else if (price1>45000 && price1<50001){

                    purchaseamount =Long.valueOf(500);

                }

                else if (price1>50000 && price1<55001){

                    purchaseamount =Long.valueOf(550);

                }

                else if (price1>55000 && price1<60001){

                    purchaseamount =Long.valueOf(600);

                }

                else if (price1>60000 && price1<65001){

                    purchaseamount =Long.valueOf(650);

                }

                else if (price1>65000 && price1<70001){

                    purchaseamount =Long.valueOf(700);

                }

                else if (price1>70000 && price1<75001){

                    purchaseamount =Long.valueOf(750);

                }

                else if (price1>75000 && price1<80001){

                    purchaseamount =Long.valueOf(800);

                }

                else if (price1>80000 && price1<85001){

                    purchaseamount =Long.valueOf(850);

                }

                else if (price1>85000 && price1<90001){

                    purchaseamount =Long.valueOf(900);

                }

                else if (price1>90000 && price1<95001){

                    purchaseamount =Long.valueOf(950);

                }

                else if (price1>95000 && price1<100001){

                    purchaseamount =Long.valueOf(1000);

                }

               else if (price1>100000){

                    purchaseamount =Long.valueOf(1100);

                }
                Intent intent = new Intent(RegisterDevicePage4.this, RegisterDevicePage4.class);
           /* intent.putExtra("Brand",brandtemp);
            intent.putExtra("Model",modeltemp);
            intent.putExtra("IMEI1",imei1temp);
            intent.putExtra("IMEI2",imei2temp);
            intent.putExtra("SerialNo",serialnotemp);
            intent.putExtra("DayPurchase",day1);
            intent.putExtra("MonthPurchase",month1);
            intent.putExtra("YearPurchase",year1);
            intent.putExtra("PricePurchase",price1);
            intent.putExtra("SubscriptionAmount",purchaseamount);
            intent.putExtra("MobileNo",mobilenotemp);
                intent.putExtra("PicBill", picbilltemp);
                intent.putExtra("PicProof", picproof.toString())
                intent.putExtra("VerificationStatus", verificationstatus.toString())
                intent.putExtra("SaleStatus", salestatus.toString())
                intent.putExtra("StolenStatus", stolenstatus.toString())
                intent.putExtra("UserID", userid);
                intent.putExtra("ProductID", productid);
                intent.putExtra("AdminID", "");
                */
                mprogress.dismiss();
                startActivity(intent);

            }
            else {

                Toast.makeText(RegisterDevicePage4.this, "Image not selected", Toast.LENGTH_SHORT).show();

            }

        }

        else if (requestCode == Read_Request_code && resultCode == RESULT_OK){

            picbill = data.getData();

            if (picproof != null) {

                Toast.makeText(RegisterDevicePage4.this, "Image selected", Toast.LENGTH_SHORT).show();

                mprogress.setMessage("Saving Details...");
                mprogress.show();

                Long purchaseamount;

                if (price1<5001){

                    purchaseamount =Long.valueOf(50);

                }

                else if (price1>5000 && price1<10001){

                    purchaseamount =Long.valueOf(100);

                }
                else if (price1>10000 && price1<15001){

                    purchaseamount =Long.valueOf(150);

                }

                else if (price1>15000 && price1<20001){

                    purchaseamount =Long.valueOf(200);

                }

                else if (price1>20000 && price1<25001){

                    purchaseamount =Long.valueOf(250);

                }

                else if (price1>25000 && price1<30001){

                    purchaseamount =Long.valueOf(300);

                }

                else if (price1>30000 && price1<35001){

                    purchaseamount =Long.valueOf(350);

                }

                else if (price1>35000 && price1<40001){

                    purchaseamount =Long.valueOf(400);

                }

                else if (price1>40000 && price1<45001){

                    purchaseamount =Long.valueOf(450);

                }

                else if (price1>45000 && price1<50001){

                    purchaseamount =Long.valueOf(500);

                }

                else if (price1>50000 && price1<55001){

                    purchaseamount =Long.valueOf(550);

                }

                else if (price1>55000 && price1<60001){

                    purchaseamount =Long.valueOf(600);

                }

                else if (price1>60000 && price1<65001){

                    purchaseamount =Long.valueOf(650);

                }

                else if (price1>65000 && price1<70001){

                    purchaseamount =Long.valueOf(700);

                }

                else if (price1>70000 && price1<75001){

                    purchaseamount =Long.valueOf(750);

                }

                else if (price1>75000 && price1<80001){

                    purchaseamount =Long.valueOf(800);

                }

                else if (price1>80000 && price1<85001){

                    purchaseamount =Long.valueOf(850);

                }

                else if (price1>85000 && price1<90001){

                    purchaseamount =Long.valueOf(900);

                }

                else if (price1>90000 && price1<95001){

                    purchaseamount =Long.valueOf(950);

                }

                else if (price1>95000 && price1<100001){

                    purchaseamount =Long.valueOf(1000);

                }

                else if (price1>100000){

                    purchaseamount =Long.valueOf(1100);

                }

                productname = brandtemp+" "+modeltemp.toString();

                Intent intent = new Intent(RegisterDevicePage4.this, RegisterDevicePage4.class);


           /* intent.putExtra("ProductName",productname);
            intent.putExtra("IMEI1",imei1temp);
            intent.putExtra("IMEI2",imei2temp);
            intent.putExtra("SerialNumber",serialnotemp);
            intent.putExtra("PurchaseDay",day1);
            intent.putExtra("PurchaseMonth",month1);
            intent.putExtra("PurchaseYear",year1);
            intent.putExtra("PurchasePrice",price1);
            intent.putExtra("SubscriptionAmount",purchaseamount);
            intent.putExtra("MobileNumber",mobilenotemp);
                intent.putExtra("PicBill", picbilltemp);
                intent.putExtra("PicProof", picproof.toString());
                 intent.putExtra("VerificationStatus", verificationstatus.toString())
                intent.putExtra("SaleStatus", salestatus.toString())
                intent.putExtra("StolenStatus", stolenstatus.toString())
                intent.putExtra("UserID", userid);
                intent.putExtra("ProductID", productid);
                intent.putExtra("AdminID", "");
                */
                mprogress.dismiss();
                startActivity(intent);

            }
            else {

                Toast.makeText(RegisterDevicePage4.this, "Image not selected", Toast.LENGTH_SHORT).show();

            }

        }
    }

}
