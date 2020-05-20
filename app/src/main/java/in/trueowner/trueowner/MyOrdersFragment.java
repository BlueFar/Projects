package in.trueowner.trueowner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MyOrdersFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productRef ;
    String userid;
    TextView cartcount;
    ImageView cartbutton;

    private MyOrders_Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.my_orders_fragment,container,false);

        cartcount = (TextView)view.findViewById(R.id.my_orders_cart_count);
        cartbutton = (ImageView)view.findViewById(R.id.my_orders_cart_button);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser !=null){

            userid = currentUser.getUid();

            productRef = db.collection("Users").document(userid).collection("MyOrders");

        CollectionReference deref = db.collection("Users").document(userid).collection("Cart");
        deref.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {

                                cartcount.setText("0");

                            } else {

                                String count = String.valueOf(task.getResult().size());
                                cartcount.setText(count);


                            }

                        } else {

                            return;

                        }
                    }
                });

       cartbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent1 = new Intent(getActivity(), CartPage.class);
                startActivity(intent1);

            }
        });

        setUpRecyclerView(view);

        adapter.setItemOnClickListener(new MyOrders_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                MyOrders_Get_Set details = documentSnapshot.toObject(MyOrders_Get_Set.class);

                Intent intent1 = new Intent(getActivity(), TrackingOrder.class);
                intent1.putExtra("UserID",userid);
                intent1.putExtra("OfferedPrice",details.getPrice());
                intent1.putExtra("DeliveryFee",details.getDeliveryFee());
                intent1.putExtra("RegisterFee",details.getRegisterFee());
                intent1.putExtra("ProductID",details.getProductID());
                intent1.putExtra("ProductName",details.getProductName());
                intent1.putExtra("Image1",details.getImage1());
                intent1.putExtra("SellerID",details.getSellerID());
                intent1.putExtra("BidderID",details.getBidderID());
                intent1.putExtra("SellerAdminID",details.getSellerAdminID());
                intent1.putExtra("BidderAdminID",details.getBidderAdminID());
                intent1.putExtra("OrderDate",details.getOrderDate());
                intent1.putExtra("OrderStatus",details.getOrderStatus());
                startActivity(intent1);
            }
        });

        }
        else {

            Toast.makeText(getActivity(), "no user", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), LoginPage.class);
            startActivity(intent);

        }

        return view;

    }

    private void setUpRecyclerView(View v) {

        Query query = productRef
                .orderBy("OrderDate", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MyOrders_Get_Set> options = new FirestoreRecyclerOptions.Builder<MyOrders_Get_Set>()
                .setQuery(query, MyOrders_Get_Set.class)
                .build();

        adapter = new MyOrders_Adapter(options);

        RecyclerView recyclerView = v.findViewById(R.id.my_orders_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {

                userid = currentUser.getUid();
                adapter.startListening();

            } else {

                Toast.makeText(getActivity(), "no user", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LoginPage.class);
                startActivity(intent);

            }


    }

    @Override
    public void onStop() {
        super.onStop();

        adapter.stopListening();

    }

}
