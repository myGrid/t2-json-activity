/**
 * Copyright (C) 2013, University of Manchester and University of Southampton
 *
 * Licensed under the GNU Lesser General Public License v2.1
 * See the "LICENSE" file that is distributed with the source code for license terms. 
 */
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
