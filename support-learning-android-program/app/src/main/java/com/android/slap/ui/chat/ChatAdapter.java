package com.android.slap.ui.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.slap.MainActivity;
import com.android.slap.R;
import com.android.slap.dao.ChatDAO;
import com.android.slap.event.ChatModelEvent;

import java.util.List;

public class ChatAdapter extends BaseAdapter {

    private Context context;
    private List<ChatDAO> arrayList;
    private ChatModelEvent chatModelEvent;

    public ChatAdapter(Context context, List<ChatDAO> arrayList, ChatModelEvent sinhVienModelEvent) {
        this.context = context;
        this.arrayList = arrayList;
        this.chatModelEvent = sinhVienModelEvent;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        ChatDAO chatItem = arrayList.get(i);
        String name = MainActivity.MAP_STUDENT.containsKey(chatItem.userId) ?
                MainActivity.MAP_STUDENT.get(chatItem.userId).name + ": " : "";

        if(chatItem.userId.equals(MainActivity.USER_ID)){
            view = inflater.inflate(R.layout.chat_item_send,null);
            name = "";
        }else{
            view = inflater.inflate(R.layout.chat_item_receive,null);
            if(chatItem.userId.equals("123456")){
                name = "THẦY: ";
            }
        }


        TextView contentText =  view.findViewById(R.id.content_message);
        contentText.setRotation(180);


        contentText.setText(name + chatItem.content);

//        if(sinhVienItem.fullname != null){
//            textName.setText(sinhVienItem.fullname);
//        }else{
//            textName.setText(sinhVienItem.name);
//        }


        return view;
    }
}

