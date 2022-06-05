package tech.yeez.investment.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.yeez.investment.model.constant.AssetConstant;
import tech.yeez.investment.model.dto.common.Result;
import tech.yeez.investment.model.dto.common.ResultDesc;
import tech.yeez.investment.model.entity.Supply;
import tech.yeez.investment.model.entity.TransactionRecord;
import tech.yeez.investment.model.enums.RiskTypeEnum;
import tech.yeez.investment.model.enums.TradeTypeEnum;
import tech.yeez.investment.model.vo.AssetVo;
import tech.yeez.investment.model.vo.ProfitVo;
import tech.yeez.investment.model.vo.TransactionVo;
import tech.yeez.investment.service.ISupplyService;
import tech.yeez.investment.service.ITransactionRecordService;
import tech.yeez.investment.utils.CommonUtil;
import tech.yeez.investment.utils.DateUtil;
import tech.yeez.investment.utils.JacksonUtil;
import tech.yeez.investment.utils.SpringBeanUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: asset
 * @author: xiangbin
 * @create: 2022-04-21 08:50
 **/
@Slf4j
@RestController
public class AssetController {

    @Autowired
    private ISupplyService supplyService;

    @Autowired
    private ITransactionRecordService transactionRecordService;
    
    @GetMapping(value = "/")
    public Result<String> getDashboard(){
        Result<String> result = new Result<>();
        result.setData("hahahah - Asset Controller Dashboard uri");

        return result;
    }
    
    @PostMapping(path= {"/{code}/getAsset", "/getAsset"})
    public Result<AssetVo> getAsset(@PathVariable(required=false,name="code") String code ,@RequestParam(required = false) String address, @RequestParam(required = false) Boolean firstpage){
        Result<AssetVo> result = new Result<>(); 
        AssetVo assetVo = new AssetVo(); 
        
        if(code.toUpperCase().equals("USDC")) {
	        assetVo.setCode(code);
	        assetVo.setRatio("0.1");
	        assetVo.setLp_token("0");
	        assetVo.setRatio("0.001");
	        assetVo.setRisk_tpye(0);
	        assetVo.setSevendayProfit("0.0654");
	        assetVo.setTotalassets("15595374.064381951250954429334747535591555415");
	        assetVo.setUser_assets("0");
	        assetVo.setUser_assets_origin("0");
	        assetVo.setUser_profit("0");
        }
        
        if(code.toUpperCase().equals("WBTC")) {
	        assetVo.setCode(code);
	        assetVo.setRatio("0.1");
	        assetVo.setLp_token("0");
	        assetVo.setRatio("0.001");
	        assetVo.setRisk_tpye(0);
	        assetVo.setSevendayProfit("0.0071");
	        assetVo.setTotalassets("15.61390013391415938485668058316149142");
	        assetVo.setUser_assets("0");
	        assetVo.setUser_assets_origin("0");
	        assetVo.setUser_profit("0");
        }
        
        if(code.toUpperCase().equals("ETH")) {
	        assetVo.setCode(code);
	        assetVo.setRatio("0.1");
	        assetVo.setLp_token("0");
	        assetVo.setRatio("0.001");
	        assetVo.setRisk_tpye(0);
	        assetVo.setSevendayProfit("0.0497");
	        assetVo.setTotalassets("1999.09204979923497559962274899599794195");
	        assetVo.setUser_assets("0");
	        assetVo.setUser_assets_origin("0");
	        assetVo.setUser_profit("0");
        }
        
        result.setData(assetVo);
        return result;
    }

    @PostMapping(value = "/profit")
    public Result<ProfitVo> getProfit(@RequestParam(required = false) String address){
        Result<ProfitVo> result = new Result<>();

        List<ProfitVo> profitVos;
        String endTime = DateUtil.getThisDayBeginTime(LocalDate.now());
        Supply supply = supplyService.getSupplyByTime(endTime);
        List<Supply> supplies = supplyService.getCurrentThirdty();
        	
        String calc = caculaApy(Instant.ofEpochMilli(Long.parseLong(supplies.get(0).getDateTime())).atZone(ZoneOffset.UTC).toLocalDate(), supply.getAverageDays());
        
        profitVos = supplies.stream().sorted(Comparator.comparing(Supply::getDateTime))
                .map(v-> new ProfitVo(new BigDecimal(v.getDateTime()).divide(new BigDecimal("1000"), 0, RoundingMode.HALF_DOWN).stripTrailingZeros().toPlainString(), caculaApy(Instant.ofEpochMilli(Long.parseLong(v.getDateTime())).atZone(ZoneOffset.UTC).toLocalDate(), supply.getAverageDays()))).collect(Collectors.toList());
        result.setDataList(profitVos);
        return result;
    }


