package com.peertutor.BookmarkMgr.controller;

import com.peertutor.BookmarkMgr.model.Bookmark;
import com.peertutor.BookmarkMgr.repository.BookmarkRepository;
import com.peertutor.BookmarkMgr.util.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(path="/bookmark")
public class BookmarkController {
    @Autowired
    AppConfig appConfig;
    @Autowired
    private BookmarkRepository bookmarkRepository;// = new CustomerRepository();
    @GetMapping(path="/")
    public @ResponseBody String defaultResponse(){

        System.out.println("appConfig="+ appConfig.toString());
        System.out.println("ver"+ SpringVersion.getVersion());
        return "Hello world Spring Ver = " + SpringVersion.getVersion() + "From Bookmark mgr";

    }
    @GetMapping(path="/public-api")
    public @ResponseBody String callPublicApi() {
        String endpoint = "https://api.publicapis.org/entries"; //url+":"+port;
        System.out.println("endpoint" + endpoint);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);

        return response.toString();
    }

    @GetMapping(path="/call-app-student-mgr")
    public @ResponseBody String callAppTwo() {
        String url = appConfig.getStudentMgr().get("url");
        String port = appConfig.getStudentMgr().get("port");

        String endpoint = url+":"+port + "/";
        System.out.println("endpoint" + endpoint);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);

        return response.toString();
    }
    @GetMapping(path="/health")
    public @ResponseBody String healthCheck(){
        return "Ok";
    }

    @PostMapping(path = "/add")
    public @ResponseBody String addNewCustomer(@RequestBody Map<String, String> customerDTO) {

        // <validation logic here>
        // todo: generalise validation logic

        // <retrieve data from request body>
        System.out.println("customerMap= " +customerDTO);
        String firstName = customerDTO.get("firstName");
        String lastName = customerDTO.get("lastName");
        // create DTO
        Bookmark customer = new Bookmark(firstName, lastName);

        // dao layer: save object to db
        bookmarkRepository.save(customer);

        // todo: better logging
        // todo: generalise response message
        return "Saved";
    }
    @GetMapping(path="/all")
    public @ResponseBody Iterable<Bookmark> getAllCustomers (){

        return bookmarkRepository.findAll();
    }


}
