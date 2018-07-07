package com.sinnguyen.util;

import com.sinnguyen.entities.Forgot;
import com.sinnguyen.entities.User;
import com.sinnguyen.model.ForgotDTO;
import com.sinnguyen.model.UserDTO;

public class Converter {
	public static UserDTO userToUserDTO(User user) {
		UserDTO u = new UserDTO();
		u.setId(user.getId());
		u.setUsername(user.getUsername());
		u.setPassword(user.getPassword());
		u.setFullname(user.getFullname());
		u.setBirthdate(user.getBirthdate());
		u.setAvatar(user.getAvatar());
		u.setEmail(user.getEmail());
		u.setPhone(user.getPhone());
		u.setCode(user.getCode());
		u.setActivated(user.isActivated());
		u.setNote(user.getNote());
		return u;
	}
	
	public static User userDTOToUser(UserDTO user) {
		User u = new User();
		u.setId(user.getId());
		u.setUsername(user.getUsername());
		u.setPassword(user.getPassword());
		u.setFullname(user.getFullname());
		u.setBirthdate(user.getBirthdate());
		u.setAvatar(user.getAvatar());
		u.setEmail(user.getEmail());
		u.setPhone(user.getPhone());
		u.setCode(user.getCode());
		u.setActivated(user.isActivated());
		u.setNote(user.getNote());
		return u;
	}
	
	public static ForgotDTO forgotToForgotDTO(Forgot forgot) {
		ForgotDTO f = new ForgotDTO();
		f.setId(forgot.getId());
		f.setCode(forgot.getCode());
		f.setTimestamp(forgot.getTimestamp());
		f.setUser(userToUserDTO(forgot.getUser()));
		return f;
	}
	
	public static Forgot forgotDTOToForgot(ForgotDTO forgot) {
		Forgot f = new Forgot();
		f.setId(forgot.getId());
		f.setCode(forgot.getCode());
		f.setTimestamp(forgot.getTimestamp());
		f.setUser(userDTOToUser(forgot.getUser()));
		return f;
	}
}
