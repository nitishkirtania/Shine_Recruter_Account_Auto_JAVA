package shine_recruter;
import base.base;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;


public class ShineDotCom extends base {

    @Test(priority = 1)
    public void Shine_Advance_search() throws Exception {
        test = reports.createTest("Shine dot com Form");
        String username = "univoeducationpvtltd";
        String password = "Univo@2025Level";
       
        // File paths
        String inputFile = "/Users/glosys/Documents/requiter/src/test/java/Excel/phone_numbers.csv";
        String outputFile = "/Users/glosys/Documents/requiter/src/test/java/Excel/candidate_data.csv";
       
        // Initialize results list
        List<String[]> results = new ArrayList<>();
        results.add(new String[]{"Phone", "Full Name", "City", "Current_Company", "Prev_Company","Education_One", "Education_Two", "Education_Three","Expriance", "Salary", "Last Updated"});

        // Login process
        // driver.get("https://recruiter.shine.com/");
        // Thread.sleep(3000);
        
        WebElement recruterLogin = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[normalize-space()='Recruiter Login']")));
        js.executeScript("arguments[0].click();", recruterLogin);

        WebElement entername = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//input[@id='name'])[1]")));
        entername.sendKeys(username);
        
        Thread.sleep(2000);
        WebElement passwordenter = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//input[@id='password']")));
        passwordenter.sendKeys(password);

        WebElement login_button = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[normalize-space()='Login']")));
        js.executeScript("arguments[0].click();", login_button);
        Thread.sleep(5000);

        WebElement reset_session = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[normalize-space()='Click here to reset session-']")));
        js.executeScript("arguments[0].click();", reset_session);
        Thread.sleep(2000);
        test.log(Status.PASS, "Login successfully acount");
        // Read phone numbers from CSV
        List<String> phoneNumbers = readPhoneNumbers(inputFile);
        
        // Process each phone number
        for (String phone : phoneNumbers) {
            try {
                // Navigate to search page
                driver.get("https://recruiter.shine.com/advancedsearch/");
                Thread.sleep(2000);
                test.log(Status.PASS, "Advance search here");
                // Search for candidate
                WebElement anykey = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//input[@id='id_any_keyword']")));
                anykey.clear();
                anykey.sendKeys(phone);
                test.log(Status.PASS, "Mobile number : " + phone);
                Thread.sleep(2000);
                
                WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//button[normalize-space()='Search Candidates'])[1]")));
                new Actions(driver)
                    .moveToElement(searchButton)
                    .click()
                    .perform();
                Thread.sleep(5000);
                
                // Check if data was found
                try {
                    List<WebElement> noDataElements = driver.findElements(
                        By.xpath("//div[@class='noresultclass']"));
                    
                    if (!noDataElements.isEmpty()) {
                        System.out.println("No data found for: " + phone);
                        test.log(Status.PASS, "No data found for: " + phone);
                        results.add(new String[]{phone, "NOT FOUND", "", "", ""});
                        continue;
                    }
                    
                    // Extract candidate data
                    // WebElement data = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    //     By.xpath("(//div[contains(@id,'cnd_div_')])[1]")));
                    // System.out.println(data.getText());
                    // test.log(Status.PASS, "All data get here");
                    
                    String fullName = extractWithDefault(() -> 
                        wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("(//a[contains(@id,'cand_')])[1]"))).getText(),
                        "NOT AVAILABLE");
                    test.log(Status.PASS, "All data get here" + fullName);
                    
                    String city = extractWithDefault(() -> 
                        wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//div[contains(@id,'cnd_div_')]//ul[@class='cc']//li[3]"))).getText(),
                        "NOT AVAILABLE");
                    test.log(Status.PASS, "All data get here" + city);

                    String Current_company = extractWithDefault(() -> 
                        wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[@id=\"id_highlight_bold_1\"]/div/span[1]/span[1]"))).getText(),
                            //*[@id="id_highlight_bold_1"]/div/span[1]/span[1]
                        "NOT AVAILABLE");
                    test.log(Status.PASS, "All data get here" + Current_company);

