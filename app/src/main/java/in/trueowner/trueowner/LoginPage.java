package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginPage extends AppCompatActivity {

    private Button SignupButton,LoginButton;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

    SignupButton = (Button) findViewById(R.id.signup_button);
        LoginButton = (Button) findViewById(R.id.login);
        forgotPassword = (TextView) findViewById(R.id.forgot_password);

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
                Intent intent2 = new Intent(LoginPage.this, HomePage.class);
                startActivity(intent2);
            }
        });

    }
}
