package in.trueowner.trueowner;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class MyProducts_Adapter extends FirestoreRecyclerAdapter<MyProducts_Get_Set, MyProducts_Adapter.MyViewHolder> {
private OnItemClickListener listener;
    public Intent intent1;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userid, productid, imei1, name, mobileno, imei2temp, serialnotemp, productname, picbilltemp, picprooftemp;
    long purchasedday, purchasedmonth, purchasedyear, purchasedprice1;
    Boolean offerstatus;

    public MyProducts_Adapter(@NonNull FirestoreRecyclerOptions<MyProducts_Get_Set> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i, @NonNull final MyProducts_Get_Set myProducts_get_set) {

        long purchasedprice = myProducts_get_set.getPurchasedPrice();
      //  long bbvalue = purchasedprice

      //   myViewHolder.tv_bbvalue.setText(String.valueOf(myProducts_get_set.getPrice()));
        myViewHolder.tv_name.setText(myProducts_get_set.getName());
        myViewHolder.tv_productid.setText(myProducts_get_set.getProductID());
        userid = myProducts_get_set.getUserID();
        userid = myProducts_get_set.getProductID();
        final Boolean check = myProducts_get_set.getSale();

        final Boolean check2 = myProducts_get_set.getVerificationStatus();

        final DocumentReference productRef =db.collection("Users").document(userid).collection("Sell").document(productid);
        final DocumentReference productRef1 =db.collection("Users").document(userid).collection("RegisterProducts").document(productid);
        productRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {



            }
        });

        if (check2){

            myViewHolder.tv_verificationstatus.setText("Verification status: Accepted");




            if (check) {

                myViewHolder.sell.setText("Remove Sale");
                myViewHolder.sell.setVisibility(View.VISIBLE);

            }

            else {

                myViewHolder.sell.setText("Sell");

            }

        }

        else {

            myViewHolder.tv_verificationstatus.setText("Verification status: Pending");

            myViewHolder.sell.setText("Sell");

        }



        myViewHolder.sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

            if (check2){

                if (check){

                    productRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            productRef1.update("SaleStatus", false);
                            ((MyProductsPage)v.getContext()).recreate();

                        }
                    });

                }
                else {

                    Intent intent = new Intent (v.getContext(), SellDeviceRegister1.class);
                    intent.putExtra("ProductID", productid);
                    intent.putExtra("UserID", userid);
                    v.getContext().startActivity(intent);

                }

            }

            else {

                Intent intent = new Intent (v.getContext(), RegisterDeviceVerificationPage.class);
                 /* intent.putExtra("Brand",brandtemp);
            intent.putExtra("Model",modeltemp);
            intent.putExtra("IMEI1",imei1temp);
            intent.putExtra("IMEI2",imei2temp);
            intent.putExtra("SerialNo",serialnotemp);
            intent.putExtra("DayPurchase",day1);
            intent.putExtra("MonthPurchase",month1);
            intent.putExtra("YearPurchase",year1);
            intent.putExtra("PricePurchase",price1);
            intent.putExtra("MobileNo",mobilenotemp);
                intent.putExtra("PicBill", picbilltemp);
                intent.putExtra("PicProof", picproof);*/
                intent.putExtra("ProductID", productid);
                intent.putExtra("UserID", userid);
                v.getContext().startActivity(intent);

            }

            }
        });


        myViewHolder.lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginPopup loginPopup = new LoginPopup((Activity) v.getContext());
                loginPopup.StartLoadingDialog();

                DocumentReference dr = db.collection("Users").document(userid).collection("RegisterProducts")
                        .document(productid);
                dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        productname = documentSnapshot.getString("ProductName");
                        imei1 = documentSnapshot.getString("Imei1");
                        imei2temp = documentSnapshot.getString("Imei2");
                        serialnotemp = documentSnapshot.getString("SerialNumber");
                        picprooftemp = documentSnapshot.getString("PicProof");
                        picbilltemp = documentSnapshot.getString("PicBill");
                        purchasedday = documentSnapshot.getLong("PurchasedDay");
                        purchasedmonth = documentSnapshot.getLong("PurchasedMonth");
                        purchasedyear = documentSnapshot.getLong("PurchasedYear");
                        purchasedprice1 = documentSnapshot.getLong("PurchasedPrice");

                    }
                });

                db.collection("TrackDevice").whereEqualTo("Imei1", imei1)
                        .limit(1).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if(task.getResult().isEmpty()){

                                        DocumentReference dref = db.collection("Users").document(userid).collection("Details").document("PersonalDetails");
                                        dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                name = documentSnapshot.getString("FullName");
                                                mobileno = documentSnapshot.getString("MobileNumber");

                                            }
                                        });

                                        Map<String, Object> productdetails = new HashMap<>();
                                        productdetails.put("ProductName", productname);
                                        productdetails.put("Imei1", imei1);
                                        productdetails.put("Imei2", imei2temp);
                                        productdetails.put("SerialNumber", serialnotemp);
                                        productdetails.put("PurchaseDay", purchasedday);
                                        productdetails.put("PurchaseMonth", purchasedmonth);
                                        productdetails.put("PurchaseYear", purchasedyear);
                                        productdetails.put("PurchasedPrice", purchasedprice1);
                                        productdetails.put("MobileNumber", mobileno);
                                        productdetails.put("FullName", name);
                                        productdetails.put("PicBill", picbilltemp);
                                        productdetails.put("PicProof", picprooftemp);
                                        productdetails.put("UserID", userid);
                                        String documentid =  db.collection("TrackDevice").document().getId();
                                        productdetails.put("DocumentID", documentid);
                                        DocumentReference dr = db.collection("TrackDevice").document(documentid);
                                        dr.set(productdetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Intent intent = new Intent(v.getContext(), LostRegisterSuccessPage.class);
                                                loginPopup.DismissDialog();
                                                v.getContext().startActivity(intent);

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                loginPopup.DismissDialog();
                                                Toast.makeText(v.getContext(), "Unable to register at this time", Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                    } else {

                                        loginPopup.DismissDialog();
                                        Toast.makeText(v.getContext(), "Product already registered as lost", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(v.getContext(), TrackDevicePage.class);
                                        v.getContext().startActivity(intent);

                                    }
                                }
                            }
                        });

            }
        });

        myViewHolder.myoffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent (v.getContext(), MyOffersPage.class);
                intent.putExtra("ProductID", productid);
                intent.putExtra("UserID", userid);
                v.getContext().startActivity(intent);

            }
        });

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_products_cardview,parent,false);

        return new MyViewHolder(v);
    }

     class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_bbvalue,tv_name,tv_productid, tv_verificationstatus;
        CardView cardview;
         Button sell, lost, myoffers;



            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                tv_name = (TextView) itemView.findViewById(R.id.item_name_id);
                tv_bbvalue = (TextView) itemView.findViewById(R.id.item_bb_value_id);
                tv_verificationstatus = (TextView) itemView.findViewById(R.id.verification_status_id);
                tv_productid = (TextView) itemView.findViewById(R.id.item_product_id);
                sell = (Button) itemView.findViewById(R.id.sell_or_remove_sale);
                lost = (Button) itemView.findViewById(R.id.report_lost_button);
                myoffers = (Button) itemView.findViewById(R.id.offers_button);
                cardview = (CardView) itemView.findViewById(R.id.cardview_market);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if(position !=RecyclerView.NO_POSITION && listener !=null) {

                            listener.onItemClick(getSnapshots().getSnapshot(position), position);

                        }
                        }
                    }
                );

            }
    }

    public interface OnItemClickListener {

        void onItemClick(DocumentSnapshot documentSnapshot, int position);

    }

    public void setItemOnClickListener(OnItemClickListener listener) {

        this.listener = listener;

    }

}