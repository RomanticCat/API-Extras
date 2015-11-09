package nz.ac.waikato.its.dspace.reporting;

import nz.ac.waikato.its.dspace.reporting.configuration.ConfigurationException;
import nz.ac.waikato.its.dspace.reporting.configuration.Field;
import nz.ac.waikato.its.dspace.reporting.configuration.Report;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * @author Andrea Schweer schweer@waikato.ac.nz for AgResearch
 */
public class ReportGeneratorTest {
	@Test
	public void testGetCannedReportConfig() {
		Report config = null;
		try {
			String configDir = TestUtils.getTestConfigDir();
			config = new ReportConfigurationService(configDir).getCannedReportConfiguration("report1");
		} catch (ConfigurationException e) {
			e.printStackTrace();
			Assert.fail("Caught exception getting canned report config");
		}
		Assert.assertNotNull("Got non-null configuration object", config);
		Assert.assertEquals("Id matches", "report1", config.getId());
	}

	@Test
	public void testQueryResultsToFile() {
		Report config = null;
		try {
			String configDir = TestUtils.getTestConfigDir();
			config = new ReportConfigurationService(configDir).getCannedReportConfiguration("report1");
		} catch (ConfigurationException e) {
			e.printStackTrace();
			Assert.fail("Caught exception getting canned report config");
		}

		Assert.assertNotNull("Got non-null configuration object", config);
		
		InputStream results;
		try {
			results = ReportGenerator.queryResultsToFile(config, TestUtils.getTestSolrServer(), null, null, null);
			Scanner scanner = new Scanner(results);
			Assert.assertTrue(scanner.hasNextLine());
			String firstLine = scanner.nextLine();
			Assert.assertEquals("Header line is as expected", "\"AgResearch_Group\",\"AgResearch_Team\",\"Output_Type\",\"Output_Subtype\",\"Title\",\"Date_Submitted\",\"Citation\",\"AgScite_Handle\"", firstLine);
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
			Assert.fail("Caught exception trying to make query / write results to file");
		}
		
		
	}

}
