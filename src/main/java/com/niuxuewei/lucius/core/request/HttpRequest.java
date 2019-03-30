package com.niuxuewei.lucius.core.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
     */
    public JSONObject request(String url, Map<String, String> headersMap, HttpMethod method) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        if (headersMap != null) headersMap.forEach(headers::set);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String body = restTemplate.exchange(url, method, entity, String.class).getBody();
        return JSON.parseObject(body);
    }

    /**
     * GET请求，携带Header
     */
    public JSONObject get(String url, Map<String, String> headersMap) {
        return request(url, headersMap, HttpMethod.GET);
    }

    /**
     * GET请求，无Header
     */
    public JSONObject get(String url) {
        return get(url, null);
    }

    /**
     * POST请求，携带Header
     */
    public JSONObject post(String url, Map<String, String> headersMap) {
        return request(url, headersMap, HttpMethod.POST);
    }

    /**
     * 把Map转化为get的参数，并进行编码，不携带?
     *
     * @param param   参数
     * @param charset 是否进行编码，如UTF-8调用StandardCharsets.UTF_8
     * @return get字符串参数，如a=2&b=3
     */
    public String getParamMapToString(Map<String, String> param, String charset) throws UnsupportedEncodingException {
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
    public String getParamMapToString(Map<String, String> param) throws UnsupportedEncodingException {
        return getParamMapToString(param, null);
    }

}
