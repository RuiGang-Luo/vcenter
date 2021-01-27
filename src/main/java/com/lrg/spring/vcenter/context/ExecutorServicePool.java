package com.lrg.spring.vcenter.context;

import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * @author LRG
 * 并发线程池
 */
public class ExecutorServicePool {
    private static ExecutorService executorService = null;

    private static final void getInstance(){
        if( executorService == null || (executorService.isShutdown() && executorService.isTerminated())){
            int num = Runtime.getRuntime().availableProcessors();
            executorService = Executors.newFixedThreadPool(num>5?5:num);
        }

    }
    public static final void execute(Runnable runnable){
        getInstance();
        executorService.execute(runnable);
    }

    public static final Future submit(Runnable runnable){
        getInstance();
        return executorService.submit(runnable);
    }

    public static void destroy(){
        executorService.shutdown();
    }
}
