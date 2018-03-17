package com.example.neerk.memegenerator;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MainActivity1 extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 20;
    public static final int INTENT_REQUEST_CODE = 10;
    public static final int WRITE_REQUEST_CODE = 30;
    private EditText topText;
    private EditText bottomText;
    FrameLayout rootLayout;
    private FloatingActionButton fab;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    final SharedComponents sharedComponents = new SharedComponents();
    final MenuSharedComponents menuSharedComponents = new MenuSharedComponents();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        rootLayout = findViewById(R.id.frame);
        topText = rootLayout.findViewById(R.id.editText1);
        bottomText = rootLayout.findViewById(R.id.editText2);
        topText.setOnLongClickListener(sharedComponents.TextOnLongClickListener());
        bottomText.setOnLongClickListener(sharedComponents.TextOnLongClickListener());
        rootLayout.setOnDragListener(menuSharedComponents.DragListener(MainActivity1.this));
        fab = findViewById(R.id.openFab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        sharedComponents.fabClick(fab1, fab2, fab, MainActivity1.this);
        sharedComponents.closeSubMenusFab(fab1, fab2, fab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu1, menu);
        sharedComponents.showCaseView(MainActivity1.this);
        menuSharedComponents.changeColor(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.text_color) {
            menuSharedComponents.chooseColorForText(MainActivity1.this);
        } else if (id == R.id.text_size) {
            menuSharedComponents.chooseTextSizeFromSpinner(MainActivity1.this);
        } else if (id == R.id.image) {
            requestPermissions();
        } else if (id == R.id.changeLayout) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity1.this);
            builder.setTitle("Confirmation").setMessage("\nAre you sure to change the layout?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(MainActivity1.this, MainActivity.class));
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


    private void requestPermissions() {
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

        } else {
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

                } else {
                    Toast.makeText(this, "Gallery Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case WRITE_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sharedComponents.downloadImage(MainActivity1.this);
                } else {
                    Toast.makeText(this, "Access to External Storage Denied!", Toast.LENGTH_SHORT).show();
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == INTENT_REQUEST_CODE) {
                Uri photoUri = data.getData();
                float rotateDegrees = sharedComponents.getCameraPhotoOrientation(photoUri,MainActivity1.this);
                Log.d("rotation degree", "" + rotateDegrees);
                ImageView imageView = findViewById(R.id.imageView);
                Picasso.with(this).load(photoUri).rotate(rotateDegrees, 0, 0).into(imageView);
            }
        }
    }



}


