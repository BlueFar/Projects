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

public class AdminSellerList extends AppCompatActivity {

    private Intent intent1;
    private ImageView backbutton;
    String adminid, userid;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productRef;
    FirebaseUser user;

    private Admin_Seller_List_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_seller_list);

        backbutton = (ImageView) findViewById(R.id.admin_seller_list_back_button);

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

            productRef = db.collection("Admins").document(adminid).collection("SellerVerification").document("Pending").collection("Users");

            setUpRecyclerView();

            adapter.setItemOnClickListener(new Admin_Seller_List_Adapter.OnItemClickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                    Admin_Seller_List_Get_Set details = documentSnapshot.toObject(Admin_Seller_List_Get_Set.class);

                    String userid = details.getUserID();
                    intent1 = new Intent(AdminSellerList.this, AdminSellerVerification.class);
                    intent1.putExtra("AdminID", adminid);
                    intent1.putExtra("UserID", userid);
                    startActivity(intent1);
                }
            });

        } else {

            Intent intent1 = new Intent(AdminSellerList.this, LoginPage.class);
            startActivity(intent1);
            finish();

        }
    }

    private void setUpRecyclerView() {

        Query query = productRef
                .orderBy("Name");
        FirestoreRecyclerOptions<Admin_Seller_List_Get_Set> options = new FirestoreRecyclerOptions.Builder<Admin_Seller_List_Get_Set>()
                .setQuery(query, Admin_Seller_List_Get_Set.class)
                .build();

        adapter = new Admin_Seller_List_Adapter(options);

        RecyclerView recyclerView = findViewById(R.id.admin_seller_list_recyclerview);
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

        } else {

            Intent intent1 = new Intent(AdminSellerList.this, LoginPage.class);
            startActivity(intent1);
            finish();

        }

        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();

        adapter.stopListening();

    }
}
