## 引入Matrix依赖
Reference: https://developer.aliyun.com/article/913595

## 生成Matrix aar包
Reference: https://juejin.cn/post/7078159321250398239

## app使用aar包
1.	将AAR包放到libs目录：
      确保app/libs目录下有你的AAR文件，比如matrixJar-debug.aar。
      如下图

    <img src="https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/e490cc87ca824aefb26acf38f942f8d8~tplv-k3u1fbpfcp-watermark.image?" alt="示例图片" height="400">

2.	配置repositories：
      `flatDir { dirs 'libs' }`告诉Gradle在libs目录中查找平板库（包括.aar文件）。
      代码：
      ```
      repositories {
      flatDir {
      dirs 'libs'
      }
      }
      ```
      老版本配置在app/build.gradle下，新版本配置在settings.gradle下。

3.	配置dependencies：
      在app/build.gradle下
      ```
      implementation fileTree(dir: "libs", include: ["*.jar", "*.aar"])
      ```
      表示从libs目录中包含所有的.jar和.aar文件作为依赖。
