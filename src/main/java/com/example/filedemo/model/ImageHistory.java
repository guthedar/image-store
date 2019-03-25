package com.example.filedemo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "imagehistory")
public class ImageHistory {
	@Id
    @GeneratedValue//(generator = "uuid")
   // @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name="ImageId")
    private int SerialNo;
	private String fileName;

    private String User;
    private String Operation;

    

    public ImageHistory() {

    }

    public ImageHistory(String fileName, String Operation, String User) {
        this.fileName = fileName;
        this.User = User;
        this.Operation = Operation;
    }

	public int getId() {
		return SerialNo;
	}

	public void setId(int id) {
		this.SerialNo = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	
	public String getUser() {
		return User;
	}

	public void setUser(String user) {
		User = user;
	}

	public String getOperation() {
		return Operation;
	}

	public void setOperation(String operation) {
		Operation = operation;
	}
	
	

    
}
