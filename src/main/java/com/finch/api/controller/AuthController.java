package com.finch.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finch.api.security.AccountCredentialsVO;
import com.finch.api.security.AuthenticationVO;
import com.finch.api.services.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "AuthenticationEndpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    IUserService service;

    @ApiOperation(value = "Authenticates a user and returns a token")
    @PostMapping(value = "/signin", produces = { "application/json", "application/xml", "application/x-yaml" },
    	consumes = { "application/json", "application/xml", "application/x-yaml" })
    public AuthenticationVO signin(@RequestBody AccountCredentialsVO data) {
    	AuthenticationVO auth = service.auth(data);
    	return auth;
    }
    
    @Transactional
    @ApiOperation(value = "Create a user and returns a token")
    @PostMapping(value = "/signup", produces = { "application/json", "application/xml", "application/x-yaml" },
            consumes = { "application/json", "application/xml", "application/x-yaml" })
    public AccountCredentialsVO signup(@RequestBody AccountCredentialsVO user) {
    	AccountCredentialsVO auth = service.save(user);
        return auth;
    }
}