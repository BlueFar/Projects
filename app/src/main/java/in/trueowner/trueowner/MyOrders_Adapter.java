package in.trueowner.trueowner;

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

public class MyOrders_Adapter extends FirestoreRecyclerAdapter<MyOrders_Get_Set, MyOrders_Adapter.MyViewHolder> {
private OnItemClickListener listener;
    public Intent intent1;

    public MyOrders_Adapter(@NonNull FirestoreRecyclerOptions<MyOrders_Get_Set> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i, @NonNull MyOrders_Get_Set myOrders_get_set) {

        myViewHolder.tv_productname.setText(myOrders_get_set.getProductName());
        myViewHolder.orderstatus.setText(myOrders_get_set.getOrderStatus());
        StringBuilder img1 = new StringBuilder("");
        img1.append(myOrders_get_set.getImage1());
        myViewHolder.image1.setText(img1.toString());
        String image1url =myViewHolder.image1.getText().toString();
        Picasso.get().load(image1url).into(myViewHolder.img_thumbnail);


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_orders_cardview,parent,false);

        return new MyViewHolder(v);
    }

     class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_productname, orderstatus, image1;
        ImageView img_thumbnail;
        CardView cardview;



            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                tv_productname = (TextView) itemView.findViewById(R.id.my_orders_product_name);
                orderstatus = (TextView) itemView.findViewById(R.id.my_orders_order_status);
                img_thumbnail = (ImageView) itemView.findViewById(R.id.my_orders_image1);
                image1 = (TextView) itemView.findViewById(R.id.my_orders_item_image1);
                cardview = (CardView) itemView.findViewById(R.id.cardview_my_orders);

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