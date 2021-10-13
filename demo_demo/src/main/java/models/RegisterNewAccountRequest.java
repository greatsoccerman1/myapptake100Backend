package models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @ToString @AllArgsConstructor @NoArgsConstructor
public class RegisterNewAccountRequest {
	private String groupName;
	private String firstName;
	private String lastName;
	private String password;
	private String userName;
	
	//response
	private String reqStatus;
}
