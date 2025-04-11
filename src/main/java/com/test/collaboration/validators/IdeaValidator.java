package com.test.collaboration.validators;

import com.test.collaboration.models.IdeaDTO;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Objects;

public class IdeaValidator {
    public static boolean isValid(IdeaDTO idea) {
        if (Objects.nonNull(idea)
                && !StringUtils.isEmpty(idea.getTitle())
                && !StringUtils.isEmpty(idea.getDescription())
                && !CollectionUtils.isEmpty(idea.getTagNames())) {
            return true;
        }
        return false;
    }
}
