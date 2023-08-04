package com.example.ttransfer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class ShowPhotoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_photo);

        // Get the photo URI from the Intent
        Intent intent = getIntent();
        Uri photoUri = intent.getData();

        // Load and display the photo using the photoUri
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageURI(photoUri);
    }
}
