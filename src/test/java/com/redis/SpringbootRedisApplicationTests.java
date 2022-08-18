package com.redis;

import com.redis.auto_proto.RedisProtoClient;
import com.redis.auto_proto.TestProtoDTO;
import com.redis.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
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
    @Autowired
    RedisTemplate<Object, Object> protobufRedisTemplate;
    @Autowired
    RedisProtoClient redisProtoClient;

    /**
     * jsckson序列化测试
     */
    @Test
    void jacksonTest() {
        jacksonRedisTemplate.opsForValue().set("test:user:jackson:name", "Hello jakson");
        jacksonRedisTemplate.opsForValue().set("test:user:jackson", new UserDto("testNme", 1));
        UserDto userDto = (UserDto) jacksonRedisTemplate.opsForValue().get("test:user:jackson");
        System.out.println(userDto);
    }

    /**
     * fast序列化测试
     */
    @Test
    void fastTest() {
        fastRedisTemplate.opsForValue().set("test:user:fast:name", "Hello fast");
        fastRedisTemplate.opsForValue().set("test:user:fast", new UserDto("testNme", 1));
        UserDto userDto = (UserDto) fastRedisTemplate.opsForValue().get("test:user:fast");
        System.out.println(userDto);
    }

    /**
     * protobuf序列化测试
     */
    @Test
    void protobufTest() {
        //String txt = "Hello proto";
        //protobufRedisTemplate.opsForValue().set("test:user:proto:name",txt);

//        UserDto userDto = new UserDto("testNme", 1);
//        protobufRedisTemplate.opsForValue().set("test:user:proto10",userDto);

        UserDto userDto = (UserDto) protobufRedisTemplate.opsForValue().get("test:user:proto10");
        System.out.println();
    }

    /**
     * Redis CallBack
     */
    @Test
    void testCalback() {
        byte[] execute = fastRedisTemplate.execute((RedisCallback<byte[]>) connection -> connection.get("test:user:proto10".getBytes()));
        System.out.println(execute);
    }


    @Test
    public void testProtoSet() {
        TestProtoDTO testProtoDTO = new TestProtoDTO();
        testProtoDTO.setName("testName");
        testProtoDTO.setAge(1);
        redisProtoClient.set("test_proto_1", testProtoDTO);
    }


    @Test
    public void testSet() {
        TestProtoDTO testProtoDTO = new TestProtoDTO();
        testProtoDTO.setName("testName");
        testProtoDTO.setAge(1);
        fastRedisTemplate.opsForValue().set("test_fast_proto_1", testProtoDTO);
    }

    @Test
    public void testPerformance() {
        long l1 = System.currentTimeMillis();
        for(int a = 0; a < 100000000; a++){
            fastRedisTemplate.opsForValue().set(a, a);
        }
        long l2 = System.currentTimeMillis();
        System.out.println((l2-l1)/1000);
    }
    @Test
    public void testPipeline() {
       fastRedisTemplate.executePipelined(new RedisCallback<Object>() {
               @Override
               public Object doInRedis(RedisConnection connection) throws DataAccessException {
                   connection.openPipeline();
                   long l1 = System.currentTimeMillis();
                   for(int a = 0; a < 5000000; a++){
                       connection.set(String.valueOf(a).getBytes(), String.valueOf(a).getBytes());
                   }
                   long l2 = System.currentTimeMillis();
                   System.out.println((l2-l1)/1000);
                   return null;
               }
          }
       );
    }


}
