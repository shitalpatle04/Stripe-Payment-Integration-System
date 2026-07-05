package com.hulkhiretech.payments.security;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hulkhiretech.payments.constant.Constant;
import com.hulkhiretech.payments.service.HmacSha256Service;
import com.hulkhiretech.payments.util.JsonUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class HmacSha256Filter extends OncePerRequestFilter {

	private final HmacSha256Service hmacSha256Service;
	private final JsonUtil jsonUtil;


	@Override
	protected void doFilterInternal(HttpServletRequest request, 
			HttpServletResponse response, FilterChain filterChain)
					throws ServletException, IOException {
		log.info("HmacSha256Filter: Processing request for URI: {} ", 
				request.getRequestURI()); 
		

		//TODO: include HmacSha256Service call here.
		//read Hmac-Signature header from request
		String hmacSignature = request.getHeader("Hmac-Signature");


		//give json

		WrappedRequest wrappedRequest = new WrappedRequest(request);

		String jsonBody = wrappedRequest.getBody();

		log.info("Read from wrappedRequest jsonBody: {} ", jsonBody);


		String formattedJson = null;
		try {

			formattedJson =  jsonUtil.prepareFormattedJsonString(jsonBody);

		} catch (Exception e) {
			log.error("Error while formatting JSON body: ", e);
			throw new AccessDeniedException("Access denied: Invalid JSON body");
		}


		hmacSha256Service.isHmacSignatureValid(formattedJson, hmacSignature);



		log.info("HmacSha256Filter: HMAC signature is valid for URI: {} ", request.getRequestURI());

		//below runs only for success case. 
		//If HMAC validation fails, exception is thrown and request is not processed further
		
		//here we know request is authenticated
		//we need to inform spring security that request is authenticated

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication authentication =
				new HmacAuthenticationToken(
						Constant.MERCHANT_ID, 
						hmacSignature, 
						Constant.ROLE_MERCHANT);

		context.setAuthentication(authentication);

		SecurityContextHolder.setContext(context);

		filterChain.doFilter(wrappedRequest, response);

		log.info("HmacSha256Filter: Completed processing request for URI: {} ", 
				request.getRequestURI());
	}



}
