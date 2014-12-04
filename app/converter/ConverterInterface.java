package converter;

import dto.technical.DTO;

/**
 * Created by florian on 4/12/14.
 */
public interface ConverterInterface<T, D extends DTO> {

    public D convert(T entity);
}
