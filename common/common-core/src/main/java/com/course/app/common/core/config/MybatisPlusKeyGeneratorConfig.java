package com.course.app.common.core.config;

import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 仅仅用于Oracle，基于Sequence计算自增字段值的生成器。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Configuration
public class MybatisPlusKeyGeneratorConfig {

    @Bean
    public IKeyGenerator keyGenerator() {
        return new OracleKeyGenerator();
    }
}
