package com.example.fastcar.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThongTin_User_Activity extends AppCompatActivity {
    ImageView btn_edit_info;
    TextView tv_hoten, tv_email, tv_sdt, tv_gioitinh, tv_ngaysinh, tv_ten_hienthi, tv_ngaythamgia;
    CircleImageView img_avt_user;
    String name, email, phone, url;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    private Uri cameraImageUri;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_user);

        mapping();
        load();

        // button edit info user
        btn_edit_info.setOnClickListener(view -> {
            startActivity(new Intent(getBaseContext(), CapNhatThongTinUser_Activity.class));
        });
    }

    private void mapping() {
        tv_hoten = findViewById(R.id.tv_hoten_user_in_thongtinchitiet);
        tv_ngaysinh = findViewById(R.id.tv_ngaysinh_user_in_thongtinchitiet);
        tv_email = findViewById(R.id.tv_email_user_in_thongtinchitiet);
        tv_sdt = findViewById(R.id.tv_sdt_user_in_thongtinchitiet);
        tv_gioitinh = findViewById(R.id.tv_gioitinh_user_in_thongtinchitiet);
        tv_ten_hienthi = findViewById(R.id.tv_name_user_in_thongtinchitiet);
        btn_edit_info = findViewById(R.id.btn_edit_info_user);
        img_avt_user = findViewById(R.id.avt_user_in_thongtinchitiet);
        tv_ngaythamgia = findViewById(R.id.tv_ngaythamgia_user);
    }

    void load() {
        Intent intent = getIntent();
        url = intent.getStringExtra("image");
        user = intent.getParcelableExtra("user");
        name = user.getUserName();
        email = user.getEmail();
        phone = user.getSDT();

        tv_ten_hienthi.setText(name);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(user.getNgayThamGia());
        tv_ngaythamgia.setText(formattedDate);

        tv_email.setText(email);

        tv_sdt.setText("0987654321");

        tv_hoten.setText(name);
        tv_gioitinh.setText("Nam");
        tv_ngaysinh.setText("11/11/2011");


        if (url != null) {
            Glide.with(getBaseContext()).load(url).into(img_avt_user);
        } else {
            Glide.with(getBaseContext()).load(R.drawable.img_avatar_user).into(img_avt_user);
        }
    }

    public void backTo_CaNhanACT(View view) {
        onBackPressed();
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
                if (img_avt_user != null) {
                    img_avt_user.setImageURI(selectedImageUri);
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
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Xử lý khi hoạt động bị hủy bỏ
            }
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

}