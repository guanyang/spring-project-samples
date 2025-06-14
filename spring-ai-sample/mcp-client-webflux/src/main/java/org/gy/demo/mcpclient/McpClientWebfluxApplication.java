package org.gy.demo.mcpclient;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class McpClientWebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpClientWebfluxApplication.class, args);
    }

    @Value("${ai.user.input}")
    private String userInput;

    @Bean
    public CommandLineRunner predefinedQuestions(ChatClient.Builder chatClientBuilder, ToolCallbackProvider tools, ConfigurableApplicationContext context) {

        return args -> {

//            var chatClient = chatClientBuilder.defaultToolCallbacks(tools).build();
//
//            System.out.println("\n>>> QUESTION: " + userInput);
//            System.out.println("\n>>> ASSISTANT: " + chatClient.prompt(userInput).call().content());

            ToolCallback[] toolCallbacks = tools.getToolCallbacks();
            for (ToolCallback toolCallback : toolCallbacks) {
                System.out.println("Tool ToolDefinition: " + toolCallback.getToolDefinition());
                System.out.println("Tool ToolMetadata: " + toolCallback.getToolMetadata());
//                String inputJson = "{\"input\": \"Valid JSON\"}";
                String inputJson = "{\"input\": \"Valid JSON\",\"state\": \"NY\",\"latitude\": \"47.6062\",\"longitude\": \"-122.3321\"}";
                System.out.println("Tool Parameters: " + toolCallback.call(inputJson));
            }

            context.close();
        };
    }

}
