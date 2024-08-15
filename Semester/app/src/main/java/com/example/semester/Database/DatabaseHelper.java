package com.example.semester.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.semester.TransactionHistoryObj;

import java.util.ArrayList;
import java.util.Objects;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, "RentBike.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        MyDatabase.execSQL("create Table KHACHHANG(MAKH INTEGER PRIMARY KEY AUTOINCREMENT, TENKH TEXT, MATKHAU TEXT, " + "NGAYSINH TEXT, GIOITINH TEXT, SDT TEXT)");
        MyDatabase.execSQL("create Table LICHSUGIAODICH(MAGIAODICH INTEGER PRIMARY KEY AUTOINCREMENT, TENNGANHANG TEXT, " + " SOTIENGIAODICH DOUBLE, TRANGTHAIGIAODICH TEXT,  THOIGIANGIAODICH TEXT, TONGSODIEM DOUBLE,MAKH INTEGER, " + "  FOREIGN KEY (MAKH) REFERENCES KHACHHANG(MAKH) )");
        MyDatabase.execSQL("create Table CHUYENDI(MACHUYENDI INTEGER PRIMARY KEY AUTOINCREMENT, TRAMXUATPHAT TEXT, DIACHI TEXT, " + "LOAIXE TEXT, BIENSOXE TEXT, KINHDO DOUBLE, VIDO DOUBLE ,TONGTHOIGIAN DOUBLE, TONGTIEN DOUBLE,MAKH INTEGER, " + "  FOREIGN KEY (MAKH) REFERENCES KHACHHANG(MAKH))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists KHACHHANG");
        MyDB.execSQL("drop Table if exists LICHSUGIAODICH");
        MyDB.execSQL("drop Table if exists CHUYENDI");
    }

    public Boolean insertData(String tenkh, String matkhau, String ngaysinh, String gioitinh, String sdt) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TENKH", tenkh);
        contentValues.put("MATKHAU", matkhau);
        contentValues.put("NGAYSINH", ngaysinh);
        contentValues.put("GIOITINH", gioitinh);
        contentValues.put("SDT", sdt);
        long result = MyDatabase.insert("KHACHHANG", null, contentValues);
        return result != -1;
    }

    public Boolean checkPhoneNumber(String phoneNumber) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from KHACHHANG where SDT = ?", new String[]{phoneNumber});
        return cursor.getCount() > 0;
    }

    public Boolean checkPassword(String sdt, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from KHACHHANG where SDT = ? and MATKHAU = ?", new String[]{sdt, password});
        return cursor.getCount() > 0;
    }

    public int getMaKH(String sdt) {
        int maKH = 0;
        try {
            Cursor cursor = this.getReadableDatabase().rawQuery("Select * from KHACHHANG where SDT = ?", new String[]{sdt});
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
                maKH = cursor.getInt(0);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("TAG", Objects.requireNonNull(e.getMessage()));
        }
        return maKH;
    }

    public String getTenKH(String sdt) {
        String tenkh = "";
        try {
            Cursor cursor = this.getReadableDatabase().rawQuery("Select * from KHACHHANG where SDT = ?", new String[]{sdt});
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
                tenkh = cursor.getString(1);
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("TAG", Objects.requireNonNull(e.getMessage()));
        }
        return tenkh;
    }

    public String getNgaySinh(String sdt) {
        String ngaysinh = "";
        try {
            Cursor cursor = this.getReadableDatabase().rawQuery("Select * from KHACHHANG where SDT = ?", new String[]{sdt});
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
                ngaysinh = cursor.getString(3);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("TAG", Objects.requireNonNull(e.getMessage()));
        }
        return ngaysinh;
    }

    public String getGioiTinh(String sdt) {
        String gioiTinh = "";
        try {
            Cursor cursor = this.getReadableDatabase().rawQuery("Select * from KHACHHANG where SDT = ?", new String[]{sdt});
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
                gioiTinh = cursor.getString(4);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("TAG", Objects.requireNonNull(e.getMessage()));
        }
        return gioiTinh;
    }

    public Boolean updateHoTen(int maKH, String hoTen) {
        Cursor cursor = this.getReadableDatabase().rawQuery("Select * from KHACHHANG where MAKH = ?", new String[]{String.valueOf(maKH)});
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        if (cursor.getCount() < 0) return false;
        else {
            cursor.moveToFirst();
            ContentValues contentValues = new ContentValues();
            contentValues.put("TENKH", hoTen);
            long result = MyDatabase.update("KHACHHANG", contentValues, " MAKH = ?", new String[]{String.valueOf(maKH)});
            return result != -1;
        }
    }

    public Boolean updateMatKhau(int maKH, String matKhau) {
        Cursor cursor = this.getReadableDatabase().rawQuery("Select * from KHACHHANG where MAKH = ?", new String[]{String.valueOf(maKH)});
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        if (cursor.getCount() < 0) return false;
        else {
            cursor.moveToFirst();
            ContentValues contentValues = new ContentValues();
            contentValues.put("TENKH", cursor.getString(1));
            contentValues.put("MATKHAU", matKhau);
            contentValues.put("NGAYSINH", cursor.getString(3));
            contentValues.put("GIOITINH", cursor.getString(4));
            contentValues.put("SDT", cursor.getString(5));
            long result = MyDatabase.update("KHACHHANG", contentValues, " MAKH = ?", new String[]{String.valueOf(maKH)});
            return result != -1;
        }
    }

    public Boolean updateGioiTinh(int maKH, String gioiTinh) {
        Cursor cursor = this.getReadableDatabase().rawQuery("Select * from KHACHHANG where MAKH = ?", new String[]{String.valueOf(maKH)});
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        if (cursor.getCount() < 0) return false;
        else {
            cursor.moveToFirst();
            ContentValues contentValues = new ContentValues();
            contentValues.put("TENKH", cursor.getString(1));
            contentValues.put("MATKHAU", cursor.getString(2));
            contentValues.put("NGAYSINH", cursor.getString(3));
            contentValues.put("GIOITINH", gioiTinh);
            contentValues.put("SDT", cursor.getString(5));
            long result = MyDatabase.update("KHACHHANG", contentValues, " MAKH = ?", new String[]{String.valueOf(maKH)});
            return result != -1;
        }
    }

    public Boolean updateNgaySinh(int maKH, String ngaySinh) {
        Cursor cursor = this.getReadableDatabase().rawQuery("Select * from KHACHHANG where MAKH = ?", new String[]{String.valueOf(maKH)});
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        if (cursor.getCount() < 0) return false;
        else {
            cursor.moveToFirst();
            ContentValues contentValues = new ContentValues();
            contentValues.put("TENKH", cursor.getString(1));
            contentValues.put("MATKHAU", cursor.getString(2));
            contentValues.put("NGAYSINH", ngaySinh);
            contentValues.put("GIOITINH", cursor.getString(4));
            contentValues.put("SDT", cursor.getString(5));
            long result = MyDatabase.update("KHACHHANG", contentValues, " MAKH = ?", new String[]{String.valueOf(maKH)});
            return result != -1;
        }
    }

    public Boolean insetTransactionHistory(TransactionHistoryObj transactionHistoryObj) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TENNGANHANG", transactionHistoryObj.getTenNganHang());
        contentValues.put("SOTIENGIAODICH", transactionHistoryObj.getSoDiemDaNop());
        contentValues.put("TRANGTHAIGIAODICH", transactionHistoryObj.getTrangThai());
        contentValues.put("THOIGIANGIAODICH", transactionHistoryObj.getThoiGian());
        contentValues.put("TONGSODIEM", transactionHistoryObj.getTongSoDiem());
        contentValues.put("MAKH", transactionHistoryObj.getMaKH());
        long result = MyDatabase.insert("LICHSUGIAODICH", null, contentValues);
        return result != -1;
    }

    public ArrayList<TransactionHistoryObj> getTransactionHistory(int maKH) {
        ArrayList<TransactionHistoryObj> list = new ArrayList<>();
        SQLiteDatabase MyDatabase = this.getReadableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from LICHSUGIAODICH INNER JOIN KHACHHANG " + "ON LICHSUGIAODICH.MAKH = KHACHHANG.MAKH WHERE LICHSUGIAODICH.MAKH = ?", new String[]{String.valueOf(maKH)});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(new TransactionHistoryObj(cursor.getString(1), cursor.getDouble(2), cursor.getString(3), cursor.getString(4), cursor.getDouble(5), cursor.getInt(6)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public double getMoney(int maKH) {
        double money = 0;

        SQLiteDatabase MyDatabase = this.getReadableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from LICHSUGIAODICH INNER JOIN " + "KHACHHANG ON LICHSUGIAODICH.MAKH = KHACHHANG.MAKH WHERE LICHSUGIAODICH.MAKH = ?", new String[]{String.valueOf(maKH)});
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            money = cursor.getDouble(5);
        }
        cursor.close();
        return money;
    }

    public double getBankingMoney(int maKH) {
        double money = 0;
        SQLiteDatabase MyDatabase = this.getReadableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from LICHSUGIAODICH INNER JOIN " + "KHACHHANG ON LICHSUGIAODICH.MAKH = KHACHHANG.MAKH WHERE LICHSUGIAODICH.MAKH = ?", new String[]{String.valueOf(maKH)});

        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            money = cursor.getDouble(2);
        }
        cursor.close();
        return money;
    }

    public Boolean insetJourney(String tramXuatPhat, String diaChi, String loaiXe, String bienSoXe, double kinhdo, double vido, int tongThoiGian, double tongTien, int makh) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TRAMXUATPHAT", tramXuatPhat);
        contentValues.put("DIACHI", diaChi);
        contentValues.put("LOAIXE", loaiXe);
        contentValues.put("BIENSOXE", bienSoXe);
        contentValues.put("KINHDO", kinhdo);
        contentValues.put("VIDO", vido);
        contentValues.put("TONGTHOIGIAN", tongThoiGian);
        contentValues.put("TONGTIEN", tongTien);
        contentValues.put("MAKH", makh);
        long result = MyDatabase.insert("CHUYENDI", null, contentValues);
        return result != -1;
    }

    public String getTramXuatPhat(int makh) {
        String tramXuatPhat = "";
        try {
            Cursor cursor = this.getReadableDatabase().rawQuery("Select * from CHUYENDI INNER JOIN KHACHHANG ON CHUYENDI.MAKH = KHACHHANG.MAKH WHERE CHUYENDI.MAKH = ?", new String[]{String.valueOf(makh)});
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
                tramXuatPhat = cursor.getString(1);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("TAG", Objects.requireNonNull(e.getMessage()));
        }
        return tramXuatPhat;
    }

    public String getDiaChi(int makh) {
        String diaChi = "";
        try {
            Cursor cursor = this.getReadableDatabase().rawQuery("Select * from CHUYENDI INNER JOIN KHACHHANG ON CHUYENDI.MAKH = KHACHHANG.MAKH WHERE CHUYENDI.MAKH = ?", new String[]{String.valueOf(makh)});
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
                diaChi = cursor.getString(2);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("TAG", Objects.requireNonNull(e.getMessage()));
        }
        return diaChi;
    }

    public String getLoaiXe(int makh) {
        String loaiXe = "";
        try {
            Cursor cursor = this.getReadableDatabase().rawQuery("Select * from CHUYENDI INNER JOIN KHACHHANG ON CHUYENDI.MAKH = KHACHHANG.MAKH WHERE CHUYENDI.MAKH = ?", new String[]{String.valueOf(makh)});
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
                loaiXe = cursor.getString(3);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("TAG", Objects.requireNonNull(e.getMessage()));
        }
        return loaiXe;
    }

    public String getBienSoXe(int makh) {
        String bienSoXe = "";
        try {
            Cursor cursor = this.getReadableDatabase().rawQuery("Select * from CHUYENDI INNER JOIN KHACHHANG ON CHUYENDI.MAKH = KHACHHANG.MAKH WHERE CHUYENDI.MAKH = ?", new String[]{String.valueOf(makh)});
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
                bienSoXe = cursor.getString(4);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("TAG", Objects.requireNonNull(e.getMessage()));
        }
        return bienSoXe;
    }

    public double getTongThoiGian(int makh) {
        double tongthoigian = 0;
        try {
            Cursor cursor = this.getReadableDatabase().rawQuery("Select * from CHUYENDI INNER JOIN KHACHHANG ON CHUYENDI.MAKH = KHACHHANG.MAKH WHERE CHUYENDI.MAKH = ?", new String[]{String.valueOf(makh)});
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
                tongthoigian = cursor.getDouble(7);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("TAG", Objects.requireNonNull(e.getMessage()));
        }
        return tongthoigian;
    }

    public boolean updateTongThoiGian(double thoigian, int makh) {
        try {
            Cursor cursor = this.getReadableDatabase().rawQuery("Select * from CHUYENDI " + "INNER JOIN KHACHHANG ON CHUYENDI.MAKH = KHACHHANG.MAKH WHERE CHUYENDI.MAKH = ?", new String[]{String.valueOf(makh)});
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
                SQLiteDatabase MyDatabase = this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("TRAMXUATPHAT", cursor.getString(1));
                contentValues.put("DIACHI", cursor.getString(2));
                contentValues.put("LOAIXE", cursor.getString(3));
                contentValues.put("BIENSOXE", cursor.getString(4));
                contentValues.put("KINHDO", cursor.getDouble(5));
                contentValues.put("VIDO", cursor.getDouble(6));
                contentValues.put("TONGTHOIGIAN", thoigian);
                contentValues.put("TONGTIEN", Math.floor((60 / thoigian) * 10000));
                long result = MyDatabase.update("CHUYENDI", contentValues, " MACHUYENDI=? ", new String[]{String.valueOf((cursor.getInt(0)))});
                cursor.close();
                return result != -1;
            }
        } catch (Exception e) {
            Log.e("TAG", Objects.requireNonNull(e.getMessage()));
        }
        return false;
    }

    public double getTongTien(int makh) {
        double tongtien = 0;
        try {
            Cursor cursor = this.getReadableDatabase().rawQuery("Select * from CHUYENDI INNER JOIN KHACHHANG ON CHUYENDI.MAKH = KHACHHANG.MAKH WHERE CHUYENDI.MAKH = ?", new String[]{String.valueOf(makh)});
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
                if (cursor.getString(3).contains("Xe đạp cơ"))
                    tongtien = Math.floor((cursor.getDouble(7) * 10000) / 60);
                else tongtien = Math.floor((cursor.getDouble(7) * 20000) / 60);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("TAG", Objects.requireNonNull(e.getMessage()));
        }
        return tongtien;
    }
}

