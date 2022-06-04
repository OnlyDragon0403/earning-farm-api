package tech.yeez.investment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import tech.yeez.investment.model.entity.Subscribe;
import tech.yeez.investment.mapper.SubscribeMapper;
import tech.yeez.investment.service.ISubscribeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author xiangbin
 * @since 
 */
@Service
public class SubscribeServiceImpl extends ServiceImpl<SubscribeMapper, Subscribe> implements ISubscribeService {

    @Autowired
    private SubscribeMapper subscribeMapper;


    @Override
    public Subscribe selectByFilterId(String filterId) {
        return subscribeMapper.selectByFilterId(filterId);
    } 


    @Override 
    public List<Subscribe> selectAll(){
        return subscribeMapper.selectAll();
    }

    @Override
    public List<Subscribe> selectByStatusStoped() {
        return subscribeMapper.selectByStatusStoped();
    }
}
