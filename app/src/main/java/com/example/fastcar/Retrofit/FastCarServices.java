package com.example.fastcar.Retrofit;

import com.example.fastcar.Model.Bank.BankNameAPI;
import com.example.fastcar.Model.Car;
import com.example.fastcar.Model.FavoriteCar;
import com.example.fastcar.Model.FeedBack;
import com.example.fastcar.Model.HangXe.CarApiResponse;
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.Model.LichSuGiaoDich;
import com.example.fastcar.Model.MauXe.CarModelApiResponse;
import com.example.fastcar.Model.NganHang;
import com.example.fastcar.Model.ResMessage;
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
    @GET("xe/listXe_NotUser/{email}")
    Call<List<Car>> getListCar_NotUser(@Path("email") String email_user,
                                       @Query("TrangThai") int TrangThai,
                                       @Query("DiaChiXe") String diachixe);

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

    // cập nhật gplx
    @Multipart
    @POST("user/upGplx/{email}")
    Call<ResMessage> updateGPLX(@Path("email") String email,
                                @Part List<MultipartBody.Part> HinhAnh_GPLX,
                                @Part MultipartBody.Part So_GPLX,
                                @Part MultipartBody.Part HoTen_GPLX,
                                @Part MultipartBody.Part NgayCap_GPLX,
                                @Part MultipartBody.Part DiaChi_GPLX );

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
    Call<List<HoaDon>> getListHoaDon(@Query("Xe") String id_xe, @Query("TrangThaiHD") String TrangThaiHD);

    // lấy danh sách hoá đơn theo user
    @GET("hoadon/list")
    Call<List<HoaDon>> getListHoaDonUser(@Query("User") String id_user, @Query("TrangThaiHD") String TrangThaiHD);

    // tạo hoá đơn
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("hoadon/create")
    Call<ResMessage> createHoaDon(@Body HoaDon hoaDon);

    // cập nhật trạng thái hoá đơn theo mã HD
    @POST("hoadon/update_trangthaiHD/{maHD}")
    Call<String> updateTrangThaiHD(@Path("maHD") String maHD, @Body HoaDon hoaDon);

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
    @GET("lsgd/list")
    Call<List<LichSuGiaoDich>> getLSGD_ofUser(@Query("User") String idUser);

    // tạo lsgd ( yc rút tiền )
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
    Call<ResMessage>addCarUser(@Part  List<MultipartBody.Part> HinhAnh,
                               @Part List<MultipartBody.Part> DangKy,
                               @Part List<MultipartBody.Part> DangKiem,
                               @Part List<MultipartBody.Part>  BaoHiem,
                               @Part("BKS") RequestBody BKS,
                               @Part("HangXe")RequestBody  hangxe,
                               @Part("MauXe")RequestBody  mauxe,
                               @Part("NSX") RequestBody  nxs,
                               @Part("ChuyenDong") RequestBody  ChuyenDong,
                               @Part("SoGhe")RequestBody  soghe,
                               @Part("LoaiNhienLieu") RequestBody  LNL,
                               @Part("TieuHao") RequestBody  TH,
                               @Part("MoTa") RequestBody  MOTA,
                               @Part("DiaChiXe")RequestBody  diachi,
                               @Part("GiaThue1Ngay") RequestBody  giathue,
                               @Part("ChuSH")RequestBody  id_user);

    // NganHang Model
    // danh sách tài khoản ngân hàng của 1 user
    @GET("nganhang/list/{email}")
    Call<List<NganHang>> getListNH_ofUser(@Path("email") String email);

    // thêm tknh
    @Headers("Content-Type: application/json")
    @POST("nganhang/create")
    Call<ResMessage> createNganHang(@Body NganHang nganHang);

    // xoá tknh
    @DELETE("nganhang/delete/{id}")
    Call<ResMessage> deleteNganHang(@Path("id") String idNganHang);

    // GET banks in VietNam
    // lấy danh sách tên các ngân hàng ở Việt Nam
    @GET("banks")
    Call<BankNameAPI> getListBankVN();
}
