package dk.bestbrains.friendly;

import dk.bestbrains.friendly.TinyContainer;
import java.util.Properties;
import dk.bestbrains.friendly.test.SampleClass;
import dk.bestbrains.friendly.test.SampleClass2;
import dk.bestbrains.friendly.test.SampleClass3;
import dk.bestbrains.friendly.test.SampleClass4;
import dk.bestbrains.friendly.test.SampleInterface;
import dk.bestbrains.friendly.test.SampleInterface2;
import dk.bestbrains.friendly.test.SampleInterface3;
import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;

public class TestTinyContainer {

    private Properties properties;
    private TinyContainer container;

    @Before
    public void setup() {
        properties = new Properties();
        properties.setProperty("dk.bestbrains.friendly.test.SampleInterface", "dk.bestbrains.friendly.test.SampleClass");
        properties.setProperty("dk.bestbrains.friendly.test.SampleInterface2", "dk.bestbrains.friendly.test.SampleClass2");
        properties.setProperty("dk.bestbrains.friendly.test.SampleInterface3", "dk.bestbrains.friendly.test.SampleClass3");

        properties.setProperty("dk.bestbrains.friendly.test.SampleClass", "dk.bestbrains.friendly.test.SampleClass");
        container = new TinyContainer(properties);
    }
    
    @Test
    public void instantiateClass() throws Exception {
        
        SampleInterface sample = (SampleInterface) container.getComponent(SampleInterface.class);
        Assert.assertEquals(SampleClass.class, sample.getClass());
    }

    @Test
    public void instantiateClassWithDependency() throws Exception {

        SampleInterface2 sample = (SampleInterface2) container.getComponent(SampleInterface2.class);
        Assert.assertEquals(SampleClass2.class, sample.getClass());
    }

    @Test
    public void instantiateClassWithoutInterface() throws Exception {

        SampleClass sample = (SampleClass) container.getComponent(SampleClass.class);
        Assert.assertEquals(SampleClass.class, sample.getClass());
    }

    @Test
    public void instantiateClassWithoutInterfaceByConventionOverRegistration() throws Exception {

        SampleClass3 sample = (SampleClass3) container.getComponent(SampleClass3.class);
        Assert.assertEquals(SampleClass3.class, sample.getClass());
    }

    @Test
    public void instantiateClassWithMultipleDependencies() throws Exception {

        SampleClass3 sample = (SampleClass3)container.getComponent(SampleInterface3.class);
        Assert.assertEquals(SampleClass3.class, sample.getClass());
        Assert.assertNotNull(sample.getDependency1());
        Assert.assertNotNull(sample.getDependency2());
    }

    @Test(expected=InstantiationException.class)
    public void instantiateClassWithMissingComponentRegistration() throws Exception {
        properties.remove("dk.bestbrains.friendly.test.SampleInterface2");
        
        SampleInterface2 sample = (SampleInterface2) container.getComponent(SampleInterface2.class);
    }

    @Test
    public void testAutomaticObjectDisposal() throws Exception {
        SampleClass4 sample = (SampleClass4) container.getComponent(SampleClass4.class);
        container.disposeAll();

        Assert.assertTrue(sample.isDisposed());
    }
}
