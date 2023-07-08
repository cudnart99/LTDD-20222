package com.android.slap.ui.diemdanh;

import android.util.JsonReader;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class APIRequest {
    public static String apiGet = "http://127.0.0.1:5000/get";
    public static String apiUpload = "http://127.0.0.1:5000/file-upload";

    public APIRequest(){}

    public Map<String,Object> getBody(HttpsURLConnection http) throws Exception{
        InputStreamReader reader = new InputStreamReader(http.getInputStream(),"UTF-8");
        JsonReader jsonReader = new JsonReader(reader);

        Map<String,Object> output = new HashMap<>();
        jsonReader.beginObject(); // Start processing the JSON object
        while (jsonReader.hasNext()) { // Loop through all keys
            String key = jsonReader.nextName(); // Fetch the next key
            output.put(key, jsonReader.nextString());
//            if (key.equals("organization_url")) { // Check if desired key
//                String value = jsonReader.nextString();
//
//                // Do something with the value
//                // ...
//
//                break; // Break out of the loop
//            } else {
//                jsonReader.skipValue(); // Skip values of other keys
//            }
        }
        return output;
    }
    public void callGet(){
        try {
//            URL url = new URL(apiGet);
//            HttpsURLConnection http = (HttpsURLConnection) url.openConnection();
//            if (http.getResponseCode() == 200) {
//                Map<String, Object> m = getBody(http);
//
//            } else {
//
//            }
            // Tạo request lên server.
            Request request = new Request.Builder()
                    .url("https://api.github.com/users")
                    .build();

            // Thực thi request.
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("Error", "Network Error");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    // Lấy thông tin JSON trả về. Bạn có thể log lại biến json này để xem nó như thế nào.
                    String json = response.body().string();
                    final List<User> users = jsonAdapter.fromJson(json);

                    // Cho hiển thị lên RecyclerView.
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rvUsers.setAdapter(new UserAdapter(users, MainActivity.this));
                        }
                    });
                }
            });
        }catch(Exception e){
            int x = 0;
        }
    }

    public void callUpload(){

    }
}
