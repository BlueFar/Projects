package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CartPage extends AppCompatActivity {

    private Intent intent1;
    private ImageView backbutton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productRef = db.collection("Marketplace");

    private Cart_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_page);
        backbutton = (ImageView) findViewById(R.id.cart_back_button);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent1 = new Intent(CartPage.this, HomePage.class);
                finish();
            }
        });

        setUpRecyclerView();

        adapter.setItemOnClickListener(new Cart_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Cart_Get_Set details = documentSnapshot.toObject(Cart_Get_Set.class);

                intent1 = new Intent(CartPage.this, ItemInfoPage.class);
                intent1.putExtra("Price",details.getPrice());
                intent1.putExtra("Description",details.getDescription());
                intent1.putExtra("Name",details.getName());
                intent1.putExtra("Condition",details.getCondition());
                intent1.putExtra("ProductID",details.getProductID());
                intent1.putExtra("Owner Name",details.getOwnerName());
                intent1.putExtra("State",details.getState());
                intent1.putExtra("City",details.getCity());
                startActivity(intent1);
            }
        });
    }

    private void setUpRecyclerView() {

        Query query = productRef
                .orderBy("Name");
        FirestoreRecyclerOptions<Cart_Get_Set> options = new FirestoreRecyclerOptions.Builder<Cart_Get_Set>()
                .setQuery(query, Cart_Get_Set.class)
                .build();

        adapter = new Cart_Adapter(options);

        RecyclerView recyclerView = findViewById(R.id.cart_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
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
