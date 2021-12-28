package com.board.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component // 직접 정의한 클래스를 Bean으로 등록하기 위한 어노테이션 
@Aspect // advice(부가기능 정의 코드) + pointcut(어디에 적용할지)
public class LoggerAspect {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Around("execution(* com.board..controller.*Controller.*(..)) or execution(* com.board..service.*Impl.*(..)) or execution(* com.board..mapper.*Mapper.*(..))")
	// 대상 객체의 메서드 실행 전후 또는 예외 발생 시점에 실행
	// 가장 광범위하게 사용됨 (@Before 나 @After 대비)
	public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
		
		String type = "";
		String name = joinPoint.getSignature().getDeclaringTypeName();
		// signature 객체에는 파일명을 포함한 전체 패키지 경로가 담겨있음 
		// 이것을 name 변수에 담아서 어떤 클래스, 어떤 메소드를 호출하는지 로그에 출력하는 것 

		if (name.contains("Controller") == true) {
			type = "Controller ===> ";
		} else if (name.contains("Service") == true) {
			type = "ServiceImpl ===> ";
		} else if (name.contains("Mapper") == true) {
			type = "Mapper ===> ";
		}

		logger.debug(type + name + "." + joinPoint.getSignature().getName() + "()");

		return joinPoint.proceed();
	}

}
