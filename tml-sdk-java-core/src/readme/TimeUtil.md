# TimeUtil 时间工具类使用说明

## 概述

`TimeUtil` 是一个高性能的时间工具类，提供多精度的时间戳获取功能。该工具类采用现代Java时间API设计，基于UTC时间标准提供统一的时间戳服务。

## 特性

- ✅ **多精度支持**：支持秒、毫秒、纳秒三种精度
- ✅ **高性能**：基于`Instant`API，性能优异
- ✅ **线程安全**：所有方法都是线程安全的
- ✅ **零依赖**：仅依赖JDK标准库
- ✅ **UTC标准**：基于UTC时间标准，确保全球一致性
- ✅ **优雅设计**：方法重载，使用简便

## 快速开始

### 基本用法

```java
import io.github.timemachinelab.util.time.TimeUtil;
import io.github.timemachinelab.util.time.TimeUtil.TimePrecision;

// 获取当前时间戳（默认毫秒精度）
long timestamp = TimeUtil.getCurrentTimestamp();
System.out.println("当前时间戳: " + timestamp);
```

### 指定精度

```java
// 获取秒级时间戳
long seconds = TimeUtil.getCurrentTimestamp(TimePrecision.SECONDS);
System.out.println("秒级时间戳: " + seconds);

// 获取毫秒级时间戳
long millis = TimeUtil.getCurrentTimestamp(TimePrecision.MILLISECONDS);
System.out.println("毫秒级时间戳: " + millis);

// 获取纳秒级时间戳
long nanos = TimeUtil.getCurrentTimestamp(TimePrecision.NANOSECONDS);
System.out.println("纳秒级时间戳: " + nanos);
```

## API列表

| 方法签名 | 描述 | 返回值 |
|---------|------|--------|
| `getCurrentTimestamp()` | 获取当前时间戳（默认毫秒精度） | `long` |
| `getCurrentTimestamp(TimePrecision precision)` | 获取指定精度的当前时间戳 | `long` |

## API使用与介绍

### 精度枚举

```java
public enum TimePrecision {
    SECONDS,        // 秒级精度
    MILLISECONDS,   // 毫秒级精度（默认）
    NANOSECONDS     // 纳秒级精度
}
```

### 参数说明

- **precision**: 时间精度，可以为`null`（使用毫秒精度）

### 使用示例

```java
// 获取默认毫秒精度时间戳
long timestamp = TimeUtil.getCurrentTimestamp();

// 获取秒级精度时间戳（适用于一般业务场景）
long secondsTimestamp = TimeUtil.getCurrentTimestamp(TimePrecision.SECONDS);

// 获取纳秒级精度时间戳（适用于高精度计时）
long nanosTimestamp = TimeUtil.getCurrentTimestamp(TimePrecision.NANOSECONDS);

// 处理null参数（自动使用默认毫秒精度）
TimePrecision precision = null;
long defaultTimestamp = TimeUtil.getCurrentTimestamp(precision);
```