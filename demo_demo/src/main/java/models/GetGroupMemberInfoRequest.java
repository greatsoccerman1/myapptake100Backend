package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @ToString @AllArgsConstructor @NoArgsConstructor
public class GetGroupMemberInfoRequest {
	private String infoForPersonId;
	private String startEarningDate;
	private String endEarningDate;
}
