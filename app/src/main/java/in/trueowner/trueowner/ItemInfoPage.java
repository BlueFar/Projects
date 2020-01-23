package in.trueowner.trueowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ItemInfoPage extends AppCompatActivity {

    private ImageView imageview1,imageview2,imageview3,imageview4, backbutton;
    private TextView image1,image2,image3,image4,price,description,name,location,condition,ownername;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    Dialog makeofferpopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_info_page);

        makeofferpopup = new Dialog(this);

        Intent intent = getIntent();
        long Price = intent.getExtras().getLong("Price");
        String Description = intent.getExtras().getString("Description");
        String Name = intent.getExtras().getString("Name");
        long Condition = intent.getExtras().getLong("Condition");
        String ProductID = intent.getExtras().getString("ProductID");
        String OwnerName = intent.getExtras().getString("Owner Name");
        String State = intent.getExtras().getString("State");
        String City = intent.getExtras().getString("City");

        backbutton = (ImageView) findViewById(R.id.item_info_back_button);
        price = (TextView) findViewById(R.id.item_info_product_price);
        description = (TextView) findViewById(R.id.item_info_product_description);
        name = (TextView) findViewById(R.id.item_info_product_name);
        location = (TextView) findViewById(R.id.item_info_product_location);
        condition = (TextView) findViewById(R.id.item_info_product_condition);
        ownername = (TextView) findViewById(R.id.item_info_owner_name);

        imageview1 = (ImageView) findViewById(R.id.item_info_image1);
        imageview2 = (ImageView) findViewById(R.id.item_info_image2);
        imageview3 = (ImageView) findViewById(R.id.item_info_image3);
        imageview4 = (ImageView) findViewById(R.id.item_info_image4);
        image1 = (TextView) findViewById(R.id.image1_text);
        image2 = (TextView) findViewById(R.id.image2_text);
        image3 = (TextView) findViewById(R.id.image3_text);
        image4 = (TextView) findViewById(R.id.image4_text);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        price.setText(String.valueOf(Price));
        description.setText(Description);
        name.setText(Name);
        location.setText(City+", "+State);
        condition.setText(String.valueOf(Condition)+" Month Old");
        ownername.setText(OwnerName);

        DocumentReference images = db.collection("Marketplace").document(ProductID);
        images.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {

                    DocumentSnapshot doc = task.getResult();
                    StringBuilder img1 = new StringBuilder("");
                   img1.append(doc.get("Image1"));
                    image1.setText(img1.toString());
                    String image1url = image1.getText().toString();
                    Picasso.get().load(image1url).into(imageview1);


                    StringBuilder img2 = new StringBuilder("");
                    img2.append(doc.get("Image2"));
                    image2.setText(img2.toString());
                    String image2url = image2.getText().toString();
                    Picasso.get().load(image2url).into(imageview2);


                   StringBuilder img3 = new StringBuilder("");
                   img3.append(doc.get("Image3"));
                    image3.setText(img3.toString());
                   String image3url = image3.getText().toString();
                    Picasso.get().load(image3url).into(imageview3);

                    StringBuilder img4 = new StringBuilder("");
                    img4.append(doc.get("Image4"));
                    image4.setText(img4.toString());
                    String image4url = image4.getText().toString();
                    Picasso.get().load(image4url).into(imageview4);

                }
            }
        }).
                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ItemInfoPage.this,"Failed",Toast.LENGTH_SHORT).show();
                    }
                });



    }

    public void ShowPopup(View v) {


        Intent intent = getIntent();
        ImageButton submitbutton;
        EditText editoffer;
        TextView exitbutton;
        Button askedpricebutton;
        makeofferpopup.setContentView(R.layout.item_info_offer_popup);
        exitbutton =(TextView) makeofferpopup.findViewById(R.id.item_info_popup_exit_button);
        editoffer = (EditText) makeofferpopup.findViewById(R.id.item_info_popup_enter_price_box);
        submitbutton = (ImageButton) makeofferpopup.findViewById(R.id.item_info_popup_price_submit_button);
        askedpricebutton = (Button) makeofferpopup.findViewById(R.id.item_info_popup_asking_price_button);
        String Price = intent.getExtras().getString("Price");
        askedpricebutton.setText(Price);

        askedpricebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        exitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeofferpopup.dismiss();
            }
        });
        makeofferpopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        makeofferpopup.show();
    }

}
