package com.redis.serializer;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.ObjectUtils;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Auther: ShouZhi@Duan
 * @Date: 2022/4/6 14:49
 * @Description:
 */
public class ProtobufRedisSerializer<T> implements RedisSerializer<T> {

    public static volatile Map<String, Codec> simpleTypeCodeMap = new HashMap<>();
    public static final Charset UTF8 = Charset.forName("UTF-8");
    private Class<T> tClass;

    public ProtobufRedisSerializer(Class<T> tClass) {
        super();
        this.tClass = tClass;
    }

    public ProtobufRedisSerializer(T t) {
        super();
        this.tClass = (Class<T>) t.getClass();
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        Codec<T> codec = getCodec(t.getClass());
        try {
            return codec.encode(t);
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (ObjectUtils.isEmpty(bytes) || bytes.length == 0){
            return null;
        }
        try {
            Codec<T> codec = getCodec(tClass);
            return codec.decode(bytes);
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }

    private Codec<T> getCodec(Class clazz){
        Codec codec = simpleTypeCodeMap.get(clazz.getTypeName());
        if (ObjectUtils.isEmpty(codec)){
            synchronized (ProtobufRedisSerializer.class) {
                codec = Optional.ofNullable(codec).orElseGet(() -> ProtobufProxy.create(clazz));
                simpleTypeCodeMap.put(tClass.getTypeName(),codec);
            }
        }
        return codec;
    }
}
