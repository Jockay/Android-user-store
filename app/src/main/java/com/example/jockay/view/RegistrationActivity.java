package com.example.jockay.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jockay.controller.Checker;
import com.example.jockay.controller.DBHandler;
import com.example.jockay.model.User;

public class RegistrationActivity extends Activity {

    private boolean connected;

    private EditText etSurname;
    private EditText etFirstName;
    private EditText etEmail;
    private EditText etBirthDate;
    private EditText etPassword;
    private EditText etPasswordAgain;
    private EditText etUserName;
    private TextView twErrorMessage;
    private TextView etState;
    private Button btRegister;

    private DBHandler dbh;
    private Checker checker;

    public boolean isAllFieldFilled() {
        if ((getEtSurname().getText().toString().isEmpty()) ||
                (getEtFirstName().getText().toString().isEmpty()) ||
                (getEtEmail().getText().toString().isEmpty()) ||
                (getEtBirthDate().getText().toString().isEmpty()) ||
                (getEtPassword().getText().toString().isEmpty()) ||
                (getEtPasswordAgain().getText().toString().isEmpty()) ||
                (getEtUserName().getText().toString().isEmpty()))
            return false;
        return true;
    }

    public boolean checkAndSetMessage() {
        String state = getEtState().getText().toString();
        if(!checkConnection()) {
            Toast.makeText(this, "Connect to internet to continue", Toast.LENGTH_LONG).show();
            return false;
        } else if(!isAllFieldFilled()) {
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_LONG).show();
            return false;
        } else if(getDbh().getUserByUsername(getEtUserName().getText().toString()) != null) {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_LONG).show();
            getEtUserName().setError("Username already exists.");
            return false;
        } else if(!getChecker().isAlpha(getEtSurname().getText().toString())) {
            getEtSurname().setError("Invalid character(s).");
            Toast.makeText(this, "Invalid character(s) in surname.", Toast.LENGTH_LONG).show();
            return false;
        } else if(!getChecker().isAlpha(getEtFirstName().getText().toString())) {
            getEtFirstName().setError("Invalid character(s).");
            Toast.makeText(this, "Invalid character(s) in first name.", Toast.LENGTH_LONG).show();
            return false;
        } else if(!getChecker().isValidDate(getEtBirthDate().getText().toString())) {
            getEtBirthDate().setError("Invalid date format (It should be: YYYY-MM-DD). Or greater date than actual.");
            Toast.makeText(this, "Invalid date format, or greater date than actual.", Toast.LENGTH_LONG).show();
            return false;
        } else if(!getChecker().isValidEmailFormat(getEtEmail().getText().toString())) {
            getEtEmail().setError("Invalid e-mail address.");
            Toast.makeText(this, "Invalid e-mail address.", Toast.LENGTH_LONG).show();
            return false;
        } else if(getDbh().getUserByEmail(getEtEmail().getText().toString()) != null) {
            getEtEmail().setError("Already registered e-mail address.");
            Toast.makeText(this, "Already registered e-mail address.", Toast.LENGTH_LONG).show();
            return false;
        } else if(!getEtPassword().getText().toString().equals(getEtPasswordAgain().getText().toString())) {
            getEtPasswordAgain().setError("Passwords must match.");
            Toast.makeText(this, "Passwords must match.", Toast.LENGTH_LONG).show();
            return false;
        } else if(!state.equals("A") && !state.equals("P") && !state.equals("D")) {
            getEtState().setError("Invalid state.");
            Toast.makeText(this, "Invalid state.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Toast.makeText(this, "Registering...", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
        //super.onBackPressed();
    }

    public void onRegisterPressed(View view) {
        if(checkAndSetMessage()) {
            getBtRegister().setEnabled(false);
            String surname = getChecker().toPersonNameFormat(getEtSurname().getText().toString());
            String firstname = getChecker().toPersonNameFormat(getEtFirstName().getText().toString());
            String birthDate = getEtBirthDate().getText().toString();
            String username = getEtUserName().getText().toString().toLowerCase();
            String password = getEtPassword().getText().toString();
            String email = getEtEmail().getText().toString();
            String role = "USER";
            String state = getEtState().getText().toString();

            User user = new User(
                surname, firstname, birthDate, username,
                password, email, role, state
            );

            if(getDbh().addUser(user)) {
                finish();
                startActivity(new Intent(this, MainActivity.class));
            } else {
                Toast.makeText(this, "Database error.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            setConnected (true);
        }
        else
            setConnected (false);

        return isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setEtSurname((EditText) findViewById(R.id.etSurname));
        setEtFirstName((EditText) findViewById(R.id.etFirstName));
        setEtEmail((EditText) findViewById(R.id.etEmail));
        setEtBirthDate((EditText) findViewById(R.id.etBirthDate));
        setEtPassword((EditText) findViewById(R.id.etPassword));
        setEtPasswordAgain((EditText) findViewById(R.id.etPasswordAgain));
        setEtUserName((EditText) findViewById(R.id.etUserName));
        setEtState((EditText) findViewById(R.id.etState));
        setTwErrorMessage((TextView) findViewById(R.id.twMessage));
        setBtRegister((Button) findViewById(R.id.btRegister));
        setDbh(new DBHandler(this, null, null, 1));
        setChecker(new Checker());
    }


    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public EditText getEtSurname() {
        return etSurname;
    }

    public void setEtSurname(EditText etSurname) {
        this.etSurname = etSurname;
    }

    public EditText getEtFirstName() {
        return etFirstName;
    }

    public void setEtFirstName(EditText etFirstName) {
        this.etFirstName = etFirstName;
    }

    public EditText getEtEmail() {
        return etEmail;
    }

    public void setEtEmail(EditText etEmail) {
        this.etEmail = etEmail;
    }

    public EditText getEtBirthDate() {
        return etBirthDate;
    }

    public void setEtBirthDate(EditText etBirthDate) {
        this.etBirthDate = etBirthDate;
    }

    public EditText getEtPassword() {
        return etPassword;
    }

    public void setEtPassword(EditText etPassword) {
        this.etPassword = etPassword;
    }

    public EditText getEtPasswordAgain() {
        return etPasswordAgain;
    }

    public void setEtPasswordAgain(EditText etPasswordAgain) {
        this.etPasswordAgain = etPasswordAgain;
    }

    public EditText getEtUserName() {
        return etUserName;
    }

    public void setEtUserName(EditText etUserName) {
        this.etUserName = etUserName;
    }

    public TextView getTwErrorMessage() {
        return twErrorMessage;
    }

    public void setTwErrorMessage(TextView twErrorMessage) {
        this.twErrorMessage = twErrorMessage;
    }

    public TextView getEtState() {
        return etState;
    }

    public void setEtState(TextView etState) {
        this.etState = etState;
    }

    public DBHandler getDbh() {
        return dbh;
    }

    public void setDbh(DBHandler dbh) {
        this.dbh = dbh;
    }

    public Checker getChecker() {
        return checker;
    }

    public void setChecker(Checker checker) {
        this.checker = checker;
    }

    public Button getBtRegister() {
        return btRegister;
    }

    public void setBtRegister(Button btRegister) {
        this.btRegister = btRegister;
    }
}
