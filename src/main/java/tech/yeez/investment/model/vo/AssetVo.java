package tech.yeez.investment.model.vo;

import lombok.Data;

/**
 * @description: vo
 * @author: xiangbin
 * @create: 2022-04-21 08:53
 **/
@Data
public class AssetVo {

    /**
     * name
     */
    private String code;
    /**
     * 1-height 0-low
     */
    private Integer risk_tpye;
    /**
     *
     */
    private String totalassets;
    /**
     *apy
     */
    private String sevendayProfit = "0";
    /**
     *
     */
    private String user_assets = "0";
    /**
     *
     */
    private String user_assets_origin = "0";
    /**
     *
     */
    private String user_profit = "0";

    /**
     *
     */
    private String lp_token = "0";

    /**
     * feeRatio lp_token
     * const base = await cfc.methods.ratio_base().call()
     * 	const ratio = await cfc.methods.harvest_fee_ratio().call()
     * 	return (ratio / base) * 100 + '%'
     */
    private String feeRatio;

    /**
     * fees vault
     *const base = await cfc.methods.ratio_base().call()
     *      * 	const ratio = await cfc.methods.withdraw_fee_ratio().call()
     *      * 	return (ratio / base) * 100 + '%'
     */
    private String ratio;

}
