package com.galihpw.simakmhs.config;

/**
 * Created by ACER on 24/10/2016.
 */

public class Config {
    //public static final String URL = "http://simak.pe.hu/";
    public static final String URL = "http://192.168.8.105/~classroom/";
    public static final String LOGIN_URL = "http://192.168.8.105/~classroom/loginmhs.php";

    public static final String KEY_NIM = "nim";
    public static final String KEY_NAMA = "nama_mhs";
    public static final String KEY_KELAS = "kelas";
    public static final String KEY_ALAMAT = "alamat_mhs";
    public static final String KEY_KONTAK = "kontak_mhs";
    public static final String KEY_BINTANG = "bintang";
    public static final String KEY_EMAIL = "email_mhs";
    public static final String KEY_FB = "facebook_mhs";
    public static final String KEY_TW = "twitter_mhs";
    public static final String SUCCESS = "success";

    public static final String JSON_ARRAY = "result";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the email of current logged in user
    public static final String NIM_SHARED_PREF = "nim";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
}
