package com.galihpw.simakmhs;

/**
 * Created by Nada on 12/21/2016.
 */

public class Resume {
    private String judulCat;
    private String isiCat;

    public Resume(String judulCat, String isiCat){
        this.judulCat = judulCat;
        this.isiCat = isiCat;
    }

    public String getJudulCat() {
        return judulCat;
    }

    public void setJudulCat(String judulCat) {
        this.judulCat = judulCat;
    }

    public String getIsiCat() {
        return isiCat;
    }

    public void setIsiCat(String isiCat) {
        this.isiCat = isiCat;
    }
}
