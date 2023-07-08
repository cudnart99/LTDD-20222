package com.android.slap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class FriendLocation2 extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_friend_location2);
    GPSTracker mGPS = new GPSTracker(this);
    TextView text = findViewById(R.id.text_location);
    if (mGPS.canGetLocation) {
      mGPS.getLocation();
      text.setText("Lat" + mGPS.getLatitude() + "Lon" + mGPS.getLongitude());
    } else {
      text.setText("Unabletofind");
      System.out.println("Unable");
    }

    AsyncTask.execute(
      new Runnable() {
        @Override
        public void run() {
          // All your networking logic
          // Create URL
          URL githubEndpoint = null;
          try {
            githubEndpoint = new URL("https://geolocation-db.com/json/");

            // Create connection
            HttpsURLConnection myConnection = (HttpsURLConnection) githubEndpoint.openConnection();

            myConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");

            if (myConnection.getResponseCode() == 200) {
              // Success
              System.out.println("Success");
              InputStream responseBody = myConnection.getInputStream();
              InputStreamReader responseBodyReader = new InputStreamReader(
                responseBody,
                "UTF-8"
              );
              JsonReader jsonReader = new JsonReader(responseBodyReader);
              jsonReader.beginObject(); // Start processing the JSON object
              while (jsonReader.hasNext()) { // Loop through all keys
                String key = jsonReader.nextName(); //
                String value = jsonReader.nextString();
                if (value != null) {
                  System.out.println(key + ": " + value); // Fetc
                }
                // h the next key
                //                            if (key.equals("current_user_url")) { // Check if desired key
                //                                // Fetch the value as a String
                //                                String value = jsonReader.nextString();
                //                                System.out.println(value);
                //
                //                                // Do something with the value
                //                                // ...
                //
                //                                break; // Break out of the loop
                //                            } else {
                //                                jsonReader.skipValue(); // Skip values of other keys
                //                            }
              }
              jsonReader.close();
              myConnection.disconnect();
              // Further processing here
            } else {
              System.out.println("Error");
              // Error handling code goes here
            }
          } catch (MalformedURLException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }
          // should be here
        }
      }
    );
  }
}
