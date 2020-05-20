package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyOrdersBill extends AppCompatActivity {

    ImageView backbutton, productimage;
    TextView  orderdate, orderstatus, productname, productprice, deliverydeduction, total, registrationfee, registrationtext;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userid,productid, orderstatus1, productname1, total1, orderdate2, bidderid, sellerid ;
    long offeredprice, registrationfee1, deliverydeduction1;
    FirebaseUser user;
    Date orderdate1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_orders_bill);

        Intent intent = getIntent();
        productid = intent.getExtras().getString("ProductID");
        backbutton = (ImageView) findViewById(R.id.bill_back_button);
        orderdate = (TextView) findViewById(R.id.bill_order_date);
        orderstatus = (TextView) findViewById(R.id.bill_order_status);
        productname = (TextView) findViewById(R.id.bill_product_name);
        productprice = (TextView) findViewById(R.id.bill_product_price);
        registrationfee = (TextView) findViewById(R.id.bill_registration_fee);
        registrationtext = (TextView) findViewById(R.id.registration_text);
        deliverydeduction = (TextView) findViewById(R.id.bill_delivery_deduction);
        total = (TextView) findViewById(R.id.bill_total);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user !=null) {

            userid = user.getUid();

            DocumentReference dr = db.collection("Users").document(userid).collection("MyOrders").document(productid);
            dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {


                    orderdate1 = documentSnapshot.getDate("OrderDate");
                    orderstatus1 = documentSnapshot.getString("OrderStatus");
                    bidderid = documentSnapshot.getString("BidderID");
                    sellerid = documentSnapshot.getString("SellerID");
                    productname1 = documentSnapshot.getString("ProductName");
                    orderstatus1 = documentSnapshot.getString("OrderStatus");
                    offeredprice = documentSnapshot.getLong("OfferedPrice");
                    deliverydeduction1 = documentSnapshot.getLong("DeliveryFee");
                    registrationfee1 = documentSnapshot.getLong("RegistrationFee");


                }
            });



            if (userid == bidderid){

                DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
                orderdate2 = dateFormat.format(orderdate1);
                orderdate.setText(orderdate2);
                orderstatus.setText(orderstatus1);
                productname.setText(productname1);
                productprice.setText(String.valueOf(offeredprice));
                deliverydeduction.setText(String.valueOf(deliverydeduction1));
                registrationfee.setText(String.valueOf(registrationfee1));
                total1 = String.valueOf(offeredprice+registrationfee1-deliverydeduction1);
                total.setText(total1);

            } else if (userid == sellerid) {

                registrationfee.setVisibility(View.GONE);
                registrationtext.setVisibility(View.GONE);
                DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
                orderdate2 = dateFormat.format(orderdate1);
                orderdate.setText(orderdate2);
                orderstatus.setText(orderstatus1);
                productname.setText(productname1);
                productprice.setText(String.valueOf(offeredprice));
                deliverydeduction.setText(String.valueOf(deliverydeduction1));
                total1 = String.valueOf(offeredprice-deliverydeduction1);
                total.setText(total1);


            }



            backbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MyOrdersBill.this, MyOrdersFragment.class);
                    startActivity(intent);
                    finish();

                }
            });

        } else {

            Intent intent1 = new Intent(MyOrdersBill.this, LoginPage.class);
            startActivity(intent1);
            finish();

        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user !=null){

            userid = user.getUid();

        } else {

            Intent intent1 = new Intent(MyOrdersBill.this, LoginPage.class);
            startActivity(intent1);
            finish();

        }

    }

}
