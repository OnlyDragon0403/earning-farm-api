package tech.yeez.investment.service;

import tech.yeez.investment.model.entity.Supply;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *
 * </p>
 *
 * @author xiangbin
 * @since 
 */
public interface ISupplyService extends IService<Supply> {

    Supply getSupplyByTime(String dateTime);


    List<Supply> getSupplyBetweenTime(String startTime, String endTime);

    Double selectAvgApy(String startTime, String endTime);

    List<Supply> getCurrentThirdty();
    
    List<Supply> getCurrentThirdtyByCode(String code);

    /**
     *
     * @return
     */
    Supply getLastSupplly();

    List<Supply> getSupplyLastCount(String startTime, Integer size);

}
