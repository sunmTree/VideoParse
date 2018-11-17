### VideoParse Project

用于学习Android Camera 的项目。 工作中遇到一个需求类似TextView的跑马灯效果，但是
是用于ImageView的，所以在这个项目中自己实现了一个ImageView的跑马灯效果。

#### CircleImageView

- setBitmap(Bitmap bitmap)          设置图片
- setDuration(long duration)        设置动画时长
- setRepeatCount(int repeatCount)   设置动画重复次数
- setCircleModel()  设置跑马灯模式，有四种。 TOP LEFT RIGHT BOTTOM

> 注意： CircleImageView 的onMeasure() 返回的是Bitmap的大小。而且跑马灯也是
依据Bitmap大小实现的。如果使用需要根据项目实际情况做出修改。但跑马灯效果的核心代码
已经有所体现。