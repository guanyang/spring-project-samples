import http from 'k6/http';
import {check} from 'k6';

export const options = {
    //配置阈值判断，不满足阈值的作为错误结果
    // thresholds: {
    //     http_req_failed: ['rate<0.01'], // http errors should be less than 1%
    //     http_req_duration: ['p(95)<200'], // 95% of requests should be below 200ms
    // },
    //配置压测阶段，可以定义多个阶段进行
    // stages: [
    //     { duration: '1m', target: 200 },
    //     { duration: '1m', target: 400 },
    //     { duration: '1m', target: 600 },
    //     { duration: '1m', target: 800 },
    //     { duration: '1m', target: 1000 },
    // ],
};
export default function () {
    const params = {
        headers: {
            'token': 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjY3NCwiZGVwdE5hbWUiOiJNQkfmlbDlrZfljJbns7vnu5_pg6giLCJmaXJzdEVudHJ5RGF0ZSI6MTQ2NDcxMDQwMCwic2V4IjowLCJkZXB0SWQiOjM3LCJqb2JQb3N0SWQiOjY2NDU0MSwiZGVwdFRyZWVQYXRoTmFtZSI6IkZT77yI6aOe6YCf5Yib5paw77yJL-ezu-e7n-eglOWPkeS4reW_gy9NQkfmlbDlrZfljJbns7vnu5_pg6giLCJkZXB0VHlwZSI6MCwidXNlck5hbWUiOiLmvZjkvJ8oQnVjay5QYW4pIiwidXVpZCI6ImY3MTY1NjRiYjJmYTRjOTk4NTI5ODEzMDMyYTljN2NlIiwidWlkIjo2NzQsIm5iZiI6MTc2MjMzMDI4NSwiam9iUG9zdCI6IueglOWPkee7j-eQhiIsImVuTmFtZSI6IkJ1Y2suUGFuIiwiYWRtaW5JZCI6IjI1MzYiLCJmc0lkIjoiNzRnODJjNTciLCJpZCI6MjQyLCJleHAiOjE3NjIzNTkwODUsImlhdCI6MTc2MjMzMDI4NSwiY2hOYW1lIjoi5r2Y5LyfIiwiZnNObyI6IkZTMDAwMjE2IiwiZW1haWwiOiJidWNrLnBhbkBmZWlzdS5jb20ifQ.MY4NaZ7yXycFNPwfLTZqg5WEqTyy7TlRFqLCL9XgLMY',
        },
    };
    const res = http.get(`${__ENV.url}`,params);
    check(res, {
        'is status 200': (r) => r.status === 200
    });
}