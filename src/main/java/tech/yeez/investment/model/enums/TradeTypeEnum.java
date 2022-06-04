package tech.yeez.investment.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import tech.yeez.investment.model.constant.AssetConstant;
import tech.yeez.investment.model.entity.Supply;
import tech.yeez.investment.utils.CommonUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

@NoArgsConstructor
@AllArgsConstructor
public enum TradeTypeEnum {
    WITH_DRAW("Withdraw","Withdraw", false, SubscriberTypeEnum.EVENT),//vault contract address
    DEPOSIT("Deposit","Deposit", false ,SubscriberTypeEnum.EVENT),//vault
    TOTAL_SUPPLY("totalSupply","totalSupply", false ,SubscriberTypeEnum.VALUE),//ef-token
    VIRTUAL_PRICE("virtualPrice","virtualPrice", false, SubscriberTypeEnum.VALUE),//vault
    FEE("fee","fee",true,SubscriberTypeEnum.VALUE),//vault
    DECIMALS("decimals","decimals",true,SubscriberTypeEnum.VALUE),//ef-token
    FEE_RATIO("feeRatio","feeRatio",true,SubscriberTypeEnum.VALUE),//vault
    CRV("crv","crv",false,SubscriberTypeEnum.VALUE),//oracle
    USDC_RATION("usdc_ration","usdc_ration",true,SubscriberTypeEnum.VALUE);//usdc


    @Getter
    private String type;

    @Getter
    private String name;

    /**
     * 仅启动时初始化的变量
     */
    @Getter
    private Boolean local;

    /**
     * 事件订阅还是值订阅
     */
    @Getter
    private SubscriberTypeEnum subscriberTypeEnum;


    public static TradeTypeEnum queryByType(String type){
        for (TradeTypeEnum tradeTypeEnum : TradeTypeEnum.values()) {
            if(tradeTypeEnum.getType().equals(type)){
                return tradeTypeEnum;
            }
        }
        return null;
    }

    public static void fieldFillValue(Supply supply, Supply yesterdaySupply, String type, String value){
        if(supply == null || StringUtils.isAnyBlank(type, value)){
            return;
        }
        if(TradeTypeEnum.TOTAL_SUPPLY.getType().equals(type)){
            supply.setTotalSupply(value);
        } else if(TradeTypeEnum.VIRTUAL_PRICE.getType().equals(type)){
            supply.setVirtualPrice(value);
        } else if(TradeTypeEnum.CRV.getType().equals(type)){
            supply.setCrv(value);
        }
        BigDecimal apy = BigDecimal.ZERO;
        if(yesterdaySupply != null && !StringUtils.isAnyBlank(yesterdaySupply.getVirtualPrice(),supply.getVirtualPrice())){
            apy = (new BigDecimal(supply.getVirtualPrice()).divide(new BigDecimal(AssetConstant.DECIMALS))
                    .subtract(new BigDecimal(yesterdaySupply.getVirtualPrice()).divide(new BigDecimal(AssetConstant.DECIMALS)))
                    .divide(new BigDecimal(yesterdaySupply.getVirtualPrice()).divide(new BigDecimal(AssetConstant.DECIMALS)), 18, RoundingMode.HALF_UP));
        }
        if(apy.compareTo(BigDecimal.ZERO) > 0){
            supply.setApy(apy.stripTrailingZeros().toPlainString());
        }
    }


    public static void setDefaultValue(TradeTypeEnum tradeTypeEnum, String value){

        value = CommonUtil.hexToTenString(value);//十六进制转十进制

        if(tradeTypeEnum == null || StringUtils.isBlank(value)){
            return;
        }
        if(TradeTypeEnum.FEE.equals(tradeTypeEnum)){
            if (Integer.valueOf(value) > 0) {
                AssetConstant.FEE = new BigDecimal(value).divide(new BigDecimal(AssetConstant.ratio_base)).toString();
            }
        } else if(TradeTypeEnum.FEE_RATIO.equals(tradeTypeEnum)){
            AssetConstant.FEERATIO = new BigDecimal(value).divide(new BigDecimal(AssetConstant.ratio_base)).toString();
        } else if(TradeTypeEnum.DECIMALS.equals(tradeTypeEnum)){
            AssetConstant.DECIMALS = new BigInteger("10").pow(Integer.parseInt(value)).toString();
        } else if(TradeTypeEnum.USDC_RATION.equals(tradeTypeEnum)){
            AssetConstant.USDC_RATION = new BigInteger("10").pow(Integer.parseInt(value)).toString();
        }
    }

    public static void main(String[] args) {
        Supply supply = new Supply();
        supply.setVirtualPrice("1077316898422556552");
        Supply yesterdaySupply= new Supply();
        yesterdaySupply.setVirtualPrice("1077228989527891586");
        BigDecimal apy = BigDecimal.ZERO;
        if(yesterdaySupply != null && !StringUtils.isAnyBlank(yesterdaySupply.getVirtualPrice(),supply.getVirtualPrice())){
            apy = (new BigDecimal(supply.getVirtualPrice()).divide(new BigDecimal("1000000000000000000"))
                    .subtract(new BigDecimal(yesterdaySupply.getVirtualPrice()).divide(new BigDecimal("1000000000000000000")))
                    .divide(new BigDecimal(yesterdaySupply.getVirtualPrice()).divide(new BigDecimal("1000000000000000000")), 18, RoundingMode.HALF_UP));
        }
        if(apy.compareTo(BigDecimal.ZERO) > 0){
            supply.setApy(apy.stripTrailingZeros().toPlainString());
        }
        System.out.println(supply.getApy());
    }
}
