package com.eastnets.service.reporting.scriptlet;

import java.util.HashMap;
import java.util.Map;

public class TransferTypesMap {

	private static Map<String, String> map = new HashMap<String, String>();
	
	static {
		map.put("BNK","Securities Related Item - Bank Fees");
		map.put("BOE","Bill of Exchange");
		map.put("BRF","Brokerage Fee");
		map.put("CAR","Securities Related Item - Corporate Actions Related (should only be used when no specific corporate action event code is available)");
		map.put("CAS","Securities Related Item - Cash in Lieu");
		map.put("CHG","Charges and Other Expenses");
		map.put("CHK","Cheques");
		map.put("CLR","Cash Letters/Cheques Remittance");
		map.put("CMI","Cash Management Item - No Detail");
		map.put("CMN","Cash Management Item - Notional Pooling");
		map.put("CMP","Compensation Claims");
		map.put("CMS","Cash Management Item - Sweeping");
		map.put("CMT","Cash Management Item - Topping");
		map.put("CMZ","Cash Management Item - Zero Balancing");
		map.put("COL","Collections (used when entering a principal amount)");
		map.put("COM","Commission");
		map.put("CPN","Securities Related Item - Coupon Payments");
		map.put("DCR","Documentary Credit (used when entering a principal amount)");
		map.put("DDT","Direct Debit Item");
		map.put("DIS","Securities Related Item - Gains Disbursement");
		map.put("DIV","Securities Related Item - Dividends");
		map.put("EQA","Equivalent Amount");
		map.put("EXT","Securities Related Item - External Transfer for Own Account");
		map.put("FEX","Foreign Exchange");
		map.put("INT","Interest Related Amount");
		map.put("LBX","Lock Box");
		map.put("LDP","Loan Deposit");
		map.put("MAR","Securities Related Item - Margin Payments/Receipts");
		map.put("MAT","Securities Related Item - Maturity");
		map.put("MGT","Securities Related Item - Management Fees");
		map.put("MSC","Miscellaneous");
		map.put("NWI","Securities Related Item - New Issues Distribution");
		map.put("ODC","Overdraft Charge");
		map.put("OPT","Securities Related Item - Options");
		map.put("PCH","Securities Related Item - Purchase (including STIF and Time deposits)");
		map.put("POP","Securities Related Item - Pair-off Proceeds");
		map.put("PRN","Securities Related Item - Principal Pay-down/Pay-up");
		map.put("REC","Securities Related Item - Tax Reclaim");
		map.put("RED","Securities Related Item - Redemption/Withdrawal");
		map.put("RIG","Securities Related Item - Rights");
		map.put("RTI","Returned Item");
		map.put("SAL","Securities Related Item - Sale (including STIF and Time deposits)");
		map.put("SEC","Securities (used when entering a principal amount)");
		map.put("SLE","Securities Related Item - Securities Lending Related");
		map.put("STO","Standing Order");
		map.put("STP","Securities Related Item - Stamp Duty");
		map.put("SUB","Securities Related Item - Subscription");
		map.put("SWP","Securities Related Item - SWAP Payment");
		map.put("TAX","Securities Related Item - Withholding Tax Payment");
		map.put("TCK","Travellers Cheques");
		map.put("TCM","Securities Related Item - Tripartite Collateral Management");
		map.put("TRA","Securities Related Item - Internal Transfer for Own Account");
		map.put("TRF","Transfer");
		map.put("TRN","Securities Related Item - Transaction Fee");
		map.put("UWC","Securities Related Item - Underwriting Commission");
		map.put("VDA","Value Date Adjustment (used with an entry made to withdraw an incorrectly dated entry - it will be followed by the correct entry with the relevant code)");
		map.put("WAR","Securities Related Item - Warrant");

	}
	
	public static String extractTransactionType(String expression) {
		if(expression.startsWith("S") || expression.startsWith("s")) {
			return "SWIFT transfer";
		}
		String key="";
		if(expression.length() > 4 && (expression.charAt(0) == 'N' || expression.charAt(0) == 'F')) {
			key = expression.substring(1,4);
			return map.get(key);
		}
		return "";
	}


}
