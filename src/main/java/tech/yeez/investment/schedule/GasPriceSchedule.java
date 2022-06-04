package tech.yeez.investment.schedule;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import tech.yeez.investment.listener.ApplicationStartListener;
import tech.yeez.investment.model.constant.AssetConstant;
import tech.yeez.investment.model.dto.common.Result;
import tech.yeez.investment.model.dto.common.ResultDesc;
import tech.yeez.investment.model.dto.request.SubscribeRequestDto;
import tech.yeez.investment.model.dto.response.GasResponseDto;
import tech.yeez.investment.model.entity.Subscribe;
import tech.yeez.investment.model.entity.Supply;
import tech.yeez.investment.model.enums.SubscribeStatusEnum;
import tech.yeez.investment.model.enums.SubscriberTypeEnum;
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
 * @description: gas
 * @author: xiangbin
 * @create: 2022-04-24 13:48
 **/
@Slf4j
@Configuration
@EnableScheduling
@EnableAsync
public class GasPriceSchedule {

    @Autowired
    private ISupplyService supplyService;

    @Autowired
    private ISubscribeService subscribeService;

    @Autowired
    private ISubscriptionService subscriptionService;

//    @Async
//    @Scheduled(cron = "0/5 * * * * ? ")
    public void gasPrice(){
        RestTemplate restTemplate = new RestTemplate();

        try { 
            String gasUrl = "https://api.etherscan.io/api?module=gastracker&action=gasoracle&apikey=WSZUSR11KDK1H4SZGC62EVXGIWHWVR9HFI";
            String object = restTemplate.postForObject(gasUrl, null, String.class);
//            log.info("[gasPrice] object:{}", object);
            JSONObject jsonObject = JacksonUtil.json2pojo(object, JSONObject.class);
            if (jsonObject == null) {
                log.error("[gasPrice] jsonObject is null, url:{}", gasUrl);
                return;
            }
            if (jsonObject.getString("status").equals("0")) {
                log.error("[gasPrice] request error massage:{}", jsonObject.getString("message"));
                return;
            }
            GasResponseDto gasResponseDto = JacksonUtil.json2pojo(JacksonUtil.obj2json(jsonObject.get("result")), GasResponseDto.class);
            if (gasResponseDto == null || StringUtils.isBlank(gasResponseDto.getProposeGasPrice())) {
                log.error("[gasPrice] gasResponseDto is null gasUrl:{}", gasUrl);
                return;
            }
            String timestamp = DateUtil.getThisDayBeginTime(LocalDate.now());
            Supply supply = supplyService.getSupplyByTime(timestamp);
            if (supply == null) {
                supply = new Supply();
                String lastDay = DateUtil.getThisDayBeginTime(LocalDate.now().minusDays(1));
                Supply lastDaySupply = supplyService.getSupplyByTime(lastDay);
                if (lastDaySupply != null) {
                    BeanUtils.copyProperties(lastDaySupply, supply);
                }
            }
            supply.setDateTime(timestamp);
            supply.setCreateDate(LocalDateTime.now());
            supply.setGas(gasResponseDto.getProposeGasPrice());
            boolean save = supplyService.saveOrUpdate(supply);
            if(!save){
                log.error("[gasPrice] save status error");
            }
        } catch (Exception e){
            log.error("[gasPrice] exception e:", e);
        }
    }

    @Async
    @Scheduled(cron = "1 0 0 * * ? ")
    public void supplyCopy() {//every day 00:00:01 call

        try {
            log.info("[supplyCopy] running...");
            Supply supply = supplyService.getLastSupplly();
            Supply todaySupply = new Supply();
            if (supply == null) {
                log.error("[supplycopy-schedule]no last supply!");
            } else {
                BeanUtils.copyProperties(supply, todaySupply, "id");
            }
            String todayTime = DateUtil.getThisDayBeginTime(LocalDate.now());
            todaySupply.setDateTime(todayTime);
            todaySupply.setCreateDate(LocalDateTime.now());
            todaySupply.setApy("0");
            todaySupply.setId(null);
            supplyService.save(todaySupply);
        } catch (Exception e){
            log.error("[supplyCopy] exception e:", e);
        }
    }


