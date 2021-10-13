package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @ToString @AllArgsConstructor @NoArgsConstructor
public class AddTaskReq {
	private String taskName;
	private String taskDescription;
	private String jobId;
	private String taskStatus;
	private String refreshRate;
}
