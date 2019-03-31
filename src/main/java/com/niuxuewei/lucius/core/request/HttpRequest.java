package com.niuxuewei.lucius.core.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Http通用请求
 */
@Slf4j
public class HttpRequest {

    /**
     * 通用请求
     * @param url 请求地址
     * @param headersMap headers，用HashMap<String, String>
     * @param dataMap from表单，用LinkedMultiValueMap
     * @param method 请求方法
     * @return json字符串
     */
    public String request(String url, Map<String, String> headersMap, MultiValueMap<String, String> dataMap, HttpMethod method) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        if (headersMap != null) headersMap.forEach(headers::set);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(dataMap, headers);
        return restTemplate.exchange(url, method, entity, String.class).getBody();
    }

    /**
     * GET请求，携带Header
     *
     * @param url        请求地址
     * @param headersMap headers，请用HashMap<String, String>
     * @return json字符串
     */
    public String get(String url, Map<String, String> headersMap) {
        return request(url, headersMap, null, HttpMethod.GET);
    }

    /**
     * GET请求，无Header
     */
    public String get(String url) {
        return get(url, null);
    }

    /**
     * POST表单请求，携带Header
     * @param url 目标网址
     * @param headersMap headers，默认包括Content-Type: application/x-www-form-urlencoded
     * @param dataMap 可以new一个LinkedMultiValueMap
     * @return json字符串
     */
    public String post(String url, Map<String, String> headersMap, MultiValueMap<String, String> dataMap) {
        return request(url, headersMap, dataMap, HttpMethod.POST);
    }

    /**
     * POST请求，无Header
     */
    public String post(String url, MultiValueMap<String, String> dataMap) {
        return request(url, null, dataMap, HttpMethod.POST);
    }

    /**
     * 把Map转化为get的参数，并进行编码，不携带?
     *
     * @param param   参数
     * @param charset 是否进行编码，如UTF-8调用StandardCharsets.UTF_8
     * @return get字符串参数，如a=2&b=3
     */
    public String convertMapToForm(Map<String, String> param, String charset) throws UnsupportedEncodingException {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> pair : param.entrySet()) {
            stringBuilder.append(pair.getKey()).append("=");
            if (charset == null) {
                stringBuilder.append(pair.getValue()).append("&");
            } else {
                stringBuilder.append(URLEncoder.encode(pair.getValue(), "UTF-8")).append("&");
            }
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    /**
     * 把Map转化为get的参数，不携带?，不进行编码
     *
     * @param param 参数
     * @return get字符串参数，如a=2&b=3
     */
    public String convertMapToForm(Map<String, String> param) throws UnsupportedEncodingException {
        return convertMapToForm(param, null);
    }

}
