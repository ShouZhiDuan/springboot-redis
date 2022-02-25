package com.redis;

import com.redis.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class SpringbootRedisApplicationTests {

//    @Resource
//    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;




    @Test
    void set() {
        redisTemplate.opsForValue().set("t2","666666");
        redisTemplate.opsForValue().set("t3",new UserDto("testNme",1));
        Object o = redisTemplate.opsForValue().get("t6");
        System.out.println(o.toString());
    }

}
