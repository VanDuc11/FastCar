package com.example.fastcar.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fastcar.Adapter.ListBankAdapter;
import com.example.fastcar.Adapter.NganHangAdapter;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Model.Bank.Bank;
import com.example.fastcar.Model.Bank.BankNameAPI;
import com.example.fastcar.Model.NganHang;
import com.example.fastcar.Model.ResMessage;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaiKhoanNganHang_Activity extends AppCompatActivity {
    ShimmerFrameLayout shimmer_view;
    LinearLayout data_view;
    NganHangAdapter nganHangAdapter;
    RecyclerView recyclerView;
    LinearLayout ln_noResult;
    SwipeRefreshLayout refreshLayout;
    String email;
    User user;
    ImageView ic_back, ic_add;
    TextView edtTenNH;
    EditText edtChuNH, edtSTK;
    String tenNH, tenChuTK, stk;
    ListBankAdapter listBankAdapter;
    List<NganHang> nganHangList = new ArrayList<>();
    List<Bank> listBanks, listFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_khoan_ngan_hang);

        mapping();
        load();

        refreshLayout.setOnRefreshListener(() -> {
            load();
            refreshLayout.setRefreshing(false);
        });

        ic_back.setOnClickListener(view -> onBackPressed());
        ic_add.setOnClickListener(view -> showDialog_ThemNganHang_orUpdateNganHang(0, null, 0));
    }

    private void mapping() {
        recyclerView = findViewById(R.id.recyclerView_nganhang);
        shimmer_view = findViewById(R.id.shimmer_view_inNganHang);
        data_view = findViewById(R.id.data_view_inNganHang);
        refreshLayout = findViewById(R.id.refresh_data_inNganHang);
        ln_noResult = findViewById(R.id.ln_no_result_inNganHang);
        ic_back = findViewById(R.id.icon_back_inNganHang);
        ic_add = findViewById(R.id.icon_add_inNganHang);
    }

    private void load() {
        data_view.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        shimmer_view.setVisibility(View.VISIBLE);
        shimmer_view.startShimmerAnimation();
        ln_noResult.setVisibility(View.GONE);

        Intent intent = getIntent();
        email = intent.getStringExtra("emailUser");
        user = intent.getParcelableExtra("user");
        fetch_ListNH_ofUser(email);
    }

    @SuppressLint("SetTextI18n")
    private void showDialog_ThemNganHang_orUpdateNganHang(int index, NganHang nganHang, int position) {
        if (nganHangList.size() == 1 && index == 0) {
            CustomDialogNotify.showToastCustom(getBaseContext(), "Chỉ thêm tối đa 1 tài khoản");
        } else {
            LayoutInflater inflater = LayoutInflater.from(TaiKhoanNganHang_Activity.this);
            @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.layout_dialog_them_tknh, null);
            Dialog dialog = new Dialog(TaiKhoanNganHang_Activity.this);
            dialog.setContentView(custom);
            dialog.setCanceledOnTouchOutside(false);

            Window window = dialog.getWindow();
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
            dialog.show();

            ImageView ic_back = dialog.findViewById(R.id.icon_back_inThemNganHang);
            TextView btnConfirm = dialog.findViewById(R.id.btn_confirm_inThemNganHang);
            TextView tv_actionbar = dialog.findViewById(R.id.tv_actionbar_dialog_themnganhang);
            edtTenNH = dialog.findViewById(R.id.edt_tenNH_inThemNganHang);
            edtChuNH = dialog.findViewById(R.id.edt_tenChuTK_inThemNganHang);
            edtSTK = dialog.findViewById(R.id.edt_soTK_inThemNganHang);

            edtTenNH.setOnClickListener(view -> showDialogListBank());

            if (index == 0) {
                // thêm
                edtTenNH.setText("Chưa chọn");
                tv_actionbar.setText("Thêm tài khoản ngân hàng");
                btnConfirm.setText("Thêm");
                ic_back.setOnClickListener(view -> {
                    dialog.dismiss();
                });
                btnConfirm.setOnClickListener(view -> {
                    tenNH = edtTenNH.getText().toString();
                    tenChuTK = edtChuNH.getText().toString();
                    stk = edtSTK.getText().toString();
                    if (validateForm()) {
                        NganHang nganHangNew = new NganHang(tenNH, tenChuTK, stk, user);
                        createNewModel(nganHangNew, dialog);
                    }
                });
            } else {
                // sửa
                edtTenNH.setText(nganHang.getTenNH());
                edtChuNH.setText(nganHang.getTenChuTK());
                edtSTK.setText(nganHang.getSoTK());
                tv_actionbar.setText("Sửa tài khoản ngân hàng");
                btnConfirm.setText("Sửa");
                ic_back.setOnClickListener(view -> {
                    dialog.dismiss();
                    recyclerView.getAdapter().notifyItemChanged(position);
                });
                btnConfirm.setOnClickListener(view -> {
                    tenNH = edtTenNH.getText().toString();
                    tenChuTK = edtChuNH.getText().toString();
                    stk = edtSTK.getText().toString();
                    if (validateForm()) {
                        nganHang.setTenNH(tenNH);
                        nganHang.setTenChuTK(tenChuTK);
                        nganHang.setSoTK(stk);
                        updateItem(nganHang, dialog);
                    }
                });
            }
        }
    }

    private void fetch_ListNH_ofUser(String email) {
        RetrofitClient.FC_services().getListNH_ofUser(email).enqueue(new Callback<List<NganHang>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<NganHang>> call, Response<List<NganHang>> response) {
                data_view.setVisibility(View.VISIBLE);
                shimmer_view.stopShimmerAnimation();
                shimmer_view.setVisibility(View.GONE);

                if (response.code() == 200) {
                    if (!response.body().isEmpty()) {
                        refreshLayout.setVisibility(View.VISIBLE);
                        ln_noResult.setVisibility(View.GONE);
                        nganHangList = response.body();
                        nganHangAdapter = new NganHangAdapter(TaiKhoanNganHang_Activity.this, nganHangList);
                        recyclerView.setAdapter(nganHangAdapter);
                        nganHangAdapter.notifyDataSetChanged();

                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callBackMethod);
                        itemTouchHelper.attachToRecyclerView(recyclerView);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        ln_noResult.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<NganHang>> call, Throwable t) {
                System.out.println("Có lỗi khi fetch_ListNH_ofUser(): " + t);
                refreshLayout.setVisibility(View.GONE);
                ln_noResult.setVisibility(View.VISIBLE);
            }
        });
    }

    ItemTouchHelper.SimpleCallback callBackMethod = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            NganHang nganHang = nganHangList.get(position);

            if (direction == ItemTouchHelper.LEFT) {
                // delete item
                showDialogConfirmDelete(nganHang.get_id(), position);
            } else if (direction == ItemTouchHelper.RIGHT) {
                // delete item
                showDialog_ThemNganHang_orUpdateNganHang(1, nganHang, position);
            }
        }


        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftLabel("Xoá")
                    .setSwipeLeftLabelColor(Color.WHITE)
                    .addSwipeLeftActionIcon(R.drawable.icon_delete)
                    .setSwipeLeftActionIconTint(Color.WHITE)
                    .addSwipeLeftBackgroundColor(Color.RED)
                    .addSwipeLeftCornerRadius(1, 15)
                    .addSwipeLeftPadding(1, 10, 10, 10)
                    .setSwipeLeftLabelTextSize(1, 16)
                    .addSwipeRightLabel("Sửa")
                    .setSwipeRightLabelColor(Color.WHITE)
                    .addSwipeRightActionIcon(R.drawable.icon_edit)
                    .setSwipeRightActionIconTint(Color.WHITE)
                    .addSwipeRightBackgroundColor(Color.GREEN)
                    .addSwipeRightCornerRadius(1, 15)
                    .addSwipeRightPadding(1, 10, 10, 10)
                    .setSwipeRightLabelTextSize(1, 16)
                    .create().decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void createNewModel(NganHang nganHang, Dialog dialog) {
        RetrofitClient.FC_services().createNganHang(nganHang).enqueue(new Callback<ResMessage>() {
            @Override
            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                if (response.code() == 201) {
                    CustomDialogNotify.showToastCustom(TaiKhoanNganHang_Activity.this, "Thêm thành công");
                    load();
                    Handler handler = new Handler(Looper.getMainLooper());
                    Runnable myRunnable = dialog::dismiss;
                    handler.postDelayed(() -> handler.post(myRunnable), 1000);
                } else if (response.code() == 203) {
                    CustomDialogNotify.showToastCustom(getBaseContext(), "Thông tin thẻ này đã tồn tại");
                } else {
                    CustomDialogNotify.showToastCustom(getBaseContext(), response.message());
                }
            }

            @Override
            public void onFailure(Call<ResMessage> call, Throwable t) {
                System.out.println("Có lỗi khi thêm thẻ ngân hàng: " + t);
            }
        });
    }

    private boolean validateForm() {
        if (tenNH.equals("Chưa chọn")) {
            CustomDialogNotify.showToastCustom(getBaseContext(), "Chưa chọn ngân hàng");
            return false;
        }
        if (stk.isEmpty()) {
            CustomDialogNotify.showToastCustom(getBaseContext(), "Chưa có số tài khoản");
            edtSTK.requestFocus();
            return false;
        }
        if (tenChuTK.isEmpty()) {
            CustomDialogNotify.showToastCustom(getBaseContext(), "Chưa có tên chủ tài khoản");
            edtChuNH.requestFocus();
            return false;
        }
        return true;
    }

    private void updateItem(NganHang nganHang, Dialog dialog) {
        RetrofitClient.FC_services().updateNganHang(nganHang.get_id(), nganHang).enqueue(new Callback<ResMessage>() {
            @Override
            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                if (response.code() == 200) {
                    CustomDialogNotify.showToastCustom(getBaseContext(), "Thành công");
                    Handler handler = new Handler(Looper.getMainLooper());
                    Runnable myRunnable = dialog::dismiss;
                    handler.postDelayed(() -> handler.post(myRunnable), 1000);
                } else {
                    CustomDialogNotify.showToastCustom(getBaseContext(), "Có lỗi xảy ra");
                }
            }

            @Override
            public void onFailure(Call<ResMessage> call, Throwable t) {
                CustomDialogNotify.showToastCustom(getBaseContext(), "Có lỗi xảy ra");
                System.out.println("Có lỗi khi sửa tài khoản ngân hàng: " + t);
            }
        });
    }

    private void deleteItem(String idBank) {
        RetrofitClient.FC_services().deleteNganHang(idBank).enqueue(new Callback<ResMessage>() {
            @Override
            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                if (response.code() == 200) {
                    CustomDialogNotify.showToastCustom(getBaseContext(), "Thành công");
                } else {
                    CustomDialogNotify.showToastCustom(getBaseContext(), "Có lỗi xảy ra");
                }
            }

            @Override
            public void onFailure(Call<ResMessage> call, Throwable t) {
                System.out.println("Có lỗi khi xoá tài khoản ngân hàng: " + t);
                CustomDialogNotify.showToastCustom(getBaseContext(), "Có lỗi xảy ra");
            }
        });
    }

    private void showDialogConfirmDelete(String id, int position) {
        LayoutInflater inflater = LayoutInflater.from(TaiKhoanNganHang_Activity.this);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.dialog_confirm_delete_item, null);
        Dialog dialog = new Dialog(TaiKhoanNganHang_Activity.this);
        dialog.setContentView(custom);
        dialog.setCanceledOnTouchOutside(false);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        // set kích thước dialog
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set vị trí dialog
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.show();

        TextView btn_cancel = dialog.findViewById(R.id.btn_cancel_delete_item);
        TextView btn_confirm = dialog.findViewById(R.id.btn_confirm_delete_item);

        btn_cancel.setOnClickListener(view -> {
            dialog.dismiss();
            recyclerView.getAdapter().notifyItemChanged(position);
        });

        btn_confirm.setOnClickListener(view -> {
            deleteItem(id);
            nganHangList.remove(position);
            recyclerView.getAdapter().notifyItemRemoved(position);
            dialog.dismiss();
        });
    }

    private void showDialogListBank() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_name_bank);
        dialog.show();

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        RecyclerView recyclerView_nameBank = dialog.findViewById(R.id.recyclerView_nameBank);
        EditText textFind = dialog.findViewById(R.id.ed_search_list_bank);
        TextView tv_noResult = dialog.findViewById(R.id.id_noResult_nameBank);

        RetrofitClient.FC_services_Banks().getListBankVN().enqueue(new Callback<BankNameAPI>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<BankNameAPI> call, Response<BankNameAPI> response) {
                if (response.code() == 200) {
                    tv_noResult.setVisibility(View.GONE);
                    listBanks = new ArrayList<>(response.body().getData());
                    listFind = new ArrayList<>(listBanks);
                    listBankAdapter = new ListBankAdapter(getBaseContext(), listFind, edtTenNH, dialog);
                    recyclerView_nameBank.setAdapter(listBankAdapter);
                    listBankAdapter.notifyDataSetChanged();
                } else {
                    CustomDialogNotify.showToastCustom(getBaseContext(), "Có lỗi khi getListBank");
                }
            }

            @Override
            public void onFailure(Call<BankNameAPI> call, Throwable t) {
                System.out.println("Có lỗi khi getListBankVN(): " + t);
            }
        });

        textFind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listFind.clear();
                for (Bank item : listBanks) {
                    if (item.getShortName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        listFind.add(item);
                    }
                }

                if (listFind.size() > 0) {
                    tv_noResult.setVisibility(View.GONE);
                    recyclerView_nameBank.setVisibility(View.VISIBLE);
                } else {
                    tv_noResult.setVisibility(View.VISIBLE);
                    recyclerView_nameBank.setVisibility(View.GONE);
                }
                listBankAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}