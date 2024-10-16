package com.gaorch.demo02.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpUtils {

    @Autowired
    private static RestTemplate restTemplate;

    /**
     * 发送 POST 请求，携带 JSON 格式的请求体，并处理返回的 JSON 格式响应
     *
     * @param url          请求的 URL 示例：localhost:619/message/list
     * @param requestBody  请求体对象，将会被转换为 JSON 格式
     * @param responseType 返回体的类型 示例：User.class
     * @param <T>         请求体类型
     * @param <R>         返回体类型
     * @return 返回反序列化后的响应对象
     */
    public static <T, R> R PostRequest(String url, T requestBody, Class<R> responseType) {
        // 创建请求头，指定内容类型为 JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 将请求体和请求头封装到 HttpEntity 中
        HttpEntity<T> entity = new HttpEntity<>(requestBody, headers);

        // 发送 POST 请求，并获取响应
        ResponseEntity<R> responseEntity = restTemplate.postForEntity(url, entity, responseType);

        // 返回响应体
        return responseEntity.getBody();
    }
}
