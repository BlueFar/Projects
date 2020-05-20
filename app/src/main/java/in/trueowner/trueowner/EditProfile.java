package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    ImageView backbutton;
    EditText namebox, emailbox, mobilenumberbox;
    Button updatebutton;
    String name, email, mobileno;
    String userid;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        backbutton = (ImageView) findViewById(R.id.edit_profile_back_button);
        updatebutton = (Button) findViewById(R.id.edit_profile_submit_button);
        namebox = (EditText) findViewById(R.id.edit_profile_name);
        emailbox = (EditText) findViewById(R.id.edit_profile_email);
        mobilenumberbox = (EditText) findViewById(R.id.edit_profile_number);

        Intent intent = getIntent();
        String nametemp = intent.getExtras().getString("FullName");
        String emailtemp = intent.getExtras().getString("Email");
        String mobilenotemp = intent.getExtras().getString("MobileNumber");
        userid = intent.getExtras().getString("UserID");

        namebox.setHint(nametemp);
        emailbox.setHint(emailtemp);
        mobilenumberbox.setHint(mobilenotemp);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register();



            }
        });

    }

    public void register(){

        name = namebox.getText().toString().trim();
        email = emailbox.getText().toString().trim();
        mobileno = mobilenumberbox.getText().toString().trim();


        if(!validate()){



        }

        else{

            final Map<String, Object> personaldetails = new HashMap<>();
            personaldetails.put("FullName", name);
            personaldetails.put("Email", email);
            personaldetails.put("MobileNumber", mobileno);

            DocumentReference dr = db.collection("Users").document(userid).collection("Details").document("PersonalDetails");
            dr.update(personaldetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Intent intent = new Intent(EditProfile.this, PersonalDetailsPage.class);
                    startActivity(intent);
                    finish();

                }
            });


        }

    }

    public Boolean validate(){

        Boolean valid = true;

        if (name.isEmpty() || name.length()<2){

            namebox.setError("Please enter your name");

            valid = false;

        }

        if (email.isEmpty() || email.length()<5){

            emailbox.setError("Please enter a valid email address");

            valid = false;

        }
        if (mobileno.isEmpty() || mobileno.length()<10 ){

            mobilenumberbox.setError("Please enter a valid mobile number");

            valid = false;

        }

        return valid;
    }

}
