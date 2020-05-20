package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AdminDropCourier extends AppCompatActivity {

    private Intent intent1;
    private ImageView backbutton;
    String adminid, userid;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productRef;
    FirebaseUser user;
    private AdminDropCourier_List_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_drop_courier);

        backbutton = (ImageView) findViewById(R.id.admin_drop_courier_back_button);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user !=null) {

            userid = user.getUid();


            adminid = userid;

            backbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            productRef = db.collection("Admins").document(adminid).collection("Drop").document("ToCourier").collection("Details");

            setUpRecyclerView();

            adapter.setItemOnClickListener(new AdminDropCourier_List_Adapter.OnItemClickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                    AdminDropCourier_List_Get_Set details = documentSnapshot.toObject(AdminDropCourier_List_Get_Set.class);

                    String productid = details.getProductID();
                    intent1 = new Intent(AdminDropCourier.this, AdminPickupSeller.class);
                    intent1.putExtra("AdminID", adminid);
                    intent1.putExtra("ProductID", productid);
                    startActivity(intent1);
                }
            });

        } else {

            Intent intent1 = new Intent(AdminDropCourier.this, LoginPage.class);
            startActivity(intent1);
            finish();

        }

    }

    private void setUpRecyclerView() {

        Query query = productRef
                .orderBy("ProductName");
        FirestoreRecyclerOptions<AdminDropCourier_List_Get_Set> options = new FirestoreRecyclerOptions.Builder<AdminDropCourier_List_Get_Set>()
                .setQuery(query, AdminDropCourier_List_Get_Set.class)
                .build();

        adapter = new AdminDropCourier_List_Adapter(options);

        RecyclerView recyclerView = findViewById(R.id.admin_drop_courier_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user !=null){

            userid = user.getUid();

            adapter.startListening();

        }
        else {

            Intent intent1 = new Intent(AdminDropCourier.this, LoginPage.class);
            startActivity(intent1);
            finish();

        }



    }

    @Override
    protected void onStop() {
        super.onStop();

        adapter.stopListening();

    }

}
