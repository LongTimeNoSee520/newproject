package com.zjtc.base.util;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.zjtc.model.User;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * JWT 加密
 * @author yuchen
 * @date 2020/05/07
 */
@Component
public class JWTUtil {

  private final Logger logger = LoggerFactory.getLogger(JWTUtil.class);


  @Autowired
  private RedisUtil redisUtil;


  /**
   * 获取共有密匙
   */
  public String getPublicKey() {
    String publicKey = null;
    try {
      publicKey = redisUtil.getString("publicKey");
      if (StringUtils.isBlank(publicKey)) {
//        publicKey = UUID.randomUUID().toString().replace("-", "");
        publicKey = "2bccf9dc91d240b8ad7f303a7e3f3d63";
        redisUtil.setString("publicKey", publicKey, 0);
      }
    } catch (Exception e) {
      logger.error("获取共有密匙异常,errMsg==={}", e.getMessage());
    }
    return publicKey;
  }


  public String creatToken(User user, String publicKey) throws JOSEException {
    if (null == user || StringUtils.isBlank(publicKey)) {
      return null;
    }
    //3.先建立一个头部Header
    /**
     * JWSHeader参数：1.加密算法法则,2.类型，3.。。。。。。。
     * 一般只需要传入加密算法法则就可以。
     * 这里则采用HS256
     *
     * JWSAlgorithm类里面有所有的加密算法法则，直接调用。
     */
    JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

    Map<String, Object> payloadMap = new HashMap<>();
    payloadMap.put("id", user.getId() + TimeUtil.formatDateToTime(new Date()));
    payloadMap.put("username", user.getUsername());
    payloadMap.put("loginId",user.getLoginId());
    //payloadMap.put("password", user.getPassword());
    payloadMap.put("nodeCode",user.getNodeCode());
    //建立一个载荷Payload
    Payload payload = new Payload(new JSONObject(payloadMap));

    //将头部和载荷结合在一起
    JWSObject jwsObject = new JWSObject(jwsHeader, payload);

    //建立一个密匙

    JWSSigner jwsSigner = new MACSigner(publicKey.getBytes());

    //签名
    jwsObject.sign(jwsSigner);

    //生成token
    return jwsObject.serialize();
  }

  /**
   * 根据token解析当前登陆者信息
   */
  public User getUserByToken(String token) {

    if (StringUtils.isBlank(token)) {
      return null;
    }
    User user = null;
    try {
      JWSObject jwsObject = JWSObject.parse(token);
      //获取到载荷
      Payload payload = jwsObject.getPayload();
      JSONObject jsonObject = payload.toJSONObject();
      String result = jsonObject.toString();
      user = com.alibaba.fastjson.JSONObject.parseObject(result, User.class);
      String userId = user.getId();
      user.setId(userId.substring(0, userId.length() - 14));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return user;
  }


  /**
   * 验证token
   */
  public boolean validateToken(String token) {
    boolean success = false;
    if (StringUtils.isNotBlank(token)) {
      try {
        JWSObject jwsObject = JWSObject.parse(token);
        String publicKey = redisUtil.getString("publicKey");
        //建立一个解锁密匙
        JWSVerifier jwsVerifier = new MACVerifier(publicKey.getBytes());
        if (jwsObject.verify(jwsVerifier)) {
          boolean exist = redisUtil.ifExist("token", token);
          if (exist) {
            success = true;
          }
        }
      } catch (ParseException e1) {
        logger.error("token解析失败,errMsg==={}", e1.getMessage());
      } catch (JOSEException e2) {
        logger.error("密匙解析失败,errMsg==={}", e2.getMessage());
      } catch (NullPointerException e3) {
        logger.error("token验证失败,errMsg==={}", e3.getMessage());
      }
    }
    return success;
  }



}
