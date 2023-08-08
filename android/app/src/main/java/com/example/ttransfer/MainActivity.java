package com.example.ttransfer;

// https://www.geeksforgeeks.org/how-to-open-camera-through-intent-and-display-captured-image-in-android/
// https://www.tutorialspoint.com/how-to-convert-image-into-base64-string-in-androidhttps://www.tutorialspoint.com/how-to-convert-image-into-base64-string-in-android
// http://androidapplicationdeveloper.weebly.com/android-tutorial/how-to-convert-bitmap-to-string-and-string-to-bitmap
// https://stackoverflow.com/questions/9224056/android-bitmap-to-base64-string
// https://square.github.io/picasso/
// Drawable Background Image: https://giphy.com/gifs/nadrient-90s-80s-computer-l41lMAzNZfYAiyR0s

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import com.squareup.picasso.Picasso;


import android.os.Handler;

public class MainActivity extends AppCompatActivity{
    private static final int pic_id = 123;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 101;
    private static final String HOST = "192.168.43.236";
    private static final String PORT = "7800";
    // private static final int PORT = 7800;
    private static final int HTTP_PORT = 8857;
    private static final String button_start_home = "Take Screenshot";
    private static final String button_start_return = "Return";
    ConstraintLayout layout;
    Drawable background;
    TextView textview;
    Button button_start;
    Button button_save;
    private Sender sender;
    ImageView imageView;
    private Handler handler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sender = new Sender();
        layout = (ConstraintLayout) findViewById(R.id.layout);
        background = layout.getBackground();
        imageView = (ImageView) findViewById(R.id.imageView_camera);
        textview = (TextView) findViewById(R.id.textView);
        button_start = (Button) findViewById(R.id.button_start);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String button_start_text = button_start.getText().toString();
                if(button_start_text.compareTo(button_start_home) == 0) {
                    openCameraActivity();
                }else {
                    return_to_homepage();
                    //TODO: the problem: after get back to homepage, it's not possible
                    //to send the "message" again, this also happens when save button clicked
                    //IDEE: after these actions just reopen the app?
                }

            }
        });
        button_save = (Button) findViewById(R.id.button_save);
        button_save.setVisibility(imageView.INVISIBLE);
        button_save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                saveImageToGallery();
            }
        });
    }

    public void openCameraActivity() {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera_intent, pic_id);
    }

    public void saveImageToGallery() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        } else {
            saveImage();
        }
    }

    private void saveImage() {
        Bitmap imageBitmap = drawableToBitmap(imageView.getDrawable());
        if (imageBitmap == null) {
            Toast.makeText(this, "Image not available in ImageView.", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentResolver contentResolver = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "ImageFromImageView");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        // Save the image to the gallery
        Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if (imageUri != null) {
            try {
                OutputStream outputStream = contentResolver.openOutputStream(imageUri);
                if (outputStream != null) {
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.close();
                    Toast.makeText(this, "Image saved to gallery.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to save image.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Failed to create image in gallery.", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            Bitmap bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888
            );
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap photo;
        String bitmap_to_string;
        if (requestCode == pic_id) {
            photo = (Bitmap) data.getExtras().get("data");
            bitmap_to_string = bitmapToString(photo);
            //Log.i("BitmapString",bitmap_to_string);
            sender.execute(bitmap_to_string, HOST, PORT);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getImageFromHTTP();
                    hide_homepage();
                }
            }, 300);
        }
    }

    private void hide_homepage() {
        textview.setVisibility(View.INVISIBLE);
        background.setAlpha(0);
        button_save.setVisibility(imageView.VISIBLE);
        button_start.setText(button_start_return);
    }

    private void return_to_homepage() {
        textview.setVisibility(View.VISIBLE);
        background.setAlpha(255);
        button_save.setVisibility(imageView.INVISIBLE);
        button_start.setText(button_start_home);
    }

    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    private void getImageFromHTTP() {
        String http = "http://"+ HOST + ":" + HTTP_PORT + "/screenshot/screenshot.png";
        Picasso.get().load(http).into(imageView);
    }
}