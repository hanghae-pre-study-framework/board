package com.hanghae.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(modifyOnCreate = false)
@Configuration
public class JpaAuditingConfig {

}
