# 小富翁股票投资 · Android App

基于 [xfw-net](https://github.com/jdprs/xfw-net) 网页版用 Capacitor 打包的 Android 应用。

## 功能

- 📳 **横幅震动**：游戏内横幅出现时按类型触发不同强度震动（错误强震、成功轻震等）
- 📤 **存档码分享**：导出存档码时可直接通过系统分享面板发送给朋友
- 📤 **房间号分享**：创建房间后可复制/分享房间号邀请好友
- 📋 **剪贴板自动检测**：启动时自动检测剪贴板中的房间号（6位数字）或存档码，弹窗提示一键加入/导入；App 运行中复制内容也会实时检测

## 下载安装

- Debug 包：`小富翁股票投资-v10.1-debug.apk`（直接安装到 Android 手机，需允许"未知来源"）
- 最低 Android 版本：7.0（API 24）

## 开发构建

```bash
# 1. 同步网页资源（xfw-net 有改动后执行）
rsync -a --delete --exclude='.git' --exclude='node_modules' ../xfw-net/ www/
npx cap sync android

# 2. 构建 APK
cd android
JAVA_HOME=/path/to/jdk21 ANDROID_HOME=/path/to/android-sdk ./gradlew assembleDebug
# 产物：android/app/build/outputs/apk/debug/app-debug.apk
```

### 环境要求
- JDK 21
- Android SDK（compileSdk 36）
- Node.js 18+

## 项目结构

```
xfw-net-app/
├── www/                        # 网页资源（从 xfw-net 同步）
│   └── src/app/44-capacitor-bridge.js   # 原生桥接（震动/分享/剪贴板）
├── android/                    # Capacitor Android 工程
│   └── app/src/main/java/com/xfw/stockgame/
│       ├── MainActivity.java           # 入口，注册自定义插件
│       └── ClipboardWatcher.java       # 剪贴板变化监听插件
├── capacitor.config.json
└── package.json
```

## 原生插件

- `@capacitor/haptics` — 震动
- `@capacitor/clipboard` — 剪贴板读写
- `@capacitor/share` — 系统分享
- `ClipboardWatcher`（自定义）— 监听剪贴板变化并通知 JS
