package com.zjtc.base.constant;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ResourcesConstants {

  /**
   * 资源类型
   */
  public static final Map<String,Object> RESOURCE_TYPE_MAP ;

  static
  {
    RESOURCE_TYPE_MAP=new HashMap<>();
    RESOURCE_TYPE_MAP.put("1","市级");
    RESOURCE_TYPE_MAP.put("2","区县");
  }

}