    @PostMapping(value = "/transaction")
    public Result<TransactionVo> transaction(@RequestParam(required = false) String address,@RequestParam(required = false) Long pageNo, @RequestParam(required = false) Long pageSize){			// pageNo, pageSize, orderBy , order
        log.info("[transaction] address:{} pageNo:{} pageSize:{}", address, pageNo, pageSize);
        Result<TransactionVo> result = new Result<>();
        tech.yeez.investment.model.dto.common.Page page = new tech.yeez.investment.model.dto.common.Page();
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 10 : pageSize;
        if(StringUtils.isBlank(address)){
            result.setDataList(new ArrayList<>());
            page.setPageNo(pageNo);
            page.setPageSize(pageSize);
            page.setCount(0);
            result.setPage(page);
            return result;
        }

        Page<TransactionRecord> iPage = new Page<>(pageNo, pageSize);
        Page<TransactionRecord> transactionRecordPage = transactionRecordService.findTranForPage(iPage, CommonUtil.removeHexPrefixIfExists(address));

        List<TransactionVo> transactionVos = transactionRecordPage.getRecords().stream().map(TransactionVo::transfer).collect(Collectors.toList());
        result.setDataList(transactionVos);


        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        page.setCount(transactionRecordPage.getTotal());
        result.setPage(page);

        return result;
    }


    /**
     *
     * cal  days  avarge apy
     * @param startDate startDate
     */
    private static String caculaApy(LocalDate startDate, Integer days){
//        LocalDate endDate = startDate.minusDays(days);
        ISupplyService supplyService = SpringBeanUtil.getBean(ISupplyService.class);
        if(supplyService == null){
            log.error("[caculaApy] supplyService is null!");
            return "0";
        }
//        List<Supply> supplyList = supplyService.getSupplyBetweenTime(DateUtil.getThisDayBeginTime(endDate), DateUtil.getThisDayBeginTime(startDate));
        List<Supply> supplyList = supplyService.getSupplyLastCount(DateUtil.getThisDayBeginTime(startDate), days);
        supplyList = supplyList.stream().filter(v->StringUtils.isNotBlank(v.getApy())).collect(Collectors.toList());
        if(supplyList.size() == 0){
            return "0";
        }
        days = days > supplyList.size() ? supplyList.size() : days;
        BigDecimal totalApy = supplyList.stream().map(Supply::getApy).map(BigDecimal::new).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        return totalApy.divide(new BigDecimal(days), 18, RoundingMode.HALF_DOWN).multiply(new BigDecimal("365")).setScale(4, RoundingMode.HALF_DOWN).stripTrailingZeros().toPlainString();

    }


