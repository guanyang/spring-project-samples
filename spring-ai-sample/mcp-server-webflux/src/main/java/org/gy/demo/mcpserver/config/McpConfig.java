package org.gy.demo.mcpserver.config;

import org.gy.demo.mcpserver.service.IpService;
import org.gy.demo.mcpserver.service.WeatherService;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author guanyang
 */
@Configuration
public class McpConfig {

    @Bean
    public ToolCallbackProvider weatherTools(WeatherService weatherService) {
        return MethodToolCallbackProvider.builder().toolObjects(weatherService).build();
    }

    @Bean
    public ToolCallbackProvider ipTools(IpService ipService) {
        return MethodToolCallbackProvider.builder().toolObjects(ipService).build();
    }

//    @Bean
//    public ToolCallbackProvider helloWorldTools(HelloWorldMcpService helloWorldMcpService) {
//        return MethodToolCallbackProvider.builder().toolObjects(helloWorldMcpService).build();
//    }

    public record TextInput(String input) {
    }

    @Bean
    public ToolCallback toUpperCase() {
        return FunctionToolCallback.builder("toUpperCase", (TextInput input) -> input.input().toUpperCase()).inputType(TextInput.class).description("Put the text to upper case").build();
    }
}
