package com.example.fastcar.Retrofit;

import com.example.fastcar.Model.Bank.BankNameAPI;
import com.example.fastcar.Model.Car;
import com.example.fastcar.Model.Distance.DistanceMatrix;
import com.example.fastcar.Model.FavoriteCar;
import com.example.fastcar.Model.FeedBack;
import com.example.fastcar.Model.Geolocation.Geolocation;
import com.example.fastcar.Model.HangXe.CarApiResponse;
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.Model.LichSuGiaoDich;
import com.example.fastcar.Model.MauXe.CarModelApiResponse;
import com.example.fastcar.Model.NganHang;
import com.example.fastcar.Model.Places.Place;
import com.example.fastcar.Model.ResMessage;
import com.example.fastcar.Model.ThongBao;
import com.example.fastcar.Model.User;
import com.example.fastcar.Model.Voucher;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FastCarServices {
    // Car Model URL: xe/
    // danh sách xe
    @GET("xe/list")
    Call<List<Car>> getListCar();

    @GET("xe/findById/{id}")
    Call<Car> getCarByID(@Path("id") String idXe);

    // danh sách xe của user đang login
    @GET("xe/listXe_user/{email}")
    Call<List<Car>> getListCar_ofUser(@Path("email") String email_user, @Query("TrangThai") String TrangThai);

    // danh sách xe không phải của user đang login
    @Headers("Content-Type: application/json; charset=utf-8")
    @GET("xe/listXe_NotUser/{email}")
    Call<List<Car>> getListCar_NotUser(@Path("email") String email_user,
                                       @Query("TrangThai") int TrangThai,
                                       @Query("HangXe") String HangXe,
                                       @Query("TheChap") String TheChap,
                                       @Query("ChuyenDong") String ChuyenDong,
                                       @Query("LoaiNhienLieu") String LoaiNhienLieu,
                                       @Query("TrungBinhSao") String TrungBinhSao,
                                       @Query("priceFrom") String priceFrom,
                                       @Query("priceTo") String priceTo,
                                       @Query("soGheFrom") String soGheFrom,
                                       @Query("soGheTo") String soGheTo,
                                       @Query("yearFrom") String yearFrom,
                                       @Query("yearTo") String yearTo
    );

    // top 5 xe có số chuyến nhiều nhất
    @GET("xe/top5xe/{email}")
    Call<List<Car>> getListTop5Car(@Path("email") String email_user,
                                   @Query("TrangThai") int TrangThai);

    // cập nhật 1 số thông tin của xe: số chuyến, số sao
    @PUT("xe/update/{id}")
    Call<ResMessage> updateXe(@Path("id") String id_xe, @Body Car car);

    // cập nhật trạng thái xe
    @PUT("xe/updateTrangThaiXe/{id}")
    Call<ResMessage> updateTrangThaiXe(@Path("id") String id_xe, @Body Car car);

    // xoá xe
    @DELETE("xe/delete/{id}")
    Call<ResMessage> deleteXe(@Path("id") String id_xe);

    // User Model URL: user/
    // get user
    @GET("user/list")
    Call<List<User>> getListUser(@Query("Email") String email);

    // thêm user
    @Headers("Content-Type: application/json")
    @POST("user/create")
    Call<ResMessage> addNewUser(@Body User user);

    // cập nhật thông tin user
    @Headers("Content-Type: application/json")
    @PUT("user/updateUser/{email}")
    Call<ResMessage> updateUser(@Path("email") String email, @Body User user);

    // cập nhật lại số dư
    @Headers("Content-Type: application/json")
    @PUT("user/updateSoDu/{email}")
    Call<ResMessage> updateSoDu(@Path("email") String email, @Body User user);

    // cập nhật số lượng thông báo đã đọc
    @Headers("Content-Type: application/json")
    @PUT("user/updateNumberNotifyRead/{email}")
    Call<ResMessage> updateReadNotify(@Path("email") String email, @Body User user);

    // cập nhật gplx
    @Multipart
    @POST("user/upGplx/{email}")
    Call<ResMessage> updateGPLX(@Path("email") String email,
                                @Part List<MultipartBody.Part> HinhAnh_GPLX,
                                @Part MultipartBody.Part So_GPLX,
                                @Part MultipartBody.Part HoTen_GPLX,
                                @Part MultipartBody.Part NgayCap_GPLX,
                                @Part MultipartBody.Part DiaChi_GPLX);

    // đăng xuất
    @Headers("Content-Type: application/json")
    @POST("user/logout/{email}")
    Call<ResMessage> logout(@Path("email") String emailUser);

    // Voucher Model URL: voucher/
    // danh sách mã giảm giá/ mã khuyến mãi
    @GET("voucher/list")
    Call<List<Voucher>> getListVoucher();

    // HoaDon model URL: hoadon/

    // lấy danh sách hoá đơn theo xe
    @GET("hoadon/list")
    Call<List<HoaDon>> getListHoaDon_byCar(@Query("Xe") String id_xe);

    // lấy danh sách hoá đơn theo xe + trạng thái hoá đơn để check có bị trùng lịch hay không
    @GET("hoadon/list")
    Call<List<HoaDon>> getListHoaDon(@Query("Xe") String id_xe,
                                     @Query("TrangThaiHD") String TrangThaiHD,
                                     @Query("startDate") String NgayThue,
                                     @Query("endDate") String NgayTra);

    // lấy danh sách hoá đơn theo user
    @GET("hoadon/list")
    Call<List<HoaDon>> getListHoaDonUser(@Query("User") String id_user, @Query("TrangThaiHD") String TrangThaiHD);

    // lấy hoá đơn theo mahd
    @GET("hoadon/list")
    Call<List<HoaDon>> getHoaDonbyMaHD(@Query("MaHD") String maHD);

    // tạo hoá đơn
    @Headers("Content-Type: application/json")
    @POST("hoadon/create")
    Call<ResMessage> createHoaDon(@Body HoaDon hoaDon);

    // cập nhật trạng thái hoá đơn theo mã HD
    @Headers("Content-Type: application/json")
    @POST("hoadon/update_trangthaiHD/{maHD}")
    Call<String> updateTrangThaiHD(@Path("maHD") String maHD, @Body HoaDon hoaDon);

    // update thời gian chủ xe xác nhận đồng ý cho thuê
    @Headers("Content-Type: application/json")
    @POST("hoadon/update_timeXNHD/{maHD}")
    Call<ResMessage> updateTimeXNHD(@Path("maHD") String maHD, @Body HoaDon hoaDon);

    // update ảnh chủ xe giao xe thành công

    @Multipart
    @POST("hoadon/update_hinhAnhGiaoXe/{maHD}")
    Call<ResMessage> updateHinhAnh_ChuXeGiaoXe(@Path("maHD") String maHD,
                                               @Part List<MultipartBody.Part> HinhAnhChuXeGiaoXe);

    // update ảnh khách hàng trả xe thành công
    @Multipart
    @POST("hoadon/update_hinhAnhTraXe/{maHD}")
    Call<ResMessage> updateHinhAnh_KhachHangTraXe(@Path("maHD") String maHD,
                                                  @Part List<MultipartBody.Part> HinhAnhKhachHangTraXe);


    // Feedback model URL: feedback/

    // danh sách đánh giá của 1 xe
    @GET("feedback/list")
    Call<List<FeedBack>> getListFeedBack(@Query("Xe") String id_xe);

    // thêm đánh giá
    @POST("feedback/create")
    Call<ResMessage> createFeedBack(@Body FeedBack feedBack);

    // Favorite Car model

    // lấy danh sách xe yêu thích của 1 user
    @GET("favoriteCar/list/{userId}")
    Call<List<Car>> getListFavoriteCar(@Path("userId") String id_user);

    // tìm xem idXe truyền vào params có được user đó lưu vào ds yêu thích hay không, để hiển thị icon love, nolove tương ứng
    @GET("favoriteCar/find/{userId}/{carId}")
    Call<List<Car>> findFavoriteCar(@Path("userId") String id_user, @Path("carId") String id_car);

    // thêm xe yêu thích
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("favoriteCar/add")
    Call<ResMessage> addFavoriteCar(@Body FavoriteCar favoriteCar);

    // xoá
    @Headers("Content-Type: application/json; charset=utf-8")
    @DELETE("favoriteCar/delete/{userId}/{carId}")
    Call<ResMessage> deleteFavoriteCar(
            @Path("userId") String userId, @Path("carId") String carId);

    // model LSGD
    // lấy lsgd của 1 user
    @GET("lsgd/list/{email}")
    Call<List<LichSuGiaoDich>> getLSGD_ofUser(@Path("email") String emailUser,
                                              @Query("title") String title,
                                              @Query("startDate") String startDate);

    // tạo lsgd ( yc rút tiền )
    @Headers("Content-Type: application/json")
    @POST("lsgd/create")
    Call<ResMessage> createLSGD(@Body LichSuGiaoDich lichSuGiaoDich);

    // lấy danh sách hãng xe
    @GET("makes")
    Call<CarApiResponse> getListHang();

    // danh sách mẫu xe
    @GET("models")
    Call<CarModelApiResponse> getListModel(@Query("make_id") int make_id, @Query("year") int year);

    // thêm xe mới theo user
    @Multipart
    @POST("xe/create")
    Call<ResMessage> addCarUser(@Part List<MultipartBody.Part> HinhAnh,
                                @Part List<MultipartBody.Part> DangKy,
                                @Part List<MultipartBody.Part> DangKiem,
                                @Part List<MultipartBody.Part> BaoHiem,
                                @Part MultipartBody.Part BKS,
                                @Part MultipartBody.Part HangXe,
                                @Part MultipartBody.Part MauXe,
                                @Part MultipartBody.Part NSX,
                                @Part MultipartBody.Part ChuyenDong,
                                @Part MultipartBody.Part SoGhe,
                                @Part MultipartBody.Part LoaiNhienLieu,
                                @Part MultipartBody.Part TieuHao,
                                @Part MultipartBody.Part MoTa,
                                @Part MultipartBody.Part DiaChiXe,
                                @Part MultipartBody.Part Latitude,
                                @Part MultipartBody.Part Longitude,
                                @Part MultipartBody.Part GiaThue1Ngay,
                                @Part MultipartBody.Part TheChap,
                                @Part MultipartBody.Part ThoiGianGiaoXe,
                                @Part MultipartBody.Part ThoiGianNhanXe,
                                @Part MultipartBody.Part ChuSH
    );

    // NganHang Model
    // danh sách tài khoản ngân hàng của 1 user
    @GET("nganhang/list/{email}")
    Call<List<NganHang>> getListNH_ofUser(@Path("email") String email);

    // thêm tknh
    @Headers("Content-Type: application/json")
    @POST("nganhang/create")
    Call<ResMessage> createNganHang(@Body NganHang nganHang);

    // sửa tknh
    @Headers("Content-Type: application/json")
    @PUT("nganhang/update/{id}")
    Call<ResMessage> updateNganHang(@Path("id") String idBank,
                                    @Body NganHang nganHang);

    // xoá tknh
    @DELETE("nganhang/delete/{id}")
    Call<ResMessage> deleteNganHang(@Path("id") String idBank);

    // GET banks in VietNam
    // lấy danh sách tên các ngân hàng ở Việt Nam
    @GET("banks")
    Call<BankNameAPI> getListBankVN();


    // autocomplete location
    @GET("Place/AutoComplete")
    Call<Place> getDiaDiem(@Query("api_key") String api_key,
                           @Query("location") String location,
                           @Query("input") String input,
                           @Query("radius") int radius,
                           @Query("more_compound") boolean more_compound);

    // detail location, get geolocation (longitude, latitude)
    @GET("Place/Detail")
    Call<Geolocation> getGeolocation(@Query("place_id") String place_id,
                                     @Query("api_key") String api_key);

    // get the distance between 2 cars using geolocation (longitude, latitude)
    @GET("DistanceMatrix")
    Call<DistanceMatrix> getDistance(@Query("origins") String origins,
                                     @Query("destinations") String destinations,
                                     @Query("vehicle") String vehicle,
                                     @Query("api_key") String api_key);

    //Thông báo
    @GET("thongbao/list")
    Call<List<ThongBao>> getThongbao(@Query("user") String id_user, @Query("start_date") String date);
}
