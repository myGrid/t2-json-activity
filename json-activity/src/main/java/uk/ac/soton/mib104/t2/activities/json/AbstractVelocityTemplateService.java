/**
 * Copyright (C) 2013, University of Manchester and University of Southampton
 *
 * Licensed under the GNU Lesser General Public License v2.1
 * See the "LICENSE" file that is distributed with the source code for license terms. 
 */
package uk.ac.soton.mib104.t2.activities.json;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.log.Log4JLogChute;

/**
 * Abstract base class for Apache Velocity-based template services.
 * 
 * @author Mark Borkum
 * @version 0.0.1-SNAPSHOT
 *
 * @param <V>
 */
public abstract class AbstractVelocityTemplateService<V> implements TemplateService<Template, V> {
	
	/**
	 * Static initializer for Apache Velocity runtime system. 
	 * <p>
	 * This class is provided because, currently, it is not possible to create *anonymous*
	 * Apache Velocity templates without invoking the runtime system.  
	 * <p>
	 * Wrapping the initializer in a singleton class, ensures that it is invoked exactly once.  
	 * 
	 * @author Mark Borkum
	 * @version 0.0.1-SNAPSHOT
	 */
	private static final class VelocityInitializer {
		
		private static final class SingletonWrapper {
			
			private static VelocityInitializer INSTANCE;
			
			public static final VelocityInitializer getInstance() {
				if (INSTANCE == null) {
					INSTANCE = new VelocityInitializer();
				}
				
				return INSTANCE;
			}
			
		}
		
		public static final VelocityInitializer getInstance() {
			return SingletonWrapper.getInstance();
		}
		
		private VelocityInitializer() {
			super();
			
			final Properties p = new Properties();
			p.put("runtime.log.logsystem.class", Log4JLogChute.class.getName());
			p.put("runtime.log.logsystem.log4j.logger", logger.getName());
			Velocity.init(p);
		}
		
	}
	
	private static final Logger logger = Logger.getLogger(AbstractVelocityTemplateService.class);
	
	/**
	 * Returns new name based on the absolute path of a temporary file. 
	 * 
	 * @return  Temporary name.
	 * @throws Throwable
	 */
	private static final String createTempName() throws Throwable {
		final File tempFile = File.createTempFile("tmp", ".vm");
		
		final String name = tempFile.getAbsolutePath();
		
		tempFile.delete();
		
		return name;
	}
	
	/**
	 * Sole constructor.
	 */
	public AbstractVelocityTemplateService() {
		super();
		
		VelocityInitializer.getInstance();
	}

	@Override
	public final Template createTemplate(final String t) throws Throwable {
		// Apache Velocity appears to be designed primarily for the use case where the
		// same set of templates are merged repeatedly (for different bindings.) Unfortunately,
		// this means that we must take special measures for our non-standard use case.
		
		// Create a new name.
		final String name = createTempName();
        
		// Create an empty, uninitialized template.
        final Template template = new Template();
        
        // Obtain the runtime services bean (at runtime--rather than at compile time--to ensure that Velocity has been initialized.)
        final RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
        
        template.setData(runtimeServices.parse(new StringReader((t == null) ? "" : t), name));
        template.setName(name);
        template.setRuntimeServices(runtimeServices);
        
        // Here be dragons!  This method registers the template with the Velocity runtime. 
        template.initDocument();
        
        return template;
	}

	@Override
	public final V mergeTemplate(final String t, final Map<String, Object> bindings) throws Throwable {
		return this.mergeTemplate(this.createTemplate(t), bindings);
	}

	@Override
	public final V mergeTemplate(final Template template, final Map<String, Object> bindings) throws Throwable {
		// Create a new context (an implementation of Map). 
		final VelocityContext context = new VelocityContext();
		
		if (bindings != null) {
			for (final Map.Entry<String, Object> entry : bindings.entrySet()) {
				// Proxy the value of each entry before it is added to the context. 
				context.put(entry.getKey(), this.proxyInput(entry.getValue()));
			}
		}
		
		final StringWriter sw = new StringWriter();
		
		template.merge(context, sw);
		
		final String t = sw.toString();
		
		// Proxy the result.
		return this.proxyOutput(t);
	}
	
	/**
	 * Proxy the given object <code>obj</code>. 
	 * 
	 * @param obj 
	 * @return 
	 * @throws Throwable
	 */
	protected abstract Object proxyInput(Object obj) throws Throwable;
	
	/**
	 * Proxy the given string <code>t</code>. 
	 * 
	 * @param t
	 * @return 
	 * @throws Throwable
	 */
	protected abstract V proxyOutput(String t) throws Throwable;

}
