package crawler;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps city names to their airport codes for Ctrip URL generation
 */
public class CityCodeMapper {
    private static final Map<String, String> cityToCode = new HashMap<>();

    static {
        // Major Chinese cities
        cityToCode.put("beijing", "bjs");
        cityToCode.put("shanghai", "sha");
        cityToCode.put("tianjin", "tsn");
        cityToCode.put("chongqing", "ckg");
        cityToCode.put("shijiazhuang", "sjw");
        cityToCode.put("shenyang", "she");
        cityToCode.put("dalian", "dlc");
        cityToCode.put("harbin", "hrb");
        cityToCode.put("changchun", "cgq");
        cityToCode.put("nanjing", "nkg");
        cityToCode.put("hangzhou", "hgh");
        cityToCode.put("ningbo", "ngb");
        cityToCode.put("xiamen", "xmn");
        cityToCode.put("fuzhou", "foc");
        cityToCode.put("guangzhou", "can");
        cityToCode.put("shenzhen", "szx");
        cityToCode.put("chengdu", "ctu");
        cityToCode.put("chongqing", "ckg");
        cityToCode.put("kunming", "kmg");
        cityToCode.put("xian", "xiy");
        cityToCode.put("wuhan", "wuh");
        cityToCode.put("qingdao", "tao");
        cityToCode.put("jinan", "tna");
        cityToCode.put("zhengzhou", "cgo");
        cityToCode.put("taiyuan", "tyn");
        cityToCode.put("nanning", "nng");
        cityToCode.put("guilin", "kwl");
        cityToCode.put("haikou", "hak");
        cityToCode.put("sanya", "syx");
        cityToCode.put("urumqi", "urc");
        cityToCode.put("lhasa", "lxa");
        cityToCode.put("hongkong", "hkg");
        cityToCode.put("macau", "mfm");

        // International cities (examples)
        cityToCode.put("tokyo", "tyo");
        cityToCode.put("osaka", "osa");
        cityToCode.put("seoul", "sel");
        cityToCode.put("bangkok", "bkk");
        cityToCode.put("singapore", "sin");
        cityToCode.put("kualalumpur", "kul");
        cityToCode.put("taipei", "tpe");
        cityToCode.put("hongkong", "hkg");
        cityToCode.put("london", "lon");
        cityToCode.put("paris", "par");
        cityToCode.put("newyork", "nyc");
        cityToCode.put("losangeles", "lax");
        cityToCode.put("sanfrancisco", "sfo");
        cityToCode.put("sydney", "syd");
        cityToCode.put("melbourne", "mel");

        // Add more mappings as needed
    }

    /**
     * Convert a city name (or partial name) to its airport code
     * 
     * @param cityName - the city name input by user (can be partial)
     * @return the 3-letter airport code, or the original input if not found
     */
    public static String getCityCode(String cityName) {
        if (cityName == null || cityName.trim().isEmpty()) {
            return "";
        }

        String normalized = cityName.toLowerCase().trim().replaceAll("\\s+", "");

        // Exact match
        if (cityToCode.containsKey(normalized)) {
            return cityToCode.get(normalized);
        }

        // Partial match - find the first city that starts with the input
        for (Map.Entry<String, String> entry : cityToCode.entrySet()) {
            if (entry.getKey().startsWith(normalized)) {
                return entry.getValue();
            }
        }

        // If already a 3-letter code, return as is
        if (normalized.length() == 3 && normalized.matches("[a-z]{3}")) {
            return normalized;
        }

        // Return original if no match found
        return normalized;
    }

    /**
     * Get the full city name from a code
     */
    public static String getCityName(String code) {
        if (code == null || code.trim().isEmpty()) {
            return "";
        }

        String normalized = code.toLowerCase().trim();
        for (Map.Entry<String, String> entry : cityToCode.entrySet()) {
            if (entry.getValue().equals(normalized)) {
                return entry.getKey();
            }
        }
        return code;
    }
}
