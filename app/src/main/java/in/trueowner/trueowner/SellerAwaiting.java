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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class SellerAwaiting extends AppCompatActivity {

    ImageView backbutton;
    Button contactbutton;
    public static final int REQUEST_CALL = 9;
    String sellerid, productid, bidderid, biddernumber;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_awaiting);

        Intent intent = getIntent();
        productid = intent.getExtras().getString("ProductID");
        bidderid = intent.getExtras().getString("BidderID");
        sellerid = intent.getExtras().getString("SellerID");
        backbutton = (ImageView) findViewById(R.id.seller_awaiting_back_button);
        contactbutton = (Button) findViewById(R.id.seller_awaiting_contact_button);

        DocumentReference dbref = db.collection("Users").document(sellerid).collection("Sell").document(productid).collection("Offers").document(bidderid);
        dbref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                biddernumber = documentSnapshot.getString("BidderNumber");

            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SellerAwaiting.this, MyProductsPage.class);
                startActivity(intent);
                finish();

            }
        });

        contactbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makePhoneCall(biddernumber);

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

                    Boolean editofferstatustemp = documentSnapshot.getBoolean("EditOfferStatus");
                    Boolean reviewstatustemp = documentSnapshot.getBoolean("ReviewStatus");
                    Boolean paydeliverystatustemp = documentSnapshot.getBoolean("PayDeliveryStatus");

                    if(reviewstatustemp){

                        if (paydeliverystatustemp){

                            Intent intent = new Intent(SellerAwaiting.this,SellerAwaiting.class);
                            intent.putExtra("SellerID",sellerid.toString());
                            intent.putExtra("ProductID",productid.toString());
                            intent.putExtra("BidderID",bidderid.toString());
                            startActivity(intent);
                            finish();

                        }

                       else if (editofferstatustemp){

                            Intent intent = new Intent(SellerAwaiting.this,SellerFinalConfirmation.class);
                            intent.putExtra("SellerID",sellerid.toString());
                            intent.putExtra("ProductID",productid.toString());
                            intent.putExtra("BidderID",bidderid.toString());
                            startActivity(intent);
                            finish();

                        }

                    }

                }
            }
        });

    }

    public void makePhoneCall(String number){

        if(number.trim().length() < 10 ){

            Toast.makeText(SellerAwaiting.this, "Seller does not wish to share his number", Toast.LENGTH_SHORT).show();

        } else {

            if (ContextCompat.checkSelfPermission(SellerAwaiting.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(SellerAwaiting.this,
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

                makePhoneCall(biddernumber);

            } else {

                Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show();

            }

        }
    }

}
