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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jockay.controller.DBHandler;
import com.example.jockay.model.User;

import java.util.List;

public class AdminSessionActivity extends Activity {

    private TextView twWelcome;
    private TextView twMessage;
    private Button   btSettings;
    private Button   btLogOut;
    private ListView lwUserList;

    private boolean connected;
    private DBHandler dbh;
    private String username;
    private String welcomeUser;
    private List<String> usernamesList;
    private ArrayAdapter<String> userListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_session);
        getActionBar().setDisplayHomeAsUpEnabled(false);
        setDbh(new DBHandler(this, null, null, 1));
        setTwWelcome((TextView) findViewById(R.id.twWelcome));
        setTwMessage((TextView) findViewById(R.id.twMessage));
        setBtSettings((Button) findViewById(R.id.btSettings));
        setBtLogOut((Button) findViewById(R.id.btLogOut));
        setLwUserList((ListView) findViewById(R.id.lwUserList));

        Intent intent = getIntent();
        setUsername(intent.getStringExtra("com.example.jockay.hw.name"));
        setWelcomeUser(getUsername());
        getTwWelcome().setText("Welcome " + getWelcomeUser() + "!");
        setUserList();
        getLwUserList().setFastScrollAlwaysVisible(true);
        getLwUserList().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        getLwUserList().setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String s = String.valueOf(parent.getItemAtPosition(position));
                        setUsername(s.split(" - ")[0]);
                        view.setSelected(true);
                        getTwMessage().setText("Selected user: " + getUsername());
                    }
                }
        );

        if(getLwUserList().getItemAtPosition(0) != null) {
            getTwMessage().setText("Selected user: " +
                    getLwUserList().getItemAtPosition(0).toString().split(" - ")[0]);
        } else {
            getTwMessage().setText("");
        }
    }

    public void setUserList() {
        setUsernamesList(getDbh().getAllUsername());
        String[] usernames = getUsernamesList().toArray(new String[getUsernamesList().size()]);
        for(int i = 0; i < usernames.length; i++) {
            String s = usernames[i];
            User u = getDbh().getUserByUsername(s);
            s = s + " - "+ u.getState() +" - ";
            if(u.getRole().equals("ADMIN")) {
                s = s + "Admin";
            } else if(u.getRole().equals("USER")) {
                s = s + "User";
            }
            usernames[i] = s;
        }
        setUserListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, usernames));
        getLwUserList().setAdapter(getUserListAdapter());
    }

    public void onViewPressed(View view) {
        if(checkConnection()) {
            if (!getUsername().equals("")) {
                try {
                    User u = getDbh().getUserByUsername(getUsername());

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Account details");
                    builder.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    String state = "";
                    if (u.getState().equals("A"))
                        state = "Active";
                    else if (u.getState().equals("P"))
                        state = "Suspended";
                    else if (u.getState().equals("D"))
                        state = "Deleted";

                    builder.setMessage("Username: " + u.getUsername() + "\n" +
                            "Surname: " + u.getSurname() + "\n" +
                            "First Name: " + u.getFirstName() + "\n" +
                            "Birth Date: " + u.getBirthDate() + "\n" +
                            "E-mail: " + u.getEmail() + "\n" +
                            "State: " + state + "\n" +
                            "Role: " + u.getRole() + "\n");

                    AlertDialog alert = builder.create();
                    alert.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "No user selected", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Please connect to internet.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onDeletePressed(View view) {
        if(checkConnection()) {
            try {
                User u = getDbh().getUserByUsername(getUsername());
                if (u.getState().equals("D")) {
                    Toast.makeText(this, "Already deleted user.", Toast.LENGTH_LONG).show();
                } else if (u.getState().equals("A")) {
                    Toast.makeText(this, "Cannot delete active user.", Toast.LENGTH_LONG).show();
                } else if (u.getRole().equals("ADMIN")) {
                    Toast.makeText(this, "Cannot delete administrator.", Toast.LENGTH_LONG).show();
                } else if (u.getState().equals("P")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                getDbh().deleteUser(getUsername());
                                setUserList();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Please connect to internet.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onMakeAdminPressed(View view) {
        if(checkConnection()) {
            try {
                User u = getDbh().getUserByUsername(getUsername());
                if (u.getRole().equals("ADMIN")) {
                    Toast.makeText(this, "Already an administrator.", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                getDbh().makeAdminFromUser(getUsername());
                                setUserList();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Please connect to internet.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSettingsPressed(View view) {
        if(checkConnection()) {
            getBtSettings().setEnabled(false);
            Intent intent = new Intent(this, AdminSettingsActivity.class);
            intent.putExtra("com.example.jockay.hw.name", getWelcomeUser());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please connect to internet.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onLogOutPressed(View view) {
        getBtLogOut().setEnabled(false);
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onRefreshListPressed(View view) {
        if(checkConnection()) {
            setUserList();
            if(getLwUserList().getItemAtPosition(0) != null) {
                getTwMessage().setText("Selected user: " +
                        getLwUserList().getItemAtPosition(0).toString().split(" - ")[0]);
            } else {
                getTwMessage().setText("");
            }
        } else {
            Toast.makeText(this, "Please connect to internet.", Toast.LENGTH_SHORT).show();
        }
    }

    public TextView getTwWelcome() {
        return twWelcome;
    }

    public void setTwWelcome(TextView twWelcome) {
        this.twWelcome = twWelcome;
    }

    public TextView getTwMessage() {
        return twMessage;
    }

    public void setTwMessage(TextView twMessage) {
        this.twMessage = twMessage;
    }

    public Button getBtSettings() {
        return btSettings;
    }

    public void setBtSettings(Button btSettings) {
        this.btSettings = btSettings;
    }

    public Button getBtLogOut() {
        return btLogOut;
    }

    public void setBtLogOut(Button btLogOut) {
        this.btLogOut = btLogOut;
    }

    public ListView getLwUserList() {
        return lwUserList;
    }

    public void setLwUserList(ListView lwUserList) {
        this.lwUserList = lwUserList;
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

    public String getWelcomeUser() {
        return welcomeUser;
    }

    public void setWelcomeUser(String welcomeUser) {
        this.welcomeUser = welcomeUser;
    }

    public List<String> getUsernamesList() {
        return usernamesList;
    }

    public void setUsernamesList(List<String> usernamesList) {
        this.usernamesList = usernamesList;
    }

    public ArrayAdapter<String> getUserListAdapter() {
        return userListAdapter;
    }

    public void setUserListAdapter(ArrayAdapter<String> userListAdapter) {
        this.userListAdapter = userListAdapter;
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

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
