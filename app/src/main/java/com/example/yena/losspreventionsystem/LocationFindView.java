package com.example.yena.losspreventionsystem;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by yena on 2016-05-23.
 */
public class LocationFindView extends View {

    private static final int ALPHA_MAX = 255, ALPHA_MIN = 0, ALPHA_RATE = 3;
    private static final int TEXT_SIZE = 100;

    private int lcRadius, mcRadius, scRadius;
    private int lcColor, mcColor, scColor;
    private String label;
    private int labelColor;

    private int alpha;
    private boolean alphaPlus;
    private boolean lcActivated, mcActivated, scActivated;

    private Paint lcPaint, mcPaint, scPaint, labelPaint;
    private int viewWidthHalf, viewHeightHalf;

    public LocationFindView(Context context){
        super(context);


    }

    public LocationFindView(Context context, AttributeSet attrs) {

        super(context, attrs);
        init(attrs);

        lcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lcPaint.setStyle(Paint.Style.FILL);
        mcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mcPaint.setStyle(Paint.Style.FILL);
        scPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scPaint.setStyle(Paint.Style.FILL);
        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPaint.setTextAlign(Paint.Align.CENTER);
        labelPaint.setTextSize(TEXT_SIZE);

    }

    public void init(AttributeSet attrs)
    {
        TypedArray attrsArray = getContext().obtainStyledAttributes(attrs, R.styleable.locationFindView);
        lcColor = attrsArray.getColor(R.styleable.locationFindView_lcColor, 0);
        mcColor = attrsArray.getColor(R.styleable.locationFindView_mcColor, 0);
        scColor = attrsArray.getColor(R.styleable.locationFindView_scColor, 0);
        label = attrsArray.getString(R.styleable.locationFindView_label);
        labelColor = attrsArray.getColor(R.styleable.locationFindView_labelColor, 0);

        alpha = 0;
        alphaPlus = true;
        lcActivated = false;
        mcActivated = false;
        scActivated = false;

        attrsArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        lcPaint.setColor(lcColor);
        mcPaint.setColor(mcColor);
        scPaint.setColor(scColor);
        labelPaint.setColor(labelColor);

        if(lcActivated){
            lcPaint.setAlpha(alpha);
        } else if(mcActivated){
            mcPaint.setAlpha(alpha);
        } else if(scActivated){
            scPaint.setAlpha(alpha);
        }

        canvas.drawCircle(viewWidthHalf, viewHeightHalf, lcRadius, lcPaint);
        canvas.drawCircle(viewWidthHalf, viewHeightHalf, mcRadius, mcPaint);
        canvas.drawCircle(viewWidthHalf, viewHeightHalf, scRadius, scPaint);
        canvas.drawText(label, viewWidthHalf, viewHeightHalf + (TEXT_SIZE / 3), labelPaint);

        if(alphaPlus){
            if(alpha < ALPHA_MAX){
                alpha += ALPHA_RATE;
            } else{
                alphaPlus = false;
            }
        } else{
            if(alpha > ALPHA_MIN){
                alpha -= ALPHA_RATE;
            } else{
                alphaPlus = true;
            }
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidthHalf = this.getMeasuredWidth() / 2;
        viewHeightHalf = this.getMeasuredHeight() / 2;

        lcRadius = viewWidthHalf;
        int tempRadiusHeight = viewHeightHalf;
        if(tempRadiusHeight < lcRadius){
            lcRadius = tempRadiusHeight;
        }
        mcRadius = lcRadius * 2 / 3;
        scRadius = lcRadius / 3;
    }

    public void setLcActivatedTrue(){
        this.lcActivated = true;
        this.mcActivated = false;
        this.scActivated = false;
    }
    public void setMcActivatedTrue(){
        this.lcActivated = false;
        this.mcActivated = true;
        this.scActivated = false;
    }
    public void setScActivatedTrue(){
        this.lcActivated = false;
        this.mcActivated = false;
        this.scActivated = true;
    }
}
