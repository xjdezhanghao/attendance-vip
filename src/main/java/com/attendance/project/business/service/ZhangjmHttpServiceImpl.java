package com.attendance.project.business.service;

import com.attendance.project.business.dto.DeviceResponse;
import com.attendance.framework.config.ZmhConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 与掌静脉/人脸设备通过HTTP通信的服务接口的实现类。
 */
@Service("zhangjmHttpService")
public class ZhangjmHttpServiceImpl implements IZhangjmHttpService {

    private static final Logger logger = LoggerFactory.getLogger(ZhangjmHttpServiceImpl.class);

    private final ZmhConfig zmhConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public ZhangjmHttpServiceImpl(ZmhConfig zmhConfig, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.zmhConfig = zmhConfig;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    private <T> DeviceResponse<T> createErrorResponse(TypeReference<DeviceResponse<T>> typeRef, String code, String msg) {
        DeviceResponse<T> errorResponse = new DeviceResponse<>();
        errorResponse.setResult(0);
        errorResponse.setSuccess(false);
        errorResponse.setCode(code);
        errorResponse.setMsg(msg);
        return errorResponse;
    }

    // 内部实际执行POST请求的方法 (使用MultiValueMap)
    private <T> DeviceResponse<T> doPostRequestInternal(String baseUrl, String apiPath, MultiValueMap<String, String> params, TypeReference<DeviceResponse<T>> responseTypeRef) {
        if (!isServiceEnabled()) {
            logger.warn("HTTP掌脉设备服务未启用，无法发送POST请求到: {}", apiPath);
            return createErrorResponse(responseTypeRef, "SERVICE_DISABLED", "HTTP掌脉设备服务未启用");
        }
        String fullUrl = baseUrl + apiPath;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        try {
            logger.debug("发送POST请求(MultiValueMap)到: {}，参数: {}", fullUrl, params);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(fullUrl, requestEntity, String.class);
            return handleHttpResponse(responseEntity, fullUrl, responseTypeRef);
        } catch (RestClientException e) {
            logger.error("请求 {} 时发生RestClientException: {}", fullUrl, e.getMessage(), e);
            return createErrorResponse(responseTypeRef, "REST_CLIENT_EXCEPTION", "网络或客户端错误: " + e.getMessage());
        } catch (Exception e) { // Catching general exception for JsonProcessingException or others
            logger.error("请求 {} 时发生未知异常: {}", fullUrl, e.getMessage(), e);
            return createErrorResponse(responseTypeRef, "UNKNOWN_ERROR", "未知错误: " + e.getMessage());
        }
    }

    // 新增：内部实际执行POST请求的方法 (使用String请求体)
    private <T> DeviceResponse<T> doPostRequestWithStringBody(String baseUrl, String apiPath, String requestBody, TypeReference<DeviceResponse<T>> responseTypeRef) {
        if (!isServiceEnabled()) {
            logger.warn("HTTP掌脉设备服务未启用，无法发送POST请求(String body)到: {}", apiPath);
            return createErrorResponse(responseTypeRef, "SERVICE_DISABLED", "HTTP掌脉设备服务未启用");
        }
        String fullUrl = baseUrl + apiPath;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        try {
            logger.debug("发送POST请求(String body)到: {}，请求体: {}", fullUrl, requestBody);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(fullUrl, requestEntity, String.class);
            return handleHttpResponse(responseEntity, fullUrl, responseTypeRef);
        } catch (RestClientException e) {
            logger.error("请求 {} 时发生RestClientException: {}", fullUrl, e.getMessage(), e);
            return createErrorResponse(responseTypeRef, "REST_CLIENT_EXCEPTION", "网络或客户端错误: " + e.getMessage());
        } catch (Exception e) {
            logger.error("请求 {} 时发生未知异常: {}", fullUrl, e.getMessage(), e);
            return createErrorResponse(responseTypeRef, "UNKNOWN_ERROR", "未知错误: " + e.getMessage());
        }
    }


    // 内部实际执行GET请求的方法
    private <T> DeviceResponse<T> doGetRequestInternal(String baseUrl, String apiPath, MultiValueMap<String, String> params, TypeReference<DeviceResponse<T>> responseTypeRef) {
        if (!isServiceEnabled()) {
            logger.warn("HTTP掌脉设备服务未启用，无法发送GET请求到: {}", apiPath);
            return createErrorResponse(responseTypeRef, "SERVICE_DISABLED", "HTTP掌脉设备服务未启用");
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + apiPath);
        if (params != null && !params.isEmpty()) {
            builder.queryParams(params);
        }
        String fullUrl = builder.toUriString();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            logger.debug("发送GET请求到: {}", fullUrl);
            ResponseEntity<String> responseEntity = restTemplate.exchange(fullUrl, HttpMethod.GET, requestEntity, String.class);
            return handleHttpResponse(responseEntity, fullUrl, responseTypeRef);
        } catch (RestClientException e) {
            logger.error("请求 {} 时发生RestClientException: {}", fullUrl, e.getMessage(), e);
            return createErrorResponse(responseTypeRef, "REST_CLIENT_EXCEPTION", "网络或客户端错误: " + e.getMessage());
        } catch (Exception e) {
            logger.error("请求 {} 时发生未知异常: {}", fullUrl, e.getMessage(), e);
            return createErrorResponse(responseTypeRef, "UNKNOWN_ERROR", "未知错误: " + e.getMessage());
        }
    }

