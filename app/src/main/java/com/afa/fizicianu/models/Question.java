package com.afa.fizicianu.models;

/**
 * Created by filip on 4/25/2016.
 */
public class Question {
    private String intrebare;
    private String r1;
    private String r2;
    private String r3;
    private String r4;
    private int rc;

    public Question(){

    }

    public String getIntrebare() {
        return intrebare;
    }

    public String getR1() {
        return r1;
    }

    public String getR2() {
        return r2;
    }

    public String getR3() {
        return r3;
    }

    public String getR4() {
        return r4;
    }

    public int getRc() {
        return rc;
    }

    public void setRC(int rc) {
        this.rc = rc;
    }

    public void setIntrebare(String intrebare) {
        this.intrebare = intrebare;
    }

    public void setR1(String r1) {
        this.r1 = r1;
    }

    public void setR2(String r2) {
        this.r2 = r2;
    }

    public void setR3(String r3) {
        this.r3 = r3;
    }

    public void setR4(String r4) {
        this.r4 = r4;
    }
}
