package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LostDeviceRegisterPage extends AppCompatActivity {

    ImageView backbutton;
    Button trackdevice, registdevicebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_device_register_page);

        backbutton = (ImageView) findViewById(R.id.track_device_back_button1);
        trackdevice = (Button) findViewById(R.id.track_device_tab_button2);
        registdevicebutton = (Button) findViewById(R.id.register_device_button);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        trackdevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LostDeviceRegisterPage.this, TrackDevicePage.class);
                startActivity(intent);

            }
        });

        registdevicebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

    }
}
