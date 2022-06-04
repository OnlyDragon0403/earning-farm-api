package tech.yeez.investment.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: profit
 * @author: xiangbin
 * @create: 2022-04-21 16:10
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfitVo {

    /**
     *  timestamp
     */
    private String date;

    /**
     *
     */
    private String profit;
}
