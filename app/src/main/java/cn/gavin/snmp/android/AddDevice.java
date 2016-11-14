package cn.gavin.snmp.android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import cn.gavin.snmp.MainController;
import cn.gavin.snmp.core.model.DeviceImp;
import cn.gavin.snmp.core.model.Protocol;
import cn.gavin.snmp.core.model.SNMPVersion;

public class AddDevice extends ActionBarActivity {

    private View progressView;
    private View v3AuthView;
    private View v1v2cAuthView;
    private MainController mainController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        progressView = findViewById(R.id.login_progress);
        setTitle(getString(R.string.add_device_title));
        mainController = MainController.init(this);
        v3AuthView = findViewById(R.id.snm_v3_auth_field);
        v1v2cAuthView = findViewById(R.id.community_field);
    }
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            v1v2cAuthView.setVisibility(show ? View.GONE : View.VISIBLE);
            v3AuthView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void finishedDiscovey(DeviceImp ... devices){
        showProgress(false);
        //Show device detail
    }

    public void submit(){
        showProgress(true);
        final RadioGroup radioGroup = (RadioGroup)findViewById(R.id.snmp_version_group);
        final String ip = ((EditText)findViewById(R.id.identity_field)).getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                DeviceImp device = null;
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.v1:
                        device = mainController.addV1Device(ip, ((TextView)findViewById(R.id.community_field)).getText().toString());
                        break;
                    case R.id.v2c:
                        device = mainController.addV2cDevice(ip, ((TextView)findViewById(R.id.community_field)).getText().toString());
                        break;
                    case R.id.v3:
                        device = mainController.addV3Device(ip,
                                (Protocol)((Spinner)findViewById(R.id.auth_Protocol_selector)).getSelectedItem(),
                                ((EditText)findViewById(R.id.auth_text)).getText().toString(),
                                (Protocol)((Spinner)findViewById(R.id.priv_Protocol)).getSelectedItem(),
                                ((EditText)findViewById(R.id.priv_text)).getText().toString());
                        break;
                }
                if(device!=null) {
                    device.discovery();
                }
                finishedDiscovey(device);
            }
        }).start();


    }
}
