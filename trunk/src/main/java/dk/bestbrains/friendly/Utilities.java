package dk.bestbrains.friendly;

public class Utilities {
    public String getRandom() {
        return new RandomString(16).nextString();
    }
}
