package com.tomato.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: wangrun
 * Date: 2016/9/5 10:22
 */
public class HttpClientManager {

    private static final Logger LOG = LoggerFactory.getLogger(HttpClientManager.class);

    public static final String POST = "post";

    private static ExecutorService httpPool;

    public HttpClientManager() {
        if (httpPool == null) {
            synchronized (HttpClientManager.class) {
                if (httpPool == null) {
                    httpPool = Executors.newFixedThreadPool(2);
                }
            }
        }
    }

    public void httpGet(String url) {
        try {
            HttpThread httpThread = new HttpThread(url);
            httpPool.execute(httpThread);
        } catch (Exception e) {
            LOG.error("send url:" + url + " failed.", e);
        }
    }

    public void httpPost(String url, Map<String, String> parameters) {
        try {
            HttpThread httpThread = new HttpThread(url);
            httpThread.setType(POST);
            httpThread.setParameters(parameters);
            httpPool.execute(httpThread);
        } catch (Exception e) {
            LOG.error("send url:" + url + " failed.", e);
        }
    }

    class HttpThread implements Runnable {

        private String type;

        private String url;

        private Map<String, String> parameters;

        public HttpThread(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Map<String, String> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, String> parameters) {
            this.parameters = parameters;
        }

        @Override
        public void run() {
            if (POST.equals(type)) {
                String message = HttpClientUtil.httpPost(getUrl(), getParameters());
                LOG.info("http post: {}, {}", getUrl(), message);
            }else {
                String message = HttpClientUtil.httpGet(getUrl());
                LOG.info("http get: {}, {}", getUrl(), message);
            }
        }
    }
}
