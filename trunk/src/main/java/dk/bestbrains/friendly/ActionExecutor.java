package dk.bestbrains.friendly;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import dk.bestbrains.friendly.results.ActionResult;

public class ActionExecutor {
    private final Class controllerClass;
    private final String actionName;
    private final HttpServletRequest request;
    private final BaseController controller;

    public ActionExecutor(Class controllerClass, String actionName, HttpServletRequest request, BaseController controller) {
        this.controllerClass = controllerClass;
        this.actionName = actionName;
        this.request = request;
        this.controller = controller;
    }

    public ActionResult invoke() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception {

        for (Method method : controllerClass.getMethods()) {
            if (method.getName().equals(actionName)) {
                if (method.getAnnotation(ParameterOrder.class) != null) {
                    String parameterOrder = method.getAnnotation(ParameterOrder.class).value();
                    Class[] parameterTypes = method.getParameterTypes();
                    String[] parameterNames = parameterOrder.split(",");
                    Object[] parameterValues = new Object[parameterNames.length];

                    if(parameterNames.length != parameterTypes.length)
                        throw new FrameworkException(String.format("Action '%s' on controller '%s' has %s parameter(s) " +
                                "defined in method signature but @ParameterOrder annotation " +
                                "mentions %s parameter(s).",
                                actionName,
                                controllerClass.getName(),
                                parameterTypes.length,
                                parameterNames.length));


                    for (int i = 0; i < parameterNames.length; i++) {
                        String parameterName = parameterNames[i].trim();
                        if (parameterTypes[i].equals(String.class)) {
                            Object value = request.getParameter(parameterName);

                            if(value == null)
                                value = request.getAttribute(parameterName);

                            parameterValues[i] = value;
                        }
                        if (parameterTypes[i].isArray()) {
                            parameterValues[i] = request.getParameterValues(parameterName);
                        }
                        if (parameterTypes[i].equals(int.class)) {
                            String value = request.getParameter(parameterName);

                            if(value == null)
                                value = (String)request.getAttribute(parameterName);

                            if(value == null)
                                parameterValues[i] = 0;
                            else
                                parameterValues[i] = value.length() > 0 ? Integer.parseInt(value) : 0;
                        }
                    }
                    controller.preAction();
                    ActionResult result = (ActionResult)method.invoke(controller, parameterValues);
                    controller.postAction();
                    return result;
                } else {
                    int parameterCount = method.getParameterTypes().length;
                    if(parameterCount != 0)
                        throw new FrameworkException(
                                String.format("Action '%s' on controller '%s' has %s parameter(s) " +
                                "defined in method signature without having a @ParameterOrder " +
                                "annotation",
                                actionName,
                                controllerClass.getName(),
                                parameterCount));

                    controller.preAction();
                    ActionResult result = (ActionResult)method.invoke(controller, null);
                    controller.postAction();

                    return result;
                }
            }
        }

        throw new FrameworkException(
                String.format("Could not find method for action '%s' on controller '%s'",
                    actionName,
                    controllerClass.getName()));
    }

}
