package org.apache.tapestry5.portlet;

import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.Test;

import com.thoughtworks.selenium.Wait;

public class TemplatingTest extends SeleniumTestCase {

	@Test
	public void testTemplatingWhenNormal() {

		open("/tapestry5-portlet/portal/Index");
		click("//a[@id='templating']");

		checkStrings("Normal", "Normal", "Normal :", "Message for Normal State");
	}

	@Test
	public void testTemplatingWhenMinimized() {
		open("/tapestry5-portlet/portal/Index");
		click("//a[@id='templating']");
		sleep(10000);
		click("//div[@id='portlets-right-column']//a[@title='Minimize']");

		checkStrings("Min", "Normal", "Minimized :",
				"Message for Minimized State");
	}

	@Test
	public void testTemplatingWhenMaximized() {
		open("/tapestry5-portlet/portal/Index");
		click("//a[@id='templating']");
		sleep(10000);
		click("//div[@id='portlets-right-column']//a[@title='Maximize']");

		checkStrings("Max", "Maximized", "Maximized :",
				"Message for Maximized State");
	}

	private void checkStrings(final String string, final String string2,
			final String string3, final String string4) {
		new Wait() {

			@Override
			public boolean until() {
				return isElementPresent("//li[@class='template'][contains(text(), '"
						+ string + "')]");
			}
		}.wait("The Template should be " + string, 5000l);

		new Wait() {

			@Override
			public boolean until() {
				return isElementPresent("//li[@class='template_bundle'][contains(text(), '"
						+ string2 + "')]");
			}
		}.wait("The Normal Bundle should be used for the page", 5000l);

		new Wait() {

			@Override
			public boolean until() {
				return isElementPresent("//li[@class='template_component'][contains(text(), '"
						+ string3 + "')]");
			}
		}.wait("The Normal Template should be used for the Component", 5000l);

		new Wait() {

			@Override
			public boolean until() {
				return isElementPresent("//li[@class='template_component'][contains(text(), '"
						+ string4 + "')]");
			}
		}.wait("The Normal Bundle should be used for the Component", 5000l);
	}
}
