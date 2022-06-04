package tech.yeez.investment.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
public enum SubscriberStatusEnum {
    SUCCESS("SUCCESS","SUCCESS"),
    FAIL("FAIL","FAIL");
    @Getter
    private String status;

    @Getter
    private String desc;
}
