package com.eastnets.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WDUSERSEARCHPARAMETER")
public class MessageSearchRequest extends SearchRequest {

	@Id
	@Column(name = "REQUESTID")
	private Integer requestID;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "SENDERBIC")
	private String senderBic;

	@Column(name = "RECEIVERBIC")
	private String receiverBic;

	@Column(name = "TRN_REF")
	private String transactionRef;

	@Column(name = "SUBFORMAT")
	private String subFormat;

	@Column(name = "AMOUNT")
	private Double amountFrom;

	@Column(name = "AMOUNTTO")
	private Double amountTo;

	@Column(name = "CCY")
	private String currency;

	@Column(name = "MSGTYPE")
	private String messageType;

	@Column(name = "DAYSVALUE")
	private Integer daysValue;

	@Column(name = "FIELDCODE")
	private Integer fieldCode;

	@Column(name = "FIELDOPTION")
	private String fieldOption;

	@Column(name = "FIELDCODEID")
	private Integer fieldCodeID;

	@Column(name = "FIELDVALUE")
	private String fieldValue;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "AUTODELETE")
	private Integer autoDelete;

	@Column(name = "USERGROUP")
	private Integer groupRequest;

	@Column(name = "EXPIRATIONDATE")
	private Date expirationDate;

	@Column(name = "STATUS")
	private Integer status;

	@Column(name = "REQUESTOR_DN")
	private String requestorDN;

	@Column(name = "RESPONDER_DN")
	private String responserDN;

	public MessageSearchRequest() {

	}

	public Integer getRequestID() {
		return requestID;
	}

	public void setRequestID(Integer requestID) {
		this.requestID = requestID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSenderBic() {
		return senderBic;
	}

	public void setSenderBic(String senderBic) {
		this.senderBic = senderBic;
	}

	public String getReceiverBic() {
		return receiverBic;
	}

	public void setReceiverBic(String receiverBic) {
		this.receiverBic = receiverBic;
	}

	public String getTransactionRef() {
		return transactionRef;
	}

	public void setTransactionRef(String transactionRef) {
		this.transactionRef = transactionRef;
	}

	public String getSubFormat() {
		return subFormat;
	}

	public void setSubFormat(String subFormat) {
		this.subFormat = subFormat;
	}

	public Double getAmountFrom() {
		return amountFrom;
	}

	public void setAmountFrom(Double amountFrom) {
		this.amountFrom = amountFrom;
	}

	public Double getAmountTo() {
		return amountTo;
	}

	public void setAmountTo(Double amountTo) {
		this.amountTo = amountTo;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public Integer getDaysValue() {
		return daysValue;
	}

	public void setDaysValue(Integer daysValue) {
		this.daysValue = daysValue;
	}

	public Integer getFieldCode() {
		return fieldCode;
	}

	public void setFieldCode(Integer fieldCode) {
		this.fieldCode = fieldCode;
	}

	public String getFieldOption() {
		return fieldOption;
	}

	public void setFieldOption(String fieldOption) {
		this.fieldOption = fieldOption;
	}

	public Integer getFieldCodeID() {
		return fieldCodeID;
	}

	public void setFieldCodeID(Integer fieldCodeID) {
		this.fieldCodeID = fieldCodeID;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getAutoDelete() {
		return autoDelete;
	}

	public void setAutoDelete(Integer autoDelete) {
		this.autoDelete = autoDelete;
	}

	public Integer getGroupRequest() {
		return groupRequest;
	}

	public void setGroupRequest(Integer groupRequest) {
		this.groupRequest = groupRequest;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRequestorDN() {
		return requestorDN;
	}

	public void setRequestorDN(String requestorDN) {
		this.requestorDN = requestorDN;
	}

	public String getResponserDN() {
		return responserDN;
	}

	public void setResponserDN(String responserDN) {
		this.responserDN = responserDN;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amountFrom == null) ? 0 : amountFrom.hashCode());
		result = prime * result + ((amountTo == null) ? 0 : amountTo.hashCode());
		result = prime * result + ((autoDelete == null) ? 0 : autoDelete.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((daysValue == null) ? 0 : daysValue.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((expirationDate == null) ? 0 : expirationDate.hashCode());
		result = prime * result + ((fieldCode == null) ? 0 : fieldCode.hashCode());
		result = prime * result + ((fieldCodeID == null) ? 0 : fieldCodeID.hashCode());
		result = prime * result + ((fieldOption == null) ? 0 : fieldOption.hashCode());
		result = prime * result + ((fieldValue == null) ? 0 : fieldValue.hashCode());
		result = prime * result + ((groupRequest == null) ? 0 : groupRequest.hashCode());
		result = prime * result + ((messageType == null) ? 0 : messageType.hashCode());
		result = prime * result + ((receiverBic == null) ? 0 : receiverBic.hashCode());
		result = prime * result + ((requestID == null) ? 0 : requestID.hashCode());
		result = prime * result + ((requestorDN == null) ? 0 : requestorDN.hashCode());
		result = prime * result + ((responserDN == null) ? 0 : responserDN.hashCode());
		result = prime * result + ((senderBic == null) ? 0 : senderBic.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((subFormat == null) ? 0 : subFormat.hashCode());
		result = prime * result + ((transactionRef == null) ? 0 : transactionRef.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageSearchRequest other = (MessageSearchRequest) obj;
		if (amountFrom == null) {
			if (other.amountFrom != null)
				return false;
		} else if (!amountFrom.equals(other.amountFrom)) {
			return false;
		}
		if (amountTo == null) {
			if (other.amountTo != null)
				return false;
		} else if (!amountTo.equals(other.amountTo)) {
			return false;
		}
		if (autoDelete == null) {
			if (other.autoDelete != null)
				return false;
		} else if (!autoDelete.equals(other.autoDelete)) {
			return false;
		}
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency)) {
			return false;
		}
		if (daysValue == null) {
			if (other.daysValue != null)
				return false;
		} else if (!daysValue.equals(other.daysValue)) {
			return false;
		}
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email)) {
			return false;
		}
		if (expirationDate == null) {
			if (other.expirationDate != null)
				return false;
		} else if (!expirationDate.equals(other.expirationDate)) {
			return false;
		}
		if (fieldCode == null) {
			if (other.fieldCode != null)
				return false;
		} else if (!fieldCode.equals(other.fieldCode)) {
			return false;
		}
		if (fieldCodeID == null) {
			if (other.fieldCodeID != null)
				return false;
		} else if (!fieldCodeID.equals(other.fieldCodeID)) {
			return false;
		}
		if (fieldOption == null) {
			if (other.fieldOption != null)
				return false;
		} else if (!fieldOption.equals(other.fieldOption)) {
			return false;
		}
		if (fieldValue == null) {
			if (other.fieldValue != null)
				return false;
		} else if (!fieldValue.equals(other.fieldValue)) {
			return false;
		}
		if (groupRequest == null) {
			if (other.groupRequest != null)
				return false;
		} else if (!groupRequest.equals(other.groupRequest)) {
			return false;
		}
		if (messageType == null) {
			if (other.messageType != null)
				return false;
		} else if (!messageType.equals(other.messageType)) {
			return false;
		}
		if (receiverBic == null) {
			if (other.receiverBic != null) {
				return false;
			}
		} else if (!receiverBic.equals(other.receiverBic)) {
			return false;
		}
		if (requestID == null) {
			if (other.requestID != null) {
				return false;
			}
		} else if (!requestID.equals(other.requestID)) {
			return false;
		}
		if (requestorDN == null) {
			if (other.requestorDN != null) {
				return false;
			}
		} else if (!requestorDN.equals(other.requestorDN)) {
			return false;
		}
		if (responserDN == null) {
			if (other.responserDN != null) {
				return false;
			}
		} else if (!responserDN.equals(other.responserDN)) {
			return false;
		}
		if (senderBic == null) {
			if (other.senderBic != null) {
				return false;
			}
		} else if (!senderBic.equals(other.senderBic)) {
			return false;
		}
		if (status == null) {
			if (other.status != null) {
				return false;
			}
		} else if (!status.equals(other.status))
			return false;
		if (subFormat == null) {
			if (other.subFormat != null) {
				return false;
			}
		} else if (!subFormat.equals(other.subFormat)) {
			return false;
		}
		if (transactionRef == null) {
			if (other.transactionRef != null) {
				return false;
			}
		} else if (!transactionRef.equals(other.transactionRef)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "MessageSearchRequest [requestID=" + requestID + ", description=" + description + ", senderBic="
				+ senderBic + ", receiverBic=" + receiverBic + ", transactionRef=" + transactionRef + ", subFormat="
				+ subFormat + ", amountFrom=" + amountFrom + ", amountTo=" + amountTo + ", currency=" + currency
				+ ", messageType=" + messageType + ", daysValue=" + daysValue + ", fieldCode=" + fieldCode
				+ ", fieldOption=" + fieldOption + ", fieldCodeID=" + fieldCodeID + ", fieldValue=" + fieldValue
				+ ", email=" + email + ", autoDelete=" + autoDelete + ", groupRequest=" + groupRequest
				+ ", expirationDate=" + expirationDate + ", status=" + status + ", requestorDN=" + requestorDN
				+ ", responserDN=" + responserDN + "]";
	}

}