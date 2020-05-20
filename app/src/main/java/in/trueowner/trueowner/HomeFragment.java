package in.trueowner.trueowner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private ImageView optionOneLogo, optionTwoLogo, optionthreeLogo, optionOneArrow, optionTwoArrow, optionThreeArrow, cartbutton;
    private TextView optionOneText, optionTwoText, optionThreeText, cartcount;
    private ViewPager2 viewPager2;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    String  userid;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        cartbutton = (ImageView) view.findViewById(R.id.home_cart_button);
        optionOneLogo = (ImageView) view.findViewById(R.id.option_one_logo);
        optionOneText = (TextView) view.findViewById(R.id.option_one_text);
        optionOneArrow = (ImageView) view.findViewById(R.id.option_one_arrow);
        optionTwoLogo = (ImageView) view.findViewById(R.id.option_two_logo);
        optionTwoText = (TextView) view.findViewById(R.id.option_two_text);
        optionTwoArrow = (ImageView) view.findViewById(R.id.option_two_arrow);
        optionthreeLogo = (ImageView) view.findViewById(R.id.option_three_logo);
        optionThreeText = (TextView) view.findViewById(R.id.option_three_text);
        optionThreeArrow = (ImageView) view.findViewById(R.id.option_three_arrow);
        cartcount = (TextView) view.findViewById(R.id.home_cart_count);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {

            userid = currentUser.getUid();

        CollectionReference dref = db.collection("Users").document(userid).collection("Cart");
        dref.get()
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

        viewPager2 = view.findViewById(R.id.viewPagerImageSlider);

        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.images));
        sliderItems.add(new SliderItem(R.drawable.images));
        sliderItems.add(new SliderItem(R.drawable.images));
        sliderItems.add(new SliderItem(R.drawable.images));
        sliderItems.add(new SliderItem(R.drawable.images));
        sliderItems.add(new SliderItem(R.drawable.images));
        sliderItems.add(new SliderItem(R.drawable.images));
        sliderItems.add(new SliderItem(R.drawable.images));

        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_IF_CONTENT_SCROLLS);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(0));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                // float r = 1 - Math.abs(position);
                // page.setScaleY(0.9f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        cartbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), CartPage.class);
                startActivity(intent1);
            }
        });

        optionOneLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), RegisterDevicePage3.class);
                startActivity(intent1);
            }
        });

        optionOneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), RegisterDevicePage3.class);
                startActivity(intent1);
            }
        });


        optionOneArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), RegisterDevicePage3.class);
                startActivity(intent1);
            }
        });

        optionTwoLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), Marketplace_Page.class);
                startActivity(intent1);
            }
        });

        optionTwoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), Marketplace_Page.class);
                startActivity(intent1);
            }
        });


        optionTwoArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), Marketplace_Page.class);
                startActivity(intent1);
            }
        });

        optionthreeLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), TrackDevicePage.class);
                startActivity(intent1);
            }
        });

        optionThreeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), TrackDevicePage.class);
                startActivity(intent1);
            }
        });


        optionThreeArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), TrackDevicePage.class);
                startActivity(intent1);
            }
        });

        } else {

            Toast.makeText(getActivity(), "no user", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), LoginPage.class);
            startActivity(intent);

        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {

            userid = currentUser.getUid();

        } else {

        Toast.makeText(getActivity(), "no user", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), LoginPage.class);
        startActivity(intent);

    }

    }
}
