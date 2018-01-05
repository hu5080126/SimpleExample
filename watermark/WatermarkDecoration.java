package eebochina.com.testtechniques.watermark;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * recyclerView 添加水印
 */

public class WatermarkDecoration extends RecyclerView.ItemDecoration {
    
    WatermarkParams mWatermarkParams;
    WaterMarkDrawable mDrawable;
    int mScrollY = 0;
    int mListDrawTextSize = -1;
    
    private WatermarkDecoration(WatermarkParams mWatermarkParams) {
        this.mWatermarkParams = mWatermarkParams;
        mDrawable = new WaterMarkDrawable();
        mListDrawTextSize = mWatermarkParams.mDrawTexts != null
                ? mWatermarkParams.mDrawTexts.size() : -1;
    }
    
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int top = -mScrollY;
        int right = parent.getWidth() / mWatermarkParams.mColumnNum;
        int start = Math.abs(mScrollY / mWatermarkParams.mRowHeight);
        int max = (parent.getHeight() + mScrollY) / mWatermarkParams.mRowHeight + 1;
        for (int i = start; i < max; i++) {
            int tempTop = top + (mWatermarkParams.mRowHeight * start);
            int tempLeft = right / 3;
            for (int j = 0; j < mWatermarkParams.mColumnNum; j++) {
                mDrawable.setBounds(tempLeft, top, tempLeft + right, tempTop + mWatermarkParams.mRowHeight);
                if (mListDrawTextSize == -1) {
                    mDrawable.setDrawTex(mWatermarkParams.mDrawText);
                } else {
                    mDrawable.setDrawTex(mWatermarkParams.mDrawTexts.get((i * mWatermarkParams.mColumnNum + j) % mListDrawTextSize));
                }
                mDrawable.draw(c);
                tempLeft += right;
            }
            top += mWatermarkParams.mRowHeight;
        }
        
    }
    
    //跟踪recyclerView 滚动值
    public void setScrollY(int dy) {
        this.mScrollY += dy;
    }
    
    class WaterMarkDrawable extends Drawable {
        
        Paint mPaint;
        String mTempText;
        
        public WaterMarkDrawable() {
            mPaint = new Paint();
            mPaint.setColor(mWatermarkParams.mTextColor);
            mPaint.setTextSize(mWatermarkParams.mTextSize);
            mPaint.setAlpha((int) (255 * mWatermarkParams.mAlpha));
            mPaint.setAntiAlias(true);
            mPaint.setTextAlign(Paint.Align.LEFT);
        }
        
        @Override
        public void draw(@NonNull Canvas canvas) {
            Rect rect = getBounds();
            canvas.save();
            canvas.rotate(mWatermarkParams.mDegrees, rect.left, rect.bottom);
            canvas.drawText(mTempText, rect.left, rect.bottom, mPaint);
            canvas.restore();
        }
        
        @Override
        public void setAlpha(int alpha) {
        
        }
        
        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {
        
        }
        
        @Override
        public int getOpacity() {
            return PixelFormat.UNKNOWN;
        }
        
        public void setDrawTex(String mDrawTex) {
            this.mTempText = mDrawTex;
        }
    }
    
    public static class Builder {
        private WatermarkParams mWatermarkParams;
        
        //水印背景字符
        public Builder(String drawText) {
            mWatermarkParams = new WatermarkParams(drawText);
        }
        
        //多个水印背景字符
        public Builder(List<String> drawTexts) {
            mWatermarkParams = new WatermarkParams(drawTexts);
        }
        
        //文字大小
        public Builder setTextSize(int textSize) {
            mWatermarkParams.mTextSize = textSize;
            return this;
        }
        
        //文字颜色
        public Builder setTextColor(@ColorInt int textColor) {
            mWatermarkParams.mTextColor = textColor;
            return this;
        }
        
        //展示多少列
        public Builder setColumnNum(int columnNum) {
            mWatermarkParams.mColumnNum = columnNum;
            return this;
        }
        
        //行高
        public Builder setRowHeight(int rowHeight) {
            mWatermarkParams.mRowHeight = rowHeight;
            return this;
        }
        
        //倾斜角度
        public Builder setDegrees(int degrees) {
            mWatermarkParams.mDegrees = degrees;
            return this;
        }
        
        //透明度 0-1
        public Builder setAlpha(float alpha) {
            mWatermarkParams.mAlpha = alpha;
            return this;
        }
        
        public WatermarkDecoration builder() {
            return new WatermarkDecoration(mWatermarkParams);
        }
    }
    
    static class WatermarkParams {
        String mDrawText;
        List<String> mDrawTexts;
        int mTextColor = Color.parseColor("#ebebeb");
        int mTextSize = 40;
        int mColumnNum = 3;
        int mRowHeight = 240;
        int mDegrees = -30;
        float mAlpha = 0.5f;
        
        
        public WatermarkParams(String mDrawText) {
            this.mDrawText = mDrawText;
        }
        
        public WatermarkParams(List<String> mDrawTexts) {
            this.mDrawTexts = mDrawTexts;
        }
    }
}
