package com.sinosoft.ops.cimp.cache.listener;

/**
 * 存储，缓存大类名称、缓存对象存储关键字、缓存有效期的实体
 */
public class CacheListenerMap {
    /**
     * 缓存大类名称
     */
    private String cacheName;
    /**
     * 缓存对象存储关键字
     */
    private Object key;
    /**
     * 缓存有效期 毫秒(配置文件中 秒 为单位)
     */
    private Long expiryTime;

    /**
     * 重写 key与cacheName的hashcode和equals方法
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((cacheName == null) ? 0 : cacheName.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CacheListenerMap other = (CacheListenerMap) obj;
        if (cacheName == null) {
            if (other.cacheName != null)
                return false;
        } else if (!cacheName.equals(other.cacheName))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "CacheListenerMap{" +
                "cacheName='" + cacheName + '\'' +
                ", key=" + key +
                ", expiryTime=" + expiryTime +
                '}';
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public Long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Long expiryTime) {
        this.expiryTime = expiryTime;
    }

}
