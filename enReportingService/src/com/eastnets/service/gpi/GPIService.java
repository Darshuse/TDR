package com.eastnets.service.gpi;

import com.eastnets.domain.viewer.MessageDetails;
import com.eastnets.service.Service;

public interface GPIService extends Service {

	public GPIServiceBean getGpiLists(String userName, String uetr, String trackerBic, String acountNumber, String msgDirection, boolean showStopRecal, boolean isViewerTraker, String fromDate, String toDate, MessageDetails details,
			boolean showCreditedAmountBeforeConvert, boolean useFormatAmount, boolean convertCharges, boolean calculateChargesWhenZero, boolean checkOnlyonBic6, boolean show71FCharges);
}
