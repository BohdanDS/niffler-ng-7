package qa.guru.niffler.api;


import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyCookieJar implements CookieJar {
    private final Map<String, Cookie> cookieMap = new HashMap<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookies.stream()
                .map(cookie -> createCookie(url.host(), cookie.name(), cookie.value()))
                .collect(Collectors.toMap(Cookie::name, c -> c))
                .forEach(cookieMap::put);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        return new ArrayList<>(cookieMap.values());
    }

    private Cookie createCookie(String domain, String name, String value) {
        return new Cookie.Builder().domain(domain).name(name).value(value).build();
    }
}
