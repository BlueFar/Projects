package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SellSuccessPage extends AppCompatActivity {

    Button homebutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_success_page);

        homebutton = (Button) findViewById(R.id.sell_success_home_button);

        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellSuccessPage.this, HomePage.class);
                startActivity(intent);
            }
        });

    }
}
