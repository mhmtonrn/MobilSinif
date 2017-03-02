package com.softengine.dudu.mobilsinif.Sifreleme;



public class Sifrele {

  /*  String metin;

    public Sifrele(String metin) {
        this.metin = metin;
    }*/

    public String kilitle(String metin){

        return new StringBuilder(metin).reverse().toString();
    }

    public String kilitAc(String metin){
        return new StringBuilder(metin).reverse().toString();
    }
}
