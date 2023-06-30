package com.example.cataaa;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final String IMAGE_URL = "https://cataas.com/cat";
    private static final int REQUEST = 112;
    private static final String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get components
        ImageView iv = findViewById(R.id.imageView);
        Button loadb = findViewById(R.id.load_button);
        Button saveb = findViewById(R.id.save_button);
        TextInputEditText tv = findViewById(R.id.textInput);

        final Bitmap[] last_image = {null};

        // Declaring executor to parse the URL
        Executor executor = Executors.newSingleThreadExecutor();

        // Once the executor parses the URL and receives the image, handler will load it
        // in the ImageView
        Handler h = new Handler(Looper.getMainLooper());

        loadb.setOnClickListener(view -> {
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
                    last_image[0] = image;
                    h.post(() -> iv.setImageBitmap(image));
                }

                catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        saveb.setOnClickListener(view -> {
            if (last_image[0] == null){
                Toast.makeText(getApplicationContext(),"Make a cat first!", Toast.LENGTH_SHORT).show();
            }else {
                // ask for storage if needed
                if(ContextCompat.checkSelfPermission(MainActivity.this, PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, REQUEST );
                }
                ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, 1024);



                String root = Environment.getExternalStorageDirectory().getAbsolutePath();
                File myDir = new File(root + "/saved_images");
                myDir.mkdirs();

                String fname = "Image-"+ "123123dafdadf131" +".png";
                File file = new File (myDir, fname);
                if (file.exists ()) file.delete ();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    last_image[0].compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });

    }

}