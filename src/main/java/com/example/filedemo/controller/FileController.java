package com.example.filedemo.controller;

import com.example.filedemo.model.DBFile;
import com.example.filedemo.model.ImageHistory;
import com.example.filedemo.payload.UploadFileResponse;
import com.example.filedemo.service.DBFileStorageService;

import org.apache.catalina.connector.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private DBFileStorageService DBFileStorageService;

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, @RequestParam(value="User", defaultValue="admin") String User) {
        
    	DBFile dbFile = DBFileStorageService.storeFile(file, User);
    	String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    	String Operation = "Upload";
        DBFileStorageService.storeHistory(fileName,Operation,User);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(dbFile.getId())
                .toUriString();
        

        return new UploadFileResponse(dbFile.getFileName(), fileDownloadUri,
                file.getContentType(), file.getSize(), Operation, User);
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, @RequestParam("User") String User) {
        System.out.println("---------------------values of Operation="+ " and "+ User);
    	return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file,User))
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, @RequestParam("User") String User) {
        // Load file from database
    	System.out.println("---------------- download fileId from java " + fileName);
        DBFile dbFile = DBFileStorageService.getFile(fileName,User);
        System.out.println("File Id is ="+dbFile.getId());
        DBFileStorageService.storeHistory(dbFile.getFileName(),"Download",User);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }
    
    @GetMapping("/history/{user}")
    public ResponseEntity<List<ImageHistory>> operationHistory(@PathVariable String user){
    	List<ImageHistory> history = DBFileStorageService.getHistory(user);
    	System.out.println("----------history----"+history.toString());
    	return ResponseEntity.ok().body(history);
    }
    
    @GetMapping("/retrieveFile/{fileName}")
    public ResponseEntity<Resource> retrieveFile(@PathVariable String fileName, @RequestParam("User") String User) {
        // Load file from database
    	System.out.println("---------------- retrieve fileId from java " + fileName);
        DBFile dbFile = DBFileStorageService.getFile(fileName,User);
        DBFileStorageService.storeHistory(dbFile.getFileName(),"Retrieve",User);
        /*byte[] data = dbFile.getData();
        List dbFileList = new ArrayList<byte[]>();
        dbFileList.add(data);*/
		System.out.println("------------ retrieveFile "+ dbFile.getData());
        return ResponseEntity.ok()
                .body(new ByteArrayResource(dbFile.getData()));
    }
    
    @GetMapping("/retrieveFiles/{albumName}")
    public ResponseEntity<List<DBFile>> retrieveFiles(@PathVariable String albumName) {
        // Load file from database
    	System.out.println("---------------- retrieveFiles fileId from java " + albumName);
        List<DBFile> dbFile = DBFileStorageService.getFilesInAlbum(albumName);
        
        String zipFilePath = System.getProperty("user.dir")+"/"+albumName;
        
        /*byte[] data = dbFile.getData();
        List<byte[]> dbFileList = new ArrayList<byte[]>();
        dbFileList.add(data);*/
        if(dbFile!=null)
		System.out.println("------------ retrieveFile "+ dbFile.size());
        return ResponseEntity.ok()
                .body(dbFile);
    }
    
    /*@GetMapping("/retrieveByte/{fileId}")
    public byte[] retrieveByte(@PathVariable String fileId) {
        // Load file from database
    	System.out.println("---------------- retrieve fileId from java " + fileId);
        DBFile dbFile = DBFileStorageService.getFile(fileId);
        System.out.println("------------ retrieveByte "+ dbFile.getData());
		return dbFile.getData();

        //return new ByteArrayResource(dbFile.getData());
    }*/
    
    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<Resource> deleteFile(@PathVariable String fileName, @RequestParam("User") String User){
    	System.out.println("----------inside fileId="+fileName);
    	String[] fileNames = fileName.split(":");
    	for(String file:fileNames)
    	{
	    	DBFile dbFile = DBFileStorageService.getFile(file,User);
	    	DBFileStorageService.deleteFile(file,User);
	    	DBFileStorageService.storeHistory(dbFile.getFileName(),"Delete",User);
    	}
    	return ResponseEntity.ok().build();
    }

}