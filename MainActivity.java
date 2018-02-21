package com.example.neerk.memegenerator;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import yuku.ambilwarna.AmbilWarnaDialog;

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
    ConstraintLayout.LayoutParams params;
    private ProgressDialog waitDialog;
    private boolean fabExpanded = false;
    private FloatingActionButton fab;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defaultColor = ContextCompat.getColor(MainActivity.this, R.color.colorPrimary);
        rootLayout = (FrameLayout) findViewById(R.id.frame);
        topText = (EditText) rootLayout.findViewById(R.id.editText1);
        bottomText = (EditText) rootLayout.findViewById(R.id.editText2);
        topText.setOnLongClickListener(TextOnLongClickListener());
        bottomText.setOnLongClickListener(TextOnLongClickListener());
        rootLayout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                View layoutView = (View) event.getLocalState();
                final int action = event.getAction();
                switch (action) {

                    case DragEvent.ACTION_DRAG_STARTED:
                        params = (ConstraintLayout.LayoutParams) v.getLayoutParams();
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        layoutView.setVisibility(View.VISIBLE);
                        break;
                    case DragEvent.ACTION_DROP:
                        if (layoutView.getId() == R.id.editText2) {
                            x = (int) event.getX();
                            y = (int) event.getY();
                            FrameLayout oldFrameLayout = (FrameLayout) layoutView.getParent();
                            oldFrameLayout.removeView(layoutView);
                            bottomText.setVisibility(View.GONE);
                            FrameLayout newFrameLayout = (FrameLayout) v;

                            layoutView.setX(x - (layoutView.getWidth() / 2));
                            layoutView.setY(y - (layoutView.getHeight() / 2));
                            newFrameLayout.addView(layoutView);
                        }
                        if (layoutView.getId() == R.id.editText1) {
                            x = (int) event.getX();
                            y = (int) event.getY();
                            FrameLayout oldFrameLayout = (FrameLayout) layoutView.getParent();
                            oldFrameLayout.removeView(layoutView);
                            topText.setVisibility(View.GONE);
                            FrameLayout newFrameLayout = (FrameLayout) v;

                            layoutView.setX(x - (layoutView.getWidth() / 2));
                            layoutView.setY(y - (layoutView.getHeight() / 2));
                            newFrameLayout.addView(layoutView);
                        }
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        layoutView.setVisibility(View.VISIBLE);
                        break;
                    default:
                        Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                        break;
                }
                return true;
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded == true){
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissions();
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharePhoto();
            }
        });
        closeSubMenusFab();
    }

    private void closeSubMenusFab(){
        fab1.setVisibility(View.INVISIBLE);
        fab2.setVisibility(View.INVISIBLE);
        fab.setImageResource(R.drawable.ic_file_download_black_24dp);
        fabExpanded = false;

    }

    //Opens FAB submenus
    private void openSubMenusFab(){
        fab1.setVisibility(View.VISIBLE);
        fab2.setVisibility(View.VISIBLE);
        //Change settings icon to 'X' icon
        fab.setImageResource(R.drawable.ic_close_black_24dp);
        fabExpanded = true;
    }

    private View.OnLongClickListener TextOnLongClickListener() {

        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData dragdata = ClipData.newPlainText("", "");
                View.DragShadowBuilder shdwBldr = new View.DragShadowBuilder(view);
                view.startDrag(dragdata, shdwBldr, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        Drawable drawable = menu.findItem(R.id.changeLayout).getIcon();
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
        } else if (id == R.id.text_size) {
            chooseTextSizeFromSpinner();
        }/* else if (id == R.id.share) {
            sharePhoto();
        }*/ else if (id == R.id.image1) {
            requestPermissions(findViewById(R.id.imageView));
        } else if (id == R.id.image2) {
            requestPermissions(findViewById(R.id.imageView1));
        } /*else if (id == R.id.download) {
            requestPermissions();
        }*/else if(id == R.id.changeLayout){
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

    private void downloadImage() {

        new ImageDownloader().execute();

    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_REQUEST_CODE);
        } else {
            downloadImage();
        }
    }

    private class ImageDownloader extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            waitDialog = ProgressDialog.show(MainActivity.this, "Please wait", "Downloading image");
            waitDialog.setCancelable(false);
        }

        @Override
        protected void onPostExecute(Boolean flag) {

            waitDialog.dismiss();

            if (flag) {

                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setPositiveButton("OK", null).setTitle("Success").setMessage("\n\nDownload complete!").show();
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextColor(Color.BLUE);
                Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                // override the text color of positive button
                positiveButton.setTextColor(getResources().getColor(android.R.color.black));
            } else {

                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setPositiveButton("OK", null).setTitle("Error").setMessage("\n\nDownload Incomplete, Try again...").show();
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextColor(Color.RED);
                Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                // override the text color of positive button
                positiveButton.setTextColor(getResources().getColor(android.R.color.black));

            }
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            rootLayout.setDrawingCacheEnabled(true);
            Bitmap image1 = rootLayout.getDrawingCache();
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File dir = new File(path + "/");
            dir.mkdirs();
            Log.d("imagename: " + image1.toString(), " path: " + dir.getPath());
            File file = new File(dir, System.currentTimeMillis() + ".png");
            OutputStream out = null;
            try {
                out = new FileOutputStream(file);
                image1.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                Log.d("status: complete", " image: " + image1.toString() + ".png");

            } catch (java.io.IOException e) {
                e.printStackTrace();
                Log.d("status: incomplete", " image: " + image1.toString() + ".png");
                return false;
            }
            return true;
        }

    }

    private void chooseTextSizeFromSpinner() {

        topText = (EditText) findViewById(R.id.editText1);
        bottomText = (EditText) findViewById(R.id.editText2);
        String[] fontSize = {"8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50"};
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.autocomplete_fontsize, null);
        mBuilder.setTitle("Choose text size");
        final AutoCompleteTextView textView;
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line,
                fontSize);

        textView = (AutoCompleteTextView) mView.findViewById(R.id.autoCompleteTextView2);
        textView.setText(String.valueOf(convertPixelsToSp(topText.getTextSize(), getBaseContext())));
        textView.setAdapter(arrayAdapter);

        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                try {

                    Float size = Float.parseFloat(textView.getText().toString());
                    if (size < 8 || size > 60) {
                        Toast.makeText(MainActivity.this, "Enter a number between 3 and 60 !", Toast.LENGTH_SHORT).show();
                    } else {
                        topText.setTextSize(size);
                        bottomText.setTextSize(size);
                        Toast.makeText(MainActivity.this, "Text Size set to " + textView.getText().toString(), Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                } catch (NumberFormatException e) {
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

    public int convertPixelsToSp(float px, Context context) {
        float sp = px / getResources().getDisplayMetrics().scaledDensity;
        return (int) sp;
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
                topText = (EditText) findViewById(R.id.editText1);
                bottomText = (EditText) findViewById(R.id.editText2);
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
        File sharedFile = new File(getCacheDir(), "images/image.png");
        Uri imageUri = FileProvider.getUriForFile(this, "com.neerk.memegenerator.fileprovider", sharedFile);
        sharedIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        sharedIntent.setType("image/png");
        startActivity(sharedIntent);
    }

    private void createCompositeImage() {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame);
        frameLayout.setDrawingCacheEnabled(true);
        Bitmap bitmap = frameLayout.getDrawingCache();

        File sharedFile = new File(getCacheDir(), "images");
        sharedFile.mkdirs();

        try {
            FileOutputStream outputStream = new FileOutputStream(sharedFile + "/image.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
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
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    private void createPhotoIntent2() {
        Intent intent = goToGallery();
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
                    downloadImage();
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
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                Picasso.with(this).load(photoUri).into(imageView);
            } else if (requestCode == INTENT_REQUEST_CODE_2) {
                Uri photoUri = data.getData();
                ImageView imageView = (ImageView) findViewById(R.id.imageView1);
                Picasso.with(this).load(photoUri).into(imageView);
            }
        }
    }

}


