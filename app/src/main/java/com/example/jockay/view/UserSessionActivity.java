package com.example.jockay.view;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jockay.controller.Checker;
import com.example.jockay.controller.DBHandler;
import com.example.jockay.model.User;

public class UserSessionActivity extends Activity {

    private String username;
    private User user;
    private boolean connected;

    private EditText etSurname;
    private EditText etFirstName;
    private EditText etEmail;
    private EditText etBirthDate;
    private EditText etPassword;
    private EditText etPasswordAgain;
    private TextView twErrorMessage;
    private TextView twWelcome;
    private Button btLogOut;

    private DBHandler dbh;
    private Checker checker;

    public boolean isAllFieldFilled() {
        if((getEtSurname().getText().toString().isEmpty()) ||
                (getEtFirstName().getText().toString().isEmpty()) ||
                (getEtEmail().getText().toString().isEmpty()) ||
                (getEtBirthDate().getText().toString().isEmpty()))
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
        User u = getDbh().getUserByUsername(getUsername());
        if(!checkConnection()) {
            getTwErrorMessage().setText("You are offline, please connect to internet to continue.");
            return false;
        } else if(u.getState().equals("P")) {
            Toast.makeText(this, "Suspended account.", Toast.LENGTH_LONG).show();
            return false;
        } else if(!isAllFieldFilled()) {
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_LONG).show();
            return false;
        } else if(!getChecker().isAlpha(getEtSurname().getText().toString())) {
            getEtSurname().setError("Invalid character(s).");
            Toast.makeText(this, "Invalid character(s) in surname.", Toast.LENGTH_LONG).show();
            return false;
        } else if(!getChecker().isAlpha(getEtFirstName().getText().toString())) {
            getEtFirstName().setError("Invalid character(s).");
            Toast.makeText(this, "Invalid character(s) in first name.", Toast.LENGTH_LONG).show();
            return false;
        } else if(!getChecker().isValidEmailFormat(getEtEmail().getText().toString())) {
            getEtEmail().setError("Invalid e-mail address.");
            Toast.makeText(this, "Invalid e-mail address.", Toast.LENGTH_LONG).show();
            return false;
        } else if(getDbh().getUserByEmail(getEtEmail().getText().toString()) != null
                && getDbh().getUserByEmailAndUsername(getEtEmail().getText().toString(), getUsername()) == null) {
            getEtEmail().setError("Already registered e-mail address.");
            Toast.makeText(this, "Already registered e-mail address.", Toast.LENGTH_LONG).show();
            return false;
        } else if(!getEtPassword().getText().equals("")) {
            if(!getEtPassword().getText().toString().equals(getEtPasswordAgain().getText().toString())) {
                getEtPasswordAgain().setError("Passwords must match.");
                Toast.makeText(this, "Passwords must match.", Toast.LENGTH_LONG).show();
                return false;
            } else {
                getEtPasswordAgain().setError(null);
                return true;
            }
        } else {
            getTwErrorMessage().setText("");
            return true;
        }
    }

    public void setFieldsContent() {
        setUser(getDbh().getUserByUsername(getUsername()));

        String surname   = getUser().getSurname();
        String firstName = getUser().getFirstName();
        String birthDate = getUser().getBirthDate();
        String email     = getUser().getEmail();

        getEtSurname().setText(surname, TextView.BufferType.EDITABLE);
        getEtFirstName().setText(firstName, TextView.BufferType.EDITABLE);
        getEtBirthDate().setText(birthDate, TextView.BufferType.EDITABLE);
        getEtEmail().setText(email, TextView.BufferType.EDITABLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_session);
        setDbh(new DBHandler(this, null, null, 1));
        setChecker(new Checker());
        Intent i = getIntent();
        setUsername (i.getStringExtra("com.example.jockay.hw.name"));

        setEtSurname((EditText) findViewById(R.id.etSurname));
        setEtFirstName((EditText) findViewById(R.id.etFirstName));
        setEtEmail((EditText) findViewById(R.id.etEmail));
        setEtBirthDate((EditText) findViewById(R.id.etBirthDate));
        setEtPassword((EditText) findViewById(R.id.etPassword));
        setEtPasswordAgain((EditText) findViewById(R.id.etPasswordAgain));
        setTwErrorMessage((TextView) findViewById(R.id.twMessage));
        setTwWelcome((TextView) findViewById(R.id.twWelcome));
        setBtLogOut((Button) findViewById(R.id.btLogOut));

        getTwWelcome().setText("Welcome " + getUsername() + "!");
        setFieldsContent();
        getEtBirthDate().setFocusable(false);
        getEtBirthDate().setFocusableInTouchMode(false);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }


    public void onUpdatePressed(View view) {
        if (checkAndSetMessage()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmation");
            builder.setMessage("Password:");
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(input);
            builder.setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    if(input.getText().toString().equals(getUser().getPassword())) {
                        String surname = getChecker().toPersonNameFormat(getEtSurname().getText().toString());
                        String firstname = getChecker().toPersonNameFormat(getEtFirstName().getText().toString());
                        String birthDate = getEtBirthDate().getText().toString();
                        String password = getEtPassword().getText().toString();
                        if (password.equals("")) {
                            password = getUser().getPassword();
                        }
                        String email = getEtEmail().getText().toString();
                        String role = "USER";
                        String state = "A";

                        User u = new User(
                                surname, firstname, birthDate, getUsername(),
                                password, email, role, state
                        );
                        if (getDbh().updateUser(u)) {
                            getTwErrorMessage().setText("Successfully updated.");
                            setFieldsContent();
                            getEtPassword().setText("");
                            getEtPasswordAgain().setText("");
                            setUser(u);
                        } else {
                            getTwErrorMessage().setText("Update error, something went wrong.");
                        }
                    } else {
                        getTwErrorMessage().setText("Cannot update, wrong password!");
                    }

                    dialog.dismiss();
                }

            }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void onSuspendAccountPressed(View view) {
        if(!checkConnection()) {
            Toast.makeText(this, "Please connect to internet.", Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Password:");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        builder.setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().equals(getUser().getPassword())) {
                    if (getDbh().suspendUser(getUsername())) {
                        getTwErrorMessage().setText("Successfully suspended, log out please.");
//                        finish();
                    } else {
                        getTwErrorMessage().setText("Cannot suspend, something went wrong!");
                    }
                } else {
                    getTwErrorMessage().setText("Wrong password.");
                }
                dialog.dismiss();
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        User user = getDbh().getUserByUsername(getUsername());
        if(!user.getState().equals("P")) {
            alert.show();
        } else {
            Toast.makeText(this, "Your account already suspended.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onLogOutPressed(View view) {
        getBtLogOut().setEnabled(false);
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public TextView getTwErrorMessage() {
        return twErrorMessage;
    }

    public void setTwErrorMessage(TextView twErrorMessage) {
        this.twErrorMessage = twErrorMessage;
    }

    public TextView getTwWelcome() {
        return twWelcome;
    }

    public void setTwWelcome(TextView twWelcome) {
        this.twWelcome = twWelcome;
    }

    public Button getBtLogOut() {
        return btLogOut;
    }

    public void setBtLogOut(Button btLogOut) {
        this.btLogOut = btLogOut;
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
}
