package com.redis.pipline;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class RedisPiplineTests {

    @Autowired
    RedisTemplate<Object, Object> fastRedisTemplate;


    /**
     * pipline测试
     */
    @Test
    void testPipline(){
        fastRedisTemplate.opsForValue().set("test","1");
    }


}
