package in.trueowner.trueowner;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class Cart_Adapter extends FirestoreRecyclerAdapter<Cart_Get_Set, Cart_Adapter.MyViewHolder> {
private OnItemClickListener listener;
    public Intent intent1;

    public Cart_Adapter(@NonNull FirestoreRecyclerOptions<Cart_Get_Set> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i, @NonNull Cart_Get_Set cart_get_set) {

         myViewHolder.tv_price.setText(String.valueOf(cart_get_set.getPrice()));
        myViewHolder.tv_name.setText(cart_get_set.getName());
        String statustemp = cart_get_set.getStatus();
        String statusstring1 = "Pending";
        String statusstring2 = "Accepted";
        String statusstring3 = "Rejected";
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

        myViewHolder.tv_description.setText(cart_get_set.getDescription());
        myViewHolder.tv_condition.setText(String.valueOf(cart_get_set.getCondition()));
        myViewHolder.tv_productid.setText(cart_get_set.getProductID());
        myViewHolder.tv_ownername.setText(cart_get_set.getOwnerName());
        StringBuilder img1 = new StringBuilder("");
        img1.append(cart_get_set.getImage1());
        myViewHolder.image1.setText(img1.toString());
        String image1url =myViewHolder.image1.getText().toString();
        Picasso.get().load(image1url).into(myViewHolder.img_thumbnail);

        long offer = cart_get_set.getMyOffer();
        long askedoffer = cart_get_set.getAskedPrice();

        if (offer>=askedoffer)
        {

            myViewHolder.callbutton.setVisibility(View.VISIBLE);

        }
        myViewHolder.callbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        myViewHolder.updateoffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        myViewHolder.proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        ImageView img_thumbnail;
        CardView cardview;
        Button callbutton, updateoffer, proceed;





            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                tv_name = (TextView) itemView.findViewById(R.id.item_name_id);
                tv_price = (TextView) itemView.findViewById(R.id.item_price_id);
                tv_status = (TextView) itemView.findViewById(R.id.item_status_id);
                callbutton = (Button) itemView.findViewById(R.id.call_button);
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