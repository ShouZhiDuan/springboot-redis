package com.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * shouzhi@duan
 */
@Slf4j
@SpringBootApplication
public class SpringbootRedisApplication {


    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(SpringbootRedisApplication.class, args);
        RedisTemplate redisTemplate = (RedisTemplate) ctx.getBean("redisTemplate");
        redisTemplate.opsForValue().set("string","test");
        log.info("SpingBoot启动成功");
    }

//    public void test(){
//        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
//    }

}
