package com.coop.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        //에러 코드 상태 가져오
    	Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    	//에러코드를 정수로 치환 
    	if(status !=null) {
    		int statusCode = Integer.valueOf(status.toString());
    		
    		//에러가 404일 경우 
    		if(statusCode == HttpStatus.NOT_FOUND.value()) {
    			return "/error/404";
    		//에러가 500일 경우 
    		}
    		if(statusCode == HttpStatus.FORBIDDEN.value()) {
    			return "/error/500";
    		}
    	}
    	return "/error/error.html";
    }
}

