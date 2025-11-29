package crawler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Maps city names to their airport codes for Ctrip URL generation
 * Also determines if a route is domestic or international
 */
public class CityCodeMapper {
    private static final Map<String, String> cityToCode = new HashMap<>();
    private static final Set<String> chineseCities = new HashSet<>();

    static {
        // ===== CHINESE DOMESTIC CITIES =====
        addChineseCity("beijing", "bjs");
        addChineseCity("shanghai", "sha");
        addChineseCity("tianjin", "tsn");
        addChineseCity("chongqing", "ckg");
        addChineseCity("shijiazhuang", "sjw");
        addChineseCity("shenyang", "she");
        addChineseCity("dalian", "dlc");
        addChineseCity("harbin", "hrb");
        addChineseCity("changchun", "cgq");
        addChineseCity("nanjing", "nkg");
        addChineseCity("hangzhou", "hgh");
        addChineseCity("ningbo", "ngb");
        addChineseCity("xiamen", "xmn");
        addChineseCity("fuzhou", "foc");
        addChineseCity("guangzhou", "can");
        addChineseCity("shenzhen", "szx");
        addChineseCity("chengdu", "ctu");
        addChineseCity("kunming", "kmg");
        addChineseCity("xian", "xiy");
        addChineseCity("wuhan", "wuh");
        addChineseCity("qingdao", "tao");
        addChineseCity("jinan", "tna");
        addChineseCity("zhengzhou", "cgo");
        addChineseCity("taiyuan", "tyn");
        addChineseCity("nanning", "nng");
        addChineseCity("guilin", "kwl");
        addChineseCity("haikou", "hak");
        addChineseCity("sanya", "syx");
        addChineseCity("urumqi", "urc");
        addChineseCity("lhasa", "lxa");
        addChineseCity("suzhou", "szv");
        addChineseCity("wuxi", "wux");
        addChineseCity("changsha", "csx");
        addChineseCity("nanchang", "khc");
        addChineseCity("hefei", "hfe");
        addChineseCity("lanzhou", "lhw");
        addChineseCity("yinchuan", "inc");
        addChineseCity("xining", "xnn");
        addChineseCity("hohhot", "het");

        // ===== INTERNATIONAL CITIES =====
        // Japan
        addInternationalCity("tokyo", "tyo");
        addInternationalCity("osaka", "osa");
        addInternationalCity("kyoto", "ukb");
        addInternationalCity("nagoya", "ngo");
        addInternationalCity("sapporo", "cts");
        addInternationalCity("fukuoka", "fuk");
        addInternationalCity("okinawa", "oka");

        // South Korea
        addInternationalCity("seoul", "sel");
        addInternationalCity("busan", "pus");
        addInternationalCity("jeju", "cju");
        addInternationalCity("incheon", "icn");

        // Southeast Asia
        addInternationalCity("bangkok", "bkk");
        addInternationalCity("singapore", "sin");
        addInternationalCity("kualalumpur", "kul");
        addInternationalCity("jakarta", "jkt");
        addInternationalCity("manila", "mnl");
        addInternationalCity("hanoi", "han");
        addInternationalCity("hochiminh", "sgn");
        addInternationalCity("phuket", "hkt");
        addInternationalCity("bali", "dps");

        // Taiwan & Hong Kong & Macau
        addInternationalCity("taipei", "tpe");
        addInternationalCity("hongkong", "hkg");
        addInternationalCity("macau", "mfm");
        addInternationalCity("taichung", "txg");
        addInternationalCity("kaohsiung", "khh");

        // Europe
        addInternationalCity("london", "lon");
        addInternationalCity("paris", "par");
        addInternationalCity("berlin", "ber");
        addInternationalCity("rome", "rom");
        addInternationalCity("madrid", "mad");
        addInternationalCity("amsterdam", "ams");
        addInternationalCity("brussels", "bru");
        addInternationalCity("zurich", "zrh");
        addInternationalCity("vienna", "vie");
        addInternationalCity("moscow", "mow");
        addInternationalCity("frankfurt", "fra");
        addInternationalCity("munich", "muc");

        // Americas
        addInternationalCity("newyork", "nyc");
        addInternationalCity("losangeles", "lax");
        addInternationalCity("sanfrancisco", "sfo");
        addInternationalCity("chicago", "chi");
        addInternationalCity("seattle", "sea");
        addInternationalCity("boston", "bos");
        addInternationalCity("washington", "was");
        addInternationalCity("toronto", "yto");
        addInternationalCity("vancouver", "yvr");
        addInternationalCity("mexico", "mex");

        // Oceania
        addInternationalCity("sydney", "syd");
        addInternationalCity("melbourne", "mel");
        addInternationalCity("brisbane", "bne");
        addInternationalCity("auckland", "akl");

        // Middle East
        addInternationalCity("dubai", "dxb");
        addInternationalCity("abudhabi", "auh");
        addInternationalCity("doha", "doh");
        addInternationalCity("telaviv", "tlv");
    }

    private static void addChineseCity(String name, String code) {
        cityToCode.put(name, code);
        chineseCities.add(name);
        chineseCities.add(code);
    }

    private static void addInternationalCity(String name, String code) {
        cityToCode.put(name, code);
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

    /**
     * Determine if a route is domestic or international
     * 
     * @param origin      - origin city name or code
     * @param destination - destination city name or code
     * @return "Domestic" if both cities are in China, "International" otherwise
     */
    public static String determineFlightType(String origin, String destination) {
        String originCode = getCityCode(origin);
        String destCode = getCityCode(destination);

        // Check if both are Chinese cities
        boolean originIsChinese = chineseCities.contains(origin.toLowerCase()) ||
                chineseCities.contains(originCode);
        boolean destIsChinese = chineseCities.contains(destination.toLowerCase()) ||
                chineseCities.contains(destCode);

        if (originIsChinese && destIsChinese) {
            return "Domestic";
        } else {
            return "International";
        }
    }
}
