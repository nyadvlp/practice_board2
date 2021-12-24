package com.board.util;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.board.controller.Method;

// @Controller
public class UiUtils {

	public String showMessageWithRedirect(
			@RequestParam(value = "message", required = false) String message,
			@RequestParam(value = "redirectUri", required = false) String redirectUri,
			@RequestParam(value = "method", required = false) Method method,
			@RequestParam(value = "params", required = false) Map<String, Object> params, Model model) {
		
		
		System.out.println(":: UIUTILS ::");
		
		model.addAttribute("message", message); // 유저에게 전달될 메시지
		model.addAttribute("redirectUri", redirectUri);
		model.addAttribute("method", method);
		model.addAttribute("params", params); // 화면으로 전달할 파라미터 

		return "utils/message-redirect";

	}

}
