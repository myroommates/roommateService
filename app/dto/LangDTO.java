package dto;

import dto.technical.DTO;

/**
 * Created by florian on 6/01/15.
 */
public class LangDTO extends DTO {

    private String language;

    private String code;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


    @Override
    public String toString() {
        return "LangDTO{" +
                "language='" + language + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
