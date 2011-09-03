package dk.bestbrains.friendly;

import freemarker.cache.FileTemplateLoader;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import freemarker.template.Template;
import freemarker.template.Configuration;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import javax.servlet.annotation.WebServlet;
import dk.bestbrains.friendly.results.ActionResult;
import dk.bestbrains.friendly.results.JsonResult;
import dk.bestbrains.friendly.results.ViewResult;
import dk.bestbrains.friendly.results.XmlResult;

@WebServlet
public class Servlet extends HttpServlet {
    private static Configuration cfg;
    private static boolean debugMode = false;
    private static Properties friendlyProperties;
    private static RoutingHandler routingHandler;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        TimeWatch watch = TimeWatch.start();
        try {
            initIfNeeded();
        } catch (Exception ex) {
            throw new ServletException(ex.getMessage(), ex);
        }

        ServletOutputStream output = response.getOutputStream();
        ByteArrayOutputStream memoryStream = new ByteArrayOutputStream();
        String templatePath = "";
        
        try { 
            RoutingResult action = routingHandler.getRoute(request.getPathInfo());

            Map viewData = new HashMap();
            
            ActionResult actionResult = constructAndInvokeController(action, request, response);

            OutputStreamWriter writer = new OutputStreamWriter(memoryStream);
            
            if(actionResult.getClass().equals(ViewResult.class)) {
                viewData.put("view", ((ViewResult)actionResult).getViewData());
                viewData.put("util", new Utilities());
                response.setContentType("text/html;charset=UTF-8");

                String viewName = (String)((Map)(viewData.get("view"))).get("view");

                if(debugMode) {
                    Debugger debugger = new Debugger(request, watch, viewName, action);
                    viewData.put("debug", debugger.getDebugInformation());
                }

                templatePath = getTemplatePath(action, viewName);

                processTemplate(viewData, templatePath,
                        writer, request.getLocale());
            }
            else if (actionResult.getClass().equals(JsonResult.class)) {
                writer.write(((JsonResult)actionResult).getValue());
                response.setContentType("text/html;charset=UTF-8");
            }
            else if (actionResult.getClass().equals(XmlResult.class)) {
                writer.write(((XmlResult)actionResult).getValue());
                response.setContentType("text/xml;charset=UTF-8");
            }
            
            writer.flush();
            output.write(memoryStream.toByteArray());
        }
        catch(Exception e) {
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            
            if(debugMode) {
                DebugInformation information = new DebugInformation(e, templatePath, cfg);
                information.print(output);
            }

        }
        finally {
            if(debugMode)
                response.addHeader("render-time", watch.time() + "ms");
            
            output.close();
        }
    }

    private void initIfNeeded() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        if(cfg == null) {
            Configuration tempConfig = new Configuration();

            Reader reader = new BufferedReader(new InputStreamReader(getClass()
                .getResourceAsStream("/friendly.properties")));
            
            friendlyProperties = new Properties();
            friendlyProperties.load(reader);

            debugMode = friendlyProperties.get("debug").toString().toLowerCase().equals("true");

            String webappLocation = friendlyProperties.getProperty("webapp.location");
            File ftlRoot = new File(webappLocation);
            if(ftlRoot == null)
                throw new RuntimeException("webapp.location not configured correctly. '" +
                        webappLocation + "' does not exist.");

            FileTemplateLoader loader = new FileTemplateLoader(ftlRoot);

            tempConfig.setTemplateLoader(loader);
            tempConfig.addAutoInclude("initialize.ftl");
            tempConfig.setTemplateUpdateDelay(Integer.parseInt(friendlyProperties.getProperty("template.update.delay")));

            Class routingClass = Class.forName(friendlyProperties.getProperty("routing.class"));
            routingHandler = (RoutingHandler)routingClass.newInstance();

            cfg = tempConfig;
        }
    }

    private String getTemplatePath(RoutingResult action, String viewName) {
        return "views/" + 
                action.getControllerBaseName().toLowerCase() + "/" +
                viewName + ".ftl";
    }

    private void processTemplate(Map viewData, String templatePath, OutputStreamWriter writer, Locale locale) throws Exception
    {
        Template template = cfg.getTemplate(templatePath, locale);
        template.process(viewData, writer);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "The Friendly Web Framework";
    }

    private ActionResult constructAndInvokeController(RoutingResult action,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        String controllerPackage = friendlyProperties.getProperty("controller.package");
        Class controllerClass = Class.forName(controllerPackage + "." + action.getControllerName());
        
        TinyContainer container = new TinyContainer(friendlyProperties);
        BaseController controller = (BaseController)container.getComponent(controllerClass);

        controller.setDefaultView(action.getActionName());
        controller.setHttpSession(request.getSession());
        controller.setResponse(response);
        controller.setRequest(request);
        
        ActionExecutor executor = new ActionExecutor(
                controllerClass, action.getActionName(), request, controller);

        try {
            ActionResult result = executor.invoke();
        
            if(result == null)
                return new ViewResult(controller.View);
            else
                return result;
        }
        finally {
            container.disposeAll();
        }
    }
}
