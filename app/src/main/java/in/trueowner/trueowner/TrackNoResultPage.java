package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TrackNoResultPage extends AppCompatActivity {

    Button searchagain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_no_result_page);

        searchagain = (Button) findViewById(R.id.no_result_search_again_button);

        searchagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TrackNoResultPage.this, TrackDevicePage.class);
                startActivity(intent);

            }
        });

    }
}
