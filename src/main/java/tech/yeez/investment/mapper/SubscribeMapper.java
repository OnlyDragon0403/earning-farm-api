package tech.yeez.investment.mapper;

import org.apache.ibatis.annotations.Select;
import tech.yeez.investment.model.entity.Subscribe;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * SubscribeMapper interface
 * </p>
 *
 * @author xiangbin
 */
public interface SubscribeMapper extends BaseMapper<Subscribe> {

    @Select("select * from subscribe where filter_id = #{filterId}")
    Subscribe selectByFilterId(String filterId);

    @Select("select * from subscribe where is_del = 0 order by `order_init` asc")
    List<Subscribe> selectAll();

    @Select("SELECT * FROM subscribe where is_del = 0 and status = 0")
    List<Subscribe> selectByStatusStoped();

}
