package dk.bestbrains.friendly;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class TinyContainer {
    private final Properties configuration;
    private List<Disposable> disposables;

    public TinyContainer(Properties configuration) {
        this.configuration = configuration;
        disposables = new LinkedList<Disposable>();
    }

    public Object getComponent(Class aClass) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {

        String concreteClassName = configuration.getProperty(aClass.getName());        

        if(concreteClassName == null && aClass.isInterface())
            throw new InstantiationException(
                    String.format("Missing container registration. '" + aClass.getName()
                    + "' does not have a concrete implementation registered"));

        if(concreteClassName == null && !aClass.isInterface())
            concreteClassName = aClass.getName();

        Class concreteClass = Class.forName(concreteClassName);

        if(concreteClass.getConstructors().length > 1)
            throw new RuntimeException(String.format("Controller '%s' has %s constructors. Only one allowed",
                    concreteClass.getName(),
                    concreteClass.getConstructors().length));

        if(concreteClass.getConstructors().length == 0)
            return concreteClass.newInstance();

        Constructor constructor = concreteClass.getConstructors()[0];
        
        if(constructor.getParameterTypes().length > 0) {
            List<Object> dependencies = new LinkedList<Object>();
            
            for(Class parameterType : constructor.getParameterTypes()) {
                Object dependency = getComponent(parameterType);
                dependencies.add(dependency);
            }

            Object result = constructor.newInstance(dependencies.toArray());

            if(isDisposable(result.getClass()))
                disposables.add((Disposable)result);

            return result;
        }
        else {
            Object result = concreteClass.newInstance();

            if(isDisposable(result.getClass()))
                disposables.add((Disposable)result);

            return result;
        }
    }

    public void disposeAll() {
        for(Disposable disposable : disposables)
            disposable.dispose();
    }

    private boolean isDisposable(Class result) {
        if(result.equals(Object.class))
            return false;
        
        if(isDisposable(result.getSuperclass()))
            return true;

        for(Class _interface : result.getInterfaces()) {
            if(_interface.equals(Disposable.class))
                return true;
        }

        return false;
    }
}
