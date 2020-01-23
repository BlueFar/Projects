package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class Marketplace_Page extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private Intent intent1;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productRef = db.collection("Marketplace");
    SharedPreferences mSharedPref;

    private Button filterbutton,sortbutton;
    private ImageView backbutton;
    private TextView resultstextview;

    private Marketplace_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marketplace__page);

        backbutton = (ImageView) findViewById(R.id.marketplace_back_button);
        sortbutton = (Button) findViewById(R.id.marketplace_sort_button);
        filterbutton = (Button) findViewById(R.id.marketplace_filter_button);
        resultstextview = (TextView) findViewById(R.id.marketplace_results_textview);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mSharedPref = getSharedPreferences("TemporaryFilterSettings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putLong("applyPrice1", 0);
                editor.putLong("applyPrice2", 100000);
                editor.putLong("applyCondition1", 0);
                editor.putLong("applyCondition2", 120);
                editor.putString("applyState", "All States");
                editor.putString("applyCity", "All Cities");
                editor.apply();
            }
        });

        setUpRecyclerView();

      //  resultstextview.setText(String.valueOf(adapter.getItemCount())) ;

        adapter.setItemOnClickListener(new Marketplace_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                    Marketplace_Get_Set details = documentSnapshot.toObject(Marketplace_Get_Set.class);

                intent1 = new Intent(Marketplace_Page.this, ItemInfoPage.class);
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


        sortbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });


        filterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Marketplace_Page.this, FilterPage.class);
                startActivity(intent);
            }
        });

    }

    public void showPopup(View v){

        PopupMenu sort = new PopupMenu(this,v);
        sort.setOnMenuItemClickListener(this);
        sort.inflate(R.menu.sort_menu);
        sort.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){

            case R.id.price_low_to_high_item:
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "LowToHigh");
                editor.apply();
                finish();
                startActivity(getIntent());
                overridePendingTransition(0, 0);

                return true;
            case R.id.price_high_to_low_item:

                editor = mSharedPref.edit();
                editor.putString("Sort", "HighToLow");
                editor.apply();
                finish();
                startActivity(getIntent());
                overridePendingTransition(0, 0);


                return true;
            case R.id.condition_recent_item:

                editor = mSharedPref.edit();
                editor.putString("Sort", "Condition");
                editor.apply();
                finish();
                startActivity(getIntent());
                overridePendingTransition(0, 0);


                return true;

            default:
                return false;

        }
    }

    private void setUpRecyclerView() {

        mSharedPref = getSharedPreferences("Sort Settings", MODE_PRIVATE);
        String mSorting = mSharedPref.getString("Sort", "Name");


        if(mSorting.equals("LowToHigh"))
        {

            mSharedPref = getSharedPreferences("TemporaryFilterSettings",MODE_PRIVATE);
            long price1 = mSharedPref.getLong("applyPrice1", 0);
            long price2 = mSharedPref.getLong("applyPrice2", 100000);
            long condition1 = mSharedPref.getLong("applyCondition1", 0);
            long condition2 = mSharedPref.getLong("applyCondition2", 120);
            String state = mSharedPref.getString("applyState", "All States");
            String city = mSharedPref.getString("applyCity", "All Cities");

            if(price1==0 && price2 == 100000 && condition1 == 0 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 0 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 0 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 0 && condition2 == 1 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 0 && condition2 == 1 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 0 && condition2 == 1 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 1 && condition2 == 12 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 1 && condition2 == 12 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 1 && condition2 == 12 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 12 && condition2 == 24 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 12 && condition2 == 24 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 12 && condition2 == 24 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 24 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 24 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 24 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 0 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 0 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 0 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 0 && condition2 == 1 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 0 && condition2 == 1 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 0 && condition2 == 1 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 1 && condition2 == 12 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 1 && condition2 == 12 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 1 && condition2 == 12 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 12 && condition2 == 24 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 12 && condition2 == 24 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 12 && condition2 == 24 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 24 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 24 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 24 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

        else if(price1==5000 && price2 == 10000 && condition1 == 0 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
        }

        else if(price1==5000 && price2 == 10000 && condition1 == 0 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
        }

        else if(price1==5000 && price2 == 10000 && condition1 == 0 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
        }

        else if(price1==5000 && price2 == 10000 && condition1 == 0 && condition2 == 1 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
        }

        else if(price1==5000 && price2 == 10000 && condition1 == 0 && condition2 == 1 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
        }

        else if(price1==5000 && price2 == 10000 && condition1 == 0 && condition2 == 1 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
        }

        else if(price1==5000 && price2 == 10000 && condition1 == 1 && condition2 == 12 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
        }

        else if(price1==5000 && price2 == 10000 && condition1 == 1 && condition2 == 12 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
        }

        else if(price1==5000 && price2 == 10000 && condition1 == 1 && condition2 == 12 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
        }

        else if(price1==5000 && price2 == 10000 && condition1 == 12 && condition2 == 24 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
        }

        else if(price1==5000 && price2 == 10000 && condition1 == 12 && condition2 == 24 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
        }

        else if(price1==5000 && price2 == 10000 && condition1 == 12 && condition2 == 24 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
        }

        else if(price1==5000 && price2 == 10000 && condition1 == 24 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
        }

        else if(price1==5000 && price2 == 10000 && condition1 == 24 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
        }

        else if(price1==5000 && price2 == 10000 && condition1 == 24 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
        }

            else if(price1==10000 && price2 == 20000 && condition1 == 0 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 0 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 0 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 0 && condition2 == 1 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 0 && condition2 == 1 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 0 && condition2 == 1 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 1 && condition2 == 12 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 1 && condition2 == 12 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 1 && condition2 == 12 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 12 && condition2 == 24 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 12 && condition2 == 24 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 12 && condition2 == 24 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 24 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 24 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 24 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 0 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 0 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 0 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 0 && condition2 == 1 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 0 && condition2 == 1 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 0 && condition2 == 1 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 1 && condition2 == 12 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 1 && condition2 == 12 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 1 && condition2 == 12 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 12 && condition2 == 24 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 12 && condition2 == 24 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 12 && condition2 == 24 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 24 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 24 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 24 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else{

                Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();

            }



          //  if (state=="All States"){
         /*  Query query = productRef
                        .orderBy("Price", Query.Direction.ASCENDING);
               FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();*/

            //  .whereGreaterThanOrEqualTo("Price",price1)
            //   .whereLessThanOrEqualTo("Price",price2)
            //  .whereGreaterThanOrEqualTo("Condition",condition1)
            //  .whereLessThanOrEqualTo("Condition",condition2)




          //  }

           /* else if (city == "All Cities"){

                Query query = productRef.whereGreaterThanOrEqualTo("Price",price1)
                        .whereLessThanOrEqualTo("Price",price2)
                        .whereGreaterThanOrEqualTo("Condition",condition1)
                        .whereLessThanOrEqualTo("Condition",condition2)
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();



            }

            else {

                Query query = productRef.whereGreaterThanOrEqualTo("Price",price1)
                        .whereLessThanOrEqualTo("Price",price2)
                        .whereGreaterThanOrEqualTo("Condition",condition1)
                        .whereLessThanOrEqualTo("Condition",condition2)
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();



            }*/

        }

        else if (mSorting.equals("HighToLow"))
        {

            mSharedPref = getSharedPreferences("TemporaryFilterSettings",MODE_PRIVATE);
            long price1 = mSharedPref.getLong("applyPrice1", 0);
            long price2 = mSharedPref.getLong("applyPrice2", 100000);
            long condition1 = mSharedPref.getLong("applyCondition1", 0);
            long condition2 = mSharedPref.getLong("applyCondition2", 120);
            String state = mSharedPref.getString("applyState", "All States");
            String city = mSharedPref.getString("applyCity", "All Cities");

            if(price1==0 && price2 == 100000 && condition1 == 0 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 0 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 0 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 0 && condition2 == 1 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 0 && condition2 == 1 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 0 && condition2 == 1 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 1 && condition2 == 12 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 1 && condition2 == 12 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 1 && condition2 == 12 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 12 && condition2 == 24 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 12 && condition2 == 24 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 12 && condition2 == 24 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 24 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 24 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 24 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 0 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 0 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 0 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 0 && condition2 == 1 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 0 && condition2 == 1 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 0 && condition2 == 1 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 1 && condition2 == 12 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 1 && condition2 == 12 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 1 && condition2 == 12 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 12 && condition2 == 24 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 12 && condition2 == 24 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 12 && condition2 == 24 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 24 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 24 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 24 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 0 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 0 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 0 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 0 && condition2 == 1 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 0 && condition2 == 1 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 0 && condition2 == 1 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 1 && condition2 == 12 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 1 && condition2 == 12 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 1 && condition2 == 12 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 12 && condition2 == 24 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 12 && condition2 == 24 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 12 && condition2 == 24 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 24 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 24 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 24 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 0 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 0 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 0 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 0 && condition2 == 1 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 0 && condition2 == 1 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 0 && condition2 == 1 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 1 && condition2 == 12 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 1 && condition2 == 12 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 1 && condition2 == 12 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 12 && condition2 == 24 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 12 && condition2 == 24 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 12 && condition2 == 24 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 24 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 24 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 24 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 0 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 0 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 0 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 0 && condition2 == 1 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 0 && condition2 == 1 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 0 && condition2 == 1 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 1 && condition2 == 12 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 1 && condition2 == 12 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 1 && condition2 == 12 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 12 && condition2 == 24 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 12 && condition2 == 24 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 12 && condition2 == 24 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 24 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 24 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 24 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else{

                Query query = productRef
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();

            }

            // if (state=="All States"){

              /*  Query query = productRef
                        //.whereGreaterThanOrEqualTo("Price",price1)
                       // .whereLessThanOrEqualTo("Price",price2)
                       // .whereGreaterThanOrEqualTo("Condition",condition1)
                      //  .whereLessThanOrEqualTo("Condition",condition2)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();*/


          //  }

          /*  else if (city == "All Cities"){

                Query query = productRef.whereGreaterThanOrEqualTo("Price",price1)
                        .whereLessThanOrEqualTo("Price",price2)
                        .whereGreaterThanOrEqualTo("Condition",condition1)
                        .whereLessThanOrEqualTo("Condition",condition2)
                        .whereEqualTo("State", state)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();



            }

            else {

                Query query = productRef.whereGreaterThanOrEqualTo("Price",price1)
                        .whereLessThanOrEqualTo("Price",price2)
                        .whereGreaterThanOrEqualTo("Condition",condition1)
                        .whereLessThanOrEqualTo("Condition",condition2)
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Price", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();



            }*/


        }

        else if (mSorting.equals("Condition"))
        {

            mSharedPref = getSharedPreferences("TemporaryFilterSettings",MODE_PRIVATE);
            long price1 = mSharedPref.getLong("applyPrice1", 0);
            long price2 = mSharedPref.getLong("applyPrice2", 100000);
            long condition1 = mSharedPref.getLong("applyCondition1", 0);
            long condition2 = mSharedPref.getLong("applyCondition2", 120);
            String state = mSharedPref.getString("applyState", "All States");
            String city = mSharedPref.getString("applyCity", "All Cities");

            if(price1==0 && price2 == 100000 && condition1 == 0 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 0 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 0 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 0 && condition2 == 1 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 0 && condition2 == 1 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 0 && condition2 == 1 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 1 && condition2 == 12 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 1 && condition2 == 12 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 1 && condition2 == 12 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 12 && condition2 == 24 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 12 && condition2 == 24 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 12 && condition2 == 24 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 24 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 24 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 100000 && condition1 == 24 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 0 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 0 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 0 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 0 && condition2 == 1 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 0 && condition2 == 1 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 0 && condition2 == 1 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 1 && condition2 == 12 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 1 && condition2 == 12 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 1 && condition2 == 12 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 12 && condition2 == 24 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 12 && condition2 == 24 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 12 && condition2 == 24 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 24 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 24 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==0 && price2 == 5000 && condition1 == 24 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 0 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 0 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 0 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 0 && condition2 == 1 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 0 && condition2 == 1 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 0 && condition2 == 1 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 1 && condition2 == 12 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 1 && condition2 == 12 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 1 && condition2 == 12 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 12 && condition2 == 24 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 12 && condition2 == 24 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 12 && condition2 == 24 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 24 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 24 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==5000 && price2 == 10000 && condition1 == 24 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 0 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 0 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 0 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 0 && condition2 == 1 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 0 && condition2 == 1 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 0 && condition2 == 1 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 1 && condition2 == 12 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 1 && condition2 == 12 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 1 && condition2 == 12 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 12 && condition2 == 24 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 12 && condition2 == 24 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 12 && condition2 == 24 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 24 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 24 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==10000 && price2 == 20000 && condition1 == 24 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 0 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 0 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 0 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 0 && condition2 == 1 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 0 && condition2 == 1 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 0 && condition2 == 1 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 1 && condition2 == 12 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 1 && condition2 == 12 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 1 && condition2 == 12 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 12 && condition2 == 24 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 12 && condition2 == 24 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 12 && condition2 == 24 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 24 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 24 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else if(price1==20000 && price2 == 100000 && condition1 == 24 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();
            }

            else{

                Query query = productRef
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();

            }

           // if (state=="All States"){

              /*  Query query = productRef
                        //.whereGreaterThanOrEqualTo("Price",price1)
                       // .whereLessThanOrEqualTo("Price",price2)
                       // .whereGreaterThanOrEqualTo("Condition",condition1)
                      //  .whereLessThanOrEqualTo("Condition",condition2)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                mSharedPref = getSharedPreferences("Sort Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();*/


          //  }

          /*  else if (city == "All Cities"){

                Query query = productRef.whereGreaterThanOrEqualTo("Price",price1)
                        .whereLessThanOrEqualTo("Price",price2)
                        .whereGreaterThanOrEqualTo("Condition",condition1)
                        .whereLessThanOrEqualTo("Condition",condition2)
                        .whereEqualTo("State", state)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();


            }

            else {

                Query query = productRef.whereGreaterThanOrEqualTo("Price",price1)
                        .whereLessThanOrEqualTo("Price",price2)
                        .whereGreaterThanOrEqualTo("Condition",condition1)
                        .whereLessThanOrEqualTo("Condition",condition2)
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Condition", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putString("Sort", "Name");
                editor.apply();


            }*/

        }

        else if (mSorting.equals("Name")) {


            mSharedPref = getSharedPreferences("TemporaryFilterSettings",MODE_PRIVATE);
            long price1 = mSharedPref.getLong("applyPrice1", 0);
            long price2 = mSharedPref.getLong("applyPrice2", 100000);
            long condition1 = mSharedPref.getLong("applyCondition1", 0);
            long condition2 = mSharedPref.getLong("applyCondition2", 120);
            String state = mSharedPref.getString("applyState", "All States");
            String city = mSharedPref.getString("applyCity", "All Cities");

            if(price1==0 && price2 == 100000 && condition1 == 0 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 100000 && condition1 == 0 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 100000 && condition1 == 0 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 100000 && condition1 == 0 && condition2 == 1 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 100000 && condition1 == 0 && condition2 == 1 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 100000 && condition1 == 0 && condition2 == 1 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 100000 && condition1 == 1 && condition2 == 12 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 100000 && condition1 == 1 && condition2 == 12 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 100000 && condition1 == 1 && condition2 == 12 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 100000 && condition1 == 12 && condition2 == 24 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 100000 && condition1 == 12 && condition2 == 24 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 100000 && condition1 == 12 && condition2 == 24 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 100000 && condition1 == 24 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 100000 && condition1 == 24 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 100000 && condition1 == 24 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 5000 && condition1 == 0 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 5000 && condition1 == 0 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 5000 && condition1 == 0 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 5000 && condition1 == 0 && condition2 == 1 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 5000 && condition1 == 0 && condition2 == 1 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 5000 && condition1 == 0 && condition2 == 1 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 5000 && condition1 == 1 && condition2 == 12 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 5000 && condition1 == 1 && condition2 == 12 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 5000 && condition1 == 1 && condition2 == 12 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 5000 && condition1 == 12 && condition2 == 24 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 5000 && condition1 == 12 && condition2 == 24 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 5000 && condition1 == 12 && condition2 == 24 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 5000 && condition1 == 24 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 5000 && condition1 == 24 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==0 && price2 == 5000 && condition1 == 24 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==5000 && price2 == 10000 && condition1 == 0 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==5000 && price2 == 10000 && condition1 == 0 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==5000 && price2 == 10000 && condition1 == 0 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==5000 && price2 == 10000 && condition1 == 0 && condition2 == 1 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==5000 && price2 == 10000 && condition1 == 0 && condition2 == 1 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==5000 && price2 == 10000 && condition1 == 0 && condition2 == 1 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==5000 && price2 == 10000 && condition1 == 1 && condition2 == 12 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==5000 && price2 == 10000 && condition1 == 1 && condition2 == 12 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==5000 && price2 == 10000 && condition1 == 1 && condition2 == 12 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==5000 && price2 == 10000 && condition1 == 12 && condition2 == 24 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==5000 && price2 == 10000 && condition1 == 12 && condition2 == 24 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==5000 && price2 == 10000 && condition1 == 12 && condition2 == 24 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==5000 && price2 == 10000 && condition1 == 24 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==5000 && price2 == 10000 && condition1 == 24 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==5000 && price2 == 10000 && condition1 == 24 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==10000 && price2 == 20000 && condition1 == 0 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==10000 && price2 == 20000 && condition1 == 0 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==10000 && price2 == 20000 && condition1 == 0 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==10000 && price2 == 20000 && condition1 == 0 && condition2 == 1 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==10000 && price2 == 20000 && condition1 == 0 && condition2 == 1 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==10000 && price2 == 20000 && condition1 == 0 && condition2 == 1 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==10000 && price2 == 20000 && condition1 == 1 && condition2 == 12 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==10000 && price2 == 20000 && condition1 == 1 && condition2 == 12 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==10000 && price2 == 20000 && condition1 == 1 && condition2 == 12 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==10000 && price2 == 20000 && condition1 == 12 && condition2 == 24 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==10000 && price2 == 20000 && condition1 == 12 && condition2 == 24 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==10000 && price2 == 20000 && condition1 == 12 && condition2 == 24 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==10000 && price2 == 20000 && condition1 == 24 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==10000 && price2 == 20000 && condition1 == 24 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==10000 && price2 == 20000 && condition1 == 24 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==20000 && price2 == 100000 && condition1 == 0 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==20000 && price2 == 100000 && condition1 == 0 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==20000 && price2 == 100000 && condition1 == 0 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==20000 && price2 == 100000 && condition1 == 0 && condition2 == 1 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==20000 && price2 == 100000 && condition1 == 0 && condition2 == 1 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==20000 && price2 == 100000 && condition1 == 0 && condition2 == 1 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==20000 && price2 == 100000 && condition1 == 1 && condition2 == 12 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==20000 && price2 == 100000 && condition1 == 1 && condition2 == 12 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==20000 && price2 == 100000 && condition1 == 1 && condition2 == 12 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==20000 && price2 == 100000 && condition1 == 12 && condition2 == 24 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==20000 && price2 == 100000 && condition1 == 12 && condition2 == 24 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==20000 && price2 == 100000 && condition1 == 12 && condition2 == 24 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==20000 && price2 == 100000 && condition1 == 24 && condition2 == 120 && state == "All States"){

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==20000 && price2 == 100000 && condition1 == 24 && condition2 == 120 && city == "All Cities"){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else if(price1==20000 && price2 == 100000 && condition1 == 24 && condition2 == 120 ){

                Query query = productRef
                        .whereEqualTo("State", state)
                        .whereEqualTo("City", city)
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            else{

                Query query = productRef
                        .orderBy("Name");
                FirestoreRecyclerOptions<Marketplace_Get_Set> options = new FirestoreRecyclerOptions.Builder<Marketplace_Get_Set>()
                        .setQuery(query,Marketplace_Get_Set.class)
                        .build();

                adapter = new Marketplace_Adapter(options);

                RecyclerView recyclerView = findViewById(R.id.marketplace_recyclerview);
                GridLayoutManager layoutManager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);


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
