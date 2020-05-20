package in.trueowner.trueowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class EmailVerification extends AppCompatActivity {

    EditText emailbox;
    Button submit;
    FirebaseUser user;
    String userid;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_verification);

        emailbox = findViewById(R.id.verification_email);
        submit = findViewById(R.id.verification_submit);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailbox.getText().toString().trim();
                if (email.isEmpty()){

                    emailbox.setError("Enter a valid number");
                    emailbox.requestFocus();
                    return;
                }

                Map<String, Object> userdetails = new HashMap<>();
                userdetails.put("Email", email);
                DocumentReference dr = db.collection("Users").document(userid).collection("Details").document("PersonalDetails");
                dr.set(userdetails, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Intent intent = new Intent(EmailVerification.this, HomePage.class);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(EmailVerification.this, "Unable to verify at this time", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });
    }
}
