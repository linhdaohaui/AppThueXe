package com.example.semester;

public class TransactionHistoryObj {
    private String tenNganHang;
    private double soDiemDaNop;
    private String trangThai;
    private String thoiGian;
    private double tongSoDiem;
    private int maKH;

    public TransactionHistoryObj() {
    }

    public TransactionHistoryObj(String tenNganHang, double soDiemDaNop, String trangThai, String thoiGian, double tongSoDiem, int maKH) {
        this.tenNganHang = tenNganHang;
        this.soDiemDaNop = soDiemDaNop;
        this.trangThai = trangThai;
        this.thoiGian = thoiGian;
        this.tongSoDiem = tongSoDiem;
        this.maKH = maKH;
    }

    public String getTenNganHang() {
        return tenNganHang;
    }

    public void setTenNganHang(String tenNganHang) {
        this.tenNganHang = tenNganHang;
    }

    public double getSoDiemDaNop() {
        return soDiemDaNop;
    }

    public void setSoDiemDaNop(double soDiemDaNop) {
        this.soDiemDaNop = soDiemDaNop;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public double getTongSoDiem() {
        return tongSoDiem;
    }

    public void setTongSoDiem(double tongSoDiem) {
        this.tongSoDiem = tongSoDiem;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }
}
