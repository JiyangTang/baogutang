package top.baogutang.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * okhttp连接池单例
 *
 * @author developer
 **/
@Slf4j
public class OkHttpUtil {
    private OkHttpUtil() {
    }

    public static OkHttpClient getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    private enum Singleton {
        /**
         *
         */
        INSTANCE;
        private final OkHttpClient singleton;

        Singleton() {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(10L, TimeUnit.SECONDS);
            builder.readTimeout(60L, TimeUnit.SECONDS);
            builder.writeTimeout(60L, TimeUnit.SECONDS);
            ConnectionPool connectionPool = new ConnectionPool(50, 60, TimeUnit.SECONDS);
            builder.connectionPool(connectionPool);
            singleton = builder.build();
        }

        public OkHttpClient getInstance() {
            return singleton;
        }
    }


    public static <T> T post(String url, Map<String, String> params, TypeReference<T> type) {
        try {
            FormBody.Builder builder = new FormBody.Builder();
            params.forEach(builder::add);
            RequestBody body = builder.build();
            Request request = new Request.Builder().post(body).url(url).build();
            Response response = OkHttpUtil.getInstance().newCall(request).execute();
            if (response.isSuccessful()) {
                String content = Objects.requireNonNull(response.body()).string();
                if (StringUtils.isNotBlank(content)) {
                    return JSON.parseObject(content, type);
                }
            } else {
                log.error("postRequest fail ,url:{}, params:{}, res:{}", url, params, response);
            }
        } catch (Exception e) {
            log.error(">>>>>>>>>>request error:{}<<<<<<<<<<", e.getMessage(), e);
        }
        return null;
    }

    public static <T> T post(String url, Map<String, String> headerMap, Map<String, String> params, TypeReference<T> type) {
        try {
            Headers.Builder headerBuilder = new Headers.Builder();
            if (Objects.nonNull(headerMap) && !headerMap.isEmpty()) {
                headerMap.forEach(headerBuilder::add);
            }
            RequestBody body = RequestBody.create(JSON.toJSONString(params), MediaType.parse("application/json; charset=utf-8"));
            Request request = new Request.Builder().post(body).headers(headerBuilder.build()).url(url).build();
            Response response = OkHttpUtil.getInstance().newCall(request).execute();
            log.info("postJSONRequestReq:{},Res:{}, ", JSON.toJSONString(params), JSON.toJSONString(response));
            if (response.isSuccessful()) {
                String content = Objects.requireNonNull(response.body()).string();
                if (StringUtils.isNotBlank(content)) {
                    return JSON.parseObject(content, type);
                }
            } else {
                log.error("postJSONRequest fail ,url:{}, params:{}, res:{}", url, params, response);
            }
        } catch (Exception e) {
            log.error(">>>>>>>>>>request error:{}<<<<<<<<<<", e.getMessage(), e);
        }
        return null;
    }


    public static <T> T get(String url, Map<String, String> headerMap, Map<String, String> params, TypeReference<T> type) {
        try {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(url);
            if (Objects.nonNull(params) && !params.isEmpty()) {
                urlBuilder.append("?");
                for (Map.Entry<String, String> m : params.entrySet()) {
                    urlBuilder.append(m.getKey()).append("=");
                    urlBuilder.append(m.getValue()).append("&");
                }
            }
            Headers.Builder headerBuilder = new Headers.Builder();
            if (Objects.nonNull(headerMap) && !headerMap.isEmpty()) {
                headerMap.forEach(headerBuilder::add);
            }
            Request request = new Request.Builder().get().url(urlBuilder.toString()).headers(headerBuilder.build()).build();
            Response response = OkHttpUtil.getInstance().newCall(request).execute();
            if (response.isSuccessful()) {
                String content = Objects.requireNonNull(response.body()).string();
                if (StringUtils.isNotBlank(content)) {
                    return JSON.parseObject(content, type);
                }
            } else {
                log.error("getRequest fail ,url:{}, params:{}, res:{}", url, params, response);
            }
        } catch (Exception e) {
            log.error(">>>>>>>>>>request error:{}<<<<<<<<<<", e.getMessage(), e);
        }
        return null;
    }

}