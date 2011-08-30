package dk.bestbrains.friendly.results;

public class JsonResult implements ActionResult {
    private final String value;

    public JsonResult(String json) {
        this.value = json;
    }

    public String getValue() {
        return value;
    }
}
