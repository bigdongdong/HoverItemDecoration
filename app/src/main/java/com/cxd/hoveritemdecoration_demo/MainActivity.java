package com.cxd.hoveritemdecoration_demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.cxd.hoveritemdecoration.HoverItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = this ;

        RecyclerView recycler = findViewById(R.id.recycler);

        recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        recycler.addItemDecoration(new HoverItemDecoration(DensityUtil.dip2px(context,50)){
            @Override
            protected List<Integer> getHoverPosis() {
                List<Integer> posis = new ArrayList<>();
                for (int i = 0; i < 30; i++) {
                    posis.add(i);
                }
                return posis;
            }

            @Override
            protected void drawHover(Canvas c, int position, Rect rect) {
                c.drawColor(Color.parseColor("#BBFFFF"));
                Log.i("aaa", "drawHover: "+rect.toString());

                Path path = new Path();
                path.moveTo(100,rect.centerY());
                path.rLineTo(rect.right - 100 ,0);
                path.close();

                Paint paint = new Paint();
                paint.setTextSize(DensityUtil.sp2px(context,14));
                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(3);
                paint.setColor(Color.BLACK);
                c.drawTextOnPath("position : "+position,path,0,14 ,paint);
                c.drawPath(path,paint);
//
                path = new Path();
                path.moveTo(rect.left,rect.top + 2.5f);
                path.rLineTo(rect.right ,0);
                path.close();
                paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                paint.setColor(Color.RED);
                c.drawPath(path,paint);
            }
        });

        QuickAdapter adapter = new QuickAdapter(this) {
            @Override
            protected Object getEmptyIdOrView() {
                return null;
            }

            @Override
            protected Object getItemViewOrId() {
                RecyclerView recycler = new RecyclerView(context);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1,-2);
                recycler.setLayoutParams(params);
                recycler.setNestedScrollingEnabled(false);
                recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                        super.getItemOffsets(outRect, view, parent, state);
                        outRect.bottom = DensityUtil.dip2px(context,10);
                        final int transX = (ScreenUtil.getScreenWidth(context) - DensityUtil.dip2px(context,300))/6;
                        view.setTranslationX(transX);
                    }
                });
                recycler.setLayoutManager(new GridLayoutManager(context,3){
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                });

                final QuickAdapter adapter1 = new QuickAdapter(context) {
                    @Override
                    protected Object getEmptyIdOrView() {
                        return null;
                    }

                    @Override
                    protected Object getItemViewOrId() {
                        View v = new View(context);
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DensityUtil.dip2px(context,100),DensityUtil.dip2px(context,100));
                        v.setLayoutParams(params);
                        v.setBackgroundColor(Color.GRAY);
                        return v;
                    }

                    @Override
                    protected void onBindViewHolder(ViewHolder holder, Object o, int position) {

                    }
                };
                recycler.setAdapter(adapter1);
                adapter1.doTest(new Random().nextInt(10));

                return recycler;
            }

            @Override
            protected void onBindViewHolder(ViewHolder holder, Object o, int position) {

            }
        };
        recycler.setAdapter(adapter);

        adapter.doTest(30);
    }
}
