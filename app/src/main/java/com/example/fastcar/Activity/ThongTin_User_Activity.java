package com.example.fastcar.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.fastcar.Activity.act_bottom.CaNhan_Activity;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.example.fastcar.User_Method;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class ThongTin_User_Activity extends AppCompatActivity {
    ImageView btn_edit_info;
    TextView tv_email, tv_sdt, tv_gioitinh, tv_ngaysinh, tv_ten_hienthi, tv_ngaythamgia, tv_tt_gplx;
    LinearLayout btn_updateGPLX;
    CircleImageView img_avt_user;
    String name, email, phone, url;
    ShimmerFrameLayout shimmer_view;
    LinearLayout data_view;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    private Uri cameraImageUri;
    User user;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_user);

        mapping();
        load();

        // button edit info user
        btn_edit_info.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), CapNhatThongTinUser_Activity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });

        btn_updateGPLX.setOnClickListener(view -> {
            Intent intent = new Intent(ThongTin_User_Activity.this, ThongTinGPLX_Activity.class);
            intent.putExtra("emailUser", user.getEmail());
            startActivity(intent);
        });
    }

    private void mapping() {
        tv_ngaysinh = findViewById(R.id.tv_ngaysinh_user_in_thongtinchitiet);
        tv_email = findViewById(R.id.tv_email_user_in_thongtinchitiet);
        tv_sdt = findViewById(R.id.tv_sdt_user_in_thongtinchitiet);
        tv_gioitinh = findViewById(R.id.tv_gioitinh_user_in_thongtinchitiet);
        tv_ten_hienthi = findViewById(R.id.tv_name_user_in_thongtinchitiet);
        btn_edit_info = findViewById(R.id.btn_edit_info_user);
        img_avt_user = findViewById(R.id.avt_user_in_thongtinchitiet);
        tv_ngaythamgia = findViewById(R.id.tv_ngaythamgia_user);
        data_view = findViewById(R.id.data_view_inThongTinUser);
        shimmer_view = findViewById(R.id.shimmer_view_inThongTinUser);
        btn_updateGPLX = findViewById(R.id.btn_updateGPLX);
        tv_tt_gplx = findViewById(R.id.tv_tt_gplx);
    }

    void load() {
        data_view.setVisibility(View.GONE);
        shimmer_view.setVisibility(View.VISIBLE);
        shimmer_view.startShimmerAnimation();

        Intent intent = getIntent();
        email = intent.getStringExtra("emailUser");

        fetchData_UserLogin(email);
    }

    public void backTo_CaNhanACT(View view) {
        Intent intent = new Intent(ThongTin_User_Activity.this, CaNhan_Activity.class);
        startActivity(intent);
    }

    private void fetchData_UserLogin(String emailUser) {
        RetrofitClient.FC_services().getListUser(emailUser).enqueue(new Callback<List<User>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<User>> call, retrofit2.Response<List<User>> response) {
                data_view.setVisibility(View.VISIBLE);
                shimmer_view.stopShimmerAnimation();
                shimmer_view.setVisibility(View.GONE);

                List<User> list = response.body();
                user = list.get(0);
                name = user.getUserName();
                phone = user.getSDT();
                url = user.getAvatar();

                tv_ten_hienthi.setText(name);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = dateFormat.format(user.getNgayThamGia());
                tv_ngaythamgia.setText(formattedDate);

                if (user.getGioiTinh() == null || user.getGioiTinh().equals("")) {
                    tv_gioitinh.setText("");
                } else {
                    tv_gioitinh.setText(user.getGioiTinh());
                }

                if (user.getNgaySinh() == null || user.getNgaySinh().equals("")) {
                    tv_ngaysinh.setText("");
                } else {
                    tv_ngaysinh.setText(user.getNgaySinh());
                }

                if (phone == null || user.getSDT().equals("")) {
                    tv_sdt.setText("");
                } else {
                    tv_sdt.setText(phone);
                }

                tv_email.setText(email);

                if (user.getTrangThai_GPLX() == 0) {
                    tv_tt_gplx.setText("Chưa xác minh");
                    tv_tt_gplx.setTextColor(Color.RED);
                } else if (user.getTrangThai_GPLX() == 1) {
                    tv_tt_gplx.setText("Đang chờ duyệt");
                    tv_tt_gplx.setTextColor(Color.parseColor("#CCC32B"));
                } else if (user.getTrangThai_GPLX() == 2) {
                    tv_tt_gplx.setText("Đã xác minh");
                    tv_tt_gplx.setTextColor(Color.parseColor("#1F2EB5"));
                } else {
                    tv_tt_gplx.setText("Xác minh thất bại");
                    tv_tt_gplx.setTextColor(Color.RED);
                }

                if (url != null) {
                    Glide.with(getBaseContext()).load(url).into(img_avt_user);
                } else {
                    Glide.with(getBaseContext()).load(R.drawable.img_avatar_user_v1).into(img_avt_user);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println("Có lỗi khi fetch user có email: " + emailUser + " --- " + t);
            }
        });
    }

    public void update_Avatar_User(View view) {
        showImageDialog();
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

        cameraButton.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkCameraPermission()) {
                    openCamera();
                }
                dialog.dismiss();
            }
        });

        libraryButton.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkGalleryPermission()) {
                    openGallery();
                }
                dialog.dismiss();
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
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
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
                if (img_avt_user != null) {
                    img_avt_user.setImageURI(selectedImageUri);
                    updateAvatarUser_inFirebase_andMongoDB(selectedImageUri);
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Xử lý khi hoạt động bị hủy bỏ
            }
        } else if (requestCode == REQUEST_GALLERY) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri selectedImageUri = data.getData();
                // Xử lý ảnh được chọn từ thư viện
                if (img_avt_user != null) {
                    img_avt_user.setImageURI(selectedImageUri);
                    updateAvatarUser_inFirebase_andMongoDB(selectedImageUri);
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Xử lý khi hoạt động bị hủy bỏ
            }
        }
    }


    private void updateAvatarUser_inFirebase_andMongoDB(Uri selectedImageUri) {
        // Lấy hồ sơ người dùng
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Cập nhật ID hình ảnh
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(selectedImageUri.toString()))
                .build();

        String imageName = getFileName(selectedImageUri);
        StorageReference imageRef = storageRef.child("images/" + imageName);

        // Cập nhật hồ sơ người dùng
        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });

        try {
            InputStream stream = getContentResolver().openInputStream(selectedImageUri);
            UploadTask uploadTask = imageRef.putStream(stream);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Lấy URL công khai của ảnh sau khi tải lên thành công
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
                            String photoUrlString = downloadUri.toString();
                            User userModel = new User(null, photoUrlString);
                            User_Method.func_updateUser(ThongTin_User_Activity.this, email, userModel, true);
                            load();
                        }
                    });
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CAMERA:
                // Kiểm tra quyền camera
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    CustomDialogNotify.showToastCustom(ThongTin_User_Activity.this, "Vui lòng cấp quyền cho ứng dụng");
                }
                break;

            case REQUEST_GALLERY:
                // Kiểm tra quyền đọc bộ nhớ ngoại
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    CustomDialogNotify.showToastCustom(ThongTin_User_Activity.this, "Vui lòng cấp quyền cho ứng dụng");
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        result = cursor.getString(displayNameIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }
}