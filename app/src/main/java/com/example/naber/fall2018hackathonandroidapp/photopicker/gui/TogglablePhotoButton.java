package com.example.naber.fall2018hackathonandroidapp.photopicker.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.Log;

import com.example.naber.fall2018hackathonandroidapp.R;

public class TogglablePhotoButton extends PhotoButton {

    private static final String LOG_ID = TogglablePhotoButton.class.getSimpleName();

    boolean isClicked;
    private static VectorDrawableCompat CHECKMARK;

    public TogglablePhotoButton(Context context, String imageUri) {
        super(context, imageUri);

        if (CHECKMARK == null) {
            CHECKMARK = VectorDrawableCompat.create(getContext().getResources(), R.drawable.checkmark, null);
        }

        isClicked = false;
    }

    public void toggle() {
        isClicked = !isClicked;
        invalidate();
        Log.i(LOG_ID, "Toggling state to: " + isClicked);
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

            CHECKMARK.setBounds(0,0,getWidth() - 10, getHeight() - 10);
            canvas.translate(getPaddingLeft(), getPaddingRight());
            CHECKMARK.draw(canvas);
            canvas.translate(-getPaddingLeft(), -getPaddingRight());

        }
    }
}
