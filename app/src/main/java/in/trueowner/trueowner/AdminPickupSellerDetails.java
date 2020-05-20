package in.trueowner.trueowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminPickupSellerDetails extends AppCompatActivity {

    ImageView backbutton;
    Button acceptbutton;
    TextView productname, sellername, imei;
    String adminid, sellerid, bidderid, sellername1, productname1, productid, imei1, bidderadminid, biddername, bidderstate, biddercity,
            biddernumber, sellerstate, sellernumber, sellercity;
    long offeredprice, registerfee, deliveryfee;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_pickup_seller_details);

        Intent intent = getIntent();
        adminid = intent.getExtras().getString("AdminID");
        productid = intent.getExtras().getString("ProductID");
        backbutton = (ImageView) findViewById(R.id.admin_pickup_seller_details_back_button);
        acceptbutton = (Button) findViewById(R.id.admin_pickup_seller_details_accept_button);
        productname = (TextView) findViewById(R.id.admin_pickup_seller_details_product_name);
        sellername = (TextView) findViewById(R.id.admin_pickup_seller_details_seller_name);
        imei = (TextView) findViewById(R.id.admin_pickup_seller_details_imei1);

            DocumentReference productRef =db.collection("Admins").document(adminid).collection("Pickup")
                    .document("FromSeller").collection("Details").document(productid);
            productRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    productname1 = documentSnapshot.getString("ProductName");
                    sellername1 = documentSnapshot.getString("SellerName");
                    imei1 = documentSnapshot.getString("Imei1");
                    sellerid = documentSnapshot.getString("SellerID");
                    bidderid = documentSnapshot.getString("BidderID");
                    bidderadminid = documentSnapshot.getString("BidderAdminID");
                    biddername = documentSnapshot.getString("BidderName");
                    bidderstate = documentSnapshot.getString("BidderState");
                    biddercity = documentSnapshot.getString("BidderCity");
                    sellerstate = documentSnapshot.getString("SellerState");
                    sellercity = documentSnapshot.getString("SellerCity");
                    sellernumber = documentSnapshot.getString("SellerNumber");
                    biddernumber = documentSnapshot.getString("BidderNumber");
                    offeredprice = documentSnapshot.getLong("OfferedPrice");
                    deliveryfee = documentSnapshot.getLong("DeliveryFee");
                    registerfee = documentSnapshot.getLong("RegisterFee");


                }
            });


        productname.setText(productname1);
        sellername.setText(sellername1);
        imei.setText(imei1);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminPickupSellerDetails.this, AdminPickupSeller.class);
                startActivity(intent);
                finish();

            }
        });

        acceptbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Map<String, Object> admindetails = new HashMap<>();
                admindetails.put("ProductID", productid);
                admindetails.put("BidderAdminID", bidderadminid);
                admindetails.put("ProductName", productname1);
                admindetails.put("Imei1", imei1);
                admindetails.put("BidderID", bidderid);
                admindetails.put("BidderName", biddername);
                admindetails.put("SellerID", sellerid);
                admindetails.put("SellerName", sellername1);
                admindetails.put("BidderState", bidderstate);
                admindetails.put("BidderCity", biddercity);
                admindetails.put("SellerState", sellerstate);
                admindetails.put("SellerCity", sellercity);
                admindetails.put("SellerAdminID", adminid);
                admindetails.put("BidderNumber", biddernumber);
                admindetails.put("SellerNumber", sellernumber);
                admindetails.put("OfferedPrice", offeredprice);
                admindetails.put("DeliveryPrice", deliveryfee);
                admindetails.put("RegisterPrice", registerfee);
                admindetails.put("OrderStatus", "Dropped");

                DocumentReference dref2 = db.collection("Users").document(sellerid).collection("MyOrders").document(productid);
                dref2.update("OrderStatus", "Dropped").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        DocumentReference dref1 = db.collection("Users").document(bidderid).collection("MyOrders").document(productid);
                        dref1.update("OrderStatus", "Dropped").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                DocumentReference dref3 = db.collection("Admins").document(adminid)
                                        .collection("Drop").document("ToCourier")
                                        .collection("Details").document(productid);
                                dref3.set(admindetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        DocumentReference dref4 =db.collection("Admins").document(adminid)
                                                .collection("Pickup")
                                                .document("FromSeller").collection("Details").document(productid);
                                        dref4.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Toast.makeText(AdminPickupSellerDetails.this, "Updated", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(AdminPickupSellerDetails.this, AdminHomePage.class);
                                                startActivity(intent);
                                                finish();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                DocumentReference dref5 = db.collection("Admins").document(adminid)
                                                        .collection("Drop").document("ToCourier")
                                                        .collection("Details").document(productid);
                                                dref5.delete();
                                                DocumentReference dref9 = db.collection("Users").document(bidderid).collection("MyOrders").document(productid);
                                                dref9.update("OrderStatus", "Confirmed");
                                                DocumentReference dref10 = db.collection("Users").document(sellerid).collection("MyOrders").document(productid);
                                                dref10.update("OrderStatus", "Confirmed");
                                                Toast.makeText(AdminPickupSellerDetails.this, "Unable to Confirm, please try again later", Toast.LENGTH_SHORT).show();


                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        DocumentReference dref7 = db.collection("Users").document(bidderid).collection("MyOrders").document(productid);
                                        dref7.update("OrderStatus", "Confirmed");
                                        DocumentReference dref8 = db.collection("Users").document(sellerid).collection("MyOrders").document(productid);
                                        dref8.update("OrderStatus", "Confirmed");
                                        Toast.makeText(AdminPickupSellerDetails.this, "Unable to Confirm, please try again later", Toast.LENGTH_SHORT).show();


                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                DocumentReference dref6 = db.collection("Users").document(sellerid).collection("MyOrders").document(productid);
                                dref6.update("OrderStatus", "Confirmed");
                                Toast.makeText(AdminPickupSellerDetails.this, "Unable to Confirm, please try again later", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(AdminPickupSellerDetails.this, "Unable to Confirm, please try again later", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });

    }
}
