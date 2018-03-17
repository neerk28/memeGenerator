package com.example.neerk.memegenerator;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 20;
    public static final int INTENT_REQUEST_CODE = 10;
    public static final int WRITE_REQUEST_CODE = 30;
    public static final int INTENT_REQUEST_CODE_2 = 30;
    private EditText topText;
    private EditText bottomText;
    int defaultColor;
    private int x, y;
    FrameLayout rootLayout;
    private FloatingActionButton fab;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    final SharedComponents sharedComponents = new SharedComponents();
    final MenuSharedComponents menuSharedComponents = new MenuSharedComponents();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        defaultColor = ContextCompat.getColor(MainActivity.this, R.color.colorPrimary);
        rootLayout = findViewById(R.id.frame);
        topText = rootLayout.findViewById(R.id.editText1);
        bottomText = rootLayout.findViewById(R.id.editText2);
        topText.setOnLongClickListener(sharedComponents.TextOnLongClickListener());
        bottomText.setOnLongClickListener(sharedComponents.TextOnLongClickListener());
        rootLayout.setOnDragListener(menuSharedComponents.DragListener(MainActivity.this));
        fab =  findViewById(R.id.openFab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        sharedComponents.fabClick(fab1, fab2, fab, MainActivity.this);

        sharedComponents.closeSubMenusFab(fab1, fab2, fab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        sharedComponents.showCaseView(MainActivity.this);
        menuSharedComponents.changeColor(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.text_color) {
            menuSharedComponents.chooseColorForText(MainActivity.this);
        } else if (id == R.id.text_size) {
            menuSharedComponents.chooseTextSizeFromSpinner(MainActivity.this);
        } else if (id == R.id.image1) {
            requestPermissions(findViewById(R.id.imageView));
        } else if (id == R.id.image2) {
            requestPermissions(findViewById(R.id.imageView1));
        } else if (id == R.id.changeLayout) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Confirmation").setMessage("\nAre you sure to change the layout?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(MainActivity.this, MainActivity1.class));
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
        return super.onOptionsItemSelected(item);
    }

    private void createPhotoIntent() {
        Intent intent = menuSharedComponents.goToGallery();
        startActivityForResult(intent, INTENT_REQUEST_CODE);
    }

    private void createPhotoIntent2() {
        Intent intent = menuSharedComponents.goToGallery();
        startActivityForResult(intent, INTENT_REQUEST_CODE_2);
    }

    private void requestPermissions(View view) {
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

        } else if (view.equals(findViewById(R.id.imageView))) {
            createPhotoIntent();
        } else {
            createPhotoIntent2();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createPhotoIntent();

                } else {
                    Toast.makeText(this, "Gallery Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case WRITE_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sharedComponents.downloadImage(MainActivity.this);
                } else {
                    Toast.makeText(this, "Access to External Storage Denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == INTENT_REQUEST_CODE) {
                Uri photoUri = data.getData();
                float rotateDegrees = sharedComponents.getCameraPhotoOrientation(photoUri,MainActivity.this);
                Log.d("rotation degree",""+rotateDegrees);
                ImageView imageView =  findViewById(R.id.imageView);
                Picasso.with(this).load(photoUri).rotate(rotateDegrees,0,0).into(imageView);
            } else if (requestCode == INTENT_REQUEST_CODE_2) {
                Uri photoUri = data.getData();
                float rotateDegrees = sharedComponents.getCameraPhotoOrientation(photoUri,MainActivity.this);
                Log.d("rotation degree",""+rotateDegrees);
                ImageView imageView = findViewById(R.id.imageView1);
                Picasso.with(this).load(photoUri).rotate(rotateDegrees,0,0).into(imageView);
            }
        }
    }

}
