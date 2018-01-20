package com.example.neerk.memegenerator;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 20 ;
    public static final int INTENT_REQUEST_CODE = 10;
    public static final int INTENT_REQUEST_CODE_2 = 30;
    private EditText topText;
    private EditText bottomText;
    int defaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        defaultColor = ContextCompat.getColor(MainActivity.this,R.color.colorPrimary);
      /*  ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissions(view);
            }
        });
        ImageView imageView1 = (ImageView)findViewById(R.id.imageView1);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissions(view);
            }
        });*/

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
        }else if(id ==R.id.image1){
            requestPermissions(findViewById(R.id.imageView));
        }else if (id ==R.id.image2){
            requestPermissions(findViewById(R.id.imageView1));
        }
        return super.onOptionsItemSelected(item);
    }

    private void chooseTextSizeFromSpinner() {
        String[] fontSize ={"8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50"};
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.autocomplete_fontsize,null);
        mBuilder.setTitle("Choose text size");
        final AutoCompleteTextView textView;
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line,
                fontSize);

        textView = (AutoCompleteTextView) mView.findViewById(R.id.autoCompleteTextView2);
        textView.setAdapter(arrayAdapter);

        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                        try {
                            topText = (EditText) findViewById(R.id.editText1);
                            bottomText = (EditText) findViewById(R.id.editText2);
                            Float size =Float.parseFloat(textView.getText().toString());
                            if(size<8 || size >60){
                                Toast.makeText(MainActivity.this, "Enter a number between 3 and 60 !", Toast.LENGTH_SHORT).show();
                            }else {
                                topText.setTextSize(size);
                                bottomText.setTextSize(size);
                                Toast.makeText(MainActivity.this, "Text Size set to " + textView.getText().toString(), Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        }catch (NumberFormatException e){
                            Toast.makeText(MainActivity.this, "Invalid Input! Enter only numbers!", Toast.LENGTH_SHORT).show();
                        }


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
        Intent intent = goToGallery();
        startActivityForResult(intent, INTENT_REQUEST_CODE);
    }

    @NonNull
    private Intent goToGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        File photoDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        Uri uri = Uri.parse(photoDirectory.getPath());
        intent.setDataAndType(uri,"image/*");
        return intent;
    }

    private void createPhotoIntent2() {
        Intent intent = goToGallery();
        startActivityForResult(intent, INTENT_REQUEST_CODE_2);
    }

    private void requestPermissions(View view){
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

        }else if(view.equals(findViewById(R.id.imageView))){
            createPhotoIntent();
        }else{
            createPhotoIntent2();
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
                ImageView imageView = (ImageView)findViewById(R.id.imageView);
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;
                Picasso.with(this).load(photoUri).into(imageView);
            }else{
                Uri photoUri=data.getData();
                ImageView imageView = (ImageView)findViewById(R.id.imageView1);
                Picasso.with(this).load(photoUri).into(imageView);
            }
        }
    }
}

