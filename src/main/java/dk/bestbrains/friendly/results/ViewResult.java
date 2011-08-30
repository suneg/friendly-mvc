package dk.bestbrains.friendly.results;

import java.util.Map;

public class ViewResult implements ActionResult {
    private final Map viewData;

    public ViewResult(Map viewData) {
        this.viewData = viewData;
    }

    public Map getViewData() {
        return viewData;
    }
}
