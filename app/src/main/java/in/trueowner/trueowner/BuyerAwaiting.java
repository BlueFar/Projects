package in.trueowner.trueowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class BuyerAwaiting extends AppCompatActivity {

    ImageView backbutton;
    Button contactbutton;
    String productid, sellerid, bidderid, mobno;;
    public static final int REQUEST_CALL = 9;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyer_awaiting);

        Intent intent = getIntent();
        productid = intent.getExtras().getString("ProductID");
        bidderid = intent.getExtras().getString("BidderID");
        sellerid = intent.getExtras().getString("SellerID");
        backbutton = (ImageView) findViewById(R.id.buyer_awaiting_back_button);
        contactbutton = (Button) findViewById(R.id.buyer_awaiting_contact_button);

        DocumentReference productRef =db.collection("Users").document(bidderid).collection("Cart").document(productid);
productRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
    @Override
    public void onSuccess(DocumentSnapshot documentSnapshot) {

       mobno = documentSnapshot.getString("SellerNumber");

    }
});
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BuyerAwaiting.this, CartPage.class);
                startActivity(intent);
                finish();

            }
        });

        contactbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makePhoneCall(mobno);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        DocumentReference productRef =db.collection("Users").document(bidderid).collection("Cart").document(productid);
        productRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(BuyerAwaiting.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (documentSnapshot.exists()) {

                    Boolean verificationstatustemp = documentSnapshot.getBoolean("VerificationStatus");
                    Boolean reviewstatustemp = documentSnapshot.getBoolean("ReviewStatus");
                    Boolean editofferstatustemp = documentSnapshot.getBoolean("EditOfferStatus");
                    Boolean finalacceptstatustemp = documentSnapshot.getBoolean("FinalAcceptStatus");
                    Boolean paydeliverystatustemp = documentSnapshot.getBoolean("PayDeliveryStatus");

                    if (verificationstatustemp.equals(true) && reviewstatustemp.equals(false) && editofferstatustemp.equals(false) && finalacceptstatustemp.equals(false) && paydeliverystatustemp.equals(false)){

                        Intent intent = new Intent(BuyerAwaiting.this,BuyerVerificationReviewPage.class);
                        intent.putExtra("SellerID",sellerid.toString());
                        intent.putExtra("ProductID",productid.toString());
                        intent.putExtra("BidderID",bidderid.toString());
                        startActivity(intent);
                        finish();

                    }

                    if (verificationstatustemp.equals(true) && reviewstatustemp.equals(true) && editofferstatustemp.equals(false) && finalacceptstatustemp.equals(true) && paydeliverystatustemp.equals(false)){

                        Intent intent = new Intent(BuyerAwaiting.this,BuyerFinalConformation.class);
                        intent.putExtra("SellerID",sellerid.toString());
                        intent.putExtra("ProductID",productid.toString());
                        intent.putExtra("BidderID",bidderid.toString());
                        startActivity(intent);
                        finish();

                    }

                    if (verificationstatustemp.equals(true) && reviewstatustemp.equals(false) && editofferstatustemp.equals(true) && finalacceptstatustemp.equals(false) && paydeliverystatustemp.equals(false)){

                        Intent intent = new Intent(BuyerAwaiting.this,BuyerFinalConformation.class);
                        intent.putExtra("SellerID",sellerid.toString());
                        intent.putExtra("ProductID",productid.toString());
                        intent.putExtra("BidderID",bidderid.toString());
                        startActivity(intent);
                        finish();

                    }

                    if (verificationstatustemp.equals(true) && reviewstatustemp.equals(false) && editofferstatustemp.equals(true) && finalacceptstatustemp.equals(true) && paydeliverystatustemp.equals(false)){

                        Intent intent = new Intent(BuyerAwaiting.this,BuyerFinalConformation.class);
                        intent.putExtra("SellerID",sellerid.toString());
                        intent.putExtra("ProductID",productid.toString());
                        intent.putExtra("BidderID",bidderid.toString());
                        startActivity(intent);
                        finish();

                    }

                    if (verificationstatustemp.equals(true) && reviewstatustemp.equals(false) && editofferstatustemp.equals(true) && finalacceptstatustemp.equals(true) && paydeliverystatustemp.equals(true)){

                        Intent intent = new Intent(BuyerAwaiting.this,BuyerFinalConformation.class);
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

    private void makePhoneCall(String number){

        if(number.trim().length() < 10 ){

            Toast.makeText(BuyerAwaiting.this, "Seller does not wish to share his number", Toast.LENGTH_SHORT).show();

        } else {

            if (ContextCompat.checkSelfPermission(BuyerAwaiting.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(BuyerAwaiting.this,
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

                makePhoneCall(mobno);

            } else {

                Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show();

            }

        }
    }
}
