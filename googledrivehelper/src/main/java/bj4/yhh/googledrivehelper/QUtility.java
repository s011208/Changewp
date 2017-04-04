package bj4.yhh.googledrivehelper;

import java.util.List;

/**
 * Created by s011208 on 2017/4/4.
 */

public class QUtility {
    public static String generateQCondition(List<String> conditions) {
        int counter = 0;
        if (conditions.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        do {
            if (sb.length() != 0) sb.append(" and ");
            sb.append(conditions.get(counter++));
        } while (counter < conditions.size());
        return sb.toString();
    }
}
