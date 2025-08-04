package screenshot;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenShots {

	public static String takeScreenshot(WebDriver driver, String testname) throws IOException {
		 try {
			 File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		        String destDir = System.getProperty("user.dir") + "/Reports/Screenshot";
		        File destFolder = new File(destDir);
		        if (!destFolder.exists()) destFolder.mkdirs();

		        String destPath = destDir + "/" + testname + ".png";
		        File dest = new File(destPath);
		        FileUtils.copyFile(src, dest);

		        return dest.getAbsolutePath(); // ✅ return full path
		    } catch (IOException e) {
		        e.printStackTrace();
		        return null;
		    }
	}

}
