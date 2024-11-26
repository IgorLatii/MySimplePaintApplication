package com.example.mysimplepaintapplication;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import se.warting.signatureview.views.SignaturePad;
import yuku.ambilwarna.AmbilWarnaDialog;

import android.Manifest;

public class MainActivity extends AppCompatActivity {
    SignaturePad signatureView;// объект для того чтобы рисовать
    ImageButton imgEraser,imgColor, imgSave;// 3 объекта кнопок
    SeekBar seekBar;// слайдбар для изменения размера ручки
    TextView txtPensize;// текст для пояснения

    private static String fileName;

    //File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myPaintings");
    int defaultcolor;// стандартный цвет который мы используем на данный момент

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signatureView = findViewById(R.id.signature_view);
        seekBar = findViewById(R.id.penSize);
        txtPensize = findViewById(R.id.TxtPenSize);
        imgColor = findViewById(R.id.btnColor);
        imgEraser = findViewById(R.id.btnEraser);
        imgSave = findViewById(R.id.btnSave);

        //askPermission();

        /*SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String date = format.format(new Date());
        fileName = path + "/" + date + ".png";*/

        defaultcolor = ContextCompat.getColor(MainActivity.this, R.color.black);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtPensize.setText(progress + "dp");
                signatureView.setMaxWidth(progress);
                seekBar.setMax(50);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        imgColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        imgEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureView.clear();
            }
        });

        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!signatureView.isEmpty()){
                    saveImage();
                }
            }
        });
    }

    /*private void makeDirectory() {
        if (!path.exists()) {
            boolean b = path.mkdirs();
            if (b) {
                Toast.makeText(MainActivity.this, "Directory created!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Directory is not created!", Toast.LENGTH_SHORT).show();
            }
        } else {Toast.makeText(MainActivity.this, "Directory already exists!", Toast.LENGTH_SHORT).show();}
    }*/

    /*private void saveImage() throws IOException {
        makeDirectory();

        File file = new File(fileName);
        Bitmap bitmap = signatureView.getSignatureBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] bitmapData = bos.toByteArray();
        Toast.makeText(MainActivity.this, "bit map compressed!!", Toast.LENGTH_SHORT).show();

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapData);
            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            Log.e("FileError", "FileNotFoundException: " + e.getMessage());
            Log.e("FileError", "Cause: " + e.getCause()); // Обычно возвращает null

        };

    }*/

    private void saveImage() {
        if (!signatureView.isEmpty()) {
            // Getting the picture in bitmap format
            Bitmap bitmap = signatureView.getSignatureBitmap();

            // Creating the unique file name
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String date = format.format(new Date());
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, "image_" + date + ".png");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyPaintings");

            // Adding the picture in MediaStore
            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri != null) {
                try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    Toast.makeText(MainActivity.this, "Image saved in Gallery", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Log.e("SaveImage", "Error saving image", e);
                    Toast.makeText(MainActivity.this, "Error saving image", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(MainActivity.this, "Signature is empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void openColorPicker() {
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, defaultcolor,
                new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultcolor = color;
                signatureView.setPenColor(color);
            }
        });
        ambilWarnaDialog.show();
    }

    /*private void askPermission() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        Toast.makeText(MainActivity.this, "Granted!", Toast.LENGTH_SHORT).show();
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            Log.d("Permissions", "All permissions granted");
                        } else {
                            Log.d("Permissions", "Some permissions denied");
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }*/
}