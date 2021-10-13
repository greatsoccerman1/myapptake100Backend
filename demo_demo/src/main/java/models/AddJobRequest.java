package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @ToString @AllArgsConstructor @NoArgsConstructor
public class AddJobRequest {
	private String jobName;
	private String groupId;
	private String address;
	private int jobCost;
	private int refreshRate;
	private String jobStatus;
}
