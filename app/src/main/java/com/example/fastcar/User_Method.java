package com.example.fastcar;

import android.content.Context;

import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Model.ResMessage;
import com.example.fastcar.Model.User;
import com.example.fastcar.Retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_Method {
    public static void func_updateUser(Context context, String email, User user, boolean isShowToast) {
        RetrofitClient.FC_services().updateUser(email, user).enqueue(new Callback<ResMessage>() {
            @Override
            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                if (isShowToast) {
                    CustomDialogNotify.showToastCustom(context, "Cập nhật thành công");
                }
            }

            @Override
            public void onFailure(Call<ResMessage> call, Throwable t) {
                CustomDialogNotify.showToastCustom(context, "Đã xảy ra lỗi");
                System.out.println("Có lỗi khi updateUser() " + t);
            }
        });
    }

}
