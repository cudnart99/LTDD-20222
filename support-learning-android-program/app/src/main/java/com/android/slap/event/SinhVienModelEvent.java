package com.android.slap.event;

import com.android.slap.dao.SinhVienDAO;

import java.util.List;

public interface SinhVienModelEvent {
    void afterGetData(List<SinhVienDAO> list);

    void afterSave();
}
