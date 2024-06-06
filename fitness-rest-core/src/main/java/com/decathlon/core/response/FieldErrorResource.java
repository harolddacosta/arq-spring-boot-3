/* AssentSoftware (C)2023 */
package com.decathlon.core.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldErrorResource implements Serializable {

    private static final long serialVersionUID = 5089966234230930326L;

    private Long identifier;
    private String code;
    private String field;
    private String title;
    private String detail;
    private String translationKey;
    private String[] translationKeyParameters;
    private String resource;

    public FieldErrorResource(String title, String detail, String translationKey) {
        super();

        this.title = title;
        this.detail = detail;
        this.translationKey = translationKey;
    }

    public FieldErrorResource(
            String title, String detail, String translationKey, String[] translationKeyParameters) {
        super();

        this.title = title;
        this.detail = detail;
        this.translationKey = translationKey;
        this.translationKeyParameters = translationKeyParameters;
    }

    public FieldErrorResource(String title, String detail, String field, String translationKey) {
        super();

        this.title = title;
        this.detail = detail;
        this.field = field;
        this.translationKey = translationKey;
    }

    public FieldErrorResource(
            String title,
            String detail,
            String field,
            String translationKey,
            String[] translationKeyParameters) {
        super();

        this.title = title;
        this.detail = detail;
        this.field = field;
        this.translationKey = translationKey;
        this.translationKeyParameters = translationKeyParameters;
    }
}
