package com.eastnets.watchdog.criteria.builders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.eastnets.entities.Appendix;
import com.eastnets.entities.Instance;
import com.eastnets.entities.Mesg;
import com.eastnets.entities.Text;
import com.eastnets.entities.TextField;
import com.eastnets.entities.WDMessageSearchRequest;
import com.eastnets.repositories.MesgRepository;

public class SimpleAndOperatorChecker extends AbstractOperator {

	@Deprecated
	@Override
	public List<Mesg> findMatchedMessages(MesgRepository mesgRepository, WDMessageSearchRequest searchRequest, Appendix appe, boolean includeText, boolean includeTextField, boolean isPart, Date xCreaDate) {
		return mesgRepository.findAll(new Specification<Mesg>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Mesg> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				predicates.add(cb.equal(root.get("id").get("aid"), appe.getId().getAid()));
				predicates.add(cb.equal(root.get("id").get("umidl"), appe.getId().getAppeSUmidl()));
				predicates.add(cb.equal(root.get("id").get("umidh"), appe.getId().getAppeSUmidh()));
				if (isPart) {
					predicates.add(cb.equal(root.get("mesgCreaDateTime"), xCreaDate));
				}
				if (isNotEmpty(searchRequest.getMessageType())) {
					predicates.add(cb.and(cb.equal(root.get("mesgType"), searchRequest.getMessageType())));
				}
				if (isNotEmpty(searchRequest.getSenderBic())) {
					predicates.add(cb.and(cb.equal(root.get("mesgSenderX1"), searchRequest.getSenderBic())));
				}
				if (isNotEmpty(searchRequest.getReceiverBic())) {
					predicates.add(cb.and(cb.equal(root.get("xReceiverX1"), searchRequest.getReceiverBic())));
				}
				if (isNotEmpty(searchRequest.getIdentifier())) {
					predicates.add(cb.and(cb.equal(root.get("mesgIdentifier"), searchRequest.getIdentifier())));
				}
				if (isNotEmpty(searchRequest.getRequestorDN())) {
					predicates.add(cb.and(cb.equal(root.get("mesgRequestorDn"), searchRequest.getRequestorDN())));
				}
				if (isNotEmpty(searchRequest.getResponderDN())) {
					predicates.add(cb.and(cb.like(root.get("instances").get("instResponderDn"), searchRequest.getResponderDN())));
				}
				if (isNotEmpty(searchRequest.getSubFormat())) {
					predicates.add(cb.and(cb.equal(root.get("mesgSubFormat"), searchRequest.getSubFormat())));
				}
				if (searchRequest.getAmountOP() == 0) {
					predicates.add(cb.and(cb.equal(root.get("xFinCcy"), searchRequest.getCurrency())));
					predicates.add(cb.and(cb.equal(root.get("xFinAmount"), searchRequest.getAmountFrom())));
				} else if (searchRequest.getAmountOP() == 1) {
					predicates.add(cb.and(cb.equal(root.get("xFinCcy"), searchRequest.getCurrency())));
					predicates.add(cb.and(cb.lessThanOrEqualTo(root.get("xFinAmount"), searchRequest.getAmountTo())));
				} else if (searchRequest.getAmountOP() == 2) {
					predicates.add(cb.and(cb.equal(root.get("xFinCcy"), searchRequest.getCurrency())));
					predicates.add(cb.and(cb.greaterThanOrEqualTo(root.get("xFinAmount"), searchRequest.getAmountFrom())));
				} else if (searchRequest.getAmountOP() == 3) {
					predicates.add(cb.and(cb.equal(root.get("xFinCcy"), searchRequest.getCurrency())));
					predicates.add(cb.and(cb.greaterThanOrEqualTo(root.get("xFinAmount"), searchRequest.getAmountFrom())));
					predicates.add(cb.and(cb.lessThanOrEqualTo(root.get("xFinAmount"), searchRequest.getAmountTo())));
				}
				if (searchRequest.getDateValueOP() == 4) {
					Calendar c = Calendar.getInstance();
					c.setTime(appe.getId().getAppeDateTime());
					c.add(Calendar.DATE, searchRequest.getDaysValue());
					predicates.add(cb.and(cb.lessThanOrEqualTo(root.get("xFinValueDate"), c.getTime())));
				}
				if (isNotEmpty(searchRequest.getTransactionRef())) {
					predicates.add(cb.and(cb.equal(root.get("mesgTrnRef"), searchRequest.getTransactionRef())));
				}

				if (includeText && isNotEmpty(searchRequest.getFieldValue())) {
					// I must check with fadi if we need to check the rtext
				}

				if (includeTextField) {
					predicates.add(cb.and(cb.equal(root.get("textField").get("id").get("fieldCode"), searchRequest.getFieldCode())));
					predicates.add(cb.and(cb.like(root.get("textField").get("value"), "%" + searchRequest.getFieldValue() + "%")));

				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		});
	}

