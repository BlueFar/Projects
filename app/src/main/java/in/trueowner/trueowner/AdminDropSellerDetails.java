package in.trueowner.trueowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminDropSellerDetails extends AppCompatActivity {

    ImageView backbutton;
    ImageButton callbutton;
    Button acceptbutton;
    TextView productname, sellername, imei;
    String adminid, sellerid, bidderid, sellername1, productname1, productid, imei1, bidderadminid, biddername, bidderstate, biddercity,
            biddernumber, sellerstate, sellernumber, sellercity;
    long offeredprice, registerfee, deliveryfee;
    public static final int REQUEST_CALL = 9;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_drop_seller_details);

        Intent intent = getIntent();
        adminid = intent.getExtras().getString("AdminID");
        productid = intent.getExtras().getString("ProductID");
        backbutton = (ImageView) findViewById(R.id.admin_drop_seller_details_back_button);
        callbutton = (ImageButton) findViewById(R.id.admin_drop_seller_details_call_button);
        acceptbutton = (Button) findViewById(R.id.admin_drop_seller_details_accept_button);
        productname = (TextView) findViewById(R.id.admin_drop_seller_details_product_name);
        sellername = (TextView) findViewById(R.id.admin_drop_seller_details_seller_name);
        imei = (TextView) findViewById(R.id.admin_drop_seller_details_imei1);

        DocumentReference productRef =db.collection("Admins").document(adminid).collection("Drop")
                .document("ToSeller").collection("Details").document(productid);
        productRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                productname1 = documentSnapshot.getString("ProductName");
                sellername1 = documentSnapshot.getString("SellerName");
                imei1 = documentSnapshot.getString("Imei1");
                sellerid = documentSnapshot.getString("SellerID");
                bidderid = documentSnapshot.getString("BidderID");
                bidderadminid = documentSnapshot.getString("BidderAdminID");
                biddername = documentSnapshot.getString("BidderName");
                bidderstate = documentSnapshot.getString("BidderState");
                biddercity = documentSnapshot.getString("BidderCity");
                sellerstate = documentSnapshot.getString("SellerState");
                sellercity = documentSnapshot.getString("SellerCity");
                biddernumber = documentSnapshot.getString("BidderNumber");
                sellernumber = documentSnapshot.getString("SellerNumber");
                offeredprice = documentSnapshot.getLong("OfferedPrice");
                deliveryfee = documentSnapshot.getLong("DeliveryFee");
                registerfee = documentSnapshot.getLong("RegisterFee");


            }
        });

        callbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makePhoneCall(sellernumber);

            }
        });

        productname.setText(productname1);
        sellername.setText(sellername1);
        imei.setText(imei1);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminDropSellerDetails.this, AdminDropSeller.class);
                startActivity(intent);
                finish();

            }
        });

        acceptbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DocumentReference dref2 = db.collection("Users").document(sellerid).collection("MyOrders").document(productid);
                dref2.update("OrderStatus", "Picked").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                                        DocumentReference dref4 =db.collection("Admins").document(adminid)
                                                .collection("Drop")
                                                .document("ToSeller").collection("Details").document(productid);
                                        dref4.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Toast.makeText(AdminDropSellerDetails.this, "Updated", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(AdminDropSellerDetails.this, AdminHomePage.class);
                                                startActivity(intent);
                                                finish();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                DocumentReference dref10 = db.collection("Users").document(sellerid).collection("MyOrders").document(productid);
                                                dref10.update("OrderStatus", "ReceivedBack");
                                                Toast.makeText(AdminDropSellerDetails.this, "Unable to Confirm, please try again later", Toast.LENGTH_SHORT).show();


                                            }
                                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(AdminDropSellerDetails.this, "Unable to Confirm, please try again later", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });

    }

    private void makePhoneCall(String number){

        if(number.trim().length() < 10 ){

            Toast.makeText(AdminDropSellerDetails.this, "Seller does not wish to share his number", Toast.LENGTH_SHORT).show();

        } else {

            if (ContextCompat.checkSelfPermission(AdminDropSellerDetails.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(AdminDropSellerDetails.this,
                        new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);

            } else {

                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

            }

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {

            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                makePhoneCall(sellernumber);

            } else {

                Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show();

            }

        }
    }


}
