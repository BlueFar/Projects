package in.trueowner.trueowner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SupportPage extends AppCompatActivity {

    private EditText namebox, emailbox, messagebox;
    private ImageView backbutton;
    FloatingActionButton submitbutton;
    Dialog successpopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_page);

        successpopup = new Dialog(this);

        backbutton = (ImageView) findViewById(R.id.support_back_button);
        namebox = (EditText) findViewById(R.id.support_name_box);
        emailbox = (EditText) findViewById(R.id.support_email_box);
        messagebox = (EditText) findViewById(R.id.support_message_box);
        submitbutton = (FloatingActionButton) findViewById(R.id.support_submit_button);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowPopup(v) ;

                namebox.setText("");
                emailbox.setText("");
                messagebox.setText("");
            }
        });

    }

    public void ShowPopup(View v) {

        Button successbutton;
        successpopup.setContentView(R.layout.support_popup);
        successbutton =(Button) successpopup.findViewById(R.id.success_button);



        successbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                successpopup.dismiss();

            }
        });
        successpopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        successpopup.show();
    }

}
