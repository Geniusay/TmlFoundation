# Result 企业级HTTP响应类

## 概述

Result类是一个企业级的HTTP通信响应类，专为现代微服务架构设计。它提供了丰富的功能特性，包括可扩展性、链路追踪、压缩传输、动态时区支持等，旨在为开发者提供一个高效、灵活、易用的响应处理解决方案。

## 核心特性

### 🚀 基础功能
- **泛型支持**: 支持任意类型的数据返回
- **标准属性**: 包含状态码、状态、消息、数据、时间戳等标准字段
- **便捷方法**: 提供success()、error()等静态方法，简化使用
- **建造者模式**: 支持链式调用，构建复杂响应对象

### 🔧 高级特性
- **可扩展架构**: 支持自定义扩展属性和功能
- **动态时区**: 时间戳可根据配置动态调整时区
- **链路追踪**: 内置TraceId支持，便于微服务间调用追踪
- **压缩传输**: 支持多种压缩算法（GZIP、LZ4等），减少带宽占用
- **轻量化设计**: 优化内存使用，提供高性能表现
- **配置管理**: 统一的配置管理系统，支持环境切换

## 快速开始

### 基本用法

```java
// 成功响应
Result<String> success = Result.success("操作成功", "Hello World");

// 错误响应
Result<Void> error = Result.error("操作失败");

// 验证错误
Result<Void> validationError = Result.validationError("参数不能为空");

// 未授权
Result<Void> unauthorized = Result.unauthorized("用户未登录");
```

### 建造者模式

```java
Result<Map<String, Object>> result = new Result.Builder<Map<String, Object>>()
    .code(200)
    .status("success")
    .message("查询成功")
    .data(userData)
    .extension("page", 1)
    .extension("total", 100)
    .build();
```

### 链式调用

```java
Result<List<User>> result = Result.success("用户列表", userList)
    .addExtension("count", userList.size())
    .addExtension("hasMore", false)
    .withTraceId("custom-trace-id");
```

## 功能详解

### 1. 时区配置

```java
// 设置全局默认时区
Result.setDefaultTimeZone(ZoneId.of("Asia/Shanghai"));

// 或使用配置管理
ResultConfig.builder()
    .timezone("UTC")
    .apply();
```

### 2. 压缩功能

```java
// 设置压缩策略
CompressionFactory.setDefaultStrategy("gzip");

// 智能选择压缩策略
Result.CompressionStrategy strategy = CompressionFactory
    .selectOptimalStrategy(dataSize, "json");

// 注册自定义压缩策略
CompressionFactory.registerStrategy("custom", new CustomCompressionStrategy());
```

### 3. 链路追踪

```java
// 自动生成TraceId
Result<String> result1 = Result.success("服务A响应");
String traceId = result1.getTraceId();

// 传递TraceId到下游服务
Result<String> result2 = new Result.Builder<String>()
    .code(200)
    .status("success")
    .message("服务B响应")
    .traceId(traceId)
    .build();
```

### 4. 扩展属性

```java
// 添加全局扩展属性
Result.addGlobalExtension("version", "1.0.0");
Result.addGlobalExtension("environment", "production");

// 添加特定扩展属性
result.addExtension("executionTime", 150)
      .addExtension("cacheHit", true);
```

### 5. 配置管理

```java
// 使用构建器配置
ResultConfig.builder()
    .timezone("Asia/Shanghai")
    .compressionStrategy("gzip")
    .compressionMinSize(1024)
    .traceEnabled(true)
    .performanceMonitoring(false)
    .apply();

// 快速环境配置
ResultConfig.QuickConfig.production();  // 生产环境
ResultConfig.QuickConfig.development(); // 开发环境
ResultConfig.QuickConfig.highPerformance(); // 高性能
ResultConfig.QuickConfig.minimalBandwidth(); // 最小带宽
```

## 压缩策略

### 内置策略

| 策略名称 | 描述 | 适用场景 |
|---------|------|----------|
| none | 无压缩 | 小数据量、开发环境 |
| gzip | GZIP压缩 | 文本数据、平衡压缩率和速度 |
| lz4 | LZ4压缩 | 大数据量、速度优先 |

### 自定义压缩策略

```java
public class CustomCompressionStrategy implements Result.CompressionStrategy {
    @Override
    public <T> Result<T> compress(Result<T> result) {
        // 自定义压缩逻辑
        return result;
    }
    
    @Override
    public <T> Result<T> decompress(Result<T> result) {
        // 自定义解压逻辑
        return result;
    }
}

// 注册自定义策略
CompressionFactory.registerStrategy("custom", new CustomCompressionStrategy());
```

