package cn.gavin.snmp.android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

import cn.gavin.snmp.MainController;
import cn.gavin.snmp.core.model.DeviceImp;
import cn.gavin.snmp.core.model.Protocol;

public class AddDevice extends ActionBarActivity {

    private View progressView;
    private View v3AuthView;
    private View v1v2cAuthView;
    private MainController mainController;
    private RadioGroup radioGroup;

    private android.os.Handler handler = new android.os.Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        progressView = findViewById(R.id.login_progress);
        setTitle(getString(R.string.add_device_title));
        mainController = MainController.init(this);
        v3AuthView = findViewById(R.id.snm_v3_auth_field);
        v1v2cAuthView = findViewById(R.id.community_field);
        radioGroup = (RadioGroup) findViewById(R.id.snmp_version_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.v1:
                    case R.id.v2c:
                        v3AuthView.setVisibility(View.GONE);
                        v1v2cAuthView.setVisibility(View.VISIBLE);
                        break;
                    default:
                        v3AuthView.setVisibility(View.VISIBLE);
                        v1v2cAuthView.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        v1v2cAuthView.setVisibility(show ? View.GONE : View.VISIBLE);
        v3AuthView.setVisibility(show ? View.GONE : View.VISIBLE);
        findViewById(R.id.submit_device).setEnabled(show ? false : true);
        ((Button)findViewById(R.id.submit_device)).setText(show ? "Discovering" : "SUBMIT");
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

        }
    }

    public void finishedDiscovey(DeviceImp... devices) {
        showProgress(false);
        //Show device detail
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(getString(R.string.discovery_result_tile));
        ListView listView = new ListView(this);
        final DeviceAdapter deviceAdapter = new DeviceAdapter(Arrays.asList(devices), true);
        listView.setAdapter(deviceAdapter);
        dialog.setView(listView);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.add_selected_device), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(DeviceImp device : deviceAdapter.getChecks()){
                    MainController.getDeviceManager().save(device);
                }
            }
        });
        dialog.show();
    }

    public void submit(View view) {
        showProgress(true);
        final String ip = ((EditText) findViewById(R.id.identity_field)).getText().toString();
        final String community = ((TextView) findViewById(R.id.community_field)).getText().toString();
        final Object authProc = ((Spinner) findViewById(R.id.auth_Protocol_selector)).getSelectedItem();
        final String authString = ((EditText) findViewById(R.id.auth_text)).getText().toString();
        final Object privProc = ((Spinner) findViewById(R.id.priv_Protocol)).getSelectedItem();
        final String privString = ((EditText) findViewById(R.id.priv_text)).getText().toString();
        if (ip.isEmpty()) {
            showProgress(false);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                DeviceImp device = null;
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.v1:
                        device = mainController.addV1Device(ip, community);
                        break;
                    case R.id.v2c:
                        device = mainController.addV2cDevice(ip, community);
                        break;
                    case R.id.v3:
                        device = mainController.addV3Device(ip,
                                (Protocol) authProc,
                                authString,
                                (Protocol) privProc,
                                privString);
                        break;
                }
                if (device != null) {
                    String alias = ((EditText) findViewById(R.id.alias_field)).getText().toString();
                    if(!alias.isEmpty()){
                        device.setName(alias);
                    }
                    device.discovery();
                }
                final DeviceImp finalDevice = device;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        finishedDiscovey(finalDevice);
                    }
                });
            }
        }).start();


    }
}
