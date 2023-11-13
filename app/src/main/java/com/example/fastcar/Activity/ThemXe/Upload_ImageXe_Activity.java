package com.example.fastcar.Activity.ThemXe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fastcar.Activity.ThongTin_User_Activity;
import com.example.fastcar.Activity.act_bottom.CaNhan_Activity;
import com.example.fastcar.Adapter.DanhSachTenHangXeAdapter;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Model.AddCar;
import com.example.fastcar.Model.ResMessage;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Upload_ImageXe_Activity extends AppCompatActivity {
    RelativeLayout ic_back, ic_close;
    ImageView img_truoc, img_sau, img_trai, img_phai, img_dangky, img_dangkiem, img_baohiem;
    AppCompatButton btn_confirm;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    private Uri cameraImageUri;
    int index=0;
    String pathBaoHiem="",pathDangKy="",pathDangkiem="";
    RequestBody BKS,HangXe,MauXe,NSX,Soghe,lnl,tieuHao,mota,diachi,giathue,id_user,chuyenDong;



    List<Uri> listImage;




    AddCar addCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_xe);

        mapping();
        load();

        ic_back.setOnClickListener(view -> onBackPressed());

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listImage.size()<4||pathBaoHiem.length()==0||pathDangkiem.length()==0||pathDangKy.length()==0){
                    System.out.println("1 : "+listImage.size()+" 2: "+pathBaoHiem.length()+" 3 :"+pathDangkiem.length()+" 4 : "+pathDangKy.length());
           }else {
               RetrofitClient.FC_services().addCarUser(OutImagePats(),filePart("DangKyXe",pathDangKy),filePart("DangKiem",pathDangkiem),filePart("BaoHiem",pathBaoHiem),BKS,HangXe,MauXe,NSX,chuyenDong,Soghe, lnl,tieuHao, mota, diachi, giathue, id_user).enqueue(new Callback<ResMessage>() {
                   @Override
                   public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                       if(response.code()==201){
                           Toast.makeText(Upload_ImageXe_Activity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(getBaseContext(), CaNhan_Activity.class));
                           finish();
                       }else {
                           System.out.println("messs"+response.message());
                       }
                   }

                   @Override
                   public void onFailure(Call<ResMessage> call, Throwable t) {
                       System.out.println("Có lỗi khi thực hiện: " + t);
                   }
               });
           }
            }
        });
