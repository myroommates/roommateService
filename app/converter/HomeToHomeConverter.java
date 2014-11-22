package converter;

import dto.HomeDTO;
import entities.Home;

/**
 * Created by florian on 11/11/14.
 */
public class HomeToHomeConverter {

    public HomeDTO converter(Home home) {
        HomeDTO dto = new HomeDTO();

        dto.setId(home.getId());
        dto.setName(home.getName());

        return dto;
    }
}
