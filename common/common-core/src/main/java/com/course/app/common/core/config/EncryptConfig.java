package com.course.app.common.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 目前用于用户密码加密，UAA接入应用客户端的client_secret加密。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Configuration
public class EncryptConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
