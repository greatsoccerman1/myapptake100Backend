package models;

import com.example.demoController.MarkJobCompleteResponse;

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

public class MarkJobCompleteRequest {
    private String jobId;
    private String groupId;
    private int refreshRate;
    private String mongoId;
}
