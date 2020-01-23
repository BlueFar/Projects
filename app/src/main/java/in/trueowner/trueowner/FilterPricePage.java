package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class FilterPricePage extends AppCompatActivity {

    private TextView allprices, under5, between5to10, between10to20, over20;
    private ImageView backbutton;
    private Intent intent1;
    SharedPreferences mSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_price_page);

        backbutton = (ImageView) findViewById(R.id.price_back_button);
        allprices = (TextView) findViewById(R.id.price_filter_all_prices);
        under5 = (TextView) findViewById(R.id.price_filter_under_5);
        between5to10 = (TextView) findViewById(R.id.price_filter_5_10);
        between10to20 = (TextView) findViewById(R.id.price_filter_10_20);
        over20 = (TextView) findViewById(R.id.price_filter_over_20);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        allprices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent1 = new Intent(FilterPricePage.this, FilterPage.class);
                mSharedPref = getSharedPreferences("TemporaryFilterSettings", MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putLong("TemporaryPrice1", 0);
                editor.putLong("TemporaryPrice2", 100000);
                editor.apply();
                startActivity(intent1);
                finish();
            }
        });

        under5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent1 = new Intent(FilterPricePage.this, FilterPage.class);
                mSharedPref = getSharedPreferences("TemporaryFilterSettings", MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putLong("TemporaryPrice1", 0);
                editor.putLong("TemporaryPrice2", 5000);
                editor.apply();
                startActivity(intent1);
                finish();
            }
        });

        between5to10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent1 = new Intent(FilterPricePage.this, FilterPage.class);
                mSharedPref = getSharedPreferences("TemporaryFilterSettings", MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putLong("TemporaryPrice1", 5000);
                editor.putLong("TemporaryPrice2", 10000);
                editor.apply();
                startActivity(intent1);
                finish();
            }
        });

        between10to20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent1 = new Intent(FilterPricePage.this, FilterPage.class);
                mSharedPref = getSharedPreferences("TemporaryFilterSettings", MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putLong("TemporaryPrice1", 10000);
                editor.putLong("TemporaryPrice2", 20000);
                editor.apply();
                startActivity(intent1);
                finish();
            }
        });

        over20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent1 = new Intent(FilterPricePage.this, FilterPage.class);
                mSharedPref = getSharedPreferences("TemporaryFilterSettings", MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putLong("TemporaryPrice1", 20000);
                editor.putLong("TemporaryPrice2", 100000);
                editor.apply();
                startActivity(intent1);
                finish();
            }
        });

    }
}
