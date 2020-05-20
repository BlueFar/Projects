package in.trueowner.trueowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordPage extends AppCompatActivity {

    private Button resetPassword;
    private TextView signupButton;
    EditText emailbox;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String emailAddress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_page);

    signupButton = (TextView) findViewById(R.id.forgot_password_signup);
    resetPassword = (Button) findViewById(R.id.reset_password_button);
    emailbox = (EditText) findViewById(R.id.forgot_password_email);


    signupButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(ForgotPasswordPage.this, SignupPage.class);
            startActivity(intent);
        }
    });


    resetPassword.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            emailAddress = emailbox.getText().toString().trim();
            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPasswordPage.this, "Reset password link sent", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    });


    }
}
