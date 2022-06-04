package tech.yeez.investment.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * supply
 * </p>
 *
 * @author xiangbin
 * @since 
 */
@Data
@TableName("supply")
public class Supply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    /**
     *
     */
    private String totalSupply;

    /**
     *
     */
    private String virtualPrice;

    /**
     *
     */
    private String crv;

    /**
     *
     */
    private String dateTime;

    /**
     *
     */
    private String apy;
    
    /**
     *
     */
    private String gas;


    /**
     *
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createDate;

    private Integer averageDays;
}
