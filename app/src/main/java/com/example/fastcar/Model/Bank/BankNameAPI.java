package com.example.fastcar.Model.Bank;

import java.util.List;

public class BankNameAPI {
    String code, desc;
    List<Bank> data;

    public BankNameAPI(String code, String desc, List<Bank> data) {
        this.code = code;
        this.desc = desc;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Bank> getData() {
        return data;
    }

    public void setData(List<Bank> data) {
        this.data = data;
    }
}
