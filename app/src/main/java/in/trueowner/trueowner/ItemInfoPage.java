package in.trueowner.trueowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ItemInfoPage extends AppCompatActivity {

    private ImageView imageview1,imageview2,imageview3,imageview4,imageview5,imageview6,backbutton,cartbutton;
    private TextView image1,image2,image3,image4,image5,image6,price,description,name,location,condition,ownername,cartcount;
    Button whishlistbutton, makeofferfirstbutton;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    Dialog makeofferpopup;
    CharSequence asktemp;
    String productid, userid, sellerid, sellername,sellernumber,sellerstate,sellercity,imagefirst, productdescription, productname, previousaskedprice;
    long askedprice, acceptableprice, productcondition;
    int flag =0, flag1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_info_page);

        makeofferpopup = new Dialog(this);

        Intent intent = getIntent();
        productid = intent.getExtras().getString("ProductID");


        backbutton = (ImageView) findViewById(R.id.item_info_back_button);
        cartbutton = (ImageView) findViewById(R.id.logo_cart);
        price = (TextView) findViewById(R.id.item_info_product_price);
        description = (TextView) findViewById(R.id.item_info_product_description);
        name = (TextView) findViewById(R.id.item_info_product_name);
        location = (TextView) findViewById(R.id.item_info_product_location);
        condition = (TextView) findViewById(R.id.item_info_product_condition);
        ownername = (TextView) findViewById(R.id.item_info_owner_name);
        cartcount = (TextView) findViewById(R.id.item_info_cart_count);

        imageview1 = (ImageView) findViewById(R.id.item_info_image1);
        imageview2 = (ImageView) findViewById(R.id.item_info_image2);
        imageview3 = (ImageView) findViewById(R.id.item_info_image3);
        imageview4 = (ImageView) findViewById(R.id.item_info_image4);
        imageview5 = (ImageView) findViewById(R.id.item_info_image5);
        imageview6 = (ImageView) findViewById(R.id.item_info_image6);
        image1 = (TextView) findViewById(R.id.image1_text);
        image2 = (TextView) findViewById(R.id.image2_text);
        image3 = (TextView) findViewById(R.id.image3_text);
        image4 = (TextView) findViewById(R.id.image4_text);
        image5 = (TextView) findViewById(R.id.image5_text);
        image6 = (TextView) findViewById(R.id.image6_text);
        whishlistbutton = (Button) findViewById(R.id.item_info_whishlist_button);
        makeofferfirstbutton = (Button) findViewById(R.id.item_info_make_offer_button);

        CollectionReference deref = db.collection("Users").document(userid).collection("Cart");
        deref.get()
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

        DocumentReference docref = db.collection("Marketplace").document("Mobiles").collection("AllPrices").document("Conditions").collection("AllConditions").document(productid);
        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()){

                    sellerid = documentSnapshot.getString("OwnerID");
                    sellername = documentSnapshot.getString("OwnerName");
                    askedprice = documentSnapshot.getLong("AskedPrice");
                    acceptableprice = documentSnapshot.getLong("AcceptablePrice");
                    imagefirst = documentSnapshot.getString("Image1");
                    sellernumber = documentSnapshot.getString("OwnerNumber");
                    productdescription = documentSnapshot.getString("Description");
                    productname = documentSnapshot.getString("ProductName");
                    productcondition = documentSnapshot.getLong("Condition");
                    sellerstate = documentSnapshot.getString("OwnerState");
                    sellercity = documentSnapshot.getString("OwnerCity");



                }

            }
        });

        DocumentReference docref1 = db.collection("Users").document(userid).collection("Cart").document(productid);
        docref1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()){

                    makeofferfirstbutton.setText("UPDATE OFFER");

                }
                else {

                    makeofferfirstbutton.setText("Make OFFER");

                }

            }
        });

        DocumentReference docref2 = db.collection("Users").document(userid).collection("Favourites").document(productid);
        docref2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()){

                    whishlistbutton.setText("Liked");

                }
                else {

                    makeofferfirstbutton.setText("Like");

                }

            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cartbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemInfoPage.this, CartPage.class);
                startActivity(intent);
            }
        });


        price.setText(String.valueOf(askedprice));
        description.setText(productdescription);
        name.setText(productname);
        location.setText(sellercity+", "+sellerstate);
        condition.setText(String.valueOf(productcondition)+" Month Old");
        ownername.setText(sellername);

        DocumentReference images = db.collection("Marketplace").document("Mobiles").collection("AllPrices").document("Conditions").collection("AllConditions").document(productid);
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

                    StringBuilder img5 = new StringBuilder("");
                    img5.append(doc.get("Image5"));
                    image5.setText(img5.toString());
                    String image5url = image5.getText().toString();
                    Picasso.get().load(image5url).into(imageview5);

                    StringBuilder img6 = new StringBuilder("");
                    img6.append(doc.get("Image6"));
                    image6.setText(img6.toString());
                    String image6url = image6.getText().toString();
                    Picasso.get().load(image6url).into(imageview6);

                }
            }
        }).
                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ItemInfoPage.this,"Failed",Toast.LENGTH_SHORT).show();
                    }
                });

        whishlistbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DocumentReference docref3 = db.collection("Users").document(userid).collection("Favourites").document(productid);
                docref3.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()){

                            whishlistbutton.setText("Liked");

                        }
                        else {

                            Map<String, Object> wishlist = new HashMap<>();
                            wishlist.put("ProductID", productid);
                            wishlist.put("ProductName", productname);
                            wishlist.put("Image1", imagefirst);
                            wishlist.put("Condition", productcondition);
                            wishlist.put("AskedPrice", askedprice);
                            docref3.set(wishlist).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(ItemInfoPage.this,"Added to favourites",Toast.LENGTH_SHORT).show();

                                }
                            });
                            whishlistbutton.setText("Liked");

                        }

                    }
                });



            }
        });

        makeofferfirstbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                DocumentReference docref4 = db.collection("Users").document(userid).collection("Cart").document(productid);
                docref4.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()){

                           previousaskedprice =String.valueOf(documentSnapshot.getLong("OfferedPrice"));
                            ShowPopup2(v, productid, userid, previousaskedprice, askedprice, sellerid, sellername, sellernumber, sellercity, sellerstate, productname);

                        }
                        else {


                            ShowPopup(v, productid, userid, productname, productcondition, sellerid, sellername, sellernumber, sellerstate, sellercity, askedprice, acceptableprice, imagefirst);

                        }

                    }
                });


            }
        });



    }

    public void ShowPopup(View v, final String productidpopup, final String useridpopup, final String productname1, final long productcondition1, final String sellerid1, final String sellername1, final String sellernumber1, final String sellerstate1, final String sellercity1, final long askedprice1, final long acceptableprice1, final String imagefirst1) {


        ImageButton submitbutton;
        final EditText editoffer;
        TextView exitbutton;
        final Button askedpricebutton;
        makeofferpopup.setContentView(R.layout.item_info_offer_popup);
        exitbutton =(TextView) makeofferpopup.findViewById(R.id.item_info_popup_exit_button);
        editoffer = (EditText) makeofferpopup.findViewById(R.id.item_info_popup_enter_price_box);
        submitbutton = (ImageButton) makeofferpopup.findViewById(R.id.item_info_popup_price_submit_button);
        askedpricebutton = (Button) makeofferpopup.findViewById(R.id.item_info_popup_asking_price_button);
        String Price =String.valueOf(askedprice1);
        askedpricebutton.setText(Price);


        askedpricebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                asktemp = askedpricebutton.getText();
                editoffer.setText(asktemp);


            }
        });

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final long offeredprice = Long.valueOf(editoffer.getText().toString());

                Map<String, Object> cart = new HashMap<>();
                cart.put("BidderID", useridpopup);
                cart.put("ProductID", productidpopup);
                cart.put("SellerID", sellerid);
                cart.put("SellerName", sellername1);
                cart.put("ProductName", productname1);
                cart.put("AskedPrice", askedprice1);
                cart.put("AcceptablePrice", acceptableprice1);
                cart.put("Image1", imagefirst1);
                cart.put("OfferedPrice", offeredprice);
                cart.put("OfferStatus", "Pending");
                cart.put("SellerNumber", sellernumber1);
                cart.put("Condition", productcondition1);
                cart.put("VerificationStatus", false);
                cart.put("ReviewStatus", false);
                cart.put("EditOfferStatus", false);
                cart.put("FinalAcceptStatus", false);
                cart.put("PayDeliveryStatus", false);
                cart.put("Display", "");
                cart.put("TouchScreen", "");
                cart.put("ButtonAndFingerprint", "");
                cart.put("Wifi", "");
                cart.put("Bluetooth", "");
                cart.put("Camera", "");
                cart.put("Mic", "");
                cart.put("Speaker", "");
                cart.put("HeadphoneJack", "");
                cart.put("BuyerCity", "");
                cart.put("BuyerState", "");
                cart.put("DeliveryFee", 0);
                cart.put("SellerCity", sellercity1);
                cart.put("SellerState", sellerstate1);

                Map<String, Object> sell = new HashMap<>();
                sell.put("BidderID", useridpopup);
                sell.put("ProductID", productidpopup);
                sell.put("SellerID", sellerid);
                sell.put("SellerName", sellername1);
                sell.put("ProductName", productname1);
                sell.put("OfferedPrice", offeredprice);
                sell.put("OfferStatus", "Pending");
                sell.put("SellerNumber", sellernumber1);
                sell.put("VerificationStatus", false);
                sell.put("ReviewStatus", false);
                sell.put("EditOfferStatus", false);
                sell.put("FinalAcceptStatus", false);
                sell.put("PayDeliveryStatus", false);
                sell.put("BuyerCity", "");
                sell.put("BuyerState", "");
                sell.put("DeliveryFee", 0);
                sell.put("SellerCity", sellercity1);
                sell.put("SellerState", sellerstate1);
                sell.put("AdminID", "");


                    DocumentReference dbref = db.collection("Users").document(useridpopup).collection("Cart").document(productidpopup);
                    dbref.set(cart).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            flag =1;

                        }
                    });

                    if (flag ==1){

                        DocumentReference dbre = db.collection("Users").document(sellerid1).collection("Sell").document(productidpopup).collection("Offers").document(useridpopup);
                        dbre.set(sell).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                flag = 2;

                            }
                        });

                    }

                    if (flag==2){

                        makeofferpopup.dismiss();
                        makeofferfirstbutton.setText("EDIT OFFER");
                        Toast.makeText(ItemInfoPage.this,"Offer submited",Toast.LENGTH_SHORT).show();

                    }






            }
        });

        exitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeofferpopup.dismiss();
            }
        });

        makeofferpopup.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
        makeofferpopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        makeofferpopup.show();
    }

    public void ShowPopup2(View v, final String productidpopup, final String useridpopup, final String previousaskedprice1, final long askedprice1, final String sellerid1, final String sellername1, final String sellernumber1, final String sellercity1, final String sellerstate1, final String productname1) {


        Intent intent = getIntent();
        ImageButton submitbutton;
        final EditText editoffer;
        TextView exitbutton;
        final Button askedpricebutton;
        makeofferpopup.setContentView(R.layout.item_info_offer_popup);
        exitbutton =(TextView) makeofferpopup.findViewById(R.id.item_info_popup_exit_button);
        editoffer = (EditText) makeofferpopup.findViewById(R.id.item_info_popup_enter_price_box);
        submitbutton = (ImageButton) makeofferpopup.findViewById(R.id.item_info_popup_price_submit_button);
        askedpricebutton = (Button) makeofferpopup.findViewById(R.id.item_info_popup_asking_price_button);
        String Price = String.valueOf(askedprice1);
        askedpricebutton.setText(Price);
        editoffer.setText(previousaskedprice1);


        askedpricebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                asktemp = askedpricebutton.getText();
                editoffer.setText(asktemp);


            }
        });

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final long offeredprice = Long.valueOf(editoffer.getText().toString());

                final Map<String, Object> sell = new HashMap<>();
                sell.put("BidderID", useridpopup);
                sell.put("ProductID", productidpopup);
                sell.put("SellerID", sellerid);
                sell.put("SellerName", sellername1);
                sell.put("ProductName", productname1);
                sell.put("OfferedPrice", offeredprice);
                sell.put("OfferStatus", "Pending");
                sell.put("SellerNumber", sellernumber1);
                sell.put("VerificationStatus", false);
                sell.put("ReviewStatus", false);
                sell.put("EditOfferStatus", false);
                sell.put("FinalAcceptStatus", false);
                sell.put("PayDeliveryStatus", false);
                sell.put("BuyerCity", "");
                sell.put("BuyerState", "");
                sell.put("DeliveryFee", 0);
                sell.put("SellerCity", sellercity1);
                sell.put("SellerState", sellerstate1);
                sell.put("AdminID", "");


                DocumentReference dbref = db.collection("Users").document(useridpopup).collection("Cart").document(productidpopup);
                dbref.update("OfferedPrice", offeredprice).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        flag1 = 1;

                    }
                });

                if (flag1==1){


                    DocumentReference dbre = db.collection("Users").document(sellerid1).collection("Sell").document(productidpopup).collection("Offers").document(useridpopup);
                    dbre.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if (documentSnapshot.exists()){

                                DocumentReference dbre1 = db.collection("Users").document(sellerid1).collection("Sell").document(productidpopup).collection("Offers").document(useridpopup);
                                dbre1.update("OfferedPrice", offeredprice).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        flag1 =2;

                                    }
                                });

                            }
                            else {

                                DocumentReference dbre4 = db.collection("Users").document(sellerid1).collection("Sell").document(productidpopup).collection("Offers").document(useridpopup);
                                dbre4.set(sell).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        DocumentReference dbref5 = db.collection("Users").document(useridpopup).collection("Cart").document(productidpopup);
                                        dbref5.update("OfferStatus", "Pending");
                                        flag1 =2;

                                    }
                                });


                            }

                        }
                    });


                }


                if (flag1 == 2){

                    makeofferpopup.dismiss();
                    Toast.makeText(ItemInfoPage.this,"Offer Updated",Toast.LENGTH_SHORT).show();

                }


            }
        });

        exitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeofferpopup.dismiss();
            }
        });

        makeofferpopup.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
        makeofferpopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        makeofferpopup.show();
    }

}
