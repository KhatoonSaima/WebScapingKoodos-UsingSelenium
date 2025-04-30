# WebScraping Koodos Using Selenium

This project automates the extraction of product and plan data from the [Koodo Mobile](https://www.koodomobile.com/) website using Selenium WebDriver with Java. It scrapes detailed information about mobile phones and mobile plans, and saves them into CSV files for analysis or downstream processing.

## 🔍 Features

- Scrapes data of Koodo's mobile phones and plans
- Saves structured data into CSV files
- Uses Selenium for browser automation
- Modular and easy-to-extend Java codebase

## 🛠 Tech Stack

- **Language:** Java
- **Automation Tool:** Selenium WebDriver
- **Build Tool:** Maven
- **Output Format:** CSV

## 📁 Project Structure

```
WebScapingKoodos-UsingSelenium/
│
├── src/
│   └── main/
│       └── java/
│           └── org/
│               └── example/
│                   └── Main.java
│
├── pom.xml
├── MobilePhones.csv
└── Verizon_MobilePlans.csv
```

## 🚀 How to Run

1. **Clone this repository**  
   ```bash
   git clone https://github.com/KhatoonSaima/WebScapingKoodos-UsingSelenium.git
   cd WebScapingKoodos-UsingSelenium
   ```

2. **Build the project**  
   Make sure Maven is installed. Then run:  
   ```bash
   mvn clean install
   ```

3. **Run the scraper**  
   ```bash
   mvn exec:java -Dexec.mainClass="org.example.Main"
   ```

4. **Check Output Files**
   - `MobilePhones.csv` — data about mobile devices
   - `Verizon_MobilePlans.csv` — data about available plans

## 🙌 Acknowledgements

- [Koodo Mobile](https://www.koodomobile.com/)
- Selenium WebDriver for enabling browser automation
