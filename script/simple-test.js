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
    const res = http.get(`${__ENV.url}`);
    check(res, {
        'is status 200': (r) => r.status === 200
    });
}