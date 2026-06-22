package com.mco;

import java.util.Date;

import org.hibernate.annotations.Columns;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class GeekUserDetails {
	@Id
	private int geekUserId;
	@Column
	private String geekUsername;
	@Column
	private int numberOfPosts;
	@Column
	private String createdBy;
	@Column
	private Date createdDate;

	public int getGeekUserId() {
		return geekUserId;
	}

	public void setGeekUserId(int geekUserId) {
		this.geekUserId = geekUserId;
	}

	public String getGeekUsername() {
		return geekUsername;
	}

	public void setGeekUsername(String geekUsername) {
		this.geekUsername = geekUsername;
	}

	public int getNumberOfPosts() {
		return numberOfPosts;
	}

	public void setNumberOfPosts(int numberOfPosts) {
		this.numberOfPosts = numberOfPosts;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return "GeekUserDetails [geekUserId=" + geekUserId + ", geekUsername=" + geekUsername + ", numberOfPosts="
				+ numberOfPosts + ", createdBy=" + createdBy + ", createdDate=" + createdDate + "]";
	}

}