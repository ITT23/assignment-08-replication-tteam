package com.example.ttransfer;

// https://www.geeksforgeeks.org/how-to-open-camera-through-intent-and-display-captured-image-in-android/
// https://www.tutorialspoint.com/how-to-convert-image-into-base64-string-in-androidhttps://www.tutorialspoint.com/how-to-convert-image-into-base64-string-in-android
// http://androidapplicationdeveloper.weebly.com/android-tutorial/how-to-convert-bitmap-to-string-and-string-to-bitmap
// https://stackoverflow.com/questions/9224056/android-bitmap-to-base64-string
// https://square.github.io/picasso/
// Drawable Background Image & Icon: https://giphy.com/gifs/nadrient-90s-80s-computer-l41lMAzNZfYAiyR0s

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
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
    private static final int PIC_ID = 123;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 103;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 101;
    private static final String BUTTON_START_HOME = "Take Screenshot";
    private static final String BUTTON_START_RETURN = "Return";
    private static final int HTTP_PORT = 8857;
    private String host = "192.168.43.236";
    private String port = "7800";
    private Drawable background;
    private TextView textview;
    private Button button_connect;
    private Button button_start;
    private Button button_save;
    private Sender sender;
    private ImageView imageView;
    private final Handler handler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init
        setContentView(R.layout.activity_main);
        sender = new Sender();
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
        background = layout.getBackground();
        imageView = (ImageView) findViewById(R.id.imageView_camera);
        textview = (TextView) findViewById(R.id.textView);
        button_start = (Button) findViewById(R.id.button_start);
        button_save = (Button) findViewById(R.id.button_save);
        button_connect = (Button) findViewById(R.id.button_connect);

        //ask camera permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }

        // buttons onclickListener
        button_start.setOnClickListener(view -> {
            String button_start_text = button_start.getText().toString();
            if(button_start_text.compareTo(BUTTON_START_HOME) == 0) {
                openCameraActivity();
            }else {
                scheduleAppRestart(view.getContext());
            }
        });
        button_save.setVisibility(View.INVISIBLE);
        button_save.setOnClickListener(view -> {
            saveImageToGallery();
            scheduleAppRestart(view.getContext());
        });
        button_connect.setOnClickListener(view -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, new Connect_Fragment());
            transaction.commit();
            button_connect.setVisibility(ImageView.INVISIBLE);
            button_start.setVisibility(ImageView.INVISIBLE);
        });
    }

    // a solution for "go back" to homepage---restart app
    private void scheduleAppRestart(Context context) {
        Intent restartIntent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, restartIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime() + (long) 100, pendingIntent);
        }
        finishAffinity();
    }

    // start_button click event
    private void openCameraActivity() {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera_intent, PIC_ID);
    }

    // save_button click event
    private void saveImageToGallery() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        } else {
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
    }

    // confirm event after took photo
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap photo;
        String bitmap_to_string;
        if (requestCode == PIC_ID) {
            photo = (Bitmap) data.getExtras().get("data");
            bitmap_to_string = bitmapToString(photo);
            //Log.i("BitmapString",bitmap_to_string);
            sender.execute(bitmap_to_string, host, port);
            handler.postDelayed(() -> {
                getImageFromHTTP();
                hide_homepage();
            }, 200);
        }
    }

    // change "pages" function
    private void hide_homepage() {
        textview.setVisibility(View.INVISIBLE);
        background.setAlpha(0);
        button_save.setVisibility(View.VISIBLE);
        button_start.setText(BUTTON_START_RETURN);
        button_connect.setVisibility(View.INVISIBLE);
    }

    // a function used for saveImageToGallery()
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

    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    private void getImageFromHTTP() {
        String http = "http://"+ host + ":" + HTTP_PORT + "/screenshot/screenshot.png";
        Picasso.get().load(http).into(imageView);
    }

    public void set_button_connect_visible() {
        button_connect.setVisibility(ImageView.VISIBLE);
    }

    public void set_button_start_visible() {
        button_start.setVisibility(ImageView.VISIBLE);
    }

    public void set_host(String h) {
        this.host = h;
    }

    public void set_port(String p) {
        this.port = p;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted ;)
            } else {
                // Permission denied :(
            }
        }

        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted ;)
            } else {
                // Permission denied :(
            }
        }
    }
}