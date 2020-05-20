package in.trueowner.trueowner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class SellDeviceRegister6 extends AppCompatActivity {

    ImageView backbutton, imageupload, browse;
    Uri picbill;
    File file;
    private static final int Camera_Request_code = 1, Read_Request_code = 3;
    private int CAMERA_PERMISSION_CODE = 2, READ_PERMISSION_CODE = 4, WRITE_PERMISSION_CODE = 6;
    Boolean camera = false, write = false, read = false;
    String frontpictemp,backpictemp,leftpictemp,rightpictemp,bottompictemp,productid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_device_register6);

        backbutton = (ImageView) findViewById(R.id.sell_register_back_button6);
        imageupload = (ImageView) findViewById(R.id.sell_register_image_upload6);
        browse = (ImageView) findViewById(R.id.sell_register_browse6);

        Intent intent = getIntent();
        frontpictemp = intent.getExtras().getString("FrontPic");
        backpictemp = intent.getExtras().getString("BackPic");
        backpictemp = intent.getExtras().getString("LeftPic");
        rightpictemp = intent.getExtras().getString("RightPic");
        bottompictemp = intent.getExtras().getString("BottomPic");
        productid = intent.getExtras().getString("ProductID");

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

                    if (ContextCompat.checkSelfPermission(SellDeviceRegister6.this,
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(SellDeviceRegister6.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File photoFile = null;
                        photoFile = createPhotoFile();


                        if (photoFile != null) {



                            String pathToFile = photoFile.getAbsolutePath();
                            picbill = FileProvider.getUriForFile(SellDeviceRegister6.this, "in.trueowner.fileprovider", photoFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, picbill);
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

                if (Build.VERSION.SDK_INT >= 23) {

                    if (ContextCompat.checkSelfPermission(SellDeviceRegister6.this,
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
                    ActivityCompat.requestPermissions(SellDeviceRegister6.this,
                            new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
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
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
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
                    ActivityCompat.requestPermissions(SellDeviceRegister6.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION_CODE);
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
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION_CODE);
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
                    ActivityCompat.requestPermissions(SellDeviceRegister6.this,
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
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                camera = true;
                finaloutcome();

            } else {


            }
        }

        if (requestCode == READ_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                write = true;
                finaloutcome();

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
                picbill = FileProvider.getUriForFile(SellDeviceRegister6.this, "in.trueowner.fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, picbill);
                startActivityForResult(intent, Camera_Request_code);

            }

        } else {


        }

    }

    private void finaloutcome1() {

        if (read.equals(true)) {

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, Read_Request_code);




        }

        else {

            Toast.makeText(SellDeviceRegister6.this, "Dont have the permission", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Camera_Request_code && resultCode == RESULT_OK) {


            if (picbill != null) {

                Toast.makeText(SellDeviceRegister6.this, "Image selected", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SellDeviceRegister6.this, SellDeviceRegister7.class);
                intent.putExtra("FrontPic", frontpictemp);
                intent.putExtra("BackPic", backpictemp);
                intent.putExtra("LeftPic", leftpictemp);
                intent.putExtra("RightPic", leftpictemp);
                intent.putExtra("BottomPic", leftpictemp);
                intent.putExtra("TopPic", picbill.toString());
                intent.putExtra("ProductID", productid);
                startActivity(intent);

            }  else {

                Toast.makeText(SellDeviceRegister6.this, "Image not selected", Toast.LENGTH_SHORT).show();

            }


        }

        else if (requestCode == Read_Request_code && resultCode == RESULT_OK) {

            picbill = data.getData();

            if (picbill != null) {

                Toast.makeText(SellDeviceRegister6.this, "Image selected", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SellDeviceRegister6.this, SellDeviceRegister7.class);
                intent.putExtra("FrontPic", frontpictemp);
                intent.putExtra("BackPic", backpictemp);
                intent.putExtra("LeftPic", leftpictemp);
                intent.putExtra("RightPic", leftpictemp);
                intent.putExtra("BottomPic", leftpictemp);
                intent.putExtra("TopPic", picbill.toString());
                intent.putExtra("ProductID", productid);
                startActivity(intent);

            } else {

                Toast.makeText(SellDeviceRegister6.this, "Image not selected", Toast.LENGTH_SHORT).show();

            }
        }

    }
}
