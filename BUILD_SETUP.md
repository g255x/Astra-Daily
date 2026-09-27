# Astra-Daily 构建环境配置说明

支持桌面端（Windows x86_64）和移动端（PojavLauncher ARM64）双平台打包，三层混淆保护。

---

## 1. JDK 版本要求

**必须使用 JDK 22**

| JDK | J2CC 能启动? | ASM 兼容? | 结论 |
|-----|:---:|:---:|:---:|
| 21  | ❌（class v65 < 需要 v66） | ✅ | 不可用 |
| **22** | **✅** | **✅** | **唯一兼容版本** |
| 23+ | ✅ | ❌ `Unsupported class file major version` | 不可用 |

> J2CC 内嵌 ASM 9.7.1，最高支持 Java 22（class major version 66）。

### 配置方法

编辑 `gradle.properties`：

```properties
org.gradle.java.home=D:\\Program Files\\Java\\jdk-22
```

---

## 2. 必备工具目录

```
tools/
├── proguard.jar               # ProGuard 7.10.0
├── j2cc.jar                   # J2CC 原生编译
├── kbox-protector.jar         # KBox 保护器
├── libs/                      # KBox 运行时依赖（asm-*.jar 等）
├── natives/                   # J2CC 原生库（nativeUtils.dll, util.h 等）
└── zig_compiler/              # Zig 0.17.0-dev（zig.exe, lib/, doc/）
```

项目根目录：
```
lib/                           # 本地 Mod 依赖
dic.txt                        # ProGuard 混淆字典
```

---

## 3. Mod 依赖库（lib/）

```
lib/
├── baritone-api-fabric-1.11.2.jar
├── IAS-9.0.6+1.21.1-fabric.jar
└── satin-2.0.0.jar
```

---

## 4. 配置文件

| 文件 | 用途 |
|------|------|
| `gradle.properties` | JDK 路径、Minecraft/Fabric 版本 |
| `build.gradle` | 三层混淆 + 双平台构建脚本 |
| `proguard-rules.pro` | ProGuard 保留规则 |
| `kbox-config.conf` | KBox 混淆强度配置 |
| `settings.gradle` | Fabric Maven 仓库 |
| `dic.txt` | ProGuard 混淆字典 |

---

## 5. 构建命令

```powershell
# 桌面端（x86_64-windows-gnu）
.\gradlew buildProtectedJar
# 产物: build/libs/protected/Astra-Daily-main-obf.jar

# 移动端（aarch64-linux-gnu / PojavLauncher）
.\gradlew buildMobileProtectedJar
# 产物: build/libs/protected/Astra-Daily-main-obf-mobile.jar
```

> 任务自动执行 `clean`，无须手动清理。

---

## 6. 混淆流水线

```
remapJar（原始 JAR）
  │
  ▼ Layer 1: ProGuard —— 名称混淆
  │
  ├── 桌面端: J2CC → x86_64-windows-gnu (.dll)
  └── 移动端: J2CC → aarch64-linux-gnu   (.so)
  │
  ▼ 剥离 Mixin 类
  ▼ Layer 3: KBox —— 控制流(Lv2) + 字符串加密(Lv2) + 反编译器对抗(Lv2)
  ▼ 恢复 Mixin 类
  ▼ 最终产物
```

---

## 7. KBox 混淆强度

| 配置项 | 值 | 说明 |
|--------|:--:|------|
| flattenerLevel | 2 | 中等强度控制流平坦化 |
| 字符串加密 | Level 2 | XOR + AES |
| antiDecompiler | Level 2 | 反编译器对抗 |
| 方法指令数范围 | 32~128 | 只混淆中等大小方法 |

---

## 8. 环境要求

| 要求 | 说明 |
|------|------|
| OS | Windows |
| JDK | **JDK 22** |
| Gradle | 项目自带 `gradlew` |
| 编译器 | 内置 Zig 0.17.0-dev |
| 网络 | 首次需下载 MC/Fabric 依赖 |

---

## 9. 常见问题

### `Unsupported class file major version XX`
JDK 不是 22。在 `gradle.properties` 中设置 `org.gradle.java.home` 指向 JDK 22。

### `nativeUtils.dll not found`
确保 `tools/natives/nativeUtils.dll` 存在。

### ProGuard "can't find referenced class" 警告
正常，Minecraft 中间映射名导致，`proguard-rules.pro` 已 `-ignorewarnings`。

### KBox 报错
检查 `tools/kbox-protector.jar` 和 `tools/libs/` 是否完整，`kbox-config.conf` 是否在根目录。

### 移动端 `__stdcall` / `char` narrowing 错误
`build.gradle` 已自动处理 JNI 头文件切换和 `-fsigned-char` 标志。如仍报错，检查 JDK `include/win32/` 目录权限。