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

public class AdminRegisterList_Adapter extends FirestoreRecyclerAdapter<AdminRegisterList_Get_Set, AdminRegisterList_Adapter.MyViewHolder> {
private OnItemClickListener listener;
    public Intent intent1;

    public AdminRegisterList_Adapter(@NonNull FirestoreRecyclerOptions<AdminRegisterList_Get_Set> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i, @NonNull final AdminRegisterList_Get_Set adminRegisterList_get_set) {

        myViewHolder.fullname.setText(adminRegisterList_get_set.getFullName());
        myViewHolder.mobileno.setText(adminRegisterList_get_set.getMobileNumber());


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_register_list_cardview,parent,false);

        return new MyViewHolder(v);
    }

     class MyViewHolder extends RecyclerView.ViewHolder {

        TextView fullname, mobileno;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                fullname = (TextView) itemView.findViewById(R.id.register_list_name);
                mobileno = (TextView) itemView.findViewById(R.id.register_list_email);

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