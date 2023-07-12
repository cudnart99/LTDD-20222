package com.android.slap.event;

import com.android.slap.dao.ChatDAO;
import com.android.slap.dao.SinhVienDAO;

import java.util.List;

public interface ChatModelEvent {
    void afterGetData(List<ChatDAO> list, boolean newData);

    void afterSend();
}
