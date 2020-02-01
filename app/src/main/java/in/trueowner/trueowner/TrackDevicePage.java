package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TrackDevicePage extends AppCompatActivity {

    ImageView backbutton;
    Button registerlostdevice;
    EditText nobox;
    FloatingActionButton submitbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_device_page);

        backbutton = (ImageView) findViewById(R.id.track_device_back_button1);
        registerlostdevice = (Button) findViewById(R.id.register_device_tab_button1);
        nobox = (EditText) findViewById(R.id.track_device_textbox);
        submitbutton = (FloatingActionButton) findViewById(R.id.track_device_submit_button);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        registerlostdevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TrackDevicePage.this, LostDeviceRegisterPage.class);
                startActivity(intent);

            }
        });

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

    }
}
