package models;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @ToString @AllArgsConstructor @NoArgsConstructor
public class GetGroupMemberInfoResponse {

	private BigDecimal totalIncome;
}
