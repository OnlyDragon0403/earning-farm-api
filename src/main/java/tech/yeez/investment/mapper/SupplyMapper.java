package tech.yeez.investment.mapper;

import org.apache.ibatis.annotations.Select;
import tech.yeez.investment.model.entity.Supply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * SupplyMapper interface
 * </p>
 *
 * @author xiangbin
 */

public interface SupplyMapper extends BaseMapper<Supply> {


    @Select("select * from supply where date_time = #{dateTime}")
    Supply getSupplyByTime(String dateTime);

    @Select("select * from supply where date_time between  #{startTime} and #{endTime}")
    List<Supply> getSupplyBetweenTime(String startTime, String endTime);

    @Select("SELECT avg(apy) FROM supply where date_time between  #{startTime} and #{endTime}")
    Double selectAvgApy(String startTime, String endTime);

    @Select("select * from supply order by date_time desc limit 31")
    List<Supply> getCurrentThirdty();

    @Select("select * from supply order by date_time desc limit 1")
    Supply getLastSupplly();

    @Select("select * from supply where date_time <= #{startTime} order by date_time desc limit #{size}")
    List<Supply> getSupplyLastCount(String startTime, Integer size);
}
