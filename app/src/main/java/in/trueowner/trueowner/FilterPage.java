package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class FilterPage extends AppCompatActivity {

    private ImageView backbutton;
    private LinearLayout pricebutton1, pricebutton2, locationbutton1,locationbutton2;
    private Button clearallbutton, applybutton;
    private TextView locationtext, pricefrom, priceto;;
    RadioGroup radiogroup;
    RadioButton radiobutton;
    private Intent intent1;
    SharedPreferences mSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_page);

        backbutton = (ImageView) findViewById(R.id.filter_back_button);
        pricefrom = (TextView) findViewById(R.id.filter_price_from);
        priceto = (TextView) findViewById(R.id.filter_price_to);
        pricebutton1 = (LinearLayout) findViewById(R.id.filter_price_box_1);
        pricebutton2 = (LinearLayout) findViewById(R.id.filter_price_box_2);
        radiogroup = (RadioGroup) findViewById(R.id.filter_radio_group);
        locationbutton1 = (LinearLayout) findViewById(R.id.filter_location_box_1);
        locationbutton2 = (LinearLayout) findViewById(R.id.filter_location_box_2);
        locationtext = (TextView) findViewById(R.id.filter_location_text);
        applybutton = (Button) findViewById(R.id.filter_apply_button);
        clearallbutton = (Button) findViewById(R.id.filter_clearall_button);
        radiobutton = (RadioButton) findViewById(R.id.filter_radio_1);

        pricebutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent1 = new Intent(FilterPage.this, FilterPricePage.class);
                startActivity(intent1);

            }
        });

        pricebutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent1 = new Intent(FilterPage.this, FilterPricePage.class);
                startActivity(intent1);

            }
        });

        mSharedPref = getSharedPreferences("TemporaryFilterSettings",MODE_PRIVATE);
        long mprice1 = mSharedPref.getLong("TemporaryPrice1", 0);
        long mprice2 = mSharedPref.getLong("TemporaryPrice2", 100000);
        String mlocationstate = mSharedPref.getString("TemporaryState", "All States");
        String mlocationcity = mSharedPref.getString("TemporaryCity", "All City");

        if (mlocationstate == "All States"){

            locationtext.setText(mlocationstate);

        }

        else {

            locationtext.setText(mlocationstate+", "+mlocationcity);

        }

        pricefrom.setText(String.valueOf(mprice1));
        priceto.setText(String.valueOf(mprice2));

