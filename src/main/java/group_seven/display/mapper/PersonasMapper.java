package group_seven.display.mapper;

import group_seven.display.model.Personas;

import java.util.List;

public interface PersonasMapper {
    List<Personas> getPersonasList(String purpose);
}