    // 辅助方法：处理HTTP响应
    private <T> DeviceResponse<T> handleHttpResponse(ResponseEntity<String> responseEntity, String fullUrl, TypeReference<DeviceResponse<T>> responseTypeRef) throws JsonProcessingException {
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String responseBody = responseEntity.getBody();
            logger.debug("收到来自 {} 的响应: {}", fullUrl, responseBody);
            if (responseBody != null) {
                return objectMapper.readValue(responseBody, responseTypeRef);
            } else {
                logger.error("来自 {} 的响应体为空", fullUrl);
                return createErrorResponse(responseTypeRef, "EMPTY_RESPONSE", "设备响应体为空");
            }
        }
        logger.error("请求 {} 失败，HTTP状态码: {}", fullUrl, responseEntity.getStatusCode());
        return createErrorResponse(responseTypeRef, "HTTP_ERROR_" + responseEntity.getStatusCodeValue(), "HTTP请求错误，状态码: " + responseEntity.getStatusCodeValue());
    }

    // 辅助方法，用于通用POST请求，并自动添加'pass'
    private <T> DeviceResponse<T> executePost(String baseUrl, String apiPath, String devicePassword, MultiValueMap<String, String> params, TypeReference<DeviceResponse<T>> responseTypeRef) {
        if (params == null) {
            params = new LinkedMultiValueMap<>();
        }
        params.add("pass", devicePassword);
        return doPostRequestInternal(baseUrl, apiPath, params, responseTypeRef);
    }

    // 辅助方法，用于通用GET请求，并根据需要自动添加'pass'
    private <T> DeviceResponse<T> executeGet(String baseUrl, String apiPath, String devicePassword, MultiValueMap<String, String> params, TypeReference<DeviceResponse<T>> responseTypeRef, boolean requiresPass) {
        if (params == null) {
            params = new LinkedMultiValueMap<>();
        }
        if (requiresPass) {
            params.add("pass", devicePassword);
        }
        return doGetRequestInternal(baseUrl, apiPath, params, responseTypeRef);
    }

    // --- 接口方法实现 ---

    @Override
    public DeviceResponse<String> setIdentifyCallbackUrl(String callbackUrl) {
        return setIdentifyCallbackUrl(getBaseUrlFromConfig(), getDevicePasswordFromConfig(), callbackUrl);
    }
    
    @Override
    public DeviceResponse<String> setIdentifyCallbackUrl(String baseUrl, String devicePassword, String callbackUrl) {
        // 手动构建 application/x-www-form-urlencoded 请求体字符串
        try {
            if (devicePassword == null) {
                logger.error("设备密码未配置，无法设置回调URL。");
                return createErrorResponse(new TypeReference<DeviceResponse<String>>() {}, "CONFIG_ERROR", "设备密码未配置");
            }

            StringBuilder requestBodyBuilder = new StringBuilder();
            requestBodyBuilder.append("pass=").append(URLEncoder.encode(devicePassword, StandardCharsets.UTF_8.name()));
            requestBodyBuilder.append("&callbackUrl=").append(URLEncoder.encode(callbackUrl, StandardCharsets.UTF_8.name()));

            String requestBody = requestBodyBuilder.toString();

            return doPostRequestWithStringBody(baseUrl, "/setIdentifyCallBack", requestBody, new TypeReference<DeviceResponse<String>>() {});
        } catch (UnsupportedEncodingException e) {
            logger.error("构建回调URL请求体时发生编码错误: {}", e.getMessage(), e);
            return createErrorResponse(new TypeReference<DeviceResponse<String>>() {}, "ENCODING_ERROR", "请求体编码错误");
        }
    }

    @Override
    public boolean isServiceEnabled() {
        return zmhConfig.getEnable() != null && Boolean.parseBoolean(zmhConfig.getEnable());
    }

    @Override
    public String getDevicePasswordFromConfig() {
        return zmhConfig.getDevicePassword();
    }

    @Override
    public String getBaseUrlFromConfig() {
        return zmhConfig.getBaseUrl();
    }
}
