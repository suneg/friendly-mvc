package com.company.web;

import dk.bestbrains.friendly.RoutingHandler;
import dk.bestbrains.friendly.RoutingResult;

public class Routing implements RoutingHandler {

    @Override
    public RoutingResult getRoute(String uri) {
        if (uri.startsWith("/")) {
            uri = uri.substring(1);
        }

        String[] parts = uri.split("/");

        String controller = "HomeController";
        String action = "index";

        if (parts.length > 0 && parts[0].length() > 0) {
            controller = parts[0] + "Controller";
        }

        if (parts.length > 1) {
            action = parts[1].replace(".", "_");
        }

        // IDEA:
        /*
         * request.setAttribute("id", part[2]);
         * ..to enable /booking/view/12352342 style url
         */

        // Example of aesthetic URLs
        if(parts[0].toLowerCase().equals("homepage"))
            controller = "HomeController";

        return new RoutingResult(controller, action);
    }
}
