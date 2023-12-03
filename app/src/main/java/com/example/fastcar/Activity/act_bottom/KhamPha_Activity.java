package com.example.fastcar.Activity.act_bottom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fastcar.Activity.DanhSachXe_Activity;
import com.example.fastcar.Activity.Login_Activity;
import com.example.fastcar.Activity.ThongBao_Activity;
import com.example.fastcar.Adapter.PlacesAdapter;
import com.example.fastcar.Adapter.VoucherAdapter;
import com.example.fastcar.Adapter.XeKhamPhaAdapter;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Model.Car;
import com.example.fastcar.Model.Geolocation.Geolocation;
import com.example.fastcar.Model.Places.Place;
import com.example.fastcar.Model.ResMessage;
import com.example.fastcar.Model.User;
import com.example.fastcar.Model.Voucher;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.example.fastcar.Server.HostApi;
import com.example.fastcar.User_Method;
import com.facebook.login.LoginManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KhamPha_Activity extends AppCompatActivity {
    TextView tvName, btnTim, tvTime_inKhamPha, tvDiaDiem;
    RecyclerView recyclerView_khuyenmai, recyclerView_xeKhamPha;
    CircleImageView img_user;
    private FirebaseAuth auth;
    ShimmerFrameLayout shimmer_view;
    LinearLayout data_view;
    private FirebaseUser fBaseuser;
    FirebaseDatabase database;
    MaterialDatePicker<Pair<Long, Long>> datePicker;
    Long todayInMillis, tomorrowInMillis;
    private boolean isChooseDatePicker = false;
    ImageView img_notify;
    Dialog dialogDiaChi;
    FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 100;
    String tokenFCM;
    private VoucherAdapter adapterVoucher;
    private PlacesAdapter adapter;
    private ProgressBar progressBar;
    private EditText edt_diachi;
    private String place_id, longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kham_pha);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        fBaseuser = auth.getCurrentUser();

        mapping();
        load();
        build_DatePicker();
        setDefault_SelectionDate();
        getTokenFCM();
        Save();
        loadXeKhamPha();

        btnTim.setOnClickListener(view -> {
            String diachi = tvDiaDiem.getText().toString();
            String time = tvTime_inKhamPha.getText().toString();
            if (!diachi.isEmpty()) {
                SharedPreferences preferences = getSharedPreferences("diachiXe", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("diachi", diachi);
                editor.putString("time", time);
                editor.putString("lat", latitude);
                editor.putString("long", longitude);
                editor.apply();

                Intent intent = new Intent(getBaseContext(), DanhSachXe_Activity.class);
                startActivity(intent);
            } else {
                CustomDialogNotify.showToastCustom(KhamPha_Activity.this, "Vui lòng nhập địa chỉ để tìm xe");
            }

        });

        SharedPreferences preferences = getSharedPreferences("timePicker", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("startTime1", todayInMillis);
        editor.putLong("endTime1", tomorrowInMillis);
        editor.putBoolean("check", isChooseDatePicker);
        editor.apply();

    }

    void mapping() {
        tvName = findViewById(R.id.tvName);
        btnTim = findViewById(R.id.act_khamha_tvTimXe);
        tvTime_inKhamPha = findViewById(R.id.tvTime_inKhamPha);
        tvDiaDiem = findViewById(R.id.tv_diadiem_inKhamPha);
        recyclerView_khuyenmai = findViewById(R.id.recyclerView_khuyenmai);
        img_user = findViewById(R.id.img_user_inKhamPha);
        recyclerView_xeKhamPha = findViewById(R.id.recyclerView_XeKhamPha);
        img_notify = findViewById(R.id.ic_notify);
        data_view = findViewById(R.id.data_view_inXeKhamPha);
        shimmer_view = findViewById(R.id.shimmer_view_inXeKhamPha);
    }

    void load() {
        recyclerView_khuyenmai.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        tokenFCM = "";
        Intent intent = getIntent();
        boolean isIcon = intent.getBooleanExtra("SHOW_ICON_ADD", false);
        RetrofitClient.FC_services().getListVoucher().enqueue(new Callback<List<Voucher>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<Voucher>> call, Response<List<Voucher>> response) {
                if (response.code() == 200) {
                    if (!response.body().isEmpty()) {
                        adapterVoucher = new VoucherAdapter(KhamPha_Activity.this, response.body(), isIcon, 0);
                        recyclerView_khuyenmai.setAdapter(adapterVoucher);
                        adapterVoucher.notifyDataSetChanged();
                    } else {
                    }
                } else {
                    System.out.println("Có lỗi khi get list voucher: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Voucher>> call, Throwable t) {
                System.out.println("Có lỗi khi get list voucher: " + t);
            }
        });
        recyclerView_khuyenmai.setAdapter(adapterVoucher);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView_khuyenmai);

        img_notify.setOnClickListener(view -> startActivity(new Intent(KhamPha_Activity.this, ThongBao_Activity.class)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (fBaseuser == null) {
            signOut();
        }
    }

    private void getTokenFCM() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            tokenFCM = task.getResult();
                            System.out.println("TokenFCM: " + tokenFCM);
                            User user = new User(tokenFCM, null);
                            User_Method.func_updateUser(KhamPha_Activity.this, fBaseuser.getEmail(), user, false);
                        } else {
                            System.out.println("Lỗi lấy token FCM: " + task.getException());
                        }
                    }
                });
    }

    void Save() {
        Intent intent = getIntent();
        String personName = fBaseuser.getDisplayName();
        String userName = personName == null ? "Khách hàng" : personName;
        String Email = fBaseuser.getEmail();
        String pass = intent.getStringExtra("pass");
        Uri uri = fBaseuser.getPhotoUrl();

        if (uri != null) {
            Glide.with(getBaseContext()).load(uri).into(img_user);
        } else {
            uri = Uri.parse("https://cdn.landesa.org/wp-content/uploads/default-user-image.png");
            Glide.with(getBaseContext()).load(R.drawable.img_avatar_user_v1).into(img_user);
        }

        tvName.setText(userName);
        if (pass == null) {
            pass = "";
        }

        String finalPass = pass;
        Uri finalUri = uri;
        Date getTimeNow = new Date();
        User userNew = new User(fBaseuser.getUid(), userName, Email, finalPass, String.valueOf(finalUri), getTimeNow, tokenFCM);

        getUser_fromEmail(Email, userNew);
    }

    void signOut() {
        auth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        startActivity(new Intent(this, Login_Activity.class));
        finish();
    }

    private void getUser_fromEmail(String email, User userNew) {
        RetrofitClient.FC_services().getListUser(email).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, retrofit2.Response<List<User>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (!response.body().isEmpty()) {
                        List<User> list = response.body();
                        Gson gson = new Gson();
                        String json = gson.toJson(list.get(0));

                        SharedPreferences preferences = getSharedPreferences("model_user_login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("user", json);
                        editor.apply();
                        Glide.with(getBaseContext()).load(response.body().get(0).getAvatar()).into(img_user);
                    } else {
                        funcAddNewUser(userNew);
                        Gson gson = new Gson();
                        String json = gson.toJson(userNew);

                        SharedPreferences preferences = getSharedPreferences("model_user_login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("user", json);
                        editor.apply();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println("Có lỗi: " + t);
            }
        });
    }

    private void funcAddNewUser(User user) {
        RetrofitClient.FC_services().addNewUser(user).enqueue(new Callback<ResMessage>() {
            @Override
            public void onResponse(Call<ResMessage> call, retrofit2.Response<ResMessage> response) {
                System.out.println("Thêm người dùng mới thành công");
            }

            @Override
            public void onFailure(Call<ResMessage> call, Throwable t) {
                System.out.println("Có lỗi khi addNewUser " + t);
            }
        });
    }

    public void tab1_to_tab2(View view) {
        startActivity(new Intent(getBaseContext(), ChuyenXe_Activity.class));
    }

    public void tab1_to_tab3(View view) {
        startActivity(new Intent(getBaseContext(), HoTro_Activity.class));
    }

    public void tab1_to_tab4(View view) {
        startActivity(new Intent(getBaseContext(), CaNhan_Activity.class));
    }

    private void build_DatePicker() {
        Calendar today = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);

        todayInMillis = today.getTimeInMillis();
        tomorrowInMillis = tomorrow.getTimeInMillis();

        datePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setSelection(Pair.create(todayInMillis, tomorrowInMillis))
                .setCalendarConstraints(buildCalendarConstraints())
                .build();
    }

    public void showDatePicker(View view) {
        datePicker.show(getSupportFragmentManager(), "Material_Range");

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                // Lấy ngày bắt đầu và ngày kết thúc từ Pair
                Long startDate = selection.first;
                Long endDate = selection.second;

                isChooseDatePicker = true;

                SharedPreferences preferences = getSharedPreferences("timePicker", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putLong("startTime2", startDate);
                editor.putLong("endTime2", endDate);
                editor.putBoolean("check", isChooseDatePicker);
                editor.apply();


                // Định dạng ngày tháng theo định dạng tiếng Việt
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String formattedStartDate = sdf.format(new Date(startDate));
                String formattedEndDate = sdf.format(new Date(endDate));

                String resultText = formattedStartDate + " - " + formattedEndDate;
                tvTime_inKhamPha.setText(resultText);
            }
        });
    }

    private void setDefault_SelectionDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedToday = sdf.format(todayInMillis);
        String formattedTomorrow = sdf.format(tomorrowInMillis);

        String defaultText = formattedToday + " - " + formattedTomorrow;
        tvTime_inKhamPha.setText(defaultText);
    }

    private CalendarConstraints buildCalendarConstraints() {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();

        // Chỉ cho phép chọn từ ngày hiện tại trở đi
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        constraintsBuilder.setStart(today);

        // Vô hiệu hóa ngày trong quá khứ bằng cách sử dụng setValidator
        // Các ngày không hợp lệ (trong quá khứ) sẽ không thể chọn được
        constraintsBuilder.setValidator(new CalendarConstraints.DateValidator() {
            @Override
            public boolean isValid(long date) {
                return date >= today;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
            }
        });

        return constraintsBuilder.build();
    }

    @Override
    public void onBackPressed() {
    }

    private void loadXeKhamPha() {
        String Email = fBaseuser.getEmail();

        data_view.setVisibility(View.GONE);
        shimmer_view.setVisibility(View.VISIBLE);
        shimmer_view.startShimmerAnimation();

        recyclerView_xeKhamPha.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        RetrofitClient.FC_services().getListTop5Car(Email, 1).enqueue(new Callback<List<Car>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<Car>> call, retrofit2.Response<List<Car>> response) {
                data_view.setVisibility(View.VISIBLE);
                shimmer_view.stopShimmerAnimation();
                shimmer_view.setVisibility(View.GONE);

                if (response.code() == 200) {
                    XeKhamPhaAdapter adapter = new XeKhamPhaAdapter(KhamPha_Activity.this, response.body());
                    recyclerView_xeKhamPha.setAdapter(adapter);
                    SnapHelper snapHelper = new PagerSnapHelper();
                    snapHelper.attachToRecyclerView(recyclerView_xeKhamPha);
                    adapter.notifyDataSetChanged();
                } else {
                    System.out.println("Không có dữ liệu phù hợp in getListTop5Car()");
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                System.out.println("Có lỗi khi thực hiện: " + t);
            }
        });
    }

    public void showDialog_DiaDiem(View view) {
        LayoutInflater inflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.layout_chon_diadiem_thuexe, null);
        dialogDiaChi = new Dialog(this);
        dialogDiaChi.setContentView(custom);
        dialogDiaChi.setCanceledOnTouchOutside(false);

        Window window = dialogDiaChi.getWindow();
        if (window == null) {
            return;
        }
        // set kích thước dialog
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set vị trí dialog
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialogDiaChi.show();
        btnTim.setEnabled(false);
        btnTim.setBackgroundResource(R.drawable.disable_custom_btn4);

        ImageView btn_back = dialogDiaChi.findViewById(R.id.icon_back_in_chon_diadiem_thueXe);
        TextView getDiaChiHienTai = dialogDiaChi.findViewById(R.id.btn_getVitri_hientai);
        edt_diachi = dialogDiaChi.findViewById(R.id.edt_diadiem_inDialog);
        progressBar = dialogDiaChi.findViewById(R.id.progressBar);
        ListView listPlaces = dialogDiaChi.findViewById(R.id.listPlaces);
        adapter = new PlacesAdapter(this);

        // back
        btn_back.setOnClickListener(view1 -> dialogDiaChi.dismiss());

        // get vị trí hiện tại
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getDiaChiHienTai.setOnClickListener(view1 -> getLocation(dialogDiaChi));

        progressBar.setVisibility(View.GONE);
        adapter = new PlacesAdapter(this);
        listPlaces.setAdapter(adapter);


        listPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapter.getCount() > 0) {
                    Place.Predictions predictions = (Place.Predictions) adapter.getItem(i);
                    callBackDialog(predictions.getDescription());
                    place_id = predictions.getPlace_id();
                    getGeolocation(place_id);
                    dialogDiaChi.dismiss();
                }
            }
        });

        edt_diachi.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    if (edt_diachi.length() > 0) {
                        searchPlaces();
                    } else {
                        CustomDialogNotify.showToastCustom(KhamPha_Activity.this, "Chưa nhập địa chỉ");
                    }
                }
                return false;
            }
        });
    }

    private void searchPlaces() {
        progressBar.setVisibility(View.VISIBLE);
        // vị trí làm tâm để tìm kiếm ( latitude, longitude )
        String location = "21.013715429594125,%20105.79829597455202";
        // khoảng cách bán kính tính từ tâm ( phạm vi tìm kiếm ) = 2000 km
        int radius = 2000;
        // tự động hoàn thành các trường trả về như xã, huyện, tỉnh ( phường, quận, tp )
        boolean more_compound = true;
        // địa chỉ nhập từ bàn phím
        String input = edt_diachi.getText().toString();
        RetrofitClient.GoongIO_Services().getDiaDiem(HostApi.api_key_goong, location, input, radius, more_compound).enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {
                if (response.code() == 200) {
                    List<Place.Predictions> predictionsList = response.body().getPredictions();
                    progressBar.setVisibility(View.GONE);
                    adapter.setPredictions(predictionsList);
                }
            }

            @Override
            public void onFailure(Call<Place> call, Throwable t) {
                System.out.println("Có lỗi khi getDiaDiem: " + t);
            }
        });
    }

    private void getGeolocation(String placeId) {
        RetrofitClient.GoongIO_Services().getGeolocation(placeId, HostApi.api_key_goong).enqueue(new Callback<Geolocation>() {
            @Override
            public void onResponse(Call<Geolocation> call, Response<Geolocation> response) {
                if (response.code() == 200) {
                    Geolocation geolocation = response.body();
                    longitude = String.valueOf(geolocation.getResult().getGeometry().getLocation().getLng());
                    latitude = String.valueOf(geolocation.getResult().getGeometry().getLocation().getLat());

                    btnTim.setEnabled(true);
                    btnTim.setBackgroundResource(R.drawable.custom_btn4);
                }
            }

            @Override
            public void onFailure(Call<Geolocation> call, Throwable t) {

            }
        });
    }

    private void getLocation(Dialog dialog) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            Geocoder geocoder = new Geocoder(KhamPha_Activity.this, Locale.getDefault());
                            List<Address> addresses;
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                callBackDialog(addresses.get(0).getAddressLine(0));
                                dialog.dismiss();

                                latitude = String.valueOf(location.getLatitude());
                                longitude = String.valueOf(location.getLongitude());

                                btnTim.setEnabled(true);
                                btnTim.setBackgroundResource(R.drawable.custom_btn4);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    });
        } else {
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(KhamPha_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation(dialogDiaChi);
            } else {
                CustomDialogNotify.showToastCustom(this, "Vui lòng cấp quyền để sử dụng dịch vụ");
            }
        }
    }

    private void callBackDialog(String diachi) {
        tvDiaDiem.setText(diachi);
    }

}