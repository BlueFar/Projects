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
import com.google.firebase.firestore.DocumentSnapshot;

public class Admin_Seller_List_Adapter extends FirestoreRecyclerAdapter<Admin_Seller_List_Get_Set, Admin_Seller_List_Adapter.MyViewHolder> {
private OnItemClickListener listener;
    public Intent intent1;

    public Admin_Seller_List_Adapter(@NonNull FirestoreRecyclerOptions<Admin_Seller_List_Get_Set> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i, @NonNull final Admin_Seller_List_Get_Set admin_seller_list_get_set) {

        myViewHolder.sellername.setText(admin_seller_list_get_set.getSellerName());
        myViewHolder.imei1.setText(admin_seller_list_get_set.getImei1());


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_seller_list_cardview,parent,false);

        return new MyViewHolder(v);
    }

     class MyViewHolder extends RecyclerView.ViewHolder {

        TextView sellername, imei1;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                sellername = (TextView) itemView.findViewById(R.id.seller_name);
                imei1 = (TextView) itemView.findViewById(R.id.offer_price);

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