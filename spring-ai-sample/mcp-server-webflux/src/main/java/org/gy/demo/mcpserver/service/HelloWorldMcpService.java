//package org.gy.demo.mcpserver.service;
//
//import jakarta.annotation.Resource;
//import org.gy.demo.mcpserver.entity.HelloWorld;
//import org.gy.demo.mcpserver.repository.HelloWorldRepository;
//import org.springframework.ai.tool.annotation.Tool;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
///**
// * @author guanyang
// */
//@Service
//public class HelloWorldMcpService {
//
//    @Resource
//    private HelloWorldRepository helloWorldRepository;
//
//    @Tool(description = "Get entity by id")
//    public HelloWorld findById(Long id) {
//        return helloWorldRepository.findById(id).block();
//    }
//
//    @Tool(description = "Find entity by name")
//    public List<HelloWorld> findByName(String name) {
//        return helloWorldRepository.findByName(name).collectList().block();
//    }
//
//    @Tool(description = "Save entity")
//    public HelloWorld save(HelloWorld entity) {
//        return helloWorldRepository.save(entity).block();
//    }
//}
