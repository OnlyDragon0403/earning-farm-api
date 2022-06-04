package tech.yeez.investment.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 */
@NoArgsConstructor
@AllArgsConstructor
public enum SubscriberTypeEnum {
    EVENT(0,"event"),
    VALUE(1,"value");

    @Getter
    private Integer type;

    @Getter
    private String desc;
}
