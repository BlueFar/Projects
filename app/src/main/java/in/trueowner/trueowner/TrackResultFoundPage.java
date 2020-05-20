package in.trueowner.trueowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TrackResultFoundPage extends AppCompatActivity {

    ImageView backbutton, callbutton;
    public static final int REQUEST_CALL = 9;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String documentid, mobno;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_result_found_page);

        Intent intent = getIntent();
        documentid = intent.getExtras().getString("DocumentID");
        backbutton = (ImageView) findViewById(R.id.result_found_back_button);
        callbutton = (ImageView) findViewById(R.id.result_found_call_icon);
        name = (TextView) findViewById(R.id.result_found_profile_name);

        DocumentReference dr = db.collection("TrackDevice").document(documentid);
        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String name1 = documentSnapshot.getString("FullName");
                String mobno = documentSnapshot.getString("MobileNumber");
                name.setText(name1);

            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        callbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              makePhoneCall(mobno);

            }
        });

    }

    public void makePhoneCall(String number){

        if(number.trim().length() < 10 ){

            Toast.makeText(TrackResultFoundPage.this, "Seller does not wish to share his number", Toast.LENGTH_SHORT).show();

        } else {

            if (ContextCompat.checkSelfPermission(TrackResultFoundPage.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(TrackResultFoundPage.this,
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

}
