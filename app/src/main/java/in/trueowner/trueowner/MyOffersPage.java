package in.trueowner.trueowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MyOffersPage extends AppCompatActivity {

    ImageView backbutton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productRef = db.collection("Users");
    CollectionReference pd;
    public static final int REQUEST_CALL = 9;
    private My_offers_Adapter adapter;
    String sellerid, productid, biddernumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_offers_page);

        Intent intent = getIntent();
        productid = intent.getExtras().getString("ProductID");
        sellerid = intent.getExtras().getString("UserID");
        biddernumber = adapter.biddernumber.toString();
        backbutton = (ImageView) findViewById(R.id.offers_back_button);

        CollectionReference pd = productRef.document(sellerid).collection("Sell").document(productid).collection("Offers");

        setUpRecyclerView();

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setUpRecyclerView() {

        Query query = pd
                .orderBy("OrderStatus");
        FirestoreRecyclerOptions<My_Offers_Get_Set> options = new FirestoreRecyclerOptions.Builder<My_Offers_Get_Set>()
                .setQuery(query, My_Offers_Get_Set.class)
                .build();

        adapter = new My_offers_Adapter(options);

        RecyclerView recyclerView = findViewById(R.id.my_offers_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void makePhoneCall(String number){

        if(number.trim().length() < 10 ){

            Toast.makeText(MyOffersPage.this, "Seller does not wish to share his number", Toast.LENGTH_SHORT).show();

        } else {

            if (ContextCompat.checkSelfPermission(MyOffersPage.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MyOffersPage.this,
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

    @Override
    protected void onStart() {
        super.onStart();

        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();

        adapter.stopListening();

    }

}
