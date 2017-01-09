package com.galihpw.simakmhs;

/**
 * Created by Nada on 12/26/2016.
 */

public class Topik {

    private String judulForum;
    private String isiForum;
    private String idTopik;
    private String namaForum;

    public String getIdTopik() {
        return idTopik;
    }

    public void setIdTopik(String idTopik) {
        this.idTopik = idTopik;
    }

    public String getJudulForum() {
        return judulForum;
    }

    public void setJudulForum(String judulForum) {
        this.judulForum = judulForum;
    }

    public String getIsiForum() {
        return isiForum;
    }

    public void setIsiForum(String isiForum) {
        this.isiForum = isiForum;
    }

    public String getNamaForum() {
        return namaForum;
    }

    public void setNamaForum(String namaForum) {
        this.namaForum = namaForum;
    }

    public Topik(String judulForum, String isiForum, String idTopik, String namaForum){
        this.judulForum = judulForum;
        this.isiForum = isiForum;
        this.idTopik = idTopik;
        this.namaForum = namaForum;
    }


}
