package tech.yeez.investment.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum SubscribeStatusEnum {

    CLOSE(0,"close"),
    OPEN(1,"open");

    @Getter
    private Integer type;

    @Getter
    private String desc;

}
