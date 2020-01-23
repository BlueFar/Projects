package in.trueowner.trueowner;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private LinearLayout myproducts1, myproducts2, personaldetails1,personaldetails2, mywishlist1, mywishlist2, support1, support2, logout1, logout2;
    private TextView emailid;

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


        emailid.setText("Email Address");  // user email address

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
                Intent intent1 = new Intent(getActivity(),LoginPage.class);
                startActivity(intent1);
            }
        });

        logout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),LoginPage.class);
                startActivity(intent1);
            }
        });


        return view;

    }
}
