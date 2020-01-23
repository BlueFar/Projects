package in.trueowner.trueowner;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Marketplace_Adapter extends FirestoreRecyclerAdapter<Marketplace_Get_Set, Marketplace_Adapter.MyViewHolder> {
private OnItemClickListener listener;
    public Intent intent1;

    public Marketplace_Adapter(@NonNull FirestoreRecyclerOptions<Marketplace_Get_Set> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i, @NonNull Marketplace_Get_Set marketplace_get_set) {

         myViewHolder.tv_price.setText(String.valueOf(marketplace_get_set.getPrice()));
        myViewHolder.tv_description.setText(marketplace_get_set.getDescription());
        myViewHolder.tv_name.setText(marketplace_get_set.getName());
        myViewHolder.tv_condition.setText(String.valueOf(marketplace_get_set.getCondition()));
        myViewHolder.tv_productid.setText(marketplace_get_set.getProductID());
        myViewHolder.tv_ownername.setText(marketplace_get_set.getOwnerName());
        StringBuilder img1 = new StringBuilder("");
        img1.append(marketplace_get_set.getImage1());
        myViewHolder.image1.setText(img1.toString());
        String image1url =myViewHolder.image1.getText().toString();
        Picasso.get().load(image1url).into(myViewHolder.img_thumbnail);




    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.marketplace_cardview,parent,false);

        return new MyViewHolder(v);
    }

     class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_price,tv_description,tv_name,tv_condition,tv_productid,tv_ownername,image1;
        ImageView img_thumbnail;
        CardView cardview;



            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                tv_price = (TextView) itemView.findViewById(R.id.item_price_id);
                tv_description = (TextView) itemView.findViewById(R.id.item_description_id);
                tv_name = (TextView) itemView.findViewById(R.id.item_name_id);
                tv_condition = (TextView) itemView.findViewById(R.id.item_condition_id);
                tv_productid = (TextView) itemView.findViewById(R.id.item_product_id);
                tv_ownername = (TextView) itemView.findViewById(R.id.item_owner_name);
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