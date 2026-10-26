package sa.urtv.screensaver;

/** For tests/prayer_method_test.py: PrayerMethod.java on a plain JVM. Prints PASS/FAIL lines. */
public final class PrayerMethodRun {
    static int failed;

    static void check(String name, boolean ok, String detail) {
        System.out.println((ok ? "PASS " : "FAIL ") + name + (ok ? "" : "  -> " + detail));
        if (!ok) failed++;
    }

    public static void main(String[] args) {
        check("parse: jafari and auto, in any case and with spaces", "jafari".equals(PrayerMethod.parse(" Jafari ")) && "auto".equals(PrayerMethod.parse("AUTO")), "");
        check("parse: anything else is null", PrayerMethod.parse("shia") == null && PrayerMethod.parse("") == null && PrayerMethod.parse(null) == null && PrayerMethod.parse("4") == null, "");
        check("fragment: nothing for auto (the page is then as it was), #pm=jafari for the Ja'fari",
                PrayerMethod.fragment("auto").isEmpty() && PrayerMethod.fragment(null).isEmpty() && "&pm=jafari".equals(PrayerMethod.fragment("jafari")), "");
        check("search: Saudi Arabia and the five other Gulf states, nothing else",
                PrayerMethod.searchable("SA") && PrayerMethod.searchable("bh") && PrayerMethod.searchable("KW") && PrayerMethod.searchable("QA")
                && PrayerMethod.searchable("AE") && PrayerMethod.searchable("OM") && !PrayerMethod.searchable("EG") && !PrayerMethod.searchable("") && !PrayerMethod.searchable(null), "");
        check("label: auto names the country's calculation by the zone; the Ja'fari names itself",
                PrayerMethod.label("auto", "Asia/Riyadh").equals("تلقائي · أم القرى") && PrayerMethod.label("auto", "Asia/Kuwait").equals("تلقائي · الكويت")
                && PrayerMethod.label("auto", "Asia/Qatar").equals("تلقائي · قطر") && PrayerMethod.label("auto", "Asia/Dubai").equals("تلقائي · الإمارات")
                && PrayerMethod.label("auto", "Asia/Bahrain").equals("تلقائي · الخليج") && PrayerMethod.label("jafari", "Asia/Kuwait").startsWith("جعفري"), "");
        // the Gulf cities of the list
        int gulf = 0;
        java.util.Set<String> names = new java.util.HashSet<>();
        for (Cities.Region r : Cities.REGIONS) {
            for (String[] c : r.cities) {
                check("city " + c[0] + ": a name nobody else has", names.add(c[0]), c[0]);
                boolean saudi = c.length == 3;
                if (!saudi) gulf++;
                double lat = Double.parseDouble(c[1]), lon = Double.parseDouble(c[2]);
                check("city " + c[0] + ": coordinates in the Gulf (16-32 N, 34-60 E)", lat > 16 && lat < 32 && lon > 34 && lon < 60, c[1] + "," + c[2]);
                if (!saudi) check("city " + c[0] + ": its zone is its country's (" + c[3] + ")", java.util.Arrays.asList("Asia/Bahrain", "Asia/Kuwait", "Asia/Qatar", "Asia/Dubai", "Asia/Muscat").contains(c[3])
                        && java.util.Arrays.asList(java.util.TimeZone.getAvailableIDs()).contains(c[3]), c[3]);
            }
        }
        check("the five Gulf states are in the list, with their big cities (at least 40)", gulf >= 40, String.valueOf(gulf));
        // the «الموقع» group: the country filters the city list, and is told by the city's zone
        check("country: Saudi Arabia's regions, or the Gulf state's one", Location.regions(0).length == 13 && Location.regions(1).length == 1
                && Location.regions(1)[0].name.equals("البحرين") && Location.regions(5)[0].name.equals("سلطنة عُمان"), String.valueOf(Location.regions(0).length));
        int listed = 0;
        for (int i = 0; i < Location.COUNTRIES.length; i++) listed += Location.regions(i).length;
        check("country: every region of the list is in one country", listed == Cities.REGIONS.length, listed + " of " + Cities.REGIONS.length);
        check("country: by the city's zone; a place of one's own elsewhere says so", Location.countryLabel("Asia/Riyadh").equals("السعودية")
                && Location.countryLabel("Asia/Bahrain").equals("البحرين") && Location.countryLabel("Africa/Cairo").equals("مكان من اختيارك"), "");
        check("country: the codes are the ones the search takes", java.util.Arrays.stream(Location.COUNTRIES).allMatch(c -> PrayerMethod.searchable(c[0]))
                && Location.COUNTRIES.length == PrayerMethod.SEARCH_COUNTRIES.length, "");
        // the first-run note: while the city was never set, the first three starts
        check("note: due on the first three starts while no city was set, never once one was",
                Location.noteDue(true, false, 0) && Location.noteDue(true, false, 2) && !Location.noteDue(true, false, 3) && !Location.noteDue(true, true, 0), "");
        check("note: never on a TV that had the app before and was updated, whatever its city", !Location.noteDue(false, false, 0) && !Location.noteDue(false, true, 0), "");
        check("note: its words", Location.note("الرياض").equals("المدينة: الرياض. لتغييرها: الإعدادات ← الموقع"), Location.note("الرياض"));
        System.exit(failed == 0 ? 0 : 1);
    }
}