                    String Prev_company = extractWithDefault(() -> 
                        wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[@id=\"id_highlight_bold_1\"]/div/span[1]/span[2]"))).getText(),
                            //*[@id="id_highlight_bold_1"]/div/span[1]/span[1]
                        "NOT AVAILABLE");
                    test.log(Status.PASS, "All data get here" + Prev_company);


                    
                    // String company = extractWithDefault(() -> 
                    //     wait.until(ExpectedConditions.visibilityOfElementLocated(
                    //         By.xpath("(//span[@class='iconWrap pcomp'])[1]"))).getText(),
                    //     "NOT AVAILABLE");
                    // test.log(Status.PASS, "All data get here" + company);
                    
                    // String education = extractWithDefault(() -> 
                    //     wait.until(ExpectedConditions.visibilityOfElementLocated(
                    //         By.xpath("(//span[@class='iconWrap educ'])[1]"))).getText(),
                    //     "NOT AVAILABLE");
                    // test.log(Status.PASS, "All data get here" + education);

                    String education_One = extractWithDefault(() -> 
                        wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("(//span[@class='iconWrap educ'])[1]//p[1]"))).getText(),
                        "NOT AVAILABLE");
                    test.log(Status.PASS, "All data get here" + education_One);

                    String education_Two = extractWithDefault(() -> 
                        wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("(//span[@class='iconWrap educ'])[1]//p[2]"))).getText(),
                        "NOT AVAILABLE");
                    test.log(Status.PASS, "All data get here" + education_Two);

                    String education_Three = extractWithDefault(() -> 
                        wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("(//span[@class='iconWrap educ'])[1]//p[3]"))).getText(),
                        "NOT AVAILABLE");
                    test.log(Status.PASS, "All data get here" + education_Three);

                    String expriance = extractWithDefault(() -> 
                        wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//div[@id='cnd_div_5980ad2beb64ef2f6b61c9e0']//ul[@class='cc']//li[1]"))).getText(),
                        "NOT AVAILABLE");
                    test.log(Status.PASS, "All data get here" + expriance);

                    String Salary = extractWithDefault(() -> 
                        wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//div[@id='cnd_div_5980ad2beb64ef2f6b61c9e0']//ul[@class='cc']//li[2]"))).getText(),
                        "NOT AVAILABLE");
                    test.log(Status.PASS, "All data get here" + Salary);

                    String updated_date = extractWithDefault(() -> 
                        wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//div[@class='list-content__right__bottom--active_date']//p[1]"))).getText(),
                        "NOT AVAILABLE");
                    test.log(Status.PASS, "All data get here" + updated_date);

                    Thread.sleep(5000);
                    
                    // Store results
                    // results.add(new String[]{phone, fullName, city, company, education});
                    // System.out.printf("Processed: %s | %s | %s | %s | %s%n", 
                    //     phone, fullName, city, company, education);
                    results.add(new String[]{
                                    phone, 
                                    fullName,
                                    city, 
                                    Current_company.replace("Current: ", ""),
                                    Prev_company.replace("Previous: ", ""),
                                    education_One,
                                    education_Two,
                                    education_Three,
                                    expriance,
                                    Salary,
                                    updated_date.replace("Updated: ", "")
                                });
                    
                } catch (Exception e) {
                    System.err.println("Error processing phone: " + phone);
                    results.add(new String[]{phone, "ERROR", "ERROR", "ERROR", "ERROR"});
                }
                
            } catch (Exception e) {
                System.err.println("Fatal error for phone: " + phone);
                results.add(new String[]{phone, "FATAL ERROR", "", "", ""});
            }
        }
        
        // Write results to CSV
        writeResultsToCSV(results, outputFile);
        Thread.sleep(3000);
    }

    // Helper method to read phone numbers from CSV
    private List<String> readPhoneNumbers(String filePath) throws IOException {
        List<String> numbers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                numbers.add(line.trim());
            }
        }
        return numbers;
    }

    // // Helper method to write results to CSV
    // private void writeResultsToCSV(List<String[]> data, String filePath) throws IOException {
    //     try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
    //     for (String[] row : data) {
    //         // Format each field
    //         String[] formattedRow = new String[row.length];
    //         for (int i = 0; i < row.length; i++) {
    //             if (row[i] == null) {
    //                 formattedRow[i] = "";
    //             } else {
    //                 // Escape quotes and preserve line breaks
    //                 formattedRow[i] = "\"" + row[i].replace("\"", "\"\"") + "\"";
    //             }
    //         }
    //         pw.println(String.join(",", formattedRow));
    //     }
    // }
    // }
    // Modified CSV writer method
    private void writeResultsToCSV(List<String[]> data, String filePath) throws IOException {
        boolean fileExists = new File(filePath).exists();
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, fileExists))) {
            // Write header only for new file
            if (!fileExists) {
                pw.println("\"Phone\",\"Full Name\",\"City\",\"Current_Company\",\"Prev_Company\","
                        + "\"Education_One\",\"Education_Two\",\"Education_Three\","
                        + "\"Expriance\",\"Salary\",\"Last Updated\"");
            }
            
            // Write data rows
            for (String[] row : data) {
                String[] formattedRow = new String[row.length];
                for (int i = 0; i < row.length; i++) {
                    String value = (row[i] == null) ? "" : row[i];
                    formattedRow[i] = "\"" + value.replace("\"", "\"\"") + "\"";
                }
                pw.println(String.join(",", formattedRow));
            }
        }
    }



    // Helper method for safe extraction with default value
    private String extractWithDefault(Supplier<String> extractor, String defaultValue) {
        try {
        String result = extractor.get();
        return result == null ? "" : result.trim();  // Return empty string for null
    } catch (Exception e) {
        return defaultValue;  // This will now be empty string when you pass ""
    }
    }


}
