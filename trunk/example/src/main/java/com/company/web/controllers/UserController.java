package com.company.web.controllers;

import dk.bestbrains.friendly.BaseController;
import dk.bestbrains.friendly.ParameterOrder;
import dk.bestbrains.friendly.results.JsonResult;

public class UserController extends BaseController {

    public void all() {
        
    }

    public void index() {
        
    }

    @ParameterOrder("id")
    public JsonResult details_json(String id) {
        return toJson("hello world details with id " + id);
    }

    @ParameterOrder("id")
    public void details(int id) {
        View.put("id", id);
    }
}
