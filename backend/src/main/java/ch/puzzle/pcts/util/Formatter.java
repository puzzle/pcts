package ch.puzzle.pcts.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Formatter {

    public static DecimalFormat createFormatting() {
        DecimalFormat df = new DecimalFormat("#.00");
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df;
    }
}
