package com.company.web.controllers;

import java.io.IOException;
import dk.bestbrains.friendly.BaseController;
import dk.bestbrains.friendly.ParameterOrder;

public class HomeController extends BaseController {
    public void index() {
    }

    @ParameterOrder("parm1,parm2")
    public void parameters(String parm1, String parm2) {
        View.put("parm1", parm1);
        View.put("parm2", parm2);

        render("index");
    }

    @ParameterOrder("args")
    public void multi(String[] args) {
        View.put("parm1", args[0]);
        View.put("parm2", args[1]);

        render("index");
    }

    public void safe() throws IOException {
        String authorization = getRequest().getHeader("Authorization");
        
        if("".equals(authorization) || authorization == null) {
            getResponse().setHeader("WWW-Authenticate", "Basic realm=\"Logen Test Servlet Users\"");
            getResponse().sendError(401);
        }
        else {
            View.put("authorization", authorization);
            
            render("index");
        }
    }
}
