package com.board.aop;

import java.util.Collections;
import java.util.List;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Configuration
public class TransactionAspect {

	@Autowired
	private PlatformTransactionManager transactionManager;
	// DBConfiguration 에서 등록했던 객체

	private static final String EXPRESSION = "execution(* com.board..service.*Impl.*(..))";
	// 포인트컷 : 모든 ServiceImpl 메소드를 의미

	@Bean
	public TransactionInterceptor transactionAdvice() {

		List<RollbackRuleAttribute> rollbackRules = Collections
				.singletonList(new RollbackRuleAttribute(Exception.class));
		// 어떤 예외가 발생하더라도 rollback 수행
		
		RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();
		transactionAttribute.setRollbackRules(rollbackRules);
		transactionAttribute.setName("*"); // 트랜잭션의 이름 설정 

		MatchAlwaysTransactionAttributeSource attributeSource = new MatchAlwaysTransactionAttributeSource();
		attributeSource.setTransactionAttribute(transactionAttribute);

//		 return new TransactionInterceptor(transactionManager, attributeSource); 
//		// deprecated

		TransactionInterceptor interceptor = new TransactionInterceptor();
		interceptor.setTransactionAttributeSource(attributeSource);
		interceptor.setTransactionManager(transactionManager);

		return interceptor;
	}

	@Bean
	public Advisor transactionAdvisor() {

		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(EXPRESSION);

		return new DefaultPointcutAdvisor(pointcut, transactionAdvice());

	}

}
