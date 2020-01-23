package in.trueowner.trueowner;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private ImageView optionOneLogo, optionTwoLogo, optionthreeLogo, optionOneArrow, optionTwoArrow, optionThreeArrow;
    private TextView optionOneText, optionTwoText, optionThreeText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment,container,false);

        optionOneLogo = (ImageView) view.findViewById(R.id.option_one_logo);
        optionOneText = (TextView) view.findViewById(R.id.option_one_text);
        optionOneArrow = (ImageView) view.findViewById(R.id.option_one_arrow);
        optionTwoLogo = (ImageView) view.findViewById(R.id.option_two_logo);
        optionTwoText = (TextView) view.findViewById(R.id.option_two_text);
        optionTwoArrow = (ImageView) view.findViewById(R.id.option_two_arrow);
        optionthreeLogo = (ImageView) view.findViewById(R.id.option_three_logo);
        optionThreeText = (TextView) view.findViewById(R.id.option_three_text);
        optionThreeArrow = (ImageView) view.findViewById(R.id.option_three_arrow);

        optionOneLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),RegisterDevicePage.class);
                startActivity(intent1);
            }
        });

        optionOneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),RegisterDevicePage.class);
                startActivity(intent1);
            }
        });


        optionOneArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),RegisterDevicePage.class);
                startActivity(intent1);
            }
        });

        optionTwoLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),Marketplace_Page.class);
                startActivity(intent1);
            }
        });

        optionTwoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),Marketplace_Page.class);
                startActivity(intent1);
            }
        });


        optionTwoArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),Marketplace_Page.class);
                startActivity(intent1);
            }
        });

        optionthreeLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),RegisterDevicePage.class);
                startActivity(intent1);
            }
        });

        optionThreeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),Marketplace_Page.class);
                startActivity(intent1);
            }
        });


        optionThreeArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),RegisterDevicePage.class);
                startActivity(intent1);
            }
        });

        return view;

    }
}
