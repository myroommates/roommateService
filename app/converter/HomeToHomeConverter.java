package converter;

import dto.HomeDTO;
import model.entities.Home;

/**
 * Created by florian on 11/11/14.
 */
public class HomeToHomeConverter implements ConverterInterface<Home,HomeDTO>{

    public HomeDTO convert(Home home) {
        HomeDTO dto = new HomeDTO();

        dto.setId(home.getId());

        return dto;
    }
}
