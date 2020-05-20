package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class AdminRegisterVerification extends AppCompatActivity {

    ImageView backbutton, picbill, picproof;
    Button acceptbutton, rejectbutton;
    TextView brandmodel, imei1, imei2, serialno, purhasedate, purchaseprice, mobileno, image1, image2;
    String picbillstring, picproofstring, productname, productid, fullname, imeifirst, imeisecond, serialno1, mobilenumber, purchaseday, purchasemonth, purchaseyear, purchaseprice1;
    Dialog picbillpopup, picproofpopup;
    String adminid, userid;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_register_verification);

        Intent intent = getIntent();
        adminid = intent.getExtras().getString("AdminID");
        userid = intent.getExtras().getString("UserID");
        backbutton = (ImageView) findViewById(R.id.buyer_review_back_button);
        brandmodel = (TextView) findViewById(R.id.textbox_bran);
        imei1 = (TextView) findViewById(R.id.textbox_imei_1);
        imei2 = (TextView) findViewById(R.id.textbox_imei_2);
        serialno = (TextView) findViewById(R.id.textbox_serial_no);
        purhasedate = (TextView) findViewById(R.id.textbox_purchase_date);
        purchaseprice = (TextView) findViewById(R.id.textbox_purchase_price);
        mobileno = (TextView) findViewById(R.id.textbox_mobile_no);
        image1 = (TextView) findViewById(R.id.admin_register_item_image1);
        image2 = (TextView) findViewById(R.id.admin_register_item_image2);
        picbill = (ImageView) findViewById(R.id.pic_of_bill);
        picproof = (ImageView) findViewById(R.id.pic_of_proof);
        acceptbutton = (Button) findViewById(R.id.admin_register_verification_accept_button);
        rejectbutton = (Button) findViewById(R.id.admin_register_verification_reject_button);
        picbillpopup = new Dialog(this);
        picproofpopup = new Dialog(this);

        DocumentReference productRef = db.collection("Admins").document(adminid).collection("RegisterVerification").document("Pending").collection("Users").document(userid);
        productRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                productname = documentSnapshot.getString("ProductName");
                productid = documentSnapshot.getString("ProductID");
                fullname = documentSnapshot.getString("FullName");
                imeifirst = documentSnapshot.getString("Imei1");
                imeisecond = documentSnapshot.getString("Imei2");
                serialno1 = documentSnapshot.getString("SerialNumber");
                mobilenumber = documentSnapshot.getString("MobileNumber");
                picbillstring = documentSnapshot.getString("PicBill");
                picproofstring = documentSnapshot.getString("PicProof");
                purchaseday = String.valueOf(documentSnapshot.getLong("PurchaseDay"));
                purchasemonth = String.valueOf(documentSnapshot.getLong("PurchaseMonth"));
                purchaseyear = String.valueOf(documentSnapshot.getLong("PurchaseYear"));
                purchaseprice1 = String.valueOf(documentSnapshot.getLong("PurchasePrice"));

            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        brandmodel.setText(productname);
        imei1.setText(imeifirst);
        imei2.setText(imeisecond);
        serialno.setText(serialno1);
        purhasedate.setText(purchaseday  + "/" + purchasemonth  + "/" + purchaseyear);
        purchaseprice.setText(purchaseprice1);
        mobileno.setText(mobilenumber);

        StringBuilder img1 = new StringBuilder("");
        img1.append(picbillstring);
        image1.setText(img1.toString());
        String image1url =image1.getText().toString();
        Picasso.get().load(image1url).into(picbill);

        StringBuilder img2 = new StringBuilder("");
        img2.append(picproofstring);
        image2.setText(img2.toString());
        String image2url =image2.getText().toString();
        Picasso.get().load(image2url).into(picproof);

        picbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowPopup(v);

            }
        });

        picproof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowPopup2(v);

            }
        });

        acceptbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Map<String, Object> registerverification = new HashMap<>();
                registerverification.put("ProductName", productname);
                registerverification.put("ProductID", productid);
                registerverification.put("UserID", userid);
                registerverification.put("FullName", fullname);
                registerverification.put("Imei1", imei1);
                registerverification.put("Imei2", imei2);
                registerverification.put("MobileNumber", mobileno);
                registerverification.put("SerialNumber", serialno);
                registerverification.put("PurchaseDay", purchaseday);
                registerverification.put("PurchaseMonth", purchasemonth);
                registerverification.put("PurchaseYear", purchaseyear);
                registerverification.put("PurchasePrice", purchaseprice);
                registerverification.put("PicBill", picbill);
                registerverification.put("PicProof", picproof);
                registerverification.put("AdminID", adminid);

                DocumentReference dref2 = db.collection("Admins").document(adminid).collection("RegisterVerification").document("Done").collection("Users").document(userid);
                dref2.set(registerverification).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        DocumentReference dref3 = db.collection("Users").document(userid).collection("RegisterProducts").document(productid);
                        dref3.update("VerificationStatus", true);

                        DocumentReference dref4 = db.collection("Admins").document(adminid).collection("RegisterVerification").document("Pending").collection("Users").document(userid);
                        dref4.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(AdminRegisterVerification.this, "Verification Accepted",Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(AdminRegisterVerification.this, AdminHomePage.class);
                                startActivity(intent1);
                                finish();

                            }
                        });

                    }
                });

            }
        });

        rejectbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference dref6 = db.collection("Users").document(userid).collection("RegisterProducts").document(productid);
                dref6.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        DocumentReference dref5 = db.collection("Admins").document(adminid).collection("RegisterVerification").document("Pending").collection("Users").document(userid);
                        dref5.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(AdminRegisterVerification.this, "Verification Rejected",Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(AdminRegisterVerification.this, AdminHomePage.class);
                                startActivity(intent1);
                                finish();

                            }
                        });

                    }
                });



            }
        });

    }

    public void ShowPopup(View v) {

        ImageView largepicbill, exitbutton;
        TextView image3;

        picbillpopup.setContentView(R.layout.admin_register_verification_popup);


        exitbutton = (ImageView) findViewById(R.id.admin_register_popup_exit);
        largepicbill = (ImageView) findViewById(R.id.admin_register_popup_image);
        image3 = (TextView) findViewById(R.id.admin_register_popup_item_image1);

        StringBuilder img3 = new StringBuilder("");
        img3.append(picbillstring);
        image3.setText(img3.toString());
        String image3url =image3.getText().toString();
        Picasso.get().load(image3url).into(largepicbill);


        exitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                picbillpopup.dismiss();

            }
        });

        picbillpopup.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
        picbillpopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        picbillpopup.show();

    }

    public void ShowPopup2(View v) {

        ImageView largepicproof, exitbutton;
        TextView image4;

        picproofpopup.setContentView(R.layout.admin_register_verification_popup);


        exitbutton = (ImageView) findViewById(R.id.admin_register_popup_exit);
        largepicproof = (ImageView) findViewById(R.id.admin_register_popup_image);
        image4 = (TextView) findViewById(R.id.admin_register_popup_item_image1);

        StringBuilder img4 = new StringBuilder("");
        img4.append(picproofstring);
        image4.setText(img4.toString());
        String image4url =image4.getText().toString();
        Picasso.get().load(image4url).into(largepicproof);


        exitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                picproofpopup.dismiss();

            }
        });

        picproofpopup.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
        picproofpopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        picproofpopup.show();

    }

}
