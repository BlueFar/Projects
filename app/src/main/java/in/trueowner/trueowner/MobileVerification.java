package in.trueowner.trueowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MobileVerification extends AppCompatActivity {

    EditText mobilebox;
    Button submit;
    Dialog otpcodepopup;
    String verificationid;
    EditText otpbox;
    FirebaseUser user;
    String userid;
    String phonenumber;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_verification);

        mAuth = FirebaseAuth.getInstance();
        mobilebox = findViewById(R.id.verification_mobile_no);
        submit = findViewById(R.id.verification_submit);
        otpcodepopup = new Dialog(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String num = mobilebox.getText().toString().trim();
                if (num.isEmpty() || num.length()<10){

                    mobilebox.setError("Enter a valid number");
                    mobilebox.requestFocus();
                    return;
                }

                ShowPopup(num) ;

            }
        });

    }

    private void ShowPopup(String num) {

        otpcodepopup.setContentView(R.layout.mobile_otp_popup);
         otpbox = otpcodepopup.findViewById(R.id.verification_otp_code);
        Button otpbutton = otpcodepopup.findViewById(R.id.otp_submit);

        phonenumber = "+91" + num;
        sendVerificationCode(phonenumber);

        otpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = otpbox.getText().toString().trim();

                if (code.isEmpty() || code.length()<6){

                    otpbox.setError("Enter Valid OTP");
                    otpbox.requestFocus();
                    return;

                }
                verifycode(code);

            }
        });

        otpcodepopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        otpcodepopup.show();

    }
    private void sendVerificationCode(String phonenumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phonenumber, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallBack);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
    mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationid = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code !=null){

                otpbox.setText(code);
                verifycode(code);


            } else {



            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }
    };

    private void verifycode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);
        signInWithCredential(credential);

    }

    private void signInWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Map<String, Object> userdetails = new HashMap<>();
                    userdetails.put("MobileNumber", phonenumber);
                    DocumentReference dr = db.collection("Users").document(userid).collection("Details").document("PersonalDetails");
                    dr.set(userdetails, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            DocumentReference dr1 = db.collection("Users").document(userid).collection("Details").document("PersonalDetails");
                            dr1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    Boolean adminstatus = documentSnapshot.getBoolean("AdminStatus");
                                    if (adminstatus){

                                        DocumentReference dr3 = db.collection("Admins").document(userid).collection("Details").document("PersonalDetails");
                                        dr3.set(userdetails, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {



                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                DocumentReference dr4 = db.collection("Users").document(userid).collection("Details").document("PersonalDetails");
                                                dr4.delete();
                                                Toast.makeText(MobileVerification.this, "Unable to verify at this time", Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    DocumentReference dr2 = db.collection("Users").document(userid).collection("Details").document("PersonalDetails");
                                    dr2.delete();
                                    Toast.makeText(MobileVerification.this, "Unable to verify at this time", Toast.LENGTH_SHORT).show();

                                }
                            });

                            FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                            user1.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                otpcodepopup.dismiss();
                                                FirebaseUser user4 = FirebaseAuth.getInstance().getCurrentUser();

                                                if (user != null){

                                                    FirebaseAuth.getInstance().signOut();

                                                }

                                                    Toast.makeText(MobileVerification.this, "Account Registered, Please Re-Login", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(MobileVerification.this, LoginPage.class);
                                                    startActivity(intent);
                                                    finish();




                                            }
                                        }
                                    });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(MobileVerification.this, "Unable to verify at this time", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(MobileVerification.this, "Invalid Code", Toast.LENGTH_SHORT).show();

            }
        });


    }
}
