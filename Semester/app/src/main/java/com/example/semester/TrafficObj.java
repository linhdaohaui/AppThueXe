package com.example.semester;

public class TrafficObj {
    private int image;
    private String loaiXe;
    private String bienSoXe;

    public TrafficObj() {
    }

    public TrafficObj(int image, String loaiXe, String bienSoXe) {
        this.image = image;
        this.loaiXe = loaiXe;
        this.bienSoXe = bienSoXe;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getLoaiXe() {
        return loaiXe;
    }

    public void setLoaiXe(String loaiXe) {
        this.loaiXe = loaiXe;
    }

    public String getBienSoXe() {
        return bienSoXe;
    }

    public void setBienSoXe(String bienSoXe) {
        this.bienSoXe = bienSoXe;
    }
}
