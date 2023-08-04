package com.example.ttransfer;

// https://www.geeksforgeeks.org/how-to-open-camera-through-intent-and-display-captured-image-in-android/

import androidx.appcompat.app.AppCompatActivity;
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
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    private static final int pic_id = 123;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 101;
    Button button_start;
    Button button_save;
    ImageView imageView;
    Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView_camera);
        button_start = (Button) findViewById(R.id.button_start);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraActivity();
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
        // Match the request 'pic id with requestCode
        if (requestCode == pic_id) {
            // BitMap is data structure of image file which store the image in memory
            photo = (Bitmap) data.getExtras().get("data");
            // Set the image in imageview for display
            imageView.setImageBitmap(photo);
            button_save.setVisibility(imageView.VISIBLE);
        }
    }
}
