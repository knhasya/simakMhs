package com.galihpw.simakmhs.data;

/**
 * Created by ACER on 20/10/2016.
 */

public class Data {
    private String nim, nama, kelas, alamat, email, facebook, twitter, foto;
    private int bintang;

    public Data() {
    }

    public Data(String nim, String nama, String kelas, int bintang, String alamat, String email, String facebook, String twitter, String foto) {
        this.nim = nim;
        this.nama = nama;
        this.kelas = kelas;
        this.bintang = bintang;
        this.alamat = alamat;
        this.email = email;
        this.facebook = facebook;
        this.twitter = twitter;
        this.foto = foto;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public int getBintang() {
        return bintang;
    }

    public void setBintang(int bintang) {
        this.bintang = bintang;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
