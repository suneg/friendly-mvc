package com.company.web;

import dk.bestbrains.friendly.ParameterMap;
import dk.bestbrains.friendly.RoutingHandler;
import dk.bestbrains.friendly.RoutingResult;

public class Routing implements RoutingHandler {

    @Override
    public RoutingResult getRoute(String uri, ParameterMap requestParameters) {
        if (uri.startsWith("/")) {
            uri = uri.substring(1);
        }

        String[] parts = uri.split("/");

        String controller = "HomeController";
        String action = "index";

        if (parts.length > 0 && parts[0].length() > 0) {
            controller = parts[0] + "Controller";
        }

        // Enable http://www.hostname.com/users/479/details url
        if(parts[0].toLowerCase().equals("users")) {
            if(isNumeric(parts[1])) {
                controller = "UserController";
                action = parts[2];
                requestParameters.setAttribute("id", parts[1]);
            }
        }

        // Allow http://www.hostname.com/Home/index.json to be translated to
        // HomeController.index_json() java method
        action = action.replace(".", "_");

        
        if(parts[0].toLowerCase().equals("homepage"))
            controller = "HomeController";

        return new RoutingResult(controller, action);
    }

    public boolean isNumeric(String number) {
        try {
            toInteger(number);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    public int toInteger(String number) {
        return Integer.parseInt(number);
    }
}
