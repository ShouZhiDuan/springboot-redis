package com.redis.dto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
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
//@ProtobufClass
public class UserDto {
    private static final long serialVersionUID = -2140242550063332020L;

    @Protobuf(fieldType = FieldType.STRING, order = 1, required = true)
    private String name;

    @Protobuf(fieldType = FieldType.INT32, order = 2, required = true)
    private int age;
}
