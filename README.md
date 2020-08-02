
[![](https://jitpack.io/v/A-Miracle/Gestures-Lock.svg)](https://jitpack.io/#A-Miracle/Gestures-Lock)

# 手势锁，图案锁(Gestures Lock, PatternLock)

- 好久之前写的，最近发现自己又用到了，想起来整理一下
- 欢迎start，有可以优化的点欢迎大佬指导 →_→

## 使用简介
- 核心 NLockBaseView，继承可以自定义样式
- 可参考继承 NLockBitmapView 上修改图片样式
- 可参考继承 NLockColorView 上修改纯绘制样式
- 其他可参考 NLockCustomView，NLockSmallView，NLockView 各种自定义样式等
- 简单手势锁功能逻辑可参考 simple 示例

### Gradle设置


```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.A-Miracle:Gestures-Lock:v1.0.0'
}
```

### Maven的设置

```xml
<!-- <repositories> section of pom.xml -->
<repository>
    <id>jitpack.io</id>
   <url>https://jitpack.io</url>
</repository>

<!-- <dependencies> section of pom.xml -->
<dependency>
    <groupId>com.github.A-Miracle</groupId>
    <artifactId>Gestures-Lock</artifactId>
    <version>v1.1.0</version>
</dependency>
```

<br/>

![image](https://raw.githubusercontent.com/A-Miracle/Gestures-Lock/blob/master/demo2.gif)
![image](https://raw.githubusercontent.com/A-Miracle/Gestures-Lock/blob/master/demo.gif)
