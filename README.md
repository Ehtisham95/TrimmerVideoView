2017/9/27
 下一步功能： update by endAction & scroll 完成

2017/9/28
下一步功能：
问题复杂化，当前默认是截取 10S，并且画面上展示的也是 10帧的图片，程序中已经对 展示的图片数量做了兼容，
那么如果我展示 10帧，但是这10帧代表的却是 20S的视频呢？
再比如，我想展示20S，并且我也想截取20S，然而选取的视屏不足 20S 又该怎么处理呢？  完成

2017/9/29
下一步功能：
为了方便调试，并且也为了可能的需求，在两个拖动的指针上方，添加当前指针所对应得时间点
并且应该为 TrimmerSeekBar 限定最小时间

在这两个完成后，就可开始整体调试，并且需要注意的是，关于 trimmerView release


2017/10/3
最后的问题在于，两边剪切点，对于2个TextView 不准确的问题
