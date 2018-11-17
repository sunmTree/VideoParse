package com.video.module.circle;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class CircleImageView extends View {
    private Paint mPaint;
    private Bitmap mBitmap;
    private int mTranslateLen;
    private long mAnimDuration = 1000;
    private int mRepeatCount = 1;
    private CircleModelImpl mImpl;
    private CircleModel mCircleModel = CircleModel.LEFT;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mImpl = mLeftImpl;
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        invalidate();
    }

    public enum CircleModel {
        LEFT(1),
        RIGHT(1),
        TOP(0),
        BOTTOM(0);

        public int transModel;
        CircleModel(int transModel) {
            this.transModel = transModel;
        }
    }

    public void setCircleModel(CircleModel model) {
        mCircleModel = model;
        switch (model) {
            case TOP:
                mImpl = mTopImpl;
                break;
            case LEFT:
                mImpl = mLeftImpl;
                break;
            case RIGHT:
                mImpl = mRightImpl;
                break;
            case BOTTOM:
                mImpl = mBottomImpl;
                break;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mImpl.onCircleDraw(canvas, mTranslateLen);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mBitmap != null) {
            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();
            int widthSpace = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
            int heightSpace = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            setMeasuredDimension(widthSpace, heightSpace);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setDuration(long duration) {
        this.mAnimDuration = duration;
    }

    public void setRepeatCount(int repeatCount) {
        this.mRepeatCount = repeatCount;
    }

    public void startAnim() {
        int transValue;
        if (mCircleModel.transModel == 1) {
            transValue = mBitmap.getWidth();
        } else {
            transValue = mBitmap.getHeight();
        }
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, transValue);
        valueAnimator.setDuration(mAnimDuration);
        valueAnimator.setRepeatCount(mRepeatCount);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(animation -> {
            mTranslateLen = (int) animation.getAnimatedValue();
            invalidate();
        });
        valueAnimator.start();
    }

    interface CircleModelImpl {
        void onCircleDraw(Canvas canvas, int translate);
    }

    private CircleModelImpl mLeftImpl = new CircleModelImpl() {
        @Override
        public void onCircleDraw(Canvas canvas, int translate) {
            int width = getWidth();
            int height = getHeight();
            // 左侧
            Rect srcRect = new Rect(translate, 0, width, height);
            Rect dstRect = new Rect(0, 0, width - translate, height);
            canvas.drawBitmap(mBitmap, srcRect, dstRect, mPaint);
            // 右侧
            Rect rightSrcRec = new Rect(0, 0, translate, height);
            Rect rightDstRec = new Rect(width - translate, 0, width, height);
            canvas.drawBitmap(mBitmap, rightSrcRec, rightDstRec, mPaint);
        }
    };

    private CircleModelImpl mRightImpl = new CircleModelImpl() {
        @Override
        public void onCircleDraw(Canvas canvas, int translate) {
            int width = getWidth();
            int height = getHeight();

            // 右侧
            Rect rSrcRect = new Rect(0, 0, width - translate, height);
            Rect rDstRect = new Rect(translate, 0, width, height);
            canvas.drawBitmap(mBitmap, rSrcRect, rDstRect, mPaint);

            // 左侧
            Rect lSrcRect = new Rect(width - translate, 0, width, height);
            Rect lDstRect = new Rect(0, 0, translate, height);
            canvas.drawBitmap(mBitmap, lSrcRect, lDstRect, mPaint);
        }
    };

    private CircleModelImpl mTopImpl = new CircleModelImpl() {
        @Override
        public void onCircleDraw(Canvas canvas, int translate) {
            int width = getWidth();
            int height = getHeight();

            // 上方
            Rect tSrcRect = new Rect(0, translate, width, height);
            Rect tDstRect = new Rect(0, 0, width, height - translate);
            canvas.drawBitmap(mBitmap, tSrcRect, tDstRect, mPaint);

            // 下方
            Rect bDstRect = new Rect(0, height - translate, width, height);
            Rect bSrcRect = new Rect(0, 0, width, translate);
            canvas.drawBitmap(mBitmap, bSrcRect, bDstRect, mPaint);
        }
    };

    private CircleModelImpl mBottomImpl = new CircleModelImpl() {
        @Override
        public void onCircleDraw(Canvas canvas, int translate) {
            int width = getWidth();
            int height = getHeight();

            // 上方
            Rect tSrcRect = new Rect(0, height - translate, width, height);
            Rect tDstRect = new Rect(0, 0, width, translate);
            canvas.drawBitmap(mBitmap, tSrcRect, tDstRect, mPaint);

            // 下方
            Rect bSrcRect = new Rect(0, 0, width, height - translate);
            Rect bDstRect = new Rect(0, translate, width, height);
            canvas.drawBitmap(mBitmap, bSrcRect, bDstRect, mPaint);
        }
    };
}
