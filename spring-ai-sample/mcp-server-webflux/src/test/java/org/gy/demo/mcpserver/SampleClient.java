package org.gy.demo.mcpserver;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.spec.McpClientTransport;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ListToolsResult;

import java.util.Map;

public class SampleClient {

    private final McpClientTransport transport;

    public SampleClient(McpClientTransport transport) {
        this.transport = transport;
    }

    public void run() {

        var client = McpClient.sync(this.transport).build();

        client.initialize();

        client.ping();

        // List and demonstrate tools
        ListToolsResult toolsList = client.listTools();
        System.out.println("Available Tools = " + toolsList);

        CallToolResult weatherForcastResult = client.callTool(new CallToolRequest("getWeatherForecastByLocation",
                Map.of("latitude", "47.6062", "longitude", "-122.3321")));
        System.out.println("Weather Forcast: " + weatherForcastResult);

        CallToolResult alertResult = client.callTool(new CallToolRequest("getAlerts", Map.of("state", "NY")));
        System.out.println("Alert Response = " + alertResult);

        client.closeGracefully();

    }
}
