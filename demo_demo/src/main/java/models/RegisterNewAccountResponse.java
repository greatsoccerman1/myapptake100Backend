package models;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @ToString @AllArgsConstructor @NoArgsConstructor
public class RegisterNewAccountResponse {
	private String registerNewAccountStatus;
	private String groupId;
	private String userId;
	private ArrayList<groupInfo> groupInfo;
}
