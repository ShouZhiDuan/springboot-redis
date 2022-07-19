package com.redis;

import com.redis.auto_proto.RedisProtoClient;
import com.redis.auto_proto.TestProtoDTO;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * Redis Protobuf测试
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@NoArgsConstructor
class RedisProtobufTests {

    @Autowired
    private RedisProtoClient redisProtoClient;

    /**
     * Redis Protobuf测试
     */
    @Test
    public void testSet(){
        TestProtoDTO testProtoDTO = new TestProtoDTO();
        testProtoDTO.setName("testName");
        testProtoDTO.setAge(1);
        redisProtoClient.set("test_proto",testProtoDTO);
    }


}
