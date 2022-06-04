package tech.yeez.investment.model.vo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import tech.yeez.investment.model.constant.AssetConstant;
import tech.yeez.investment.model.entity.TransactionRecord;
import tech.yeez.investment.model.enums.RiskTypeEnum;

import java.math.BigDecimal;

/**
 * @description: transaction
 * @author: xiangbin
 * @create: 2022-04-21 16:29
 **/
@Data
public class TransactionVo {

    /**
     *
     */
    private String date;

    /**
     *
     */
    private String txh;
    /**
     *
     */
    private String amount;
    /**
     *  Withdraw / Deposit
     */
    private String operation;
    /**
     *
     */
    private String fee;
    /**
     *  success or fail
     */
    private String flag;



    public static TransactionVo transfer(TransactionRecord record){
        TransactionVo transactionVo = new TransactionVo();
        transactionVo.setAmount(new BigDecimal(record.getTargetAmount()).divide(new BigDecimal(AssetConstant.USDC_RATION)).stripTrailingZeros().toPlainString());
        if(AssetConstant.value_type.equals(RiskTypeEnum.HEIGHT.getType())) {
            transactionVo.setAmount(new BigDecimal(record.getStableAmount()).divide(new BigDecimal(AssetConstant.USDC_RATION)).stripTrailingZeros().toPlainString());
        }
        transactionVo.setDate(record.getTradeTime());
        if(StringUtils.isNotBlank(record.getTargetFee())){
            transactionVo.setFee(new BigDecimal(record.getTargetFee()).divide(new BigDecimal(AssetConstant.USDC_RATION)).stripTrailingZeros().toPlainString());
        }
        transactionVo.setFlag("success");
        transactionVo.setOperation(record.getTradeType());
        transactionVo.setTxh(record.getTransactionHash());
        return transactionVo;
    }
}
