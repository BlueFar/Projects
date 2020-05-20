package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BuyerVerificationReviewPage extends AppCompatActivity {

    ImageView backbutton;
    TextView display, touchscreen, camera, buttonsfingerprint, wifi, bluetooth, mic, speakers, headphonejack;
    Button acceptbutton, rejectbutton;
    String updatedoffer;
    String productid, sellerid, bidderid;
    Dialog addresspopup, updateofferpopup;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyer_verification_review_page);

        Intent intent = getIntent();
        productid = intent.getExtras().getString("ProductID");
        bidderid = intent.getExtras().getString("BidderID");
        sellerid = intent.getExtras().getString("SellerID");
        backbutton = (ImageView) findViewById(R.id.buyer_review_back_button);
        display = (TextView) findViewById(R.id.textbox_display);
        touchscreen = (TextView) findViewById(R.id.textbox_Touchscreen);
        camera = (TextView) findViewById(R.id.textbox_Camera);
        buttonsfingerprint = (TextView) findViewById(R.id.textbox_Buttons_Fingerprint);
        wifi = (TextView) findViewById(R.id.textbox_Wifi);
        bluetooth = (TextView) findViewById(R.id.textbox_Bluetooth);
        mic = (TextView) findViewById(R.id.textbox_Mic);
        speakers = (TextView) findViewById(R.id.textbox_Speakers);
        headphonejack = (TextView) findViewById(R.id.textbox_Headphone_Jack);
        acceptbutton = (Button) findViewById(R.id.buyer_review_accept_button);
        rejectbutton = (Button) findViewById(R.id.buyer_review_reject_button);
        addresspopup = new Dialog(this);
        updateofferpopup = new Dialog(this);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


        display.setText("");
        touchscreen.setText("");
        camera.setText("");
        buttonsfingerprint.setText("");
        wifi.setText("");
        bluetooth.setText("");
        mic.setText("");
        speakers.setText("");
        headphonejack.setText("");

        acceptbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference productRef =db.collection("Users").document(bidderid).collection("Cart").document(productid);
                productRef.update("ReviewStatus", true);
                productRef.update("FinalAcceptStatus", true);
                DocumentReference productRef1 =db.collection("Users").document(sellerid).collection("Sell").document(productid).collection("Offers").document(bidderid);
                productRef.update("ReviewStatus", true);
                productRef.update("FinalAcceptStatus", true);
                Intent intent = new Intent(BuyerVerificationReviewPage.this, BuyerFinalConformation.class);
                intent.putExtra("SellerID",sellerid.toString());
                intent.putExtra("ProductID",productid.toString());
                intent.putExtra("BidderID",bidderid.toString());
                startActivity(intent);
                finish();

            }
        });

        rejectbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowPopup2(v, productid, sellerid, bidderid) ;


            }
        });

    }


    public void ShowPopup2(View v, final String productid1, final String sellerid1, final String Bidderid1) {

        Button updatebutton, rejectbutton;
        final EditText updateofferbox;
        updateofferpopup.setContentView(R.layout.buyer_edit_offer_popup);

        updatebutton =(Button) addresspopup.findViewById(R.id.popup_update_button);
        rejectbutton =(Button) addresspopup.findViewById(R.id.popup_Reject_button);
        updateofferbox = (EditText) findViewById(R.id.popup_update_price_box);

        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updatedoffer = updateofferbox.getText().toString();
                DocumentReference productRef2 =db.collection("Users").document(Bidderid1).collection("Cart").document(productid1);
                productRef2.update("OfferedPrice", updatedoffer);
                productRef2.update("EditOfferStatus", true);
                DocumentReference productRef3 =db.collection("Users").document(sellerid1).collection("Sell").document(productid1).collection("Offers").document(Bidderid1);
                productRef3.update("OfferedPrice", updatedoffer);
                productRef3.update("EditOfferStatus", true);
                updateofferpopup.dismiss();
                Intent intent = new Intent(BuyerVerificationReviewPage.this, BuyerFinalConformation.class);
                intent.putExtra("SellerID",sellerid1.toString());
                intent.putExtra("ProductID",productid1.toString());
                intent.putExtra("BidderID",Bidderid1.toString());
                startActivity(intent);
                finish();

            }
        });

        rejectbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference productRef2 =db.collection("Users").document(Bidderid1).collection("Cart").document(productid1);
                productRef2.delete();
                DocumentReference productRef3 =db.collection("Users").document(sellerid1).collection("Sell").document(productid1).collection("Offers").document(Bidderid1);
                productRef3.delete();
                updateofferpopup.dismiss();
                Intent intent = new Intent(BuyerVerificationReviewPage.this, CartPage.class);
                startActivity(intent);
                finish();

            }
        });

        updateofferpopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        updateofferpopup.show();

    }

}
