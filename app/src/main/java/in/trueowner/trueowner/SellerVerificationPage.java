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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class SellerVerificationPage extends AppCompatActivity {

    ImageView backbuton;
    EditText textbox;
    FloatingActionButton submitbutton;
    public String sellerid, productid, bidderid, imei1, adminid, sellername;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_verification_page);

        backbuton = (ImageView) findViewById(R.id.seller_verification_back_button1);
        textbox = (EditText) findViewById(R.id.seller_verification_textbox);
        submitbutton = (FloatingActionButton) findViewById(R.id.seller_verification_submit_button);

        Intent intent = getIntent();
        productid = intent.getExtras().getString("ProductID");
        bidderid = intent.getExtras().getString("BidderID");
        sellerid = intent.getExtras().getString("SellerID");

        DocumentReference dref = db.collection("Users").document(sellerid).collection("RegisterProducts").document(productid);
        dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                imei1 = documentSnapshot.getString("Imei1");

            }
        });

        DocumentReference dref3 = db.collection("Users").document(sellerid).collection("Details").document("PersonalDetails");
        dref3.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                sellername = documentSnapshot.getString("Name");

            }
        });

        backbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SellerVerificationPage.this, MyProductsPage.class);
                startActivity(intent);
                finish();

            }
        });


        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Map<String, Object> sellverification = new HashMap<>();
                sellverification.put("BidderID", bidderid);
                sellverification.put("ProductID", productid);
                sellverification.put("SellerID", sellerid);
                sellverification.put("SellerName", sellername);
                sellverification.put("Imei1", imei1);
                sellverification.put("Display", "");
                sellverification.put("Touchscreen", "");
                sellverification.put("ButtonAndFingerprint", "");
                sellverification.put("Wifi", "");
                sellverification.put("Bluetooth", "");
                sellverification.put("Camera", "");
                sellverification.put("Mic", "");
                sellverification.put("Speaker", "");
                sellverification.put("HeadphoneJack", "");
                sellverification.put("AdminID", adminid);

                adminid = textbox.getText().toString();
                DocumentReference dref = db.collection("Admins").document(adminid).collection("SellVerification").document("Pending").collection("Users").document(sellerid);
                dref.set(sellverification).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(SellerVerificationPage.this, "Verification request sent",Toast.LENGTH_SHORT).show();

                    }
                });

                DocumentReference dref1 = db.collection("Users").document(sellerid).collection("Sell").document(productid).collection("Offers").document(bidderid);
                dref1.update("AdminID", adminid);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        DocumentReference productRef5 =db.collection("Users").document(sellerid).collection("Sell").document(productid).collection("Offers").document(bidderid);
        productRef5.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {

                    return;
                }

                if (documentSnapshot.exists()) {

                    Boolean verificationstatustemp = documentSnapshot.getBoolean("VerificationStatus");

                    if(verificationstatustemp){

                        Intent intent = new Intent(SellerVerificationPage.this,SellerAwaiting.class);
                        intent.putExtra("SellerID",sellerid.toString());
                        intent.putExtra("ProductID",productid.toString());
                        intent.putExtra("BidderID",bidderid.toString());
                        startActivity(intent);
                        finish();

                    }

                }
            }
        });

    }
}
