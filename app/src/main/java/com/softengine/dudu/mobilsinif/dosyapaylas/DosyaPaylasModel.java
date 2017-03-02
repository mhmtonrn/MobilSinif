package com.softengine.dudu.mobilsinif.dosyapaylas;

/**
 * Created by mehmet on 7.1.2017.
 */

public class DosyaPaylasModel {
    private String Aciklama;
    private String Ad;
    private String Baslik;
    private String Ders;
    private String Image;

    public DosyaPaylasModel(String aciklama, String ad, String baslik, String ders, String Image) {
        Aciklama = aciklama;
        Ad = ad;
        Baslik = baslik;
        Ders = ders;
        this.Image = Image;
    }

    public DosyaPaylasModel() {
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

    public String getImage() {
        return Image;
    }

    public void setLink(String Image) {
        this.Image = Image;
    }
}
