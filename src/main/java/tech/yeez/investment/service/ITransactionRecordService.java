package tech.yeez.investment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import tech.yeez.investment.model.entity.TransactionRecord;
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
public interface ITransactionRecordService extends IService<TransactionRecord> {


    List<TransactionRecord> findTranByAddress(String address);

    List<TransactionRecord> findTranProfitByAddress(String address);

    Page<TransactionRecord> findTranForPage(IPage<TransactionRecord> page, String address);

    TransactionRecord findByTransactionHash(String transactionHash);

    boolean saveTransactionAndUpdateProfit(TransactionRecord transactionRecord, List<TransactionRecord> transactionRecordList);



}
