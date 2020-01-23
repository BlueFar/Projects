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

public class MyProducts_Adapter extends FirestoreRecyclerAdapter<MyProducts_Get_Set, MyProducts_Adapter.MyViewHolder> {
private OnItemClickListener listener;
    public Intent intent1;

    public MyProducts_Adapter(@NonNull FirestoreRecyclerOptions<MyProducts_Get_Set> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i, @NonNull MyProducts_Get_Set myProducts_get_set) {

        long purchasedprice = myProducts_get_set.getPurchasedPrice();
      //  long bbvalue = purchasedprice

      //   myViewHolder.tv_bbvalue.setText(String.valueOf(myProducts_get_set.getPrice()));
        myViewHolder.tv_name.setText(myProducts_get_set.getName());
        myViewHolder.tv_productid.setText(myProducts_get_set.getProductID());

        final Boolean check2 = myProducts_get_set.getVerificationStatus();

        if (check2){

            myViewHolder.tv_verificationstatus.setText("Verification status: Accepted");

            Boolean check = myProducts_get_set.getSale();


            if (check) {

                myViewHolder.sell.setText("Offers");

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
            public void onClick(View v) {

            if (check2){

                Intent intent = new Intent (v.getContext(), CartPage.class);
                v.getContext().startActivity(intent);
            }

            else {

                Intent intent = new Intent (v.getContext(), CartPage.class);
                v.getContext().startActivity(intent);

            }

            }
        });


        myViewHolder.lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



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
         Button sell, lost;



            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                tv_name = (TextView) itemView.findViewById(R.id.item_name_id);
                tv_bbvalue = (TextView) itemView.findViewById(R.id.item_bb_value_id);
                tv_verificationstatus = (TextView) itemView.findViewById(R.id.verification_status_id);
                tv_productid = (TextView) itemView.findViewById(R.id.item_product_id);
                sell = (Button) itemView.findViewById(R.id.sell_or_offers_button);
                lost = (Button) itemView.findViewById(R.id.report_lost_button);
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