package tech.yeez.investment.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import tech.yeez.investment.model.enums.SubscribeStatusEnum;
import tech.yeez.investment.model.enums.TradeTypeEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * subscribe record
 * </p>
 *
 * @author xiangbin
 * @since 
 */
@Data
@TableName("subscribe")
public class Subscribe implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     *
     */
    private String contractAddress;

    /**
     *
     */
    private String topics;

    /**
     *
     */
    private String fromBlock;

    /**
     *
     */
    private String receiveAddress;

    /**
     *
     */
    private String filterId;

    /**
     *
     * @see TradeTypeEnum
     */
    private String tradeType;

    /**
     *
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    /**
     * 1-yes 0-no
     */
    private Integer isDel;

    /**
     * 1-open 0-close
     * @see SubscribeStatusEnum
     */
    private Integer status;


    private Integer orderInit;
}
