package com.example.fastcar.Model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class Car implements Parcelable {
    String _id, BKS, HangXe, MauXe, NSX;
    int SoGhe;
    String ChuyenDong, LoaiNhienLieu;
    float TieuHao;
    String MoTa;
    ArrayList<String> HinhAnh;
    String DangKyXe, DangKiem, BaoHiem;
    String DiaChiXe, Longitude, Latitude;
    int GiaThue1Ngay;
    boolean TheChap;
    String ThoiGianGiaoXe, ThoiGianNhanXe;
    User ChuSH;
    int TrangThai, SoChuyen;
    float TrungBinhSao;
    List<String> LichBan;
    private double distance;

    public Car(String _id, String BKS, String hangXe, String mauXe, String NSX, int soGhe, String chuyenDong, String loaiNhienLieu, float tieuHao, String moTa, ArrayList<String> hinhAnh, String dangKyXe, String dangKiem, String baoHiem, String diaChiXe, String longitude, String latitude, int giaThue1Ngay, boolean theChap, String thoiGianGiaoXe, String thoiGianNhanXe, User chuSH, int trangThai, int soChuyen, float trungBinhSao, List<String> lichBan) {
        this._id = _id;
        this.BKS = BKS;
        this.HangXe = hangXe;
        this.MauXe = mauXe;
        this.NSX = NSX;
        this.SoGhe = soGhe;
        this.ChuyenDong = chuyenDong;
        this.LoaiNhienLieu = loaiNhienLieu;
        this.TieuHao = tieuHao;
        this.MoTa = moTa;
        this.HinhAnh = hinhAnh;
        this.DangKyXe = dangKyXe;
        this.DangKiem = dangKiem;
        this.BaoHiem = baoHiem;
        this.DiaChiXe = diaChiXe;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.GiaThue1Ngay = giaThue1Ngay;
        this.TheChap = theChap;
        this.ThoiGianGiaoXe = thoiGianGiaoXe;
        this.ThoiGianNhanXe = thoiGianNhanXe;
        this.ChuSH = chuSH;
        this.TrangThai = trangThai;
        this.SoChuyen = soChuyen;
        this.TrungBinhSao = trungBinhSao;
        this.LichBan = lichBan;
    }


    public Car() {
    }

    protected Car(Parcel in) {
        _id = in.readString();
        BKS = in.readString();
        HangXe = in.readString();
        MauXe = in.readString();
        NSX = in.readString();
        SoGhe = in.readInt();
        ChuyenDong = in.readString();
        LoaiNhienLieu = in.readString();
        TieuHao = in.readFloat();
        MoTa = in.readString();
        HinhAnh = in.createStringArrayList();
        DangKyXe = in.readString();
        DangKiem = in.readString();
        BaoHiem = in.readString();
        DiaChiXe = in.readString();
        Latitude = in.readString();
        Longitude = in.readString();
        GiaThue1Ngay = in.readInt();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            TheChap = in.readBoolean();
        }
        ThoiGianGiaoXe = in.readString();
        ThoiGianNhanXe= in.readString();
        ChuSH = in.readParcelable(User.class.getClassLoader());
        TrangThai = in.readInt();
        SoChuyen = in.readInt();
        TrungBinhSao = in.readFloat();
        LichBan = in.createStringArrayList();
    }

    public static final Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

    public String getBKS() {
        return BKS;
    }

    public void setBKS(String BKS) {
        this.BKS = BKS;
    }

    public String getHangXe() {
        return HangXe;
    }

    public void setHangXe(String hangXe) {
        HangXe = hangXe;
    }

    public String getMauXe() {
        return MauXe;
    }

    public void setMauXe(String mauXe) {
        MauXe = mauXe;
    }

    public String getNSX() {
        return NSX;
    }

    public void setNSX(String NSX) {
        this.NSX = NSX;
    }

    public int getSoGhe() {
        return SoGhe;
    }

    public void setSoGhe(int soGhe) {
        SoGhe = soGhe;
    }

    public String getChuyenDong() {
        return ChuyenDong;
    }

    public void setChuyenDong(String chuyenDong) {
        ChuyenDong = chuyenDong;
    }

    public String getLoaiNhienLieu() {
        return LoaiNhienLieu;
    }

    public void setLoaiNhienLieu(String loaiNhienLieu) {
        LoaiNhienLieu = loaiNhienLieu;
    }

    public float getTieuHao() {
        return TieuHao;
    }

    public void setTieuHao(float tieuHao) {
        TieuHao = tieuHao;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        MoTa = moTa;
    }

    public ArrayList<String> getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(ArrayList<String> hinhAnh) {
        HinhAnh = hinhAnh;
    }


    public int getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(int trangThai) {
        TrangThai = trangThai;
    }

    public int getSoChuyen() {
        return SoChuyen;
    }

    public void setSoChuyen(int soChuyen) {
        SoChuyen = soChuyen;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public User getChuSH() {
        return ChuSH;
    }

    public void setChuSH(User chuSH) {
        ChuSH = chuSH;
    }

    public String getDiaChiXe() {
        return DiaChiXe;
    }

    public void setDiaChiXe(String diaChiXe) {
        DiaChiXe = diaChiXe;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public int getGiaThue1Ngay() {
        return GiaThue1Ngay;
    }

    public void setGiaThue1Ngay(int giaThue1Ngay) {
        GiaThue1Ngay = giaThue1Ngay;
    }

    public boolean getTheChap() {
        return TheChap;
    }

    public void setTheChap(boolean theChap) {
        TheChap = theChap;
    }

    public String getThoiGianGiaoXe() {
        return ThoiGianGiaoXe;
    }

    public void setThoiGianGiaoXe(String thoiGianGiaoXe) {
        ThoiGianGiaoXe = thoiGianGiaoXe;
    }

    public String getThoiGianNhanXe() {
        return ThoiGianNhanXe;
    }

    public void setThoiGianNhanXe(String thoiGianNhanXe) {
        ThoiGianNhanXe = thoiGianNhanXe;
    }

    public float getTrungBinhSao() {
        return TrungBinhSao;
    }

    public void setTrungBinhSao(float trungBinhSao) {
        TrungBinhSao = trungBinhSao;
    }

    public String getDangKyXe() {
        return DangKyXe;
    }

    public void setDangKyXe(String dangKyXe) {
        DangKyXe = dangKyXe;
    }

    public String getDangKiem() {
        return DangKiem;
    }

    public void setDangKiem(String dangKiem) {
        DangKiem = dangKiem;
    }

    public String getBaoHiem() {
        return BaoHiem;
    }

    public void setBaoHiem(String baoHiem) {
        BaoHiem = baoHiem;
    }

    public List<String> getLichBan() {
        return LichBan;
    }

    public void setLichBan(List<String> lichBan) {
        LichBan = lichBan;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(BKS);
        parcel.writeString(HangXe);
        parcel.writeString(MauXe);
        parcel.writeString(NSX);
        parcel.writeInt(SoGhe);
        parcel.writeString(ChuyenDong);
        parcel.writeString(LoaiNhienLieu);
        parcel.writeFloat(TieuHao);
        parcel.writeString(MoTa);
        parcel.writeStringList(HinhAnh);
        parcel.writeString(DangKyXe);
        parcel.writeString(DangKiem);
        parcel.writeString(BaoHiem);
        parcel.writeString(DiaChiXe);
        parcel.writeString(Latitude);
        parcel.writeString(Longitude);
        parcel.writeInt(GiaThue1Ngay);
        parcel.writeBoolean(TheChap);
        parcel.writeString(ThoiGianGiaoXe);
        parcel.writeString(ThoiGianNhanXe);
        parcel.writeParcelable(ChuSH, i);
        parcel.writeInt(TrangThai);
        parcel.writeInt(SoChuyen);
        parcel.writeFloat(TrungBinhSao);
        parcel.writeStringList(LichBan);
    }
}
