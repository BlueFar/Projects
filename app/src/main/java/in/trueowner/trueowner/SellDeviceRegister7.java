package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SellDeviceRegister7 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView backbutton;
    EditText condition, price, negotiableprice, description;
    Button submitbutton;
    int conditiontempint;
    String productname,ownername,ownernumber,userid,productid,conditiontemp,pricetemp,negotiablepricetemp,descriptiontemp, frontpictemp, backpictemp, leftpictemp, rightpictemp, bottompictemp, toppictemp, statetemp, citytemp, city[] = null;
   long conditiontemp1,pricetemp1, negotiablepricetemp1;
    Spinner statespinner, cityspinner;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_device_register7);
        backbutton = (ImageView) findViewById(R.id.sell_register_back_button7);
        condition = (EditText) findViewById(R.id.sell_condition);
        price = (EditText) findViewById(R.id.sell_price);
        negotiableprice = (EditText) findViewById(R.id.sell_negotiable_price);
        description = (EditText) findViewById(R.id.sell_description);
        submitbutton = (Button) findViewById(R.id.sell_register_submit_button);
        statespinner = (Spinner) findViewById(R.id.spinner_state);
        cityspinner = (Spinner) findViewById(R.id.spinner_city);

        Intent intent = getIntent();
        frontpictemp = intent.getExtras().getString("FrontPic");
        backpictemp = intent.getExtras().getString("BackPic");
        leftpictemp = intent.getExtras().getString("LeftPic");
        rightpictemp = intent.getExtras().getString("RightPic");
        bottompictemp = intent.getExtras().getString("BottomPic");
        toppictemp = intent.getExtras().getString("TopPic");
        productid = intent.getExtras().getString("ProductID");

        DocumentReference dref = db.collection("Users").document(userid).collection("RegisterProducts").document(productid);
        DocumentReference dref1 = db.collection("Users").document(userid).collection("Details").document("PersonalDetails");

        dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()){

                productname =  documentSnapshot.getString("ProductName");

                }

            }
        });

        dref1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()){

                   ownername =  documentSnapshot.getString("FullName");
                   ownernumber = documentSnapshot.getString("MobileNumber");

                }

            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register(userid, productid, productname, ownername, ownernumber, frontpictemp, backpictemp, leftpictemp, rightpictemp, bottompictemp, toppictemp);


            }
        });

        ArrayAdapter<CharSequence> adapterstate = ArrayAdapter.createFromResource(this, R.array.statelist, android.R.layout.simple_spinner_item);
        adapterstate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statespinner.setAdapter(adapterstate);

        statespinner.setOnItemSelectedListener(this);


    }

    public void register(String userid1, String productid1, String productname1, String ownername1, String ownernumber1 ,String frontpictemp1, String backpictemp1, String leftpictemp1, String rightpictemp1, String bottompictemp1, String toppictemp1){

        conditiontemp = condition.getText().toString().trim();
        conditiontemp1 = Long.valueOf(condition.getText().toString());
        conditiontempint = Integer.parseInt(condition.getText().toString());
        pricetemp =price.getText().toString().trim();
        pricetemp1 = Long.valueOf(price.getText().toString());
        negotiablepricetemp = negotiableprice.getText().toString().trim();
        negotiablepricetemp1 = Long.valueOf(negotiableprice.getText().toString());
        descriptiontemp = description.getText().toString().trim();
        statetemp = statespinner.getSelectedItem().toString().trim();
        citytemp = cityspinner.getSelectedItem().toString().trim();

        if(!validate()){



        }

        else{


            Intent intent = new Intent(SellDeviceRegister7.this, SellSuccessPage.class);
          /*  intent.putExtra("OwnerState",statetemp);
            intent.putExtra("OwnerCity",citytemp);
            intent.putExtra("Description",descriptiontemp);
            intent.putExtra("AcceptablePrice",negotiablepricetemp1);
            intent.putExtra("AskedPrice",pricetemp1);
            intent.putExtra("Condition",conditiontemp1);
            intent.putExtra("ProductID",productid1);
            intent.putExtra("ProductName",productname1);
            intent.putExtra("OwnerName",ownername1);
            intent.putExtra("OwnerNumber",ownernumber1);
            intent.putExtra("Image1", frontpictemp1);
             intent.putExtra("Image2", backpictemp1);
             intent.putExtra("Image3", leftpictemp1);
             intent.putExtra("Image4", rightpictemp1);
             intent.putExtra("Image5", bottompictemp1);
             intent.putExtra("Image6", toppictemp1);
             intent.putExtra("OwnerID", userid1); */
            startActivity(intent);

        }

    }

    public Boolean validate(){

        Boolean valid = true;

        if (conditiontemp.isEmpty()){

            condition.setError("Please enter the condition of the device");

            valid = false;

        }

        else if(conditiontempint > 120){

            condition.setError("Cannot sell a device older then 120 months");

            valid = false;

        }

        if (pricetemp.isEmpty()){

            price.setError("Please enter a price");

            valid = false;

        }

        if (negotiablepricetemp.isEmpty()){

            negotiableprice.setError("Please enter a negotiable price");

            valid = false;

        }

        if (descriptiontemp.isEmpty()){

            description.setError("Please enter description of the device");

            valid = false;

        }

        return valid;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (position==0){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==1){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==2){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==3){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==4){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==5){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==6){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==7){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==8){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==9){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==10){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==11){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==12){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==13){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==14){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==15){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==16){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==17){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==18){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==19){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==20){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==21){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==22){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==23){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==24){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==25){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==26){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==27){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        if (position==28){

            city = new String[]{"Mangalore", "Bangalore"};

        }

        ArrayAdapter<String> adaptercity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, city);
        adaptercity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cityspinner.setAdapter(adaptercity);


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

       /* */


}
