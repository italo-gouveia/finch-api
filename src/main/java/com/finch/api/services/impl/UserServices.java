package com.finch.api.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.finch.api.converter.DozerConverter;
import com.finch.api.data.model.Permission;
import com.finch.api.data.model.User;
import com.finch.api.exception.ResourceNotFoundException;
import com.finch.api.repository.PermissionRepository;
import com.finch.api.repository.UserRepository;
import com.finch.api.security.AccountCredentialsVO;
import com.finch.api.security.AuthenticationVO;
import com.finch.api.security.jwt.JwtTokenProvider;
import com.finch.api.services.IUserService;
import com.finch.api.util.MessagesUtil;

@Service
public class UserServices implements UserDetailsService, IUserService {
	
	private static Long ADMIN_PERMISSION = 1L;
	private static Long MANAGER_PERMISSION = 1L;

    @Autowired
    UserRepository repository;
    
    @Autowired
    PermissionRepository permissionRepository;
    
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    JwtTokenProvider tokenProvider;

    public UserServices(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = repository.findByUsername(username);
        if(user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found");
        }
    }
    
    @Override
    public User findByUsernameOrEmail(String username, String email) {
        return repository.findByUsernameOrEmail(username, email);
    }
    
    @Override
    public AccountCredentialsVO save(AccountCredentialsVO vo) {
        User user = new User();
        user.setEmail(vo.getEmail());
        user.setFullName(vo.getFullname());
        user.setUserName(vo.getUsername());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode(vo.getPassword());
        user.setPassword(encryptedPassword);

        Permission admin = permissionRepository.findById(ADMIN_PERMISSION).orElseThrow(() -> new ResourceNotFoundException(MessagesUtil.NO_RECORDS_FOUND));
        Permission manager = permissionRepository.findById(MANAGER_PERMISSION).orElseThrow(() -> new ResourceNotFoundException(MessagesUtil.NO_RECORDS_FOUND));

        List<Permission> permissions = new ArrayList<>();
        permissions.add(admin);
        permissions.add(manager);

        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setPermissions(permissions);
        
        User entity = DozerConverter.parseObject(user, User.class);
        return DozerConverter.parseObject(repository.save(entity), AccountCredentialsVO.class);
    }
    
    public String getValidateAuthMessage(String username, String email) {
    	String message = "";
    	if(username != null && email != null) {
    		message = "User with username " + username + " and email " + email + " not found";
    	}
    	else if	(username != null) {
    		message = "User with username " + username + " not found";
    	}
    	else if (email != null) {
    		message = "User with email " + email + " not found";
    	}
    	else {
    		message = "Please send a username or email that be registred!";
    	}
    	
    	return message;
    }
    
    @Override
    public AuthenticationVO auth(AccountCredentialsVO data) {
       	var username = data.getUsername();
        var email = data.getEmail();
        var password = data.getPassword();

        var user = findByUsernameOrEmail(username, email);
        if (user == null) {
        	throw new UsernameNotFoundException(getValidateAuthMessage(username, email));
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), password));

        var token = tokenProvider.createToken(user.getUsername(), user.getRoles());
        
        AuthenticationVO auth = new AuthenticationVO();
        auth.setToken(token);
        auth.setUsername(user.getUsername());
        return auth;
    }
    
}
