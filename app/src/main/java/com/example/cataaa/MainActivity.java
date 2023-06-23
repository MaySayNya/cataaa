package com.example.cataaa;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView iv = findViewById(R.id.imageView);

        // Declaring executor to parse the URL
        Executor executor = Executors.newSingleThreadExecutor();

        // Once the executor parses the URL and receives the image, handler will load it
        // in the ImageView
        Handler h = new Handler(Looper.getMainLooper());

        // Background process for network operation
        executor.execute(() -> {
            // Image URL
            String imageURL = "https://cataas.com/cat";

            // Try to get the image and post it in the ImageView
            try {
                InputStream is = (new URL(imageURL)).openStream();
                Bitmap image = BitmapFactory.decodeStream(is);

                Looper.prepare();
                Toast.makeText(getApplicationContext(), "Loaded cat!", Toast.LENGTH_LONG).show();

                // Leave background thread to make ui change
                h.post(() -> iv.setImageBitmap(image));
            }

            catch (Exception e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "Could not load cat!", Toast.LENGTH_LONG).show();
            }
        });
    }

}