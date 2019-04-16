package com.sinosoft.ops.cimp.vo.to.user;

import io.swagger.annotations.ApiModelProperty;

public class RelPositionViewModel extends ProjectPositionViewModel {
    /**
     * 职级ID
     */
    @ApiModelProperty(value = "职级ID")
    private String rankId;
    /**
     * 职级名称
     */
    @ApiModelProperty(value = "职级名称")
    private String rankName;

    public String getRankId() {
        return rankId;
    }

    public void setRankId(String rankId) {
        this.rankId = rankId;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }
}
