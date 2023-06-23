package com.example.cataaa;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final String IMAGEURL = "https://cataas.com/cat";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView iv = findViewById(R.id.imageView);

        Button b = findViewById(R.id.button);

        // Declaring executor to parse the URL
        Executor executor = Executors.newSingleThreadExecutor();

        // Once the executor parses the URL and receives the image, handler will load it
        // in the ImageView
        Handler h = new Handler(Looper.getMainLooper());

        b.setOnClickListener(view -> {
            // Background process for network operation
            executor.execute(() -> {
                // Try to get the image and post it in the ImageView
                try {
                    InputStream is = (new URL(IMAGEURL)).openStream();
                    Bitmap image = BitmapFactory.decodeStream(is);
                    // Leave background thread to make ui change
                    h.post(() -> iv.setImageBitmap(image));
                }

                catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

    }

}