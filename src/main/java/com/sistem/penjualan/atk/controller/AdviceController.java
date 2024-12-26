package com.sistem.penjualan.atk.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.sistem.penjualan.atk.util.Constant;

@ControllerAdvice
public class AdviceController {

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return Constant.PAGE_TITLE;
    }
}