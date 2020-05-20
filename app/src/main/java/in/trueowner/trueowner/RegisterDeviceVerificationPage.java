package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterDeviceVerificationPage extends AppCompatActivity {

    ImageView backbuton;
    EditText textbox;
    FloatingActionButton submitbutton;
    public String  productid, userid, imei1, adminid, mobileno,serialno,productname,picbill,picproof,fullname,imei2;
    long purchaseday, purchasemonth, purchaseyear, purchaseprice;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_device_verification_page);

        Intent intent = getIntent();
        productid = intent.getExtras().getString("ProductID");
        userid = intent.getExtras().getString("UserID");
        backbuton = (ImageView) findViewById(R.id.seller_verification_back_button1);
        textbox = (EditText) findViewById(R.id.seller_verification_textbox);
        submitbutton = (FloatingActionButton) findViewById(R.id.seller_verification_submit_button);

        DocumentReference dref = db.collection("Users").document(userid).collection("RegisterProducts").document(productid);
        dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                imei1 = documentSnapshot.getString("Imei1");
                imei2 = documentSnapshot.getString("Imei2");
                productname = documentSnapshot.getString("ProductName");
                mobileno = documentSnapshot.getString("MobileNumber");
                serialno = documentSnapshot.getString("SerialNumber");
                purchaseday = documentSnapshot.getLong("PurchaseDay");
                purchaseyear = documentSnapshot.getLong("PurchaseYear");
                purchaseprice = documentSnapshot.getLong("PurchasePrice");
                purchasemonth = documentSnapshot.getLong("PurchaseMonth");
                picbill = documentSnapshot.getString("PicBill");
                picproof = documentSnapshot.getString("PicProof");

            }
        });

        DocumentReference dref1 = db.collection("Users").document(userid).collection("Details").document("PersonalDetails");
        dref1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                fullname = documentSnapshot.getString("FullName");

            }
        });

        backbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterDeviceVerificationPage.this, RegisterDeviceSuccessPage.class);
                startActivity(intent);

            }
        });


        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adminid = textbox.getText().toString();

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

                DocumentReference dref2 = db.collection("Admins").document(adminid).collection("RegisterVerification").document("Pending").collection("Users").document(userid);
                dref2.set(registerverification).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        DocumentReference dref3 = db.collection("Users").document(userid).collection("RegisterProducts").document(productid);
                        dref3.update("AdminID", adminid);
                        Toast.makeText(RegisterDeviceVerificationPage.this, "Verification request sent",Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(RegisterDeviceVerificationPage.this, RegisterDeviceSuccessPage.class);
                        startActivity(intent1);
                        finish();

                    }
                });






            }
        });



    }
}
