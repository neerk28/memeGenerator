package com.example.neerk.memegenerator;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import uk.co.deanwild.materialshowcaseview.IShowcaseListener;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

/**
 * Created by neerk on 06/03/18.
 */

public class SharedComponents {

    private FloatingActionButton fab;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private EditText topText;
    private boolean fabExpanded = false;
    private ProgressDialog waitDialog;
    public static final int WRITE_REQUEST_CODE = 30;
    private FrameLayout rootLayout;

    public void showCaseView(final Activity activity) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                fab = activity.findViewById(R.id.openFab);
                fab1 = activity.findViewById(R.id.fab1);
                fab2 = activity.findViewById(R.id.fab2);
                topText = activity.findViewById(R.id.editText1);
                new MaterialShowcaseView.Builder(activity).setTarget(activity.findViewById(R.id.changeLayout)).setTitleText("LAYOUT").setDelay(500).setTargetTouchable(false).useFadeAnimation().setFadeDuration(200).
                        setContentText("Memify using 1 or 2 images").setDismissText("GOT IT").setContentTextColor(-1).singleUse("1").setListener(new IShowcaseListener() {


                    @Override
                    public void onShowcaseDisplayed(MaterialShowcaseView materialShowcaseView) {

                    }

                    @Override
                    public void onShowcaseDismissed(MaterialShowcaseView materialShowcaseView) {
                        new MaterialShowcaseView.Builder(activity).setTarget(activity.findViewById(R.id.goToGallery)).setTitleText("IMAGE").setDelay(500).setTargetTouchable(false).useFadeAnimation().setFadeDuration(200).
                                setContentText("Hurray!! Choose your Meme image from gallery").setDismissText("GOT IT").setListener(new IShowcaseListener() {
                            @Override
                            public void onShowcaseDisplayed(MaterialShowcaseView materialShowcaseView) {

                            }

                            @Override
                            public void onShowcaseDismissed(MaterialShowcaseView materialShowcaseView) {
                                new MaterialShowcaseView.Builder(activity).setTarget(activity.findViewById(R.id.edit)).setTitleText("EDIT").setDelay(500).setTargetTouchable(false).useFadeAnimation().setFadeDuration(200).
                                        setContentText("Vibrant colours and size for your Meme text").setDismissText("GOT IT").setListener(new IShowcaseListener() {
                                    @Override
                                    public void onShowcaseDisplayed(MaterialShowcaseView materialShowcaseView) {

                                    }

                                    @Override
                                    public void onShowcaseDismissed(MaterialShowcaseView materialShowcaseView) {
                                        new MaterialShowcaseView.Builder(activity).setTarget(fab).setTitleText("OPTIONS").setDelay(500).setTargetTouchable(false).useFadeAnimation().setFadeDuration(200).
                                                setContentText("Socialize your Meme - Share or Download").setDismissText("GOT IT").setListener(new IShowcaseListener() {
                                            @Override
                                            public void onShowcaseDisplayed(MaterialShowcaseView materialShowcaseView) {

                                            }

                                            @Override
                                            public void onShowcaseDismissed(MaterialShowcaseView materialShowcaseView) {
                                                openSubMenusFab(fab1, fab2, fab);
                                                new MaterialShowcaseView.Builder(activity).setTarget(fab1).setTitleText("DOWNLOAD").setDelay(100).setTargetTouchable(false).useFadeAnimation().setFadeDuration(200).
                                                        setContentText("Your Meme goes right into your Gallery").setDismissText("GOT IT").setListener(new IShowcaseListener() {
                                                    @Override
                                                    public void onShowcaseDisplayed(MaterialShowcaseView materialShowcaseView) {

                                                    }

                                                    @Override
                                                    public void onShowcaseDismissed(MaterialShowcaseView materialShowcaseView) {
                                                        new MaterialShowcaseView.Builder(activity).setTarget(fab2).setTitleText("SHARE").setDelay(100).setTargetTouchable(false).useFadeAnimation().setFadeDuration(200).
                                                                setContentText("Share Memes, Share Love").setDismissText("GOT IT").setListener(new IShowcaseListener() {
                                                            @Override
                                                            public void onShowcaseDisplayed(MaterialShowcaseView materialShowcaseView) {

                                                            }

                                                            @Override
                                                            public void onShowcaseDismissed(MaterialShowcaseView materialShowcaseView) {
                                                                closeSubMenusFab(fab1, fab2, fab);
                                                                new MaterialShowcaseView.Builder(activity).setTarget(topText).setTitleText("DRAG AND DROP").setDelay(100).setTargetTouchable(false).useFadeAnimation().setFadeDuration(200).
                                                                        setContentText("Long press and move the text around \n\nHAPPY MEMEING!!!").setDismissText("GOT IT").setListener(new IShowcaseListener() {
                                                                    @Override
                                                                    public void onShowcaseDisplayed(MaterialShowcaseView materialShowcaseView) {

                                                                    }

                                                                    @Override
                                                                    public void onShowcaseDismissed(MaterialShowcaseView materialShowcaseView) {

                                                                    }
                                                                }).show();
                                                            }
                                                        }).show();
                                                    }
                                                }).show();
                                            }
                                        }).show();
                                    }
                                }).show();
                            }
                        }).show();
                    }
                }).show();
            }
        });
    }

    public void closeSubMenusFab(FloatingActionButton fab1, FloatingActionButton fab2, FloatingActionButton fab) {
        fab1.setVisibility(View.INVISIBLE);
        fab2.setVisibility(View.INVISIBLE);
        fab.setImageResource(R.drawable.ic_file_download_black_24dp);
        fabExpanded = false;

    }

    //Opens FAB submenus
    private void openSubMenusFab(FloatingActionButton fab1, FloatingActionButton fab2, FloatingActionButton fab) {
        fab1.setVisibility(View.VISIBLE);
        fab2.setVisibility(View.VISIBLE);
        //Change settings icon to 'X' icon
        fab.setImageResource(R.drawable.ic_close_black_24dp);
        fabExpanded = true;
    }

    public void fabClick(final FloatingActionButton fab1, final FloatingActionButton fab2, final FloatingActionButton fab, final Activity activity) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded) {
                    closeSubMenusFab(fab1, fab2, fab);
                } else {
                    openSubMenusFab(fab1, fab2, fab);
                }
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissions(activity);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharePhoto(activity);
            }
        });
    }

    private void requestPermissions(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_REQUEST_CODE);
        } else {
            downloadImage(activity);
        }
    }

    public void downloadImage(Activity activity) {

        new ImageDownloader(activity).execute();

    }


    private class ImageDownloader extends AsyncTask<String, Void, Boolean> {
        private Activity activity;

        public ImageDownloader(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            waitDialog = ProgressDialog.show(activity, "Please wait", "Downloading image");
            waitDialog.setCancelable(false);
        }

        @Override
        protected void onPostExecute(Boolean flag) {

            waitDialog.dismiss();

            if (flag) {

                new AlertDialog.Builder(activity).setPositiveButton("OK", null).setTitle("Success").setMessage("\nDownload complete!").show();

            } else {

                new AlertDialog.Builder(activity).setPositiveButton("OK", null).setTitle("Error").setMessage("\nDownload Incomplete, Try again...").show();

            }
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            rootLayout = activity.findViewById(R.id.frame);
            rootLayout.setDrawingCacheEnabled(true);
            Bitmap image1 = rootLayout.getDrawingCache();
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File dir = new File(path + "/");
            dir.mkdirs();
            File file = new File(dir, System.currentTimeMillis() + ".png");
            OutputStream out;
            try {
                out = new FileOutputStream(file);
                image1.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();

            } catch (java.io.IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

    }

    private void sharePhoto(Activity activity) {
        createCompositeImage(activity);
        createSharedIntent(activity);

    }

    private void createSharedIntent(Activity activity) {
        Intent sharedIntent = new Intent(Intent.ACTION_SEND);
        File sharedFile = new File(activity.getCacheDir(), "images/image.png");
        Uri imageUri = FileProvider.getUriForFile(activity, "com.neerk.memegenerator.fileprovider", sharedFile);
        sharedIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        sharedIntent.setType("image/png");
        activity.startActivity(Intent.createChooser(sharedIntent, "Share using"));
    }

    private void createCompositeImage(Activity activity) {
        FrameLayout frameLayout = activity.findViewById(R.id.frame);
        frameLayout.setDrawingCacheEnabled(true);
        Bitmap bitmap = frameLayout.getDrawingCache();

        File sharedFile = new File(activity.getCacheDir(), "images");
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

    public View.OnLongClickListener TextOnLongClickListener() {

        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData dragdata = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(dragdata, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            }
        };
    }


    public float getCameraPhotoOrientation(Uri photoUri,Activity activity) {
        String path = "";
        if ("content".equalsIgnoreCase(photoUri.getScheme())) {
            path = getDataColumn(activity, photoUri, null, null);
        }

        Log.d("path", photoUri.getPath());
        Log.d("pathString", path);
        float rotate = 0f;
        File file = new File(path);
        try {
            FileInputStream inputStream = new FileInputStream(file.getAbsolutePath());

            ExifInterface exif;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                exif = new ExifInterface(inputStream);

                String exifOrientation = exif
                        .getAttribute(ExifInterface.TAG_ORIENTATION);
                Log.d("exifOrientation", exifOrientation);
                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);
                Log.d("orientation", "orientation :" + orientation);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270f;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180f;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90f;
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate;
    }
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return "";
    }
}
