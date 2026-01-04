package io.github.timemachinelab.test.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 企业级测试性能监控器
 * 提供测试执行时间监控、内存使用监控等功能
 * 
 * @author TimeMachineLab
 * @version 1.0.0
 */
public class TestPerformanceMonitor {
    
    private static final Logger logger = LoggerFactory.getLogger(TestPerformanceMonitor.class);
    
    private final ConcurrentHashMap<String, Long> startTimes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> executionCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> totalExecutionTimes = new ConcurrentHashMap<>();
    
    /**
     * 开始监控指定测试方法
     */
    public void startMonitoring(String testMethodName) {
        startTimes.put(testMethodName, System.currentTimeMillis());
        executionCounts.computeIfAbsent(testMethodName, k -> new AtomicLong(0)).incrementAndGet();
        
        // 记录内存使用情况
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        logger.debug("Test [{}] started - Memory usage: {} MB", 
                testMethodName, usedMemory / (1024 * 1024));
    }
    
    /**
     * 结束监控并记录性能数据
     */
    public long stopMonitoring(String testMethodName) {
        Long startTime = startTimes.remove(testMethodName);
        if (startTime == null) {
            logger.warn("No start time found for test method: {}", testMethodName);
            return 0;
        }
        
        long executionTime = System.currentTimeMillis() - startTime;
        totalExecutionTimes.computeIfAbsent(testMethodName, k -> new AtomicLong(0))
                .addAndGet(executionTime);
        
        // 记录执行结果
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        
        logger.info("Test [{}] completed - Execution time: {} ms, Memory usage: {} MB", 
                testMethodName, executionTime, usedMemory / (1024 * 1024));
        
        // 性能警告
        if (executionTime > 5000) { // 超过5秒
            logger.warn("Test [{}] execution time ({} ms) exceeds performance threshold", 
                    testMethodName, executionTime);
        }
        
        return executionTime;
    }
    
    /**
     * 获取测试方法的平均执行时间
     */
    public double getAverageExecutionTime(String testMethodName) {
        AtomicLong totalTime = totalExecutionTimes.get(testMethodName);
        AtomicLong count = executionCounts.get(testMethodName);
        
        if (totalTime == null || count == null || count.get() == 0) {
            return 0.0;
        }
        
        return (double) totalTime.get() / count.get();
    }
    
    /**
     * 获取测试方法的执行次数
     */
    public long getExecutionCount(String testMethodName) {
        AtomicLong count = executionCounts.get(testMethodName);
        return count != null ? count.get() : 0;
    }
    
    /**
     * 打印性能统计报告
     */
    public void printPerformanceReport() {
        logger.info("=== Test Performance Report ===");
        
        executionCounts.forEach((testMethod, count) -> {
            double avgTime = getAverageExecutionTime(testMethod);
            logger.info("Test [{}]: Executions={}, Average Time={:.2f}ms", 
                    testMethod, count.get(), avgTime);
        });
        
        logger.info("=== End of Performance Report ===");
    }
    
    /**
     * 清理监控数据
     */
    public void clear() {
        startTimes.clear();
        executionCounts.clear();
        totalExecutionTimes.clear();
        logger.debug("Performance monitoring data cleared");
    }
    
    /**
     * 执行垃圾回收并记录内存使用情况
     */
    public void forceGCAndLogMemory(String context) {
        Runtime runtime = Runtime.getRuntime();
        long beforeGC = runtime.totalMemory() - runtime.freeMemory();
        
        System.gc();
        
        long afterGC = runtime.totalMemory() - runtime.freeMemory();
        logger.info("Memory usage [{}]: Before GC={} MB, After GC={} MB, Freed={} MB", 
                context, 
                beforeGC / (1024 * 1024), 
                afterGC / (1024 * 1024), 
                (beforeGC - afterGC) / (1024 * 1024));
    }
}