    public static void main(String[] args) {
//        System.out.println(Double.parseDouble("0.000001192918425032") > 0.0);
//        System.out.println(new BigDecimal("1407468624412685002")
//                .add(new BigDecimal("1099976237260820718"))
//                .add(new BigDecimal("410099028174875250"))
//                .add(new BigDecimal("329926776053083641"))
//                .add(new BigDecimal("336525277918912100"))
//                .add(new BigDecimal("395912048154686442"))
//                .add(new BigDecimal("7258449800486861006"))
//                .add(new BigDecimal("3299264585459345308"))
//                .add(new BigDecimal("4592793118105078"))
//                .add(new BigDecimal("699055267806261"))
//                .add(new BigDecimal("14278319798700472"))
//                .add(new BigDecimal("6990552662551088"))
//                .subtract(new BigDecimal("6598584533808932880"))
//                .subtract(new BigDecimal("3299295532199574416"))
//                .subtract(new BigDecimal("713915990441437"))
//                .subtract(new BigDecimal("14278319808557475"))
//                .subtract(new BigDecimal("999482386619012"))
//                .subtract(new BigDecimal("4650311314574307146"))
//        );
//        System.out.println(new BigDecimal("1.9994688E-11").stripTrailingZeros().toPlainString());

//        BigDecimal apy = new BigDecimal("1089863469252806023517").divide(new BigDecimal("1000000000000000000"))
//                .subtract(new BigDecimal("15154725621551875035").divide(new BigDecimal("1000000000000000000")))
//                .divide(new BigDecimal("15154725621551875035").divide(new BigDecimal("1000000000000000000")), 18, RoundingMode.HALF_UP);
//        System.out.println(apy.stripTrailingZeros().toPlainString());
//
//        System.out.println(apy.compareTo(BigDecimal.ZERO) > 0);

//        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
//
//        System.out.println(localDateTime.atZone(ZoneId.of("Europe/Paris")).toInstant().toEpochMilli());
//        System.out.println(localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli());
////        System.out.println(localDateTime.toInstant(ZoneOffset.of("8")).toEpochMilli());
//        System.out.println(localDateTime.toInstant(ZoneOffset.MIN).toEpochMilli());
//        System.out.println(localDateTime.toInstant(ZoneOffset.MAX).toEpochMilli());
//        System.out.println(Instant.ofEpochMilli(1650470400000L));
//        System.out.println(Instant.ofEpochMilli(1650499200000L));
//        System.out.println(Instant.ofEpochMilli(1650564000000L));
//        System.out.println(Instant.ofEpochMilli(1650434400000L));
//        System.out.println(ZoneId.systemDefault());
//
//        System.out.println(CommonUtil.formatBytes32Address("000000000000000000000000e5afc078684683dc232e053c2c9d86015aa00ec6"));
//        System.out.println(CommonUtil.hexToTenString("0000000000000000000000000000000000000000000000000000006df76d83ae"));
//        System.out.println(CommonUtil.hexToTenString("00000000000000000000000000000000000000000000533e724efbd02c30ec65"));
//        System.out.println(CommonUtil.hexToTenString("00000000000000000000000000000000000000000000000010adcac09f4c20e5"));

        /*
         * from :0xe5afc078684683dc232e053c2c9d86015aa00ec6
         * target_amount :472302584750
         * cff_amount : 393108353008390565325925
         * virtual_price : 1201839604232888549
         */
//        String dataWithDraw = "0x00000000000000000000000056dba60a326c8a1e1ed148486a2695884aa34e3b00000000000000000000000000000000000000000000001a910a311302ba6cc2000000000000000000000000000000000000000000000000000000003b9aca000000000000000000000000000000000000000000000000000018dc0b4ec44cd9000000000000000000000000000000000000000000000ed4aa6059079c27b914";
//        List<String> stringList = CommonUtil.splitBy32Bytes(dataWithDraw);
//        for (String str : stringList) {
//            System.out.println(str);
//        }



        /*
          from : 0x9276f4cbe9ca5973faba62f8b4bc0e21724e2896
          target_amount : 603521574720
          cff_amount : 503188337452125648063340
          target_fee : 604125700
          virtual_price : 1201840216766476611
         */

//        String dataDepoit = "0x0000000000000000000000003bf97e2284d2c1a5297536bca9712af69490e38900000000000000000000000000000000000000000000000000000000009896800000000000000000000000000000000000000000000000008ac7230489e800000000000000000000000000000000000000000000000000000de0b6b3a7640000";
//        List<String> stringListDep = CommonUtil.splitBy32Bytes(dataDepoit);
//        for (String str : stringListDep) {
//            System.out.println(str);
//        }

//        System.out.println(DateUtil.getThisDayBeginTime(LocalDate.now().minusDays(1)));
//        BigDecimal apy = (new BigDecimal("15154725621551875035").divide(new BigDecimal("1000000000000000000"))
//                .subtract(new BigDecimal("15154725621439757545").divide(new BigDecimal("1000000000000000000")))
//                .divide(new BigDecimal("15154725621439757545").divide(new BigDecimal("1000000000000000000")), 18, RoundingMode.HALF_UP));


    }
}