	@Override
	public boolean isMatchedMessage(Mesg mesg, WDMessageSearchRequest searchRequest, Appendix appe, boolean includeText, boolean includeTextField, boolean isPart, Date xCreaDate) {
		if (isNotEmpty(searchRequest.getMessageType())) {
			if (!searchRequest.getMessageType().equalsIgnoreCase(mesg.getMesgType())) {
				return false;
			}
		}
		if (isNotEmpty(searchRequest.getSenderBic())) {
			if (!searchRequest.getSenderBic().equalsIgnoreCase(mesg.getMesgSenderX1())) {
				return false;
			}
		}
		if (isNotEmpty(searchRequest.getReceiverBic())) {
			if (!searchRequest.getReceiverBic().equalsIgnoreCase(mesg.getxReceiverX1())) {
				return false;
			}
		}
		if (isNotEmpty(searchRequest.getIdentifier())) {
			if (!searchRequest.getIdentifier().equalsIgnoreCase(mesg.getMesgIdentifier())) {
				return false;
			}
		}
		if (isNotEmpty(searchRequest.getRequestorDN())) {
			if (!searchRequest.getRequestorDN().equalsIgnoreCase(mesg.getMesgRequestorDn())) {
				return false;
			}
		}
		if (isNotEmpty(searchRequest.getResponderDN())) {
			Instance instance = mesg.getInstances().get(0);
			if (!searchRequest.getResponderDN().equalsIgnoreCase(instance.getInstResponderDn())) {
				return false;
			}
		}
		if (isNotEmpty(searchRequest.getSubFormat())) {
			if (!searchRequest.getSubFormat().equalsIgnoreCase(mesg.getMesgSubFormat())) {
				return false;
			}
		}
		if (searchRequest.getAmountOP() == 0) {
			if (!searchRequest.getCurrency().equalsIgnoreCase(mesg.getxFinCcy())) {
				return false;
			}
			int compareTo = BigDecimal.valueOf(searchRequest.getAmountFrom()).compareTo(mesg.getxFinAmount());
			if (compareTo != 0) {
				return false;
			}
		} else if (searchRequest.getAmountOP() == 1) {
			if (!searchRequest.getCurrency().equalsIgnoreCase(mesg.getxFinCcy())) {
				return false;
			}
			int compareTo = BigDecimal.valueOf(searchRequest.getAmountTo()).compareTo(mesg.getxFinAmount());
			if (compareTo != 0 && compareTo != 1) {
				return false;
			}
		} else if (searchRequest.getAmountOP() == 2) {
			if (!searchRequest.getCurrency().equalsIgnoreCase(mesg.getxFinCcy())) {
				return false;
			}
			int compareTo = BigDecimal.valueOf(searchRequest.getAmountFrom()).compareTo(mesg.getxFinAmount());
			if (compareTo != 0 && compareTo != -1) {
				return false;
			}
		} else if (searchRequest.getAmountOP() == 3) {

			if (!searchRequest.getCurrency().equalsIgnoreCase(mesg.getxFinCcy())) {
				return false;
			}
			int compareTo = BigDecimal.valueOf(searchRequest.getAmountFrom()).compareTo(mesg.getxFinAmount()); // 1
			if (compareTo != 0 && compareTo != -1) {
				return false;
			}
			int compareTo2 = BigDecimal.valueOf(searchRequest.getAmountTo()).compareTo(mesg.getxFinAmount());
			if (compareTo2 != 0 && compareTo2 != 1) {
				return false;
			}
		}

		if (searchRequest.getDateValueOP() == 4) {
			Calendar c = Calendar.getInstance();
			c.setTime(appe.getId().getAppeDateTime());
			c.add(Calendar.DATE, searchRequest.getDaysValue());
			Date time = c.getTime();
			LocalDate convertToLocalDate1 = convertToLocalDate(time);
			LocalDate convertToLocalDate2 = convertToLocalDate(mesg.getxFinValueDate());
			if (!convertToLocalDate1.isEqual(convertToLocalDate2)) {
				return false;
			}
		}
		if (isNotEmpty(searchRequest.getTransactionRef())) {
			if (!searchRequest.getTransactionRef().equalsIgnoreCase(mesg.getMesgTrnRef())) {
				return false;
			}
		}

		if (includeText && isNotEmpty(searchRequest.getFieldValue())) {
			Text text = mesg.getText();
			boolean isMatchedText = text.getTextDataBlock().contains(searchRequest.getFieldValue());
			if (!isMatchedText) {
				return false;
			}
		}

		if (includeTextField) {
			Integer fieldCode = searchRequest.getFieldCode();
			String fieldValue = searchRequest.getFieldValue();
			List<TextField> textField = mesg.getTextField();
			List<TextField> matchedTextField = textField.stream().filter(field -> (field.getValue().contains(fieldValue) && field.getId().getFieldCode() == fieldCode)).collect(Collectors.toList());
			if (matchedTextField == null || matchedTextField.isEmpty()) {
				return false;
			}

		}

		return true;
	}

	public LocalDate convertToLocalDate(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

}
