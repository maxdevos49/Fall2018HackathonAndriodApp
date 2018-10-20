package com.example.naber.fall2018hackathonandroidapp.photopicker.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.widget.ImageView;

import com.example.naber.fall2018hackathonandroidapp.R;

public class TogglablePhotoButton extends PhotoButton {

    private static final String LOG_ID = TogglablePhotoButton.class.getSimpleName();

    boolean isClicked;
    private static Bitmap CHECKMARK;

    public TogglablePhotoButton(Context context, String imageUri) {
        super(context, imageUri);

        if (CHECKMARK == null) {
            CHECKMARK = BitmapFactory.decodeResource(getResources(), R.drawable.checkmark);
        }

        isClicked = false;
        super.setOnClickListener((view) -> {
            isClicked = !isClicked;
            invalidate();
            Log.i(LOG_ID, "Toggling state to: " + isClicked);
        });
    }

    public boolean isClicked() {
        return isClicked;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (isClicked) {
            Paint pnt = new Paint();
            pnt.setColor(Color.GRAY);
            pnt.setAlpha(150);
            canvas.drawRect(new Rect(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom()), pnt);
            canvas.drawBitmap(CHECKMARK, 40,40, null);
        }
    }
}
