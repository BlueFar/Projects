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

public class MyProductsPage extends AppCompatActivity {

    private Intent intent1;
    private ImageView backbutton;
    String userid;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private MyProducts_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_products_page);


        backbutton = (ImageView) findViewById(R.id.my_products_back_button);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setUpRecyclerView();

    }

    private void setUpRecyclerView() {

        CollectionReference productRef = db.collection("Users").document(userid).collection("RegisteredProducts");
        Query query = productRef
                .orderBy("ProductName");
        FirestoreRecyclerOptions<MyProducts_Get_Set> options = new FirestoreRecyclerOptions.Builder<MyProducts_Get_Set>()
                .setQuery(query, MyProducts_Get_Set.class)
                .build();

        adapter = new MyProducts_Adapter(options);

        RecyclerView recyclerView = findViewById(R.id.my_products_recyclerview);
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
