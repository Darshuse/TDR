package com.eastnets.watchdog.criteria.builders;

import java.util.Date;
import java.util.List;

import com.eastnets.entities.Appendix;
import com.eastnets.entities.Mesg;
import com.eastnets.entities.WDMessageSearchRequest;
import com.eastnets.repositories.MesgRepository;

public abstract class AbstractOperator {

	public abstract List<Mesg> findMatchedMessages(MesgRepository mesgRepository, WDMessageSearchRequest searchRequest, Appendix appe, boolean includeText, boolean includeTextField, boolean isPart, Date xCreaDate);

	public abstract boolean isMatchedMessage(Mesg mesg, WDMessageSearchRequest searchRequest, Appendix appe, boolean includeText, boolean includeTextField, boolean isPart, Date xCreaDate);

	public boolean isNotEmpty(String field) {
		return field != null && !field.isEmpty();
	}
}
