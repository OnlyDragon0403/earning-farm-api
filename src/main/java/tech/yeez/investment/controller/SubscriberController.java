package tech.yeez.investment.controller;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.yeez.investment.model.constant.AssetConstant;
import tech.yeez.investment.model.dto.common.Result;
import tech.yeez.investment.model.dto.common.ResultDesc;
import tech.yeez.investment.model.dto.request.NoticeSubValueDto;
import tech.yeez.investment.model.dto.request.TransactionCallRequestDto;
import tech.yeez.investment.model.dto.response.TransactionDto;
import tech.yeez.investment.model.dto.response.TransactionLogsDto;
import tech.yeez.investment.model.entity.Subscribe;
import tech.yeez.investment.model.entity.Supply;
import tech.yeez.investment.model.entity.TransactionRecord;
import tech.yeez.investment.model.enums.ProfitCalEnum;
import tech.yeez.investment.model.enums.RiskTypeEnum;
import tech.yeez.investment.model.enums.SubscriberStatusEnum;
import tech.yeez.investment.model.enums.TradeTypeEnum;
import tech.yeez.investment.service.ISubscribeService;
import tech.yeez.investment.service.ISupplyService;
import tech.yeez.investment.service.ITransactionRecordService;
import tech.yeez.investment.service.feign.ISubscriptionService;
import tech.yeez.investment.utils.CommonUtil;
import tech.yeez.investment.utils.DateUtil;
import tech.yeez.investment.utils.JacksonUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: subscribe callback interface
 * @author: xiangbin
 * @create: 2022-04-20 18:58
 **/
@Slf4j
@RestController
public class SubscriberController {
	
    @Autowired
    private ISubscribeService subscribeService;

    @Autowired
    private ISupplyService supplyService;

    @Autowired
    private ITransactionRecordService transactionRecordService;

