package com.softengine.dudu.mobilsinif.paylas;



public class PaylasModel {
    private String Aciklama;
    private String Ad;
    private String Baslik;
    private String Ders;
    private String Image;

    public PaylasModel() {
    }

    public PaylasModel(String aciklama, String ad, String baslik, String ders, String image) {
        Aciklama = aciklama;
        Ad = ad;
        Baslik = baslik;
        Ders = ders;
        Image = image;
    }


    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getAciklama() {
        return Aciklama;
    }

    public void setAciklama(String aciklama) {
        Aciklama = aciklama;
    }

    public String getAd() {
        return Ad;
    }

    public void setAd(String ad) {
        Ad = ad;
    }

    public String getBaslik() {
        return Baslik;
    }

    public void setBaslik(String baslik) {
        Baslik = baslik;
    }

    public String getDers() {
        return Ders;
    }

    public void setDers(String ders) {
        Ders = ders;
    }


}
