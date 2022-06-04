package tech.yeez.investment.model.dto.request;

import lombok.Data;
import tech.yeez.investment.model.enums.SubscriberTypeEnum;

import java.io.Serializable;
import java.util.List;

/**
 * @description: subscribe info
 * @author: xiangbin
 * @create: 2022-04-12 16:45
 **/
@Data
public class SubscribeRequestDto implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     *  Mainnet or Ropsten
     */
    private String network;

    /**
     *
     */
    private String fromBlock;

    /**
     * contract address
     */
    private String address;

    private List<String> topics;

    private Integer intervalPeriod;

    /**
     *
     */
    private String noticeUrl;

    /**
     *  0-transaction 1- vault
     * @see SubscriberTypeEnum
     */
    private Integer noticeType;

    /**
     * appName
     */
    private String appName;

}