// "₹ "+

        long price1 = Long.parseLong(pricefrom.getText().toString());
        long price2 = Long.parseLong(priceto.getText().toString());


        pricefrom.setText("₹ "+String.valueOf(mprice1));
        priceto.setText("₹ "+String.valueOf(mprice2));

        mSharedPref = getSharedPreferences("TemporaryFilterSettings",MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putLong("TemporaryPrice1", price1);
        editor.putLong("TemporaryPrice2", price2);
        editor.apply();

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSharedPref = getSharedPreferences("TemporaryFilterSettings",MODE_PRIVATE);
                long tempprice1 = mSharedPref.getLong("applyPrice1", 0);
                long tempprice2 = mSharedPref.getLong("applyPrice2", 100000);
                long tempcondition1 = mSharedPref.getLong("applyCondition1", 0);
                long tempcondition2 = mSharedPref.getLong("applyCondition2", 120);
                String tempstate = mSharedPref.getString("applyState", "All States");
                String tempcity = mSharedPref.getString("applyCity", "All City");

                mSharedPref = getSharedPreferences("TemporaryFilterSettings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putLong("TemporaryPrice1", tempprice1);
                editor.putLong("TemporaryPrice2", tempprice2);
                editor.putLong("TemporaryCondition1", tempcondition1);
                editor.putLong("TemporaryCondition2", tempcondition2);
                editor.putString("TemporaryState", tempstate);
                editor.putString("TemporaryCity", tempcity);
                editor.apply();

                finish();

            }
        });


        locationbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent1 = new Intent(FilterPage.this, LocationFilterStatePage.class);
                startActivity(intent1);
            }
        });

        locationbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent1 = new Intent(FilterPage.this, LocationFilterStatePage.class);
                startActivity(intent1);
            }
        });

        applybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent1 = new Intent(FilterPage.this, Marketplace_Page.class);

                mSharedPref = getSharedPreferences("TemporaryFilterSettings",MODE_PRIVATE);
                long applyprice1 = mSharedPref.getLong("TemporaryPrice1", 0);
                long applyprice2 = mSharedPref.getLong("TemporaryPrice2", 100000);
                long applycondition1 = mSharedPref.getLong("TemporaryCondition1", 0);
                long applycondition2 = mSharedPref.getLong("TemporaryCondition2", 120);
                String applystate = mSharedPref.getString("TemporaryState", "All States");
                String applycity = mSharedPref.getString("TemporaryCity", "All Cities");

                mSharedPref = getSharedPreferences("TemporaryFilterSettings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putLong("applyPrice1", applyprice1);
                editor.putLong("applyPrice2", applyprice2);
                editor.putLong("applyCondition1", applycondition1);
                editor.putLong("applyCondition2", applycondition2);
                editor.putString("applyState", applystate);
                editor.putString("applyCity", applycity);
                editor.apply();

                startActivity(intent1);
                finish();
            }
        });

        clearallbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putLong("TemporaryPrice1", 0);
                editor.putLong("TemporaryPrice2", 100000);
                editor.putLong("TemporaryCondition1", 0);
                editor.putLong("TemporaryCondition2", 120);
                editor.putString("TemporaryState", "All States");
                editor.putString("TemporaryCity", "All Cities");
                editor.apply();

                mSharedPref = getSharedPreferences("TemporaryFilterSettings",MODE_PRIVATE);
                long mprice1 = mSharedPref.getLong("TemporaryPrice1", 0);
                long mprice2 = mSharedPref.getLong("TemporaryPrice2", 100000);
                String mlocationstate = mSharedPref.getString("TemporaryState", "All States");
                String mlocationcity = mSharedPref.getString("TemporaryCity", "All City");

                radiobutton.setChecked(true);
                pricefrom.setText("₹ "+String.valueOf(mprice1));
                priceto.setText("₹ "+String.valueOf(mprice2));
                locationtext.setText(mlocationstate);

            }
        });

    }

    public void checkButton(View v){

        int radioid = radiogroup.getCheckedRadioButtonId();

        switch (radioid){
            case 0:

                mSharedPref = getSharedPreferences("TemporaryFilterSettings",MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putLong("TemporaryCondition1", 0);
                editor.putLong("TemporaryCondition2", 120);
                editor.apply();

                break;

            case 1:

                mSharedPref = getSharedPreferences("TemporaryFilterSettings",MODE_PRIVATE);
                editor = mSharedPref.edit();
                editor.putLong("TemporaryCondition1", 0);
                editor.putLong("TemporaryCondition2", 1);
                editor.apply();

                break;

            case 2:

                mSharedPref = getSharedPreferences("TemporaryFilterSettings",MODE_PRIVATE);
                editor = mSharedPref.edit();
                editor.putLong("TemporaryCondition1", 1);
                editor.putLong("TemporaryCondition2", 12);
                editor.apply();

                break;

            case 3:

                mSharedPref = getSharedPreferences("TemporaryFilterSettings",MODE_PRIVATE);
                editor = mSharedPref.edit();
                editor.putLong("TemporaryCondition1", 12);
                editor.putLong("TemporaryCondition2", 24);
                editor.apply();

                break;

            case 4:

                mSharedPref = getSharedPreferences("TemporaryFilterSettings",MODE_PRIVATE);
                editor = mSharedPref.edit();
                editor.putLong("TemporaryCondition1", 24);
                editor.putLong("TemporaryCondition2", 120);
                editor.apply();

                break;

        }

    }

}
