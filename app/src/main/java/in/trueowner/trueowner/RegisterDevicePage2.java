package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

import static java.util.Calendar.YEAR;

public class RegisterDevicePage2 extends AppCompatActivity {

    ImageView backbutton;
    EditText pricepurchase, mobileno;
    TextView datepurchase;
    FloatingActionButton submitbutton;
    String datepurchasetemp, pricepurchasetemp, mobilenotemp, brandtemp, modeltemp, imei1temp, imei2temp, serialnotemp;
    public long day1, month1, year1,price1;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_device_page2);
        backbutton = (ImageView) findViewById(R.id.register_back_button2);
        datepurchase = (TextView) findViewById(R.id.register_date_purchase);
        pricepurchase = (EditText) findViewById(R.id.register_price_purchase);
        mobileno = (EditText) findViewById(R.id.register_mobile_no);
        submitbutton = (FloatingActionButton) findViewById(R.id.register_submit_button_2);

        Intent intent = getIntent();
         brandtemp = intent.getExtras().getString("Brand");
         modeltemp = intent.getExtras().getString("Model");
         imei1temp = intent.getExtras().getString("IMEI1");
         imei2temp = intent.getExtras().getString("IMEI2");
         serialnotemp = intent.getExtras().getString("SerialNo");



        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        datepurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RegisterDevicePage2.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                month = month+1;

                String date = dayOfMonth + "/" + month + "/" + year;
                day1 = Long.valueOf(dayOfMonth);
                month1 = Long.valueOf(month);
                year1 = Long.valueOf(year);

                datepurchase.setText(date);

            }
        };

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register();


            }
        });
    }

    public void register(){

        datepurchasetemp = datepurchase.getText().toString().trim();
        pricepurchasetemp = pricepurchase.getText().toString().trim();
        mobilenotemp = mobileno.getText().toString().trim();


        if(!validate()){



        }

        else{


            price1 = Long.valueOf(pricepurchasetemp);
            Intent intent = new Intent(RegisterDevicePage2.this, RegisterDevicePage3.class);
            intent.putExtra("Brand",brandtemp);
            intent.putExtra("Model",modeltemp);
            intent.putExtra("IMEI1",imei1temp);
            intent.putExtra("IMEI2",imei2temp);
            intent.putExtra("SerialNo",serialnotemp);
            intent.putExtra("DayPurchase",day1);
            intent.putExtra("MonthPurchase",month1);
            intent.putExtra("YearPurchase",year1);
            intent.putExtra("PricePurchase",price1);
            intent.putExtra("MobileNo",mobilenotemp);
            startActivity(intent);

        }

    }

    public Boolean validate(){

        Boolean valid = true;

        if (datepurchasetemp.isEmpty()){

            datepurchase.setError("Please select a date");

            valid = false;

        }

        if (pricepurchasetemp.isEmpty()){

            pricepurchase.setError("Please enter the price purchased");

            valid = false;

        }
        if (mobilenotemp.isEmpty() || mobilenotemp.length()<10 ){

            mobileno.setError("Please enter a valid mobile number");

            valid = false;

        }

        return valid;
    }
}
