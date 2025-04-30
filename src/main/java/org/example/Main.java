package org.example;

import com.opencsv.CSVWriter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {

        // If the chrome driver is not opening in macos, then use this command in terminal: $ xattr -d com.apple.quarantine chromedriver
        System.setProperty("webdriver.chrome.driver","/Users/saimakhatoon/Documents/Softwares/chromedriver-mac-arm64-new/chromedriver");

        // Create new instance of the ChromeDriver
        WebDriver driver = new ChromeDriver();

        // Maximize the chrome window
        driver.manage().window().maximize();

        // Open the below URL in Chrome
        driver.get("https://www.verizon.com/");

        System.out.println("*******************");
        System.out.print("Hello from Verizon!\n");
        System.out.println("*******************");

        // Instantiating the WebdriverWait class
        WebDriverWait waitObject = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Create the object of action class
        Actions actionObject = new Actions(driver);

        workingWithMobileMenuLink(driver);

        //workingWithSearchBar(driver, waitObject, actionObject);

        //workingWithModal(driver, waitObject, actionObject);

       // Thread.sleep(1000);

        //scrapingMobilePlanDetails(driver);

        scrapingMobilePlanFormattedDetails(driver);

        //scrapingMobilePhones(driver);

        //Thread.sleep(10000);

        // Quit the driver
        driver.quit();
    }

    public static void scrapingMobilePlanFormattedDetails(WebDriver driver) throws IOException {

        // Declare the FileWriter and CSVWriter outside try block for scope
        FileWriter outputfile =null;
        CSVWriter planWrite =null;

       try {
           // Writing mobile plan details to the csv file
           File planFile = new File("Verizon_MobilePlans.csv");

           // Create FileWriter object with file as parameter
           outputfile = new FileWriter(planFile);

           // Create CSVWriter object filewriter object as parameter
            planWrite = new CSVWriter(outputfile);
       }
       catch (IOException e) {
           // Catch any IOExceptions during file writing
           System.out.println("Error while handling the file: " + e.getMessage());
           e.printStackTrace();  // Optional: print the stack trace for more detailed debugging information

       }

        // Adding header to csv
        String[] planHeader = { "Plan Name", "Plan Price", "Talk Time", "Text Time", "Duration", "Data", "Speed", "Talktime - Countries", "Texting- Countries", "Discount", "Mobile Hotspot", "Streaming Quality","Roaming", "Verizon Family Feature"};
        assert planWrite != null;
        planWrite.writeNext(planHeader);

        System.out.println("Mobile plan details: ");

        try {
            // Plan Name
            List<WebElement> planNameList = driver.findElements(By.cssSelector(".plan-header-section.u-fontDisplay.u-text--xs16.u-text--md32.plan-name.u-marginBottom--0"));

            // Plan Price
            List<WebElement> planPriceList = driver.findElements(By.cssSelector(".u-text--16.u-text--md20.plan-price.myplanPrice1.planDiscountPriceParent"));

            // Auto Pay
            List<WebElement> autoPayList = driver.findElements(By.cssSelector(".u-text--12.u-textRegular.u-marginTop--8.legal"));

            // Talktime & Texttime
            List<WebElement> talktimeList = driver.findElements(By.xpath("//*[@id=\"myplans-container\"]/div/div/div/table[2]/tbody/tr[23]"));
            WebElement talktimeRow = talktimeList.get(0);
            List<WebElement> columnsTalktime = talktimeRow.findElements(By.cssSelector("td.sectionheader-hidden-mobile"));
            //System.out.println(columnsTalktime.size());

            // Speed
            List<WebElement> speedList = driver.findElements(By.cssSelector("tr.planTableRow.tableRowHeader0"));
            WebElement speedRow = speedList.get(1);
            List<WebElement> columnsSpeed = speedRow.findElements(By.cssSelector("td.sectionheader-hidden-mobile"));

            // Countries
            List<WebElement> countriesList = driver.findElements(By.cssSelector("tr.planTableRow.tableRowHeader6"));  // Replace 'some-class' with the actual class
            WebElement countriesRow = countriesList.get(1);
            List<WebElement> columnsCountries = countriesRow.findElements(By.cssSelector("td.sectionheader-hidden-mobile"));

            // Hotspot
            List<WebElement> hotspotList = driver.findElements(By.xpath("//*[@id=\"myplans-container\"]/div/div/div/table[2]/tbody/tr[10]"));
            WebElement hotspotRow = hotspotList.get(0);
            List<WebElement> columnsHotspot = hotspotRow.findElements(By.cssSelector("td.sectionheader-hidden-mobile"));


            // Streaming
            List<WebElement> streamingList = driver.findElements(By.xpath("//*[@id=\"myplans-container\"]/div/div/div/table[2]/tbody/tr[42]"));
            WebElement streamingRow = streamingList.get(0);
            List<WebElement> columnsStreaming = streamingRow.findElements(By.cssSelector("td.sectionheader-hidden-mobile"));

            // Roaming
            List<WebElement> roamingList = driver.findElements(By.xpath("//*[@id=\"myplans-container\"]/div/div/div/table[2]/tbody/tr[23]"));
            WebElement roamingRow = roamingList.get(0);
            List<WebElement> columnsRoaming = roamingRow.findElements(By.cssSelector("td.sectionheader-hidden-mobile"));

            // Family feature
            List<WebElement> familyList = driver.findElements(By.xpath("//*[@id=\"myplans-container\"]/div/div/div/table[2]/tbody/tr[18]"));
            WebElement familyRow = familyList.get(0);
            List<WebElement> columnsFamily = familyRow.findElements(By.cssSelector("td.sectionheader-hidden-mobile"));


            for (int i = 0; i < columnsTalktime.size(); i++) {
                String[] data = new String[14];

                // Plan name
                String planName = planNameList.get(i).getAttribute("innerText");
                planName = planName.trim();
                System.out.println("Plan Name: " + planName);

                // Plan Price
                String planPrice = planPriceList.get(i).getAttribute("innerText");
                planPrice = planPrice.trim();
                planPrice = planPrice.substring(0, 3);
                System.out.println("Plan Price: " + planPrice);

                // Talktime & Texttime
                String talktime = "";
                if (columnsTalktime.isEmpty())
                    System.out.println("No columns found in the talktime row.");
                else {
                    talktime = columnsTalktime.get(i).getAttribute("innerText").trim();
                    Pattern patternTalktime = Pattern.compile("\\b\\w+\\b");
                    Matcher matcherTalktime = patternTalktime.matcher(talktime);

                    if (!matcherTalktime.find()) System.out.println("No match found.");
                    talktime = matcherTalktime.group();
                    System.out.println("Talktime : " + talktime);
                    System.out.println("TextTime : " + talktime);
                }

                // Speed
                String speed = "";
                if (columnsSpeed.isEmpty())
                    System.out.println("No columns found in the data speed row.");
                else {
                    speed = columnsSpeed.get(i).getAttribute("innerText").trim();
                    System.out.println("Speed: " + speed);
                }

                // Countries
                String country = "";
                if (columnsCountries.isEmpty())
                    System.out.println("No columns found in the countries row.");
                else {
                    WebElement columnCountries = columnsCountries.get(i);
                    country = columnCountries.getAttribute("innerText").trim();
                    country = "US, " + country;
                    System.out.println("Text and Talk Countries: " + country);
                }

                // Autopay discount
                String autopay = "";
                if (autoPayList.isEmpty())
                    System.out.println("No columns found in the auto pay.");
                else {
                    WebElement autoPayColumns = autoPayList.get(0);
                    autopay = autoPayColumns.getAttribute("innerText").trim();
                    if (autopay.contains("Auto Pay")) {
                        System.out.println("Discount: $10/mo Auto Pay discount");
                        autopay = "$10/mo Auto Pay discount";
                    }
                }

                // Hotspot
                String hotspot = "";
                if (columnsHotspot.isEmpty())
                    System.out.println("No columns found in the hotspot row.");
                else {
                    WebElement columnHotspot = columnsHotspot.get(i);
                     hotspot = Objects.requireNonNull(columnHotspot.getAttribute("innerText")).trim();
                    if (hotspot.contains("Empty")) {
                        hotspot = "-";
                    }
                    System.out.println("Hotspot Data: " + hotspot);
                }

                // Streaming
                String streaming = "";
                if (columnsStreaming.isEmpty())
                    System.out.println("No columns found in the data streaming row.");
                else {
                    WebElement columnStreaming = columnsStreaming.get(i);
                    streaming = columnStreaming.getAttribute("innerText").trim();
                    System.out.println("Streaming Quality: " + streaming);
                }

                //Roaming
                String roaming = "";
                if (columnsRoaming.isEmpty())
                    System.out.println("No data found for roaming.");
                else {
                    WebElement columnRoaming = columnsRoaming.get(i);
                    roaming = columnRoaming.getAttribute("innerText").trim();
                    Pattern pattern = Pattern.compile("[\\w& ]+?\\.");
                    Matcher matcher = pattern.matcher(roaming);
                    if (!matcher.find()) System.out.println("No match found.");
                    roaming = matcher.group();
                    System.out.println("Roaming Benefits: " + roaming);
                }

                // Family Feature
                String family = "";
                if (columnsFamily.isEmpty())
                    System.out.println("No columns found in the family feature row.");
                else {
                    WebElement columnFamily = columnsFamily.get(i);
                    family = columnFamily.getAttribute("innerText").trim();
                    System.out.println("Family feature: " + family);
                }

                // Writing data in file
                data[0] = planName;
                data[1] = planPrice;
                data[2] = talktime; //talktime
                data[3] = talktime; //texttime
                data[4] = "1month"; //duration
                if (i == 2) data[5] = "0";
                else data[5] = "unlimited";
                // data[6] = speed; //Speed
                data[7] = country; //Countries
                data[8] = country; //Countries
                data[9] = autopay; // discount
                data[10] = hotspot;      //hotspot
                data[11] = streaming;      //streaming quality
                data[12] = roaming;      // Roaming
                data[13] = family;      // family feature
                planWrite.writeNext(data);

                System.out.println();
            }
        }
        catch (NoSuchElementException e) {
            System.out.println("Element not found: " + e.getMessage());
            System.out.println("Please check the XPath or CSS selector you are using.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            outputfile.close();
        }

    }

    // To find the mobile menu anchor tag element and click on it to redirect to the mobile plans page
    public static void workingWithMobileMenuLink(WebDriver driver)
    {
        // Find the mobile menu link and click on it
        WebElement mobileLink =driver.findElement(By.cssSelector("div.gnav20-primary-menu.gnav20-featured-card a"));
        mobileLink.click();
    }

    // To find the deals menu button and click on it to redirect to the deals page.
    public static void workingWithDealsMenuLink(WebDriver driver)
    {
        // Find the deals menu link and click on it
        WebElement dealsButton1 = driver.findElement(By.cssSelector("div.gnav20-primary-menu.gnav20-featured-card a[data-track=\"global nav:deals\"]"));
        dealsButton1.click();
    }

    // This function is used to find the Apple button on the deals page and click on it.
    public static void workingWithButton(WebDriver driver)
    {
        // Find the Apple button and click on it
        WebElement iphones = driver.findElement(By.cssSelector("a[href=\"/smartphones/apple/\"]"));
        iphones.click();
    }

    // This function contains all the steps that are required for searching keywords on the search bar.
    public static void workingWithSearchBar(WebDriver driver, WebDriverWait waitObject, Actions actionObject)
    {
        // Find the search button and click on it
        WebElement searchButton = waitObject.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.gnav20-search-wrapper button")));
        searchButton.click();

        // Wait for the input textbox to be visible and then send the keys to the search bar in the input field
        WebElement alert = waitObject.until(ExpectedConditions.visibilityOfElementLocated(By.id("search_box_gnav_input")));
        alert.sendKeys("mobile");

        // Perform the enter operation on the text written in textbox using action class
        WebElement searchButton1= waitObject.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"search_box_gnav_suggestions\"]/ul/div[1]/div[2]/div/div/div[3]/li[1]/a")));
        actionObject.moveToElement(searchButton1).build().perform();

        // Wait for the search result and then click on the first search result
        WebElement firstSearchClick = waitObject.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.listContainer a")));
        firstSearchClick.click();
    }

    // This function is used to work with modal elements. Find the modal element, open and close it.
    public static void workingWithModal(WebDriver driver, WebDriverWait waitObject, Actions actionObject)
    {
        // Find the mobile menu link and click on it
        workingWithMobileMenuLink(driver);

        // Find the modal button and click on it
        WebElement modalButton = driver.findElement(By.cssSelector(".popular-plan-modal-trigger"));
        modalButton.click();

        // Wait for the modal box be visible
        waitObject.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".Modal.is-active")));

        // Find the modal close button, wait for the modal close button to visible and click on it using action class
        WebElement closeButton = waitObject.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.modal-scrollbar button[class ='Modal-close']")));
        actionObject.moveToElement(closeButton).build().perform();

        // Wait for the mobile menu to be visible, get it and click on it using action class
        WebElement mobileLink2 = waitObject.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.gnav20-primary-menu.gnav20-featured-card a[data-track=\"global nav:mobile\"]")));
        actionObject.moveToElement(mobileLink2).click().build().perform();

        System.out.println();
    }

    // This function is used to crawl through the available network plans.
    public static void scrapingMobilePlanDetails(WebDriver driver) throws IOException {
        // Scraping mobile plans from mobile plan page - first menu
        // Find the list of web elements of div tag
        List<WebElement> element1 =  driver.findElements(By.cssSelector(".teaser-item.teaser-item-stone-theme.teaser-item-carousel-stone.u-colorSecondary"));
        // Compute its size
        int numberOfDiv=element1.size();
        int j=0;

        // Writing mobile plan details to the csv file
        File planFile = new File("Verizon_MobilePlans.csv");

        // Create FileWriter object with file as parameter
        FileWriter outputfile1 = new FileWriter(planFile);

        // Create CSVWriter object filewriter object as parameter
        CSVWriter planWrite = new CSVWriter(outputfile1);

        // Adding header to csv
        String[] planHeader = { "Plan Name", "Plan Price", "Description" , "Network Speed" , "Data", "Connected device data plan", "International data, talk, & text abroad", "Device savings" , "Verizon Home Internet"};
        planWrite.writeNext(planHeader);

        System.out.println("Mobile plan details: ");
        // Iterate from 1st div to last div which is 3
        for (int i = 0; i < numberOfDiv; i++)
        {
            String[] data = new String[9];

            // List of plans at id 0,2,4
            List<WebElement> planNamePriceList =  driver.findElements(By.cssSelector(".u-displayBlock.u-width50 "));
            // List of plan prices from 0th index to 2nd index
            List<WebElement> planPriceList =  driver.findElements(By.cssSelector(".u-displayBlock.u-width50.u-textRight"));
            // List of description from 0th index to 2nd index
            List<WebElement> planDescriptionList =  driver.findElements(By.cssSelector(".teaser-subhead.u-colorSecondary.u-text--xs12.u-text--lg20.lineHeight--xs16.lineHeight--lg24.u-marginBottom--xs24.u-marginBottom--lg32 "));

            String mobilePlan = planNamePriceList.get(j).getAttribute("innerText");
            String planPrice = planPriceList.get(i).getAttribute("innerText");
            planPrice = planPrice.substring(0,3);
            String planDescription = planDescriptionList.get(i).getAttribute("innerText");

            System.out.println("Plan Name: "+ mobilePlan);
            System.out.println("Plan Price: "+ planPrice);
            System.out.println("Description: "+ planDescription);

            List<WebElement> allOtherFeaturesList =  driver.findElements(By.cssSelector(".u-marginLeft--xs4.u-marginLeft--lg4"));
            System.out.println("Other Features: ");

            int cnt=3;      // To store features from index 3 - 3rd column of file
            if(i == 0)
            {
                for(int k=19; k<25;k++)
                {
                    System.out.println(allOtherFeaturesList.get(k).getAttribute("innerText"));
                    data[cnt++] = allOtherFeaturesList.get(k).getAttribute("innerText");
                }
            }
            if(i == 1)
            {
                for(int k=25; k<29;k++)
                {
                    System.out.println(allOtherFeaturesList.get(k).getAttribute("innerText"));
                    data[cnt++] = allOtherFeaturesList.get(k).getAttribute("innerText");
                    if (k == 27)
                        cnt = cnt + 2;      // 8th column in the file
                }
            }
            if(i == 2)
            {
                //for(int k=29; k<31;k++) {
                    System.out.println(allOtherFeaturesList.get(29).getAttribute("innerText"));
                    data[3] = allOtherFeaturesList.get(29).getAttribute("innerText");   // 3rd column in the file
                    data[8] = allOtherFeaturesList.get(30).getAttribute("innerText");   // 8th column in the file
               // }
            }

            // Writing to file Verizon_MobilePlans.csv
            data[0] = mobilePlan;
            data[1] = planPrice;
            data[2] = planDescription;
            planWrite.writeNext(data);

            System.out.println();
            j=j+2;
        }

        // Writing 2 blank lines in file
        String newline[] = {};
        planWrite.writeNext(newline);
        planWrite.writeNext(newline);

        // AddOnList
        List<WebElement> planNameList =  driver.findElements(By.cssSelector(".display-name.u-marginBottom--12.u-fontDisplayLight.u-text--24.u-textLineHeightNormal"));
        List<WebElement> priceList =  driver.findElements(By.cssSelector(".u-fontDisplayLight.u-textLineHeightLoose"));
        List<WebElement> savingList =  driver.findElements(By.cssSelector(".u-text--md12.u-text--14.u-fontDisplayLight.u-textLineHeightNormal"));

        // Adding header for addon plan to csv
        String[] planHeaderNew = { "AddOn Plan Name", "AddOn Plan Price", "AddOn Saving"};
        planWrite.writeNext(planHeaderNew);
        System.out.println("AddOn Plan Name, AddOn Plan Price, AddOn Saving");

        for( int i=0; i<10; i++)
        {
            String planName = planNameList.get(i).getAttribute("innerText"); // size is 10
            assert planName != null;
            planName = planName.substring(1);
            String price = priceList.get(i).getAttribute("innerText"); // size is 10
            String saving = savingList.get(i).getAttribute("innerText"); // size is 10

            // Display add on plans
            System.out.print(planName + " , ");
            System.out.print(price + " , ");
            System.out.println(saving);

            // Writing to file
            String[] addOnDetails = new String[]{planName, price , saving};
            planWrite.writeNext(addOnDetails);
        }
        System.out.println();

        // close file Verizon_MobilePlans.csv
        planWrite.close();
    }

    // This function is used to crawl through the available iPhones and extract their name, price, and image link.
    public static void scrapingMobilePhones(WebDriver driver) throws IOException, InterruptedException {
        // Writing Mobile details to the csv file
        File file = new File("MobilePhones.csv");

        // Create FileWriter object with file as parameter
        FileWriter outputfile = new FileWriter(file);

        // Create CSVWriter object filewriter object as parameter
        CSVWriter writer = new CSVWriter(outputfile);

        // Adding header to csv
        String[] mobileHeader = { "Mobile Model", "Price", "Image" };
        writer.writeNext(mobileHeader);

        // Click on the deals menu
        workingWithDealsMenuLink(driver);

        // Click on the Apple button
        workingWithButton(driver);

        // Scraping elements from deals page - 4th menu
        // Get the list of iphone model name
        List<WebElement> mobileModelList = driver.findElements(By.cssSelector(".StyledTypography-VDS__sc-5k55co-0.isQWmL.StyledBody-VDS__sc-1s1yqd8-0.gjyxKS "));
        // Get the list of mobile price
        List<WebElement> mobilePriceList = driver.findElements(By.cssSelector(".StyledTypography-VDS__sc-5k55co-0.hRAXYu.StyledBody-VDS__sc-1s1yqd8-0.gjyxKS"));
        // Get the list of mobile images
        List<WebElement> mobileImages= driver.findElements(By.xpath("//*[@id=\"productDetails\"]/a/div/img"));

        int k=1; // used to get price at index 1, 3, 5, ..., 48
        System.out.println("Iphone models with price and image link:");
        // mobileModelList.size() = 24
        for (int i=0 ; i<mobileModelList.size() ; i++)
        {
            //Thread.sleep(1000);
            String mobileModel = mobileModelList.get(i).getAttribute("innerText"); // size is 24
            String mobilePrice = mobilePriceList.get(k).getAttribute("innerText"); // size is 48
            String mobileImage = mobileImages.get(i).getAttribute("data-src"); // size is 24

            // Display Mobile model, prize and images link
            System.out.print(mobileModelList.get(i).getAttribute("innerText") + " , ");
            System.out.print(mobilePrice + " , ");
            System.out.println(mobileImages.get(i).getAttribute("data-src"));

            // Writing to file
            String[] mobileDetails = new String[]{mobileModel, mobilePrice , mobileImage};
            writer.writeNext(mobileDetails);

            k=k+2;  // mobile price are at index 1, 3, 5, ..., 48
        }
        writer.close();
    }
}