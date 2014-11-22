package dto;

import dto.technical.DTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florian on 11/11/14.
 */
public class ListDTO<T extends DTO> extends DTO {

    private List<T> list = new ArrayList<>();

    public ListDTO() {
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void addElement(T dto) {
        list.add(dto);
    }

    @Override
    public String toString() {
        return "ListDTO{" +
                "list=" + list +
                '}';
    }
}