## 最佳实践

### 1. Controller中的使用

```java
@RestController
public class UserController {
    
    @GetMapping("/users/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        try {
            User user = userService.findById(id);
            if (user == null) {
                return Result.notFound("用户不存在");
            }
            return Result.success("查询成功", user);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/users")
    public Result<User> createUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            User user = userService.create(userDTO);
            return Result.success("创建成功", user)
                .addExtension("location", "/users/" + user.getId());
        } catch (ValidationException e) {
            return Result.validationError(e.getMessage());
        } catch (Exception e) {
            return Result.error("创建失败: " + e.getMessage());
        }
    }
}
```

### 2. 微服务间调用

```java
@Service
public class OrderService {
    
    public Result<Order> createOrder(OrderDTO orderDTO) {
        // 调用用户服务
        Result<User> userResult = userServiceClient.getUser(orderDTO.getUserId());
        if (userResult.isError()) {
            return Result.error("用户验证失败")
                .withTraceId(userResult.getTraceId());
        }
        
        // 调用库存服务
        Result<Void> stockResult = stockServiceClient.reserveStock(orderDTO.getItems());
        if (stockResult.isError()) {
            return Result.error("库存不足")
                .withTraceId(stockResult.getTraceId());
        }
        
        // 创建订单
        Order order = createOrderInternal(orderDTO);
        return Result.success("订单创建成功", order)
            .addExtension("userId", orderDTO.getUserId())
            .addExtension("totalAmount", order.getTotalAmount());
    }
}
```

### 3. 异常处理

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ValidationException.class)
    public Result<Void> handleValidation(ValidationException e) {
        return Result.validationError(e.getMessage())
            .addExtension("field", e.getField())
            .addExtension("rejectedValue", e.getRejectedValue());
    }
    
    @ExceptionHandler(UnauthorizedException.class)
    public Result<Void> handleUnauthorized(UnauthorizedException e) {
        return Result.unauthorized("访问被拒绝")
            .addExtension("requiredRole", e.getRequiredRole());
    }
    
    @ExceptionHandler(Exception.class)
    public Result<Void> handleGeneral(Exception e) {
        return Result.error("系统内部错误")
            .addExtension("errorType", e.getClass().getSimpleName())
            .addExtension("timestamp", System.currentTimeMillis());
    }
}
```

## 性能优化

### 1. 压缩配置

```java
// 根据环境选择合适的压缩策略
if (isProduction()) {
    CompressionFactory.setDefaultStrategy("gzip");
} else {
    CompressionFactory.setDefaultStrategy("none");
}

// 设置合理的压缩阈值
ResultConfig.setConfig("result.compression.min-size", 1024);
```

### 2. 内存优化

```java
// 避免在Result中存储大对象
// 推荐使用分页或引用ID
Result<List<String>> result = Result.success("用户ID列表", userIds)
    .addExtension("total", totalCount)
    .addExtension("hasMore", hasMore);
```

### 3. 链路追踪优化

```java
// 在高并发场景下可以关闭链路追踪
ResultConfig.setConfig("result.trace.enabled", false);

// 或使用采样策略
if (Math.random() < 0.1) { // 10%采样率
    result.withTraceId(generateTraceId());
}
```

## 扩展开发

### 1. 自定义Result子类

```java
public class PagedResult<T> extends Result<List<T>> {
    private int page;
    private int size;
    private long total;
    
    public static <T> PagedResult<T> success(List<T> data, int page, int size, long total) {
        PagedResult<T> result = new PagedResult<>();
        // 设置属性...
        return result;
    }
}
```

### 2. 自定义扩展属性处理器

```java
public class MetricsExtensionHandler {
    public static void addMetrics(Result<?> result, String operation) {
        result.addExtension("operation", operation)
              .addExtension("timestamp", System.currentTimeMillis())
              .addExtension("server", getServerInfo());
    }
}
```

## 注意事项

1. **序列化兼容性**: Result类实现了Serializable接口，确保在分布式环境下的序列化兼容性
2. **线程安全**: 静态配置方法是线程安全的，但实例方法不保证线程安全
3. **内存使用**: 避免在Result中存储大量数据，推荐使用引用或分页
4. **压缩策略**: 压缩功能会增加CPU开销，需要根据实际场景权衡
5. **扩展属性**: 扩展属性会增加序列化大小，应适度使用

## 版本历史

- **v1.0.0**: 初始版本，包含所有核心功能

## 许可证

Apache License 2.0