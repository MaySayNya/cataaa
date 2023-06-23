package com.example.cataaa;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final String IMAGE_URL = "https://cataas.com/cat";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get components
        ImageView iv = findViewById(R.id.imageView);
        Button b = findViewById(R.id.button);
        TextInputEditText tv = findViewById(R.id.textInput);

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
                    InputStream is;
                    String s = tv.getEditableText().toString();
                    // if there is not text then get an image without text
                    if (s.equals("")){
                        is = (new URL(IMAGE_URL)).openStream();
                    }
                    // otherwise get an image with text
                    else{
                        is = (new URL(IMAGE_URL + "/says/" + s)).openStream();
                    }
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