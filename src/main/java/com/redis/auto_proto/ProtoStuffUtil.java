package com.redis.auto_proto;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import lombok.extern.slf4j.Slf4j;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @Auther: ShouZhi@Duan
 * @Date: 2022/4/7 11:52
 * @Description:
 */
@Slf4j
public class ProtoStuffUtil {
    /**
     * 序列化对象
     */
    public static <T> byte[] serialize(T obj) {
        if (obj == null) {
            log.error("Failed to serializer, obj is null");
            throw new RuntimeException("Failed to serializer");
        }

        @SuppressWarnings("unchecked") Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(obj.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(1024 * 1024);
        byte[] protoStuff;
        try {
            protoStuff = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            log.error("Failed to serializer, obj:{}", obj, e);
            throw new RuntimeException("Failed to serializer");
        } finally {
            buffer.clear();
        }
        return protoStuff;
    }

    /**
     * 反序列化对象
     */
    public static <T> T deserialize(byte[] paramArrayOfByte, Class<T> targetClass) {
        if (paramArrayOfByte == null || paramArrayOfByte.length == 0) {
            log.error("Failed to deserialize, byte is empty");
            throw new RuntimeException("Failed to deserialize");
        }

        T instance;
        try {
            instance = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("Failed to deserialize", e);
            throw new RuntimeException("Failed to deserialize");
        }

        Schema<T> schema = RuntimeSchema.getSchema(targetClass);
        ProtostuffIOUtil.mergeFrom(paramArrayOfByte, instance, schema);
        return instance;
    }

    /**
     * 序列化列表
     */
    public static <T> byte[] serializeList(List<T> objList) {
        if (objList == null || objList.isEmpty()) {
            log.error("Failed to serializer, objList is empty");
            throw new RuntimeException("Failed to serializer");
        }

        @SuppressWarnings("unchecked") Schema<T> schema =
                (Schema<T>) RuntimeSchema.getSchema(objList.get(0).getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(1024 * 1024);
        byte[] protoStuff;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            ProtostuffIOUtil.writeListTo(bos, objList, schema, buffer);
            protoStuff = bos.toByteArray();
        } catch (Exception e) {
            log.error("Failed to serializer, obj list:{}", objList, e);
            throw new RuntimeException("Failed to serializer");
        } finally {
            buffer.clear();
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return protoStuff;
    }

    /**
     * 反序列化列表
     */
    public static <T> List<T> deserializeList(byte[] paramArrayOfByte, Class<T> targetClass) {
        if (paramArrayOfByte == null || paramArrayOfByte.length == 0) {
            log.error("Failed to deserialize, byte is empty");
            throw new RuntimeException("Failed to deserialize");
        }

        Schema<T> schema = RuntimeSchema.getSchema(targetClass);
        List<T> result;
        try {
            result = ProtostuffIOUtil.parseListFrom(new ByteArrayInputStream(paramArrayOfByte), schema);
        } catch (IOException e) {
            log.error("Failed to deserialize", e);
            throw new RuntimeException("Failed to deserialize");
        }
        return result;
    }
}
