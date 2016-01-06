package com.example.jockay.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.example.jockay.controller.DBHandler;

public class MainActivity extends Activity {

    private boolean connected = false;
    private DBHandler dbh;
    private String username;
    private int loginState;

    private EditText etUserName;
    private EditText etPassword;
    private TextView twErrorMessage;
    private Button btLogin;
    private Button btRegister;

    public boolean isAllFieldFilled() {
        if((getEtPassword().getText().toString().isEmpty()) ||
                (getEtUserName().getText().toString().isEmpty()))
            return false;
        return true;
    }

    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            setConnected(true);
        }
        else
            setConnected(false);

        return isConnected();
    }

    public boolean checkAndSetMessage() {
        setLoginState(getDbh().login(getEtUserName().getText().toString().toLowerCase(),
                getEtPassword().getText().toString()));
        if(!checkConnection()) {
            Toast.makeText(this, "Login and registration disabled without internet connection!", Toast.LENGTH_LONG).show();
            return false;
        } else if(!isAllFieldFilled()) {
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_LONG).show();
            return false;
        } else if(getLoginState() == -1) {
            Toast.makeText(this, "Login problem, something went wrong.", Toast.LENGTH_LONG).show();
            return false;
        } else if(getLoginState() == -2) {
            Toast.makeText(this, "Login Denied - Suspended user.", Toast.LENGTH_LONG).show();
            getEtUserName().setError("Login Denied - Suspended user.");
            return false;
        } else if(getLoginState() == -3) {
            Toast.makeText(this, "Login Denied - Deleted user.", Toast.LENGTH_LONG).show();
            getEtUserName().setError("Login Denied - Deleted user.");
            return false;
        } else if(getLoginState() == -4) {
            Toast.makeText(this, "User does not exists.", Toast.LENGTH_LONG).show();
            getEtUserName().setError("User does not exists.");
            return false;
        } else if(getLoginState() == -5) {
            getEtPassword().setError("Wrong password.");
            return false;
        } else {
            getTwErrorMessage().setText("");
            return true;
        }
    }

    public void onLoginPressed(View view) {
        getBtLogin().setEnabled(false);
        if(checkAndSetMessage()) {
            Intent intent;
            finish();
            if(getLoginState() == 1) {
                intent = new Intent(this, AdminSessionActivity.class);
                intent.putExtra("com.example.jockay.hw.name", etUserName.getText().toString());
                startActivity(intent);
            } else if(getLoginState() == 0) {
                intent = new Intent(this, UserSessionActivity.class);
                intent.putExtra("com.example.jockay.hw.name", etUserName.getText().toString());
                startActivity(intent);
            }
        } else {
            getBtLogin().setEnabled(true);
        }
    }

    public void onRegistrationPressed(View view) {
        getBtRegister().setEnabled(false);
        if(checkConnection()) {
            Intent intent= new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        }
        else {
            getTwErrorMessage().setText("You are offline! Please connect to internet to use the application.");
            getBtRegister().setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTwErrorMessage((TextView) findViewById(R.id.twMessage));
        setEtUserName((EditText) findViewById(R.id.etUserName));
        setEtPassword((EditText) findViewById(R.id.etPassword));
        setBtLogin((Button) findViewById(R.id.btLogIn));
        setBtRegister((Button) findViewById(R.id.btRegister));
        setDbh(new DBHandler(this, null, null, 1));

        if (checkConnection()) {
            getTwErrorMessage().setText("");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Caution");
            builder.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setMessage("You are offline! Please connect to internet to use the application.");
            AlertDialog alert = builder.create();
            alert.show();
            getTwErrorMessage().setText("You are offline, login and register disabled. Please connect to internet.");
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public DBHandler getDbh() {
        return dbh;
    }

    public void setDbh(DBHandler dbh) {
        this.dbh = dbh;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLoginState() {
        return loginState;
    }

    public void setLoginState(int loginState) {
        this.loginState = loginState;
    }

    public EditText getEtUserName() {
        return etUserName;
    }

    public void setEtUserName(EditText etUserName) {
        this.etUserName = etUserName;
    }

    public EditText getEtPassword() {
        return etPassword;
    }

    public void setEtPassword(EditText etPassword) {
        this.etPassword = etPassword;
    }

    public TextView getTwErrorMessage() {
        return twErrorMessage;
    }

    public void setTwErrorMessage(TextView twErrorMessage) {
        this.twErrorMessage = twErrorMessage;
    }

    public Button getBtLogin() {
        return btLogin;
    }

    public void setBtLogin(Button btLogin) {
        this.btLogin = btLogin;
    }

    public Button getBtRegister() {
        return btRegister;
    }

    public void setBtRegister(Button btRegister) {
        this.btRegister = btRegister;
    }
}
