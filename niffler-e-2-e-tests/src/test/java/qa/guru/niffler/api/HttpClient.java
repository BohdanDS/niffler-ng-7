package qa.guru.niffler.api;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import java.util.Arrays;
import java.util.List;

public class HttpClient {

    private static final String DOMAIN = "127.0.0.1";

    private static final Cookie COOKIE_JSESSIONID = new Cookie.Builder()
            .name("JSESSIONID")
            .value("E8FD87827AEE39DD20931C7FD0E6C9AB")
            .domain(DOMAIN)
            .path("/")
            .httpOnly()
            .secure()
            .build();

    private static final Cookie COOKIE_XSRF_TOKEN = new Cookie.Builder()
            .name("XSRF-TOKEN")
            .value("825b00e6-a73e-48d1-b480-89db301251e3")
            .domain(DOMAIN)
            .path("/")
            .httpOnly()
            .secure()
            .build();

    private static final CookieJar COOKIE_JAR = new CookieJar() {
        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            // Можно добавить сохранение куков, если это необходимо
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            if (url.host().equals(DOMAIN)) {
                return Arrays.asList(COOKIE_JSESSIONID, COOKIE_XSRF_TOKEN);
            }
            return List.of();
        }
    };

    public static OkHttpClient createClient() {
        return new OkHttpClient.Builder()
                .cookieJar(COOKIE_JAR)
                .build();
    }
}


