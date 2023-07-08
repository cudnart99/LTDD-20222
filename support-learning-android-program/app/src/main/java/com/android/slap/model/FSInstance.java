package com.android.slap.model;

import com.google.firebase.firestore.FirebaseFirestore;

public class FSInstance {
    public static FirebaseFirestore db;

    static {
        db = FirebaseFirestore.getInstance();
    }
}
