package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @ToString @AllArgsConstructor @NoArgsConstructor
public class AddGroupMememberRequest {
	/*private String name;
	private String description;
	private String need;
	private String userId;
	private String groupId;*/
	private boolean newGroup;
	private String firstName;
	private String lastName;
	private String groupId;
	private String userName;
	private String role;
	private String groupName;
	
}
