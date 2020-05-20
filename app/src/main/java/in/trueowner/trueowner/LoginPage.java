package in.trueowner.trueowner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class LoginPage extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    private Button SignupButton,LoginButton;
    com.facebook.login.widget.LoginButton fbLoginButton;
    private TextView forgotPassword;
    EditText loginemail, loginpassword;
    String loginemailtemp, loginpasswordtemp, userid;
    LoginPopup loginPopup = new LoginPopup(LoginPage.this);
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

    SignupButton = (Button) findViewById(R.id.signup_button);
        LoginButton = (Button) findViewById(R.id.login);
        forgotPassword = (TextView) findViewById(R.id.forgot_password);
        loginemail = (EditText) findViewById(R.id.login_email);
        loginpassword = (EditText) findViewById(R.id.login_password);

        mAuth = FirebaseAuth.getInstance();


    SignupButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginPage.this, SignupPage.class);
            startActivity(intent);
        }
    });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(LoginPage.this, ForgotPasswordPage.class);
                startActivity(intent1);
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register();

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void updateUI(FirebaseUser user, String name, String email, String userid2) {

        if (user != null){

            loginPopup.StartLoadingDialog();
            DocumentReference dr = db.collection("Users").document(userid2).collection("Details").document("PersonalDetails");

                    dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    if (document.getString("MobileNumber") != null) {

                                        Boolean adminstatus = document.getBoolean("AdminStatus");

                                        if (adminstatus){

                                            loginPopup.DismissDialog();
                                            Intent intent = new Intent(LoginPage.this, AdminHomePage.class);
                                            startActivity(intent);
                                            finish();

                                        } else {

                                            loginPopup.DismissDialog();
                                            Intent intent = new Intent(LoginPage.this, HomePage.class);
                                            startActivity(intent);
                                            finish();

                                        }

                                    } else {

                                        loginPopup.DismissDialog();
                                        Intent intent = new Intent(LoginPage.this, MobileVerification.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {

                                    loginPopup.DismissDialog();
                                    //        loginPopup.DismissDialog();
                                  //  Toast.makeText(LoginPage.this, "No Account Found, Please signup.",
                                   //         Toast.LENGTH_SHORT).show();
                                   //         Intent intent = new Intent(LoginPage.this, SignupPage.class);
                                    //        startActivity(intent);

                                    }

                                } else {

                                loginPopup.DismissDialog();

                            }
                            }


                        });

        } else {

            Toast.makeText(LoginPage.this, "Login Failed", Toast.LENGTH_SHORT).show();

        }

    }

    public void register(){

        loginemailtemp = loginemail.getText().toString().trim();
        loginpasswordtemp = loginpassword.getText().toString().trim();

        if(!validate()){



        }

        else{
            loginPopup.StartLoadingDialog();
            mAuth.signInWithEmailAndPassword(loginemailtemp, loginpasswordtemp)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null){
                                    String userid2 = user.getUid();
                                    DocumentReference dr = db.collection("Users").document(userid2).collection("Details").document("PersonalDetails");
                                    dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                            String name = documentSnapshot.getString("FullName");
                                            String email = documentSnapshot.getString("Email");
                                            updateUI(user,name,email, userid2);

                                        }
                                    });


                                } else {

                                    loginPopup.DismissDialog();
                                    Toast.makeText(LoginPage.this, "No account found", Toast.LENGTH_SHORT).show();

                                }

                            } else {
                                loginPopup.DismissDialog();
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginPage.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

        }

    }

    public Boolean validate(){

        Boolean valid = true;

        if (loginemailtemp.isEmpty()){

            loginemail.setError("Please enter your email");

            valid = false;

        }


        if (loginpasswordtemp.isEmpty()){

            loginpassword.setError("Please enter a password");

            valid = false;

        }

        else if (loginpasswordtemp.length()<6){

            loginpassword.setError("Password must contain at least 6 characters");

            valid = false;

        }


        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() !=null) {

            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                String name = user.getDisplayName();
                String email = user.getEmail();
                String userid2 = user.getUid();
                updateUI(user, name, email, userid2);
            }

        }

    }



}
