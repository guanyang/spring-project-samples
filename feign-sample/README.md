## feign-sample

### Getting Started
#### 工程定义说明
- feign-sample-api: 接口契约定义
- feign-sample-consumer: feign消费端定义
- feign-sample-provider: feign生产端定义

#### 调用示例
- 请求示例: 
```
curl --location 'http://127.0.0.1:8081/api/test/hello?name=test&age=20'
```
- 响应示例: 
```json
{
    "code": 0,
    "message": "success",
    "data": {
        "name": "test",
        "age": 20,
        "time": 1695297874605
    }
}
```
