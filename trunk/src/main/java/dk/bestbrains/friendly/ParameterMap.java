package dk.bestbrains.friendly;

import javax.servlet.http.HttpServletRequest;

public class ParameterMap {

    private HttpServletRequest request;

    public ParameterMap(HttpServletRequest request) {
        this.request = request;
    }

    public boolean containsKey(String key) {
        return request.getParameterMap().containsKey(key);
    }

    public Object getAttribute(String key) {
        return request.getAttribute(key);
    }

    public void setAttribute(String key, String value) {
        request.setAttribute(key, value);
    }
}
