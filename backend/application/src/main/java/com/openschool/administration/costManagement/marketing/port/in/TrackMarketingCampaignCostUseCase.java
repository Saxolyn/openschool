package com.openschool.administration.costManagement.marketing.port.in;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.cost.Cost;
import java.util.UUID;

public interface TrackMarketingCampaignCostUseCase {
    PageResult<Cost> trackMarketingCampaignCost(UUID campaignId, PageInfo pageInfo);
}

