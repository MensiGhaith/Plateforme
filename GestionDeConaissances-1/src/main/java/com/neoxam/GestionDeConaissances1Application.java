package com.neoxam;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.neoxam.service.FilesStorageService;

@SpringBootApplication
public class GestionDeConaissances1Application implements CommandLineRunner{
	 @Resource
	  FilesStorageService storageService;
	public static void main(String[] args) {
		SpringApplication.run(GestionDeConaissances1Application.class, args);
	}
	
	@Override
	  public void run(String... arg) throws Exception {
	    storageService.deleteAll();
	    storageService.init();
	  }
	 

}
