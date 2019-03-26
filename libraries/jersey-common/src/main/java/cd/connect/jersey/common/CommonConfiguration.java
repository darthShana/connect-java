package cd.connect.jersey.common;

import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.message.GZipEncoder;

import javax.ws.rs.core.Configurable;

import static org.glassfish.jersey.servlet.ServletProperties.PROVIDER_WEB_APP;

public class CommonConfiguration {

	public static void basic(Configurable<? extends Configurable> config) {
		config.property(CommonProperties.METAINF_SERVICES_LOOKUP_DISABLE, true);
		config.property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);
		config.property(CommonProperties.MOXY_JSON_FEATURE_DISABLE, true);

		config.property(PROVIDER_WEB_APP, false); // do not scan!

		config.register(JacksonFeature.class);
		config.register(MultiPartFeature.class);
		config.register(GZipEncoder.class);
		config.register(JacksonContextProvider.class);
		config.register(JerseyExceptionMapper.class);
	}
}