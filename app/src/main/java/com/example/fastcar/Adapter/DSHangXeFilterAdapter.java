package com.example.fastcar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastcar.FavoriteCar_Method;
import com.example.fastcar.Model.FavoriteCar;
import com.example.fastcar.Model.HangXeFilter;
import com.example.fastcar.R;

import java.util.ArrayList;
import java.util.List;

public class DSHangXeFilterAdapter extends RecyclerView.Adapter<DSHangXeFilterAdapter.ViewHolder> {
    Context context;
    List<HangXeFilter> listHangXe;
    private SparseBooleanArray selectedItems;

    public DSHangXeFilterAdapter(Context context, List<HangXeFilter> listHangXe) {
        this.context = context;
        this.listHangXe = listHangXe;
        selectedItems = new SparseBooleanArray();
        selectedItems.put(0, true);

        restoreSelectedItemsFromSharedPreferences();

        if (!hasSelectedItem()) {
            selectedItems.put(0, true);
        }
    }

    private boolean hasSelectedItem() {
        for (int i = 0; i < selectedItems.size(); i++) {
            if (selectedItems.valueAt(i)) {
                return true;
            }
        }
        return false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox ckbox;
        TextView tvTenHangXe, tvSLxe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ckbox = itemView.findViewById(R.id.ckbox_inItem_hangxe_filter);
            tvTenHangXe = itemView.findViewById(R.id.tv_tenhangxe_inItem_filter);
            tvSLxe = itemView.findViewById(R.id.tv_slxe_inItem_filter);
        }
    }

    @NonNull
    @Override
    public DSHangXeFilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_hangxe_filter, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull DSHangXeFilterAdapter.ViewHolder holder, int position) {
        int index = position;
        HangXeFilter hangXeFilter = listHangXe.get(position);
        holder.tvTenHangXe.setText(hangXeFilter.getTenHangXe());
        holder.tvSLxe.setText("(" + String.valueOf(hangXeFilter.getSoluong()) + " xe)");
        holder.ckbox.setChecked(selectedItems.get(position, false));

        holder.itemView.setOnClickListener(v -> {
            // Nếu item All đã được chọn và position khác item All, thì bỏ chọn All và chọn item hiện tại
            if (selectedItems.get(0, false) && position != 0) {
                selectedItems.put(0, false);
                selectedItems.put(position, true);
            } else {
                // Đảo ngược trạng thái checked của item khi được click
                boolean newState = !selectedItems.get(position, false);

                // Nếu item khác All được chọn, hãy đảm bảo rằng item All không được chọn
                if (position != 0) {
                    selectedItems.put(0, false);
                    selectedItems.put(position, newState);
                } else {
                    selectedItems.clear();
                    selectedItems.put(0, true);
                }
                // Nếu không có item nào được chọn, chọn mặc định item All
                if (!hasSelectedItem()) {
                    selectedItems.put(0, true);
                }
            }
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return listHangXe.size();
    }

    public boolean saveSelectedItemsToSharedPreferences() {
        boolean check = false;
        SharedPreferences preferences = context.getSharedPreferences("data_filter", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // Xóa trạng thái cũ trước khi lưu mới
        editor.clear();

        // Lưu trạng thái mới
        for (int i = 0; i < getItemCount(); i++) {
            editor.putBoolean("item" + i, selectedItems.get(i, false));
            if (selectedItems.get(0, false)) {
                check = true;
                editor.putBoolean("isSelectedHangXeFilter", false);
            }
            editor.putBoolean("isSelectedHangXeFilter", true);
        }
        editor.apply();
        return check;
    }

    private void restoreSelectedItemsFromSharedPreferences() {
        SharedPreferences preferences = context.getSharedPreferences("data_filter", Context.MODE_PRIVATE);

        // Lặp qua danh sách key và kiểm tra xem item đã chọn hay không
        for (int i = 0; i < getItemCount(); i++) {
            boolean isSelected = preferences.getBoolean("item" + i, false);
            selectedItems.put(i, isSelected);
        }
    }

    public List<HangXeFilter> getSelectedHangXeList() {
        List<HangXeFilter> selectedHangXeList = new ArrayList<>();

        for (int i = 0; i < getItemCount(); i++) {
            if (selectedItems.get(i, false)) {
                selectedHangXeList.add(listHangXe.get(i));
            }
        }
        return selectedHangXeList;
    }

}
