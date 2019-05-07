package org.jboss.resteasy.plugins.server.resourcefactory;

import org.jboss.resteasy.resteasy_jaxrs.i18n.Messages;
import org.jboss.resteasy.spi.ConstructorInjector;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.HttpResponse;
import org.jboss.resteasy.spi.PropertyInjector;
import org.jboss.resteasy.spi.ResourceFactory;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.spi.metadata.ResourceBuilder;
import org.jboss.resteasy.spi.metadata.ResourceClass;
import org.jboss.resteasy.spi.metadata.ResourceConstructor;

/**
 * Allocates an instance of a class at each invocation
 *
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class POJOResourceFactory implements ResourceFactory
{
   private final ResourceBuilder resourceBuilder;
   private final Class<?> scannableClass;
   private final ResourceClass resourceClass;
   private ConstructorInjector constructorInjector;
   private PropertyInjector propertyInjector;

   @Deprecated
   public POJOResourceFactory(final Class<?> scannableClass)
   {
      this(new ResourceBuilder(), scannableClass);
   }

   public POJOResourceFactory(final ResourceBuilder resourceBuilder, final Class<?> scannableClass)
   {
      this.resourceBuilder = resourceBuilder;
      this.scannableClass = scannableClass;
      this.resourceClass = resourceBuilder.getRootResourceFromAnnotations(scannableClass);
   }

   public POJOResourceFactory(final ResourceClass resourceClass)
   {
      this(new ResourceBuilder(), resourceClass);
   }

   public POJOResourceFactory(final ResourceBuilder resourceBuilder, final ResourceClass resourceClass)
   {
      this.resourceBuilder = resourceBuilder;
      this.scannableClass = resourceClass.getClazz();
      this.resourceClass = resourceClass;
   }

   public void registered(ResteasyProviderFactory factory)
   {
      ResourceConstructor constructor = resourceClass.getConstructor();
      if (constructor == null) constructor = resourceBuilder.getConstructor(resourceClass.getClazz());
      if (constructor == null)
      {
         throw new RuntimeException(Messages.MESSAGES.unableToFindPublicConstructorForClass(scannableClass.getName()));
      }
      this.constructorInjector = factory.getInjectorFactory().createConstructor(constructor, factory);
      this.propertyInjector = factory.getInjectorFactory().createPropertyInjector(resourceClass, factory);
   }

   public Object createResource(HttpRequest request, HttpResponse response, ResteasyProviderFactory factory)
   {
      Object obj = constructorInjector.construct(request, response);
      propertyInjector.inject(request, response, obj);
      return obj;
   }

   public void unregistered()
   {
   }

   public Class<?> getScannableClass()
   {
      return scannableClass;
   }


   public void requestFinished(HttpRequest request, HttpResponse response, Object resource)
   {
   }
}