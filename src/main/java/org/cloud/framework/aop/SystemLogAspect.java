package org.cloud.framework.aop;

import org.aspectj.lang.annotation.Aspect;
import org.cloud.framework.annotation.SystemControllerLog;
import org.cloud.framework.model.SystemLog;
import org.cloud.framework.model.SystemUser;
import org.cloud.framework.service.ISystemLogService;
import org.cloud.framework.utils.DateUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


/**
 * 系统日志切面类
 */

@Aspect
@Component
public class SystemLogAspect {

    private  static  final Logger logger = LoggerFactory.getLogger(SystemLogAspect. class);

    private static final ThreadLocal<Date> beginTimeThreadLocal = new NamedThreadLocal<Date>("ThreadLocal beginTime");
    private static final ThreadLocal<SystemLog> logThreadLocal = new NamedThreadLocal<SystemLog>("ThreadLocal log");

    private static final ThreadLocal<SystemUser> currentUser=new NamedThreadLocal<>("ThreadLocal user");

    @Autowired(required=false)
    private HttpServletRequest request;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private ISystemLogService systemLogService;

    /**
     * 自定义bean
     * @return
     */
    @Bean
    public ThreadPoolTaskExecutor exeThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor aThreadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        aThreadPoolTaskExecutor.setCorePoolSize(5);
        aThreadPoolTaskExecutor.setMaxPoolSize(10);
        aThreadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        return aThreadPoolTaskExecutor;
    }

    /**
     * Controller层切点 注解拦截
     */
    @Pointcut("@annotation(org.cloud.framework.annotation.SystemControllerLog)")
    public void controllerAspect(){}

    /**
     * 前置通知 用于拦截Controller层记录用户的操作的开始时间
     * @param joinPoint 切点
     * @throws InterruptedException
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) throws InterruptedException{
        Date beginTime=new Date();
        beginTimeThreadLocal.set(beginTime);//线程绑定变量（该数据只有当前请求的线程可见）
        if (logger.isDebugEnabled()){//这里日志级别为debug
            logger.debug("开始计时: {}  URI: {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                    .format(beginTime), request.getRequestURI());
        }

        //读取session中的用户
//        HttpSession session = request.getSession();
        // SystemUser user = (SystemUser) session.getAttribute("user");
        SystemUser user = new SystemUser();
        user.setUsername("anhuifeng");
        currentUser.set(user);
    }

    /**
     * 后置通知 用于拦截Controller层记录用户的操作
     * @param joinPoint 切点
     */
    @After("controllerAspect()")
    public void doAfter(JoinPoint joinPoint) {
        SystemUser user = currentUser.get();
        if(user !=null){
            String title="";
            String type="info";                       //日志类型(info:入库,error:错误)
            String remoteAddr = request.getRemoteAddr();//请求的IP
            String requestUri = request.getRequestURI();//请求的Uri
            String method = request.getMethod();        //请求的方法类型(post/get)
            Map<String,String[]> params = request.getParameterMap(); //请求提交的参数

            try {
                title = getControllerMethodDescription(joinPoint);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 打印JVM信息。
            long beginTime = beginTimeThreadLocal.get().getTime();//得到线程绑定的局部变量（开始时间）
            long endTime = System.currentTimeMillis();  //2、结束时间
            if (logger.isDebugEnabled()){
                logger.debug("计时结束：{}  URI: {}  耗时： {}   最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(endTime),
                        request.getRequestURI(),
                        DateUtils.formatDateTime(endTime - beginTime),
                        Runtime.getRuntime().maxMemory()/1024/1024,
                        Runtime.getRuntime().totalMemory()/1024/1024,
                        Runtime.getRuntime().freeMemory()/1024/1024,
                        (Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()+Runtime.getRuntime().freeMemory())/1024/1024);
            }

            SystemLog systemLog=new SystemLog();
            systemLog.setTitle(title);
            systemLog.setType(type);
            systemLog.setRemoteAddr(remoteAddr);
            systemLog.setRequestUri(requestUri);
            systemLog.setMethod(method);
            systemLog.setMapToParams(params);
            //systemLog.setCreateUser(user.getUsername());
            Date operateDate=beginTimeThreadLocal.get();
            systemLog.setCreateTime(DateUtils.formatDate(operateDate,null));
            systemLog.setTimeout(DateUtils.formatDateTime(endTime - beginTime));

            //1.直接执行保存操作
            //this.logService.createSystemLog(log);

            //2.优化:异步保存日志
            //new SaveLogThread(log, logService).start();

            //3.再优化:通过线程池来执行日志保存
            threadPoolTaskExecutor.execute(new SaveLogThread(systemLog, systemLogService));
            logThreadLocal.set(systemLog);
        }

    }

    /**
     *  异常通知 记录操作报错日志
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "controllerAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        SystemLog systemLog = logThreadLocal.get();
        systemLog.setType("error");
        systemLog.setException(e.toString());
        new UpdateLogThread(systemLog, systemLogService).start();
    }

    /**
     * 获取注解中对方法的描述信息 用于service层注解
     * @param joinPoint 切点
     * @return discription
     */
//    public static String getServiceMethodDescription(JoinPoint joinPoint) {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        SystemServiceLog serviceLog = method.getAnnotation(SystemServiceLog.class);
//        String description = serviceLog.description();
//        return description;
//    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return discription
     */
    public static String getControllerMethodDescription(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SystemControllerLog controllerLog = method.getAnnotation(SystemControllerLog.class);
        String description = controllerLog.description();
        return description;
    }

    /**
     * 保存日志线程
     */
    private static class SaveLogThread implements Runnable {
        private SystemLog systemLog;
        @Autowired
        private ISystemLogService systemLogService;

        public SaveLogThread(SystemLog systemLog, ISystemLogService systemLogService) {
            this.systemLog = systemLog;
            this.systemLogService = systemLogService;
        }

        @Override
        public void run() {
            systemLogService.save(systemLog);
        }
    }

    /**
     * 日志更新线程
     */
    private static class UpdateLogThread extends Thread {
        private SystemLog systemLog;
        @Autowired
        private ISystemLogService systemLogService;

        public UpdateLogThread(SystemLog systemLog, ISystemLogService systemLogService) {
            super(UpdateLogThread.class.getSimpleName());
            this.systemLog = systemLog;
            this.systemLogService = systemLogService;
        }

        @Override
        public void run() {
            this.systemLogService.updateById(systemLog);
        }
    }

}
