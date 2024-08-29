+ Android Studio 开发练手用

[TOC]

## 按钮列表

+ 需求：传入字符串数组，得到指定数量的单选按钮；超出特定数量的按钮显示在“更多”栏中；可动态修改按钮列表的按钮、显示数量等
+ <img src="https://cdn.jsdelivr.net/gh/Jungezi/wwwImage@master/img/Screenshot_20240829_100343.jpg" style="zoom:30%;" />
+ <img src="https://cdn.jsdelivr.net/gh/Jungezi/wwwImage@master/img/Screenshot_20240829_100401.jpg" style="zoom:30%;" />

## 流式布局

+ 需求：传入View列表，根据宽度进行流式排列，可以动态增减、限制最大行数、修改每一行的对齐方式

| 中间对齐<br><img src="https://cdn.jsdelivr.net/gh/Jungezi/wwwImage@master/img/Screenshot_20240829_100824.jpg" alt="Screenshot_20240829_100824" style="zoom:30%;" /> | 下方对齐<br><img src="https://cdn.jsdelivr.net/gh/Jungezi/wwwImage@master/img/Screenshot_20240829_100846.jpg" alt="Screenshot_20240829_100846" style="zoom:30%;" /> |
| ------------------------------------------------------------ | ------------------------------------------------------------ |

​    

## 可折叠TextView

+ 需求：指定TextView的最大行数后，若行数超出，则生成省略号和“展开”按钮，点击后显示全部文本和“收起”按钮；其中按钮的文本可自定义

| <img src="https://cdn.jsdelivr.net/gh/Jungezi/wwwImage@master/img/Screenshot_20240829_101909.jpg" style="zoom:30%;" /> | <img src="https://cdn.jsdelivr.net/gh/Jungezi/wwwImage@master/img/Screenshot_20240829_101917.jpg" style="zoom:30%;" /> |
| ------------------------------------------------------------ | ------------------------------------------------------------ |



## 折线图绘制

+ 需求：显示折线和坐标轴，触摸可获取最近点的信息，折线下方颜色有渐变
+ <img src="https://cdn.jsdelivr.net/gh/Jungezi/wwwImage@master/img/Screenshot_20240829_101534.jpg" alt="Screenshot_20240829_101534" style="zoom:30%;" />



## 多颜色拖动条

+ 需求：传入包含颜色资源和长度的列表，根据长度的占比生成不同颜色组合成的拖动条，支持wrap_content和match_parent，指定拖动条高度、整体宽度、字体大小、进度指示三角形高度、当前进度，未指定的尺寸根据layoutParams自动调整或设为默认值；同时支持动态增减颜色条、直接拖动进度条并回调得到被**拖动**改变的进度

+ 自动尺寸、适应高度、适应宽度

    <img src="https://cdn.jsdelivr.net/gh/Jungezi/wwwImage@master/img/Screenshot_20240829_102958.jpg" alt="Screenshot_20240829_102958" style="zoom:30%;" />

+ 指定尺寸、新增颜色、指定进度

    <img src="https://cdn.jsdelivr.net/gh/Jungezi/wwwImage@master/img/Screenshot_20240829_103106.jpg" style="zoom:30%;" />

+ 指定尺寸、适应宽度、拖动进度

    <img src="https://cdn.jsdelivr.net/gh/Jungezi/wwwImage@master/img/Screenshot_20240829_103122.jpg" style="zoom:30%;" />