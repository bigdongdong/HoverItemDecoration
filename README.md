# HoverItemDecoration
RecyclerView 分组悬停装饰类

# 项目配置

```
  allprojects {
      repositories {
          ...
          maven { url 'https://jitpack.io' }  //添加jitpack仓库
      }
  }
  
  dependencies {
	  implementation 'com.github.bigdongdong:HoverItemDecoration:1.0' //添加依赖
  }
```

# 使用方式
```java
recycler.addItemDecoration(new HoverItemDecoration(DensityUtil.dip2px(context,50)){
            @Override
            protected List<Integer> getHoverPosis() {
	    	//需要悬停的positions
            }

            @Override
            protected void drawHover(Canvas c, int position, Rect rect) {
                //悬停内容布局
            }
        });
```
