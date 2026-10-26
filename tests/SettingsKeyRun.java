package sa.urtv.home;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/** For tests/settings_key_test.py: SettingsKeyLogic.java on tests/settings_key_cases.tsv. One answer per case line.
 *  "decide" lines: decide, on(0/1), keyCode, action, repeat, downWasOurs(0/1), expected(PASS|OPEN|SWALLOW), note.
 *  "order" lines: order, comma-separated installed packages ("-" none), expected (comma-separated), note. */
public final class SettingsKeyRun {
    public static void main(String[] args) throws IOException {
        StringBuilder out = new StringBuilder();
        for (String line : Files.readAllLines(Paths.get(args[0]), StandardCharsets.UTF_8)) {
            if (line.isEmpty() || line.startsWith("#")) continue;
            String[] f = line.split("\t", -1);
            if (f[0].equals("decide")) {
                int r = SettingsKeyLogic.decide("1".equals(f[1]), Integer.parseInt(f[2]), Integer.parseInt(f[3]), Integer.parseInt(f[4]), "1".equals(f[5]));
                out.append(r == SettingsKeyLogic.OPEN ? "OPEN" : r == SettingsKeyLogic.SWALLOW ? "SWALLOW" : "PASS");
            } else {
                Set<String> have = new HashSet<>();
                if (!f[1].equals("-")) for (String p : f[1].split(",")) have.add(p);
                out.append(String.join(",", SettingsKeyLogic.candidates(have)));
            }
            out.append('\n');
        }
        System.out.write(out.toString().getBytes(StandardCharsets.UTF_8));
        System.out.flush();
    }
}
