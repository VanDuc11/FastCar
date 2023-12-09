package com.example.fastcar.Activity.ChuXe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fastcar.Activity.TaiKhoanNganHang_Activity;
import com.example.fastcar.Adapter.LichBanAdapter;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Model.Car;
import com.example.fastcar.Model.NganHang;
import com.example.fastcar.Model.ResMessage;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.example.fastcar.Server.HostApi;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LichBan_CuaXe_Activity extends AppCompatActivity {
    private Car car, carIntent;
    private ImageView btnBack, btnAdd;
    private ShimmerFrameLayout shimmer_view;
    private LinearLayout data_view, ln_no_result;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private LichBanAdapter lichBanAdapter;
    private List<String> listLichBan;
    private MaterialDatePicker<Pair<Long, Long>> datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_ban_cua_xe);

        mapping();
        load();

        refreshLayout.setOnRefreshListener(() -> {
            load();
            refreshLayout.setRefreshing(false);
        });

        btnBack.setOnClickListener(view -> onBackPressed());
        btnAdd.setOnClickListener(view -> showDialogDatePicker());
    }

    private void mapping() {
        shimmer_view = findViewById(R.id.shimmer_view_inLichBan);
        data_view = findViewById(R.id.data_view_inLichBan);
        btnBack = findViewById(R.id.icon_back_inLichBan);
        btnAdd = findViewById(R.id.icon_add_inLichBan);
        ln_no_result = findViewById(R.id.ln_no_result_lichban);
        refreshLayout = findViewById(R.id.refresh_data_inLichBan);
        recyclerView = findViewById(R.id.recyclerView_lichban);
    }

    private void load() {
        Intent intent = getIntent();
        carIntent = intent.getParcelableExtra("car");

        data_view.setVisibility(View.GONE);
        shimmer_view.setVisibility(View.VISIBLE);
        shimmer_view.startShimmerAnimation();

        fetchData_ofCar(carIntent.get_id());
    }

    private void fetchData_ofCar(String idXe) {
        RetrofitClient.FC_services().getCarByID(idXe).enqueue(new Callback<Car>() {
            @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                data_view.setVisibility(View.VISIBLE);
                shimmer_view.stopShimmerAnimation();
                shimmer_view.setVisibility(View.GONE);

                if (response.code() == 200) {
                    if (response.body() != null) {
                        car = response.body();
                        listLichBan = car.getLichBan();
                        if (listLichBan.size() != 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            ln_no_result.setVisibility(View.GONE);
                            lichBanAdapter = new LichBanAdapter(listLichBan, LichBan_CuaXe_Activity.this);
                            recyclerView.setAdapter(lichBanAdapter);
                            lichBanAdapter.notifyDataSetChanged();
                            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callBackMethod);
                            itemTouchHelper.attachToRecyclerView(recyclerView);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            ln_no_result.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                System.out.println("Có lỗi xảy ra: " + t);
            }
        });
    }

    private void showDialogDatePicker() {
        datePicker = MaterialDatePicker.Builder.dateRangePicker().setCalendarConstraints(buildCalendarConstraints()).build();
        datePicker.show(getSupportFragmentManager(), "Material_Range");
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                Long startDate = selection.first;
                Long endDate = selection.second;
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String start1 = simpleDateFormat.format(new Date(startDate));
                String end1 = simpleDateFormat.format(new Date(endDate));
                listLichBan.add(start1 + " - " + end1);
                car.setLichBan(listLichBan);
                updateLichBanXe(car);
            }
        });
    }

    private CalendarConstraints buildCalendarConstraints() {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();

        long today = MaterialDatePicker.todayInUtcMilliseconds();
        constraintsBuilder.setStart(today);

        Calendar maxEndDate = Calendar.getInstance();
        maxEndDate.set(Calendar.DAY_OF_MONTH, maxEndDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        maxEndDate.add(Calendar.MONTH, 3);
        long maxEndDateMillis = maxEndDate.getTimeInMillis();
        constraintsBuilder.setEnd(maxEndDateMillis);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        constraintsBuilder.setValidator(new CalendarConstraints.DateValidator() {
            @Override
            public boolean isValid(long date) {
                for (String dateRange : listLichBan) {
                    if (isDateInRange(dateRange, date)) {
                        return false;
                    }
                }
                return date >= today && date <= maxEndDateMillis;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
            }

            private boolean isDateInRange(String dateRange, long date) {
                try {
                    String[] dates = dateRange.split(" - ");
                    long startDate = dateFormat.parse(dates[0]).getTime();
                    long endDate = dateFormat.parse(dates[1]).getTime() + (24 * 60 * 60 * 1000);
                    if (date >= startDate && date <= endDate) {
                        return true;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        return constraintsBuilder.build();
    }

    ItemTouchHelper.SimpleCallback callBackMethod = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();

            if (direction == ItemTouchHelper.LEFT) {
                // delete item
                showDialogConfirmDeleteItem(position);
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
                    .create().decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void showDialogConfirmDeleteItem(int position) {
        LayoutInflater inflater = LayoutInflater.from(LichBan_CuaXe_Activity.this);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.dialog_confirm_delete_item, null);
        Dialog dialog = new Dialog(LichBan_CuaXe_Activity.this);
        dialog.setContentView(custom);
        dialog.setCanceledOnTouchOutside(false);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
            listLichBan.remove(position);
            recyclerView.getAdapter().notifyItemRemoved(position);
            car.setLichBan(listLichBan);
            updateLichBanXe(car);
            dialog.dismiss();
        });
    }

    private void updateLichBanXe(Car car) {
        RetrofitClient.FC_services().updateXe(carIntent.get_id(), car).enqueue(new Callback<ResMessage>() {
            @Override
            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                if (response.code() == 200) {
                    CustomDialogNotify.showToastCustom(LichBan_CuaXe_Activity.this, "Thành công");
                    load();
                }
            }

            @Override
            public void onFailure(Call<ResMessage> call, Throwable t) {
                System.out.println("Có lỗi khi updateLichBanXe: " + t);
            }
        });
    }
}