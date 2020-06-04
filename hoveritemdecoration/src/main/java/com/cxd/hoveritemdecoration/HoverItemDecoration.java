package com.cxd.hoveritemdecoration;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 * create by chenxiaodong on 2020/6/4
 * 分组悬停 recycler 装饰类
 * 需要重写 :
 *      {@link HoverItemDecoration#getHoverPosis} -> 告诉装饰类哪些item的顶部需要留出hover的位置
 *      {@link HoverItemDecoration#drawHover(Canvas, int, Rect)} -> 绘制hover内容，Rect是hover的坐标
 */
public abstract class HoverItemDecoration extends RecyclerView.ItemDecoration {
    private final int mHoverHeight;
    private final List<Integer> mHoverPosis ;


    /**
     * @param hoverHeight hover高度
     */
    public HoverItemDecoration(int hoverHeight) {
        this.mHoverHeight = hoverHeight;
        mHoverPosis = getHoverPosis() ;
    }

    private Path getOnDrawPath(View view){
        Path p = new Path();
        p.moveTo(view.getLeft(),view.getTop() - mHoverHeight);
        p.rLineTo(view.getWidth(),0);
        p.rLineTo(0,mHoverHeight);
        p.rLineTo(-view.getWidth(),0);
        p.close();
        return p;
    }

    private Path getOnDrawOverPath(View parent ,Integer disance){
        if(disance == null){
            disance = mHoverHeight ;
        }

        Path p = new Path();
        p.moveTo(parent.getPaddingLeft() ,0 - (mHoverHeight - disance));
        p.rLineTo(parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight(),0);
        p.rLineTo(0,mHoverHeight);
        p.rLineTo(-parent.getWidth() + parent.getPaddingLeft() + parent.getPaddingRight(),0);
        p.close();
        return p;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

        RecyclerView.Adapter adapter = parent.getAdapter();
        if(adapter != null){
            final int count = parent.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = parent.getChildAt(i);
                int position = parent.getChildAdapterPosition(child);
                if(isHover(position)){
                    c.save();
                    c.clipPath(getOnDrawPath(child));
                    drawHover(c,position,new Rect(child.getLeft(),
                            child.getTop() - mHoverHeight,child.getRight(),child.getTop()));
                    c.restore();
                }
            }
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        RecyclerView.LayoutManager lm = parent.getLayoutManager();
        if(!(lm instanceof LinearLayoutManager)){
            return;
        }
        int position = ((LinearLayoutManager) lm).findFirstVisibleItemPosition();
        if(position == RecyclerView.NO_POSITION){
            return;
        }

        if(parent.getAdapter() != null){
            int curHoverPosi = 0 ;
            for (int i = 0; i < mHoverPosis.size(); i++) {
                if(mHoverPosis.get(i) > position){
                    break;
                }else {
                    curHoverPosi = mHoverPosis.get(i);
                }
            }


            /*绘制顶部hover以及hover的变化*/
            c.save();
            if(isHover(position+1) && parent.getChildAt(1) != null &&
                    parent.getChildAt(1).getTop() - mHoverHeight <= mHoverHeight){
                final int disance = parent.getChildAt(1).getTop() - mHoverHeight ;
                c.clipPath(getOnDrawOverPath(parent,disance));
                drawHover(c,curHoverPosi,new Rect(0, - (mHoverHeight - disance),parent.getWidth(),disance));

            }else{
                c.clipPath(getOnDrawOverPath(parent,null));
                drawHover(c,curHoverPosi,new Rect(0,0,parent.getWidth(),mHoverHeight));
            }
            c.restore();
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);
        if(isHover(position)){
            outRect.top = mHoverHeight ;
        }
    }


    /**
     * 获取所有需要展示hover的position集合
     * @return
     */
    protected abstract List<Integer> getHoverPosis();

    /**
     *
     * @param c
     * @param position 对应的hover的position
     * @param rect hover区域的矩形框
     */
    protected abstract void drawHover(Canvas c , int position ,Rect rect);


    /**
     * 判断对应position是否需要hover
     * @param position
     * @return
     */
    private boolean isHover(int position){
        if(mHoverPosis == null){
            return false;
        }
        return mHoverPosis.contains(position);
    }
}
