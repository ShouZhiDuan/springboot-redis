package com.redis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Auther: ShouZhi@Duan
 * @Date: 2022/2/25 11:48
 * @Description:
 */
@Data
@AllArgsConstructor
public class UserDto {
    private String name;
    private int age;
}
