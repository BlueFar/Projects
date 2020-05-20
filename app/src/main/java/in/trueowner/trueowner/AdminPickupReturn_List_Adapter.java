package in.trueowner.trueowner;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class AdminPickupReturn_List_Adapter extends FirestoreRecyclerAdapter<AdminPickupReturn_List_Get_Set, AdminPickupReturn_List_Adapter.MyViewHolder> {
private OnItemClickListener listener;
    public Intent intent1;

    public AdminPickupReturn_List_Adapter(@NonNull FirestoreRecyclerOptions<AdminPickupReturn_List_Get_Set> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i, @NonNull final AdminPickupReturn_List_Get_Set adminPickupReturn_list_get_set) {

        myViewHolder.productname.setText(adminPickupReturn_list_get_set.getProductName());
        myViewHolder.imei1.setText(adminPickupReturn_list_get_set.getImei1());


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_pickup_return_cardview,parent,false);

        return new MyViewHolder(v);
    }

     class MyViewHolder extends RecyclerView.ViewHolder {

        TextView productname, imei1;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                productname = (TextView) itemView.findViewById(R.id.admin_pickup_return_product_name);
                imei1 = (TextView) itemView.findViewById(R.id.admin_pickup_return_imei1);

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