package com.example.filedemo.service;

import com.example.filedemo.exception.FileStorageException;
import com.example.filedemo.exception.MyFileNotFoundException;
import com.example.filedemo.model.DBFile;
import com.example.filedemo.model.ImageHistory;
import com.example.filedemo.repository.DBFileRepository;
import com.example.filedemo.repository.DBFileRepositoryHistory;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Service
public class DBFileStorageService {

	
	@Autowired
    private DBFileRepositoryHistory dbFileRepositoryHistory;
    @Autowired
    private DBFileRepository dbFileRepository;
    @Autowired
    private EntityManager entityManager;
    
    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }
    
    

    public DBFile storeFile(MultipartFile file, String user) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            
            Session session = getSession();
        	Criteria criteria = session.createCriteria(DBFile.class);
        	List<DBFile> dbFileList = (List<DBFile>) criteria.add(Restrictions.eq("fileName", fileName))
    										 .add(Restrictions.eq("user", user)).list();
        	if(dbFileList.isEmpty())
        	{
	            DBFile dbFile = new DBFile(fileName, user, file.getBytes());
	            return dbFileRepository.save(dbFile);
        	}else
        		throw new IOException();
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public DBFile getFile(String fileName, String user) {
    	Session session = getSession();
    	System.out.println("session factory"+ session);
    	Criteria criteria = session.createCriteria(DBFile.class);
		DBFile dbFile = (DBFile) criteria.add(Restrictions.eq("fileName", fileName))
										  .add(Restrictions.eq("user", user))
										  .uniqueResult();
		return dbFile;
       /* return dbFileRepository.findById(fileName)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileName));*/
    }

	public void deleteFile(String fileName, String user) {
		
		Session session = getSession();
    	System.out.println("session factory"+ session);
    	Criteria criteria = session.createCriteria(DBFile.class);
    	DBFile dbFile = (DBFile) criteria.add(Restrictions.eq("fileName", fileName))
						    			 .add(Restrictions.eq("user", user))
						                 .uniqueResult();
    	
		dbFileRepository.deleteById(dbFile.getId());
    	//dbFileRepository.delete(dbFile);
		
	}

	public ImageHistory storeHistory(String fileName, String operation, String user) {
		
		try{
		ImageHistory dbHistory = new ImageHistory(fileName, operation, user);
		System.out.println("----------from storeHistory----"+dbHistory.toString());
		System.out.println("repository is---"+dbFileRepositoryHistory);
		return dbFileRepositoryHistory.save(dbHistory);
		}
		catch(Exception e){
			System.out.println("Exception in storing history...."+e.getStackTrace());
			throw e;
		}
	}

	public List<ImageHistory> getHistory(String user) {
		Session session = getSession();
    	System.out.println("session factory"+ user);
    	Criteria criteria = session.createCriteria(ImageHistory.class);
    	if("admin".equals(user)){
    		return dbFileRepositoryHistory.findAll();
    	}else{
    		List<ImageHistory> dbFile = (List<ImageHistory>) criteria.add(Restrictions.eq("User", user)).list();
    		return dbFile;
    	}
		
	}



	public List<DBFile> getFilesInAlbum(String albumName) {
		Session session = getSession();
    	System.out.println("session factory"+ albumName);
    	Criteria criteria = session.createCriteria(DBFile.class);
		List<DBFile> dbFile = (List<DBFile>) criteria.add(Restrictions.eq("albumName", albumName)).list();
                
		return dbFile;
	}
}