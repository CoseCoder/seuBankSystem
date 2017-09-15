package group_seven.display.mapper;

import group_seven.display.model.CreditOne;

import java.util.List;

public interface CreditOneMapper {
    List<CreditOne> getCreditOneList(String account);
}
