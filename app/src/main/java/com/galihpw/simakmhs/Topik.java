package com.galihpw.simakmhs;

/**
 * Created by Nada on 12/26/2016.
 */

public class Topik {

    private String judulForum;
    private String isiForum;

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

    public Topik(String judulForum, String isiForum){
        this.judulForum = judulForum;
        this.isiForum = isiForum;
    }


}
