package tech.yeez.investment.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: dto
 * @author: xiangbin
 * @create: 2022-04-24 13:54
 **/
@Data
public class GasResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * "LastBlock": "14645684",
     *         "SafeGasPrice": "19",
     *         "ProposeGasPrice": "19",
     *         "FastGasPrice": "21",
     *         "suggestBaseFee": "18.473544799",
     *         "gasUsedRatio": "0.166172366666667,0.221014766666667,0.336660733333333,0.998691266666667,0.999367217911709"
     */
    @JsonProperty(value = "LastBlock")
    private String LastBlock;

    @JsonProperty(value = "SafeGasPrice")
    private String SafeGasPrice;

    @JsonProperty(value = "ProposeGasPrice")
    private String ProposeGasPrice;

    @JsonProperty(value = "FastGasPrice")
    private String FastGasPrice;

    private String suggestBaseFee;

    private String gasUsedRatio;
}
