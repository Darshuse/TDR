package com.eastnets.aspect;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.eastnets.entities.AuditLog;
import com.eastnets.entities.LdErrors;
import com.eastnets.models.AuthenticationRequest;
import com.eastnets.models.MessageResponse;
import com.eastnets.services.AuditLogService;
import com.eastnets.services.LdErrorsService;
import com.eastnets.util.WebUtils;

@Configuration
@Aspect
public class ApplicationAspect {

	private static final Logger LOGGER = LogManager.getLogger(ApplicationAspect.class);

	@Autowired
	private AuditLogService auditLogService;

	@Autowired
	private LdErrorsService ldErrorsService;

	@Autowired
	private WebUtils webUtils;

	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void restController() {
	}

	@AfterReturning(value = "restController() && args(.., @RequestBody body)", returning = "result")
	public void afterReturning(JoinPoint joinPoint, Object body, Object result) {

		String programName = "MessageStoreAPI";

		String methodName = joinPoint.getSignature().getName();

		ResponseEntity<Object> myResult = (ResponseEntity<Object>) result;

		int statusCode = myResult.getStatusCodeValue();
		String status = String.valueOf(statusCode).substring(0, 1);

		if (methodName.equals("authenticate")) {
			String loginName = ((AuthenticationRequest) body).getUsername();
			String event = "Authentication";
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
			String clientIp = webUtils.getClientIp(request);
			String action = null;
			LOGGER.debug("Audit user authentication");
			if (status.equals("2")) {
				action = "Authorized User";
			}
			if (status.equals("4")) {
				action = "Unauthorized User";
			}

			AuditLog auditLog = new AuditLog(loginName, programName, event, action, LocalDateTime.now(), clientIp);
			auditLogService.save(auditLog);
		}

		if (methodName.equals("saveMessage")) {

			String errorLevel = null;
			String errExeName = "MessageStoreAPI";

			LOGGER.debug("Trace Message saving");
			MessageResponse messageResponse = (MessageResponse) myResult.getBody();

			if (status.equals("2")) {
				errorLevel = "Success";
			}
			if (status.equals("4")) {
				errorLevel = "Failure";
			}
			LdErrors ldErrors = new LdErrors(errExeName, LocalDateTime.now(), errorLevel, programName, "",
					messageResponse.getStatusMsg());
			ldErrorsService.save(ldErrors);
		}
	}

	@AfterReturning(value = "execution(* com.eastnets.util.*JwtUtil.*(..)))", returning = "result")
	public void token(JoinPoint jp, Object result) {

		String programName = "MessageStoreAPI";

		String errExeName = "MessageStoreAPI";
		String errorLevel = "Failure";
		if (result == null) {
			LdErrors ldErrors = new LdErrors(errExeName, LocalDateTime.now(), errorLevel, programName, "",
					"Access Denied");
			ldErrorsService.save(ldErrors);
		}

	}

}
