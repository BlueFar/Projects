package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ForgotPasswordPage extends AppCompatActivity {

    private Button resetPassword;
    private TextView signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_page);

    signupButton = (TextView) findViewById(R.id.forgot_password_signup);
    resetPassword = (Button) findViewById(R.id.reset_password_button);

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


        }
    });


    }
}
