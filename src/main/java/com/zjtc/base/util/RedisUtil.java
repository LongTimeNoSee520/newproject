package com.zjtc.base.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;


/**
 * create by jason on 2019/04/01
 */
@Component
public class RedisUtil {

    private Logger logger = LoggerFactory.getLogger(RedisUtil.class);


    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;

    private JedisPool jedisPool;

    public RedisUtil(JedisPool jedisPool){
        this.jedisPool = jedisPool;
    }

    public void setex(final String key, final int seconds, final String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.setex(key, seconds, value);
        } catch (JedisException e) {
            logger.error("redis 赋初值,设过期出错", e);
            handleJedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    public void incr(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.incr(key);
        } catch (JedisException e) {
            logger.error("redis 赋累加出错", e);
            handleJedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 存入key、JSON化后的字符串
     *
     * @param value
     * @return
     */
    public <T> void set(String key, T value, int seconds) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, JSON.toJSONString(value));
            if (seconds != 0) {
                jedis.expire(key, seconds);
            }
        } catch (JedisException e) {
            logger.error("redis 赋初值出错 key:{},value:{}", key, value , e);
            handleJedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 存入字符串
     * @param key
     * @param value
     * @param seconds
     */
    public void setString(String key, String value, int seconds) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            if (seconds != 0) {
                jedis.expire(key, seconds);
            }
        } catch (JedisException e) {
            logger.error("redis 赋初值出错", e);
            handleJedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 读取字符串
     * @param key
     * @return
     */
    public String getString(String key) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = jedisPool.getResource();
            if (!jedis.exists(key)) {
                logger.info("redis 当前key 不存在");
                return result;
            }
            result = jedis.get(key);
        } catch (JedisException e) {
            logger.error("redis 获取对象出错", e);
            handleJedisException(e);
            return result;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    /**
     * 存入key、JSON化后的字符串
     *
     * @param value
     * @return
     */
    public <T> Long setnx(String key, T value, int seconds) {
        Jedis jedis = null;
        Long setnx = 0L;
        try {
            jedis = jedisPool.getResource();
            setnx = jedis.setnx(key, JSON.toJSONString(value));
            if (seconds != 0) {
                jedis.expire(key, seconds);
            }
            return setnx;
        } catch (JedisException e) {
            logger.error("redis 赋初值出错", e);
            handleJedisException(e);
            return setnx;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /**
     * 释放分布式锁
     *
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public boolean releaseDistributedLock(String lockKey, String requestId) {
        Jedis jedis = null;
        try {
            Object result = null;
            jedis = jedisPool.getResource();
            String currentValue = jedis.get( lockKey );
            if ( StringUtils.trimToEmpty( currentValue ).equals(   StringUtils.trimToEmpty(  requestId  ) )  ) {
                result = jedis.del( lockKey );
            }

//            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
//            Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));

            if (RELEASE_SUCCESS.equals(result)) {
                return true;
            }
            return false;

        } catch (JedisException e) {
            logger.error("redis 分布式解锁异常", e);
            handleJedisException(e);
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /**
     * 获取JSON 对象
     *
     * @param key
     * @return
     */
    public boolean exists(String key) {
        Jedis jedis = null;
        boolean flag = false;
        try {
            jedis = jedisPool.getResource();
            flag = jedis.exists(key);
        } catch (JedisException e) {
            logger.error("redis 获取对象出错", e);
            handleJedisException(e);
            flag = false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return flag;
    }

    /**
     * 获取JSON 对象
     *
     * @param key
     * @return
     */
    public Object get(String key, Class<?> cla) {
        Jedis jedis = null;
        Object data = null;
        try {
            jedis = jedisPool.getResource();
            if (!jedis.exists(key)) {
                logger.info("redis 当前key 不存在");
                return data;
            }
            String value = jedis.get(key);
            data = JSON.parseObject(value, cla);
        } catch (JedisException e) {
            logger.error("redis 获取对象出错", e);
            handleJedisException(e);
            return data;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return data;
    }

    /**
     * 获取JSON 对象数组
     *
     * @param key
     * @return
     */
    public Object getArray(String key, Class<?> cla) {
        Jedis jedis = null;
        Object data = null;
        try {
            jedis = jedisPool.getResource();
            if (!jedis.exists(key)) {
                logger.info("redis 当前key 不存在");
                return data;
            }
            String value = jedis.get(key);
            data = JSON.parseArray(value, cla);
        } catch (JedisException e) {
            logger.error("redis 获取对象数组出错", e);
            handleJedisException(e);
            return data;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return data;
    }

    /**
     * 获取JSON 对象.
     *
     * @param key
     * @return
     */
    public <T> T getObject(String key, Class<T> cla) {
        Jedis jedis = null;
        T data = null;
        try {
            jedis = jedisPool.getResource();
            if (!jedis.exists(key)) {
                logger.info("redis 当前key 不存在");
                return data;
            }
            String value = jedis.get(key);
            data = JSON.parseObject(value, cla);
        } catch (JedisException e) {
            logger.error("redis 获取对象出错", e);
            handleJedisException(e);
            return data;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return data;
    }

    /**
     * 获取JSON 对象.
     *
     * @param key
     * @return
     */
    public <T> T getTypeReferenceObject(String key, TypeReference<T> cla) {
        Jedis jedis = null;
        T data = null;
        try {
            jedis = jedisPool.getResource();
            if (!jedis.exists(key)) {
                logger.info("redis 当前key 不存在");
                return data;
            }
            String value = jedis.get(key);
            data = JSON.parseObject(value, cla);
        } catch (JedisException e) {
            logger.error("redis 获取对象出错", e);
            handleJedisException(e);
            return data;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return data;
    }
    /**
     * 获取JSON 对象 剩余生命时间
     *
     * @param key
     * @return
     */
    public Long ttl(String key) {
        Jedis jedis = null;
        Long value = -1L;
        try {
            jedis = jedisPool.getResource();
            if (!jedis.exists(key)) {
                value = -1L;
            } else {
                value = jedis.ttl(key);
            }
        } catch (JedisException e) {
            logger.error("redis 获取对象出错", e);
            handleJedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return value;
    }

    /**
     * 删除key
     *
     * @param key
     * @return
     */
    public void del(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (!jedis.exists(key)) {
                logger.info("redis 当前key 不存在");
            }
            jedis.del(key);
        } catch (JedisException e) {
            logger.error("令牌删除失败", e);
            handleJedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /**
     * 设置过期
     *
     * @param key
     * @return
     */
    public void expire(String key, int seconds) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (!jedis.exists(key)) {
                logger.info("redis 当前key 不存在");
            }
            jedis.expire(key, seconds);
        } catch (JedisException e) {
            logger.error("redis 设置过期失败", e);
            handleJedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 队列设置
     * 命令用来向列表左边增加元素，返回表示增加元素后列表的长度
     *
     * @param key
     * @return
     */
    public <T> void lpush(String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.lpush(key, JSON.toJSONString(value));
        } catch (JedisException e) {
            logger.error("redis 设置队列失败", e);
            handleJedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 队列设置
     * 命令用来向列表右边增加元素，返回表示增加元素后列表的长度
     *
     * @param key
     * @return
     */
    public <T> void rpush(String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.rpush(key, JSON.toJSONString(value));
        } catch (JedisException e) {
            logger.error("redis 设置队列失败", e);
            handleJedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 队列设置
     * 1：将列表左边的元素从列表中移除，2：返回被移除元素值
     *
     * @param key
     * @return
     */
    public Object lpop(String key, Class<?> cla) {
        Jedis jedis = null;
        Object data = null;
        try {
            jedis = jedisPool.getResource();
            if (!jedis.exists(key)) {
                logger.info("redis 当前key 不存在");
                return data;
            }
            String value = jedis.lpop(key);
            data = JSON.parseObject(value, cla);
        } catch (JedisException e) {
            logger.error("redis lpop 失败");
            handleJedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return data;
    }

    /**
     * 队列设置
     * 1：将列表右边的元素从列表中移除，2：返回被移除元素值
     *
     * @param key
     * @return
     */
    public Object rpop(String key, Class<?> cla) {
        Jedis jedis = null;
        Object data = null;
        try {
            jedis = jedisPool.getResource();
            String value = jedis.rpop(key);
            if (!jedis.exists(key)) {
                logger.info("redis 当前key 不存在");
                return data;
            }
            data = JSON.parseObject(value, cla);
        } catch (JedisException e) {
            logger.error("redis rpop 失败");
            handleJedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return data;
    }

    /**
     * 队列设置
     * 1：将列表右边的元素从列表中移除，2：返回被移除元素值
     *
     * @param key
     * @return
     */
    public <T> List<T> lpops(String key, Class<T> cla) {
        Jedis jedis = null;
        List<T> data = new ArrayList();
        try {
            jedis = jedisPool.getResource();
            while (jedis.exists(key)) {
                String value = jedis.lpop(key);
                T t = JSON.parseObject(value, cla);
                data.add(t);
            }
        } catch (JedisException e) {
            logger.error("redis rpop 失败");
            handleJedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return data;
    }

    /**
     * 队列设置
     * 1：将列表右边的元素从列表中移除，2：返回被移除元素值
     *
     * @param key
     * @return
     */
    public <T> List<T> rpops(String key, Class<T> cla) {
        Jedis jedis = null;
        List<T> data = new ArrayList();
        try {
            jedis = jedisPool.getResource();
            while (jedis.exists(key)) {
                String value = jedis.rpop(key);
                T t = JSON.parseObject(value, cla);
                data.add(t);
            }
        } catch (JedisException e) {
            logger.error("redis rpop 失败");
            handleJedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return data;
    }

    /**
     * 通过key通配符获取所有的key
     *
     * @param pattern
     * @return
     */
    public List<String> getKeys(String pattern) {
        Jedis jedis = null;
        List<String> data = new ArrayList<>();
        try {
            jedis = jedisPool.getResource();
            Set<String> set = jedis.keys(pattern);
            for (String key : set) {
                data.add(key);
            }
        } catch (JedisException e) {
            logger.error("redis rpop 失败 keys:"+pattern,e);
            handleJedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return data;

    }



    /**
     * Handle jedisException, write log and return whether the connection is broken.
     */
    protected boolean handleJedisException(JedisException jedisException) {
        if (jedisException instanceof JedisConnectionException) {
            logger.error("Redis connection lost.", jedisException);
        } else if (jedisException instanceof JedisDataException) {
            if ((jedisException.getMessage() != null) && (jedisException.getMessage().indexOf("READONLY") != -1)) {
                logger.error("Redis connection " + " are read-only slave.", jedisException);
            } else {
                return false;
            }
        } else {
            logger.error("Jedis exception happen.", jedisException);
        }
        return true;
    }

    public void hmset(final String key, final Map<String, String> hash) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hmset(key, hash);
        } catch (JedisException e) {
            handleJedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Map<String, String> hgetAll(final String key) {
        Jedis jedis = null;
        Map<String, String> map = null;

        try {
            jedis = jedisPool.getResource();
            map = jedis.hgetAll(key);
        } catch (JedisException e) {
            handleJedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        return map;
    }

    public void hdel(final String key, final String... fields) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hdel(key, fields);
        } catch (JedisException e) {
            handleJedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 向set中添加字符串
     * @param key
     * @param value
     */
    public void addSetString(String key,String value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.sadd(key,value);
        } catch (JedisException e) {
            handleJedisException(e);
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }


  /**
   * 删除set中的值
   * @param key
   * @param value
   */
    public void delSetString(String key,String value){
      Jedis jedis = null;
      try {
        jedis = jedisPool.getResource();
        jedis.srem(key,value);
      } catch (JedisException e) {
        handleJedisException(e);
      }finally {
        if(jedis != null){
          jedis.close();
        }
      }
    }

  /**
   * 判断set中元素是存在
   * @param key
   * @param value
   * @return
   */
    public boolean ifExist(String key,String value){
      Jedis jedis = null;
      boolean exist = false;
      try {
        jedis = jedisPool.getResource();
        exist = jedis.sismember(key,value);
      } catch (JedisException e) {
        handleJedisException(e);
      }finally {
        if(jedis != null){
          jedis.close();
        }
      }
      return exist;
    }




}
