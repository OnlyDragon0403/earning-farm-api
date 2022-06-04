package tech.yeez.investment.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tech.yeez.investment.model.entity.TransactionRecord;
import tech.yeez.investment.mapper.TransactionRecordMapper;
import tech.yeez.investment.service.ITransactionRecordService;
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
public class TransactionRecordServiceImpl extends ServiceImpl<TransactionRecordMapper, TransactionRecord> implements ITransactionRecordService {

    @Autowired
    private TransactionRecordMapper transactionRecordMapper;

    @Override
    public List<TransactionRecord> findTranByAddress(String address) {
        return transactionRecordMapper.findTranByAddress(address);
    }

    @Override
    public Page<TransactionRecord> findTranForPage(IPage<TransactionRecord> page, String address) {
        return transactionRecordMapper.findTranForPage(page, address);
    }

    @Override
    public TransactionRecord findByTransactionHash(String transactionHash) {
        return transactionRecordMapper.findByTransactionHash(transactionHash);
    }

    @Override
    @Transactional
    public boolean saveTransactionAndUpdateProfit(TransactionRecord transactionRecord, List<TransactionRecord> transactionRecordList) {

        int i = transactionRecordMapper.insert(transactionRecord);
        if (transactionRecordList != null && transactionRecordList.size() > 0) {
            for (TransactionRecord record : transactionRecordList) {
                i += transactionRecordMapper.updateTransactionProfit(record.getId());
            }
            return i == transactionRecordList.size() + 1;
        }
        return i > 0;
    }

    @Override
    public List<TransactionRecord> findTranProfitByAddress(String address) {
        return transactionRecordMapper.findTranProfitByAddress(address);
    }
}
