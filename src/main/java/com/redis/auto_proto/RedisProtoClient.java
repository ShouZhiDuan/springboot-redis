package com.redis.auto_proto;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: ShouZhi@Duan
 * @Date: 2022/4/7 13:27
 * @Description:
 */
@Component
public class RedisProtoClient {
    
    @Autowired
    private RedisTemplate<Object, Object> fastRedisTemplate;

    /**
     * get cache
     */
    public <T> T get(final String field, Class<T> targetClass) {
        byte[] result = fastRedisTemplate.execute((RedisCallback<byte[]>) connection -> connection.get(field.getBytes()));
        if (result == null) {
            return null;
        }

        return ProtoStuffUtil.deserialize(result, targetClass);
    }

    /**
     * put cache
     */
    public <T> void set(String field, T obj) {
        final byte[] value = ProtoStuffUtil.serialize(obj);
        fastRedisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.set(field.getBytes(), value);
            return null;
        });
    }

    /**
     * put cache with expire time
     */
    public <T> void setWithExpire(String field, T obj, final long expireTime) {
        final byte[] value = ProtoStuffUtil.serialize(obj);
        fastRedisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.setEx(field.getBytes(), expireTime, value);
            return null;
        });
    }

    /**
     * get list cache
     */
    public <T> List<T> getList(final String field, Class<T> targetClass) {
        byte[] result = fastRedisTemplate.execute((RedisCallback<byte[]>) connection -> connection.get(field.getBytes()));
        if (result == null) {
            return null;
        }

        return ProtoStuffUtil.deserializeList(result, targetClass);
    }

    /**
     * put list cache
     */
    public <T> void setList(String field, List<T> objList) {
        final byte[] value = ProtoStuffUtil.serializeList(objList);
        fastRedisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.set(field.getBytes(), value);
            return null;
        });
    }

    /**
     * put list cache with expire time
     */
    public <T> void setListWithExpire(String field, List<T> objList, final long expireTime) {
        final byte[] value = ProtoStuffUtil.serializeList(objList);
        fastRedisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.setEx(field.getBytes(), expireTime, value);
            return null;
        });
    }

    /**
     * get h cache
     */
    public <T> T hGet(final String key, final String field, Class<T> targetClass) {
        byte[] result = fastRedisTemplate
                .execute((RedisCallback<byte[]>) connection -> connection.hGet(key.getBytes(), field.getBytes()));
        if (result == null) {
            return null;
        }

        return ProtoStuffUtil.deserialize(result, targetClass);
    }

    /**
     * put hash cache
     */
    public <T> boolean hSet(String key, String field, T obj) {
        final byte[] value = ProtoStuffUtil.serialize(obj);
        return fastRedisTemplate.execute(
                (RedisCallback<Boolean>) connection -> connection.hSet(key.getBytes(), field.getBytes(), value));
    }

    /**
     * put hash cache
     */
    public <T> void hSetWithExpire(String key, String field, T obj, long expireTime) {
        final byte[] value = ProtoStuffUtil.serialize(obj);
        fastRedisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.hSet(key.getBytes(), field.getBytes(), value);
            connection.expire(key.getBytes(), expireTime);
            return null;
        });
    }

    /**
     * get list cache
     */
    public <T> List<T> hGetList(final String key, final String field, Class<T> targetClass) {
        byte[] result = fastRedisTemplate
                .execute((RedisCallback<byte[]>) connection -> connection.hGet(key.getBytes(), field.getBytes()));
        if (result == null) {
            return null;
        }

        return ProtoStuffUtil.deserializeList(result, targetClass);
    }

    /**
     * put list cache
     */
    public <T> boolean hSetList(String key, String field, List<T> objList) {
        final byte[] value = ProtoStuffUtil.serializeList(objList);
        return fastRedisTemplate.execute(
                (RedisCallback<Boolean>) connection -> connection.hSet(key.getBytes(), field.getBytes(), value));
    }

    /**
     * get cache by keys
     */
    public <T> Map<String, T> hMGet(String key, Collection<String> fields, Class<T> targetClass) {
        List<byte[]> byteFields = fields.stream().map(String::getBytes).collect(Collectors.toList());
        byte[][] queryFields = new byte[byteFields.size()][];
        byteFields.toArray(queryFields);
        List<byte[]> cache = fastRedisTemplate
                .execute((RedisCallback<List<byte[]>>) connection -> connection.hMGet(key.getBytes(), queryFields));

        Map<String, T> results = new HashMap<>(16);
        Iterator<String> it = fields.iterator();
        int index = 0;
        while (it.hasNext()) {
            String k = it.next();
            if (cache.get(index) == null) {
                index++;
                continue;
            }

            results.put(k, ProtoStuffUtil.deserialize(cache.get(index), targetClass));
            index++;
        }

        return results;
    }

    /**
     * set cache by keys
     */
    public <T> void hMSet(String field, Map<String, T> values) {
        Map<byte[], byte[]> byteValues = new HashMap<>(16);
        for (Map.Entry<String, T> value : values.entrySet()) {
            byteValues.put(value.getKey().getBytes(), ProtoStuffUtil.serialize(value.getValue()));
        }

        fastRedisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.hMSet(field.getBytes(), byteValues);
            return null;
        });
    }

    /**
     * get caches in hash
     */
    public <T> Map<String, T> hGetAll(String key, Class<T> targetClass) {
        Map<byte[], byte[]> records = fastRedisTemplate
                .execute((RedisCallback<Map<byte[], byte[]>>) connection -> connection.hGetAll(key.getBytes()));

        Map<String, T> ret = new HashMap<>(16);
        for (Map.Entry<byte[], byte[]> record : records.entrySet()) {
            T obj = ProtoStuffUtil.deserialize(record.getValue(), targetClass);
            ret.put(new String(record.getKey()), obj);
        }

        return ret;
    }

    /**
     * list index
     */
    public <T> T lIndex(String key, int index, Class<T> targetClass) {
        byte[] value =
                fastRedisTemplate.execute((RedisCallback<byte[]>) connection -> connection.lIndex(key.getBytes(), index));
        return ProtoStuffUtil.deserialize(value, targetClass);
    }

    /**
     * list range
     */
    public <T> List<T> lRange(String key, int start, int end, Class<T> targetClass) {
        List<byte[]> value = fastRedisTemplate
                .execute((RedisCallback<List<byte[]>>) connection -> connection.lRange(key.getBytes(), start, end));
        return value.stream().map(record -> ProtoStuffUtil.deserialize(record, targetClass))
                .collect(Collectors.toList());
    }

    /**
     * list left push
     */
    public <T> void lPush(String key, T obj) {
        final byte[] value = ProtoStuffUtil.serialize(obj);
        fastRedisTemplate.execute((RedisCallback<Long>) connection -> connection.lPush(key.getBytes(), value));
    }

    /**
     * list left push
     */
    public <T> void lPush(String key, List<T> objList) {
        List<byte[]> byteFields = objList.stream().map(ProtoStuffUtil::serialize).collect(Collectors.toList());
        byte[][] values = new byte[byteFields.size()][];

        fastRedisTemplate.execute((RedisCallback<Long>) connection -> connection.lPush(key.getBytes(), values));
    }

    /**
     * 精确删除key
     */
    public void deleteCache(String key) {
        fastRedisTemplate.delete(key);
    }


    /**
     * 排行榜的存入
     */
    public void zAdd(String redisKey, ImmutablePair<String, BigDecimal> immutablePair) {
        final byte[] key = redisKey.getBytes();
        final byte[] value = immutablePair.getLeft().getBytes();
        fastRedisTemplate.execute((RedisCallback<Boolean>) connection -> connection
                .zAdd(key, immutablePair.getRight().doubleValue(), value));

    }

    /**
     * 获取排行榜低->高排序
     */
    public List<ImmutablePair<String, BigDecimal>> zRangeWithScores(String redisKey, int start, int end) {
        Set<RedisZSetCommands.Tuple> items = fastRedisTemplate.execute(
                (RedisCallback<Set<RedisZSetCommands.Tuple>>) connection -> connection
                        .zRangeWithScores(redisKey.getBytes(), start, end));
        return items.stream()
                .map(record -> ImmutablePair.of(new String(record.getValue()), BigDecimal.valueOf(record.getScore())))
                .collect(Collectors.toList());
    }


    /**
     * 获取排行榜高->低排序
     */
    public List<ImmutablePair<String, BigDecimal>> zRevRangeWithScores(String redisKey, int start, int end) {
        Set<RedisZSetCommands.Tuple> items = fastRedisTemplate.execute(
                (RedisCallback<Set<RedisZSetCommands.Tuple>>) connection -> connection
                        .zRevRangeWithScores(redisKey.getBytes(), start, end));
        return items.stream()
                .map(record -> ImmutablePair.of(new String(record.getValue()), BigDecimal.valueOf(record.getScore())))
                .collect(Collectors.toList());
    }

}
