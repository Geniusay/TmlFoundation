package io.github.timemachinelab.common.resp.result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Result配置持有者
 * 用于在静态上下文中访问ResultConfig
 * 
 * @author TimeMachineLab
 * @version 1.0.0
 */
@Component
public class ResultConfigHolder {
    
    @Autowired
    private ResultConfig resultConfig;
    
    private static ResultConfig staticConfig;
    
    @PostConstruct
    public void init() {
        staticConfig = this.resultConfig;
    }
    
    public static ResultConfig getConfig() {
        return staticConfig;
    }
}