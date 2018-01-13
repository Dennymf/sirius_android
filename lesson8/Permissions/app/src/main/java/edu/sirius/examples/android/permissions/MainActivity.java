package edu.sirius.examples.android.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_IMAGE_CODE = 2;

    private ImageView imageView;

    private Uri fileUri;

    private boolean shouldRequestPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckBox checkBox = findViewById(R.id.check_permission);
        Button button = findViewById(R.id.pick_image);
        imageView = findViewById(R.id.image_view);

        button.setOnClickListener((v) -> {
            if (shouldRequestPermission && !isCameraGranted()) {
                checkPermission();
            } else {
                startPickerActivity();
            }
        });

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> shouldRequestPermission = isChecked);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        startPickerActivity();
    }

    private void startPickerActivity() {
        startActivityForResult(getPickerIntent(), REQUEST_IMAGE_CODE);
    }

    private Intent getPickerIntent() {
        PackageManager packageManager = getPackageManager();
        List<Intent> intents = new ArrayList<>();
        Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> cams = packageManager.queryIntentActivities(capture, 0);
        if (isCameraGranted()) {
            for (ResolveInfo info : cams) {
                if (fileUri == null) {
                    fileUri = getCameraImageUri();
                }
                Intent intent = new Intent(capture);
                intent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
                intent.setPackage(info.activityInfo.packageName);
                if (fileUri != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                }
                intents.add(intent);
            }
        }
        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        List<ResolveInfo> galleries = packageManager.queryIntentActivities(gallery, 0);
        for (ResolveInfo info : galleries) {
            if (isPermitted(info)) {
                Intent intent = new Intent(gallery);
                intent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
                intent.setPackage(info.activityInfo.packageName);
                intents.add(intent);
            }
        }

        Intent mainIntent = intents.get(intents.size() - 1);
        for (Intent intent : intents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }

        intents.remove(mainIntent);

        Intent chooser = Intent.createChooser(mainIntent, "Выберите источник");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toArray(new Parcelable[intents.size()]));

        return chooser;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = getUri(data);
                imageView.setImageURI(uri);
            } else {
                Toast.makeText(this, "Не удалось получить картинку", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkPermission() {
        if (!isCameraGranted()) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_CAMERA_PERMISSION);
        }
    }

    private boolean isPermitted(ResolveInfo info) {
        String permission = info.activityInfo.permission;
        if (permission == null) {
            permission = info.activityInfo.applicationInfo.permission;
        }
        return permission == null || (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean isCameraGranted() {
        return !shouldRequestPermission || (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private Uri getUri(Intent data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? fileUri : data.getData();
    }

    private Uri getCameraImageUri() {
        try {
            File imgFile = createTempFile();
            return Uri.fromFile(imgFile);
        } catch (IOException e) {
            return null;
        }
    }

    private File createTempFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "JPEG_" + timestamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(fileName, ".jpg", storageDir);
    }
}
