package com.example.fastcar.Activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Model.ResMessage;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.example.fastcar.Server.HostApi;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThongTinGPLX_Activity extends AppCompatActivity {
    ShimmerFrameLayout shimmer_view;
    LinearLayout data_view;
    ImageView btn_back;
    TextView btn_confirm, edt_ngaycap;
    ImageView img_mattruoc_gplx, img_matsau_gplx;
    EditText edt_sogplx, edt_hoten, edt_diachithuongtru;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    private Uri cameraImageUri;
    String imagePath_mattruoc, imagePath_matsau;
    User user;
    String email;
    Calendar calendar;
    private int isSelectedCamera = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_gplx);

        mapping();
        load();

        btn_back.setOnClickListener(view -> onBackPressed());
        edt_ngaycap.setOnClickListener(view -> showDatePicker_inDialogGPLX());
        btn_confirm.setOnClickListener(view -> updateGPLX_forUser());
    }

    private void mapping() {
        btn_back = findViewById(R.id.icon_back_in_gplx);
        btn_confirm = findViewById(R.id.btn_confirm_inGPLX);
        img_mattruoc_gplx = findViewById(R.id.img_mattruoc_gplx);
        img_matsau_gplx = findViewById(R.id.img_matsau_gplx);
        edt_sogplx = findViewById(R.id.edt_soGPLX_inGPLX);
        edt_hoten = findViewById(R.id.edt_hoten_inGPLX);
        edt_ngaycap = findViewById(R.id.edt_ngaycap_inGPLX);
        edt_diachithuongtru = findViewById(R.id.edt_diachithuongtru_inGPLX);
        data_view = findViewById(R.id.data_view_inGPLX);
        shimmer_view = findViewById(R.id.shimmer_view_inGPLX);
    }

    private void load() {
        Intent intent = getIntent();
        email = intent.getStringExtra("emailUser");
        calendar = Calendar.getInstance();
        data_view.setVisibility(View.GONE);
        shimmer_view.startShimmerAnimation();
        fetchData_UserLogin(email);
    }

    private void fetchData_UserLogin(String emailUser) {
        RetrofitClient.FC_services().getListUser(emailUser).enqueue(new Callback<List<User>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<User>> call, retrofit2.Response<List<User>> response) {
                data_view.setVisibility(View.VISIBLE);
                shimmer_view.stopShimmerAnimation();
                shimmer_view.setVisibility(View.GONE);

                if (response.code() == 200) {
                    List<User> list = response.body();
                    user = list.get(0);

                    edt_sogplx.setText(user.getSo_GPLX());
                    edt_hoten.setText(user.getHoTen_GPLX());
                    edt_ngaycap.setText(user.getNgayCap_GPLX());
                    edt_diachithuongtru.setText(user.getDiaChi_GPLX());

                    if (user.getTrangThai_GPLX() == 2) {
                        // nếu đã xác minh thì ẩn button sửa, enabled edittext
                        btn_confirm.setVisibility(View.GONE);
                        edt_sogplx.setEnabled(false);
                        edt_hoten.setEnabled(false);
                        edt_diachithuongtru.setEnabled(false);
                        edt_ngaycap.setEnabled(false);
                    }

                    if (user.getHinhAnh_GPLX().size() == 2) {
                        Glide.with(getBaseContext()).load(HostApi.URL_Image + user.getHinhAnh_GPLX().get(0)).into(img_mattruoc_gplx);
                        Glide.with(getBaseContext()).load(HostApi.URL_Image + user.getHinhAnh_GPLX().get(1)).into(img_matsau_gplx);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println("Có lỗi khi fetch user có email: " + emailUser + " --- " + t);
            }
        });
    }

    private void updateGPLX_forUser() {
        String sogplx = edt_sogplx.getText().toString();
        String hoten = edt_hoten.getText().toString();
        String diachi = edt_diachithuongtru.getText().toString();
        String ngaycap = edt_ngaycap.getText().toString();
        if (validateForm(sogplx, hoten, diachi, ngaycap)) {
            List<MultipartBody.Part> listPart = new ArrayList<>();

            String url = "/storage/emulated/0/Download/";

            if (user.getHinhAnh_GPLX().size() == 0) {
                // user chưa có ảnh trong DB
                if (imagePath_mattruoc != null && imagePath_matsau != null) {
                    // chọn cả 2 ảnh
                    listPart = genderListPart(imagePath_mattruoc, imagePath_matsau);
                } else if (imagePath_mattruoc == null && imagePath_matsau != null) {
                    CustomDialogNotify.showToastCustom(ThongTinGPLX_Activity.this, "Chưa tải lên ảnh mặt trước GPLX");
                    return;
                } else if (imagePath_mattruoc != null) {
                    CustomDialogNotify.showToastCustom(ThongTinGPLX_Activity.this, "Chưa tải lên ảnh mặt sau GPLX");
                    return;
                }
            } else {
                // user đã có ảnh lưu trong DB
                // chọn cả 2 ảnh
                if (imagePath_mattruoc != null && imagePath_matsau != null) {
                    // sửa cả 2 ảnh
                    listPart = genderListPart(imagePath_mattruoc, imagePath_matsau);
                } else if (img_mattruoc_gplx != null && imagePath_matsau == null) {
                    // sửa ảnh 1, giữ ảnh 2
                    listPart = genderListPart(imagePath_mattruoc, url + user.getHinhAnh_GPLX().get(1));
                } else if (img_mattruoc_gplx == null && imagePath_matsau != null) {
                    // giữ ảnh 1, sửa ảnh 2
                    listPart = genderListPart(url + user.getHinhAnh_GPLX().get(0), imagePath_matsau);
                } else {
                    // không sửa ảnh nào
                    listPart = null;
                }
            }

            MultipartBody.Part sogplxPart = MultipartBody.Part.createFormData("So_GPLX", sogplx);
            MultipartBody.Part hotenPart = MultipartBody.Part.createFormData("HoTen_GPLX", hoten);
            MultipartBody.Part ngaycapPart = MultipartBody.Part.createFormData("NgayCap_GPLX", ngaycap);
            MultipartBody.Part diachiPart = MultipartBody.Part.createFormData("DiaChi_GPLX", diachi);

            RetrofitClient.FC_services().updateGPLX(user.getEmail(), listPart, sogplxPart, hotenPart, ngaycapPart, diachiPart).enqueue(new Callback<ResMessage>() {
                @Override
                public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                    CustomDialogNotify.showToastCustom(ThongTinGPLX_Activity.this, "Cập nhật thành công");
                    Handler handler = new Handler(Looper.getMainLooper());
                    Runnable myRunnable = () -> {
                        onBackPressed();
                        finish();
                    };
                    handler.postDelayed(() -> handler.post(myRunnable), 1000);
                }

                @Override
                public void onFailure(Call<ResMessage> call, Throwable t) {
                    CustomDialogNotify.showToastCustom(ThongTinGPLX_Activity.this, "Đã xảy ra lỗi");
                    System.out.println("Có lỗi khi updateGPLX: " + t);
                }
            });
        }
    }

    private List<MultipartBody.Part> genderListPart(String imagePath_mattruoc, String imagePath_matsau) {
        List<MultipartBody.Part> listPart = new ArrayList<>();
        List<String> listPath = new ArrayList<>();
        listPath.add(imagePath_mattruoc);
        listPath.add(imagePath_matsau);

        for (String path : listPath) {
            File file = new File(path);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("HinhAnh_GPLX", file.getName(), requestBody);
            listPart.add(imagePart);
        }
        return listPart;
    }

    public void upload_mattruoc_GPLX(View view) {
        isSelectedCamera = 1;
        showImageDialog();
    }

    public void upload_matsau_GPLX(View view) {
        isSelectedCamera = 2;
        showImageDialog();
    }

    private void showDatePicker_inDialogGPLX() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        edt_ngaycap.setText(selectedDate);
                    }
                }, year, month, day);

        // Giới hạn ngày tối đa
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == DialogInterface.BUTTON_NEGATIVE) {
                    datePickerDialog.dismiss();
                }
            }
        });

        datePickerDialog.show();
    }

    private boolean validateForm(String sogplx, String hoten, String diachi, String ngaycap) {
        if (sogplx.length() == 0) {
            CustomDialogNotify.showToastCustom(ThongTinGPLX_Activity.this, "Chưa nhập số GPLX");
            edt_sogplx.requestFocus();
            return false;
        } else {
            if (sogplx.length() != 12) {
                CustomDialogNotify.showToastCustom(ThongTinGPLX_Activity.this, "Số GPLX phải có đủ 12 số");
                edt_sogplx.requestFocus();
                return false;
            }
        }

        if (hoten.isEmpty()) {
            CustomDialogNotify.showToastCustom(ThongTinGPLX_Activity.this, "Chưa nhập họ tên");
            edt_hoten.requestFocus();
            return false;
        }

        if (ngaycap.isEmpty()) {
            CustomDialogNotify.showToastCustom(ThongTinGPLX_Activity.this, "Chưa chọn ngày cấp");
            return false;
        }
        if (diachi.isEmpty()) {
            CustomDialogNotify.showToastCustom(ThongTinGPLX_Activity.this, "Chưa nhập địa chỉ");
            edt_diachithuongtru.requestFocus();
            return false;
        }
        return true;
    }

    public void showImageDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_select_camera_or_library);
        dialog.show();

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


        TextView cameraButton = dialog.findViewById(R.id.btn_open_camera);
        TextView libraryButton = dialog.findViewById(R.id.btn_open_library);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkCameraPermission()) {
                        openCamera();
                    }
                    dialog.dismiss();
                }
            }
        });

        libraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkGalleryPermission()) {
                        openGallery();
                    }
                    dialog.dismiss();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkGalleryPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GALLERY);
            return false;
        }
        return true;
    }

    private void openCamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager()) != null) {
            cameraImageUri = createImageFile();
            if (cameraImageUri != null) {
                List<ResolveInfo> resolvedIntentActivities = getPackageManager().queryIntentActivities(takePicture, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                    String packageName = resolvedIntentInfo.activityInfo.packageName;
                    grantUriPermission(packageName, cameraImageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
                startActivityForResult(takePicture, REQUEST_CAMERA);
            }
        }
    }


    private Uri createImageFile() {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (imageFile != null) {
            return FileProvider.getUriForFile(this, getPackageName() + ".provider", imageFile);
        } else {
            return null;
        }
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri selectedImageUri = cameraImageUri;
                // Xử lý ảnh được chọn từ thư viện
                // check điều kiện
                if (isSelectedCamera == 1) {
                    if (img_mattruoc_gplx != null) {
                        img_mattruoc_gplx.setImageURI(selectedImageUri);
                        imagePath_mattruoc = getRealPathFromUri(selectedImageUri);
                    }
                } else {
                    if (img_matsau_gplx != null) {
                        img_matsau_gplx.setImageURI(selectedImageUri);
                        imagePath_matsau = getRealPathFromUri(selectedImageUri);
                    }
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Xử lý khi hoạt động bị hủy bỏ
            }
        } else if (requestCode == REQUEST_GALLERY) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri selectedImageUri = data.getData();
                // Xử lý ảnh được chọn từ thư viện
                if (isSelectedCamera == 1) {
                    if (img_mattruoc_gplx != null) {
                        img_mattruoc_gplx.setImageURI(selectedImageUri);
                        imagePath_mattruoc = getRealPathFromUri(selectedImageUri);
                    }
                } else {
                    if (img_matsau_gplx != null) {
                        img_matsau_gplx.setImageURI(selectedImageUri);
                        imagePath_matsau = getRealPathFromUri(selectedImageUri);
                    }
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Xử lý khi hoạt động bị hủy bỏ
            }
        }
    }

    // Hàm để lấy đường dẫn thực sự của ảnh từ Uri
    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String imagePath = cursor.getString(columnIndex);
        cursor.close();
        return imagePath;
    }

}