    @Async
    @Scheduled(cron = "0/10 * * * * ? ")
    public void subscribeStatus() {//every 10 seconds call
//        log.info("[subscribeStatus] running...");
        List<Subscribe> subscribeList = subscribeService.selectByStatusStoped();
//        log.info("[subscribeStatus] subscribeList:{}", JacksonUtil.obj2json(subscribeList));
        if (subscribeList == null || subscribeList.size() == 0) {
            return;
        }

        for (Subscribe subscribe : subscribeList) {
            try {
                TradeTypeEnum tradeTypeEnum = TradeTypeEnum.queryByType(subscribe.getTradeType());
                if (tradeTypeEnum == null) {
                    log.error("[subscribeStatus-schedule]tradeTypeEnum is null subId:{}", subscribe.getId());
                    continue;
                }
                SubscriberTypeEnum subscriberTypeEnum = tradeTypeEnum.getSubscriberTypeEnum();
                if (subscriberTypeEnum == null) {
                    log.error("[subscribeStatus-schedule] subscriberTypeEnum is null subId:{}", subscribe.getId());
                    continue;
                }
                //vault
                if(tradeTypeEnum.getLocal() && subscriberTypeEnum.getType().equals(SubscriberTypeEnum.VALUE.getType())){
                    //locl
                    String value = AssetConstant.ethCall(subscriptionService, AssetConstant.net_work, subscribe.getContractAddress(), subscribe.getTopics());
                    log.info("subscribeStatus-schedule return subscribe:{}, value:{}", subscribe.getTradeType(), value);
                    if (StringUtils.isBlank(value)) {
                        log.error("[subscribeStatus-schedule]tradeTypeEnum value is null subId:{}", subscribe.getId());
                        continue;
                    }
                    TradeTypeEnum.setDefaultValue(tradeTypeEnum, value);
                    continue;
                }
//
                String fromBlock = subscribe.getFromBlock();

                if (StringUtils.isNotBlank(subscribe.getFilterId())) {
                    Result<String> result = subscriptionService.subscripeStatus(subscribe.getFilterId());
                    if (result.getResultCode() != ResultDesc.SUCCESS.getResultCode()) {
                        log.error("[subscribeStatus-schedule] subscripeStatus error filterId:{} status resultDesc:{}", subscribe.getFilterId(), result.getResultDesc());
                        continue;
                    }
                    if (result.getData().equalsIgnoreCase("1")) {//0-close 1-open
                        log.info("[subscribeStatus-schedule] subId:{} status is ok!", subscribe.getId());
                        subscribe.setStatus(SubscribeStatusEnum.OPEN.getType());
                        subscribeService.updateById(subscribe);
                        continue;
                    }
                }
                SubscribeRequestDto subscribeRequestDto = new SubscribeRequestDto();
                subscribeRequestDto.setAddress(subscribe.getContractAddress());
                subscribeRequestDto.setFromBlock(fromBlock);
                subscribeRequestDto.setNetwork(AssetConstant.net_work);
                subscribeRequestDto.setTopics(Collections.singletonList(subscribe.getTopics()));
                subscribeRequestDto.setNoticeType(tradeTypeEnum.getSubscriberTypeEnum().getType()); 
                subscribeRequestDto.setNoticeUrl(subscribe.getReceiveAddress());
                subscribeRequestDto.setAppName(AssetConstant.cff_name + "-" + AssetConstant.risk_type +"-" + AssetConstant.net_work);
                Result<String> result = subscriptionService.subscripe(subscribeRequestDto);
                if (result.getResultCode() != ResultDesc.SUCCESS.getResultCode()) {
                    log.error("[subscribeStatus-schedule] subscripe retry error filterId:{} status resultDesc:{}", subscribe.getFilterId(), result.getResultDesc());
                    subscribe.setStatus(SubscribeStatusEnum.CLOSE.getType());
                } else {
                    subscribe.setStatus(SubscribeStatusEnum.OPEN.getType());
                    subscribe.setFilterId(result.getData());
                }
                subscribeService.updateById(subscribe);

            } catch (Exception e) {
                log.error("[subscribeStatus-schedule] subscripe retry error filterId:{} status e:{}", subscribe.getFilterId(), e);
            }
        }

    }


}
