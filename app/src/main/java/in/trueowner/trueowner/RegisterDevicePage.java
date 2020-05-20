package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RegisterDevicePage extends AppCompatActivity {

    ImageView backbutton;
    EditText brand, model, imei1, imei2, serialno;
    Button submitbutton;
    String brandtemp, modeltemp, imei1temp, imei2temp, serialnotemp,picbilltemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_device_page);

        backbutton = (ImageView) findViewById(R.id.register_back_button);
        brand = (EditText) findViewById(R.id.register_brand);
        model = (EditText) findViewById(R.id.register_model);
        imei1 = (EditText) findViewById(R.id.register_imei_1);
        imei2 = (EditText) findViewById(R.id.register_imei_2);
        serialno = (EditText) findViewById(R.id.register_serial_no);
        submitbutton = (Button) findViewById(R.id.register_submit_button);

        Intent intent = getIntent();
        picbilltemp = intent.getExtras().getString("PicBill");

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register();


            }
        });
    }

    public void register(){

        brandtemp = brand.getText().toString().trim();
        modeltemp = model.getText().toString().trim();
        imei1temp = imei1.getText().toString().trim();
        imei2temp = imei2.getText().toString().trim();
        serialnotemp = serialno.getText().toString().trim();

            if(!validate()){



            }

            else{

                Intent intent = new Intent(RegisterDevicePage.this, RegisterDevicePage2.class);
                intent.putExtra("Brand",brandtemp);
                intent.putExtra("Model",modeltemp);
                intent.putExtra("IMEI1",imei1temp);
                intent.putExtra("IMEI2",imei2temp);
                intent.putExtra("SerialNo",serialnotemp);
                intent.putExtra("PicBill", picbilltemp);
                startActivity(intent);

            }

    }

    public Boolean validate(){

        Boolean valid = true;

        if (brandtemp.isEmpty()){

            brand.setError("Please enter brand name");

            valid = false;

        }

        if (modeltemp.isEmpty()){

            model.setError("Please enter brand name");

            valid = false;

        }
        if (imei1temp.isEmpty() || imei1temp.length()<15 || imei1temp.length()>15){

            imei1.setError("Please enter a valid IMEI 1 number");

            valid = false;

        }

        if (imei2temp.isEmpty()){



        }

           else if(imei2temp.length()>0){

               if(imei2temp.length()<15 || imei2temp.length()>15){

                   imei2.setError("Please enter a valid IMEI 2 number");

                   valid = false;

               }



            }


        if (serialnotemp.isEmpty()){



        }

        else if(serialnotemp.length()>0){

            if(serialnotemp.length()<16 || serialnotemp.length()>16){

                serialno.setError("Please enter a valid serial number");

                valid = false;

            }



        }



        return valid;
    }

}
