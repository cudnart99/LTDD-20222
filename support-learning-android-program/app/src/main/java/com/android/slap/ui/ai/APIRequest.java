package com.android.slap.ui.ai;

import android.os.AsyncTask;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APIRequest {
    public static String srcPrefix = "Diem_Danh/";
    public APIRequest() {}

    public void callGet(String src, AfterFetch afterFetch){
        try{
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    // Create URL
                    try{
                        URL githubEndpoint = new URL("http://192.168.1.10:5000/get?src=" + srcPrefix + src);
//                        URL githubEndpoint = new URL("http://192.168.7.3:5500/fe/i.json");
//                        URL githubEndpoint = new URL("http://192.168.7.3:5000/get?src=.%2F..%2Ffe%2Fsrc%2Fimages%2F07_07_2023_23_12_17_minhduc.jpg");
// Create connection
                        HttpURLConnection myConnection =
                                (HttpURLConnection) githubEndpoint.openConnection();
                        if (myConnection.getResponseCode() == 200) {
                            InputStream responseBody = myConnection.getInputStream();
                            InputStreamReader responseBodyReader =
                                    new InputStreamReader(responseBody, "UTF-8");
                            JsonReader jsonReader = new JsonReader(responseBodyReader);
//                            jsonReader.beginObject(); // Start processing the JSON object
                            Gson gson = new Gson();
                            GetBody getBody = gson.fromJson(jsonReader, GetBody.class);
                            afterFetch.callback(null, getBody);

                        } else {
                            // Error handling code goes here
                        }
                        int c = 0;
                    }catch(Exception e){
                        int i = 0;
                    }

                }
            });
        }catch(Exception e){

        }

    }
    private void writeFormField(DataOutputStream outputStream, String fieldName, String value) throws IOException {
        writeBoundary(outputStream);
        outputStream.writeBytes("Content-Disposition: form-data; name=\"" + fieldName + "\"\r\n\r\n");
        outputStream.writeBytes(value + "\r\n");
    }

    public static FileInputStream fileInputStream;
    private void writeFileData(DataOutputStream outputStream, File file) throws IOException {
        writeBoundary(outputStream);
        outputStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"\r\n");
        outputStream.writeBytes("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName()) + "\r\n\r\n");

//        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();

        outputStream.writeBytes("\r\n");
    }

    public String BOUNDARY = "WebKitFormBoundaryrxAPGZSMcH8AMeXw";
    private void writeBoundary(DataOutputStream outputStream) throws IOException {
        outputStream.writeBytes("--" + BOUNDARY + "\r\n");
    }

    public void callPost(File file){
        try{
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    // Create URL
                    try{
                        URL url = new URL("http://192.168.1.10:5000/file-upload");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        connection.setRequestMethod("POST");
                        connection.setDoOutput(true);
                        connection.setRequestProperty("Content-Type", "multipart/form-data");

                        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                        writeFormField(outputStream, "file", file.getName());
                        writeFileData(outputStream, file);
                        writeBoundary(outputStream);
//                        OutputStream outputStream = connection.getOutputStream();
//                        FileInputStream fileInputStream = getContext().getContentResolver().openInputStream(result.mData.getData());// new FileInputStream(file);

                        // Write file data to output stream
//                        byte[] buffer = new byte[4096];
//                        int bytesRead;
//                        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
//                            outputStream.write(buffer, 0, bytesRead);
//                        }
//
//                        // Close streams
//                        fileInputStream.close();
//                        outputStream.flush();
//                        outputStream.close();

                        if (connection.getResponseCode() == 201) {
                            InputStream responseBody = connection.getInputStream();
                            InputStreamReader responseBodyReader =
                                    new InputStreamReader(responseBody, "UTF-8");
                            JsonReader jsonReader = new JsonReader(responseBodyReader);

                            Gson gson = new Gson();
                            UploadBody object = gson.fromJson(jsonReader, UploadBody.class);

                            int x = 0;
                            // Success
                            // Further processing here
                        } else {
                            // Error handling code goes here
                        }
                    }catch(Exception e){
                        int i = 0;
                    }

                }
            });
        }catch(Exception e){

        }

    }

    public UploadBody uploadBody = new UploadBody();
    public GetBody getBody = new GetBody();
    public void callUpload2(String uri, AfterFetch onClickListener){
//        HttpClient httpCligent = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost("http://192.168.7.3:5000/file-upload"); // Replace with your server URL
//
//// Create the MultipartEntityBuilder for the request
//        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
//
//// Add the file to the request
//        File file = new File(uri.getPath()); // Replace with your method to get the file path from Uri
//        FileBody fileBody = new FileBody(file);
//        entityBuilder.addPart("file", fileBody);
//
//// Build the request entity
//        HttpEntity entity = entityBuilder.build();
//
//// Set the request entity to the HttpPost
//        httpPost.setEntity(entity);
//
//// Execute the request
//
//        try {
//            HttpResponse response = httpClient.execute(httpPost);
//            int x;
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        OkHttpClient client = new OkHttpClient();
        File file = new File(uri); // Replace with the actual file path

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("fieldName", "fieldValue")
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.10:5000/file-upload") // Replace with your server URL
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle the response here
                if (response.isSuccessful()) {
                    // Request was successful
                    String responseData = response.body().string();
                    Gson gson = new Gson();
                    uploadBody = gson.fromJson(responseData, UploadBody.class);
                    onClickListener.callback(uploadBody,null);

                    // Process the response data
                } else {
                    // Request failed
                    // Handle the error
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // Handle the failure
                e.printStackTrace();
            }
        });

    }
}
