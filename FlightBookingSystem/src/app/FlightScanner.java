package app;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class FlightScanner {

    public static void main(String[] args) {
        // 1. 准备查询数据
        String origin = "Tokyo"; // 建议改为机场代码 "HND" 或 "NRT"
        String dest = "Osaka"; // 建议改为 "ITM" 或 "KIX"
        String date = "2025-11-21";

        // 2. 生成任务列表 (整合你的 URLGenerator 逻辑)
        List<AirlineTask> tasks = new ArrayList<>();

        // 注意：这里需要你根据实际网站情况，填入正确的 CSS 选择器
        // ANA 示例 (假设 URL 是搜索结果页):
        tasks.add(new AirlineTask("ANA",
                // 这里为了演示，使用了你之前的 generator 逻辑（需稍作修改以返回纯 URL）
                "https://aswbe-d.ana.co.jp/...", // 填入你的实际搜索结果 URL 或生成逻辑
                "em.pliceSmallEn" // ANA 的价格 CSS 选择器
        ));

        // JAL 示例 (假设):
        tasks.add(new AirlineTask("JAL",
                "https://www.jal.co.jp/...",
                ".current-price" // 假设 JAL 的价格 class 是这个
        ));

        // 3. 启动 Selenium 爬虫
        runScraperAndOpenBest(tasks);
    }

    public static void runScraperAndOpenBest(List<AirlineTask> tasks) {
        // 自动设置 ChromeDriver
        WebDriverManager.chromedriver().setup();

        // 设置 Chrome 选项
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless"); // 如果不想看到浏览器弹出，取消注释这一行
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        // 伪装 User-Agent 防止被反爬
        options.addArguments(
                "user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36");

        WebDriver driver = new ChromeDriver(options);

        double minPrice = Double.MAX_VALUE;
        String bestUrl = "";
        String bestAirline = "";

        System.out.println("=== 开始爬取机票价格 ===");

        try {
            for (AirlineTask task : tasks) {
                System.out.print("正在查询: " + task.name + "... ");

                try {
                    // 打开网页
                    driver.get(task.url);

                    // 智能等待：最多等 15 秒，直到价格元素出现
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
                    WebElement priceElement = wait
                            .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(task.priceSelector)));

                    // 提取价格文本 (例如 "JPY17,690")
                    String rawPrice = priceElement.getText();

                    // 解析价格 (去掉非数字字符)
                    double price = parsePrice(rawPrice);
                    System.out.println("价格: " + price);

                    // 比价逻辑
                    if (price > 0 && price < minPrice) {
                        minPrice = price;
                        bestUrl = task.url;
                        bestAirline = task.name;
                    }

                } catch (Exception e) {
                    System.out.println("查询失败 (超时或反爬): " + e.getMessage().split("\n")[0]);
                }
            }
        } finally {
            // 爬取结束，关闭 Selenium 浏览器
            driver.quit();
        }

        // 4. 结果处理：打开系统默认浏览器
        if (minPrice != Double.MAX_VALUE) {
            System.out.println("\n═══════════════════════════════════════");
            System.out.println(" 推荐结果: " + bestAirline);
            System.out.println(" 最低价格: " + minPrice);
            System.out.println(" 正在为您打开订票页面...");
            System.out.println("═══════════════════════════════════════");

            openInDefaultBrowser(bestUrl);
        } else {
            System.out.println("未找到有效价格信息。");
        }
    }

    // 辅助方法：将 "JPY17,690" 转换为 17690.0
    private static double parsePrice(String raw) {
        try {
            // 替换掉所有非数字和小数点
            String clean = raw.replaceAll("[^0-9.]", "");
            return Double.parseDouble(clean);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // 辅助方法：调用系统默认浏览器打开链接
    private static void openInDefaultBrowser(String url) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("无法自动打开浏览器，请手动复制链接: " + url);
        }
    }
}