package tech.yeez.investment.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum RiskTypeEnum {
    HEIGHT("1", "height－risk"),
    LOW("0", "low－risk");

    @Getter
    private String type;

    @Getter
    private String desc;
}
