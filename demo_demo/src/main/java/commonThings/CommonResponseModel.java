package commonThings;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import models.GetGroupMembersResponse;
import models.GroupMember;

@Getter @Setter @Builder @ToString @AllArgsConstructor @NoArgsConstructor

public class CommonResponseModel {
	private String status;
	private String errorMessage;
}
