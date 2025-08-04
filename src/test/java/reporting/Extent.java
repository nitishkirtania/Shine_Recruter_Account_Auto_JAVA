package reporting;

import java.io.File;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class Extent {



	public static ExtentReports generateReport(String programName) {

		String reportDir = System.getProperty("user.dir") + "/Reports/ExtentReport/";
		new File(reportDir).mkdir();
		
		// Fixed report file per program
        String reportFile = programName + ".html";
        String reportPath = reportDir + reportFile;
        
     // Reporter setup
        ExtentHtmlReporter reporter = new ExtentHtmlReporter(reportPath);
        reporter.config().setDocumentTitle(programName + " Automation Report");
        reporter.config().setReportName("Test Report for " + programName);
        reporter.config().setTheme(Theme.STANDARD);
        reporter.config().setTimeStampFormat("dd-MM-yyyy HH:mm");

     // Attach and return report
        ExtentReports reports = new ExtentReports();
        reports.attachReporter(reporter);
        return reports;
	}




}
