package com.eastnets.textbreak.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TextBreakAOP {

	private static final Logger LOGGER = Logger.getLogger(TextBreakAOP.class);
	/*
	 * @Around("execution(* com.eastnets.textbreak.writer.DBMessageWriter.*(..)))") public Object writeMontring(ProceedingJoinPoint proceedingJoinPoint) throws Throwable { MethodSignature
	 * methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
	 * 
	 * // Get intercepted method details String className = methodSignature.getDeclaringType().getSimpleName(); String methodName = methodSignature.getName();
	 * 
	 * final StopWatch stopWatch = new StopWatch();
	 * 
	 * // Measure method execution time stopWatch.start(); Object result = proceedingJoinPoint.proceed(); stopWatch.stop();
	 * 
	 * // Log method execution time LOGGER.info("Execution time of " + className + "." + methodName + " " + ":: " + stopWatch.getTotalTimeMillis() + " ms");
	 * 
	 * return result; }
	 * 
	 * @Around("execution(* com.eastnets.textbreak.parser..*(..)))") public Object parserMontring(ProceedingJoinPoint proceedingJoinPoint) throws Throwable { MethodSignature methodSignature =
	 * (MethodSignature) proceedingJoinPoint.getSignature();
	 * 
	 * // Get intercepted method details String className = methodSignature.getDeclaringType().getSimpleName(); String methodName = methodSignature.getName();
	 * 
	 * final StopWatch stopWatch = new StopWatch();
	 * 
	 * // Measure method execution time stopWatch.start(); Object result = proceedingJoinPoint.proceed(); stopWatch.stop();
	 * 
	 * // Log method execution time LOGGER.info("Execution time of " + className + "." + methodName + " " + ":: " + stopWatch.getTotalTimeMillis() + " ms");
	 * 
	 * return result; }
	 * 
	 * @Around("execution(* com.eastnets.textbreak.readers..*(..)))") public Object readerMontring(ProceedingJoinPoint proceedingJoinPoint) throws Throwable { MethodSignature methodSignature =
	 * (MethodSignature) proceedingJoinPoint.getSignature();
	 * 
	 * // Get intercepted method details String className = methodSignature.getDeclaringType().getSimpleName(); String methodName = methodSignature.getName();
	 * 
	 * final StopWatch stopWatch = new StopWatch();
	 * 
	 * // Measure method execution time stopWatch.start(); Object result = proceedingJoinPoint.proceed(); stopWatch.stop();
	 * 
	 * // Log method execution time LOGGER.info("Execution time of " + className + "." + methodName + " " + ":: " + stopWatch.getTotalTimeMillis() + " ms");
	 * 
	 * return result; }
	 */
}
