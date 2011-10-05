package com.company.web.controllers;

import com.company.web.models.User;
import dk.bestbrains.friendly.BaseController;
import dk.bestbrains.friendly.ParameterOrder;
import dk.bestbrains.friendly.results.JsonResult;
import dk.bestbrains.friendly.results.XmlResult;

public class UserController extends BaseController {

    public void index() {
    
    }

    @ParameterOrder("id")
    public void details(int id) {
        User user = loadUser(id);
        View.put("user", user);
    }

    @ParameterOrder("id")
    public JsonResult details_json(int id) {
        User user = loadUser(id);
        return toJson(user);
    }

    @ParameterOrder("id")
    public XmlResult details_xml(int id) {
        User user = loadUser(id);
        return toXml(user);
    }

    private User loadUser(int id) {
        return User.construct(id, "Sune Gynthersen", "BestBrains", 30);
    }
}
