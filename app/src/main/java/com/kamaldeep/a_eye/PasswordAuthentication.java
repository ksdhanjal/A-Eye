package com.kamaldeep.a_eye;


import androidx.annotation.NonNull;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordAuthentication
{
    private static final String TAG = PasswordAuthentication.class.getSimpleName();

    @NonNull
    public static String hashPassword(String password)
    {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword)
    {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}