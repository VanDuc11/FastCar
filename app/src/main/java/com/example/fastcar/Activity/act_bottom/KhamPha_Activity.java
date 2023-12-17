package com.example.fastcar.Activity.act_bottom;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.app.DatePickerDialog;
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
import android.os.Build;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.example.fastcar.CustomTimePickerDialog;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Model.Car;
import com.example.fastcar.Model.Geolocation.Geolocation;
import com.example.fastcar.Model.Places.Place;
import com.example.fastcar.Model.ResMessage;
import com.example.fastcar.Model.ThongBao;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KhamPha_Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private TextView tvName, btnTim, tvTime_inKhamPha, tvDiaDiem, tvNumberNotify;
    private RecyclerView recyclerView_khuyenmai, recyclerView_xeKhamPha;
    private CircleImageView img_user;
    private FirebaseAuth auth;
    private ShimmerFrameLayout shimmer_view, shimmer_view_khuyenmai;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayout data_view;
    private FirebaseUser fBaseuser;
    FirebaseDatabase database;
    private MaterialDatePicker<Pair<Long, Long>> datePicker;
    private Long todayInMillis, tomorrowInMillis;
    private boolean isChooseDatePicker = false;
    private FrameLayout img_notify;
    private Dialog dialogDiaChi;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 100;
    private String tokenFCM;
    private VoucherAdapter adapterVoucher;
    private PlacesAdapter adapter;
    private ProgressBar progressBar;
    private EditText edt_diachi;
    private User user;
    private String place_id, longitude, latitude;
    private CustomTimePickerDialog timePickerDialog;
    private String formattedStartDate, formattedEndDate;
    private int time1, time2;
    private boolean isDatePickerDialogShowing = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kham_pha);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        fBaseuser = auth.getCurrentUser();

        mapping();
        Save();
        build_DatePicker();
        setDefault_SelectionDate();
        getTokenFCM();

        refreshLayout.setOnRefreshListener(() -> {
            load();
            build_DatePicker();
            setDefault_SelectionDate();
            Save();
            loadXeKhamPha();
            refreshLayout.setRefreshing(false);
        });

        btnTim.setOnClickListener(view -> {
            String diachi = tvDiaDiem.getText().toString();
            String time = tvTime_inKhamPha.getText().toString();
            if (!diachi.isEmpty()) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                try {
                    Date date1 = dateFormat.parse(formattedStartDate);
                    Date date2 = dateFormat.parse(formattedEndDate);
                    Date timeNow = new Date();
                    long timeThree = date1.getTime() - timeNow.getTime();
                    long threeHoursInMillis = 3 * 60 * 60 * 1000;

                    // So sánh
                    if (date1.after(timeNow)) {
                        if (timeThree > threeHoursInMillis) {
                            if (date2.after(date1)) {
                                long timeDifference = date2.getTime() - date1.getTime();
                                long hoursDifference = timeDifference / (60 * 60 * 1000);

                                // Nếu chênh lệch ít hơn 3 giờ, return false
                                if (hoursDifference < 3) {
                                    CustomDialogNotify.showToastCustom(this, "Tối thiểu 3 tiếng khi thuê xe trong ngày");
                                } else {
                                    SharedPreferences preferences = getSharedPreferences("diachiXe", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("diachi", diachi);
                                    editor.putString("time", time);
                                    editor.putString("lat", latitude);
                                    editor.putString("long", longitude);
                                    editor.apply();

                                    Intent intent = new Intent(getBaseContext(), DanhSachXe_Activity.class);
                                    startActivity(intent);
                                }
                            } else {
                                CustomDialogNotify.showToastCustom(this, "Thời gian không hợp lệ");
                            }
                        } else {
                            CustomDialogNotify.showToastCustom(this, "Thời gian nhận xe cần cách thời gian hiện tại 3 tiếng");
                        }
                    } else {
                        CustomDialogNotify.showToastCustom(this, "Thời gian không hợp lệ");
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                CustomDialogNotify.showToastCustom(KhamPha_Activity.this, "Vui lòng nhập địa chỉ để tìm xe");
            }

        });

        SharedPreferences preferences = getSharedPreferences("timePicker", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("startTime1", todayInMillis);
        editor.putLong("endTime1", tomorrowInMillis);
        editor.putInt("startHour1", 47);
        editor.putInt("endHour1", 47);
        editor.putString("s1", "23:00");
        editor.putString("e1", "23:00");
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
        shimmer_view_khuyenmai = findViewById(R.id.shimmer_view_khuyenmai);
        refreshLayout = findViewById(R.id.refresh_data_inKhamPha);
        tvNumberNotify = findViewById(R.id.notification_badge);
    }

    void load() {
        recyclerView_khuyenmai.setVisibility(View.GONE);
        shimmer_view_khuyenmai.setVisibility(View.VISIBLE);
        shimmer_view_khuyenmai.startShimmerAnimation();

        recyclerView_khuyenmai.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        tokenFCM = "";
        Intent intent = getIntent();
        boolean isIcon = intent.getBooleanExtra("SHOW_ICON_ADD", false);
        RetrofitClient.FC_services().getListVoucher(null).enqueue(new Callback<List<Voucher>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<Voucher>> call, Response<List<Voucher>> response) {
                recyclerView_khuyenmai.setVisibility(View.VISIBLE);
                shimmer_view_khuyenmai.stopShimmerAnimation();
                shimmer_view_khuyenmai.setVisibility(View.GONE);

                if (response.code() == 200) {
                    if (!response.body().isEmpty()) {
                        adapterVoucher = new VoucherAdapter(KhamPha_Activity.this, response.body(), isIcon, 0);
                        recyclerView_khuyenmai.setAdapter(adapterVoucher);
                        adapterVoucher.notifyDataSetChanged();
                        recyclerView_khuyenmai.setOnFlingListener(null);
                        SnapHelper snapHelper = new PagerSnapHelper();
                        snapHelper.attachToRecyclerView(recyclerView_khuyenmai);
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

        img_notify.setOnClickListener(view -> startActivity(new Intent(KhamPha_Activity.this, ThongBao_Activity.class)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (fBaseuser.isEmailVerified()) {
            if (fBaseuser == null) {
                signOut();
            }
        } else {
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

        Uri finalUri = uri;
        Date getTimeNow = new Date();
        User userNew = new User(fBaseuser.getUid(), userName, Email, pass, String.valueOf(finalUri), getTimeNow, tokenFCM);

        getUser_fromEmail(Email, userNew, pass);
    }

    void signOut() {
        auth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        startActivity(new Intent(this, Login_Activity.class));
        finish();
    }

    private void getUser_fromEmail(String email, User userNew, String password) {
        RetrofitClient.FC_services().getListUser(email).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, retrofit2.Response<List<User>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (!response.body().isEmpty()) {
                        Glide.with(getBaseContext()).load(response.body().get(0).getAvatar()).into(img_user);
                        List<User> list = response.body();
                        user = list.get(0);
                        Gson gson = new Gson();
                        String json = gson.toJson(list.get(0));

                        SharedPreferences preferences = getSharedPreferences("model_user_login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("user", json);
                        editor.apply();

                        if (password.length() != 0) {
                            if (!password.equals(user.getMatKhau())) {
                                user.setMatKhau(password);
                                User_Method.func_updateUser(KhamPha_Activity.this, email, user, false);
                            }
                        }
                        getListNotify_ofUser(user);
                    } else {
                        funcAddNewUser(userNew);
                        Gson gson = new Gson();
                        String json = gson.toJson(userNew);

                        SharedPreferences preferences = getSharedPreferences("model_user_login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("user", json);
                        editor.apply();
                    }

                    load();
                    loadXeKhamPha();
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
        if (!isDatePickerDialogShowing) {
            isDatePickerDialogShowing = true;
            timePickerDialog = new CustomTimePickerDialog();
            timePickerDialog.setListener(KhamPha_Activity.this, time1, time2, 0);
            timePickerDialog.show(getSupportFragmentManager(), "MonthYearPickerDialog");

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("Material_Range");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
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
                    formattedStartDate = sdf.format(new Date(startDate));
                    formattedEndDate = sdf.format(new Date(endDate));
                }
            });

            datePicker.addOnDismissListener(dialog -> isDatePickerDialogShowing = false);
        }
    }

    private void setDefault_SelectionDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        formattedStartDate = "23:00 " + sdf.format(todayInMillis);
        formattedEndDate = "23:00 " + sdf.format(tomorrowInMillis);

        String defaultText = formattedStartDate + " - " + formattedEndDate;
        tvTime_inKhamPha.setText(defaultText);
    }

    private CalendarConstraints buildCalendarConstraints() {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();

        // Chỉ cho phép chọn từ ngày hiện tại trở đi
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        constraintsBuilder.setStart(today);

        // Tính toán ngày kết thúc tối đa (max 3 tháng)
        Calendar maxEndDate = Calendar.getInstance();
        maxEndDate.set(Calendar.DAY_OF_MONTH, maxEndDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        maxEndDate.add(Calendar.MONTH, 2);
        long maxEndDateMillis = maxEndDate.getTimeInMillis();
        constraintsBuilder.setEnd(maxEndDateMillis);

        constraintsBuilder.setValidator(new CalendarConstraints.DateValidator() {
            @Override
            public boolean isValid(long date) {
                return date >= today && date <= maxEndDateMillis;
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
                    recyclerView_xeKhamPha.setOnFlingListener(null);
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

    private void getListNotify_ofUser(User user) {
        String ngaythamgia = new SimpleDateFormat("yyyy/MM/dd").format(user.getNgayThamGia());
        tvNumberNotify.setVisibility(View.VISIBLE);
        RetrofitClient.FC_services().getThongbao(user.get_id(), ngaythamgia).enqueue(new Callback<List<ThongBao>>() {
            @Override
            public void onResponse(Call<List<ThongBao>> call, Response<List<ThongBao>> response) {
                if (response.code() == 200) {
                    int number_ofNotify = response.body().size();
                    int total = number_ofNotify - user.getReadNotify();
                    if (user.getReadNotify() == number_ofNotify) {
                        tvNumberNotify.setVisibility(View.GONE);
                    } else {
                        if (total > 99) {
                            tvNumberNotify.setText("99+");
                        } else {
                            tvNumberNotify.setText(total + "");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ThongBao>> call, Throwable t) {
                System.out.println("lỗi : " + t);
            }
        });
    }

    private void callBackDialog(String diachi) {
        tvDiaDiem.setText(diachi);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePicker datePicker, int t1, int t2, int i2) {
        SharedPreferences preferences = getSharedPreferences("timePicker", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("startHour2", t1);
        editor.putInt("endHour2", t2);

        time1 = t1;
        time2 = t2;
        String selectedTimeRange = timePickerDialog.getSelectedTime();
        int indexOfDash = selectedTimeRange.indexOf(" - ");
        formattedStartDate = selectedTimeRange.substring(0, indexOfDash).trim() + " " + formattedStartDate;
        formattedEndDate = selectedTimeRange.substring(indexOfDash + 3).trim() + " " + formattedEndDate;

        tvTime_inKhamPha.setText(formattedStartDate + " - " + formattedEndDate);
        editor.putString("s2", selectedTimeRange.substring(0, indexOfDash).trim());
        editor.putString("e2", selectedTimeRange.substring(indexOfDash + 3).trim());
        editor.apply();
    }
}