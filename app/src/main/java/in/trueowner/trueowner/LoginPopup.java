package in.trueowner.trueowner;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class LoginPopup {

    private Activity activity;
    private AlertDialog dialog;

    public LoginPopup(Activity myActivity) {

        activity = myActivity;

    }

    void StartLoadingDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.login_popup, null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();

    }

    void DismissDialog() {

        dialog.dismiss();

    }

}
