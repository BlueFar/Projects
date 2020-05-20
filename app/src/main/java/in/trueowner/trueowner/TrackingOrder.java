package in.trueowner.trueowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TrackingOrder extends AppCompatActivity {

    ImageView backbutton;
    Button homebutton, detailsbutton, paymentbutton;
    ImageButton callbutton1, callbutton2;
    TextView text1, text2, text3, text4, text5, text6, text7, text8, text9;
    View view1, view2, view21, view3, view31, view32, view4, view41, view42, view5, view51, view52,view6, view61, view62, view7, view71, view72, view8, view81, view82, view9, view91, view92, line56, line67, line78, line89;
     String sellerid, bidderid, orderdate, orderstatus, productname, productid, bidderadminid, selleradminid, userid, bidderadminnumber, selleradminnumber;
     long offeredprice,deliveryfee, registerfee;
    public static final int REQUEST_CALL = 9;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracking_order);

        Intent intent = getIntent();
        userid = intent.getExtras().getString("UserID");
        sellerid = intent.getExtras().getString("SellerID");
        productid = intent.getExtras().getString("ProductID");
        bidderid = intent.getExtras().getString("BidderID");
        orderstatus = intent.getExtras().getString("OrderStatus");
        productname = intent.getExtras().getString("ProductName");
        bidderadminid = intent.getExtras().getString("BidderAdminID");
        selleradminid = intent.getExtras().getString("SellerAdminID");
        offeredprice = intent.getExtras().getLong("OfferedPrice");
        deliveryfee = intent.getExtras().getLong("DeliveryFee");
        registerfee = intent.getExtras().getLong("RegisterFee");
        backbutton = (ImageView) findViewById(R.id.track_order_back_button);
        homebutton = (Button) findViewById(R.id.track_order_home_button);
        detailsbutton = (Button) findViewById(R.id.track_order_details_button);
        paymentbutton = (Button) findViewById(R.id.track_order_payment_button);
        callbutton1 = (ImageButton) findViewById(R.id.track_order_call_button1);
        callbutton2 = (ImageButton) findViewById(R.id.track_order_call_button2);
        view1 = (View) findViewById(R.id.order_confirmed_view1);
        view2 = (View) findViewById(R.id.picked_seller_view2);
        view21 = (View) findViewById(R.id.picked_seller_view2_1);
        view3 = (View) findViewById(R.id.shipped_view3);
        view31 = (View) findViewById(R.id.shipped_view3_1);
        view32 = (View) findViewById(R.id.shipped_view3_2);
        view4 = (View) findViewById(R.id.picked_buyer_view4);
        view41 = (View) findViewById(R.id.picked_buyer_view4_1);
        view42 = (View) findViewById(R.id.picked_buyer_view4_2);
        view5 = (View) findViewById(R.id.order_complete_view5);
        view51 = (View) findViewById(R.id.order_complete_view5_1);
        view52 = (View) findViewById(R.id.order_complete_view5_2);
        view6 = (View) findViewById(R.id.returned_view6);
        view61 = (View) findViewById(R.id.returned_view6_1);
        view62 = (View) findViewById(R.id.returned_view6_2);
        view7 = (View) findViewById(R.id.shippedback_view7);
        view71 = (View) findViewById(R.id.shippedback_view7_1);
        view72 = (View) findViewById(R.id.shippedback_view7_2);
        view8 = (View) findViewById(R.id.receivedback_view8);
        view81 = (View) findViewById(R.id.receivedback_view8_1);
        view82 = (View) findViewById(R.id.receivedback_view8_2);
        view9 = (View) findViewById(R.id.picked_view9);
        view91 = (View) findViewById(R.id.picked_view9_1);
        view92 = (View) findViewById(R.id.picked_view9_2);
        line67 = (View) findViewById(R.id.line67);
        line78 = (View) findViewById(R.id.line78);
        line89 = (View) findViewById(R.id.line89);
        text5 = (TextView) findViewById(R.id.track_order_text5);
        text6 = (TextView) findViewById(R.id.track_order_text6);
        text7 = (TextView) findViewById(R.id.track_order_text7);
        text8 = (TextView) findViewById(R.id.track_order_text8);
        text9 = (TextView) findViewById(R.id.track_order_text9);


      //  Intent intent = getIntent();
       // String productid = intent.getExtras().getString("ProductID");
       // String productname = intent.getExtras().getString("ProductName");
       // String image1 = intent.getExtras().getString("Image1");
       // String orderid = intent.getExtras().getString("OrderID");
       // String orderdate = intent.getExtras().getString("OrderDate");
       // final String orderstatus = intent.getExtras().getString("OrderStatus");
       // long deliveryfee = intent.getExtras().getLong("DeliveryFee");
       // long price1 = intent.getExtras().getLong("Price");
       // Uri picbill = Uri.parse(intent.getExtras().getString("PicBill"));


        switch (orderstatus){ //orderstatus

            case "Confirmed" :
                break;

            case "Dropped" :
                view2.setVisibility(View.GONE);
                view21.setVisibility(View.VISIBLE);
                view3.setVisibility(View.GONE);
                view31.setVisibility(View.VISIBLE);
                text3.setTypeface(Typeface.DEFAULT_BOLD);
                break;

            case "Shipped" :
                view2.setVisibility(View.GONE);
                view21.setVisibility(View.VISIBLE);
                view3.setVisibility(View.GONE);
                view32.setVisibility(View.VISIBLE);
                view4.setVisibility(View.GONE);
                view41.setVisibility(View.VISIBLE);
                text4.setTypeface(Typeface.DEFAULT_BOLD);
                break;

            case "Received" :
                view2.setVisibility(View.GONE);
                view21.setVisibility(View.VISIBLE);
                view3.setVisibility(View.GONE);
                view32.setVisibility(View.VISIBLE);
                view4.setVisibility(View.GONE);
                view42.setVisibility(View.VISIBLE);
                view5.setVisibility(View.GONE);
                view51.setVisibility(View.VISIBLE);
                text5.setTypeface(Typeface.DEFAULT_BOLD);
                if (userid == bidderid){

                    callbutton1.setVisibility(View.VISIBLE);
                    homebutton.setVisibility(View.GONE);
                    paymentbutton.setVisibility(View.VISIBLE);

                }

                break;

            case "Completed" :
                view2.setVisibility(View.GONE);
                view21.setVisibility(View.VISIBLE);
                view3.setVisibility(View.GONE);
                view32.setVisibility(View.VISIBLE);
                view4.setVisibility(View.GONE);
                view42.setVisibility(View.VISIBLE);
                view5.setVisibility(View.GONE);
                view52.setVisibility(View.VISIBLE);
                homebutton.setVisibility(View.GONE);
                detailsbutton.setVisibility(View.VISIBLE);
                break;

            case "Returned" :
                view2.setVisibility(View.GONE);
                view21.setVisibility(View.VISIBLE);
                view3.setVisibility(View.GONE);
                view32.setVisibility(View.VISIBLE);
                view4.setVisibility(View.GONE);
                view42.setVisibility(View.VISIBLE);
                view5.setVisibility(View.GONE);
                view52.setVisibility(View.GONE);
                view62.setVisibility(View.VISIBLE);
                text5.setVisibility(View.GONE);
                text6.setVisibility(View.VISIBLE);
                if (userid == bidderid){

                    text6.setText("Returned");

                }
                else {

                    view71.setVisibility(View.VISIBLE);
                    line67.setVisibility(View.VISIBLE);
                    view8.setVisibility(View.VISIBLE);
                    line78.setVisibility(View.VISIBLE);
                    view9.setVisibility(View.VISIBLE);
                    line89.setVisibility(View.VISIBLE);
                    text7.setVisibility(View.VISIBLE);
                    text7.setTypeface(Typeface.DEFAULT_BOLD);
                    text8.setVisibility(View.VISIBLE);
                    text9.setVisibility(View.VISIBLE);

                }




                break;

            case "Shipped Back" :
                view2.setVisibility(View.GONE);
                view21.setVisibility(View.VISIBLE);
                view3.setVisibility(View.GONE);
                view32.setVisibility(View.VISIBLE);
                view4.setVisibility(View.GONE);
                view42.setVisibility(View.VISIBLE);
                view5.setVisibility(View.GONE);
                view52.setVisibility(View.GONE);
                view62.setVisibility(View.VISIBLE);
                text5.setVisibility(View.GONE);
                text6.setVisibility(View.VISIBLE);
                view71.setVisibility(View.GONE);
                view72.setVisibility(View.VISIBLE);
                line67.setVisibility(View.VISIBLE);
                view8.setVisibility(View.GONE);
                view81.setVisibility(View.VISIBLE);
                line78.setVisibility(View.VISIBLE);
                view9.setVisibility(View.VISIBLE);
                line89.setVisibility(View.VISIBLE);
                text7.setVisibility(View.VISIBLE);
                text8.setVisibility(View.VISIBLE);
                text8.setTypeface(Typeface.DEFAULT_BOLD);
                text9.setVisibility(View.VISIBLE);

                break;

            case "Received Back" :
                view2.setVisibility(View.GONE);
                view21.setVisibility(View.VISIBLE);
                view3.setVisibility(View.GONE);
                view32.setVisibility(View.VISIBLE);
                view4.setVisibility(View.GONE);
                view42.setVisibility(View.VISIBLE);
                view5.setVisibility(View.GONE);
                view52.setVisibility(View.GONE);
                view62.setVisibility(View.VISIBLE);
                text5.setVisibility(View.GONE);
                text6.setVisibility(View.VISIBLE);
                view71.setVisibility(View.GONE);
                view72.setVisibility(View.VISIBLE);
                line67.setVisibility(View.VISIBLE);
                view8.setVisibility(View.GONE);
                view81.setVisibility(View.GONE);
                view82.setVisibility(View.VISIBLE);
                line78.setVisibility(View.VISIBLE);
                view9.setVisibility(View.GONE);
                view91.setVisibility(View.VISIBLE);
                line89.setVisibility(View.VISIBLE);
                text7.setVisibility(View.VISIBLE);
                text8.setVisibility(View.VISIBLE);
                text9.setVisibility(View.VISIBLE);
                text9.setTypeface(Typeface.DEFAULT_BOLD);

                if (userid == sellerid){

                    callbutton2.setVisibility(View.VISIBLE);


                } else {



                }


                break;

            case "Picked" :
                view2.setVisibility(View.GONE);
                view21.setVisibility(View.VISIBLE);
                view3.setVisibility(View.GONE);
                view32.setVisibility(View.VISIBLE);
                view4.setVisibility(View.GONE);
                view42.setVisibility(View.VISIBLE);
                view5.setVisibility(View.GONE);
                view52.setVisibility(View.GONE);
                view62.setVisibility(View.VISIBLE);
                text5.setVisibility(View.GONE);
                text6.setVisibility(View.VISIBLE);
                view71.setVisibility(View.GONE);
                view72.setVisibility(View.VISIBLE);
                line67.setVisibility(View.VISIBLE);
                view8.setVisibility(View.GONE);
                view81.setVisibility(View.GONE);
                view82.setVisibility(View.VISIBLE);
                line78.setVisibility(View.VISIBLE);
                view9.setVisibility(View.GONE);
                view91.setVisibility(View.GONE);
                view92.setVisibility(View.VISIBLE);
                line89.setVisibility(View.VISIBLE);
                text7.setVisibility(View.VISIBLE);
                text8.setVisibility(View.VISIBLE);
                text9.setVisibility(View.VISIBLE);

                break;




        }

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(TrackingOrder.this, MyOrdersFragment.class);
              //  intent1.putExtra("Price",details.getPrice());
               // intent1.putExtra("DeliveryFee",details.getDeliveryFee());
               // intent1.putExtra("ProductID",details.getProductID());
               // intent1.putExtra("ProductName",details.getProductName());
               // intent1.putExtra("Image1",details.getImage1());
               // intent1.putExtra("OrderID",details.getOrderID());
                //intent1.putExtra("OrderDate",details.getOrderDate());
               // intent1.putExtra("OrderStatus",details.getOrderStatus());
                startActivity(intent1);
                finish();

            }
        });

        callbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DocumentReference dref = db.collection("Admins").document(bidderadminid).collection("Details")
                        .document("PersonalDetails");
                dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        bidderadminnumber = documentSnapshot.getString("MobileNumber");
                        makePhoneCall(bidderadminnumber);

                    }
                });

            }
        });

        callbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DocumentReference dref = db.collection("Admins").document(selleradminid).collection("Details")
                        .document("PersonalDetails");
                dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        selleradminnumber = documentSnapshot.getString("MobileNumber");
                        makePhoneCall(selleradminnumber);


                    }
                });

            }
        });

        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String statuscondition = "Complete";
                if (statuscondition.equals(statuscondition)){ // orderstatus

                    Intent intent = new Intent(TrackingOrder.this, MyOrdersBill.class);
                    startActivity(intent);

                }

                else{

                    Intent intent = new Intent(TrackingOrder.this, HomePage.class);
                    startActivity(intent);
                    finish();

                }

            }
        });

        detailsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(TrackingOrder.this, MyOrdersBill.class);
                //  intent1.putExtra("Price",details.getPrice());
                // intent1.putExtra("DeliveryFee",details.getDeliveryFee());
                 intent1.putExtra("ProductID",productid);
                // intent1.putExtra("ProductName",details.getProductName());
                // intent1.putExtra("Image1",details.getImage1());
                // intent1.putExtra("OrderID",details.getOrderID());
                //intent1.putExtra("OrderDate",details.getOrderDate());
                // intent1.putExtra("OrderStatus",details.getOrderStatus());
                startActivity(intent1);
                finish();

            }
        });

        paymentbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(TrackingOrder.this, BuyerFinalOverview.class);
                //  intent1.putExtra("Price",details.getPrice());
                // intent1.putExtra("DeliveryFee",details.getDeliveryFee());
                // intent1.putExtra("ProductID",details.getProductID());
                // intent1.putExtra("ProductName",details.getProductName());
                // intent1.putExtra("Image1",details.getImage1());
                // intent1.putExtra("OrderID",details.getOrderID());
                //intent1.putExtra("OrderDate",details.getOrderDate());
                // intent1.putExtra("OrderStatus",details.getOrderStatus());
                startActivity(intent1);
                finish();

            }
        });

    }

    private void makePhoneCall(String number){

        if(number.trim().length() < 10 ){



        } else {

            if (ContextCompat.checkSelfPermission(TrackingOrder.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(TrackingOrder.this,
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

                if (userid == bidderid){

                    makePhoneCall(bidderadminnumber);

                } else if (userid == sellerid) {

                    makePhoneCall(selleradminnumber);

                }


            } else {

                Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show();

            }

        }
    }

}
