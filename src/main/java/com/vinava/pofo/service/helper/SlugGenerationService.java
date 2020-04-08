package com.vinava.pofo.service.helper;

import com.vinava.pofo.util.EncoderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.vinava.pofo.util.DateUtil;

@Service
@Slf4j
public class SlugGenerationService {

    public String generateClientSlug(String clientName, int counter) {
        log.debug("Generating client slug started");
        StringBuilder sb = new StringBuilder();
        sb.append(getYearCode(DateUtil.getCurrentFinancialYear()));
        sb.append("-");
        sb.append(getClientCodeFromClientName(clientName));
        sb.append("-");
        sb.append(DateUtil.getCurrentMonth());
        sb.append(DateUtil.getCurrenDay());
        sb.append(counter);
        return EncoderUtil.getPofoEncodedString(sb.toString());
    }

    private static String getClientCodeFromClientName(String clientName) {
        int codeLength = 0;
        StringBuilder code = new StringBuilder();
        String[] splitNameArray = clientName.split("\\s+");
        while (codeLength < 3) {
            for (String name : splitNameArray) {
                code.append(Character.toUpperCase(name.charAt(0)));
                codeLength++;
                if (codeLength == 3) {
                    break;
                }
            }
        }
        return code.toString();
    }

    private String getYearCode(int year) {
        return Integer.toString(year).substring(2);
    }

}
