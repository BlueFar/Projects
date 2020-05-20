package in.trueowner.trueowner;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class My_offers_Adapter extends FirestoreRecyclerAdapter<My_Offers_Get_Set, My_offers_Adapter.MyViewHolder> {
private OnItemClickListener listener;
    public Intent intent1;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productRef = db.collection("Users");
    String sellerid, productid, bidderid,biddernumber;

    public My_offers_Adapter(@NonNull FirestoreRecyclerOptions<My_Offers_Get_Set> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i, @NonNull final My_Offers_Get_Set my_offers_get_set) {


        myViewHolder.biddername.setText(my_offers_get_set.getBidderName());
        myViewHolder.offer.setText(my_offers_get_set.getOfferedPrice());
        myViewHolder.number = my_offers_get_set.getBidderNumber();
        biddernumber = my_offers_get_set.getBidderNumber();

         sellerid = my_offers_get_set.getSellerID();
         productid = my_offers_get_set.getProductID();
         bidderid = my_offers_get_set.getBidderID();
        final String sellername = my_offers_get_set.getSellerName();
        final String offeredprice = my_offers_get_set.getOfferedPrice();
        final String status = my_offers_get_set.getStatus();
        final Boolean verificationstatustemp = my_offers_get_set.getVerificationStatus();
        final Boolean reviewstatustemp = my_offers_get_set.getReviewStatus();
        final Boolean editofferstatustemp = my_offers_get_set.getEditOfferStatus();
        final Boolean finalacceptstatustemp = my_offers_get_set.getFinalAcceptStatus();
        final Boolean paydeliverystatustemp = my_offers_get_set.getPayDeliveryStatus();
        final String pending = "Pending";
        final String Accepted = "Accepted";
        String rejected = "Rejected";

        CollectionReference productRef1 = productRef.document(sellerid).collection("Sell").document(productid).collection("Offers");
        productRef1.whereEqualTo("OfferStatus", "Accepted").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (status.equals(pending)){

                    myViewHolder.acceptbutton.setVisibility(View.GONE);
                    myViewHolder.dummybutton.setVisibility(View.INVISIBLE);

                }

                else if (status.equals(Accepted)){

                    myViewHolder.acceptbutton.setText("Proceed");

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                return;

            }
        });




        myViewHolder.callbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MyOffersPage)v.getContext()).makePhoneCall(biddernumber);

            }
        });

        myViewHolder.acceptbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (verificationstatustemp.equals(false) && reviewstatustemp.equals(false) && editofferstatustemp.equals(false) && finalacceptstatustemp.equals(false) && paydeliverystatustemp.equals(false)){

                    Intent intent = new Intent(v.getContext(),SellerVerificationPage.class);
                    intent.putExtra("SellerID",sellerid.toString());
                    intent.putExtra("ProductID",productid.toString());
                    intent.putExtra("BidderID",bidderid.toString());
                    v.getContext().startActivity(intent);
                    ((MyOffersPage)v.getContext()).finish();

                }

                if (verificationstatustemp.equals(true) && reviewstatustemp.equals(false) && editofferstatustemp.equals(false) && finalacceptstatustemp.equals(false) && paydeliverystatustemp.equals(false)){

                    Intent intent = new Intent(v.getContext(),SellerAwaiting.class);
                    intent.putExtra("SellerID",sellerid.toString());
                    intent.putExtra("ProductID",productid.toString());
                    intent.putExtra("BidderID",bidderid.toString());
                    v.getContext().startActivity(intent);
                    ((MyOffersPage)v.getContext()).finish();

                }

                if (verificationstatustemp.equals(true) && reviewstatustemp.equals(true) && editofferstatustemp.equals(false) && finalacceptstatustemp.equals(true) && paydeliverystatustemp.equals(false)){

                    Intent intent = new Intent(v.getContext(),SellerAwaiting.class);
                    intent.putExtra("SellerID",sellerid.toString());
                    intent.putExtra("ProductID",productid.toString());
                    intent.putExtra("BidderID",bidderid.toString());
                    v.getContext().startActivity(intent);
                    ((MyOffersPage)v.getContext()).finish();

                }

                if (verificationstatustemp.equals(true) && reviewstatustemp.equals(false) && editofferstatustemp.equals(true) && finalacceptstatustemp.equals(false) && paydeliverystatustemp.equals(false)){

                    Intent intent = new Intent(v.getContext(),SellerFinalConfirmation.class);
                    intent.putExtra("SellerID",sellerid.toString());
                    intent.putExtra("ProductID",productid.toString());
                    intent.putExtra("BidderID",bidderid.toString());
                    v.getContext().startActivity(intent);
                    ((MyOffersPage)v.getContext()).finish();

                }

                if (verificationstatustemp.equals(true) && reviewstatustemp.equals(false) && editofferstatustemp.equals(true) && finalacceptstatustemp.equals(true) && paydeliverystatustemp.equals(false)){

                    Intent intent = new Intent(v.getContext(),SellerPaymentAwaiting.class);
                    intent.putExtra("SellerID",sellerid.toString());
                    intent.putExtra("ProductID",productid.toString());
                    intent.putExtra("BidderID",bidderid.toString());
                    v.getContext().startActivity(intent);
                    ((MyOffersPage)v.getContext()).finish();

                }

                if (verificationstatustemp.equals(true) && reviewstatustemp.equals(false) && editofferstatustemp.equals(true) && finalacceptstatustemp.equals(true) && paydeliverystatustemp.equals(true)){

                    Intent intent = new Intent(v.getContext(),SellerPaymentAwaiting.class);
                    intent.putExtra("SellerID",sellerid.toString());
                    intent.putExtra("ProductID",productid.toString());
                    intent.putExtra("BidderID",bidderid.toString());
                    v.getContext().startActivity(intent);
                    ((MyOffersPage)v.getContext()).finish();

                }

            }
        });

        myViewHolder.rejectbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference productRef2 = productRef.document(sellerid).collection("Sell").document(productid).collection("Offers").document(bidderid);
                productRef2.delete();
                DocumentReference productRef3 = productRef.document(bidderid).collection("Cart").document(productid);
                productRef3.update("OfferStatus", "Rejected");
                ((MyOffersPage)v.getContext()).recreate();

            }
        });


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_offers_cardview,parent,false);

        return new MyViewHolder(v);
    }

     class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView callbutton;
        TextView biddername, offer;
        Button acceptbutton, rejectbutton, dummybutton;
        String number;


            public MyViewHolder(@NonNull View itemView) {
                super(itemView);


                callbutton = (ImageView) itemView.findViewById(R.id.call_button);
                biddername = (TextView) itemView.findViewById(R.id.bidder_name);
                offer = (TextView) itemView.findViewById(R.id.offer);
                acceptbutton = (Button) itemView.findViewById(R.id.accept_button);
                rejectbutton = (Button) itemView.findViewById(R.id.reject_button);
                dummybutton = (Button) itemView.findViewById(R.id.dummy_button);


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