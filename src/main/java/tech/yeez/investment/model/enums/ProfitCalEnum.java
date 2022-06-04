package tech.yeez.investment.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum ProfitCalEnum {
    YES(1, "yes"),
    NO(0, "no");

    @Getter
    private Integer type;

    @Getter
    private String desc;
}
