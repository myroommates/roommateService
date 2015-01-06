package converter;

import dto.LangDTO;
import play.i18n.Lang;

/**
 * Created by florian on 6/01/15.
 */
public class LanguageToLanguageDTOConverter implements ConverterInterface<Lang, LangDTO>{
    @Override
    public LangDTO convert(Lang entity) {
        LangDTO dto = new LangDTO();

        dto.setCode(entity.code());
        dto.setLanguage(entity.language());

        return dto;
    }
}
