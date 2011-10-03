package dk.bestbrains.friendly;

public class RoutingResult {

    private String controllerName;
    private String actionName;

    public RoutingResult(String controllerName, String actionName) {
        this.controllerName = controllerName;
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }

    public String getControllerName() {
        return controllerName;
    }

    public String getControllerBaseName() {
        return controllerName.replaceAll("Controller", "");
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
}
