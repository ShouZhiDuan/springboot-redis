package com.redis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: ShouZhi@Duan
 * @Date: 2022/2/25 11:48
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String name;
    private int age;
}
