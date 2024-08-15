package com.example.semester;

public class TrafficStationObj {
    private int image;
    private String tenTram;
    private String diaChi;

    public TrafficStationObj(int image, String tenTram, String diaChi) {
        this.image = image;
        this.tenTram = tenTram;
        this.diaChi = diaChi;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTenTram() {
        return tenTram;
    }

    public void setTenTram(String tenTram) {
        this.tenTram = tenTram;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
}
