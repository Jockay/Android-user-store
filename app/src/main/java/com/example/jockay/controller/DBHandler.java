package com.example.jockay.controller;

import android.content.Context;
import android.database.*;
import android.database.sqlite.*;
import android.content.ContentValues;

import com.example.jockay.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jockay on 2015.12.06..
 */
public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "users.db";
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_BIRTH = "birth_date";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWD = "password";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_ROLE = "role";
    public static final String COLUMN_STATE = "state";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    /**
     * Steps to do on creation of the database.
     *
     * @param db Database object to manage.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String query = "CREATE TABLE "+ TABLE_NAME +" (" +
                    " " + COLUMN_SURNAME + " TEXT NOT NULL," +
                    " " + COLUMN_FIRSTNAME + " TEXT NOT NULL," +
                    " " + COLUMN_BIRTH + " TEXT NOT NULL," +
                    " " + COLUMN_USERNAME + " TEXT PRIMARY KEY," +
                    " " + COLUMN_PASSWD + " TEXT NOT NULL," +
                    " " + COLUMN_EMAIL + " TEXT NOT NULL," +
                    " " + COLUMN_ROLE + " TEXT NOT NULL," +
                    " " + COLUMN_STATE + " TEXT NOT NULL);";
            db.execSQL(query);

            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('Abc', 'Cde', '2000-11-11', 'a1'," +
                    " '1', 'my1@mail.address', 'ADMIN', 'A' " + ");");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('Abc', 'Cde', '2000-11-11', 'a1p'," +
                    " '1', 'my2@mail.address', 'ADMIN', 'P' " + ");");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('Abc', 'Cde', '2000-11-11', 'a2p'," +
                    " '1', 'my3@mail.address', 'ADMIN', 'P' " + ");");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('Abc', 'Cde', '2000-11-11', 'a1d'," +
                    " '1', 'my4@mail.address', 'ADMIN', 'D' " + ");");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('Abc', 'Cde', '2000-11-11', 'admin1'," +
                    " '1', 'my5@mail.address', 'ADMIN', 'A' " + ");");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('Abc', 'Cde', '2000-11-11', 'admin'," +
                    " 'admin', 'my6@mail.address', 'ADMIN', 'A' " + ");");

            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('Abc', 'Cde', '1920-12-12', 'u1'," +
                    " '0', 'her1@mail.address', 'USER', 'A' " + ");");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('Abc', 'Cde', '1920-12-12', 'u1p'," +
                    " '0', 'her2@mail.address', 'USER', 'P' " + ");");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('Abc', 'Cde', '1920-12-12', 'u2p'," +
                    " '0', 'her3@mail.address', 'USER', 'P' " + ");");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('Abc', 'Cde', '1920-12-12', 'u3p'," +
                    " '0', 'her4@mail.address', 'USER', 'P' " + ");");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('Abc', 'Cde', '1920-12-12', 'u1d'," +
                    " '0', 'her5@mail.address', 'USER', 'D' " + ");");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('Abc', 'Cde', '1920-12-12', 'user1'," +
                    " '0', 'her6@mail.address', 'USER', 'A' " + ");");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('Abc', 'Cde', '1920-12-12', 'user'," +
                    " 'user', 'her7@mail.address', 'USER', 'A' " + ");");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('Abc', 'Cde', '1920-12-12', 'happyuser1'," +
                    " 'user', 'her8@mail.address', 'USER', 'A' " + ");");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Steps to do on dataabse upgrade.
     *
     * @param db Database to manage.
     * @param oldVersion Database old version.
     * @param newVersion Database new version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DELETE TABLE IF EXISTS " + TABLE_NAME + ";");
            onCreate(db);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts a new user to the database.
     *
     * @param user User object with the user details to insert.
     * @return True if succeed, else returns false.
     */
    public boolean addUser(User user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SURNAME, user.getSurname());
        values.put(COLUMN_FIRSTNAME, user.getFirstName());
        values.put(COLUMN_BIRTH, user.getBirthDate());
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWD, user.getPassword());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_ROLE, user.getRole());
        values.put(COLUMN_STATE, user.getState());

        try {
            SQLiteDatabase db = getWritableDatabase();
            db.insert(TABLE_NAME, null, values);
            db.close();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sets a users state from "P" to "D".
     *
     * @param username Username of the user to delete.
     * @return True if succeed, else returns false.
     */
    public boolean deleteUser(String username) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("UPDATE " + TABLE_NAME + " SET " + COLUMN_STATE + "='D' WHERE " +
                    COLUMN_USERNAME + "='" + username + "' AND " + COLUMN_STATE + "='P'" + ";");
            db.close();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Changes a user state from "A" to "P" in the database.
     *
     * @param username Name of the user to suspend.
     * @return True if succeed, else returns false.
     */
    public boolean suspendUser(String username) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("UPDATE " + TABLE_NAME + " SET " + COLUMN_STATE + "='P' WHERE " +
                            COLUMN_USERNAME + "='" + username + "';");
            db.close();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates a user's details in the database.
     *
     * @param user A User with the details to update to.
     * @return True if succeed, else returns false.
     */
    public boolean updateUser(User user) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("UPDATE " + TABLE_NAME + " SET " +
                    COLUMN_SURNAME + "='" + user.getSurname() + "', " +
                    COLUMN_FIRSTNAME + "='" + user.getFirstName() + "', " +
                    COLUMN_BIRTH + "='" + user.getBirthDate() + "', " +
                    COLUMN_PASSWD + "='" + user.getPassword() + "', " +
                    COLUMN_EMAIL + "='" + user.getEmail() + "' " +
                    " WHERE " + COLUMN_USERNAME + "='" + user.getUsername() + "';");
            db.close();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates a user role from "USER" to "ADMIN".
     *
     * @param username Username of the user to make admin.
     * @return True if succeed, else returns false.
     */
    public boolean makeAdminFromUser(String username) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("UPDATE " + TABLE_NAME + " SET " +
                    COLUMN_ROLE + "='ADMIN' " +
                    " WHERE " + COLUMN_USERNAME + "='" + username + "' " +
                    "AND " + COLUMN_ROLE + "='USER';");
            db.close();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns a list with user names in the database.
     *
     * @return List<String> type list with user names in the database.
     */
    public List<String> getAllUsername() {
        try {
            List<String> result = new ArrayList<>();
            SQLiteDatabase db = getWritableDatabase();
            String query = "SELECT " + COLUMN_USERNAME + " FROM " + TABLE_NAME + " WHERE 1;";

            Cursor c = db.rawQuery(query, null);

            if(!c.moveToFirst())
                return null;

//            int num = c.getCount();

            while (!c.isAfterLast()) {
                String name = c.getString(c.getColumnIndex(COLUMN_USERNAME));
                result.add(name);
                c.moveToNext();
            }
            c.close();
            db.close();
            return result;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Searches a user in the database by username and returns a User object with user details.
     *
     * @param username Email address to search.
     * @return A User object with the found user details, if nothing found, then returns null.
     */
    public User getUserByUsername(String username) {
        User user = new User();
        try {
            SQLiteDatabase db = getWritableDatabase();
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE username='" + username + "';";

            Cursor c = db.rawQuery(query, null);

            if(!c.moveToFirst())
                return null;

            while (!c.isAfterLast()) {
                user.setSurname(c.getString(c.getColumnIndex(COLUMN_SURNAME)));
                user.setFirstName(c.getString(c.getColumnIndex(COLUMN_FIRSTNAME)));
                user.setBirthDate(c.getString(c.getColumnIndex(COLUMN_BIRTH)));
                user.setUsername(c.getString(c.getColumnIndex(COLUMN_USERNAME)));
                user.setPassword(c.getString(c.getColumnIndex(COLUMN_PASSWD)));
                user.setEmail(c.getString(c.getColumnIndex(COLUMN_EMAIL)));
                user.setRole(c.getString(c.getColumnIndex(COLUMN_ROLE)));
                user.setState(c.getString(c.getColumnIndex(COLUMN_STATE)));
                c.moveToNext();
            }

            c.close();
            db.close();
            return user;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Searches a user in the database by e-mail and returns a User object with user details.
     *
     * @param email Email address to search.
     * @return A User object with the found user details, if nothing found, then returns null.
     */
    public User getUserByEmail(String email) {
        User user = new User();
        try {
            SQLiteDatabase db = getWritableDatabase();
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE "+ COLUMN_EMAIL +"='" + email + "';";

            Cursor c = db.rawQuery(query, null);

            if(!c.moveToFirst())
                return null;

            while (!c.isAfterLast()) {
                user.setSurname(c.getString(c.getColumnIndex(COLUMN_SURNAME)));
                user.setFirstName(c.getString(c.getColumnIndex(COLUMN_FIRSTNAME)));
                user.setBirthDate(c.getString(c.getColumnIndex(COLUMN_BIRTH)));
                user.setUsername(c.getString(c.getColumnIndex(COLUMN_USERNAME)));
                user.setPassword(c.getString(c.getColumnIndex(COLUMN_PASSWD)));
                user.setEmail(c.getString(c.getColumnIndex(COLUMN_EMAIL)));
                user.setRole(c.getString(c.getColumnIndex(COLUMN_ROLE)));
                user.setState(c.getString(c.getColumnIndex(COLUMN_STATE)));
                c.moveToNext();
            }

            c.close();
            db.close();
            return user;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Searches a user in the database by e-mail and username and returns a User object with user details.
     *
     * @param email Email address to search.
     * @param name Username to search.
     * @return A User object with found user details, if nothing found, then returns null.
     */
    public User getUserByEmailAndUsername(String email, String name) {
        User user = new User();
        try {
            SQLiteDatabase db = getWritableDatabase();
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE "+ COLUMN_EMAIL +"='" + email + "' AND " +
                    COLUMN_USERNAME+"='" + name + "';";

            Cursor c = db.rawQuery(query, null);

            if(!c.moveToFirst())
                return null;

            while (!c.isAfterLast()) {
                user.setSurname(c.getString(c.getColumnIndex(COLUMN_SURNAME)));
                user.setFirstName(c.getString(c.getColumnIndex(COLUMN_FIRSTNAME)));
                user.setBirthDate(c.getString(c.getColumnIndex(COLUMN_BIRTH)));
                user.setUsername(c.getString(c.getColumnIndex(COLUMN_USERNAME)));
                user.setPassword(c.getString(c.getColumnIndex(COLUMN_PASSWD)));
                user.setEmail(c.getString(c.getColumnIndex(COLUMN_EMAIL)));
                user.setRole(c.getString(c.getColumnIndex(COLUMN_ROLE)));
                user.setState(c.getString(c.getColumnIndex(COLUMN_STATE)));
                c.moveToNext();
            }

            c.close();
            db.close();
            return user;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Checks a username and password match, and returns an int constant value.
     * Constants and values: <br>
     * WRONG_PASSWORD = -5; <br>
     * NON_EXIST_USER = -4; <br>
     * DELETED_USER = -3; <br>
     * PASSIVE_USER = -2; <br>
     * ERROR = -1; <br>
     * USER = 0; <br>
     * ADMIN = 1; <br>
     *
     * @param username Username try to log in to the system.
     * @param password Password of the user try to log in to the system.
     * @return Login state code.
     */
    public int login(String username, String password) {
        final int WRONG_PASSWORD = -5;
        final int NON_EXIST_USER = -4;
        final int DELETED_USER = -3;
        final int PASSIVE_USER = -2;
        final int ERROR = -1;
        final int USER = 0;
        final int ADMIN = 1;

        User user;

        try {
             user = getUserByUsername(username);
        } catch(Exception e) {
            return ERROR;
        }

        if(user == null)
            return NON_EXIST_USER;

        if(user.getPassword().equals(password)) {
            if (user.getState().equals("P"))
                return PASSIVE_USER;
            else if (user.getState().equals("D"))
                return DELETED_USER;

            if(user.getRole().equals("ADMIN")) {
                return ADMIN;
            } else {
                return USER;
            }
        } else {
            return WRONG_PASSWORD;
        }
    }
}




