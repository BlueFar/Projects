package in.trueowner.trueowner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class LocationFilterCityPage extends AppCompatActivity {

    private Intent intent1;
    private TextView header;
    private ImageView backbutton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productRef = db.collection("States&Cities");
    SharedPreferences mSharedPref;

    private Location_Filter_City_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_filter_page);


        Intent intent = getIntent();
        final String state = intent.getExtras().getString("State");

        backbutton = (ImageView) findViewById(R.id.state_back_button);
        header = (TextView) findViewById(R.id.filter_header);
        header.setText("City");

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setUpRecyclerView(state);

        adapter.setItemOnClickListener(new Location_Filter_City_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Location_Filter_City_Get_Set details = documentSnapshot.toObject(Location_Filter_City_Get_Set.class);

                String test = details.getCity();

                    intent1 = new Intent(LocationFilterCityPage.this, FilterPage.class);
                    mSharedPref = getSharedPreferences("TemporaryFilterSettings", MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSharedPref.edit();
                    editor.putString("TemporaryState", state);
                editor.putString("TemporaryCity", test);
                    editor.apply();
                    startActivity(intent1);
                    finish();



            }
        });

    }

    private void setUpRecyclerView(String state) {

        Query query = productRef.document(state).collection("Cities")
                .orderBy("City");
                FirestoreRecyclerOptions<Location_Filter_City_Get_Set> options = new FirestoreRecyclerOptions.Builder<Location_Filter_City_Get_Set>()
                .setQuery(query, Location_Filter_City_Get_Set.class)
                .build();

        adapter = new Location_Filter_City_Adapter(options);

        RecyclerView recyclerView = findViewById(R.id.location_filter_recyclerview);
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
