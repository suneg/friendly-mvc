package dk.bestbrains.friendly.test;

public class SampleClass3 implements SampleInterface3 {
    private final SampleInterface2 parameter1;
    private final SampleInterface parameter2;

    public SampleClass3(SampleInterface2 parameter1, SampleInterface parameter2) {
        this.parameter1 = parameter1;
        this.parameter2 = parameter2;
    }

    public SampleInterface2 getDependency1() {
        return parameter1;
    }

    public SampleInterface getDependency2() {
        return parameter2;
    }
}
