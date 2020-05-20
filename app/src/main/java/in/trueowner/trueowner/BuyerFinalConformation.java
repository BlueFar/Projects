package in.trueowner.trueowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BuyerFinalConformation extends AppCompatActivity {

    ImageView backbutton, image1, image2;
    Button proceedbutton, contactseller;
    TextView text1,text2;
    Dialog addresspopup;
    String productid, sellerid, bidderid, mobno, sellercity, sellerstate;
    Boolean reviewstatus, editofferstatus, finalofferstatus;
    public static final int REQUEST_CALL = 9;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyer_final_conformation);

        Intent intent = getIntent();
        productid = intent.getExtras().getString("ProductID");
        bidderid = intent.getExtras().getString("BidderID");
        sellerid = intent.getExtras().getString("SellerID");
        backbutton = (ImageView) findViewById(R.id.buyer_final_conformation_back_button);
        image1 = (ImageView) findViewById(R.id.buyer_final_conformation_image1);
        image2 = (ImageView) findViewById(R.id.buyer_final_conformation_image2);
        proceedbutton = (Button) findViewById(R.id.buyer_final_conformation_proceed_button);
        contactseller = (Button) findViewById(R.id.buyer_final_conformation_contact_button);
        text1 = (TextView) findViewById(R.id.buyer_final_conformation_text1);
        text2 = (TextView) findViewById(R.id.buyer_final_conformation_text2);
        addresspopup = new Dialog(this);

        DocumentReference productRef =db.collection("Users").document(bidderid).collection("Cart").document(productid);
        productRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

               reviewstatus = documentSnapshot.getBoolean("ReviewStatus");
               editofferstatus = documentSnapshot.getBoolean("EditOfferStatus");
               finalofferstatus = documentSnapshot.getBoolean("FinalOfferStatus");
                mobno = documentSnapshot.getString("SellerNumber");
                sellerstate = documentSnapshot.getString("SellerState");
                sellercity = documentSnapshot.getString("SellerCity");

            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BuyerFinalConformation.this, CartPage.class);
                startActivity(intent);
                finish();

            }
        });


        if (reviewstatus.equals(true) && editofferstatus.equals(false) && finalofferstatus.equals(true)){
            proceedbutton.setVisibility(View.VISIBLE);
            contactseller.setVisibility(View.GONE);
            image1.setVisibility(View.GONE);
            image2.setVisibility(View.VISIBLE);
            text1.setText("Offer Accepted !");
            text2.setText("Please proceed to add your address and for payment");
        }
        else if (reviewstatus.equals(false) && editofferstatus.equals(true) && finalofferstatus.equals(true)){

            proceedbutton.setVisibility(View.VISIBLE);
            contactseller.setVisibility(View.GONE);
            image1.setVisibility(View.GONE);
            image2.setVisibility(View.VISIBLE);
            text1.setText("Offer Accepted !");
            text2.setText("Please proceed to add your address and for payment");

        }

        contactseller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makePhoneCall(mobno);

            }
        });

        proceedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowPopup(v, productid, sellerid, bidderid, sellercity, sellerstate) ;

            }
        });



    }

    private void makePhoneCall(String number){

        if(number.trim().length() < 10 ){

            Toast.makeText(BuyerFinalConformation.this, "Seller does not wish to share his number", Toast.LENGTH_SHORT).show();

        } else {

            if (ContextCompat.checkSelfPermission(BuyerFinalConformation.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(BuyerFinalConformation.this,
                        new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);

            } else {

                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

            }

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {

            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                makePhoneCall(mobno);

            } else {

                Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show();

            }

        }
    }

    public void ShowPopup(View v, String productid1, String sellerid1, String bidderid1, String sellercity1, String sellerstate1) {

        Button proceedbutton;
        final Spinner statespinner, cityspinner;
        final String[][] city = {null};
        final String deliverytotal;
        final long deliverytotal1;
        TextView deliveryfee;
        addresspopup.setContentView(R.layout.buyer_address_popup);

        proceedbutton =(Button) findViewById(R.id.proceed_button);
        statespinner = (Spinner) findViewById(R.id.buyer_address_spinner_state);
        cityspinner = (Spinner) findViewById(R.id.buyer_address_spinner_city);
        deliveryfee = (TextView) findViewById(R.id.buyer_address_delivery_fee);

        ArrayAdapter<CharSequence> adapterstate = ArrayAdapter.createFromResource(this, R.array.statelist, android.R.layout.simple_spinner_item);
        adapterstate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statespinner.setAdapter(adapterstate);

        statespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==1){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==2){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==3){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==4){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==5){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==6){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==7){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==8){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==9){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==10){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==11){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==12){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==13){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==14){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==15){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==16){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==17){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==18){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==19){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==20){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==21){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==22){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==23){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==24){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==25){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==26){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==27){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                if (position==28){

                    city[0] = new String[]{"Mangalore", "Bangalore"};

                }

                ArrayAdapter<String> adaptercity = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, city[0]);
                adaptercity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cityspinner.setAdapter(adaptercity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        deliverytotal = String.valueOf(5);
        deliveryfee.setText(deliverytotal);
        deliverytotal1 = Long.valueOf(deliverytotal);

        proceedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addresspopup.dismiss();
                Intent intent = new Intent(BuyerFinalConformation.this, BuyerFinalConformation.class);
                intent.putExtra("State", statespinner.getSelectedItem().toString());
                intent.putExtra("City", cityspinner.getSelectedItem().toString());
                intent.putExtra("DeliveryFee", deliverytotal1);
                startActivity(intent);

            }
        });

        addresspopup.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
        addresspopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addresspopup.show();
    }

}
