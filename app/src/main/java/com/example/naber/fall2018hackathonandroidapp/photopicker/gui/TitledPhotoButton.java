package com.example.naber.fall2018hackathonandroidapp.photopicker.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class TitledPhotoButton extends PhotoButton {

    private String title = "";

    public TitledPhotoButton(Context context, String imageUri) {
        super(context, imageUri);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint topRect = new Paint();
        topRect.setColor(Color.DKGRAY);
        topRect.setAlpha(150);
        canvas.drawRect(new Rect(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), 60), topRect);

        Paint txtPainnt = new Paint();
        txtPainnt.setTextSize(30f);
        txtPainnt.setFakeBoldText(true);
        txtPainnt.setColor(Color.WHITE);
        canvas.drawText(title,getPaddingLeft() + 10,getPaddingTop() + 30, txtPainnt);

    }
}
