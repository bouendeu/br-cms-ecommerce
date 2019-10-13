package com.itbcafrica.cmsecommercecar.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * HomeController
 */
@Controller
public class HomeController {

    @GetMapping("/otherpage")
    public String home() {
        return "home";
    }

}