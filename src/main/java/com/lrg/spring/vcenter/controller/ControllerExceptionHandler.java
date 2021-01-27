package com.lrg.spring.vcenter.controller;

import com.lrg.spring.vcenter.pojo.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * ClassName: BusinessExceptionHandler
 * Description: 全局异常处理类 统一异常输出
 *
 * @author YXH
 * @date 2019/09/4 14:13
 */
@RestControllerAdvice
public class ControllerExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(Throwable.class)
    public Object handleException(Exception e, HttpServletRequest req){
        ResultEntity r = new ResultEntity();

        //业务异常
        r.setCode(500);
        r.setMessage(e.getMessage());
        logger.error(e.getMessage());
        if(e.getMessage() == null){
            e.printStackTrace();
        }
        //使用HttpServletRequest中的header检测请求是否为ajax, 如果是ajax则返回json, 如果为非ajax则返回view(即ModelAndView)
        String contentTypeHeader = req.getHeader("Content-Type");
        String acceptHeader = req.getHeader("Accept");
        String xRequestedWith = req.getHeader("X-Requested-With");
        logger.debug(contentTypeHeader+"------"+acceptHeader+"------"+xRequestedWith);
        if ((contentTypeHeader != null && contentTypeHeader.contains("application/json"))
                || (acceptHeader != null && acceptHeader.contains("application/json"))
                || "XMLHttpRequest".equalsIgnoreCase(xRequestedWith)) {
        logger.debug("-------------------返回RMessage-------------------");
            return r;
        } else {
            // 暂时没有视图 暂时注释
           /* ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("msg", e.getMessage());
            modelAndView.addObject("url", req.getRequestURL());
            modelAndView.addObject("stackTrace", e.getStackTrace());
            modelAndView.setViewName("error");*/
            logger.debug("-------------------返回视图-------------------");
            return r;
        }
    }
}
