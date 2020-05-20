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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SignupPage extends AppCompatActivity {
        private Button LoginButton,SignupButton;
    EditText signupname, signupemail, signuppassword, signupconfirmpassword;
    String signupnametemp, signupemailtemp, signuppasswordtemp, signupconfirmpasswordtemp, userID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LoginPopup loginPopup = new LoginPopup(SignupPage.this);
    Dialog adminpopup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);

        LoginButton = (Button) findViewById(R.id.login_button);
        SignupButton = (Button) findViewById(R.id.signup);
        signupname = (EditText) findViewById(R.id.signup_name);
        signupemail = (EditText) findViewById(R.id.signup_email);
        signuppassword = (EditText) findViewById(R.id.signup_password);
        signupconfirmpassword = (EditText) findViewById(R.id.signup_confirm_password);
        adminpopup = new Dialog(this);


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupPage.this, LoginPage.class);
                startActivity(intent);
                finish();



            }
        });

        SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                register(v);

            }
        });

    }

    public void register(View view){

        signupnametemp = signupname.getText().toString().trim();
        signupemailtemp = signupemail.getText().toString().trim();
        signuppasswordtemp = signuppassword.getText().toString().trim();
        signupconfirmpasswordtemp = signupconfirmpassword.getText().toString().trim();




        if(!validate()){



        }

        else {

            loginPopup.StartLoadingDialog();
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(signupemailtemp, signuppasswordtemp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    if (user != null) {

                        if (signupnametemp.contains("devourme")) {

                            loginPopup.DismissDialog();
                            adminsignup(view, user, userID, signupnametemp, signupemailtemp);

                        } else {

                        Map<String, Object> userdetails = new HashMap<>();
                        userdetails.put("AdminStatus", false);
                        userdetails.put("FullName", signupnametemp);
                        userdetails.put("Email", signupemailtemp);
                        userdetails.put("UserID", userID);
                        DocumentReference dbref = db.collection("Users").document(userID).collection("Details").document("PersonalDetails");
                        dbref.set(userdetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                loginPopup.DismissDialog();
                                Intent intent1 = new Intent(SignupPage.this, MobileVerification.class);
                                startActivity(intent1);
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {


                                loginPopup.DismissDialog();
                            }
                        });
                    }
                    }

                }
            })

                    .addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {

                            loginPopup.DismissDialog();
                            Toast.makeText(SignupPage.this, "Account Registration Failed", Toast.LENGTH_SHORT).show();

                        }
                    });

        }

    }

    private void adminsignup(View v, FirebaseUser user1, String userID1, String signupnametemp1, String signupemailtemp1) {
        adminpopup.setContentView(R.layout.signup_admin_popup);


        Spinner statespinner, cityspinner;
        final String[][] city = {null};
        Button submitbutton;
        EditText branchbox;

        statespinner = adminpopup.findViewById(R.id.admin_signup_spinner_state);
        cityspinner = adminpopup.findViewById(R.id.admin_signup_spinner_city);
        branchbox = adminpopup.findViewById(R.id.branch_box);
        submitbutton = adminpopup.findViewById(R.id.admin_signup_button);

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

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginPopup.StartLoadingDialog();

                String branch = branchbox.getText().toString().trim();

                if (branch.isEmpty()){

                    branchbox.setError("Please Enter the branch name");
                    branchbox.requestFocus();
                    return;

                }
                else
                {

                    String state = statespinner.getSelectedItem().toString();
                    String city = cityspinner.getSelectedItem().toString();
                    String name = signupnametemp1;
                    String email = signupemailtemp1;
                    String ueerid = userID1;

                    Map<String, Object> userdetails = new HashMap<>();
                    userdetails.put("AdminStatus", true);
                    userdetails.put("FullName", name);
                    userdetails.put("Email", email);
                    userdetails.put("UserID", ueerid);

                    Map<String, Object> admindetails = new HashMap<>();
                    admindetails.put("FullName", name);
                    admindetails.put("Email", email);
                    admindetails.put("UserID", ueerid);
                    admindetails.put("State", state);
                    admindetails.put("City", city);
                    admindetails.put("Branch", branch);

                    Map<String, Object> branchdetails = new HashMap<>();
                    branchdetails.put("State", state);
                    branchdetails.put("City", city);
                    branchdetails.put("Branch", branch);

                    DocumentReference dbref = db.collection("Users").document(ueerid).collection("Details").document("PersonalDetails");
                    dbref.set(userdetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            DocumentReference dbref2 = db.collection("Admins").document(ueerid).collection("Details").document("PersonalDetails");
                            dbref2.set(admindetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    DocumentReference dbref3 = db.collection("Branches").document(state).collection(city).document(branch);
                                    dbref3.set(branchdetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            loginPopup.DismissDialog();
                                            Intent intent1 = new Intent(SignupPage.this, MobileVerification.class);
                                            startActivity(intent1);
                                            finish();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            DocumentReference dbref4 = db.collection("Users").document(ueerid).collection("Details").document("PersonalDetails");
                                            dbref4.delete();
                                            DocumentReference dbref5 = db.collection("Admins").document(ueerid).collection("Details").document("PersonalDetails");
                                            dbref5.delete();
                                            loginPopup.DismissDialog();
                                            Toast.makeText(SignupPage.this, "Unable to update at this time", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    DocumentReference dbref4 = db.collection("Users").document(ueerid).collection("Details").document("PersonalDetails");
                                    dbref4.delete();
                                    loginPopup.DismissDialog();
                                    Toast.makeText(SignupPage.this, "Unable to update at this time", Toast.LENGTH_SHORT).show();
                                }
                            });



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {


                            loginPopup.DismissDialog();
                            Toast.makeText(SignupPage.this, "Unable to update at this time", Toast.LENGTH_SHORT).show();
                        }
                    });

                }


            }
        });

        adminpopup.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
        adminpopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        adminpopup.show();
        adminpopup.setCancelable(false);

    }

    public Boolean validate(){

        Boolean valid = true;

        if (signupnametemp.isEmpty()){

            signupname.setError("Please enter your name");

            valid = false;

        }

        if (signupemailtemp.isEmpty()){

            signupemail.setError("Please enter your email");

            valid = false;

        }


        if (signuppasswordtemp.isEmpty()){

            signuppassword.setError("Please enter a password");

            valid = false;

        }

        else if (signuppasswordtemp.length()<6){

            signuppassword.setError("Password must contain at least 6 characters");

            valid = false;

        }

        if (signupconfirmpasswordtemp.isEmpty()){

            signupconfirmpassword.setError("Confirm password is empty");

            valid = false;

        }

        else if (!signupconfirmpasswordtemp.equals(signuppasswordtemp)){

                signupconfirmpassword.setError("Confirm password does not match with password");

                valid = false;

        }


        return valid;
    }

}
