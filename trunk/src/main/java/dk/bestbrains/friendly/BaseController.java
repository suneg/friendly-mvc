package dk.bestbrains.friendly;

import java.util.logging.Level;
import java.util.logging.Logger;
import dk.bestbrains.friendly.results.XmlResult;
import dk.bestbrains.friendly.results.JsonResult;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {
    public Map View = new HashMap();
    public Map Errors = new GracefulHashMap();
    private HttpServletRequest request;
    private HttpSession session;
    private HttpServletResponse response;

    public BaseController() {
        View.put("errors", Errors);
    }
    
    public void setDefaultView(String defaultViewToRender) {
        View.put("view", defaultViewToRender);
    }

    protected void render(String view) {
        View.put("view", view);
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletRequest getRequest() {
        return this.request;
    }

    public void setHttpSession(HttpSession session) {
        this.session = session;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public HttpServletResponse getResponse() {
        return this.response;
    }

    public HttpSession getHttpSession() {
        return this.session;
    }

    public JsonResult toJson(Object object) {
        Gson gson = new Gson();
        return new JsonResult(gson.toJson(object));
    }

    public XmlResult toXml(Object object) {
        XStream xstream = new XStream();
        return new XmlResult(xstream.toXML(object));
    }

    public void redirect(String url) {
        try {
            response.sendRedirect(url);
        } catch (IOException ex) {
            Logger.getLogger(BaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void redirect(String controller, String action) {
        try {
            response.sendRedirect("http://localhost" + request.getContextPath() + "/" + controller + "/" + action);
        } catch (IOException ex) {
            Logger.getLogger(BaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void preAction() {
        
    }

    protected void postAction() {

    }
}
