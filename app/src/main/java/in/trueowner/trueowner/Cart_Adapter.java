package in.trueowner.trueowner;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class Cart_Adapter extends FirestoreRecyclerAdapter<Cart_Get_Set, Cart_Adapter.MyViewHolder> {
private OnItemClickListener listener;
    public Intent intent1;
    String sellernumber;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int flag =0;

    public Cart_Adapter(@NonNull FirestoreRecyclerOptions<Cart_Get_Set> options) {
        super(options);

    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i, @NonNull Cart_Get_Set cart_get_set) {

         myViewHolder.tv_price.setText(String.valueOf(cart_get_set.getMyOffer()));
        myViewHolder.tv_name.setText(cart_get_set.getProductName());
        final String productname = cart_get_set.getProductName();
        final long offeredprice = cart_get_set.getMyOffer();
        final String offeredpricetemp =String.valueOf(cart_get_set.getMyOffer());
        final long acceptableprice = cart_get_set.getAcceptablePrice();
        final long askedprice = cart_get_set.getAskedPrice();
        final String productid = cart_get_set.getProductID();
        final String sellerid = cart_get_set.getSellerID();
        final String bidderid = cart_get_set.getBidderID();
        final String sellername = cart_get_set.getSellerName();
        sellernumber = cart_get_set.getSellerNumber();
        final String sellercity = cart_get_set.getSellerCity();
        final String sellerstate = cart_get_set.getSellerState();
        final Boolean verificationstatustemp = cart_get_set.getVerificationStatus();
        final Boolean reviewstatustemp = cart_get_set.getReviewStatus();
        final Boolean editofferstatustemp = cart_get_set.getEditOfferStatus();
        final Boolean finalacceptstatustemp = cart_get_set.getFinalAcceptStatus();
        final Boolean paydeliverystatustemp = cart_get_set.getPayDeliveryStatus();
        String statustemp = cart_get_set.getStatus();
        String statusstring1 = "Pending";
        String statusstring2 = "Accepted";
        String statusstring3 = "Rejected";

        if (offeredprice>=acceptableprice){

            myViewHolder.callbutton.setVisibility(View.VISIBLE);

        }
        if (statustemp.equals(statusstring1))
        {
            myViewHolder.tv_status.setText("Status: Awaiting owner to accept the offer");

        }

        else if (statustemp.equals(statusstring2))
        {
            myViewHolder.tv_status.setText("Status: Offer Accepted");
            myViewHolder.callbutton.setVisibility(View.VISIBLE);
            myViewHolder.proceed.setVisibility(View.VISIBLE);

        }

        else if (statustemp.equals(statusstring3))
        {
            myViewHolder.tv_status.setText("Status: Offer Rejected");

        }

        StringBuilder img1 = new StringBuilder("");
        img1.append(cart_get_set.getImage1());
        myViewHolder.image1.setText(img1.toString());
        String image1url =myViewHolder.image1.getText().toString();
        Picasso.get().load(image1url).into(myViewHolder.img_thumbnail);

        final String imagesmallurl = image1url;
        long offer = cart_get_set.getMyOffer();
        long askedoffer = cart_get_set.getAskedPrice();

        if (offer>=askedoffer)
        {

            myViewHolder.callbutton.setVisibility(View.VISIBLE);

        }

        myViewHolder.deleteitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                DocumentReference dbref = db.collection("Users").document(bidderid).collection("Cart").document(productid);
                dbref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        flag=1;

                    }
                });

                if (flag ==1){

                    DocumentReference dbre = db.collection("Users").document(sellerid).collection("Sell").document(productid).collection("Offers").document(bidderid);
                    dbre.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            flag = 2;

                        }
                    });

                }

                if (flag==2){

                    Toast.makeText(v.getContext(),"Bid Cancelled",Toast.LENGTH_SHORT).show();
                    ((CartPage)v.getContext()).recreate();

                }

            }
        });

        myViewHolder.callbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((CartPage)v.getContext()).makePhoneCall(sellernumber);

            }
        });

        myViewHolder.updateoffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ((CartPage)v.getContext()).ShowPopup2(v,productid,bidderid,offeredpricetemp,askedprice,sellerid, sellername, sellernumber, sellercity, sellerstate, productname);
                ((CartPage)v.getContext()).recreate();
            }
        });

        myViewHolder.proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (verificationstatustemp.equals(false) && reviewstatustemp.equals(false) && editofferstatustemp.equals(false) && finalacceptstatustemp.equals(false) && paydeliverystatustemp.equals(false)){

                    Intent intent = new Intent(v.getContext(),BuyerAwaiting.class);
                    intent.putExtra("SellerID",sellerid.toString());
                    intent.putExtra("ProductID",productid.toString());
                    intent.putExtra("BidderID",bidderid.toString());
                    v.getContext().startActivity(intent);

                }

                if (verificationstatustemp.equals(true) && reviewstatustemp.equals(false) && editofferstatustemp.equals(false) && finalacceptstatustemp.equals(false) && paydeliverystatustemp.equals(false)){

                    Intent intent = new Intent(v.getContext(),BuyerVerificationReviewPage.class);
                    intent.putExtra("SellerID",sellerid.toString());
                    intent.putExtra("ProductID",productid.toString());
                    intent.putExtra("BidderID",bidderid.toString());
                    v.getContext().startActivity(intent);

                }

                if (verificationstatustemp.equals(true) && reviewstatustemp.equals(true) && editofferstatustemp.equals(false) && finalacceptstatustemp.equals(true) && paydeliverystatustemp.equals(false)){

                    Intent intent = new Intent(v.getContext(),BuyerFinalConformation.class);
                    intent.putExtra("SellerID",sellerid.toString());
                    intent.putExtra("ProductID",productid.toString());
                    intent.putExtra("BidderID",bidderid.toString());
                    v.getContext().startActivity(intent);

                }

                if (verificationstatustemp.equals(true) && reviewstatustemp.equals(false) && editofferstatustemp.equals(true) && finalacceptstatustemp.equals(false) && paydeliverystatustemp.equals(false)){

                    Intent intent = new Intent(v.getContext(),BuyerFinalConformation.class);
                    intent.putExtra("SellerID",sellerid.toString());
                    intent.putExtra("ProductID",productid.toString());
                    intent.putExtra("BidderID",bidderid.toString());
                    v.getContext().startActivity(intent);

                }

                if (verificationstatustemp.equals(true) && reviewstatustemp.equals(false) && editofferstatustemp.equals(true) && finalacceptstatustemp.equals(true) && paydeliverystatustemp.equals(false)){

                    Intent intent = new Intent(v.getContext(),BuyerFinalConformation.class);
                    intent.putExtra("SellerID",sellerid.toString());
                    intent.putExtra("ProductID",productid.toString());
                    intent.putExtra("BidderID",bidderid.toString());
                    v.getContext().startActivity(intent);

                }

                if (verificationstatustemp.equals(true) && reviewstatustemp.equals(false) && editofferstatustemp.equals(true) && finalacceptstatustemp.equals(true) && paydeliverystatustemp.equals(true)){

                    Intent intent = new Intent(v.getContext(),BuyerFinalConformation.class);
                    intent.putExtra("SellerID",sellerid.toString());
                    intent.putExtra("ProductID",productid.toString());
                    intent.putExtra("BidderID",bidderid.toString());
                    v.getContext().startActivity(intent);

                }
                //((MyOffersPage)v.getContext()).finish();
            }
        });

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_cardview,parent,false);

        return new MyViewHolder(v);
    }

     class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_price,tv_description,tv_name, tv_status, tv_condition,tv_productid,tv_ownername,image1;
        ImageView img_thumbnail, deleteitem;
        CardView cardview;
        Button updateoffer, proceed;
        FloatingActionButton callbutton;






            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                tv_name = (TextView) itemView.findViewById(R.id.item_name_id);
                tv_price = (TextView) itemView.findViewById(R.id.item_price_id);
                tv_status = (TextView) itemView.findViewById(R.id.item_status_id);
                deleteitem = (ImageView) itemView.findViewById(R.id.delete_button);
                callbutton = (FloatingActionButton) itemView.findViewById(R.id.call_button);
                updateoffer = (Button) itemView.findViewById(R.id.Update_offer_button);
                proceed = (Button) itemView.findViewById(R.id.Proceed_button);
                tv_condition = (TextView) itemView.findViewById(R.id.item_condition_id);
                tv_productid = (TextView) itemView.findViewById(R.id.item_product_id);
                tv_ownername = (TextView) itemView.findViewById(R.id.item_owner_name);
                tv_description = (TextView) itemView.findViewById(R.id.item_description_id);
                img_thumbnail = (ImageView) itemView.findViewById(R.id.item_image1_id);
                image1 = (TextView) itemView.findViewById(R.id.item_image1);
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