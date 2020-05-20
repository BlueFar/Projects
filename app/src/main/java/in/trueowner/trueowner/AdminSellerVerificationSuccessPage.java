package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminSellerVerificationSuccessPage extends AppCompatActivity {

    Button homebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_seller_verification_success_page);

        homebutton = (Button) findViewById(R.id.admin_seller_verification_home_button);

        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminSellerVerificationSuccessPage.this, AdminSellerVerificationSuccessPage.class);
                startActivity(intent);
            }
        });

    }
}
