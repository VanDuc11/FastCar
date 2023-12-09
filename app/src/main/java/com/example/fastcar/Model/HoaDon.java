package com.example.fastcar.Model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;

public class HoaDon implements Parcelable {
    String MaHD;
    User User;
    Car Xe;
    Date NgayThue, NgayTra;
    int TongSoNgayThue, PhiDV;
    String MaGiamGia;
    int GiamGia, PhuPhi, TongTien, TienCoc, TienCocGoc, ThanhToan, TrangThaiHD;
    Date GioTaoHD, TimeChuXeXN;
    List<String> HinhAnhChuXeGiaoXe, HinhAnhKhachHangTraXe;
    String LyDo;
    String LoiNhan;
    boolean HaveFeedback;

    public HoaDon(String maHD, User user, Car xe, Date ngayThue, Date ngayTra, int tongSoNgayThue, int phiDV, String maGiamGia, int giamGia, int phuPhi, int tongTien, int tienCoc, int tienCocGoc, int thanhToan, String loiNhan, Date gioTaoHD, Date timeChuXeXN, List<String> hinhAnhChuXeGiaoXe, List<String> hinhAnhKhachHangTraXe, int trangThaiHD, String lyDo, boolean haveFeedback) {
        MaHD = maHD;
        User = user;
        Xe = xe;
        NgayThue = ngayThue;
        NgayTra = ngayTra;
        TongSoNgayThue = tongSoNgayThue;
        PhiDV = phiDV;
        MaGiamGia = maGiamGia;
        GiamGia = giamGia;
        PhuPhi = phuPhi;
        TongTien = tongTien;
        TienCoc = tienCoc;
        TienCocGoc = tienCocGoc;
        ThanhToan = thanhToan;
        LoiNhan = loiNhan;
        GioTaoHD = gioTaoHD;
        TimeChuXeXN = timeChuXeXN;
        HinhAnhChuXeGiaoXe = hinhAnhChuXeGiaoXe;
        HinhAnhKhachHangTraXe = hinhAnhKhachHangTraXe;
        TrangThaiHD = trangThaiHD;
        LyDo = lyDo;
        HaveFeedback = haveFeedback;
    }

    public HoaDon() {
    }

    @SuppressLint("NewApi")
    protected HoaDon(Parcel in) {
        MaHD = in.readString();
        User = in.readParcelable(User.class.getClassLoader());
        Xe = in.readParcelable(Car.class.getClassLoader());
        NgayThue = new Date(in.readLong());
        NgayTra = new Date(in.readLong());
        TongSoNgayThue = in.readInt();
        PhiDV = in.readInt();
        MaGiamGia = in.readString();
        GiamGia = in.readInt();
        PhuPhi = in.readInt();
        TongTien = in.readInt();
        TienCoc = in.readInt();
        TienCocGoc = in.readInt();
        ThanhToan = in.readInt();
        LoiNhan = in.readString();
        GioTaoHD = new Date(in.readLong());
        HinhAnhChuXeGiaoXe = in.createStringArrayList();
        HinhAnhKhachHangTraXe = in.createStringArrayList();
        TrangThaiHD = in.readInt();
        LyDo = in.readString();
        HaveFeedback = in.readBoolean();
    }

