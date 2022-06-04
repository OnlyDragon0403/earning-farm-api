package tech.yeez.investment.model.constant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import tech.yeez.investment.model.dto.common.Result;
import tech.yeez.investment.model.dto.common.ResultDesc;
import tech.yeez.investment.model.dto.request.InfuraCallRequestDto;
import tech.yeez.investment.service.feign.ISubscriptionService;

/**
 * @description: constant
 * @author: xiangbin
 * @create: 2022-04-21 13:49
 **/
@Slf4j
@Component
public class AssetConstant {

    //application start init
    public static String FEE;//subscribe withdraw_fee_ratio/ratio_base

    public static String DECIMALS;

    public static String FEERATIO = "0.1";//subscirbe harvest_fee_ratio/ratio_base

    public static String USDC_RATION;



    //from properties
    /**
     * 1-risk height 0-risk low
     */
    public static String risk_type = "1";
    /**
     * is use stableAmount  1-use 0-no use
     */
    public static String value_type = "0";

    public static String cff_name = "USDC";

    public static String net_work = "ropsten";

    public static String ratio_base = "10000";//default 10000


    public static String crv_ration = "100000000";

    @Value("${risk_type}")
    public void setRisk_type(String riskType) {
        log.info("RISK_TYPE:{}", riskType);
        risk_type = riskType;
    }
    @Value("${net_work}")
    public void setNet_work(String netWork) {
        log.info("netWork:{}", netWork);
        net_work = netWork;
    }

    @Value("${cff_name}")
    public void setCff_name(String cffName) {
        log.info("cffName:{}", cffName);
        cff_name = cffName;
    }

    @Value("${ratio_base}")
    public void setRatio_base(String ratioBase) {
        log.info("ratioBase:{}", ratioBase);
        ratio_base = ratioBase;
    }

    @Value("${crv_ration}")
    public void setCrv_ration(String crvRation) {
        log.info("crvRation:{}", crvRation);
        crv_ration = crvRation;
    }
    @Value("${value_type}")
    public void setValue_type(String value_type) {
        log.info("value_type:{}", value_type);
        AssetConstant.value_type = value_type;
    }

    @Value("${origin_ration}")
    public void setUsdcRation(String usdcRation) {
        log.info("usdcRation:{}", usdcRation);
        AssetConstant.USDC_RATION = usdcRation;
    }


    /**
     * get logs`s first log
     * @param netWork netWork
     * @param address address
     * @return String
     */
    public static  String ethCall(ISubscriptionService subscriptionService, String netWork, String address, String method){
        if(subscriptionService == null){
            log.error("[ethCall] subscriptionService is null");
            return null;
        }  
        InfuraCallRequestDto infuraCallRequestDto = new InfuraCallRequestDto();
        infuraCallRequestDto.setNetWork(netWork);
        infuraCallRequestDto.setTo(address);
        infuraCallRequestDto.setData(method);
        Result<String> result = subscriptionService.infuraCall(infuraCallRequestDto);
        if(result.getResultCode() != ResultDesc.SUCCESS.getResultCode()){
            log.error("[ethCall] error result:{}", result.getResultDesc());
            return null;
        }
        return result.getData();
    }
}
