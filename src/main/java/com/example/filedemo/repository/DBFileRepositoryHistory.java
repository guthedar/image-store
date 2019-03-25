package com.example.filedemo.repository;

import com.example.filedemo.model.ImageHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("ImageHistory")
public interface DBFileRepositoryHistory extends JpaRepository<ImageHistory, String> {

}