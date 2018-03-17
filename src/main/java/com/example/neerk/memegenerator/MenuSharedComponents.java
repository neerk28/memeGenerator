package com.example.neerk.memegenerator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Created by neerk on 08/03/18.
 */

public class MenuSharedComponents {
    private EditText topText;
    private EditText bottomText;
    private int defaultColor;
    private int x, y;
    ConstraintLayout.LayoutParams params;

    public void changeColor(Menu menu) {
        Drawable drawable = menu.findItem(R.id.changeLayout).getIcon();
        if (drawable != null)

        {
            drawable.mutate();
            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }

        Drawable drawable1 = menu.findItem(R.id.goToGallery).getIcon();
        if (drawable1 != null)

        {
            drawable1.mutate();
            drawable1.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }

        Drawable drawable2 = menu.findItem(R.id.edit).getIcon();
        if (drawable2 != null)

        {
            drawable2.mutate();
            drawable2.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
    }

    public void chooseTextSizeFromSpinner(final Activity activity) {

        topText = activity.findViewById(R.id.editText1);
        bottomText = activity.findViewById(R.id.editText2);
        String[] fontSize = activity.getResources().getStringArray(R.array.TextSize);
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        View mView = activity.getLayoutInflater().inflate(R.layout.autocomplete_fontsize, null);
        mBuilder.setTitle("Choose text size");
        final AutoCompleteTextView textView;
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                activity, android.R.layout.simple_list_item_1, fontSize);
        textView = mView.findViewById(R.id.autoCompleteTextView);
        textView.setText(String.valueOf(convertPixelsToSp(topText.getTextSize(), activity.getBaseContext())));
        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.setSelection(textView.getText().length());
            }
        });
        textView.setDropDownVerticalOffset(0);
        textView.setAdapter(arrayAdapter);

        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                try {
                    Float size = Float.parseFloat(textView.getText().toString());
                    if (size < 8 || size >= 60) {
                        Toast.makeText(activity, "Enter a number between 7 and 60 !", Toast.LENGTH_SHORT).show();
                    } else {
                        topText.setTextSize(size);
                        bottomText.setTextSize(size);
                        Toast.makeText(activity, "Text Size set to " + textView.getText().toString(), Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(activity, "Invalid Input! Enter only numbers!", Toast.LENGTH_SHORT).show();
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

    private int convertPixelsToSp(float px, Context context) {
        float sp = px / context.getResources().getDisplayMetrics().scaledDensity;
        return (int) sp;
    }

    public void chooseColorForText(final Activity activity) {
        //ambilwarna stands for color picker - indonesian
        defaultColor = ContextCompat.getColor(activity, R.color.colorPrimary);
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(activity, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                Toast.makeText(activity, "Closed Color Picker", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color;
                topText = activity.findViewById(R.id.editText1);
                bottomText = activity.findViewById(R.id.editText2);
                topText.setTextColor(defaultColor);
                bottomText.setTextColor(defaultColor);
            }
        });
        dialog.show();
    }

    public View.OnDragListener DragListener(Activity activity) {
        topText = activity.findViewById(R.id.editText1);
        bottomText = activity.findViewById(R.id.editText2);
        return new View.OnDragListener() {
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
        };

    }

    @NonNull
    public Intent goToGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        File photoDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        Uri uri = Uri.parse(photoDirectory.getPath());
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

}
