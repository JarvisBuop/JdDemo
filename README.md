# JdDemo
# study and test
http://blog.csdn.net/MrJarvisDong/article/details/78008834
## ShowTime

![圆形扫码界面](https://github.com/JarvisBuop/JdDemo/blob/master/circle_scan2.gif "Scan by Circle")

![方形扫码界面](https://github.com/JarvisBuop/JdDemo/blob/master/round_scan.gif "Scan by Round")

## 简化Zxing 条码扫描;

1.简化ZXing代码,删除无用resultHandler类别;

2.自定义扫码参数设置,建造者构建参数,intent传递对象;

3.返回值分两种情况:单个扫描和多个扫描,onactivityresult中获取也分两种情况;

4.屏幕旋转已处理;

5.策略模式,添加正方形扫描,音量键动画,圆形扫描,环绕动画,
  可自行定制扫码页面;


## Tips:
待:

1. 6.0权限,否则会出现错误open() 抛出异常;

