package dk.bestbrains.friendly;

public interface RoutingHandler {
    RoutingResult getRoute(String uri, ParameterMap requestParameters);
}
