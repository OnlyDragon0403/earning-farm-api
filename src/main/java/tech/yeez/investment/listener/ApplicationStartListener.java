package tech.yeez.investment.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import tech.yeez.investment.interceptor.GlobalExceptionHandler;
import tech.yeez.investment.model.constant.AssetConstant;
import tech.yeez.investment.model.dto.common.Result;
import tech.yeez.investment.model.dto.common.ResultDesc;
import tech.yeez.investment.model.dto.request.SubscribeRequestDto;
import tech.yeez.investment.model.entity.Subscribe;
import tech.yeez.investment.model.entity.Supply;
import tech.yeez.investment.model.enums.SubscribeStatusEnum;
import tech.yeez.investment.model.enums.SubscriberStatusEnum;
import tech.yeez.investment.model.enums.TradeTypeEnum;
import tech.yeez.investment.service.ISubscribeService;
import tech.yeez.investment.service.ISupplyService;
import tech.yeez.investment.service.feign.ISubscriptionService;
import tech.yeez.investment.utils.DateUtil;
import tech.yeez.investment.utils.JacksonUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @description: listener
 * @author: xiangbin
 * @create: 2022-04-22 16:10
 **/
@Slf4j
@Configuration
public class ApplicationStartListener implements CommandLineRunner {

    @Autowired
    private ISubscribeService subscribeService;

    @Autowired
    private ISubscriptionService subscriptionService;

    @Autowired
    private ISupplyService supplyService;

    @Override
    public void run(String... args) throws Exception {
        try {
            List<Subscribe> subscribeList = subscribeService.selectAll();
            for (Subscribe subscribe : subscribeList) {
                TradeTypeEnum tradeTypeEnum = TradeTypeEnum.queryByType(subscribe.getTradeType());
                if (tradeTypeEnum == null) { 
                    log.error("[CommandLineRunner]tradeTypeEnum is null subId:{}", subscribe.getId());
                    continue;
                }
                if (tradeTypeEnum.getLocal()) {
                    //local save value
                    String value = AssetConstant.ethCall(subscriptionService, AssetConstant.net_work, subscribe.getContractAddress(), subscribe.getTopics());
                    log.info("ethcall return subscribe:{}, value:{}", subscribe.getTradeType(), value);
                    if (StringUtils.isBlank(value)) {
                        log.error("[CommandLineRunner]tradeTypeEnum value is null subId:{}", subscribe.getId());
                        continue;
                    }
                    TradeTypeEnum.setDefaultValue(tradeTypeEnum, value);
                } 
                else {
                    String fromBlock = subscribe.getFromBlock();
                    //subscribe value
                    if (StringUtils.isNotBlank(subscribe.getFilterId())) {
                        Result<String> result = subscriptionService.subscripeStatus(subscribe.getFilterId());
                        if (result.getResultCode() != ResultDesc.SUCCESS.getResultCode()) {
                            log.error("[ApplicationStartListener] subscripeStatus error filterId:{} status resultDesc:{}", subscribe.getFilterId(), result.getResultDesc());
                            continue;  
                        }
//                        if (result.getData().equalsIgnoreCase(String.valueOf(SubscribeStatusEnum.OPEN.getType()))) {
//                            log.info("[CommandLineRunner] subId:{} status is ok!", subscribe.getId());
//                            continue;
//                        }
                    }

//                    SubscribeRequestDto subscribeRequestDto = new SubscribeRequestDto();
//                    subscribeRequestDto.setAddress(subscribe.getContractAddress());
//                    subscribeRequestDto.setFromBlock(fromBlock);
//                    subscribeRequestDto.setNetwork(AssetConstant.net_work);
//                    subscribeRequestDto.setTopics(Collections.singletonList(subscribe.getTopics()));
//                    subscribeRequestDto.setNoticeType(tradeTypeEnum.getSubscriberTypeEnum().getType());
//                    subscribeRequestDto.setNoticeUrl(subscribe.getReceiveAddress());
//                    subscribeRequestDto.setAppName(AssetConstant.cff_name + "-" + AssetConstant.risk_type +"-" + AssetConstant.net_work);
//                    Result<String> result = subscriptionService.subscripe(subscribeRequestDto);
//                    if (result.getResultCode() != ResultDesc.SUCCESS.getResultCode()) { 
//                        log.error("[ApplicationStartListener] subscripe retry error filterId:{} status resultDesc:{}", subscribe.getFilterId(), result.getResultDesc());
//                        subscribe.setStatus(SubscribeStatusEnum.CLOSE.getType());
//                    } else {
//                        subscribe.setFilterId(result.getData());
//                    }
//                    subscribeService.updateById(subscribe);

                }
            }
            //init supply
//            String timestamp = DateUtil.getThisDayBeginTime(LocalDate.now());
//            Supply supply = supplyService.getSupplyByTime(timestamp);
//            if(supply == null || StringUtils.isAnyBlank(supply.getVirtualPrice(), supply.getTotalSupply())){
//                String yesterday = DateUtil.getThisDayBeginTime(LocalDate.now().minusDays(1));
//                Supply yesterdaySupply = supplyService.getSupplyByTime(yesterday);
//                if(yesterdaySupply == null){
//                    List<Supply> supplies = supplyService.getCurrentThirdty();
//                    if(supplies != null && supplies.size() > 0){
//                        if(supply == null){
//                            yesterdaySupply = supplies.get(0);
//                        } else {
//                            if(supplies.size() > 1){
//                                yesterdaySupply = supplies.get(1);
//                            }
//                        }
//                    }
//                }
//                if(supply == null ){
//                    supply = new Supply();
//                }
//                if(yesterdaySupply != null){
//                    BeanUtils.copyProperties(yesterdaySupply, supply,"id");
//                }
//                supply.setApy("0");
//                supply.setDateTime(timestamp);
//                supply.setCreateDate(LocalDateTime.now());
//                supplyService.saveOrUpdate(supply);
//            }
//            log.info("[ApplicationStartListener]todaySupply:{}", JacksonUtil.obj2json(supply));
 
        } catch (Exception e) {
            log.error("[ApplicationStartListener] logs only for exception e:", e);
            throw new Exception("start error!");
        }
    }


}
