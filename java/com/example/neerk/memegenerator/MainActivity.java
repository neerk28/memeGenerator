package com.example.neerk.memegenerator;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 20 ;
    public static final int INTENT_REQUEST_CODE = 10;
    private EditText topText;
    private EditText bottomText;
    int defaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        defaultColor = ContextCompat.getColor(MainActivity.this,R.color.colorPrimary);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        Drawable drawable = menu.findItem(R.id.share).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
        Drawable drawable1 = menu.findItem(R.id.goToGallery).getIcon();
        if (drawable1 != null) {
            drawable1.mutate();
            drawable1.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
        Drawable drawable2 = menu.findItem(R.id.edit).getIcon();
        if (drawable2 != null) {
            drawable2.mutate();
            drawable2.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.text_color) {
           chooseColorForText();
        }else if (id ==R.id.text_size){
           chooseTextSizeFromSpinner();
        }else if (id ==R.id.share){
            sharePhoto();
        }else if(id ==R.id.goToGallery){
            createPhotoIntent();
        }
        return super.onOptionsItemSelected(item);
    }

    private void chooseTextSizeFromSpinner() {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_spinner,null);
        mBuilder.setTitle("Choose text size");
        final Spinner mSpinner = (Spinner)mView.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.TextSize));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mSpinner.setAdapter(adapter);
        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    if(!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Choose text size")){

                        topText=(EditText)findViewById(R.id.editText1);
                        bottomText= (EditText)findViewById(R.id.editText2);
                        topText.setTextSize(Float.parseFloat(mSpinner.getSelectedItem().toString()));
                        bottomText.setTextSize(Float.parseFloat(mSpinner.getSelectedItem().toString()));
                        Toast.makeText(MainActivity.this, "Text Size set to "+mSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    }
                    dialogInterface.dismiss();
            }
        });
        mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    private void chooseColorForText() {
        //ambilwarna stands for color picker - indonesian
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                Toast.makeText(MainActivity.this, "Closed Color Picker", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                    defaultColor = color;
                    topText=(EditText)findViewById(R.id.editText1);
                    bottomText= (EditText)findViewById(R.id.editText2);
                    topText.setTextColor(defaultColor);
                    bottomText.setTextColor(defaultColor);
            }
        });
        dialog.show();
    }

    private void sharePhoto() {
        createCompositeImage();
        createSharedIntent();

    }

    private void createSharedIntent() {
        Intent sharedIntent = new Intent(Intent.ACTION_SEND);
        File sharedFile = new File(getCacheDir(),"images/image.png");
        Uri imageUri= FileProvider.getUriForFile(this,"com.neerk.memegenerator.fileprovider",sharedFile);
        sharedIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
        sharedIntent.setType("image/png");
        startActivity(sharedIntent);
    }

    private void createCompositeImage() {
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.frame);
        frameLayout.setDrawingCacheEnabled(true);
        Bitmap bitmap = frameLayout.getDrawingCache();

        File sharedFile = new File(getCacheDir(),"images");
        sharedFile.mkdirs();

        try {
            FileOutputStream outputStream = new FileOutputStream(sharedFile +"/image.png");
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            outputStream.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        frameLayout.setDrawingCacheEnabled(false);
        frameLayout.destroyDrawingCache();
    }

    private void createPhotoIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        File photoDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        Uri uri = Uri.parse(photoDirectory.getPath());
        intent.setDataAndType(uri,"image/*");
        startActivityForResult(intent, INTENT_REQUEST_CODE);
    }

    private void requestPermissions(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        }else{
            createPhotoIntent();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createPhotoIntent();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this, "Gallery Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==INTENT_REQUEST_CODE){
                Uri photoUri=data.getData();
                ImageView imageView = (ImageView)findViewById(R.id.imageView2);
                Picasso.with(this).load(photoUri).into(imageView);
            }
        }
    }
}

