package tech.yeez.investment.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tech.yeez.investment.model.entity.TransactionRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * TransactionRecordMapper interface
 * </p>
 *
 * @author xiangbin
 */
public interface TransactionRecordMapper extends BaseMapper<TransactionRecord> {


    @Select("select * from transaction_record where user_address = #{address}")
    List<TransactionRecord> findTranByAddress(String address);

    @Select("select * from transaction_record where user_address = #{address} order by trade_time desc")
    Page<TransactionRecord> findTranForPage(IPage<TransactionRecord> page, String address);

    @Select("select * from transaction_record where transaction_hash = #{transactionHash}")
    TransactionRecord findByTransactionHash(String transactionHash);

    @Update("update transaction_record set is_profit = 0 where id = #{id} and is_profit = 1")
    int updateTransactionProfit(Integer id);

    @Select("select * from transaction_record where user_address = #{address} and is_profit = 1")
    List<TransactionRecord> findTranProfitByAddress(String address);

}
