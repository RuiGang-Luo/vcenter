package com.lrg.spring.vcenter.inter;

/**
 * @author LRG
 * 可初始化的
 */
public interface Initializable {
    /**
     * @return 是否中断，异常退出
     * @throws Throwable
     */
    public boolean init() throws Throwable;
}