    public static final Creator<HoaDon> CREATOR = new Creator<HoaDon>() {
        @Override
        public HoaDon createFromParcel(Parcel in) {
            return new HoaDon(in);
        }

        @Override
        public HoaDon[] newArray(int size) {
            return new HoaDon[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @SuppressLint("NewApi")
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(MaHD);
        parcel.writeParcelable(User, i);
        parcel.writeParcelable(Xe, i);
        parcel.writeLong(NgayThue.getTime());
        parcel.writeLong(NgayTra.getTime());
        parcel.writeInt(TongSoNgayThue);
        parcel.writeInt(PhiDV);
        parcel.writeString(MaGiamGia);
        parcel.writeInt(GiamGia);
        parcel.writeInt(PhuPhi);
        parcel.writeInt(TongTien);
        parcel.writeInt(TienCoc);
        parcel.writeInt(TienCocGoc);
        parcel.writeInt(ThanhToan);
        parcel.writeString(LoiNhan);
        parcel.writeLong(GioTaoHD.getTime());
        parcel.writeStringList(HinhAnhChuXeGiaoXe);
        parcel.writeStringList(HinhAnhKhachHangTraXe);
        parcel.writeInt(TrangThaiHD);
        parcel.writeString(LyDo);
        parcel.writeBoolean(HaveFeedback);
    }

    public String getMaHD() {
        return MaHD;
    }

    public void setMaHD(String maHD) {
        MaHD = maHD;
    }

    public com.example.fastcar.Model.User getUser() {
        return User;
    }

    public void setUser(com.example.fastcar.Model.User user) {
        User = user;
    }

    public Car getXe() {
        return Xe;
    }

    public void setXe(Car xe) {
        Xe = xe;
    }

    public Date getNgayThue() {
        return NgayThue;
    }

    public void setNgayThue(Date ngayThue) {
        NgayThue = ngayThue;
    }

    public Date getNgayTra() {
        return NgayTra;
    }

    public void setNgayTra(Date ngayTra) {
        NgayTra = ngayTra;
    }

    public int getPhiDV() {
        return PhiDV;
    }

    public void setPhiDV(int phiDV) {
        PhiDV = phiDV;
    }

    public String getMaGiamGia() {
        return MaGiamGia;
    }

    public void setMaGiamGia(String maGiamGia) {
        MaGiamGia = maGiamGia;
    }

    public int getGiamGia() {
        return GiamGia;
    }

    public void setGiamGia(int giamGia) {
        GiamGia = giamGia;
    }

    public int getPhuPhi() {
        return PhuPhi;
    }

    public void setPhuPhi(int phuPhi) {
        PhuPhi = phuPhi;
    }

    public int getTongTien() {
        return TongTien;
    }

    public void setTongTien(int tongTien) {
        TongTien = tongTien;
    }

    public int getTienCoc() {
        return TienCoc;
    }

    public void setTienCoc(int tienCoc) {
        TienCoc = tienCoc;
    }

    public int getTienCocGoc() {
        return TienCocGoc;
    }

    public void setTienCocGoc(int tienCocGoc) {
        TienCocGoc = tienCocGoc;
    }

    public int getThanhToan() {
        return ThanhToan;
    }

    public void setThanhToan(int thanhToan) {
        ThanhToan = thanhToan;
    }

    public int getTrangThaiHD() {
        return TrangThaiHD;
    }

    public void setTrangThaiHD(int trangThaiHD) {
        TrangThaiHD = trangThaiHD;
    }

    public Date getGioTaoHD() {
        return GioTaoHD;
    }

    public void setGioTaoHD(Date gioTaoHD) {
        GioTaoHD = gioTaoHD;
    }

    public Date getTimeChuXeXN() {
        return TimeChuXeXN;
    }

    public void setTimeChuXeXN(Date timeChuXeXN) {
        TimeChuXeXN = timeChuXeXN;
    }

    public int getTongSoNgayThue() {
        return TongSoNgayThue;
    }

    public void setTongSoNgayThue(int tongSoNgayThue) {
        TongSoNgayThue = tongSoNgayThue;
    }

    public String getLyDo() {
        return LyDo;
    }

    public void setLyDo(String lyDo) {
        LyDo = lyDo;
    }

    public String getLoiNhan() {
        return LoiNhan;
    }

    public void setLoiNhan(String loiNhan) {
        LoiNhan = loiNhan;
    }

    public boolean isHaveFeedback() {
        return HaveFeedback;
    }

    public void setHaveFeedback(boolean haveFeedback) {
        HaveFeedback = haveFeedback;
    }

    public List<String> getHinhAnhChuXeGiaoXe() {
        return HinhAnhChuXeGiaoXe;
    }

    public void setHinhAnhChuXeGiaoXe(List<String> hinhAnhChuXeGiaoXe) {
        HinhAnhChuXeGiaoXe = hinhAnhChuXeGiaoXe;
    }

    public List<String> getHinhAnhKhachHangTraXe() {
        return HinhAnhKhachHangTraXe;
    }

    public void setHinhAnhKhachHangTraXe(List<String> hinhAnhKhachHangTraXe) {
        HinhAnhKhachHangTraXe = hinhAnhKhachHangTraXe;
    }
}
