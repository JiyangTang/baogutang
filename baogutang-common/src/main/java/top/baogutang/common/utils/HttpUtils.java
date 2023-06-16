package top.baogutang.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import top.baogutang.common.constants.ErrorCodeEnum;
import top.baogutang.common.exceptions.BusinessException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

/**
 * 说明：网络请求工具
 *
 * @author nikooh
 */
@Slf4j
public final class HttpUtils {
    private static final String CHARSET_NAME = "UTF-8";

    private HttpUtils() {

    }

    /**
     * 发送post请求
     *
     * @param data 发送的数据
     * @param url  请求后台的url
     * @return 发送的result结果
     */
    public static <T> T post(Object data, String url, TypeReference<T> type) {
        try {
            if (data == null) {
                return null;
            }
            String dataStr = JSON.toJSONString(data);

            URL cUrl = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) cUrl.openConnection();
            urlConnection.setConnectTimeout(60000);
            urlConnection.setReadTimeout(60000);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("POST");
            //设置请求属性
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Charset", CHARSET_NAME);
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(dataStr.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            return dealConnect(urlConnection, type);
        } catch (Exception e) {
            log.error(">>>>>>>>>>请求异常：{}<<<<<<<<<<", e.getMessage(), e);
            throw new BusinessException(ErrorCodeEnum.E_BIZ_ERROR);
        }
    }

    public static <T> T get(String path, TypeReference<T> type) {
        return get(null, path, type);
    }

    /**
     * 发送get请求
     */
    public static <T> T get(Map<String, Object> data, String url, TypeReference<T> type) {
        try {
            String query = parseMap2Query(data);
            if (!query.isEmpty()) {
                url = url + "?" + query;
            }
            URL cUrl = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) cUrl.openConnection();
            urlConnection.setConnectTimeout(60000);
            urlConnection.setReadTimeout(60000);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("GET");
            //设置请求属性
            urlConnection.setRequestProperty("Charset", CHARSET_NAME);
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            return dealConnect(urlConnection, type);
        } catch (Exception e) {
            log.error(">>>>>>>>>>请求异常：{}<<<<<<<<<<", e.getMessage(), e);
            throw new BusinessException(ErrorCodeEnum.E_BIZ_ERROR);
        }
    }

    /**
     * 把map转成query查询字符串
     */
    private static String parseMap2Query(Map<String, Object> data) {
        if (data == null || data.size() <= 0) {
            return "";
        }
        Set<Map.Entry<String, Object>> entries = data.entrySet();
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Object> entry : entries) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return stringBuilder.toString();
    }


    /**
     * 处理连接以后的状态信息
     *
     * @param urlConnection 打开的连接
     * @return 返回发送结果
     */
    private static <T> T dealConnect(HttpURLConnection urlConnection, TypeReference<T> type) {
        try {
            int responseCode = urlConnection.getResponseCode();
            if (responseCode != 200) {
                throw new BusinessException(ErrorCodeEnum.E_BIZ_ERROR);
            }
            InputStream inputStream = urlConnection.getInputStream();
            String res = inputStream2String(inputStream);
            if (res == null || res.isEmpty()) {
                return null;
            }
            return JSON.parseObject(res, type);

        } catch (Exception e) {
            log.error(">>>>>>>>>>请求异常：{}<<<<<<<<<<", e.getMessage(), e);
            throw new BusinessException(ErrorCodeEnum.E_BIZ_ERROR);
        }
    }

    /**
     * 从输入流中读取内容到字符串
     *
     * @param inputStream 输入路
     * @return 返回字符串
     */
    private static String inputStream2String(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int len = 0;
        byte[] bytes = new byte[4096];
        try {
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(">>>>>>>>>>请求异常：{}<<<<<<<<<<", e.getMessage(), e);
            throw new BusinessException(ErrorCodeEnum.E_BIZ_ERROR);
        }
    }
}
