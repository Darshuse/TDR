package com.eastnets.domain.viewer;

public enum LineStatus {
	    receiveLine(),
	    sendWithoutReceiveLine(),
	    defultLine(),
	    OutSideSwiftGPI(),
	    NoLongerTracableIntemidiantAgent(),
	    Arrived(),
	    DashedLine,
	    notArived(), 
        ARRIVED_COVE,
        NOT_ARRIVED_COVE
	    ;   
}
