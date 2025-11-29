package crawler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import app.FlightSearchView;

public class FlightRouteGenerator {
    private static final Set<String> DEPARTURE_PLACES = new HashSet<>(Arrays.asList(
            "bjs", "sha", "tsn", "ckg", "sjw", "shp", "shf", "tyn", "dat", "cih", "het", "bav", "xil", "hlh", "hld",
            "wua", "cif", "tgo", "nzh", "hrb", "ndg", "mdg", "jmu", "hek", "cgq", "jil", "ynj", "she", "dlc", "ddg",
            "iob", "jng", "chg", "nkg", "lyg", "ntg", "czx", "xuz", "ynz", "wux", "foc", "xmn", "wus", "jjn", "kow",
            "hgh", "wnz", "ngb", "yiw", "hsn", "juz", "hyn", "khn", "jdz", "jiu", "jgs", "tna", "weh", "tao", "ynt",
            "jng", "wef", "doy", "lyi", "hfe", "txn", "fug", "aqg", "cgo", "nny", "lya", "ayn", "csx", "dyg", "cgd",
            "hny", "hjj", "llf", "dax", "wuh", "yih", "xfn", "shs", "enh", "can", "zuh", "szx", "swa", "mxz", "zha",
            "shg", "xin", "nng", "kwl", "lzh", "wuz", "bhy", "hak", "syx", "ctu", "lzo", "ybp", "mig", "jzh", "pzi",
            "dax", "wxn", "xic", "nao", "gys", "kwe", "zyi", "ava", "ten", "kmg", "ljg", "jhg", "dlu", "sym", "bsd",
            "lnj", "zat", "yua", "lum", "dig", "lxa", "bpx", "acx", "urc", "khg", "yin", "krl", "aku", "htn", "aat",
            "hmi", "kry", "fyn", "tcg", "kca", "iqm", "sia", "eny", "aka", "uyn", "hzg", "dnh", "jgn", "chw", "iqn",
            "lhw", "xnn", "goq", "inc", "hkg", "mfm"));

    private static final Set<String> ARRIVE_PLACES = new HashSet<>(Arrays.asList(
            "bjs", "sha", "tsn", "ckg", "sjw", "shp", "shf", "tyn", "dat", "cih", "het", "bav", "xil", "hlh", "hld",
            "wua", "cif", "tgo", "nzh", "hrb", "ndg", "mdg", "jmu", "hek", "cgq", "jil", "ynj", "she", "dlc", "ddg",
            "iob", "jng", "chg", "nkg", "lyg", "ntg", "czx", "xuz", "ynz", "wux", "foc", "xmn", "wus", "jjn", "kow",
            "hgh", "wnz", "ngb", "yiw", "hsn", "juz", "hyn", "khn", "jdz", "jiu", "jgs", "tna", "weh", "tao", "ynt",
            "jng", "wef", "doy", "lyi", "hfe", "txn", "fug", "aqg", "cgo", "nny", "lya", "ayn", "csx", "dyg", "cgd",
            "hny", "hjj", "llf", "dax", "wuh", "yih", "xfn", "shs", "enh", "can", "zuh", "szx", "swa", "mxz", "zha",
            "shg", "xin", "nng", "kwl", "lzh", "wuz", "bhy", "hak", "syx", "ctu", "lzo", "ybp", "mig", "jzh", "pzi",
            "dax", "wxn", "xic", "nao", "gys", "kwe", "zyi", "ava", "ten", "kmg", "ljg", "jhg", "dlu", "sym", "bsd",
            "lnj", "zat", "yua", "lum", "dig", "lxa", "bpx", "acx", "urc", "khg", "yin", "krl", "aku", "htn", "aat",
            "hmi", "kry", "fyn", "tcg", "kca", "iqm", "sia", "eny", "aka", "uyn", "hzg", "dnh", "jgn", "chw", "iqn",
            "lhw", "xnn", "goq", "inc", "hkg", "mfm"));

    private static final String BASE_URL = "https://flights.ctrip.com/online/list/oneway-%s-%s?depdate=%s&cabin=y_s_c_f&adult=1&child=0&infant=0";

    public static List<String> generateUrls(String origin, String destination, String date) {
        List<String> urls = new ArrayList<>();
        urls.add(String.format(BASE_URL, origin, destination, date));
        return urls;
    }

    public static List<String> generateUrls() {
        return new ArrayList<>();
    }
}
