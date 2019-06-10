/**
 * @project:      IIMP
 * @title:          MyInterceptor.java
 * @copyright: © 2018 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.framework.spring;

import org.aspectj.lang.annotation.Aspect;

/**
 * @ClassName: MyInterceptor
 * @description: TODO请描述一下这个类
 * @author: Nil
 * @date: 2018年1月31日 下午2:53:19
 * @version 1.0.0
 */
@Aspect
public class MyInterceptor {
//    @Autowired
//    private SysLogService sysLogService;
//
//    /***创建控制器切点*/
//    @Pointcut("execution(* com.newskysoft.iimp.*.controller.*.create*(..))")
//    private void createControllerAspect() {
//    }
//    /***修改控制器切点*/
//    @Pointcut("execution(* com.newskysoft.iimp.*.controller.*.update*(..))")
//    private void updateControllerAspect() {
//    }
//    /***删除控制器切点*/
//    @Pointcut("execution(* com.newskysoft.iimp.*.controller.*.delete*(..))")
//    private void deleteControllerAspect() {
//    }
//
//    @After("createControllerAspect() || updateControllerAspect() || deleteControllerAspect()")
//    public  void doAfter(JoinPoint joinPoint) {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
//        if(user!=null){
//            StringBuilder sb=new StringBuilder();
//            int index=1;
//            for(Class<?> type:((MethodSignature)joinPoint.getSignature()).getMethod().getParameterTypes()){
//                if(index>1){
//                    sb.append(",");
//                }
//                sb.append(type.getSimpleName());
//                sb.append(" ");
//                sb.append("p"+(index++));
//            }
//            String requestParameters=null;
//            if(request.getContentType()!=null&&request.getContentType().startsWith("multipart/form-data")){
//                for(Object arg:joinPoint.getArgs()){
//                    if(arg instanceof MultipartHttpServletRequest){
//                        MultipartHttpServletRequest mrequest=(MultipartHttpServletRequest)arg;
//                        requestParameters=JSON.toJSONString(mrequest.getParameterMap(),SerializerFeature.WriteDateUseDateFormat);
//                        break;
//                    }
//                }
//            }else{
//                requestParameters=JSON.toJSONString(request.getParameterMap(),SerializerFeature.WriteDateUseDateFormat);
//            }
//            sysLogService.log(user.getId(), joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName(), sb.length()==0?null:sb.toString(), requestParameters);
//        }
//    }
}
