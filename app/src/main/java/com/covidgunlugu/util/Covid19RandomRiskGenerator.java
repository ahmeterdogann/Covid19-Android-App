package com.covidgunlugu.util;

import java.util.Random;

public class Covid19RandomRiskGenerator {
    public static Covid19Risk getRisk(Random random) {
        int i = random.nextInt(4) + 1;

        Covid19Risk covid19Risk = null;

        if (i == 1)
            covid19Risk = Covid19Risk.DUSUK;

        else if (i == 2)
            covid19Risk = Covid19Risk.ORTA;

        else if (i == 3)
            covid19Risk = Covid19Risk.YUKSEK;

        else
            covid19Risk = Covid19Risk.COK_YUKSEK;

        return covid19Risk;

    }

    public static Covid19Risk getRisk() {
        return getRisk(new Random());
    }
}
