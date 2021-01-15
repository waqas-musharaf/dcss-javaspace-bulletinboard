package utilities;

import net.jini.space.JavaSpace05;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.core.discovery.LookupLocator;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;

/**
 * The SpaceUtils class was used with permission of Dr Gary Allen.
 * Modified by Waqas Musharaf to utilise JavaSpace05.
 * 
 * @author Gary Allen
 *
 */
public class SpaceUtils {

	public static JavaSpace05 getSpace(String hostname) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		JavaSpace05 js = null;
		try {
			LookupLocator l = new LookupLocator("jini://" + hostname);

			ServiceRegistrar sr = l.getRegistrar();

			Class c = Class.forName("net.jini.space.JavaSpace");
			Class[] classTemplate = { c };

			js = (JavaSpace05) sr.lookup(new ServiceTemplate(null, classTemplate, null));

		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
		return js;
	}

	public static JavaSpace05 getSpace() {
		return getSpace("localhost");
	}

	public static TransactionManager getManager(String hostname) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		TransactionManager tm = null;
		try {
			LookupLocator l = new LookupLocator("jini://" + hostname);

			ServiceRegistrar sr = l.getRegistrar();

			Class c = Class.forName("net.jini.core.transaction.server.TransactionManager");
			Class[] classTemplate = { c };

			tm = (TransactionManager) sr.lookup(new ServiceTemplate(null, classTemplate, null));

		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
		return tm;
	}

	public static TransactionManager getManager() {
		return getManager("localhost");
	}
}