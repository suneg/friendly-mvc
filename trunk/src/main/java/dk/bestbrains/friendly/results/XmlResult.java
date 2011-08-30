package dk.bestbrains.friendly.results;

public class XmlResult implements ActionResult {
    private final String value;

    public XmlResult(String xml) {
        this.value = xml;
    }

    public String getValue() {
        return value;
    }
}
