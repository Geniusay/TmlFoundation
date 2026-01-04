package io.github.timemachinelab.common.resp.result;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tml.web.result")
public class TmlWebResultProperties extends ResultConfig {
}
