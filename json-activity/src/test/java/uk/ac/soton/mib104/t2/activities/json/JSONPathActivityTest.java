package uk.ac.soton.mib104.t2.activities.json;

import org.junit.Before;
import org.junit.Test;

public class JSONPathActivityTest {

	private final JSONPathActivity activity = new JSONPathActivity();

	private JSONPathActivityConfigurationBean configBean;

	@Test
	public void executeAsynch() throws Exception {
		activity.configure(configBean);

		// TODO Auto-generated method stub
		return;
	}

	@Before
	public void makeConfigBean() throws Exception {
		configBean = new JSONPathActivityConfigurationBean();
	}

}
