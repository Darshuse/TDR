package com.eastnets.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class InstancePK implements Serializable {

	private static final long serialVersionUID = 7010445987481072499L;

	@Column
	private long aid;

	@Column(name = "INST_S_UMIDH")
	private long instSUmidh;

	@Column(name = "INST_S_UMIDL")
	private long instSUmidl;

	@Column(name = "INST_NUM")
	private long instNum = 0;

	public InstancePK() {
	}

	public InstancePK(int aid, int umidl, int umidh, int instnum) {
		this.aid = aid;
		instSUmidl = umidl;
		instSUmidh = umidh;
		instNum = instnum;
	}

	public long getAid() {
		return aid;
	}

	public void setAid(long aid) {
		this.aid = aid;
	}

	public long getInstSUmidh() {
		return instSUmidh;
	}

	public void setInstSUmidh(long instSUmidh) {
		this.instSUmidh = instSUmidh;
	}

	public long getInstSUmidl() {
		return instSUmidl;
	}

	public void setInstSUmidl(long instSUmidl) {
		this.instSUmidl = instSUmidl;
	}

	public long getInstNum() {
		return instNum;
	}

	public void setInstNum(long instNum) {
		this.instNum = instNum;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (aid ^ (aid >>> 32));
		result = prime * result + (int) (instNum ^ (instNum >>> 32));
		result = prime * result + (int) (instSUmidh ^ (instSUmidh >>> 32));
		result = prime * result + (int) (instSUmidl ^ (instSUmidl >>> 32));
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
		InstancePK other = (InstancePK) obj;
		if (aid != other.aid)
			return false;
		if (instNum != other.instNum)
			return false;
		if (instSUmidh != other.instSUmidh)
			return false;
		if (instSUmidl != other.instSUmidl)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "InstancePK [aid=" + aid + ", instSUmidh=" + instSUmidh + ", instSUmidl=" + instSUmidl + ", instNum="
				+ instNum + "]";
	}

}
