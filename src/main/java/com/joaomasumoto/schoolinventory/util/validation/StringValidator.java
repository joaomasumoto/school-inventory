package com.joaomasumoto.schoolinventory.util.validation;

public final class StringValidator {

    private StringValidator() {

    }

    public static String validateName(String value, String fieldName, int maxLength) {


        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be null or blank.");
        }

        String normalized = value.strip();

        if (normalized.length() > maxLength) {
            throw new IllegalArgumentException(fieldName + " must not exceed " + maxLength + " characters.");
        }

        return normalized;
    }

}
