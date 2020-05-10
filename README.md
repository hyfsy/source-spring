编译项目：

- 如果**spring-core模块**编译失败，用当前路径下的**spring-core.rar**替换
- bojenesis和cglib两个jar编译当时有点问题，如果编译失败，可以尝试在自己的模块下引用看看



添加模块：

- build.gradle文件下添加需要关联的optional，可参考**spring-learn:build.gradle**
- setting.gradle文件下添加新建模块的引用



注意：

- **gradle.properties**文件下已添加JVM运行参数
- 项目的**servlet-api**全部替换为4.0.1，详见**spring-learn-mvc:build.gradle**
- **spring-oxm**的test都被删了，原因报错，如果想看，已经在同目录下压缩过了