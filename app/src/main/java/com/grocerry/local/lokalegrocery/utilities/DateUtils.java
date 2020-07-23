package com.grocerry.local.lokalegrocery.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Prabhakar.K on 7/21/2020.
 */

public class DateUtils {

    public String getDateTimeFormat(){
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssZ");
        return simpleDateFormat.format(now);
    }
}
