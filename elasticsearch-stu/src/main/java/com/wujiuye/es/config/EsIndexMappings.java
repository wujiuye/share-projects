package com.wujiuye.es.config;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于代码创建索引（仅测试使用）
 *
 * @author wujiuye
 * @date 2020/03/04
 */
public class EsIndexMappings {

    /**
     * dynamic : false
     * properties : {"location_id":{"type":"long"},"flag":{"type":"text","index":true},"local_code":{"type":"text","index":true},"local_name":{"type":"text","index":true,"analyzer":"ik_max_word"},"lv":{"type":"long"},"sup_local_code":{"type":"text","index":true},"url":{"type":"text","index":true}}
     */
    private boolean dynamic = false;
    private Map<String, Map<String, Object>> properties;

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public Map<String, Map<String, Object>> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Map<String, Object>> properties) {
        this.properties = properties;
    }

    /**
     * 生成索引字段映射信息
     *
     * @param dynamic
     * @param type
     * @return
     */
    public static EsIndexMappings byType(boolean dynamic, Class<?> type) {
        EsIndexMappings esIndexMappings = new EsIndexMappings();
        esIndexMappings.setDynamic(dynamic);
        esIndexMappings.setProperties(new HashMap<>());
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            Map<String, Object> value = new HashMap<>();
            EsField esField = field.getAnnotation(EsField.class);
            if (esField == null) {
                value.put("type", "text");
                value.put("index", true);
            } else {
                value.put("type", esField.type());
                value.put("index", esField.index());
            }
            esIndexMappings.getProperties().put(field.getName(), value);
        }
        return esIndexMappings;
    }

}