    @Autowired
    private ISubscriptionService subscriptionService;
    
//    @PostMapping(value = "/transaction/call") 
//    public String transactionCall(@RequestBody(required = false) TransactionCallRequestDto transactionCallResponseDto){
//    	log.error("transactionCall");
//        log.info("[transactionCall] transactionCallResponseDto:{}", JacksonUtil.obj2json(transactionCallResponseDto));
//        if(transactionCallResponseDto == null || transactionCallResponseDto.getResult() == null || transactionCallResponseDto.getResult().size() == 0){
//            return SubscriberStatusEnum.SUCCESS.getStatus();
//        }
//        try {
//            List<TransactionRecord> transactionRecordList = new ArrayList<>();
//            TransactionDto transactionDto = transactionCallResponseDto.getResult().get(0);
//            TransactionRecord origin = transactionRecordService.findByTransactionHash(transactionDto.getTransactionHash());
//            if(origin != null){
//                return SubscriberStatusEnum.SUCCESS.getStatus();
//            }
//            Subscribe subscribe = subscribeService.selectByFilterId(transactionDto.getSubId().toString());
//            if(subscribe == null){
//                log.error("[transaction-call] subscribe is null subId:{}",  transactionDto.getSubId());
//                log.info("[transaction-call] subscribe is null transactionCallResponseDto:{}", JacksonUtil.obj2json(transactionCallResponseDto));
//                return SubscriberStatusEnum.FAIL.getStatus();
//            }
//            TradeTypeEnum tradeTypeEnum = TradeTypeEnum.queryByType(subscribe.getTradeType());
//            if(tradeTypeEnum == null){
//                log.error("[transaction-call] tradeTypeEnum is null subId:{}",  transactionDto.getSubId());
//                log.info("[transaction-call] tradeTypeEnum is null transactionCallResponseDto:{}", JacksonUtil.obj2json(transactionCallResponseDto));
//                return SubscriberStatusEnum.SUCCESS.getStatus();
//            }
//            TransactionRecord transactionRecord = new TransactionRecord();
//            transactionRecord.setSubId(subscribe.getId());
//            transactionRecord.setTradeType(tradeTypeEnum.getType());
//            transactionRecord.setTransactionHash(transactionDto.getTransactionHash());
//            transactionRecord.setTradeTime(transactionCallResponseDto.getTimestamp());
//            String data = transactionDto.getData();
//            List<String> stringList = CommonUtil.splitBy32Bytes(data);
//            if(AssetConstant.value_type.equals(RiskTypeEnum.HEIGHT.getType())){
//                if(tradeTypeEnum.equals(TradeTypeEnum.WITH_DRAW)){
//                    transactionRecord.setUserAddress(CommonUtil.formatBytes32Address(stringList.get(0)));
//                    String cffAmount = CommonUtil.hexToTenString(stringList.get(3));
//                    if(StringUtils.isNotBlank(cffAmount)){
//                        transactionRecord.setCffAmount(cffAmount);
//                    }
//                    String virtualPrice = CommonUtil.hexToTenString(stringList.get(5));
//                    if(StringUtils.isNotBlank(virtualPrice)) {
//                        transactionRecord.setVirtualPrice(virtualPrice);
//                    }
//                    String targetFee = CommonUtil.hexToTenString(stringList.get(4));
//                    if(StringUtils.isNotBlank(targetFee)){
//                        transactionRecord.setTargetFee(targetFee);
//                    }
//                    String stableAmount = CommonUtil.hexToTenString(stringList.get(2));
//                    if(StringUtils.isNotBlank(stableAmount)){
//                        transactionRecord.setStableAmount(stableAmount);
//                    }
//                    String targetAmount = CommonUtil.hexToTenString(stringList.get(1));
//                    if(StringUtils.isNotBlank(targetAmount)){
//                        transactionRecord.setTargetAmount(targetAmount);
//                    }
//                    //取款时判断总lp_token是否为零，为零则总收益不展示
//                    transactionRecordList = transactionRecordService.findTranByAddress(CommonUtil.formatBytes32Address(stringList.get(0)));
//                    if (StringUtils.isNotBlank(transactionCallResponseDto.getTimestamp())) {
//                        transactionRecordList = transactionRecordList.stream().filter(v -> ProfitCalEnum.YES.getType().equals(v.getIsProfit())).filter(v -> new BigInteger(transactionCallResponseDto.getTimestamp()).compareTo(new BigInteger(v.getTradeTime())) > 0).collect(Collectors.toList());
//                        if (transactionRecordList.size() > 0) {
//                            BigDecimal withDraw = transactionRecordList.stream().filter(v->TradeTypeEnum.WITH_DRAW.getType().equals(v.getTradeType())).map(TransactionRecord::getCffAmount).map(BigDecimal::new).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
//                            BigDecimal deposit = transactionRecordList.stream().filter(v->TradeTypeEnum.DEPOSIT.getType().equals(v.getTradeType())).map(TransactionRecord::getCffAmount).map(BigDecimal::new).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
//                            BigDecimal lp_token = deposit.subtract(withDraw);
//                            if (StringUtils.isNotBlank(cffAmount)) {
//                                if (new BigDecimal(cffAmount).compareTo(lp_token) >= 0) {
//                                    log.info("[transaction-call] all withdraw address:{} transactionCallResponseDto:{}", transactionRecord.getUserAddress(), JacksonUtil.obj2json(transactionCallResponseDto));
//                                    transactionRecordList.forEach(v -> v.setIsProfit(ProfitCalEnum.NO.getType()));
//                                    transactionRecord.setIsProfit(ProfitCalEnum.NO.getType());
//                                }
//                            }
//                        }
//                    }
//
//                } else if (tradeTypeEnum.equals(TradeTypeEnum.DEPOSIT)){
////                    String firstLog = this.ethGetTransactionFirstLog(AssetConstant.net_work, transactionDto.getTransactionHash());
////                    JSONObject jsonObject = JacksonUtil.json2pojo(firstLog, JSONObject.class);
////                    if(jsonObject == null){
////                        log.error("[transaction-call] ethGetTransactionFirstLog is null TransactionHash:{} firstLog:{}", transactionDto.getTransactionHash(), firstLog);
////                        return SubscriberStatusEnum.FAIL.getStatus();
////                    }
////                    JSONArray jsonArray = jsonObject.getJSONArray("topics");
////                    transactionRecord.setUserAddress(CommonUtil.formatBytes32Address(jsonArray.get(1).toString()));
//                    transactionRecord.setUserAddress(CommonUtil.formatBytes32Address(this.ethGetTransactionForUserAddress(AssetConstant.net_work, transactionDto.getTransactionHash())));
//                    String cffAmount = CommonUtil.hexToTenString(stringList.get(3));
//                    if(StringUtils.isNotBlank(cffAmount)){
//                        transactionRecord.setCffAmount(cffAmount);
//                    }
//                    String virtualPrice = CommonUtil.hexToTenString(stringList.get(4));
//                    if(StringUtils.isNotBlank(virtualPrice)) {
//                        transactionRecord.setVirtualPrice(virtualPrice);
//                    }
//                    String stableAmount = CommonUtil.hexToTenString(stringList.get(2));
//                    if(StringUtils.isNotBlank(stableAmount)){
//                        transactionRecord.setStableAmount(stableAmount);
//                    }
//                    String targetAmount = CommonUtil.hexToTenString(stringList.get(1));
//                    if(StringUtils.isNotBlank(targetAmount)){
//                        transactionRecord.setTargetAmount(targetAmount);
//                    }
//                }
//            } else {
//                if(tradeTypeEnum.equals(TradeTypeEnum.WITH_DRAW)){
//                    int size = stringList.size()-1;
//                    transactionRecord.setUserAddress(CommonUtil.formatBytes32Address(stringList.get(0)));
//                    String cffAmount = CommonUtil.hexToTenString(stringList.get(2));
//                    if(StringUtils.isNotBlank(cffAmount)){
//                        transactionRecord.setCffAmount(cffAmount);
//                    }
//                    String virtualPrice = CommonUtil.hexToTenString(stringList.get(size));
//                    if(StringUtils.isNotBlank(virtualPrice)) {
//                        transactionRecord.setVirtualPrice(virtualPrice);
//                    }
//                    if (size > 3) {
//                        String targetFee = CommonUtil.hexToTenString(stringList.get(3));
//                        if (StringUtils.isNotBlank(targetFee)) {
//                            transactionRecord.setTargetFee(targetFee);
//                        }
//                    }
//                    String targetAmount = CommonUtil.hexToTenString(stringList.get(1));
//                    if(StringUtils.isNotBlank(targetAmount)){
//                        transactionRecord.setTargetAmount(targetAmount);
//                    }
//                    //withdraw all profit to zero
//                    transactionRecordList = transactionRecordService.findTranByAddress(CommonUtil.formatBytes32Address(stringList.get(0)));
//                    if (StringUtils.isNotBlank(transactionCallResponseDto.getTimestamp())) {
//                        transactionRecordList = transactionRecordList.stream().filter(v -> ProfitCalEnum.YES.getType().equals(v.getIsProfit())).filter(v -> new BigInteger(transactionCallResponseDto.getTimestamp()).compareTo(new BigInteger(v.getTradeTime())) > 0).collect(Collectors.toList());
//                        if (transactionRecordList.size() > 0) {
//                            BigDecimal withDraw = transactionRecordList.stream().filter(v->TradeTypeEnum.WITH_DRAW.getType().equals(v.getTradeType())).map(TransactionRecord::getCffAmount).map(BigDecimal::new).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
//                            BigDecimal deposit = transactionRecordList.stream().filter(v->TradeTypeEnum.DEPOSIT.getType().equals(v.getTradeType())).map(TransactionRecord::getCffAmount).map(BigDecimal::new).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
//                            BigDecimal lp_token = deposit.subtract(withDraw);
//                            if (StringUtils.isNotBlank(cffAmount)) {
//                                if (new BigDecimal(cffAmount).compareTo(lp_token) >= 0) {
//                                    log.info("[transaction-call] all withdraw address:{} transactionCallResponseDto:{}", transactionRecord.getUserAddress(), JacksonUtil.obj2json(transactionCallResponseDto));
//                                    transactionRecordList.forEach(v -> v.setIsProfit(ProfitCalEnum.NO.getType()));
//                                    transactionRecord.setIsProfit(ProfitCalEnum.NO.getType());
//                                }
//                            }
//                        }
//                    }
//
//
//
//                } else if (tradeTypeEnum.equals(TradeTypeEnum.DEPOSIT)){
////                    String firstLog = this.ethGetTransactionFirstLog(AssetConstant.net_work, transactionDto.getTransactionHash());
////                    JSONObject jsonObject = JacksonUtil.json2pojo(firstLog, JSONObject.class);
////                    if(jsonObject == null){
////                        log.error("[transaction-call] ethGetTransactionFirstLog is null TransactionHash:{} firstLog:{}", transactionDto.getTransactionHash(), firstLog);
////                        return SubscriberStatusEnum.FAIL.getStatus();
////                    }
////                    JSONArray jsonArray = jsonObject.getJSONArray("topics");
////                    transactionRecord.setUserAddress(CommonUtil.formatBytes32Address(jsonArray.get(1).toString()));
//                    transactionRecord.setUserAddress(CommonUtil.formatBytes32Address(this.ethGetTransactionForUserAddress(AssetConstant.net_work, transactionDto.getTransactionHash())));
//                    String cffAmount = CommonUtil.hexToTenString(stringList.get(2));
//                    if(StringUtils.isNotBlank(cffAmount)){
//                        transactionRecord.setCffAmount(cffAmount);
//                    }
//                    String virtualPrice = CommonUtil.hexToTenString(stringList.get(3));
//                    if(StringUtils.isNotBlank(virtualPrice)) {
//                        transactionRecord.setVirtualPrice(virtualPrice);
//                    }
//                    String targetAmount = CommonUtil.hexToTenString(stringList.get(1));
//                    if(StringUtils.isNotBlank(targetAmount)){
//                        transactionRecord.setTargetAmount(targetAmount);
//                    }
//                }
//            }
//
//
//            if(transactionRecordList != null && transactionRecordList.size() > 0){
//                transactionRecordList = transactionRecordList.stream().filter(v->ProfitCalEnum.NO.getType().equals(v.getIsProfit())).collect(Collectors.toList());
//            }
//            boolean save = transactionRecordService.saveTransactionAndUpdateProfit(transactionRecord, transactionRecordList);
//            if(!save){
//                log.error("[transaction-call] transactionRecord save fail transactionCallResponseDto:{}", JacksonUtil.obj2json(transactionCallResponseDto));
//                return SubscriberStatusEnum.FAIL.getStatus();
//            }
//
//        } catch (Exception e) {
//            log.error("[transaction-call] throw exception transactionCallResponseDto:{}e:", JacksonUtil.obj2json(transactionCallResponseDto), e);
//            return SubscriberStatusEnum.FAIL.getStatus();
//        }
//        return SubscriberStatusEnum.SUCCESS.getStatus();
//    }
//
//
//    @PostMapping(value = "/method/call")
//    public String methodCall(@RequestBody(required = false) NoticeSubValueDto noticeSubValueDto){
//
//        log.info("[method-call] noticeSubValueDto:{}", JacksonUtil.obj2json(noticeSubValueDto));
//        if(noticeSubValueDto == null || StringUtils.isBlank(noticeSubValueDto.getSubId())){
//            log.info("[method-call] noticeSubValueDto or subId is null");
//            return SubscriberStatusEnum.SUCCESS.getStatus();
//        }
//        try {
//
//            Subscribe subscribe = subscribeService.selectByFilterId(noticeSubValueDto.getSubId());
//            if(subscribe == null){
//                log.info("[method-call] subscribe is null noticeSubValueDto:{}", JacksonUtil.obj2json(noticeSubValueDto));
//                return SubscriberStatusEnum.FAIL.getStatus();
//            }
//            //todays supply
//            String timestamp = DateUtil.getThisDayBeginTime(LocalDate.now());
//            String yesterday = DateUtil.getThisDayBeginTime(LocalDate.now().minusDays(1));
//            Supply supply = supplyService.getSupplyByTime(timestamp);
//            Supply yesterdaySupply = supplyService.getSupplyByTime(yesterday);
//            if(yesterdaySupply == null){
//                List<Supply> supplies = supplyService.getCurrentThirdty();
//                if(supplies != null && supplies.size() > 0){
//                    if(supply == null){
//                        yesterdaySupply = supplies.get(0);
//                    } else {
//                        if(supplies.size() > 1){
//                            yesterdaySupply = supplies.get(1);
//                        }
//                    }
//                }
//            }
//            if(supply == null){
//                supply = new Supply();
//                supply.setDateTime(timestamp);
//                supply.setApy("0");
//            }
//
//            TradeTypeEnum.fieldFillValue(supply, yesterdaySupply, subscribe.getTradeType(), noticeSubValueDto.getValue());
//            boolean save = supplyService.saveOrUpdate(supply);
//            if(!save){
//                log.error("[method-call] supply save fail noticeSubValueDto:{}", JacksonUtil.obj2json(noticeSubValueDto));
//                return SubscriberStatusEnum.FAIL.getStatus();
//            }
//        } catch (Exception e) {
//            log.error("[method-call] throw exception noticeSubValueDto:{}e:", JacksonUtil.obj2json(noticeSubValueDto), e);
//            return SubscriberStatusEnum.FAIL.getStatus();
//        }
//        return SubscriberStatusEnum.SUCCESS.getStatus();
//    }
//
//
//    /**
//     *
//     * find transactionHash logs for first log as user_address
//     * @param netWork net
//     * @param transactionHash hash
//     * @return string
//     */
//    private String ethGetTransactionFirstLog(String netWork, String transactionHash){
//        Result<String> result = subscriptionService.ethGetTransactionFirstLog(netWork, CommonUtil.addHexPrefixIfNotExist(transactionHash));
//        log.info("[ethGetTransactionFirstLog] result:{}", result.getData());
//        if(result.getResultCode() != ResultDesc.SUCCESS.getResultCode()){
//            log.error("[ethGetTransactionFirstLog] error result:{}", result.getResultDesc());
//            return null;
//        }
//        return result.getData();
//    }
//
//    /**
//     * find transactionHash last transfer to for user_address
//     * @param netWork net
//     * @param transactionHash hash
//     * @return string
//     */
//    private String ethGetTransactionForUserAddress(String netWork, String transactionHash){
//        try {
//            Result<String> result = subscriptionService.ethGetTransactionReceipt(netWork, CommonUtil.addHexPrefixIfNotExist(transactionHash));
//            log.info("[ethGetTransactionLastTransferLog] result:{}", result.getData());
//            if (result.getResultCode() != ResultDesc.SUCCESS.getResultCode()) {
//                log.error("[ethGetTransactionLastTransferLog] error result:{}", result.getResultDesc());
//                return null;
//            }
//            Map<String, Object> objectMap = JacksonUtil.json2map(result.getData());
//            if (objectMap == null) {
//                log.error("[ethGetTransactionLastTransferLog] objectMap is null result:{}", result.getData());
//                return null;
//            }
//            List<TransactionLogsDto> transactionLogsDtos = JacksonUtil.json2list(JacksonUtil.obj2json(objectMap.get("logs")), TransactionLogsDto.class);
//            if(transactionLogsDtos.size() > 0){
//                TransactionLogsDto transactionLogsDto = transactionLogsDtos.get(0);
//                String transfer = transactionLogsDto.getTopics().get(0);
//                transactionLogsDtos = transactionLogsDtos.stream().filter(v->v.getTopics().get(0).equals(transfer)).collect(Collectors.toList());
//            }
//            if(transactionLogsDtos.size() > 0){
//                TransactionLogsDto transactionLogsDto = transactionLogsDtos.get(transactionLogsDtos.size() - 1);
//                return transactionLogsDto.getTopics().get(transactionLogsDto.getTopics().size()-1);
//            }
//            log.error("[ethGetTransactionLastTransferLog] no result:{}", result.getData());
//            return null;
//        } catch (Exception e){
//            log.error("[ethGetTransactionLastTransferLog] exception e:", e);
//            return null;
//        }
//    }

}
