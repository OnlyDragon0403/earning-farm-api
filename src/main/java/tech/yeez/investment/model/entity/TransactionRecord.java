package tech.yeez.investment.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import tech.yeez.investment.model.enums.ProfitCalEnum;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author xiangbin
 * @since 
 */
@Data
@TableName("transaction_record")
public class TransactionRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     *
     */
    private String userAddress;

    /**
     *
     */
    private String transactionHash;

    /**
     *
     */
    private String cffAmount;

    /**
     *
     */
    private String virtualPrice;

    /**
     *
     */
    private String targetFee;

    /**
     * withdraw or deposit
     */
    private String tradeType;

    /**
     *
     */
    private String tradeTime;

    /**
     *
     */
    private Integer subId;

    /**
     *
     */
    private String stableAmount;
    /**
     *target_amount
     */
    private String targetAmount;

    /**
     *
     * @see ProfitCalEnum 1-yes 0-no
     */
    private Integer isProfit;

}
