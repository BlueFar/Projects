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

public class AdminHomePage extends AppCompatActivity {

    Button registerbutton, sellerbutton, pickupbutton, dropbutton;
    String adminid, userid;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home_page);

        registerbutton = (Button) findViewById(R.id.registration_verification_button);
        sellerbutton = (Button) findViewById(R.id.seller_verification_button);
        dropbutton = (Button) findViewById(R.id.products_drop_button);
        pickupbutton = (Button) findViewById(R.id.products_pick_button);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user !=null) {

            userid = user.getUid();

            DocumentReference da = db.collection("AdminsRef").document(userid);
            da.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    adminid = documentSnapshot.getString("AdminID");

                }
            });

            registerbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent1 = new Intent(AdminHomePage.this, AdminRegisterList.class);
                    startActivity(intent1);

                }
            });

            sellerbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent1 = new Intent(AdminHomePage.this, AdminSellerList.class);
                    startActivity(intent1);

                }
            });

            dropbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent1 = new Intent(AdminHomePage.this, AdminDropPage.class);
                    startActivity(intent1);

                }
            });

            pickupbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent1 = new Intent(AdminHomePage.this, AdminPickupPage.class);
                    startActivity(intent1);

                }
            });

        } else {

            Intent intent1 = new Intent(AdminHomePage.this, LoginPage.class);
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

           Intent intent1 = new Intent(AdminHomePage.this, LoginPage.class);
            startActivity(intent1);
            finish();

        }

    }
}
