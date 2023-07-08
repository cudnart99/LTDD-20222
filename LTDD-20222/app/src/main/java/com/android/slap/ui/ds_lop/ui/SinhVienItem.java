package com.android.slap.ui.ds_lop.ui;

public class SinhVienItem {
    private String name;
    private int diemDanh;
    private double giuaKy;
    private double cuoiKy;

    public SinhVienItem(String name, int diemDanh, double giuaKy, double cuoiKy) {
        this.name = name;
        this.diemDanh = diemDanh;
        this.giuaKy = giuaKy;
        this.cuoiKy = cuoiKy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDiemDanh() {
        return diemDanh;
    }

    public void setDiemDanh(int diemDanh) {
        this.diemDanh = diemDanh;
    }

    public double getGiuaKy() {
        return giuaKy;
    }

    public void setGiuaKy(double giuaKy) {
        this.giuaKy = giuaKy;
    }

    public double getCuoiKy() {
        return cuoiKy;
    }

    public void setCuoiKy(double cuoiKy) {
        this.cuoiKy = cuoiKy;
    }
}
