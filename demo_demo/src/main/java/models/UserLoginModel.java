package models;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginModel {
	private String userId;
	private String userName;
	// private HashMap<String, String> groups;
	private ArrayList<groupInfo> groupInfo;
}
