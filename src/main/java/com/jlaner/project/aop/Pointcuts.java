package com.jlaner.project.aop;

import org.aspectj.lang.annotation.Pointcut;


public class Pointcuts {


    @Pointcut("execution(* com.jlaner.project..*(..))")
    public void allPoint(){} // signature

    @Pointcut("within(com.jlaner.project..*Service*)")
    public void allService(){}

    @Pointcut("within(com.jlaner.project..*Controller*)")
    public void allController(){}

    @Pointcut("within(com.jlaner.project..*Repository*)")
    public void allRepository(){}

    @Pointcut("allPoint() && (allService() || allController() || allRepository())")
    public void allPointAndMvc(){}

    @Pointcut("execution(* *..*Init*.*(..))")
    public void initClass(){}

    @Pointcut("allPoint() ! initClass()")
    public void allPointNotInit(){}
}
