# 本地编译依赖

将你已有的 NeoForge 1.21.1 GWO 模组 JAR 复制为本目录中的 `gwo.jar`，内部版本应为 `2.12.87`。

GWO 与其授权内容包不属于本项目，不随仓库或 Release 分发。`gwo.jar` 已被 Git 忽略；不要提交第三方模组、授权文件或内容包。

LDLib2 2.2.40 由 Gradle 下载，不需要手动放入本目录。准备好 GWO JAR 和 JDK 21 后，在项目根目录运行 `gradlew.bat clean build`；非 Windows 环境使用 `./gradlew clean build`。
