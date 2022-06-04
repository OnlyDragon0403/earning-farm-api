package tech.yeez.investment.model.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * transaction
 * </p>
 *
 * @author xiangbin
 * @create: 2022-04-14 13:41
 */
@Data
public class TransactionLogsDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String address;

    private String blockHash;

    private String blockNumber;

    private String data;

    private String logIndex;

    private String removed;

    private List<String> topics;

    private String transactionHash;

    private String transactionIndex;

}
