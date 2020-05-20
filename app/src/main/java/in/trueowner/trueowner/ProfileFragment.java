package in.trueowner.trueowner;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileFragment extends Fragment {

    private LinearLayout myproducts1, myproducts2, personaldetails1,personaldetails2, mywishlist1, mywishlist2, support1, support2, logout1, logout2;
    private TextView emailid,cartcount;
    ImageView cartbutton;
    FirebaseUser user;
    String userid;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.profile_fragment,container,false);

        emailid = (TextView) view.findViewById(R.id.profile_email);
        personaldetails1 = (LinearLayout) view.findViewById(R.id.personal_details_box_1);
        personaldetails2 = (LinearLayout) view.findViewById(R.id.personal_details_box_2);
        myproducts1 = (LinearLayout) view.findViewById(R.id.my_products_box_1);
        myproducts2 = (LinearLayout) view.findViewById(R.id.my_products_box_2);
        mywishlist1 = (LinearLayout) view.findViewById(R.id.my_wishlist_box_1);
        mywishlist2 = (LinearLayout) view.findViewById(R.id.my_wishlist_box_2);
        support1 = (LinearLayout) view.findViewById(R.id.support_box_1);
        support2 = (LinearLayout) view.findViewById(R.id.support_box_2);
        logout1 = (LinearLayout) view.findViewById(R.id.logout_box_1);
        logout2 = (LinearLayout) view.findViewById(R.id.logout_box_2);
        cartcount = (TextView)view.findViewById(R.id.profile_frag_cart_count);
        cartbutton = (ImageView)view.findViewById(R.id.profile_frag_cart_button);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        CollectionReference deref = db.collection("Users").document(userid).collection("Cart");
        deref.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {

                                cartcount.setText("0");

                            } else {

                                String count = String.valueOf(task.getResult().size());
                                cartcount.setText(count);


                            }

                        } else {

                            return;

                        }
                    }
                });




        emailid.setText("Email Address");  // user email address

        cartbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(getActivity(), CartPage.class);
                startActivity(intent1);

            }
        });

        personaldetails1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),PersonalDetailsPage.class);
                startActivity(intent1);
            }
        });

        personaldetails2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),PersonalDetailsPage.class);
                startActivity(intent1);
            }
        });

        myproducts1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),MyProductsPage.class);
                startActivity(intent1);
            }
        });

        myproducts2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),MyProductsPage.class);
                startActivity(intent1);
            }
        });

        mywishlist1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), WishlistPage.class);
                startActivity(intent1);
            }
        });

        mywishlist2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), WishlistPage.class);
                startActivity(intent1);
            }
        });

        support1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),SupportPage.class);
                startActivity(intent1);
            }
        });

        support2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),SupportPage.class);
                startActivity(intent1);
            }
        });

        logout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent intent1 = new Intent(getActivity(),LoginPage.class);
                startActivity(intent1);
            }
        });

        logout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent1 = new Intent(getActivity(),LoginPage.class);
                startActivity(intent1);
            }
        });


        return view;

    }

}
