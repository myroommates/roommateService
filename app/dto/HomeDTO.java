package dto;

import dto.technical.DTO;

/**
 * Created by florian on 11/11/14.
 */
public class HomeDTO extends DTO {

    private Long id;

    private String moneySymbol;

    public HomeDTO() {
    }

    public String getMoneySymbol() {
        return moneySymbol;
    }

    public void setMoneySymbol(String moneySymbol) {
        this.moneySymbol = moneySymbol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
