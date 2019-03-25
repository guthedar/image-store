package com.example.filedemo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
//@Table(name = "files")
@Table(
	    name="files", 
	    uniqueConstraints=
	        @UniqueConstraint(columnNames={"fileName", "user"})
	)
public class DBFile {
	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
	
    private String fileName;
    private String user;
    
    @Lob
    private byte[] data;

    public DBFile() {

    }

    public DBFile(String fileName, String User, byte[] data) {
        this.fileName = fileName;
        this.user = User;
        this.data = data;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return user;
	}

	public void setFileType(String fileType) {
		this.user = fileType;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	
    
}