package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminPickupPage extends AppCompatActivity {

    Button  pickupseller, pickupcourier, pickupreturned;
    String adminid, userid;
    ImageView backbutton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_pickup_page);

        backbutton = (ImageView) findViewById(R.id.pickup_page_back_button);
        pickupseller = (Button) findViewById(R.id.pickup_seller_button);
        pickupcourier = (Button) findViewById(R.id.pickup_courier_button);
        pickupreturned = (Button) findViewById(R.id.pickup_returned_button);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            userid = user.getUid();

        DocumentReference da = db.collection("AdminsRef").document(userid);
        da.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                adminid = documentSnapshot.getString("AdminID");

            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(AdminPickupPage.this, AdminHomePage.class);
                startActivity(intent1);
                finish();

            }
        });

        pickupseller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(AdminPickupPage.this, AdminPickupSeller.class);
                startActivity(intent1);

            }
        });

        pickupcourier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(AdminPickupPage.this, AdminPickupCourier.class);
                startActivity(intent1);

            }
        });

        pickupreturned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(AdminPickupPage.this, AdminPickupReturn.class);
                startActivity(intent1);

            }
        });

    } else {

        Intent intent1 = new Intent(AdminPickupPage.this, LoginPage.class);
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

            Intent intent1 = new Intent(AdminPickupPage.this, LoginPage.class);
            startActivity(intent1);
            finish();

        }

    }

}
