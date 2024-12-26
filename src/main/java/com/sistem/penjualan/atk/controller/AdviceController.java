package com.sistem.penjualan.atk.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.sistem.penjualan.atk.util.Constant;

@ControllerAdvice
public class AdviceController {

    @ModelAttribute("title")
    public String title() { return Constant.Text.TITLE; }

    @ModelAttribute("login")
    public String login() { return Constant.Text.LOGIN; }

    @ModelAttribute("hintUsername")
    public String hintUsername() { return Constant.Text.HINT_USERNAME; }

    @ModelAttribute("hintPassword")
    public String hintPassword() { return Constant.Text.HINT_PASSWORD; }

    @ModelAttribute("errorLogin")
    public String errorLogin() { return Constant.Text.ERROR_LOGIN; }
}