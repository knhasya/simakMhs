package com.galihpw.simakmhs.config;

/**
 * Created by ACER on 24/10/2016.
 */

public class Config {
    //public static final String URL = "http://simak.pe.hu/";
    public static final String URL = "http://simak.pe.hu/";
    public static final String LOGIN_URL = "http://simak.pe.hu/loginmhs.php";

    public static final String KEY_NIM = "nim";
    public static final String KEY_NAMA = "nama_mhs";
    public static final String KEY_KELAS = "kelas";
    public static final String KEY_ALAMAT = "alamat_mhs";
    public static final String KEY_KONTAK = "kontak_mhs";
    public static final String KEY_BINTANG = "bintang";
    public static final String KEY_EMAIL = "email_mhs";
    public static final String KEY_FB = "facebook_mhs";
    public static final String KEY_TW = "twitter_mhs";
    public static final String KEY_MATKUL = "nama_matkul";
    public static final String KEY_KODEMATKUL = "kode_matkul";
    public static final String KEY_HARI = "hari";
    public static final String KEY_NAMADOSEN = "nama_dosen";
    public static final String KEY_RESUME = "resume";
    public static final String KEY_PERTEMUAN = "pertemuan";
    public static final String KEY_JUDUL = "judul";
    public static final String KEY_ISITOPIK = "isi_topik";
    public static final String KEY_IDTOPIK = "id_topik";
    public static final String KEY_ISIKOMENTAR = "isi_komentar";
    public static final String KEY_WAKTUMULAI = "waktu_mulai";
    public static final String KEY_WAKTUSELESAI = "waktu_selesai";
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_ISI_MATERI = "isi_materi";
    public static final String SUCCESS = "success";

    public static final String JSON_ARRAY = "result";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the email of current logged in user
    public static final String NIM_SHARED_PREF = "nim";
    public static final String MATKUL_SHARED_PREF = "matkul";
    public static final String KMATKUL_SHARED_PREF = "kodematkul";
    public static final String DOSEN_SHARED_PREF = "dosen";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
}
