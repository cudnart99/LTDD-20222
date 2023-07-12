package com.android.slap.dao;

import com.google.firebase.firestore.DocumentSnapshot;

public class ChatDAO extends BaseDAO {
    public String content;
    public String userId;
    public Long timestamp;

    public ChatDAO(DocumentSnapshot doc) {
        super(doc);
    }

    public ChatDAO() {
        super();
    }
}
