package com.android.slap.event;

import com.android.slap.dao.SinhVienDAO;

public interface DiemDanhEvent extends SinhVienModelEvent{
    void onSuccessIn(SinhVienDAO studentModel);

    void onSuccessOut(SinhVienDAO studentModel);

    void onTouchItem(SinhVienDAO sv);
}
