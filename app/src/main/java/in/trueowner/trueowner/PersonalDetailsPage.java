package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PersonalDetailsPage extends AppCompatActivity {

    ImageView backbutton, editbutton;
    TextView fullname, email, mobilenumber;
    String userid, fullname1, email1, mobilenumber1;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_details_page);

        backbutton = (ImageView) findViewById(R.id.personal_details_back_button);
        editbutton = (ImageView) findViewById(R.id.edit_button);
        fullname = (TextView) findViewById(R.id.personal_details_name);
        email = (TextView) findViewById(R.id.personal_details_email);
        mobilenumber = (TextView) findViewById(R.id.personal_details_mobile_number);

        DocumentReference dr = db.collection("Users").document(userid).collection("Details").document("PersonalDetails");
        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                fullname1 = documentSnapshot.getString("FullName");
                email1 = documentSnapshot.getString("Email");
                mobilenumber1 = documentSnapshot.getString("MobileNumber");

            }
        });

        fullname.setText(fullname1);
        email.setText(email1);
        mobilenumber.setText(mobilenumber1);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PersonalDetailsPage.this, EditProfile.class);
                intent.putExtra("FullName",fullname1);
                intent.putExtra("Email",email1);
                intent.putExtra("MobileNumber",mobilenumber1);
                intent.putExtra("UserID",userid);
                startActivity(intent);

            }
        });

    }
}
