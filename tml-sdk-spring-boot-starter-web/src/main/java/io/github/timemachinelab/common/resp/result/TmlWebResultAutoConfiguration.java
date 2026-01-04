package io.github.timemachinelab.common.resp.result;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TmlWebResultProperties.class)
public class TmlWebResultAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AutoRespAdvice autoRespAdvice(ObjectMapper objectMapper) {
        return new AutoRespAdvice(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public AutoRespExceptionResolver autoRespExceptionResolver(ObjectMapper objectMapper) {
        return new AutoRespExceptionResolver(objectMapper);
    }

    @Bean
    public SmartLifecycle tmlWebResultConfigInitializer(TmlWebResultProperties properties) {
        return new SmartLifecycle() {

            private volatile boolean running;

            @Override
            public void start() {
                ResultConfigHolder.setConfig(properties);
                running = true;
            }

            @Override
            public void stop() {
                running = false;
            }

            @Override
            public boolean isRunning() {
                return running;
            }

            @Override
            public boolean isAutoStartup() {
                return true;
            }

            @Override
            public int getPhase() {
                return Integer.MIN_VALUE;
            }
        };
    }
}
