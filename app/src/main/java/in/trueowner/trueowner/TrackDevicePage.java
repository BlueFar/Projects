package in.trueowner.trueowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class TrackDevicePage extends AppCompatActivity {

    ImageView backbutton;
    Button registerlostdevice;
    EditText nobox;
    FloatingActionButton submitbutton;
    String enteredtext, userid;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;
    LoginPopup loginPopup = new LoginPopup(TrackDevicePage.this);

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

                loginPopup.StartLoadingDialog();
                enteredtext = nobox.getText().toString().trim();

                CollectionReference dref = db.collection("TrackDevice");
                Query query = dref.whereEqualTo("Imei1", enteredtext);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if (task.getResult().isEmpty()){



                                } else {

                                    String docid = document.getId();
                                    Intent intent = new Intent(TrackDevicePage.this, TrackResultFoundPage.class);
                                    intent.putExtra("DocumentID", docid);
                                    loginPopup.DismissDialog();
                                    startActivity(intent);

                                }


                            }
                        } else {

                            CollectionReference dref = db.collection("TrackDevice");
                            Query query = dref.whereEqualTo("Imei2", enteredtext);
                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                            if (task.getResult().isEmpty()){



                                            } else {

                                                String docid = document.getId();
                                                Intent intent = new Intent(TrackDevicePage.this, TrackResultFoundPage.class);
                                                intent.putExtra("DocumentID", docid);
                                                loginPopup.DismissDialog();
                                                startActivity(intent);

                                            }


                                        }
                                    } else {

                                        CollectionReference dref = db.collection("TrackDevice");
                                        Query query = dref.whereEqualTo("SerialNumber", enteredtext);
                                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                                        if (task.getResult().isEmpty()){



                                                        } else {

                                                            String docid = document.getId();
                                                            Intent intent = new Intent(TrackDevicePage.this, TrackResultFoundPage.class);
                                                            intent.putExtra("DocumentID", docid);
                                                            loginPopup.DismissDialog();
                                                            startActivity(intent);

                                                        }


                                                    }
                                                } else {

                                                    Intent intent = new Intent(TrackDevicePage.this, TrackNoResultPage.class);
                                                    loginPopup.DismissDialog();
                                                    startActivity(intent);

                                                }
                                            }
                                        });

                                    }
                                }
                            });

                        }
                    }
                });

            }
        });

    }
}
