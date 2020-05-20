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

public class BuyerFinalOverview extends AppCompatActivity {

    ImageView backbutton;
    Button paymentbutton;
    TextView productname, productprice, deliverydeduction, total, registrationfee;
    String userid, bidderid, productid, productname1, total1;
    long productprice1, deliverydeduction1, registrationfee1;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyer_final_overview);

        backbutton = (ImageView) findViewById(R.id.buyer_overview_back_button);
        paymentbutton = (Button) findViewById(R.id.buyer_overview_payment_button);
        productname = (TextView) findViewById(R.id.buyer_overview_product_name);
        productprice = (TextView) findViewById(R.id.buyer_overview_product_price);
        deliverydeduction = (TextView) findViewById(R.id.buyer_overview_delivery_deduction);
        total = (TextView) findViewById(R.id.buyer_overview_total);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user !=null) {

            userid = user.getUid();
            bidderid = userid;

        DocumentReference productRef =db.collection("Users").document(bidderid).collection("MyOrders").document(productid);
        productRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                productname1 = documentSnapshot.getString("ProductName");
                productprice1 = documentSnapshot.getLong("ProductPrice");
                deliverydeduction1 = documentSnapshot.getLong("DeliveryDeduction");
                registrationfee1 = documentSnapshot.getLong("RegistrationFee");


            }
        });

        productname.setText(productname1);
        productprice.setText(String.valueOf(productprice1));
        deliverydeduction.setText(String.valueOf(deliverydeduction1));
        registrationfee.setText(String.valueOf(registrationfee1));
        total1 = String.valueOf(productprice1+registrationfee1-deliverydeduction1);
        total.setText(String.valueOf(total1));


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BuyerFinalOverview.this, MyOrdersFragment.class);
                startActivity(intent);
                finish();

            }
        });

        paymentbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BuyerFinalOverview.this, BuyerFinalOverview.class);
                startActivity(intent);
                finish();

            }
        });

        } else {

            Intent intent1 = new Intent(BuyerFinalOverview.this, LoginPage.class);
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

            Intent intent1 = new Intent(BuyerFinalOverview.this, LoginPage.class);
            startActivity(intent1);
            finish();

        }

    }

}
