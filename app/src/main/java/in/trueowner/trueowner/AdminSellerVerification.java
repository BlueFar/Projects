package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminSellerVerification extends AppCompatActivity {

    Spinner displayspinner, touchscreenspinner, cameraspinner, buttonfingerprintspinner, wifispinner, bluetoothspinner, micspinner, speakersspinner, headphonejackspinner;
    String displaytemp, touchscreentemp, cameratemp, buttonfingerprinttemp, wifitemp, bluetoothtemp,  mictemp, speakerstemp, headphonejacktemp;
    ImageView backbutton;
    Button submitbutton, rejectbutton;
    String adminid, sellerid, bidderid, sellername, productid, imei1;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_seller_verification);

        Intent intent = getIntent();
        adminid = intent.getExtras().getString("AdminID");
        sellerid = intent.getExtras().getString("UserID");
        backbutton = (ImageView) findViewById(R.id.admin_seller_verification_back_button);
        displayspinner = (Spinner) findViewById(R.id.spinner_display);
        touchscreenspinner = (Spinner) findViewById(R.id.spinner_Touchscreen);
        cameraspinner = (Spinner) findViewById(R.id.spinner_Camera);
        buttonfingerprintspinner = (Spinner) findViewById(R.id.spinner_Buttons_Fingerprint);
        wifispinner = (Spinner) findViewById(R.id.spinner_Wifi);
        bluetoothspinner = (Spinner) findViewById(R.id.spinner_Bluetooth);
        micspinner = (Spinner) findViewById(R.id.spinner_Mic);
        speakersspinner = (Spinner) findViewById(R.id.spinner_Speakers);
        headphonejackspinner = (Spinner) findViewById(R.id.spinner_Headphone_Jack);
        submitbutton = (Button) findViewById(R.id.admin_seller_verification_submit_button);
        rejectbutton = (Button) findViewById(R.id.admin_seller_verification_reject_button);

        DocumentReference productRef = db.collection("Admins").document(adminid).collection("SellerVerification").document("Pending").collection("Users").document(sellerid);
        productRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                bidderid = documentSnapshot.getString("BidderID");
                productid = documentSnapshot.getString("ProductID");
                sellername = documentSnapshot.getString("SellerName");
                imei1 = documentSnapshot.getString("Imei1");


            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        ArrayAdapter<CharSequence> adapterdisplay = ArrayAdapter.createFromResource(this, R.array.adminsellerreview, android.R.layout.simple_spinner_item);
        adapterdisplay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        displayspinner.setAdapter(adapterdisplay);

        ArrayAdapter<CharSequence> adaptertouchscreen = ArrayAdapter.createFromResource(this, R.array.adminsellerreview, android.R.layout.simple_spinner_item);
        adaptertouchscreen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        touchscreenspinner.setAdapter(adaptertouchscreen);

        ArrayAdapter<CharSequence> adaptercamera = ArrayAdapter.createFromResource(this, R.array.adminsellerreview, android.R.layout.simple_spinner_item);
        adaptercamera.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cameraspinner.setAdapter(adaptercamera);

        ArrayAdapter<CharSequence> adapterbuttonfingerprint = ArrayAdapter.createFromResource(this, R.array.adminsellerreview, android.R.layout.simple_spinner_item);
        adapterbuttonfingerprint.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buttonfingerprintspinner.setAdapter(adapterbuttonfingerprint);

        ArrayAdapter<CharSequence> adapterwifi = ArrayAdapter.createFromResource(this, R.array.adminsellerreview, android.R.layout.simple_spinner_item);
        adapterwifi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wifispinner.setAdapter(adapterwifi);

        ArrayAdapter<CharSequence> adapterbluetooth = ArrayAdapter.createFromResource(this, R.array.adminsellerreview, android.R.layout.simple_spinner_item);
        adapterbluetooth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bluetoothspinner.setAdapter(adapterbluetooth);

        ArrayAdapter<CharSequence> adaptermic = ArrayAdapter.createFromResource(this, R.array.adminsellerreview, android.R.layout.simple_spinner_item);
        adaptermic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        micspinner.setAdapter(adaptermic);

        ArrayAdapter<CharSequence> adapterspeakers = ArrayAdapter.createFromResource(this, R.array.adminsellerreview, android.R.layout.simple_spinner_item);
        adapterspeakers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        speakersspinner.setAdapter(adapterspeakers);

        ArrayAdapter<CharSequence> adapterheadphonejack = ArrayAdapter.createFromResource(this, R.array.adminsellerreview, android.R.layout.simple_spinner_item);
        adapterheadphonejack.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        headphonejackspinner.setAdapter(adapterheadphonejack);

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displaytemp = displayspinner.getSelectedItem().toString().trim();
                touchscreentemp = touchscreenspinner.getSelectedItem().toString().trim();
                cameratemp = cameraspinner.getSelectedItem().toString().trim();
                buttonfingerprinttemp = buttonfingerprintspinner.getSelectedItem().toString().trim();
                wifitemp = wifispinner.getSelectedItem().toString().trim();
                bluetoothtemp = bluetoothspinner.getSelectedItem().toString().trim();
                mictemp = micspinner.getSelectedItem().toString().trim();
                speakerstemp = speakersspinner.getSelectedItem().toString().trim();
                headphonejacktemp = headphonejackspinner.getSelectedItem().toString().trim();

                final Map<String, Object> sellverification = new HashMap<>();
                sellverification.put("BidderID", bidderid);
                sellverification.put("ProductID", productid);
                sellverification.put("SellerID", sellerid);
                sellverification.put("SellerName", sellername);
                sellverification.put("Imei1", imei1);
                sellverification.put("Display", displaytemp);
                sellverification.put("Touchscreen", touchscreentemp);
                sellverification.put("ButtonAndFingerprint", buttonfingerprinttemp);
                sellverification.put("Wifi", wifitemp);
                sellverification.put("Bluetooth", bluetoothtemp);
                sellverification.put("Camera", cameratemp);
                sellverification.put("Mic", mictemp);
                sellverification.put("Speaker", speakerstemp);
                sellverification.put("HeadphoneJack", headphonejacktemp);
                sellverification.put("AdminID", adminid);

                final Map<String, Object> sellverification2 = new HashMap<>();
                sellverification2.put("Display", displaytemp);
                sellverification2.put("Touchscreen", touchscreentemp);
                sellverification2.put("ButtonAndFingerprint", buttonfingerprinttemp);
                sellverification2.put("Wifi", wifitemp);
                sellverification2.put("Bluetooth", bluetoothtemp);
                sellverification2.put("Camera", cameratemp);
                sellverification2.put("Mic", mictemp);
                sellverification2.put("Speaker", speakerstemp);
                sellverification2.put("HeadphoneJack", headphonejacktemp);
                sellverification2.put("VerificationStatus", true);

                DocumentReference dref1 = db.collection("Users").document(bidderid).collection("Cart").document(productid);
                dref1.update(sellverification2).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        DocumentReference dref = db.collection("Admins").document(adminid).collection("SellVerification").document("Done").collection("Users").document(sellerid);
                        dref.set(sellverification).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                DocumentReference dref2 = db.collection("Users").document(sellerid).collection("Sell").document(productid).collection("Offers").document(bidderid);
                                dref2.update("VerificationStatus", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        DocumentReference dref3 = db.collection("Admins").document(adminid).collection("SellVerification").document("Pending").collection("Users").document(sellerid);
                                        dref3.delete();
                                        Toast.makeText(AdminSellerVerification.this, "Verification Accepted",Toast.LENGTH_SHORT).show();
                                        Intent intent1 = new Intent(AdminSellerVerification.this, AdminSellerVerificationSuccessPage.class);
                                        startActivity(intent1);
                                    }
                                });

                            }
                        });

                    }
                });

            }
        });

        rejectbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference dref3 = db.collection("Admins").document(adminid).collection("SellVerification").document("Pending").collection("Users").document(sellerid);
                dref3.delete();
                Toast.makeText(AdminSellerVerification.this, "Verification Rejected",Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(AdminSellerVerification.this, AdminHomePage.class);
                startActivity(intent1);

            }
        });

    }
}
