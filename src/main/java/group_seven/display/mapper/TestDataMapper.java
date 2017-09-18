package group_seven.display.mapper;

import java.util.List;

import group_seven.display.model.TestData;

public interface TestDataMapper {
	List<TestData> getTestDataList(String label);
}
