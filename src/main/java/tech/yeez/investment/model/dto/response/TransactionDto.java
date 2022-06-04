package tech.yeez.investment.model.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * transaction
 * </p>
 *
 * @author xiangbin
 * @create: 2022-04-14 13:41
 */
@Data
public class TransactionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String address;

    private String blockHash;

    private String blockNumber;

    private Integer blockIntNum;

    private String data;

    private String logIndex;

    private String removed;

    private String topics;

    private String transactionHash;

    private String transactionIndex;

    private Integer subId;


}
