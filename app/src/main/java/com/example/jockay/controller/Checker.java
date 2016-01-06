package com.example.jockay.controller;

import android.util.Patterns;

import com.example.jockay.model.User;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jockay on 2015.12.28..
 */
public class Checker {

    /**
     * Checks if the given string contains only letters.
     * @param name String to check.
     * @return True if the string only contains letters, else returns false.
     */
    public boolean isAlpha(String name) {
        return name.matches("[a-zA-Z]+");
    }

    /**
     * Converts a string to a format with first letter uppercase, and other letters lowercase.
     * @param name Name to convert.
     * @return The converted name, or null of the name contains not only letters.
     */
    public String toPersonNameFormat(String name) {
        if (name != null || !name.equals("")) {
            if (isAlpha(name)) {
                name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                return name;
            } else {
                return null;
            }
        }
        return name;
    }

    /**
     * Checks if the given string represented date is formatted as YYYY-MM-DD.
     *
     * @param dateStr String represented date to check.
     * @return True if the date format is YYYY-MM-DD, else returns false.
     */
    public boolean isValidDate(String dateStr) {
        //String dtStart = "2010-10-15T09:27:37Z";
        if(dateStr.length() != 10)
            return false;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(dateStr);
            if(date.after(new Date()))
                return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the given e-mail has a correct format.
     * @param email E-mail address to check.
     * @return True if the e-mail has a good format, else returns false.
     */
    public boolean isValidEmailFormat(String email) {
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        } else {
            return true;
        }
    }
}
