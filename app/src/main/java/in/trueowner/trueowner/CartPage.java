package in.trueowner.trueowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class CartPage extends AppCompatActivity {

    private Intent intent1;
    private ImageView backbutton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dtemp = db.collection("Users");
    CollectionReference productRef;
    public static final int REQUEST_CALL = 9;
    private Cart_Adapter adapter;
    String asktemp, userid, sellernumber;
    Dialog makeofferpopup;
    int flag =0, flag1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_page);

        backbutton = (ImageView) findViewById(R.id.cart_back_button);
        makeofferpopup = new Dialog(this);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {

            userid = currentUser.getUid();

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent1 = new Intent(CartPage.this, HomePage.class);
                finish();
            }
        });

         productRef = dtemp.document(userid).collection("Cart");
        setUpRecyclerView();

        adapter.setItemOnClickListener(new Cart_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Cart_Get_Set details = documentSnapshot.toObject(Cart_Get_Set.class);

                sellernumber = details.getSellerNumber();
                        intent1 = new Intent(CartPage.this, ItemInfoPage.class);
                intent1.putExtra("ProductID",details.getProductID());
                startActivity(intent1);
                finish();
            }
        });

        } else {

            Toast.makeText(CartPage.this, "no user", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CartPage.this, LoginPage.class);
            startActivity(intent);

        }

    }

    private void setUpRecyclerView() {

        Query query = productRef
                .orderBy("ProductName");
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

    public void makePhoneCall( String number){

        if(number.trim().length() < 10 ){

            Toast.makeText(CartPage.this, "Seller does not wish to share his number", Toast.LENGTH_SHORT).show();

        } else {

            if (ContextCompat.checkSelfPermission(CartPage.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(CartPage.this,
                        new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);

            } else {

                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

            }

        }


    }

    public void ShowPopup2(View v, final String productidpopup, final String useridpopup, final String previousaskedprice1, final long askedprice1, final String sellerid1, final String sellername1, final String sellernumber1, final String sellercity1, final String sellerstate1, final String productname1) {

        ImageButton submitbutton;
        final EditText editoffer;
        TextView exitbutton;
        final Button askedpricebutton;
        makeofferpopup.setContentView(R.layout.item_info_offer_popup);
        exitbutton =(TextView) makeofferpopup.findViewById(R.id.item_info_popup_exit_button);
        editoffer = (EditText) makeofferpopup.findViewById(R.id.item_info_popup_enter_price_box);
        submitbutton = (ImageButton) makeofferpopup.findViewById(R.id.item_info_popup_price_submit_button);
        askedpricebutton = (Button) makeofferpopup.findViewById(R.id.item_info_popup_asking_price_button);
        String Price = String.valueOf(askedprice1);
        askedpricebutton.setText(Price);
        editoffer.setText(previousaskedprice1);

        final Map<String, Object> sell = new HashMap<>();
        sell.put("BidderID", useridpopup);
        sell.put("ProductID", productidpopup);
        sell.put("SellerID", sellerid1);
        sell.put("SellerName", sellername1);
        sell.put("ProductName", productname1);
        sell.put("OfferedPrice", previousaskedprice1);
        sell.put("OfferStatus", "Pending");
        sell.put("SellerNumber", sellernumber1);
        sell.put("VerificationStatus", false);
        sell.put("ReviewStatus", false);
        sell.put("EditOfferStatus", false);
        sell.put("FinalAcceptStatus", false);
        sell.put("PayDeliveryStatus", false);
        sell.put("BuyerCity", "");
        sell.put("BuyerState", "");
        sell.put("DeliveryFee", 0);
        sell.put("SellerCity", sellercity1);
        sell.put("SellerState", sellerstate1);
        sell.put("AdminID", "");


        askedpricebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                asktemp = String.valueOf(askedpricebutton.getText());
                editoffer.setText(asktemp);


            }
        });

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final long offeredprice = Long.valueOf(editoffer.getText().toString());


                DocumentReference dbref = db.collection("Users").document(useridpopup).collection("Cart").document(productidpopup);
                dbref.update("OfferedPrice", offeredprice).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        flag1 = 1;

                    }
                });

                if (flag1==1){


                    DocumentReference dbre = db.collection("Users").document(sellerid1).collection("Sell").document(productidpopup).collection("Offers").document(useridpopup);
                    dbre.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if (documentSnapshot.exists()){

                                DocumentReference dbre1 = db.collection("Users").document(sellerid1).collection("Sell").document(productidpopup).collection("Offers").document(useridpopup);
                                dbre1.update("OfferedPrice", offeredprice).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        flag1 =2;

                                    }
                                });

                            }
                            else {

                                DocumentReference dbre4 = db.collection("Users").document(sellerid1).collection("Sell").document(productidpopup).collection("Offers").document(useridpopup);
                                dbre4.set(sell).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        DocumentReference dbref5 = db.collection("Users").document(useridpopup).collection("Cart").document(productidpopup);
                                        dbref5.update("OfferStatus", "Pending");
                                        flag1 =2;

                                    }
                                });


                            }

                        }
                    });

                }


                if (flag1 == 2){

                    makeofferpopup.dismiss();
                    Toast.makeText(CartPage.this,"Offer Updated",Toast.LENGTH_SHORT).show();

                }


            }
        });

        exitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeofferpopup.dismiss();
            }
        });
        makeofferpopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        makeofferpopup.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {

            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                makePhoneCall(sellernumber);

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
