package com.finch.api.services;

import com.finch.api.data.model.User;
import com.finch.api.security.AccountCredentialsVO;
import com.finch.api.security.AuthenticationVO;

public interface IUserService {
	 public User findByUsernameOrEmail(String username, String email);   
	 //public User save(AccountCredentialsVO vo);
	 //public AuthenticationVO findByUsernameOrEmail(String username, String email);
	 public AccountCredentialsVO save(AccountCredentialsVO vo);
	 public AuthenticationVO auth(AccountCredentialsVO vo);
}
