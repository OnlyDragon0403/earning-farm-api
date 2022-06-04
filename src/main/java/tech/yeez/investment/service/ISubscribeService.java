package tech.yeez.investment.service;

import tech.yeez.investment.model.entity.Subscribe;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author xiangbin
 * @since 
 */
public interface ISubscribeService extends IService<Subscribe> {


    Subscribe selectByFilterId(String filterId);

    List<Subscribe> selectAll();

    List<Subscribe> selectByStatusStoped();
}
