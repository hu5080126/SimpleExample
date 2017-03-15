package eebochina.com.testtechniques.nestedScroll;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by User on 2017/1/17.
 */
public class NestedParent extends ScrollView implements NestedScrollingParent {

    //方便测试先固定。
    private int maxHeight = 464;
    private RecyclerView mRecyclerView;

    public NestedParent(Context context) {
        super(context);
    }

    public NestedParent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedParent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        super.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return consumed;
    }

    //返回true代表父view消耗滑动速度，子View将不会滑动
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (null == mRecyclerView) mRecyclerView = (RecyclerView) target;
        if (mRecyclerView.computeVerticalScrollOffset() != 0) {
            return false;
        }
        this.fling((int) velocityY);
        return true;
    }

    //对应子view 的dispatchNestedPreScroll方法， 最后一个数组代表消耗的滚动量，下标0代表x轴，下标1代表y轴
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        //判断是否滚动到最大值
        if ( dy >= 0 && this.getScrollY() < maxHeight) {
            if (null == mRecyclerView) mRecyclerView = (RecyclerView) target;
            //计算RecyclerView的偏移量， 等于0的时候说明recyclerView没有滑动，否则应该交给recyclerView自己处理
            if (mRecyclerView.computeVerticalScrollOffset() != 0) return;
            this.smoothScrollBy(dx, dy);
            consumed[1] = dy; //consumed[1]赋值为 dy ，代表父类已经消耗了改滚动。
        }
    }
}
