package tech.yeez.investment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import tech.yeez.investment.model.entity.Supply;
import tech.yeez.investment.mapper.SupplyMapper;
import tech.yeez.investment.service.ISupplyService;
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
public class SupplyServiceImpl extends ServiceImpl<SupplyMapper, Supply> implements ISupplyService {

    @Autowired
    private SupplyMapper supplyMapper;


    @Override
    public Supply getSupplyByTime(String dateTime) {
        return supplyMapper.getSupplyByTime(dateTime);
    }

    @Override
    public List<Supply> getSupplyBetweenTime(String startTime, String endTime) {
        return supplyMapper.getSupplyBetweenTime(startTime, endTime);
    }

    @Override
    public Double selectAvgApy(String startTime, String endTime) {
        return supplyMapper.selectAvgApy(startTime, endTime);
    }

    @Override 
    public List<Supply> getCurrentThirdty() {
        return supplyMapper.getCurrentThirdty();
    }
    // add Item
    @Override 
    public List<Supply> getCurrentThirdtyByCode(String code) {
    	return supplyMapper.getCurrentThirdtyByCode(code);
    }
    
    @Override
    public Supply getLastSupplly() {
        return supplyMapper.getLastSupplly();
    }

    @Override
    public List<Supply> getSupplyLastCount(String startTime, Integer size) {
        return supplyMapper.getSupplyLastCount(startTime, size);
    }
}
