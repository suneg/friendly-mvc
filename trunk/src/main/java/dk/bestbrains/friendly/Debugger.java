package dk.bestbrains.friendly;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.google.gson.Gson;
import java.util.Collections;
import dk.bestbrains.friendly.RoutingResult;

public class Debugger {
    private final HttpServletRequest request;
    private final TimeWatch watch;
    private final String defaultView;
    private final RoutingResult action;

    Debugger(HttpServletRequest request, TimeWatch watch, String defaultView, RoutingResult action) {
        this.request = request;
        this.watch = watch;
        this.defaultView = defaultView;
        this.action = action;
    }

    public Map getDebugInformation() {
        Map map = new HashMap();
        Gson gson = new Gson();

        map.put("pathinfo", request.getPathInfo());
        map.put("controller", action.getControllerBaseName());
        map.put("action", action.getActionName());
        map.put("view", defaultView);
        map.put("locales", Collections.list(request.getLocales()));
        map.put("querystring", null == request.getQueryString() ? "" : request.getQueryString());
        map.put("session", gson.toJson(request.getSession()));
        map.put("method", request.getMethod());

        return map;
    }
}
