package dk.bestbrains.friendly.test;

import dk.bestbrains.friendly.Disposable;

public class SampleClass4 implements Disposable {
    private boolean disposed;

    public SampleClass4() {
        disposed = false;
    }

    public boolean isDisposed() {
        return disposed;
    }

    @Override
    public void dispose() {
        disposed = true;
    }
}
