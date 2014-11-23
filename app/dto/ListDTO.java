package dto;

import dto.technical.DTO;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by florian on 11/11/14.
 */
public class ListDTO<T extends DTO> extends DTO {

    private Set<T> list = new HashSet<>();

    public ListDTO() {
    }

    public Set<T> getList() {
        return list;
    }

    public void setList(Set<T> list) {
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
