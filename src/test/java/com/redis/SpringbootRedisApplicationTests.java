package com.redis;

import com.redis.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class SpringbootRedisApplicationTests {

//    @Resource
//    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    RedisTemplate<Object, Object> jacksonRedisTemplate;
    @Autowired
    RedisTemplate<Object, Object> fastRedisTemplate;

    /**
     * jsckson序列化测试
     */
    @Test
    void jacksonTest() {
        jacksonRedisTemplate.opsForValue().set("test:user:jackson:name","Hello jakson");
        jacksonRedisTemplate.opsForValue().set("test:user:jackson",new UserDto("testNme",1));
        UserDto userDto = (UserDto) jacksonRedisTemplate.opsForValue().get("test:user:jackson");
        System.out.println(userDto);
    }

    /**
     * fast序列化测试
     */
    @Test
    void fastTest() {
        fastRedisTemplate.opsForValue().set("test:user:fast:name","Hello fast");
        fastRedisTemplate.opsForValue().set("test:user:fast",new UserDto("testNme",1));
        UserDto userDto = (UserDto) fastRedisTemplate.opsForValue().get("test:user:fast");
        System.out.println(userDto);
    }







}
