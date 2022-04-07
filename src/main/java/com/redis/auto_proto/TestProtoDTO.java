package com.redis.auto_proto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: ShouZhi@Duan
 * @Date: 2022/4/7 13:37
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestProtoDTO {
    private String name;
    private Integer age;
}
