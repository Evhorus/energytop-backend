package com.energytop.energytop_backend.common.helpers;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtils {
  public static String removeAccents(String input) {
    String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
    return normalized.replaceAll("\\p{M}", "");
  }
}