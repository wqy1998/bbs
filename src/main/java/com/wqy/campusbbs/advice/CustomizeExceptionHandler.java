package com.wqy.campusbbs.advice;

import com.wqy.campusbbs.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e, Model model) {
        if (e instanceof CustomizeException) {
            model.addAttribute("message",e.getMessage());
        } else {
            model.addAttribute("message","服务太热啦，要不稍等下再来试试~");
        }
        return new ModelAndView("error");
    }
}
