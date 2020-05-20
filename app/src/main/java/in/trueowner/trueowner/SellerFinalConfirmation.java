package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SellerFinalConfirmation extends AppCompatActivity {

    ImageView backbutton;
    TextView finalofferbox;
    Button acceptbutton, rejectbutton;
    String sellerid, productid, bidderid, offeredprice;
    int flag=0, flag1=0;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_final_confirmation);

        Intent intent = getIntent();
        productid = intent.getExtras().getString("ProductID");
        bidderid = intent.getExtras().getString("BidderID");
        sellerid = intent.getExtras().getString("SellerID");
        backbutton = (ImageView) findViewById(R.id.seller_final_back_button);
        finalofferbox = (TextView) findViewById(R.id.textbox_final_offer);
        acceptbutton = (Button) findViewById(R.id.seller_final_accept_button);
        rejectbutton = (Button) findViewById(R.id.seller_final_reject_button);

        DocumentReference dbref = db.collection("Users").document(sellerid).collection("Sell").document(productid).collection("Offers").document(bidderid);
        dbref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                offeredprice = documentSnapshot.getString("OfferedPrice");

            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SellerFinalConfirmation.this, MyProductsPage.class);
                startActivity(intent);
                finish();

            }
        });

        finalofferbox.setText(offeredprice);

        acceptbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flag==0) {
                    DocumentReference dbref1 = db.collection("Users").document(sellerid).collection("Sell").document(productid).collection("Offers").document(bidderid);
                    dbref1.update("FinalAcceptStatus", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            DocumentReference dbref2 = db.collection("Users").document(bidderid).collection("Cart").document(productid);
                            dbref2.update("FinalAcceptStatus", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    flag = 2;

                                }
                            });


                        }
                    });

                }

                if (flag ==2) {
                    Intent intent = new Intent(SellerFinalConfirmation.this, SellerPaymentAwaiting.class);
                    intent.putExtra("SellerID",sellerid.toString());
                    intent.putExtra("ProductID",productid.toString());
                    intent.putExtra("BidderID",bidderid.toString());
                    startActivity(intent);
                    finish();

                }

            }
        });

        rejectbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    DocumentReference productRef2 = db.collection("Users").document(sellerid).collection("Sell").document(productid).collection("Offers").document(bidderid);
                    productRef2.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            DocumentReference productRef3 = db.collection("Users").document(bidderid).collection("Cart").document(productid);
                            productRef3.update("OfferStatus", "Rejected").addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    flag1 =2;

                                }
                            });

                        }
                    });

                if (flag1==2){

                    Intent intent = new Intent(SellerFinalConfirmation.this, MyProductsPage.class);
                    startActivity(intent);
                    finish();

                }

            }
        });

    }
}