//            Toast.makeText(this, listImage+"", Toast.LENGTH_SHORT).show();
////            CustomDialogNotify.showToastCustom(this, "Chức năng đang phát triển");
////            startActivity(new Intent(this, CaNhan_Activity.class));
////            finish();
//



        img_truoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index=0;
                showImageDialog();
            }
        });
        img_trai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index=2;
                showImageDialog();
            }
        });
        img_phai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index=3;
                showImageDialog();
            }
        });
        img_sau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index=1;
                showImageDialog();
            }
        });
        img_dangkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index=4;
                showImageDialog();
            }
        });
        img_baohiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index=5;
                showImageDialog();
            }
        });
        img_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index=6;
                showImageDialog();
            }
        });
    }

    private void mapping() {
        btn_confirm = findViewById(R.id.btn_confirm_ThemXe);
        ic_back = findViewById(R.id.icon_back_in_upload_imageCar);
        ic_close = findViewById(R.id.icon_close_in_upload_imageCar);
        img_truoc = findViewById(R.id.img_anhtruoc_xe);
        img_sau = findViewById(R.id.img_anhsau_xe);
        img_phai=findViewById(R.id.img_anhphai_xe);
        img_trai = findViewById(R.id.img_anhtrai_xe);
        img_dangky = findViewById(R.id.img_dangky_xe);
        img_baohiem = findViewById(R.id.img_baohiem_xe);
        img_dangkiem = findViewById(R.id.img_dangkiem_xe);
        listImage = new ArrayList<>();
        addCar =  (AddCar) getIntent().getSerializableExtra("addCar2");

       BKS = RequestBody.create(MultipartBody.FORM, addCar.getBKS());
       HangXe = RequestBody.create(MultipartBody.FORM,addCar.getHangXe() );
       MauXe = RequestBody.create(MultipartBody.FORM,addCar.getMauXe() );
       NSX = RequestBody.create(MultipartBody.FORM,addCar.getNSX() );
       Soghe=RequestBody.create(MultipartBody.FORM,addCar.getSoGhe()+"" );
       lnl =RequestBody.create(MultipartBody.FORM,addCar.getLoaiNhienLieu() );
       tieuHao =RequestBody.create(MultipartBody.FORM,addCar.getTieuHao()+"" );
       mota =RequestBody.create(MultipartBody.FORM,addCar.getMoTa() );
       diachi =RequestBody.create(MultipartBody.FORM,addCar.getDiaChiXe());
       giathue=RequestBody.create(MultipartBody.FORM,addCar.getGiaThue1Ngay()+"");
       id_user = RequestBody.create(MultipartBody.FORM,addCar.getId_user());
       chuyenDong =  RequestBody.create(MultipartBody.FORM,addCar.getChuyenDong());
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri selectedImageUri = cameraImageUri;
                // Xử lý ảnh được chọn từ thư viện
//                if (img_truoc != null) {
//                    img_truoc.setImageURI(selectedImageUri);
//                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "aaaaaa", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_GALLERY) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri selectedImageUri = data.getData();
                if(index ==0){
                    if (img_truoc != null) {
                        img_truoc.setImageURI(selectedImageUri);

                        listImage.add(selectedImageUri);

                    }
                } else if (index==1) {
                    if (img_sau != null) {
                        img_sau.setImageURI(selectedImageUri);
                        listImage.add(selectedImageUri);

                    }
                } else if (index==2) {
                    if (img_trai != null) {
                        img_trai.setImageURI(selectedImageUri);
                        listImage.add(selectedImageUri);

                    }

                } else if (index==3) {
                    if (img_phai != null) {
                        img_phai.setImageURI(selectedImageUri);
                        listImage.add(selectedImageUri);

                    }
                } else if (index==4) {
                    if (img_dangkiem != null) {
                        img_dangkiem.setImageURI(selectedImageUri);
                        pathDangkiem = getImagePath(selectedImageUri);
                    }

                } else if (index==5) {
                    if (img_baohiem != null) {
                        img_baohiem.setImageURI(selectedImageUri);
                        pathBaoHiem = getImagePath(selectedImageUri);
                    }

                } else if (index==6) {
                    if (img_dangky != null) {
                        img_dangky.setImageURI(selectedImageUri);
                        pathDangKy = getImagePath(selectedImageUri);

                    }

                }
            }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Xử lý khi hoạt động bị hủy bỏ
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

    private  List<MultipartBody.Part>  OutImagePats(){
        List<MultipartBody.Part> imageParts = new ArrayList<>();
        for (Uri imageuri : listImage){
            File iamge = new File(getImagePath(imageuri));
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), iamge);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("HinhAnh", iamge.getName(), requestBody);
            imageParts.add(imagePart);
        }
        return imageParts;
    }
    private String getImagePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String imagePath = cursor.getString(columnIndex);
            cursor.close();
            return imagePath;
        } else {
            return uri.getPath();
        }
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
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY);
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
                    CustomDialogNotify.showToastCustom(Upload_ImageXe_Activity.this, "Vui lòng cấp quyền cho ứng dụng");
                }
                break;

            case REQUEST_GALLERY:
                // Kiểm tra quyền đọc bộ nhớ ngoại
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    CustomDialogNotify.showToastCustom(Upload_ImageXe_Activity.this, "Vui lòng cấp quyền cho ứng dụng");
                }
                break;
        }
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

    private List<MultipartBody.Part> filePart(String name,String path){
        List<MultipartBody.Part> image = new ArrayList<>();
        File iamge = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), iamge);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData(name, iamge.getName(), requestBody);
        image.add(imagePart);
        return  image;
    }
    private void load() {
       ;

    }
}