package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class LocationFilterStatePage extends AppCompatActivity {

    private Intent intent1;
    private ImageView backbutton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productRef = db.collection("States&Cities");
    SharedPreferences mSharedPref;

    private Location_Filter_State_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_filter_page);

        backbutton = (ImageView) findViewById(R.id.state_back_button);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setUpRecyclerView();

        adapter.setItemOnClickListener(new Location_Filter_State_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Location_Filter_State_Get_Set details = documentSnapshot.toObject(Location_Filter_State_Get_Set.class);

                String test2 = "All States";
                String test = details.getState().toString();
                String test3 = test;
                Toast.makeText(LocationFilterStatePage.this,test, Toast.LENGTH_SHORT).show();
                if(test3.equals(test2)){
                    intent1 = new Intent(LocationFilterStatePage.this, FilterPage.class);
                    Toast.makeText(LocationFilterStatePage.this,test, Toast.LENGTH_SHORT).show();
                    mSharedPref = getSharedPreferences("TemporaryFilterSettings", MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSharedPref.edit();
                    editor.putString("TemporaryState", "All States");
                    editor.putString("TemporaryCity", "All Cities");
                    editor.apply();
                    startActivity(intent1);
                    finish();
                }

                else {

                    Toast.makeText(LocationFilterStatePage.this,test, Toast.LENGTH_SHORT).show();
                    intent1 = new Intent(LocationFilterStatePage.this, LocationFilterCityPage.class);
                    intent1.putExtra("State", details.getState());
                    startActivity(intent1);

                }

            }
        });

    }

    private void setUpRecyclerView() {

        Query query = productRef
                .orderBy("State");
                FirestoreRecyclerOptions<Location_Filter_State_Get_Set> options = new FirestoreRecyclerOptions.Builder<Location_Filter_State_Get_Set>()
                .setQuery(query, Location_Filter_State_Get_Set.class)
                .build();

        adapter = new Location_Filter_State_Adapter(options);

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
