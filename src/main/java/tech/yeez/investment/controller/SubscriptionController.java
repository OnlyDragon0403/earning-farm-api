package tech.yeez.investment.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.yeez.investment.model.constant.AssetConstant;
import tech.yeez.investment.model.dto.common.Result;
import tech.yeez.investment.model.dto.common.ResultDesc;
import tech.yeez.investment.model.dto.request.InfuraCallRequestDto;
import tech.yeez.investment.model.dto.request.NoticeSubValueDto;
import tech.yeez.investment.model.dto.request.SubscribeRequestDto;
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
public class SubscriptionController {

//    @PostMapping("/event/query/transaction/firstlog")
//    Result<String> ethGetTransactionFirstLog(@RequestParam(required = false) String netWork, @RequestParam(required = false) String transactionHash) {
//		return null;
//	}
//
//    @PostMapping("/event/query/transaction")
//    Result<String> ethGetTransactionReceipt(@RequestParam(required = false) String netWork, @RequestParam(required = false) String transactionHash) {
//		return null;
//	}
//
//    @PostMapping(value = "/event/subscribe")
//    Result<String> subscripe(@RequestBody(required = false) SubscribeRequestDto subscribe) {		// get filter id of subscribe
//    	Result result = new Result();
//    	result.setData(1);
//		return result;
//	}
//
//    @PostMapping(value = "/event/subscribe/status")
//    Result<String> subscripeStatus(@RequestParam(required = false) String subId) {  // get status by filter id of subscribe
//    	Result result = new Result();
//    	result.setData(-1);
//		return result;
//	}
//
//    @PostMapping(value = "/event/call")
//    Result<String> infuraCall(@RequestBody(required = false) InfuraCallRequestDto infuraCallRequestDto) {
//		return null;
//	}
//
//    @PostMapping(value = "/event/subscribe/heigh")
//    Result<String> subscripeHeight(@RequestParam(required = false) String subId) {
//		return null;
//	}

   }
